package org.wso2.carbon.bpmn.rest.api.repository;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DeploymentResourceResponse {

    private String id;
    private String url;
    private String contentUrl;
    private String mediaType;
    private String type;

    public DeploymentResourceResponse(){

    }

    public DeploymentResourceResponse(String resourceId, String url, String contentUrl,
                                      String mediaType, String type) {
        setId(resourceId);
        setUrl(url);
        setContentUrl(contentUrl);
        setMediaType(mediaType);

        this.type = type;
        if (type == null) {
            this.type = "resource";
        }
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }
    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
    public String getContentUrl() {
        return contentUrl;
    }
    public void setMediaType(String mimeType) {
        this.mediaType = mimeType;
    }
    public String getMediaType() {
        return mediaType;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
