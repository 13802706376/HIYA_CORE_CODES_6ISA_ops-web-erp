package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.dao.ErpFlowFormDao;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpFlowForm;

/**
 * 流程表单数据Service
 * @author xiaoyunfei
 * @version 2018-05-07
 */
@Service
public class ErpFlowFormService extends CrudService<ErpFlowFormDao, ErpFlowForm> {

    @Autowired
    private ErpFlowFormDao erpFlowFormDao;
	public ErpFlowForm get(String id) {
		return super.get(id);
	}
	
	public List<ErpFlowForm> findList(ErpFlowForm erpFlowForm) {
		return super.findList(erpFlowForm);
	}
	
	public Page<ErpFlowForm> findPage(Page<ErpFlowForm> page, ErpFlowForm erpFlowForm) {
		return super.findPage(page, erpFlowForm);
	}

	@Transactional(readOnly = false)
	public void delete(ErpFlowForm erpFlowForm) {
		super.delete(erpFlowForm);
	}

    @Transactional(readOnly = false)
    public void save(ErpFlowForm erpFlowForm) {
        super.save(erpFlowForm);
    }
   
    @Transactional(readOnly = false)
    public void saveErpFlowForm(String taskId,String procInsId ,String busId, String formAttrName,String taskDefKey, String formAttrValue,String attrType) {
        ErpFlowForm erpFlowForm =new ErpFlowForm();
        erpFlowForm.setTaskId(taskId);
        erpFlowForm.setBusId(busId);
        erpFlowForm.setProcInsId(procInsId);
        erpFlowForm.setTaskDef(taskDefKey);
        erpFlowForm.setFormAttrType(attrType);
        erpFlowForm.setFormAttrName(formAttrName);
        erpFlowForm.setFormAttrValue(formAttrValue);
        saveErpFlowForm(erpFlowForm);

   }
    
    @Transactional(readOnly = false)
    public void saveErpFlowForm(String taskId,String procInsId ,String busId, String formAttrName,String taskDefKey, String formAttrValue) {
        if (StringUtils.isNotBlank(formAttrValue)) {
            ErpFlowForm erpFlowForm = new ErpFlowForm();
            erpFlowForm.setTaskId(taskId);
            erpFlowForm.setBusId(busId);
            erpFlowForm.setProcInsId(procInsId);
            erpFlowForm.setTaskDef(taskDefKey);
            erpFlowForm.setFormAttrType(FlowConstant.FLOW_FORM_DATA_ATTR_TYPE_NORMAL);
            erpFlowForm.setFormAttrName(formAttrName);
            erpFlowForm.setFormAttrValue(formAttrValue);
            saveErpFlowForm(erpFlowForm);
        }
   }
    @Transactional(readOnly = false)
    public void saveErpFlowForm(ErpFlowForm erpFlowForm) {
    	ErpFlowForm flowForm = erpFlowFormDao.findByCondition(erpFlowForm);
        if (null == flowForm) {
            flowForm = new ErpFlowForm();
            flowForm.setBusId(erpFlowForm.getBusId());
            flowForm.setProcInsId(erpFlowForm.getProcInsId());
        }
        flowForm.setFormAttrDesc(erpFlowForm.getFormAttrDesc());
        flowForm.setFormAttrName(erpFlowForm.getFormAttrName());
        flowForm.setFormAttrType(erpFlowForm.getFormAttrType());
        flowForm.setFormAttrValue(erpFlowForm.getFormAttrValue());
        flowForm.setFormTextValue(erpFlowForm.getFormTextValue());
        flowForm.setTaskId(erpFlowForm.getTaskId());
        flowForm.setTaskDef(erpFlowForm.getTaskDef());


        this.save(flowForm);

    }

    public List<ErpFlowForm> findByTaskId(String taskId) {
        return dao.findByTaskId(taskId);
    }

    public int deleteByFormAttrName(String taskId, String formAttrName) {
        return dao.deleteByFormAttrName(taskId, formAttrName);
    }

    public String findByProcessIdAndAttrName(String procInsId, String attrName) {
        ErpFlowForm erpFlowForm = dao.findByProcessIdAndAttrName(procInsId, attrName);
        if (null == erpFlowForm)
            return Constant.BLANK;
        if (ErpFlowForm.TEXT.equalsIgnoreCase(erpFlowForm.getFormAttrType()))
            return erpFlowForm.getFormTextValue();
        return erpFlowForm.getFormAttrValue();
    }

    public List<ErpFlowForm> findByProcessIdAndTask(String procInsId, String taskDefinitionKey) {
        return dao.findByProcessIdAndTask(procInsId, taskDefinitionKey);
    }

	public void batchInsert(List<ErpFlowForm> list) {
		if(CollectionUtils.isNotEmpty(list)) {
			dao.batchInsert(list);
		}
	}

	public List<String> findFormAttrNamesByProcInsId(String procInsId) {
		if(StringUtils.isBlank(procInsId)) {
			return Collections.EMPTY_LIST;
		}
		List<String> list = dao.findFormAttrNamesByProcInsId(procInsId);
		return list==null?Collections.EMPTY_LIST:list;
	}

	public void updateErpFlowForm(String procInsId,String formAttrName, ErpFlowForm entity) {
		if(StringUtils.isBlank(procInsId) || StringUtils.isBlank(formAttrName)) {
			return;
		}
		entity.setProcInsId(procInsId);
		entity.setFormAttrName(formAttrName);
		dao.updateErpFlowForm(entity);
	}

	   /**
     * 根据流程id，属性名称，任务节点key查询属性值
     * @param procInsId
     * @param formAttrName
     * @param taskDefKey
     * @return
     */
	public String findByCondition(String taskDef, String procInsId, String formAttrName) {
		if(StringUtils.isBlank(taskDef)||StringUtils.isBlank(procInsId)||StringUtils.isBlank(formAttrName)) {
			return "";
		}
		
		ErpFlowForm erpFlowForm = new ErpFlowForm();
		erpFlowForm.setTaskDef(taskDef);
		erpFlowForm.setProcInsId(procInsId);
		erpFlowForm.setFormAttrName(formAttrName);
		ErpFlowForm flowForm = erpFlowFormDao.findByCondition(erpFlowForm);
		if(flowForm!=null) {
			return flowForm.getFormAttrValue();
		}
		return "";
	}

}