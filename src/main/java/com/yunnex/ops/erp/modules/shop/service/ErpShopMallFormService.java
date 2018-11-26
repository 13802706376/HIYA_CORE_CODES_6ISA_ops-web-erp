package com.yunnex.ops.erp.modules.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopActualLinkman;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopMallForm;
import com.yunnex.ops.erp.modules.shop.dao.ErpShopMallFormDao;

/**
 * 商户店铺信息收集Service
 * @author hanhan
 * @version 2018-05-26
 */
@Service
public class ErpShopMallFormService extends CrudService<ErpShopMallFormDao, ErpShopMallForm> {
    @Autowired 
    private ErpShopMallFormDao erpShopMallFormDao;
    
	public ErpShopMallForm get(String id) {
		return super.get(id);
	}
	
	public List<ErpShopMallForm> findList(ErpShopMallForm erpShopMallForm) {
		return super.findList(erpShopMallForm);
	}
	
	public Page<ErpShopMallForm> findPage(Page<ErpShopMallForm> page, ErpShopMallForm erpShopMallForm) {
		return super.findPage(page, erpShopMallForm);
	}
	
	@Transactional(readOnly = false)
	public void save(ErpShopMallForm erpShopMallForm) {
		super.save(erpShopMallForm);
	}
	
	@Transactional(readOnly = false)
	public void delete(ErpShopMallForm erpShopMallForm) {
		super.delete(erpShopMallForm);
	}
   @Transactional(readOnly = false)
    public List<ErpShopMallForm>  getShopMallFormListByShopInfoId(String shopInfoId) {
      return erpShopMallFormDao.getShopMallFormListByShopInfoId(shopInfoId);
    }
	
   
   public JSONObject deleteShopMallFormById(String id){
       JSONObject resObject = new JSONObject();
       ErpShopMallForm mallForm = this.get(id);
       if (null != mallForm) {
           this.delete(mallForm);
           resObject.put("result", true);
           resObject.put("message", "删除成功");
           return resObject;
       }
       resObject.put("result", false);
       resObject.put("message", "删除失败");
       return resObject;
       
   }
   
   
}