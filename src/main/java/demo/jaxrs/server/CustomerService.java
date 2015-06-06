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

package demo.jaxrs.server;

import org.apache.log4j.Appender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


@Path("/customerservice/")
public class CustomerService {
    long currentId = 123;
    Map<Long, Customer> customers = new HashMap<Long, Customer>();
    Map<Long, Order> orders = new HashMap<Long, Order>();

    //This it to write to logs to external file
    //private static Logger customLog = Logger.getLogger(CustomerService.class.getName());
    //This us to write to logs to wso2carbon.log file
    //private static final Log customLog = LogFactory.getLog(CustomerService.class);
    //private static final Logger customLog = Logger.getLogger(CustomerService.class);
    private static final Logger customRootLog = Logger.getRootLogger();

    public CustomerService() {
        init();
    }

    @GET
    @Path("/customers/{id}/")
    public Customer getCustomer(@PathParam("id") String id) {
        customRootLog.info("----invoking getCustomer, Customer id is: " + id);

        /*customRootLog.getAllAppenders();*/
        Enumeration<Appender> enumerationAppenders = customRootLog.getAllAppenders();

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

        customRootLog.info("get customer information for "+ id);

        long idNumber = Long.parseLong(id);
        Customer c = customers.get(idNumber);
        return c;
    }

    @GET
    @Path("/customers2/{id}/")
    public Customer getCustomer2(@PathParam("id") String id) {
        customRootLog.info("----invoking getCustomer222222, Customer id is: " + id);
        PatternLayout layout = new PatternLayout();
        String conversionPattern = "[%p] %d %c %M - %m%n";
        layout.setConversionPattern(conversionPattern);
        DailyRollingFileAppender rollingAppender = new DailyRollingFileAppender();
        rollingAppender.setFile("C:\\Users\\fnaqash\\Documents\\WSO2\\temp\\PatientEnrollmentApp4.log");
        rollingAppender.setDatePattern("'.'yyyy-MM-dd");
        rollingAppender.setLayout(layout);
        rollingAppender.activateOptions();

        // configures the root logger
        customRootLog.addAppender(rollingAppender);

        Enumeration<Appender> enumerationAppenders = customRootLog.getAllAppenders();

        customRootLog.info("Going to print the log appenders at /customers2/{id}/");
        if(enumerationAppenders != null){
            customRootLog.info("Enumeration is not null=" + enumerationAppenders.hasMoreElements());

            while (enumerationAppenders.hasMoreElements()){
                Appender appender = enumerationAppenders.nextElement();
                customRootLog.info("Appende appender="+appender.getName());

                if(appender instanceof DailyRollingFileAppender){
                    customRootLog.info("Going to print the daily rolling file appender path=" + ((DailyRollingFileAppender) appender).getFile());
                }
            }
        }

        //customLog.info("get customer2222222222222 information for "+ id);
        customRootLog.info("get customer2222222222222 information for "+ id);
        /*wso2Log.info("get customer information for "+ id);

        PatternLayout layout = new PatternLayout();
        String conversionPattern = "[%p] %d %c %M - %m%n";
        layout.setConversionPattern(conversionPattern);
        DailyRollingFileAppender rollingAppender = new DailyRollingFileAppender();
        rollingAppender.setFile("PatientEnrollmentApp.log");
        rollingAppender.setDatePattern("'.'yyyy-MM-dd");
        rollingAppender.setLayout(layout);
        rollingAppender.activateOptions();
        // configures the root logger
        LOG.addAppender(rollingAppender);*/

        long idNumber = Long.parseLong(id);
        Customer c = customers.get(idNumber);
        return c;
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

}
