package org.wso2.carbon.bpmn.rest.api.repository;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "resourceCollection")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeploymentResourceResponseCollection {

    private List<DeploymentResourceResponse> deploymentResourceResponseList;

    public DeploymentResourceResponseCollection(){

    }
    public List<DeploymentResourceResponse> getDeploymentResourceResponseList() {
        return deploymentResourceResponseList;
    }

    public void setDeploymentResourceResponseList(List<DeploymentResourceResponse> deploymentResourceResponseList) {
        this.deploymentResourceResponseList = deploymentResourceResponseList;
    }
}
