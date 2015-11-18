package org.wso2.carbon.bpmn.rest.api.internal.exception;

public class BPMNOSGIServiceException extends RuntimeException {

    private static final long serialVersionUID = 2L;

    public BPMNOSGIServiceException(String message) {
        super(message);
    }

    public BPMNOSGIServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
