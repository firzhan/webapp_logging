package org.wso2.carbon.bpmn.rest.api;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.ModelQueryProperty;
import org.activiti.engine.query.QueryProperty;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.rest.common.api.DataResponse;
import org.wso2.carbon.bpmn.rest.api.internal.common.ModelsPaginateList;
import org.wso2.carbon.bpmn.rest.api.internal.common.RestResponseFactory;
import org.wso2.carbon.bpmn.rest.api.internal.exception.BPMNOSGIServiceException;
import org.wso2.carbon.bpmn.rest.api.repository.ModelRequest;
import org.wso2.carbon.bpmn.rest.api.repository.ModelResponse;
import org.wso2.carbon.bpmn.rest.api.utils.BPMNOSGIService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/models")
public class ModelService {

    private static Map<String, QueryProperty> allowedSortProperties = new HashMap<String, QueryProperty>();

    @Context
    UriInfo uriInfo;

    static {
        allowedSortProperties.put("id", ModelQueryProperty.MODEL_ID);
        allowedSortProperties.put("category", ModelQueryProperty.MODEL_CATEGORY);
        allowedSortProperties.put("createTime", ModelQueryProperty.MODEL_CREATE_TIME);
        allowedSortProperties.put("key", ModelQueryProperty.MODEL_KEY);
        allowedSortProperties.put("lastUpdateTime", ModelQueryProperty.MODEL_LAST_UPDATE_TIME);
        allowedSortProperties.put("name", ModelQueryProperty.MODEL_NAME);
        allowedSortProperties.put("version", ModelQueryProperty.MODEL_VERSION);
        allowedSortProperties.put("tenantId", ModelQueryProperty.MODEL_TENANT_ID);
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModels() {

        RepositoryService repositoryService = BPMNOSGIService.getRepositoryService();
        if(repositoryService == null){
            throw new BPMNOSGIServiceException("RepositoryService couldn't be identified");
        }
        // Apply filters
        Map<String, String> allRequestParams = new HashMap<>();
        ModelQuery modelQuery = repositoryService.createModelQuery();

        String id = uriInfo.getQueryParameters().getFirst("id");
        if (id != null) {
            modelQuery.modelId(id);
            allRequestParams.put("id", id);
        }

        String category = uriInfo.getQueryParameters().getFirst("category");
        if( category != null){
            modelQuery.modelCategory(category);
            allRequestParams.put("category", category);
        }

        String categoryLike = uriInfo.getQueryParameters().getFirst("categoryLike");
        if (categoryLike != null) {
            modelQuery.modelCategoryLike(categoryLike);
            allRequestParams.put("categoryLike", categoryLike);
        }

        String categoryNotEquals = uriInfo.getQueryParameters().getFirst("categoryNotEquals");
        if (categoryNotEquals != null) {
            modelQuery.modelCategoryNotEquals(categoryNotEquals);
            allRequestParams.put("categoryNotEquals", categoryNotEquals);
        }

        String name = uriInfo.getQueryParameters().getFirst("name");
        if( name != null){
            modelQuery.modelName(name);
            allRequestParams.put("name", name);
        }

        String nameLike = uriInfo.getQueryParameters().getFirst("nameLike");
        if( nameLike != null){
            modelQuery.modelNameLike(nameLike);
            allRequestParams.put("nameLike", nameLike);
        }

        String key =  uriInfo.getQueryParameters().getFirst("key");
        if (key != null) {
            modelQuery.modelKey(key);
            allRequestParams.put("key", key);
        }

        String version =  uriInfo.getQueryParameters().getFirst("version");
        if (version != null) {
            modelQuery.modelVersion(Integer.valueOf(version));
            allRequestParams.put("version", version);
        }

        String latestVersion =  uriInfo.getQueryParameters().getFirst("latestVersion");
        if (latestVersion != null) {
            boolean isLatestVersion = Boolean.valueOf(latestVersion);
            if (isLatestVersion) {
                modelQuery.latestVersion();
            }
            allRequestParams.put("latestVersion", latestVersion);
        }

        String deploymentId =  uriInfo.getQueryParameters().getFirst("deploymentId");
        if (deploymentId != null) {
            modelQuery.deploymentId(deploymentId);
            allRequestParams.put("deploymentId", deploymentId);
        }

        String deployed =  uriInfo.getQueryParameters().getFirst("deployed");
        if (deployed != null) {
            boolean isDeployed = Boolean.valueOf(deployed);
            if (isDeployed) {
                modelQuery.deployed();
            } else {
                modelQuery.notDeployed();
            }
            allRequestParams.put("deployed", deployed);
        }

        String tenantId = uriInfo.getQueryParameters().getFirst("tenantId");
        if(tenantId != null){
            modelQuery.modelTenantId(tenantId);
            allRequestParams.put("tenantId", tenantId);
        }

        String tenantIdLike = uriInfo.getQueryParameters().getFirst("tenantIdLike");
        if( tenantIdLike != null){
            modelQuery.modelTenantIdLike(tenantIdLike);
            allRequestParams.put("tenantIdLike", tenantIdLike);
        }

        String sWithoutTenantId = uriInfo.getQueryParameters().getFirst("withoutTenantId");
        if( sWithoutTenantId != null){
            boolean withoutTenantId = Boolean.valueOf(sWithoutTenantId);
            if (withoutTenantId) {
                modelQuery.modelWithoutTenantId();
            }
            allRequestParams.put("withoutTenantId", sWithoutTenantId);
        }

        DataResponse response = new ModelsPaginateList(new RestResponseFactory(), uriInfo).paginateList
                (allRequestParams, modelQuery, "id", allowedSortProperties);

        List<ModelResponse> modelResponseList = (List<ModelResponse>) response.getData();

        System.out.println("modelResponseList: " + modelResponseList.size());


        return Response.ok().entity(response).build();
    }
}
