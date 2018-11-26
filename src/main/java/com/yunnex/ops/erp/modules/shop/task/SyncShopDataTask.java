package com.yunnex.ops.erp.modules.shop.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoSyncApiService;

/**
 * 定时同步门店数据
 * 
 * @author SunQ
 * @date 2017年12月13日
 */
@Service
@Lazy(false)
public class SyncShopDataTask {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncShopDataTask.class);
    
    @Autowired
    private ErpShopInfoSyncApiService erpShopInfoSyncApiService;
    

    /**
     * 定时同步门店数据
     *
     * @date 2018年4月25日
     * @author R/Q
     */
    @Scheduled(cron = "${sync_shop_data_task_cron}")
    public void startProcess() {
        LOGGER.info("定时>---定时同步门店数据---<启动");
        erpShopInfoSyncApiService.syncAbnormalAll();
    }

}