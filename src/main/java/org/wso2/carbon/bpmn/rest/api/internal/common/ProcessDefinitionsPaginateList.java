package org.wso2.carbon.bpmn.rest.api.internal.common;

import javax.ws.rs.core.UriInfo;
import java.util.List;

public class ProcessDefinitionsPaginateList extends AbstractPaginateList {

    public ProcessDefinitionsPaginateList(RestResponseFactory restResponseFactory, UriInfo uriInfo) {
        super(restResponseFactory, uriInfo);
    }
    @Override
    protected List processList(List list) {
        return restResponseFactory.createProcessDefinitionResponseList(list, uriInfo.getBaseUri().toString());
    }
}
