package org.wso2.carbon.bpmn.rest.api.runtime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.commons.lang3.StringUtils;
import org.wso2.carbon.bpmn.rest.api.engine.variable.RestVariable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = "request")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProcessInstanceCreateRequest {

    private String processDefinitionId;
    private String processDefinitionKey;
    private String message;
    private String businessKey;
    @XmlElement(name = "variables", type = RestVariable.class)
    private List<RestVariable> variables;
    private String tenantId;

    public ProcessInstanceCreateRequest(){}

    public boolean isArrayIterated() {
        return arrayIterated;
    }

    public void setArrayIterated(boolean arrayIterated) {
        this.arrayIterated = arrayIterated;
    }

   /* public boolean isReturnVariables() {
        return returnVariables;
    }
*/
    private boolean arrayIterated;

    //Added by Ryan Johnston
    private boolean returnVariables;

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantId() {
        return tenantId;
    }

    @JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, defaultImpl=RestVariable.class)
    public List<RestVariable> getVariables() {
        return variables;
    }

    public void setVariables(List<RestVariable> variables) {
        this.variables = variables;
    }

    @JsonIgnore
    public boolean isCustomTenantSet() {
        return tenantId != null && !StringUtils.isEmpty(tenantId);
    }

    //Added by Ryan Johnston
    public boolean getReturnVariables() {
        return returnVariables;
    }

    //Added by Ryan Johnston
    public void setReturnVariables(boolean returnVariables) {
        this.returnVariables = returnVariables;
    }


}
