package org.wso2.carbon.bpmn.rest.api.internal.provider.MessageBodyWriter;

import org.activiti.rest.common.api.DataResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
public class DataResponseWriter implements MessageBodyWriter<DataResponse> {

    private final Log log = LogFactory.getLog(DataResponseWriter.class);

    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        if(DataResponse.class.equals(aClass)){
            if (MediaType.APPLICATION_XML_TYPE.equals(mediaType) ) {
                System.out.println("DataResponse class achieved");
                return true;
            }
        }
        System.out.println("DataResponse class false");
        return false;
    }

    @Override
    public long getSize(DataResponse dataResponse, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return 0;
    }

    @Override
    public void writeTo(DataResponse dataResponse, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap, OutputStream outputStream) {

        System.out.println("Media type: " + mediaType.toString());
      /*  if (MediaType.APPLICATION_JSON_TYPE.equals(mediaType) ){
         DataResponseJSONWriter dataResponseJSONWriter = new DataResponseJSONWriter();
            try {
                dataResponseJSONWriter.writeTo(dataResponse,outputStream);
            } catch (IOException e) {
                log.error(e);
                System.out.println("Exception occured " + e.getMessage());
                throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
            }
        }*/

        if (MediaType.APPLICATION_XML_TYPE.equals(mediaType) ){
            System.out.println("Handling xml");
            DataResponseXMLWriter dataResponseXMLWriter = new DataResponseXMLWriter();
            try {
                dataResponseXMLWriter.writeTo(dataResponse,outputStream);
            } catch (IOException e) {
                log.error(e);
                System.out.println("Exception occured " + e.getMessage());
                throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
            }catch (XMLStreamException e) {
                log.error(e);
                System.out.println("Exception occured " + e.getMessage());
                throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
            }
        }

        System.out.println("Done with media handling");
       /* for (Iterator iterator = users.iterator(); iterator.hasNext();) {
            User user = (User) iterator.next();
            System.out.println(user.getId() + ":" + user.getName());
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(outputStream);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName(CONTENT);
        mapper.writeValue(jsonGenerator, users);
        jsonGenerator.writeEndObject();
        jsonGenerator.flush();
        jsonGenerator.close();*/
    }
}
