package com.yunnex.ops.erp.modules.agent.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.yunnex.ops.erp.modules.agent.service.ErpAgentInfoApiService;

/**
 * 定时同步服务商信息
 */
@Service
@Lazy(false)
public class SyncAgentInfoScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncAgentInfoScheduler.class);
    private static final int MILLIS = 1000;
    @Autowired
    private ErpAgentInfoApiService agentInfoApiService;

    /**
     * 每天凌晨0点同步服务商信息
     */
    @Scheduled(cron = "${sync_agent_info_scheduler_cron}")
    public void sync() {
        long start = System.currentTimeMillis();
        LOGGER.info("服务商同步定时任务开始");
        agentInfoApiService.syncAgents();
        LOGGER.info("服务商同步定时任务结束。耗时：{}s", (System.currentTimeMillis() - start) / MILLIS);
    }

}
