package org.wso2.carbon.bpmn.rest.api.internal.common;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

public class RestUrlBuilder {

    protected String baseUrl = "";

    protected RestUrlBuilder(){
    }

    protected RestUrlBuilder(String baseUrl){
        this.baseUrl = baseUrl;
    }

    /** Extracts the base URL from current request */
    public static RestUrlBuilder fromCurrentRequest(String baseUri){
        return usingBaseUrl(baseUri);
    }

    /** Uses baseUrl as the base URL */
    public static RestUrlBuilder usingBaseUrl(String baseUrl){
        if(baseUrl == null) throw new ActivitiIllegalArgumentException("baseUrl can not be null");
        if(baseUrl.endsWith("/")) baseUrl = baseUrl.substring(0, baseUrl.length()-1);
        return new RestUrlBuilder(baseUrl);
    }

    public String buildUrl(String[] fragments, Object ... arguments) {
        return new StringBuilder(baseUrl)
                .append("/")
                .append(MessageFormat.format(StringUtils.join(fragments, '/'), arguments))
                .toString();
    }
}
