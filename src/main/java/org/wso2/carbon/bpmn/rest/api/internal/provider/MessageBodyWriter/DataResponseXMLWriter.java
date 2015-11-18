package org.wso2.carbon.bpmn.rest.api.internal.provider.MessageBodyWriter;

//import org.activiti.rest.common.api.DataResponse;
import org.activiti.rest.common.api.DataResponse;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMText;
import org.wso2.carbon.bpmn.rest.api.repository.DeploymentResponse;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

public class DataResponseXMLWriter {

    public static final String NAME = "name";
    public static final String ID = "id";
    public static final String DEPLOYMENT_TIME = "deploymentTime";
    public static final String TOTAL = "total";
    public static final String START = "start";
    public static final String SORT = "sort";
    public static final String ORDER = "order";
    public static final String SIZE = "size";
    public static final String CATEGORY = "category";
    public static final String URL = "url";
    public static final String TENANAT_ID = "tenantId";
    public static final String DEPLOYMENT = "deployment";
    public static final String DEPLOYMENTS = "deployments";


    public void writeTo(DataResponse dataResponse, OutputStream entityStream) throws IOException, XMLStreamException {


        OMFactory factory = OMAbstractFactory.getOMFactory();
        OMElement root = factory.createOMElement(new QName(DEPLOYMENTS));

        List<DeploymentResponse> deploymentResponseList = ( List<DeploymentResponse>) dataResponse.getData();

        OMElement totalElement = factory.createOMElement(new QName(TOTAL));
        OMText totalText = factory.createOMText(totalElement, String.valueOf(dataResponse.getTotal()));
        totalElement.addChild(totalText);

        OMElement startElement = factory.createOMElement(new QName(START));
        OMText startText = factory.createOMText(startElement, String.valueOf(dataResponse.getStart()));
        startElement.addChild(startText);

        OMElement sortElement = factory.createOMElement(new QName(SORT));
        OMText sortText = factory.createOMText(sortElement, String.valueOf(dataResponse.getTotal()));
        sortElement.addChild(sortText);

        OMElement orderElement = factory.createOMElement(new QName(ORDER));
        OMText orderText = factory.createOMText(orderElement,dataResponse.getOrder());
        orderElement.addChild(orderText);

        OMElement sizeElement = factory.createOMElement(new QName(SIZE));
        OMText sizeText = factory.createOMText(sizeElement, String.valueOf(dataResponse.getSize()));
        sizeElement.addChild(sizeText);


        root.addChild(totalElement);
        root.addChild(startElement);
        root.addChild(sortElement);
        root.addChild(orderElement);
        root.addChild(sizeElement);

        OMElement dataElement = factory.createOMElement(new QName("data"));
        for (DeploymentResponse deploymentResponse : deploymentResponseList){
            //writeDeployment(writer, deploymentResponse);
            OMElement deploymentElement = factory.createOMElement(new QName(DEPLOYMENT));

            OMElement idElement = factory.createOMElement(new QName(ID));
            OMText idText = factory.createOMText(idElement, deploymentResponse.getId());
            idElement.addChild(idText);

            OMElement nameElement = factory.createOMElement(new QName(NAME));
            OMText nameText = factory.createOMText(nameElement, deploymentResponse.getName());
            nameElement.addChild(nameText);

            OMElement deploymentTimeElement = factory.createOMElement(new QName(DEPLOYMENT_TIME));
            OMText deploymentTimeText = factory.createOMText(deploymentTimeElement, String.valueOf(deploymentResponse
                    .getDeploymentTime()));
            deploymentTimeElement.addChild(deploymentTimeText);

            OMElement urlElement = factory.createOMElement(new QName(URL));
            OMText urlText = factory.createOMText(urlElement, deploymentResponse.getUrl());
            urlElement.addChild(urlText);

            OMElement tenantIdElement = factory.createOMElement(new QName(TENANAT_ID));
            OMText tenantIdText = factory.createOMText(tenantIdElement, deploymentResponse.getTenantId());
            tenantIdElement.addChild(tenantIdText);

            deploymentElement.addChild(idElement);
            deploymentElement.addChild(nameElement);
            deploymentElement.addChild(deploymentTimeElement);
            deploymentElement.addChild(urlElement);
            deploymentElement.addChild(tenantIdElement);

            dataElement.addChild(deploymentElement);
        }

        root.addChild(dataElement);




        System.out.println("Going to serialize xml");

        root.serialize(entityStream);
        System.out.println("Done serializing xml");

        PrintWriter printWriter = new PrintWriter(entityStream);

        printWriter.flush();
        printWriter.close();

        System.out.println("Done with XML Writing");
    }
}
