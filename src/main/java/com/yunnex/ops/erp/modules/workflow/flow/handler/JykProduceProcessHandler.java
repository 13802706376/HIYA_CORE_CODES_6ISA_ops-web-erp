package com.yunnex.ops.erp.modules.workflow.flow.handler;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunnex.ops.erp.common.utils.SpringContextHolder;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.entity.SplitGoodForm;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;

/**
 * 聚引客生产流程启动
 * 
 * @author Ejon
 * @date 2018年7月4日
 */
public class JykProduceProcessHandler implements ProcessHandler {


    public JykProduceProcessHandler() {
        super();
    }

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private ErpOrderSplitInfoService erpOrderSplitInfoService = SpringContextHolder.getBean(ErpOrderSplitInfoService.class);

    @Override
    public void start(List<SplitGoodForm> request, ErpOrderOriginalInfo erpOrderOriginalInfo, Map<String, Object> response,
                    ProcessHandlerChain chain) {

        logger.info("启动聚引客生产服务流程 ,[{}]", request);

        boolean isFist = (boolean) Optional.ofNullable(response.get(ProcessHandler.FIRST)).orElse(false);
        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoService.multiGoodSplitAndStartProcess(request, isFist,
                            request.get(0).getPlanningExpert());
        response.put("splitInfo", erpOrderSplitInfo);
        logger.info("启动聚引客生产服务流程 结束,[{}]", request);
        chain.start(request, erpOrderOriginalInfo, response, chain);
    }
}
