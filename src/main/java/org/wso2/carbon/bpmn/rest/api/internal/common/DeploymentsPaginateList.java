package org.wso2.carbon.bpmn.rest.api.internal.common;


import javax.ws.rs.core.UriInfo;
import java.util.List;

public class DeploymentsPaginateList extends AbstractPaginateList {

    public DeploymentsPaginateList(RestResponseFactory restResponseFactory, UriInfo uriInfo) {
        super(restResponseFactory, uriInfo);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected List processList(List list) {
        return restResponseFactory.createDeploymentResponseList(list, uriInfo.getBaseUri().toString());
    }
}
