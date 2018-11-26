package com.yunnex.ops.erp.modules.workflow.flow.handler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.SpringContextHolder;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.act.service.ActTaskService;
import com.yunnex.ops.erp.modules.act.utils.ActUtils;
import com.yunnex.ops.erp.modules.order.constant.OrderConstants;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.SplitGoodForm;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.service.SystemService;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;


/**
 * 订单审核服务流程启动
 * 
 * @author Ejon
 * @date 2018年7月4日
 */
public class KclOrderReviewProcessHandler implements ProcessHandler {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    private ActTaskService actTaskService = SpringContextHolder.getBean(ActTaskService.class);
    private SystemService systemService = SpringContextHolder.getBean(SystemService.class);
    private ErpOrderOriginalInfoService erpOrderOriginalInfoService = SpringContextHolder.getBean(ErpOrderOriginalInfoService.class);

    @Override
    public void start(List<SplitGoodForm> request, ErpOrderOriginalInfo erpOrderOriginalInfo, Map<String, Object> response,
                    ProcessHandlerChain chain) {

        logger.info("订单审核服务流程 ,[{}]", erpOrderOriginalInfo);

        Map<String, Object> vars = Maps.newHashMap();

        List<User> users = systemService.getUserByRoleName(FlowConstant.FIRST_ORDER_AUDITOR_SYSROLE);

        String ids = users.stream().filter(user -> StringUtils.isNotBlank(user.getId())).map(user -> user.getId()).distinct()
                        .collect(Collectors.joining(Constant.COMMA));
        vars.put(FlowConstant.FIRST_ORDER_AUDITOR_FLOWROLE, ids);

        String procInsId = actTaskService.startProcess(ActUtils.ORDER_AUDIT_FLOW[0], ActUtils.ORDER_AUDIT_FLOW[1], erpOrderOriginalInfo.getId(),
                        "客常来订单审核流程",
                        vars);

        erpOrderOriginalInfo.setAuditStatus(OrderConstants.ORDER_AUDIT_STATUS_0);

        try {
            erpOrderOriginalInfoService.saveAuditStatus(erpOrderOriginalInfo);
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
        }

        logger.info("订单审核服务流程 结束,流程ID[{}]", procInsId);
        chain.start(request, erpOrderOriginalInfo, response, chain);
    }
}