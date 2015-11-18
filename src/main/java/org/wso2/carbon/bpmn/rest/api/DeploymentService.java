package org.wso2.carbon.bpmn.rest.api;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.DeploymentQueryProperty;
import org.activiti.engine.query.QueryProperty;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.rest.common.api.DataResponse;
import org.apache.commons.io.IOUtils;
import org.wso2.carbon.bpmn.rest.api.internal.common.DeploymentsPaginateList;
import org.wso2.carbon.bpmn.rest.api.internal.common.RestResponseFactory;
import org.wso2.carbon.bpmn.rest.api.internal.exception.BPMNOSGIServiceException;
import org.wso2.carbon.bpmn.rest.api.repository.DeploymentResourceResponse;
import org.wso2.carbon.bpmn.rest.api.repository.DeploymentResourceResponseCollection;
import org.wso2.carbon.bpmn.rest.api.repository.DeploymentResponse;
import org.wso2.carbon.bpmn.rest.api.utils.BPMNOSGIService;
import org.wso2.carbon.bpmn.rest.api.utils.Utils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/deployments")
public class DeploymentService {

    private static Map<String, QueryProperty> allowedSortProperties = new HashMap<String, QueryProperty>();

    static {
        allowedSortProperties.put("id", DeploymentQueryProperty.DEPLOYMENT_ID);
        allowedSortProperties.put("name", DeploymentQueryProperty.DEPLOYMENT_NAME);
        allowedSortProperties.put("deployTime", DeploymentQueryProperty.DEPLOY_TIME);
        allowedSortProperties.put("tenantId", DeploymentQueryProperty.DEPLOYMENT_TENANT_ID);
    }

    @Context
    UriInfo uriInfo;

    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDeployments() {
        RepositoryService repositoryService = BPMNOSGIService.getRepositoryService();
        if(repositoryService == null){
            throw new BPMNOSGIServiceException("RepositoryService couldn't be identified");
        }
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();

        // Apply filters
        Map<String, String> allRequestParams = new HashMap<>();

        String name = uriInfo.getQueryParameters().getFirst("name");
        if( name != null){
            deploymentQuery.deploymentName(name);
            allRequestParams.put("name",name );
        }

        String nameLike = uriInfo.getQueryParameters().getFirst("nameLike");
        if( nameLike != null){
            deploymentQuery.deploymentNameLike(nameLike);
            allRequestParams.put("nameLike",nameLike);
        }

        String category = uriInfo.getQueryParameters().getFirst("category");
        if( category != null){
            deploymentQuery.deploymentCategory(category);
            allRequestParams.put("category",category);
        }

        String categoryNotEquals = uriInfo.getQueryParameters().getFirst("categoryNotEquals");
        if( categoryNotEquals != null){
            deploymentQuery.deploymentCategoryNotEquals(categoryNotEquals);
            allRequestParams.put("categoryNotEquals",categoryNotEquals);
        }

        String tenantId = uriInfo.getQueryParameters().getFirst("tenantId");
        if( tenantId != null){
            deploymentQuery.deploymentTenantId(tenantId);
            allRequestParams.put("tenantId",tenantId);
        }

        String tenantIdLike = uriInfo.getQueryParameters().getFirst("tenantIdLike");
        if( tenantIdLike != null){
            deploymentQuery.deploymentTenantIdLike(tenantIdLike);
            allRequestParams.put("tenantIdLike",tenantIdLike);
        }


        String sWithoutTenantId = uriInfo.getQueryParameters().getFirst("withoutTenantId");
        if( sWithoutTenantId != null){
            Boolean withoutTenantId = Boolean.valueOf(sWithoutTenantId);
            if (withoutTenantId) {
                deploymentQuery.deploymentWithoutTenantId();
            }
            allRequestParams.put("withoutTenantId",sWithoutTenantId);
        }

        String order = uriInfo.getQueryParameters().getFirst("order");
        if( order != null){
            allRequestParams.put("order",order);
        }

        String sort = uriInfo.getQueryParameters().getFirst("sort");
        if( sort != null){
            allRequestParams.put("order",sort);
        }


        DeploymentsPaginateList deploymentsPaginateList = new DeploymentsPaginateList(new RestResponseFactory(), uriInfo);
        DataResponse dataResponse = deploymentsPaginateList.paginateList(allRequestParams, deploymentQuery, "id",
                allowedSortProperties);

        System.out.println("Base ur:" + uriInfo.getBaseUri().toString());

        return Response.ok().entity(dataResponse).build();
    }

