package com.yunnex.ops.erp.modules.workflow.channel.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.persistence.Pager;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.result.IllegalArgumentErrorResult;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.excel.FastExcel;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.workflow.channel.constant.Constants;
import com.yunnex.ops.erp.modules.workflow.channel.dao.ErpChannelWeiboRechargeDao;
import com.yunnex.ops.erp.modules.workflow.channel.dto.WeiboRechargeActualRequestDto;
import com.yunnex.ops.erp.modules.workflow.channel.dto.WeiboRechargeRequestDto;
import com.yunnex.ops.erp.modules.workflow.channel.dto.WeiboRechargeResponseDto;
import com.yunnex.ops.erp.modules.workflow.channel.entity.ErpChannelWeiboRecharge;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowMonitorService;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowService;

/**
 * 微博通道充值Service
 *
 * @author yunnex
 * @version 2018-05-08
 */
@Service
public class ErpChannelWeiboRechargeService extends CrudService<ErpChannelWeiboRechargeDao, ErpChannelWeiboRecharge> {

    private static final String APPLYING_FILE_NAME = "充值申请列表.xlsx";
    private static final String SUCCESS_FILE_NAME = "充值成功列表.xlsx";
    private static final String CANCEL_FILE_NAME = "充值取消列表.xlsx";

    @Autowired
    private ErpOrderSplitInfoService erpOrderSplitInfoService;
    @Autowired
    WorkFlowMonitorService workFlowMonitorService;
    @Autowired
    private WorkFlowService workFlowService;

    @Override
    @Transactional(readOnly = false)
    public void save(ErpChannelWeiboRecharge erpChannelWeiboRecharge) {
        super.save(erpChannelWeiboRecharge);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ErpChannelWeiboRecharge erpChannelWeiboRecharge) {
        super.delete(erpChannelWeiboRecharge);
    }
  
    @Transactional(readOnly = false)
    public List<WeiboRechargeResponseDto>  findWeiboRechargeBysplitId(String splitId,String source) {
        return dao.findWeiboRechargeBysplitId(splitId, source);
    }
   
    
    /**
     * 分页查询微博充值列表
     * 
     * @param requestDto
     * @return
     */
    public Pager<WeiboRechargeResponseDto> findPage(WeiboRechargeRequestDto requestDto) {
        logger.info("微博充值 - 分页列表入参：requestDto = {}", JSON.toJSON(requestDto));
        List<WeiboRechargeResponseDto> data = dao.findByPage(requestDto);
        requestDto.setPage(false); // 计算总数时不分页，不排序
        Long count = dao.count(requestDto);
        Pager<WeiboRechargeResponseDto> pager = new Pager<>(data, count);
        return pager;
    }

    /**
     * 检查分单是否已有指定微博账号的充值记录
     *
     * @param splitId
     * @param weiboAccountNo
     * @return
     */
    public BaseResult checkSplitWeiboExists(String splitId, String weiboAccountNo, String weiboUid) {
        if (StringUtils.isBlank(splitId) || StringUtils.isBlank(weiboAccountNo) || StringUtils.isBlank(weiboUid)) {
            return new IllegalArgumentErrorResult("分单ID、微博账号或微博UID不能为空！");
        }
        boolean flag = dao.countSplitWeibo(splitId, weiboAccountNo, weiboUid) > 0;
        BaseResult result = new BaseResult();
        if (flag) {
            result.error(BaseResult.CODE_ERROR_ARG, "该分单已存在此微博账号的充值记录！");
            return result;
        }
        return result;
    }

    /**
     * 新增微博充值
     *
     * @param recharge
     * @param source
     * @param status
     * @return
     */
    @Transactional
    public BaseResult create(ErpChannelWeiboRecharge recharge, String source, String status) {
        logger.info("微博充值 - 新增充值入参：recharge = {}, source = {}, status = {}", JSON.toJSON(recharge), source, status);
        // 检验
        checkNotNull(recharge);
        BaseResult result = checkValid(recharge);
        if (!BaseResult.isSuccess(result)) {
            return result;
        }
        // 检查是否已存在充值记录
        result = checkSplitWeiboExists(recharge.getSplitId(), recharge.getWeiboAccountNo(), recharge.getWeiboUid());
        if (!BaseResult.isSuccess(result)) {
            return result;
        }
        recharge.setSource(source);
        recharge.setStatus(status);
        super.save(recharge);
        return result;
    }

    public BaseResult checkValid(ErpChannelWeiboRecharge recharge) {
        if (recharge == null) {
            throw new IllegalArgumentException("参数不合法！");
        }
        if (recharge.getApplyAmount() == null) {
            return new IllegalArgumentErrorResult("申请充值金额为必填项！");
        }
        if (recharge.getApplyDate() == null) {
            return new IllegalArgumentErrorResult("申请日期为必填项！");
        }
        if (StringUtils.isBlank(recharge.getWeiboAccountNo())) {
            return new IllegalArgumentErrorResult("微博账号不能为空！");
        }
        if (StringUtils.isBlank(recharge.getWeiboUid())) {
            return new IllegalArgumentErrorResult("微博Uid不能为空！");
        }
        return new BaseResult();
    }

