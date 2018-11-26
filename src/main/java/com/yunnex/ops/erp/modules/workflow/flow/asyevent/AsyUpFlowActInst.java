package com.yunnex.ops.erp.modules.workflow.flow.asyevent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.modules.act.service.ActProInstService;

/**
 * @author hanhan
 * @date 2018年8月27日
 */
@Component
public class AsyUpFlowActInst {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ActProInstService actProInstService;
    
    @Async // 必须有次注解
    public void upActInst(String  procInsId, String taskDefKey) {
        try {
            Thread.sleep(2* 1000);
            actProInstService.updateSysHiActinstInfo(procInsId, taskDefKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
