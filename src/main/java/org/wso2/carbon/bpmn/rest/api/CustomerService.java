/*
 * Copyright 2011-2015 WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.bpmn.rest.api;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.DeploymentQueryProperty;
import org.activiti.engine.query.QueryProperty;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.rest.common.api.DataResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.bpmn.core.BPMNEngineService;
import org.wso2.carbon.bpmn.rest.api.internal.common.DeploymentsPaginateList;
import org.wso2.carbon.bpmn.rest.api.internal.common.RestResponseFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.Map;


@Path("/customerservice/")
public class CustomerService {

    long currentId = 123;
   // Map<Long, Customer> customers = new HashMap<Long, Customer>();
    //Map<Long, Order> orders = new HashMap<Long, Order>();

    private static Map<String, QueryProperty> allowedSortProperties = new HashMap<String, QueryProperty>();

    static {
        allowedSortProperties.put("id", DeploymentQueryProperty.DEPLOYMENT_ID);
        allowedSortProperties.put("name", DeploymentQueryProperty.DEPLOYMENT_NAME);
        allowedSortProperties.put("deployTime", DeploymentQueryProperty.DEPLOY_TIME);
        allowedSortProperties.put("tenantId", DeploymentQueryProperty.DEPLOYMENT_TENANT_ID);
    }

    //This it to write to logs to external file
    //private static Logger customLog = Logger.getLogger(CustomerService.class.getName());
    //This us to write to logs to wso2carbon.log file
    //private static final Log customLog = LogFactory.getLog(CustomerService.class);
    //private static final Logger customLog = Logger.getLogger(CustomerService.class);

    private final Log log = LogFactory.getLog(CustomerService.class);


    @GET
    @Path("/repository/deployments")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDeployments(@Context UriInfo uriInfo){

        BPMNEngineService bpmnEngineService = (BPMNEngineService) PrivilegedCarbonContext.
                getThreadLocalCarbonContext().getOSGiService(BPMNEngineService.class, null);
        RepositoryService repositoryService = bpmnEngineService.getProcessEngine().getRepositoryService();

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


/*        DeploymentsPaginateList deploymentsPaginateList = new DeploymentsPaginateList(new RestResponseFactory(), uriInfo);
        DataResponse dataResponse = deploymentsPaginateList.paginateList(allRequestParams, deploymentQuery, "id",
                allowedSortProperties);*/



