package com.yunnex.ops.erp.modules.workflow.remarks.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.remarks.dao.WorkflowRemarksInfoDao;
import com.yunnex.ops.erp.modules.workflow.remarks.entity.WorkflowRemarksInfo;

/**
 * 流程备注Service
 * 
 * @author sunq
 * @version 2018-03-29
 */
@Service
public class WorkflowRemarksInfoService extends CrudService<WorkflowRemarksInfoDao, WorkflowRemarksInfo> {

    @Autowired
    private WorkflowRemarksInfoDao workflowRemarksInfoDao;
    @Autowired
    private ErpDeliveryServiceService erpDeliveryServiceService;
    
    @Override
    public WorkflowRemarksInfo get(String id) {
        return super.get(id);
    }
    
    @Override
    public List<WorkflowRemarksInfo> findList(WorkflowRemarksInfo workflowRemarksInfo) {
        return super.findList(workflowRemarksInfo);
    }
    
    @Override
    public Page<WorkflowRemarksInfo> findPage(Page<WorkflowRemarksInfo> page, WorkflowRemarksInfo workflowRemarksInfo) {
        return super.findPage(page, workflowRemarksInfo);
    }
    
    @Override
    @Transactional(readOnly = false)
    public void save(WorkflowRemarksInfo workflowRemarksInfo) {
        super.save(workflowRemarksInfo);
    }
    
    @Override
    @Transactional(readOnly = false)
    public void delete(WorkflowRemarksInfo workflowRemarksInfo) {
        WorkflowRemarksInfo dbObj = super.get(workflowRemarksInfo.getId());
        User user = UserUtils.getUser();
        if (dbObj == null || !StringUtils.equals(dbObj.getCreateUserId(), user.getId())) {
            throw new ServiceException("无法删除非用户所属的备注数据。");
        }
        super.delete(workflowRemarksInfo);
    }
    
    public List<WorkflowRemarksInfo> findListByProcInsId(String procInsId) {
        return workflowRemarksInfoDao.findListByProcInsId(procInsId);
    }

    /**
     * 业务定义：查询流程备注项
     * 
     * @date 2018年7月9日
     * @author R/Q
     */
    public List<Map<String, Object>> queryRemarkItems(String workflowKey, String workflowNodeKey) {
        List<Map<String, Object>> remarkItems = super.dao.queryRemarkItems(workflowKey, workflowNodeKey);
        if (CollectionUtils.isEmpty(remarkItems)) {
            // 设置默认备注项
            Map<String, Object> defaultValue = Maps.newHashMap();
            defaultValue.put("remarkItemId", "1");
            defaultValue.put("remarkItemName", "其它");
            defaultValue.put("remarkItemType", "1");
            remarkItems = Lists.newArrayList();
            remarkItems.add(defaultValue);
        }
        return remarkItems;
    }

    /**
     * 业务定义：批量保存
     * 
     * @date 2018年7月10日
     * @author R/Q
     */
    @Transactional(readOnly = false)
    public void batchSave(String paramJson) {
        if (StringUtils.isBlank(paramJson)) {
            throw new ServiceException("前台传递参数错误");
        }
        String remarkItemName="";
        List<WorkflowRemarksInfo> paramList = JSON.parseArray(StringEscapeUtils.unescapeHtml4(paramJson), WorkflowRemarksInfo.class);
        String procInsId= paramList.get(0).getProcInsId();
        if(!StringUtil.isBlank(procInsId)){
        	remarkItemName=workflowRemarksInfoDao.findMessageByProcInsId(procInsId);
        	remarkItemName = StringUtil.isBlank(remarkItemName)?"":remarkItemName;
        }
        if (CollectionUtils.isNotEmpty(paramList)) {
            // 备注记录一次保存数量不会超过超过10条，循环保存
            for (WorkflowRemarksInfo workflowRemarksInfo : paramList) {
            	if(remarkItemName.indexOf(workflowRemarksInfo.getRemarkItemName())==-1){
            		if(StringUtil.isBlank(remarkItemName)){
                		remarkItemName+=workflowRemarksInfo.getRemarkItemName()+",";
                	}else{
                		remarkItemName+=","+workflowRemarksInfo.getRemarkItemName();
                	}
            	}
                this.save(workflowRemarksInfo);
            }
        }
        if(!StringUtil.isBlank(remarkItemName)){
        	if(!remarkItemName.endsWith(",")){
        		remarkItemName=remarkItemName+",";
        	}
        	saveRemarkItemName(procInsId,remarkItemName);
        }
    }
    
    private void saveRemarkItemName(String procInsId,String remarkItemName) {
    	ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        if (erpDeliveryService == null) {
            logger.error("交付服务信息不存在 ！procInsId={}", procInsId);
        }else{
            // 物料制作跟踪任务完成时间
            erpDeliveryService.setExcptionLogo(remarkItemName);
            erpDeliveryServiceService.save(erpDeliveryService);
        }
    }
}