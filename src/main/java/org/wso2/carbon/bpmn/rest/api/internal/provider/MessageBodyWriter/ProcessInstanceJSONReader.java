package org.wso2.carbon.bpmn.rest.api.internal.provider.MessageBodyWriter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.wso2.carbon.bpmn.rest.api.engine.variable.RestVariable;
import org.wso2.carbon.bpmn.rest.api.runtime.ProcessInstanceCreateRequest;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProcessInstanceJSONReader {

    protected ProcessInstanceCreateRequest handleJSON(JsonReader reader) throws IOException {
        ProcessInstanceCreateRequest processInstanceCreateRequest = new ProcessInstanceCreateRequest();
        Map<String, Object> map = new HashMap<>();
        handleObject(reader, map, null, processInstanceCreateRequest);
        return processInstanceCreateRequest;
    }

    protected void handleObject(JsonReader reader, Map<String, Object> map, JsonToken token, ProcessInstanceCreateRequest
            processInstanceCreateRequest ) throws IOException {

        String key = null;
        while (true) {
            if (token == null) {
                token = reader.peek();
            }

            if (JsonToken.BEGIN_OBJECT.equals(token)) {
                reader.beginObject();

            } else if (JsonToken.END_OBJECT.equals(token)) {
                reader.endObject();
                break;

            } else if (JsonToken.NAME.equals(token)) {
                key = reader.nextName();

            } else if (JsonToken.STRING.equals(token)) {
                String value = reader.nextString();
                if (key != null) {
                    if(processInstanceCreateRequest.isArrayIterated() == false){
                        setValuesForVariables(key, value, processInstanceCreateRequest);
                    } else {
                        map.put(key, value);
                    }
                    key = null;
                }

            } else if (JsonToken.NUMBER.equals(token)) {
                Double value = reader.nextDouble();
                if (key != null) {
                    if(processInstanceCreateRequest.isArrayIterated() == false){
                        setValuesForVariables(key, value, processInstanceCreateRequest);
                    } else {
                        map.put(key, String.valueOf(value));
                    }
                    key = null;
                }

            } else if (token.equals(JsonToken.BEGIN_ARRAY)) {
                processInstanceCreateRequest.setArrayIterated(true);
                Map<String, Object> values = handleArray(reader, processInstanceCreateRequest);
                if (key != null) {
                    map.put(key, values);
                }
                processInstanceCreateRequest.setArrayIterated(false);
            } else {
                reader.skipValue();
            }

            if (reader.hasNext()) {
                token = reader.peek();
            } else {
                break;
            }
        }
    }

    private Map<String, Object> handleArray(JsonReader reader, ProcessInstanceCreateRequest
            processInstanceCreateRequest) throws IOException {
        Map<String, Object> values = new HashMap<>();
        reader.beginArray();
        while (reader.hasNext()) {
            JsonToken token = reader.peek();
            if (token.equals(JsonToken.END_ARRAY)) {
                reader.endArray();
            } else {
                handleObject(reader, values, token, processInstanceCreateRequest);
            }
        }
        return values;
    }

    public void setValuesForVariables(String key, Object value,  ProcessInstanceCreateRequest processInstanceCreateRequest){

        if(key.equalsIgnoreCase("processDefinitionId")){
            if(value != null){
                processInstanceCreateRequest.setProcessDefinitionId(String.valueOf(value));
            }
        } else if(key.equalsIgnoreCase("variables")){
            Map<String, Object> variableMap = (Map<String, Object>) value;

            for (Map.Entry<String, Object> entry : variableMap.entrySet()){

                RestVariable restVariable = new RestVariable();
                Object variableValue = entry.getValue();
                restVariable.setName(entry.getKey());
                restVariable.setVariableScope(RestVariable.RestVariableScope.LOCAL);
                if(variableValue instanceof Boolean){
                    restVariable.setType("boolean");
                } else if(variableValue instanceof Date){
                    restVariable.setType("date");
                } else if(variableValue instanceof Boolean){
                    restVariable.setType("double");
                } else if(variableValue instanceof Integer){
                    restVariable.setType("integer");
                } else if(variableValue instanceof Long){
                    restVariable.setType("long");
                } else if(variableValue instanceof Short){
                    restVariable.setType("short");
                } else if(variableValue instanceof String){
                    restVariable.setType("string");
                } else if(variableValue instanceof Byte[] || variableValue instanceof byte[]){
                    restVariable.setType("binary");
                    restVariable.setValue(entry.getValue());
                } else {
                    restVariable.setType("serializable");
                }

                if(entry.getValue() != null && restVariable.getType() != null){
                    restVariable.setValue(entry.getValue());
                } else {
                    System.out.println("Some thin is wrong on assigning variables for type: " + restVariable.getType
                            () + " variable name :" + restVariable.getName());
                }
            }

        } else if(key.equalsIgnoreCase("businessKey")){
            if(value != null){
                processInstanceCreateRequest.setBusinessKey(String.valueOf(value));
            }
        } else if(key.equalsIgnoreCase("processDefinitionKey")){
            if(value != null){
                processInstanceCreateRequest.setProcessDefinitionKey(String.valueOf(value));
            }
        } else if(key.equalsIgnoreCase("tenantId")){
            if(value != null){
                processInstanceCreateRequest.setTenantId(String.valueOf(value));
            }
        } else if(key.equalsIgnoreCase("message")){
            if(value != null){
                processInstanceCreateRequest.setMessage(String.valueOf(value));
            }
        }
    }

}
