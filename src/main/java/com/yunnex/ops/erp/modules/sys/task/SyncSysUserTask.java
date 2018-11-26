package com.yunnex.ops.erp.modules.sys.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.yunnex.ops.erp.modules.sys.service.UserApiService;

/**
 * 定时同步用户数据
 */
@Service
@Lazy(false)
public class SyncSysUserTask {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncSysUserTask.class);
    
    @Autowired
    private UserApiService userApiService;
    

    /**
     * 定时同步用户数据
     *
     * @date 2018年9月5日
     * @author R/Q
     */
    @Scheduled(cron = "${sync_user_data_task_cron}")
    public void startProcess() {
        LOGGER.info("定时>---定时同步用户数据---<启动");
        userApiService.sync();
    }

}