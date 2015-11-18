package org.wso2.carbon.bpmn.rest.api.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "model")
@XmlAccessorType(XmlAccessType.FIELD)
public class ModelRequest {
    protected String name;
    protected String key;
    protected String category;
    protected Integer version;
    protected String metaInfo;
    protected String deploymentId;
    protected String tenantId;

    protected boolean nameChanged;
    protected boolean keyChanged;
    protected boolean categoryChanged;
    protected boolean versionChanged;
    protected boolean metaInfoChanged;
    protected boolean deploymentChanged;
    protected boolean tenantChanged;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.nameChanged = true;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
        this.keyChanged = true;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
        this.categoryChanged = true;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
        this.versionChanged = true;
    }

    public String getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(String metaInfo) {
        this.metaInfo = metaInfo;
        this.metaInfoChanged = true;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
        this.deploymentChanged = true;
    }

    public void setTenantId(String tenantId) {
        tenantChanged = true;
        this.tenantId = tenantId;
    }

    public String getTenantId() {
        return tenantId;
    }

    @JsonIgnore
    public boolean isCategoryChanged() {
        return categoryChanged;
    }
    @JsonIgnore
    public boolean isKeyChanged() {
        return keyChanged;
    }
    @JsonIgnore
    public boolean isMetaInfoChanged() {
        return metaInfoChanged;
    }
    @JsonIgnore
    public boolean isNameChanged() {
        return nameChanged;
    }
    @JsonIgnore
    public boolean isVersionChanged() {
        return versionChanged;
    }
    @JsonIgnore
    public boolean isDeploymentChanged() {
        return deploymentChanged;
    }
    @JsonIgnore
    public boolean isTenantIdChanged() {
        return tenantChanged;
    }

}
