package org.wso2.carbon.bpmn.rest.api.internal.provider.MessageBodyWriter;

import org.wso2.carbon.bpmn.rest.api.repository.DeploymentResponse;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class DeploymentResponseWriter /*implements MessageBodyWriter<DeploymentResponse>*/ {
  //  @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        if(DeploymentResponse.class.equals(aClass)){
            if (MediaType.APPLICATION_JSON_TYPE.equals(mediaType) ){
                System.out.println("DeploymentResponse class achieved");
                return true;
            }
        }
        System.out.println("DeploymentResponse class false");
        return false;
    }

   // @Override
    public long getSize(DeploymentResponse deploymentResponse, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return 0;
    }

    //@Override
    public void writeTo(DeploymentResponse deploymentResponse, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {

    }
}