    @GET
    @Path("/{deploymentId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDeployment(@PathParam("deploymentId") String deploymentId){

        RepositoryService repositoryService = BPMNOSGIService.getRepositoryService();
        if(repositoryService == null){
            throw new BPMNOSGIServiceException("RepositoryService couldn't be identified");
        }
        Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();

        if (deployment == null) {
            throw new ActivitiObjectNotFoundException("Could not find a deployment with deploymentId '" + deploymentId + "'.",
                    Deployment.class);
        }

        DeploymentResponse deploymentResponse = new RestResponseFactory().createDeploymentResponse(deployment,uriInfo.getBaseUri().toString());
        return Response.ok().entity(deploymentResponse).build();
    }

    @GET
    @Path("/{deploymentId}/resources/{resourcePath:.*}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDeploymentResourceForDifferentUrl(@PathParam("deploymentId") String deploymentId, @PathParam("resourcePath") String resourcePath) {
        System.out.println("deploymentId:" + deploymentId + " resourcePath:"+resourcePath);
        RepositoryService repositoryService = BPMNOSGIService.getRepositoryService();
        if(repositoryService == null){
            throw new BPMNOSGIServiceException("RepositoryService couldn't be identified");
        }

        // Check if deployment exists
        Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
        if (deployment == null) {
            throw new ActivitiObjectNotFoundException("Could not find a deployment with id '" + deploymentId + "'.", Deployment.class);
        }

        List<String> resourceList = repositoryService.getDeploymentResourceNames(deploymentId);

        System.out.println("uriInfo.getBaseUri():" + uriInfo.getBaseUri().toString());
        if (resourceList.contains(resourcePath)) {
            // Build resource representation
            DeploymentResourceResponse deploymentResourceResponse = new RestResponseFactory()
                    .createDeploymentResourceResponse(deploymentId, resourcePath,
                            Utils.resolveContentType(resourcePath), uriInfo.getBaseUri().toString());
            return Response.ok().entity(deploymentResourceResponse).build();

        } else {
            // Resource not found in deployment
            throw new ActivitiObjectNotFoundException("Could not find a resource with id '" + resourcePath
                    + "' in deployment '" + deploymentId + "'.", Deployment.class);
        }

    }

    @GET
    @Path("/{deploymentId}/resources")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDeploymentResources(@PathParam("deploymentId") String deploymentId) {

         RepositoryService repositoryService = BPMNOSGIService.getRepositoryService();
        if(repositoryService == null){
            throw new BPMNOSGIServiceException("RepositoryService couldn't be identified");
        }
        // Check if deployment exists
        Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
        if (deployment == null) {
            throw new ActivitiObjectNotFoundException("Could not find a deployment with id '" + deploymentId + "'.", Deployment.class);
        }

        List<String> resourceList = repositoryService.getDeploymentResourceNames(deploymentId);
        DeploymentResourceResponseCollection deploymentResourceResponseCollection = new RestResponseFactory().createDeploymentResourceResponseList(deploymentId, resourceList, uriInfo.getBaseUri().toString());

        return Response.ok().entity(deploymentResourceResponseCollection).build();
    }


    @GET
    @Path("/{deploymentId}/resourcedata/{resourceId}")
    public Response getDeploymentResource(@PathParam("deploymentId") String deploymentId,
                                                      @PathParam("resourceId") String resourceId) {
        String contentType = Utils.resolveContentType(resourceId);
        return Response.ok().type(contentType).entity(getDeploymentResourceData(deploymentId, resourceId)).build();
    }




    private byte[] getDeploymentResourceData(String deploymentId, String resourceId) {

        if (deploymentId == null) {
            throw new ActivitiIllegalArgumentException("No deployment id provided");
        }
        if (resourceId == null) {
            throw new ActivitiIllegalArgumentException("No resource id provided");
        }

        RepositoryService repositoryService = BPMNOSGIService.getRepositoryService();

        if(repositoryService == null){
            throw new BPMNOSGIServiceException("RepositoryService couldn't be identified");
        }
        // Check if deployment exists
        Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
        if (deployment == null) {
            throw new ActivitiObjectNotFoundException("Could not find a deployment with id '" + deploymentId + "'.", Deployment.class);
        }

        List<String> resourceList = repositoryService.getDeploymentResourceNames(deploymentId);

        if (resourceList.contains(resourceId)) {
            final InputStream resourceStream = repositoryService.getResourceAsStream(deploymentId, resourceId);
            try {
                return IOUtils.toByteArray(resourceStream);
            } catch (Exception e) {
                throw new ActivitiException("Error converting resource stream", e);
            }
        } else {
            // Resource not found in deployment
            throw new ActivitiObjectNotFoundException("Could not find a resource with id '" + resourceId + "' in deployment '" + deploymentId + "'.", String.class);
        }
    }
}