    public void checkNotNull(ErpChannelWeiboRecharge recharge) {
        if (recharge == null) {
            throw new ServiceException("参数不合法！");
        }
        if (StringUtils.isBlank(recharge.getSplitId())) {
            throw new ServiceException("分单ID不能为空！");
        }
        if (StringUtils.isBlank(recharge.getShopInfoId())) {
            throw new ServiceException("商户ID不能为空！");
        }
    }

    /**
     * 修改实际充值金额
     *
     * @param requestDto
     * @return
     */
    @Transactional
    public BaseResult changeActualRecharge(WeiboRechargeActualRequestDto requestDto) {
        logger.info("微博充值 - 修改实际充值金额入参：requestDto = {}", JSON.toJSON(requestDto));
        if (requestDto == null || StringUtils.isBlank(requestDto.getId())) {
            throw new ServiceException("ID不能为空！");
        }
        if (requestDto.getActualAmount() == null) {
            return new IllegalArgumentErrorResult("实际充值金额不能为空！");
        }
        if (requestDto.getFinishDate() == null) {
            return new IllegalArgumentErrorResult("充值完成日期不能为空！");
        }
        ErpChannelWeiboRecharge recharge = new ErpChannelWeiboRecharge();
        recharge.setId(requestDto.getId()); // 表示要修改
        recharge.setActualAmount(requestDto.getActualAmount());
        recharge.setFinishDate(requestDto.getFinishDate());
        // 修改状态
        recharge.setStatus(Constants.STATUS_SUCCESS);
        super.save(recharge);
        microblogRechargeFinish(requestDto.getId());
        return new BaseResult();
    }

    /**
     * 修改充值状态
     *
     * @param id
     * @param status
     * @return
     */
    @Transactional
    public BaseResult changeRechargeStatus(String id, String status) {
        logger.info("微博充值 - 修改充值状态：id = {}, status = {}", id, status);
        if (StringUtils.isBlank(id)) {
            throw new ServiceException("ID不能为空！");
        }
        if (StringUtils.isBlank(status)) {
            throw new ServiceException("充值状态不能为空！");
        }
        ErpChannelWeiboRecharge recharge = new ErpChannelWeiboRecharge();
        recharge.setId(id);
        recharge.setStatus(status);
        super.save(recharge);
        return new BaseResult();
    }

    /**
     * 导出充值列表
     * 
     * @param requestDto
     * @param response
     * @return
     */
    public BaseResult export(WeiboRechargeRequestDto requestDto, HttpServletResponse response) {
        logger.info("微博充值 - Excel导出入参：requestDto = {}", JSON.toJSON(requestDto));
        if (requestDto == null) {
            throw new ServiceException("参数不合法！");
        }
        if (StringUtils.isBlank(requestDto.getStatus())) {
            return new IllegalArgumentErrorResult("充值状态不能为空！");
        }
        // 按条件查找，但不分页
        requestDto.setPageNo(1);
        requestDto.setPageSize(Integer.MAX_VALUE);
        List<WeiboRechargeResponseDto> list = dao.findByPage(requestDto);

        BaseResult result = new BaseResult();
        if (CollectionUtils.isEmpty(list)) {
            result.setCode(BaseResult.CODE_ERROR_ARG);
            result.setMessage("在此条件下查无数据！");
            return result;
        }

        String fileName = Constant.BLANK;
        String status = requestDto.getStatus();
        if (Constants.STATUS_APPLYING.equals(status)) {
            fileName = APPLYING_FILE_NAME;
        } else if (Constants.STATUS_SUCCESS.equals(status)) {
            fileName = SUCCESS_FILE_NAME;
        } else if (Constants.STATUS_CANCEL.equals(status)) {
            fileName = CANCEL_FILE_NAME;
            // Cancel和Applying导出字段一样
            status = Constants.STATUS_APPLYING;
        }

        try {
            FastExcel.exportExcel(response, fileName, status, list);
        } catch (IOException e) {
            String msg = "微博充值列表导出失败！";
            logger.error(msg, e);
            result.setCode(BaseResult.CODE_ERROR_ARG);
            result.setMessage(msg);
            return result;
        }
        return result;
    }

    // 微博充值完成,结束流程
    public void microblogRechargeFinish(String id) {
        ErpChannelWeiboRecharge WeiboRecharge = this.get(id);
        if (null != WeiboRecharge && Constants.STATUS_APPLYING.equals(WeiboRecharge.getStatus()) && Constants.SOURCE_FLOW
                        .equals(WeiboRecharge.getSource())) {
            ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoService.get(WeiboRecharge.getSplitId());
            if (null != erpOrderSplitInfo) {
                // 根据流程id和key
                List<Task> taskLists = workFlowMonitorService.getCurrentTasks(erpOrderSplitInfo.getProcInsId());
                for (Task task : taskLists) {
                    if (task.getTaskDefinitionKey().equals(JykFlowConstants.MICROBLO_RECHARGE_FINISH)) {
                        Map<String, Object> vars = Maps.newHashMap();
                        // 任务完成
                        this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, task.getId(), erpOrderSplitInfo.getProcInsId(),
                                        "微博充值完成", vars);
                    }
                }
            }
        }
    }
}
