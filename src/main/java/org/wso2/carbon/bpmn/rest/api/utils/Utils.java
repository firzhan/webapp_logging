package org.wso2.carbon.bpmn.rest.api.utils;

public class Utils {

    public static String resolveContentType(String resourceName) {
        String contentType = null;
        if (resourceName != null && !resourceName.isEmpty()) {
            String lowerResourceName = resourceName.toLowerCase();

            if (lowerResourceName.endsWith("png")) {
                contentType = "image/png";
            } else if (lowerResourceName.endsWith("xml") || lowerResourceName.endsWith("bpmn")) {
                contentType = "text/xml";
            }
        }
        return contentType;
    }
}