/*
        if(uriInfo.getQueryParameters().getFirst("sort") != null){
            deploymentQuery.deploymentTenantIdLike(uriInfo.getQueryParameters().getFirst("sort"));
        }*/


      /*  PaginateRequest paginateRequest = new PaginateRequest();
        paginateRequest.setStart(0);
        paginateRequest.setSize(10);
        paginateRequest.setOrder("id");
        paginateRequest.setSort("asc");
        ((AbstractQuery) deploymentQuery).orderBy(DeploymentQueryProperty.DEPLOYMENT_ID);
        deploymentQuery.asc();
        List<Deployment> deployments = deploymentQuery.listPage(0, 10);

        List<DeploymentResponse> responseList = new ArrayList<DeploymentResponse>();
        for (Deployment instance : deployments) {
            System.out.println("Instance ID:" + instance.getId() + " Instance Name:" + instance.getName());
            responseList.add(new DeploymentResponse(instance));
        }*/

       /* DataResponse response = new DataResponse();
        response.setStart(0);
        response.setSize(responseList.size());
        response.setSort("id");
        response.setOrder("asc");
        response.setTotal(deploymentQuery.count());
        response.setData(responseList);*/

        System.out.println("Base ur:" + uriInfo.getBaseUri().toString());

    /*    Customer customer = new Customer();
        customer.setId(1);
        customer.setName("AAAAA");*/

        return Response.ok().build();
    }

   /* @GET
    @Path("/customers/{id}/")
    public Customer getCustomer(@PathParam("id") String id) {
        log.info("----invoking getCustomer, Customer id is: " + id);

        *//*customRootLog.getAllAppenders();*//*
       *//* Enumeration<Appender> enumerationAppenders = customRootLog.getAllAppenders();

        customRootLog.info("Going to print the log appenders at /customers/{id}/");

        ClassLoader loader = Logger.class.getClassLoader();
        customRootLog.info(loader.getResource("org/apache/log4j/Logger.class"));

        if(enumerationAppenders != null){
            customRootLog.info("Enumeration is not null=" + enumerationAppenders.hasMoreElements());

            while (enumerationAppenders.hasMoreElements()){
                Appender appender = enumerationAppenders.nextElement();
                customRootLog.info("Appende appender=" + appender.getName());

                if(appender instanceof DailyRollingFileAppender){
                    customRootLog.info("Going to print the daily rolling file appender path=" + ((DailyRollingFileAppender) appender).getFile());
                }
            }
        }
*//*

        PrivilegedCarbonContext.getThreadLocalCarbonContext().getOSGiService(BPMNEngineService.class, null);
        log.info("get customer information for "+ id);



        long idNumber = Long.parseLong(id);
        Customer c = customers.get(idNumber);

        BPMNEngineService bpmnEngineService = (BPMNEngineService) PrivilegedCarbonContext.
                getThreadLocalCarbonContext().getOSGiService(BPMNEngineService.class, null);
       // System.out.println("AAAAAAAAAAAAAAAAAAA:"+bpmnEngineService.getProcessEngine().getRepositoryService().);
        return c;
       *//* GovernanceConfigurationService service = (GovernanceConfigurationService) PrivilegedCarbonContext.
                getThreadLocalCarbonContext().getOSGiService(GovernanceConfigurationService.class, null);*//*


       // PrivilegedCa
    }



    @PUT
    @Path("/customers/")
    public Response updateCustomer(Customer customer) {
        System.out.println("----invoking updateCustomer, Customer name is: " + customer.getName());
        Customer c = customers.get(customer.getId());
        Response r;
        if (c != null) {
            customers.put(customer.getId(), customer);
            r = Response.ok().build();
        } else {
            r = Response.notModified().build();
        }

        return r;
    }

    @POST
    @Path("/customers/")
    public Response addCustomer(Customer customer) {
        System.out.println("----invoking addCustomer, Customer name is: " + customer.getName());
        customer.setId(++currentId);

        customers.put(customer.getId(), customer);

        return Response.ok(customer).build();
    }

    // Adding a new method to demonstrate Consuming and Producing text/plain

    @POST
    @Path("/customers/name/")
    @Consumes("text/plain")
    @Produces("text/plain")
    public String getCustomerName(String id) {
        System.out.println("----invoking getCustomerName, Customer id is: " + id);
        return "Isuru Suriarachchi";
    }

    @DELETE
    @Path("/customers/{id}/")
    public Response deleteCustomer(@PathParam("id") String id) {
        System.out.println("----invoking deleteCustomer, Customer id is: " + id);
        long idNumber = Long.parseLong(id);
        Customer c = customers.get(idNumber);

        Response r;
        if (c != null) {
            r = Response.ok().build();
            customers.remove(idNumber);
        } else {
            r = Response.notModified().build();
        }

        return r;
    }

    @Path("/orders/{orderId}/")
    public Order getOrder(@PathParam("orderId") String orderId) {
        System.out.println("----invoking getOrder, Order id is: " + orderId);
        long idNumber = Long.parseLong(orderId);
        Order c = orders.get(idNumber);
        return c;
    }

    final void init() {
        Customer c = new Customer();
        c.setName("John");
        c.setId(123);
        customers.put(c.getId(), c);

        Order o = new Order();
        o.setDescription("order 223");
        o.setId(223);
        orders.put(o.getId(), o);
    }
*/
}
