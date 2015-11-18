package org.wso2.carbon.bpmn.rest.api.internal.provider.MessageBodyWriter;

import com.google.gson.stream.JsonWriter;
import org.activiti.rest.common.api.DataResponse;
import org.wso2.carbon.bpmn.rest.api.repository.DeploymentResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

public class DeploymentJSONWriter {

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


    public void writeTo(DeploymentResponse deploymentResponse, OutputStream entityStream) throws IOException {

        PrintWriter printWriter = new PrintWriter(entityStream);
        JsonWriter writer = new JsonWriter(printWriter);

        writer.beginObject();
        writer.name(ID).value(deploymentResponse.getId());
        writer.name(NAME).value(deploymentResponse.getName());
        writer.name(DEPLOYMENT_TIME).value(String.valueOf(deploymentResponse.getDeploymentTime()));
        writer.name(CATEGORY).value(deploymentResponse.getCategory());
        writer.name(URL).value(deploymentResponse.getUrl());
        writer.name(TENANAT_ID).value(deploymentResponse.getTenantId());
        writer.endObject();
        writer.flush();
        writer.close();
        printWriter.flush();
        printWriter.close();
    }
}
