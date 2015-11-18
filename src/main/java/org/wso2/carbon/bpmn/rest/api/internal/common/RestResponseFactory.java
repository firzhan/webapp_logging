package org.wso2.carbon.bpmn.rest.api.internal.common;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.bpmn.deployer.BpmnDeployer;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.wso2.carbon.bpmn.rest.api.engine.variable.RestVariable;
import org.wso2.carbon.bpmn.rest.api.repository.*;
import org.wso2.carbon.bpmn.rest.api.runtime.ProcessInstanceResponse;
import org.wso2.carbon.bpmn.rest.api.runtime.variable.*;
import org.wso2.carbon.bpmn.rest.api.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RestResponseFactory {

    public static final int VARIABLE_TASK = 1;
    public static final int VARIABLE_EXECUTION = 2;
    public static final int VARIABLE_PROCESS = 3;
    public static final int VARIABLE_HISTORY_TASK = 4;
    public static final int VARIABLE_HISTORY_PROCESS = 5;
    public static final int VARIABLE_HISTORY_VARINSTANCE = 6;
    public static final int VARIABLE_HISTORY_DETAIL = 7;

    public static final String BYTE_ARRAY_VARIABLE_TYPE = "binary";
    public static final String SERIALIZABLE_VARIABLE_TYPE = "serializable";

    protected List<RestVariableConverter> variableConverters = new ArrayList<RestVariableConverter>();

    public RestResponseFactory(){
        initializeVariableConverters();
    }

    public List<DeploymentResponse> createDeploymentResponseList(List<Deployment> deployments, String baseUri) {
        List<DeploymentResponse> responseList = new ArrayList<>();
        for (Deployment instance : deployments) {
            responseList.add(createDeploymentResponse(instance, baseUri));
        }
        return responseList;
    }

    protected RestUrlBuilder createUrlBuilder(String baseUri) {
        return RestUrlBuilder.fromCurrentRequest(baseUri);
    }

    public DeploymentResponse createDeploymentResponse(Deployment deployment,  String baseUri) {
        RestUrlBuilder urlBuilder = createUrlBuilder(baseUri);
        return new DeploymentResponse(deployment, urlBuilder.buildUrl(RestUrls.URL_DEPLOYMENT, deployment.getId()));
    }

    public DeploymentResourceResponseCollection createDeploymentResourceResponseList(String deploymentId,
                                                                                     List<String> resourceList, String baseUri
                                                                                     ) {
        RestUrlBuilder urlBuilder = createUrlBuilder(baseUri);
        // Add additional metadata to the artifact-strings before returning
        List<DeploymentResourceResponse> responseList = new ArrayList<>();
        for (String resourceId : resourceList) {
            responseList.add(createDeploymentResourceResponse(deploymentId, resourceId, Utils.resolveContentType
                    (resourceId), baseUri));
        }
        DeploymentResourceResponseCollection deploymentResourceResponseCollection = new
                DeploymentResourceResponseCollection();
        deploymentResourceResponseCollection.setDeploymentResourceResponseList(responseList);
        return deploymentResourceResponseCollection;
    }

    public DeploymentResourceResponse createDeploymentResourceResponse(String deploymentId, String resourceId, String contentType, String baseUri) {
        RestUrlBuilder urlBuilder = createUrlBuilder(baseUri);
        // Create URL's
        String resourceUrl = urlBuilder.buildUrl(RestUrls.URL_DEPLOYMENT_RESOURCE, deploymentId, resourceId);
        String resourceContentUrl = urlBuilder.buildUrl(RestUrls.URL_DEPLOYMENT_RESOURCE_CONTENT, deploymentId, resourceId);

        // Determine type
        String type = "resource";
        for (String suffix : BpmnDeployer.BPMN_RESOURCE_SUFFIXES) {
            if (resourceId.endsWith(suffix)) {
                type = "processDefinition";
                break;
            }
        }
        return new DeploymentResourceResponse(resourceId, resourceUrl, resourceContentUrl, contentType, type);
    }

    public List<ModelResponse> createModelResponseList(List<Model> models, String baseUri) {
       // RestUrlBuilder urlBuilder = createUrlBuilder(baseUri);
        List<ModelResponse> responseList = new ArrayList<>();
        for (Model instance : models) {
            responseList.add(createModelResponse(instance, baseUri));
        }
        return responseList;
    }

    public List<ProcessDefinitionResponse> createProcessDefinitionResponseList(List<ProcessDefinition>
                                                                                       processDefinitions, String
            baseUri) {
        List<ProcessDefinitionResponse> responseList = new ArrayList<>();
        for (ProcessDefinition instance : processDefinitions) {
            responseList.add(createProcessDefinitionResponse(instance, baseUri));
        }
        return responseList;
    }


    public ModelResponse createModelResponse(Model model, String baseUri) {
        RestUrlBuilder urlBuilder = createUrlBuilder(baseUri);
        ModelResponse modelResponse = new ModelResponse();

        modelResponse.setCategory(model.getCategory());
        modelResponse.setCreateTime(model.getCreateTime());
        modelResponse.setId(model.getId());
        modelResponse.setKey(model.getKey());
        modelResponse.setLastUpdateTime(model.getLastUpdateTime());
        modelResponse.setMetaInfo(model.getMetaInfo());
        modelResponse.setName(model.getName());
        modelResponse.setDeploymentId(model.getDeploymentId());
        modelResponse.setVersion(model.getVersion());
        modelResponse.setTenantId(model.getTenantId());

        modelResponse.setUrl(urlBuilder.buildUrl(RestUrls.URL_MODEL, model.getId()));
        if(model.getDeploymentId() != null) {
            modelResponse.setDeploymentUrl(urlBuilder.buildUrl(RestUrls.URL_DEPLOYMENT, model.getDeploymentId()));
        }

        if(model.hasEditorSource()) {
            modelResponse.setSourceUrl(urlBuilder.buildUrl(RestUrls.URL_MODEL_SOURCE, model.getId()));
        }

        if(model.hasEditorSourceExtra()) {
            modelResponse.setSourceExtraUrl(urlBuilder.buildUrl(RestUrls.URL_MODEL_SOURCE_EXTRA, model.getId()));
        }

        return modelResponse;
    }

    public ProcessDefinitionResponse createProcessDefinitionResponse(ProcessDefinition processDefinition, String
            baseUri) {
        ProcessDefinitionResponse response = new ProcessDefinitionResponse();
        RestUrlBuilder urlBuilder = createUrlBuilder(baseUri);

        response.setUrl(urlBuilder.buildUrl(RestUrls.URL_PROCESS_DEFINITION, processDefinition.getId()));
        response.setId(processDefinition.getId());
        response.setKey(processDefinition.getKey());
        response.setVersion(processDefinition.getVersion());
        response.setCategory(processDefinition.getCategory());
        response.setName(processDefinition.getName());
        response.setDescription(processDefinition.getDescription());
        response.setSuspended(processDefinition.isSuspended());
        response.setStartFormDefined(processDefinition.hasStartFormKey());
        response.setGraphicalNotationDefined(processDefinition.hasGraphicalNotation());
        response.setTenantId(processDefinition.getTenantId());

        // Links to other resources
        response.setDeploymentId(processDefinition.getDeploymentId());
        response.setDeploymentUrl(urlBuilder.buildUrl(RestUrls.URL_DEPLOYMENT, processDefinition.getDeploymentId()));
        response.setResource(urlBuilder.buildUrl(RestUrls.URL_DEPLOYMENT_RESOURCE, processDefinition.getDeploymentId(), processDefinition.getResourceName()));
        if(processDefinition.getDiagramResourceName() != null) {
            response.setDiagramResource(urlBuilder.buildUrl(RestUrls.URL_DEPLOYMENT_RESOURCE,
                    processDefinition.getDeploymentId(), processDefinition.getDiagramResourceName()));
        }
        return response;
    }

    public List<RestIdentityLink> createRestIdentityLinks(List<IdentityLink> links, String baseUri) {
        List<RestIdentityLink> responseList = new ArrayList<>();
        for (IdentityLink instance : links) {
            responseList.add(createRestIdentityLink(instance, baseUri));
        }
        return responseList;
    }

    public RestIdentityLink createRestIdentityLink(IdentityLink link, String baseUri) {
        RestUrlBuilder urlBuilder = createUrlBuilder(baseUri);
        return createRestIdentityLink(link.getType(), link.getUserId(), link.getGroupId(), link.getTaskId(),
                link.getProcessDefinitionId(), link.getProcessInstanceId(), urlBuilder);
    }

    public RestIdentityLink createRestIdentityLink(String type, String userId, String groupId, String taskId, String processDefinitionId, String processInstanceId, RestUrlBuilder urlBuilder) {
        RestIdentityLink result = new RestIdentityLink();
        result.setUser(userId);
        result.setGroup(groupId);
        result.setType(type);

        String family = null;
        if (userId != null) {
            family = RestUrls.SEGMENT_IDENTITYLINKS_FAMILY_USERS;
        } else {
            family = RestUrls.SEGMENT_IDENTITYLINKS_FAMILY_GROUPS;
        }
        if (processDefinitionId != null) {
            result.setUrl(urlBuilder.buildUrl(RestUrls.URL_PROCESS_DEFINITION_IDENTITYLINK, processDefinitionId, family, (userId != null ? userId : groupId)));
        } else if(taskId != null){
            result.setUrl(urlBuilder.buildUrl(RestUrls.URL_TASK_IDENTITYLINK, taskId, family, (userId != null ? userId : groupId), type));
        } else {
            result.setUrl(urlBuilder.buildUrl(RestUrls.URL_PROCESS_INSTANCE_IDENTITYLINK, processInstanceId, (userId != null ? userId : groupId), type));
        }
        return result;
    }

    public Object getVariableValue(RestVariable restVariable) {
        Object value = null;

        if(restVariable.getType() != null) {
            // Try locating a converter if the type has been specified
            RestVariableConverter converter = null;
            for(RestVariableConverter conv : variableConverters) {
                if(conv.getRestTypeName().equals(restVariable.getType())) {
                    converter = conv;
                    break;
                }
            }
            if(converter == null) {
                throw new ActivitiIllegalArgumentException("Variable '" + restVariable.getName() + "' has unsupported type: '" + restVariable.getType() + "'.");
            }
            value = converter.getVariableValue(restVariable);

        } else {
            // Revert to type determined by REST-to-Java mapping when no explicit type has been provided
            value = restVariable.getValue();
        }
        return value;
    }

    public List<ProcessInstanceResponse> createProcessInstanceResponseList(List<ProcessInstance> processInstances,
                                                                           String baseUri) {
        List<ProcessInstanceResponse> responseList = new ArrayList<>();
        for (ProcessInstance instance : processInstances) {
            responseList.add(createProcessInstanceResponse(instance, baseUri));
        }
        return responseList;
    }

/*    public ProcessInstanceResponse createProcessInstanceResponse(ProcessInstance processInstance, String baseUri) {
        return createProcessInstanceResponse(processInstance,baseUri);
    }*/

    public ProcessInstanceResponse createProcessInstanceResponse(ProcessInstance processInstance, String baseUri) {
        RestUrlBuilder urlBuilder = createUrlBuilder(baseUri);
        ProcessInstanceResponse result = new ProcessInstanceResponse();
        result.setActivityId(processInstance.getActivityId());
        result.setBusinessKey(processInstance.getBusinessKey());
        result.setId(processInstance.getId());
        result.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        result.setProcessDefinitionUrl(urlBuilder.buildUrl(RestUrls.URL_PROCESS_DEFINITION, processInstance.getProcessDefinitionId()));
        result.setEnded(processInstance.isEnded());
        result.setSuspended(processInstance.isSuspended());
        result.setUrl(urlBuilder.buildUrl(RestUrls.URL_PROCESS_INSTANCE, processInstance.getId()));
        result.setTenantId(processInstance.getTenantId());

        if (processInstance.isEnded()) {
            //Process complete. Note the same in the result.
            result.setCompleted(true);
        } else {
            //Process not complete. Note the same in the result.
            result.setCompleted(false);
        }

        if (processInstance.getProcessVariables() != null) {
            Map<String, Object> variableMap = processInstance.getProcessVariables();
            for (String name : variableMap.keySet()) {
                result.addVariable(createRestVariable(name, variableMap.get(name),
                        RestVariable.RestVariableScope.LOCAL, processInstance.getId(), VARIABLE_PROCESS, false, urlBuilder));
            }
        }

        return result;
    }

    public ProcessInstanceResponse createProcessInstanceResponse(ProcessInstance processInstance, boolean returnVariables,
                                                                 Map<String, Object> runtimeVariableMap,
                                                                 List<HistoricVariableInstance> historicVariableList,
                                                                 String baseUri) {

        RestUrlBuilder urlBuilder = createUrlBuilder(baseUri);
        ProcessInstanceResponse result = new ProcessInstanceResponse();
        result.setActivityId(processInstance.getActivityId());
        result.setBusinessKey(processInstance.getBusinessKey());
        result.setId(processInstance.getId());
        result.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        result.setProcessDefinitionUrl(urlBuilder.buildUrl(RestUrls.URL_PROCESS_DEFINITION, processInstance.getProcessDefinitionId()));
        result.setEnded(processInstance.isEnded());
        result.setSuspended(processInstance.isSuspended());
        result.setUrl(urlBuilder.buildUrl(RestUrls.URL_PROCESS_INSTANCE, processInstance.getId()));
        result.setTenantId(processInstance.getTenantId());

        //Added by Ryan Johnston
        if (processInstance.isEnded()) {
            //Process complete. Note the same in the result.
            result.setCompleted(true);
        } else {
            //Process not complete. Note the same in the result.
            result.setCompleted(false);
        }

        if (returnVariables) {

            if (processInstance.isEnded()) {
                if (historicVariableList != null) {
                    for (HistoricVariableInstance historicVariable : historicVariableList) {
                        result.addVariable(createRestVariable(historicVariable.getVariableName(), historicVariable.getValue(),
                                RestVariable.RestVariableScope.LOCAL, processInstance.getId(), VARIABLE_PROCESS, false, urlBuilder));
                    }
                }

            } else {
                if (runtimeVariableMap != null) {
                    for (String name : runtimeVariableMap.keySet()) {
                        result.addVariable(createRestVariable(name, runtimeVariableMap.get(name),
                                RestVariable.RestVariableScope.LOCAL, processInstance.getId(), VARIABLE_PROCESS, false, urlBuilder));
                    }
                }
            }
        }
        //End Added by Ryan Johnston

        return result;
    }


    public RestVariable createRestVariable(String name, Object value, RestVariable.RestVariableScope scope,
                                           String id, int variableType, boolean includeBinaryValue, RestUrlBuilder urlBuilder) {

        RestVariableConverter converter = null;
        RestVariable restVar = new RestVariable();
        restVar.setVariableScope(scope);
        restVar.setName(name);

        if (value != null) {
            // Try converting the value
            for (RestVariableConverter c : variableConverters) {
                if (c.getVariableType().isAssignableFrom(value.getClass())) {
                    converter = c;
                    break;
                }
            }

            if (converter != null) {
                converter.convertVariableValue(value, restVar);
                restVar.setType(converter.getRestTypeName());
            } else {
                // Revert to default conversion, which is the serializable/byte-array form
                if (value instanceof Byte[] || value instanceof byte[]) {
                    restVar.setType(BYTE_ARRAY_VARIABLE_TYPE);
                } else {
                    restVar.setType(SERIALIZABLE_VARIABLE_TYPE);
                }

                if (includeBinaryValue) {
                    restVar.setValue(value);
                }

                if (variableType == VARIABLE_TASK) {
                    restVar.setValueUrl(urlBuilder.buildUrl(RestUrls.URL_TASK_VARIABLE_DATA, id, name));
                } else if (variableType == VARIABLE_EXECUTION) {
                    restVar.setValueUrl(urlBuilder.buildUrl(RestUrls.URL_EXECUTION_VARIABLE_DATA, id, name));
                } else if (variableType == VARIABLE_PROCESS) {
                    restVar.setValueUrl(urlBuilder.buildUrl(RestUrls.URL_PROCESS_INSTANCE_VARIABLE_DATA, id, name));
                } else if (variableType == VARIABLE_HISTORY_TASK) {
                    restVar.setValueUrl(urlBuilder.buildUrl(RestUrls.URL_HISTORIC_TASK_INSTANCE_VARIABLE_DATA, id, name));
                } else if (variableType == VARIABLE_HISTORY_PROCESS) {
                    restVar.setValueUrl(urlBuilder.buildUrl(RestUrls.URL_HISTORIC_PROCESS_INSTANCE_VARIABLE_DATA, id, name));
                } else if (variableType == VARIABLE_HISTORY_VARINSTANCE) {
                    restVar.setValueUrl(urlBuilder.buildUrl(RestUrls.URL_HISTORIC_VARIABLE_INSTANCE_DATA, id));
                } else if (variableType == VARIABLE_HISTORY_DETAIL) {
                    restVar.setValueUrl(urlBuilder.buildUrl(RestUrls.URL_HISTORIC_DETAIL_VARIABLE_DATA, id));
                }
            }
        }
        return restVar;
    }

   private void initializeVariableConverters() {
        variableConverters.add(new StringRestVariableConverter());
        variableConverters.add(new IntegerRestVariableConverter());
        variableConverters.add(new LongRestVariableConverter());
        variableConverters.add(new ShortRestVariableConverter());
        variableConverters.add(new DoubleRestVariableConverter());
        variableConverters.add(new BooleanRestVariableConverter());
        variableConverters.add(new DateRestVariableConverter());
    }



}
