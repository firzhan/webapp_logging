package org.wso2.carbon.bpmn.rest.api.internal.provider.MessageBodyWriter;

import com.google.gson.stream.JsonReader;
import org.wso2.carbon.bpmn.rest.api.runtime.ProcessInstanceCreateRequest;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
public class ProcessInstanceReader implements MessageBodyReader<ProcessInstanceCreateRequest> {
    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        if (ProcessInstanceCreateRequest.class.getName().equals(aClass.getName())) {
            if (MediaType.APPLICATION_JSON_TYPE.equals(mediaType) || MediaType.APPLICATION_XML_TYPE.equals(mediaType)) {
                System.out.println("ProcessInstanceCreateRequest invoke");
                return true;
            }
        }
        System.out.println("ProcessInstanceCreateRequest invoke failed");
        return false;
    }

    @Override
    public ProcessInstanceCreateRequest readFrom(Class<ProcessInstanceCreateRequest> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
        reader.setLenient(true);
        ProcessInstanceCreateRequest processInstanceCreateRequest = new ProcessInstanceJSONReader().handleJSON(reader);
        System.out.println("ProcessInstanceCreateRequest deserialized");
        return processInstanceCreateRequest;
    }
}
