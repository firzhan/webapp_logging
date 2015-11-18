package org.wso2.carbon.bpmn.rest.api.internal.provider.MessageBodyWriter;

import com.google.gson.stream.JsonWriter;
import org.activiti.rest.common.api.DataResponse;
import org.wso2.carbon.bpmn.rest.api.repository.DeploymentResponse;
//import org.wso2.carbon.bpmn.rest.api.repository.DeploymentResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

public class DataResponseJSONWriter {

    public static final String INDENT = "  ";
    public static final String DATA = "data";

    public static final String NAME = "name";
    public static final String ID = "id";
    public static final String DEPLOYMENT_TIME = "deploymentTime";
    public static final String TOTAL = "total";
    public static final String START = "start";
    public static final String SORT = "sort";
    public static final String ORDER = "order";
    public static final String SIZE = "size";
    public static final String CATEGORY = "category";
    public static final String URL = "url";
    public static final String TENANAT_ID = "tenantId";


    public void writeTo(DataResponse dataResponse, OutputStream entityStream) throws IOException {

        System.out.println("Entered in to writeTo : " + dataResponse.getSize() );
        PrintWriter printWriter = new PrintWriter(entityStream);
        JsonWriter writer = new JsonWriter(printWriter);

        writer.setIndent(INDENT);
        writer.beginObject();
        writer.name(DATA);
        System.out.println("Entered in to writeTo2 : " + dataResponse.getSize() );

        if(dataResponse.getSize() > 0){
            System.out.println("Entered in to writeTo3 : " + dataResponse.getSize() );
            writer.beginArray();

            List<DeploymentResponse> deploymentResponseList = ( List<DeploymentResponse>) dataResponse.getData();
            System.out.println("Entered in to writeTo4 : " + deploymentResponseList.size());
            for (DeploymentResponse deploymentResponse : deploymentResponseList){
                writeDeployment(writer, deploymentResponse);
            }
            writer.endArray();
        } else {
            writer.nullValue();
        }


        writer.name(TOTAL).value(dataResponse.getTotal());
        writer.name(START).value(dataResponse.getStart());
        writer.name(SORT).value(dataResponse.getSort());
        writer.name(ORDER).value(dataResponse.getOrder());
        writer.name(SIZE).value(dataResponse.getSize());

        writer.endObject();
        writer.flush();
        writer.close();
        printWriter.flush();
        printWriter.close();
    }

    private void writeDeployment(JsonWriter writer, DeploymentResponse deploymentResponse) throws IOException {

        writer.beginObject();
        writer.name(ID).value(deploymentResponse.getId());
        writer.name(NAME).value(deploymentResponse.getName());
        writer.name(DEPLOYMENT_TIME).value(String.valueOf(deploymentResponse.getDeploymentTime()));
        writer.name(CATEGORY).value(deploymentResponse.getCategory());
        writer.name(URL).value(deploymentResponse.getUrl());
        writer.name(TENANAT_ID).value(deploymentResponse.getTenantId());
        writer.endObject();

    }
}
