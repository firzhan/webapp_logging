package org.wso2.carbon.bpmn.rest.api;


import org.activiti.engine.*;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.wso2.carbon.bpmn.rest.api.engine.variable.RestVariable;
import org.wso2.carbon.bpmn.rest.api.internal.common.RestResponseFactory;
import org.wso2.carbon.bpmn.rest.api.internal.exception.BPMNOSGIServiceException;
import org.wso2.carbon.bpmn.rest.api.runtime.ProcessInstanceCreateRequest;
import org.wso2.carbon.bpmn.rest.api.runtime.ProcessInstanceResponse;
import org.wso2.carbon.bpmn.rest.api.utils.BPMNOSGIService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/process-instances")
public class ProcessInstanceService {

    @Context
    UriInfo uriInfo;

    @POST
    @Path("/")
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public Response startInstance(ProcessInstanceCreateRequest processInstanceCreateRequest){
        System.out.println("JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ");
        System.out.println("ProcessInstanceCreateRequest:" + processInstanceCreateRequest.getProcessDefinitionId());
        System.out.println(" processInstanceCreateRequest.getVariables().size():" +  processInstanceCreateRequest.getVariables().size());
       ;

        if (processInstanceCreateRequest.getProcessDefinitionId() == null && processInstanceCreateRequest.getProcessDefinitionKey() == null
                && processInstanceCreateRequest.getMessage() == null) {
            throw new ActivitiIllegalArgumentException("Either processDefinitionId, processDefinitionKey or message is required.");
        }

        int paramsSet = ((processInstanceCreateRequest.getProcessDefinitionId() != null) ? 1 : 0)
                + ((processInstanceCreateRequest.getProcessDefinitionKey() != null) ? 1 : 0)
                + ((processInstanceCreateRequest.getMessage() != null) ? 1 : 0);

        if (paramsSet > 1) {
            throw new ActivitiIllegalArgumentException("Only one of processDefinitionId, processDefinitionKey or message should be set.");
        }

        if (processInstanceCreateRequest.isCustomTenantSet()) {
            // Tenant-id can only be used with either key or message
            if(processInstanceCreateRequest.getProcessDefinitionId() != null) {
                throw new ActivitiIllegalArgumentException("TenantId can only be used with either processDefinitionKey or message.");
            }
        }

        RestResponseFactory restResponseFactory = new RestResponseFactory();

        Map<String, Object> startVariables = null;
        if (processInstanceCreateRequest.getVariables() != null) {
            startVariables = new HashMap<>();
            for (RestVariable variable : processInstanceCreateRequest.getVariables()) {
                if (variable.getName() == null) {
                    throw new ActivitiIllegalArgumentException("Variable name is required.");
                }
                startVariables.put(variable.getName(), restResponseFactory.getVariableValue(variable));
            }
        }

       RuntimeService runtimeService = BPMNOSGIService.getRumtimeService();
        if(runtimeService == null){
            throw new BPMNOSGIServiceException("RuntimeService couldn't be identified");
        }

        ProcessInstanceResponse processInstanceResponse = null;
        // Actually start the instance based on key or id
        try {
            ProcessInstance instance = null;
            if (processInstanceCreateRequest.getProcessDefinitionId() != null) {
                instance = runtimeService.startProcessInstanceById(
                        processInstanceCreateRequest.getProcessDefinitionId(), processInstanceCreateRequest.getBusinessKey(), startVariables);
            } else if (processInstanceCreateRequest.getProcessDefinitionKey() != null) {
                if (processInstanceCreateRequest.isCustomTenantSet()) {
                    instance = runtimeService.startProcessInstanceByKeyAndTenantId(
                            processInstanceCreateRequest.getProcessDefinitionKey(), processInstanceCreateRequest.getBusinessKey(), startVariables,
                            processInstanceCreateRequest.getTenantId());
                } else {
                    instance = runtimeService.startProcessInstanceByKey(
                            processInstanceCreateRequest.getProcessDefinitionKey(), processInstanceCreateRequest.getBusinessKey(), startVariables);
                }
            } else {
                if (processInstanceCreateRequest.isCustomTenantSet()) {
                    instance = runtimeService.startProcessInstanceByMessageAndTenantId(
                            processInstanceCreateRequest.getMessage(), processInstanceCreateRequest.getBusinessKey(), startVariables, processInstanceCreateRequest.getTenantId());
                } else {
                    instance = runtimeService.startProcessInstanceByMessage(
                            processInstanceCreateRequest.getMessage(), processInstanceCreateRequest.getBusinessKey(), startVariables);
                }
            }

            //response.setStatus(HttpStatus.CREATED.value());

            HistoryService historyService = BPMNOSGIService.getHistoryService();
            if(historyService == null){
                throw new BPMNOSGIServiceException("RuntimeService couldn't be identified");
            }
            //Added by Ryan Johnston
            if (processInstanceCreateRequest.getReturnVariables()) {
                Map<String, Object> runtimeVariableMap = null;
                List<HistoricVariableInstance> historicVariableList = null;
                if (instance.isEnded()) {
                    historicVariableList = historyService.createHistoricVariableInstanceQuery()
                            .processInstanceId(instance.getId())
                            .list();
                } else {
                    runtimeVariableMap = runtimeService.getVariables(instance.getId());
                }
                processInstanceResponse = restResponseFactory.createProcessInstanceResponse(instance, true,
                        runtimeVariableMap, historicVariableList, uriInfo.getBaseUri().toString());

            } else {
                processInstanceResponse = restResponseFactory.createProcessInstanceResponse(instance,uriInfo.getBaseUri
                        ().toString() );
            }
            //End Added by Ryan Johnston

            //Removed by Ryan Johnston (obsolete given the above).
            //return factory.createProcessInstanceResponse(this, instance);
        } catch(ActivitiObjectNotFoundException aonfe) {
            throw new ActivitiIllegalArgumentException(aonfe.getMessage(), aonfe);
        }

       return Response.ok().status(Response.Status.CREATED).entity(processInstanceResponse).build();
    }
}
