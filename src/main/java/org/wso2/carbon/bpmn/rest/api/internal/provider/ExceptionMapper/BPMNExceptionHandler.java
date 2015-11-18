package org.wso2.carbon.bpmn.rest.api.internal.provider.ExceptionMapper;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ActivitiTaskAlreadyClaimedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.bpmn.rest.api.internal.exception.BPMNOSGIServiceException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class BPMNExceptionHandler implements ExceptionMapper<Exception> {

    private final Log log = LogFactory.getLog(BPMNExceptionHandler.class);
    @Override
    public Response toResponse(Exception e) {


        if(e instanceof ActivitiIllegalArgumentException){
            log.error("Exception during service invocation ", e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } else if(e instanceof ActivitiTaskAlreadyClaimedException){
            log.error("Exception during Task claiming ", e);
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        } else if(e instanceof ActivitiObjectNotFoundException){
            log.error("Exception due to Activiti Object Not Found ", e);
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } else if(e instanceof BPMNOSGIServiceException){
            log.error("Exception due to issues on osgi service ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }else {
            log.error("Unknown Exception occurred ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
