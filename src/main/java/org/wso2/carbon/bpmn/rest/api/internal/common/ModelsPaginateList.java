package org.wso2.carbon.bpmn.rest.api.internal.common;

import javax.ws.rs.core.UriInfo;
import java.util.List;

public class ModelsPaginateList extends AbstractPaginateList {

    public ModelsPaginateList(RestResponseFactory restResponseFactory, UriInfo uriInfo) {
        super(restResponseFactory, uriInfo);
    }



    @Override
    protected List processList(List list) {
        return restResponseFactory.createModelResponseList(list, uriInfo.getBaseUri().toString());
    }
}
