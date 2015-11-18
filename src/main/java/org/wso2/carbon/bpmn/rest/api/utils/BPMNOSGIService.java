package org.wso2.carbon.bpmn.rest.api.utils;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.wso2.carbon.bpmn.core.BPMNEngineService;
import org.wso2.carbon.context.PrivilegedCarbonContext;

public class BPMNOSGIService {

    public static RepositoryService getRepositoryService(){
        return getBPMNEngineService().getProcessEngine().getRepositoryService();
    }

    public static BPMNEngineService getBPMNEngineService(){
        return (BPMNEngineService) PrivilegedCarbonContext.
                getThreadLocalCarbonContext().getOSGiService(BPMNEngineService.class, null);
    }

    public static RuntimeService getRumtimeService(){
        return getBPMNEngineService().getProcessEngine().getRuntimeService();
    }

    public static HistoryService getHistoryService(){
        return getBPMNEngineService().getProcessEngine().getHistoryService();
    }



}
