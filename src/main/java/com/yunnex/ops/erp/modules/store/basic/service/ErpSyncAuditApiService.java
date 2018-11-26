
package com.yunnex.ops.erp.modules.store.basic.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.modules.shop.dao.ErpShopInfoDao;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.store.basic.dao.ErpStoreInfoDao;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.pay.dao.ErpStorePayUnionpayDao;
import com.yunnex.ops.erp.modules.store.pay.dao.ErpStorePayWeixinDao;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayUnionpay;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayWeixin;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
/**
 * 同步审核状态service
 * @author yunnex
 * @version 2017-12-09
 */
@Service
public class ErpSyncAuditApiService {
	private static final Log logger = LogFactory.getLog(ErpSyncAuditApiService.class);
    
	@Autowired
	private ErpStoreInfoDao erpStoreInfoDao;
	@Autowired
	private ErpStorePayWeixinDao erpStorePayWeixinDao;
	@Autowired
	private ErpStorePayUnionpayDao erpStorePayUnionpayDao;
	@Autowired
	private ErpShopInfoDao erpShopInfoDao;
	
	/**
	 * 同步审核状态
	 * @author yunnex
	 * @version 2017-12-09
	 */
	@Transactional(readOnly = false)
	public boolean syncAudit(String zhangbeiId,String registerNo,String bankNo,Integer audit,String content,String type){
    	ErpShopInfo shop = erpShopInfoDao.getByZhangbeiID(zhangbeiId);
    	if(null==shop){
    		return false;
    	}
    	if("0".equals(type)){
    		ErpStoreInfo store = erpStoreInfoDao.findismain(shop.getId(), Global.NO);
    		if(null==store){
    			return false;
    		}
    		store.setAuditStatus(audit);
    		store.setAuditContent(content);
    		if(null==store.getId()){
        		erpStoreInfoDao.insert(store);
    		}else{
    			erpStoreInfoDao.update(store);
    		}
    		return true;
    	}else if("1".equals(type)){
    		List<ErpStorePayWeixin> wxpaylist = erpStorePayWeixinDao.findwxpayaudit(shop.getId(),registerNo,bankNo);
    		if(CollectionUtils.isEmpty(wxpaylist) ){
    			return false;
    		}
    		ErpStorePayWeixin wxpay=null;
    		for(int i=0;i<wxpaylist.size();i++){
    			wxpay = erpStorePayWeixinDao.get(wxpaylist.get(i).getId());
    			if(wxpay.getAuditStatus()==3||wxpay.getAuditStatus()==6||wxpay.getAuditStatus()==7){
	    			wxpay.setAuditStatus(audit);
	    			if(null==content){
	    				wxpay.setAuditContent("");
	    			}else{
	    				wxpay.setAuditContent(content);
	    			}
	    			if(null==wxpay.getId()){
	    				erpStorePayWeixinDao.insert(wxpay);
	    			}else{
	        			erpStorePayWeixinDao.update(wxpay);	
	    			}
    			}
    		}
    		return true;
    	}else if("2".equals(type)){
    		List<ErpStorePayUnionpay> unionlist = erpStorePayUnionpayDao.findunionaudit(shop.getId(), registerNo, bankNo);
    		if(CollectionUtils.isEmpty(unionlist)  ){
    			return false;
    		}
    		ErpStorePayUnionpay union=null;
    		for(int i=0;i<unionlist.size();i++){
    			union = erpStorePayUnionpayDao.get(unionlist.get(i).getId());
    			logger.info("===union.AuditStatus====>>"+union.getAuditStatus());
    			if(union.getAuditStatus()==3||union.getAuditStatus()==6||union.getAuditStatus()==7){
	    			union.setAuditStatus(audit);
	    			if(null==content){
	    				union.setAuditContent("");
	    			}else{
	    				union.setAuditContent(content);
	    			}
	    			if(null==union.getId()){
	    				erpStorePayUnionpayDao.insert(union);
	    			}else{
	    				erpStorePayUnionpayDao.update(union);
	    			}
    			}
    		}
    		return true;
    	}
    	return false;
    	
    }
	/**
	 * 同步审核状态
	 * @author yunnex
	 * @version 2017-12-09
	 */
	@Transactional(readOnly = false)
	public boolean syncAudit(String zhangbeiId,String registerNo,String bankNo,String machineToolNumber,Integer audit,String content,String type){
    	ErpShopInfo shop = erpShopInfoDao.getByZhangbeiID(zhangbeiId);
    	User user = UserUtils.getUser();
    	if(null==shop){
    		return false;
    	}
    	if("0".equals(type)){
    		ErpStoreInfo store = erpStoreInfoDao.findismain(shop.getId(), Global.NO);
    		if(null==store){
    			return false;
    		}
    		store.setAuditStatus(audit);
    		store.setAuditContent(content);
    		if(null==store.getId()){
        		erpStoreInfoDao.insert(store);
    		}else{
    			erpStoreInfoDao.update(store);
    		}
    		return true;
    	}else if("1".equals(type)){
    		List<ErpStorePayWeixin> wxpaylist = erpStorePayWeixinDao.findwxpayaudit(shop.getId(),registerNo,bankNo);
    		if(CollectionUtils.isEmpty(wxpaylist) ){
    			return false;
    		}
    		ErpStorePayWeixin wxpay=null;
    		for(int i=0;i<wxpaylist.size();i++){
    			wxpay = erpStorePayWeixinDao.get(wxpaylist.get(i).getId());
    			if(wxpay.getAuditStatus()==3||wxpay.getAuditStatus()==6||wxpay.getAuditStatus()==7){
	    			wxpay.setAuditStatus(audit);
	    			if(null==content){
	    				wxpay.setAuditContent("");
	    			}else{
	    				wxpay.setAuditContent(content);
	    			}
	    			if(null==wxpay.getId()){
	    				erpStorePayWeixinDao.insert(wxpay);
	    			}else{
	        			erpStorePayWeixinDao.update(wxpay);	
	    			}
    			}
    		}
    		return true;
    	}else if("2".equals(type)){
    		List<ErpStorePayUnionpay> unionlist = erpStorePayUnionpayDao.findunionaudit(shop.getId(), registerNo, bankNo);
    		if(CollectionUtils.isEmpty(unionlist)  ){
    			return false;
    		}
    		ErpStorePayUnionpay union=null;
    		for(int i=0;i<unionlist.size();i++){
    			union = erpStorePayUnionpayDao.get(unionlist.get(i).getId());
    			logger.info(union.getAuditStatus()+"---erpStorePayUnionpayDao.get(unionlist.get(i).getId()); {}"+machineToolNumber);
    			if(union.getAuditStatus()==3||union.getAuditStatus()==6||union.getAuditStatus()==7){
    				union.setMachineToolNumber(machineToolNumber);
	    			union.setAuditStatus(audit);
	    			if(null==content){
	    				union.setAuditContent("");
	    			}else{
	    				union.setAuditContent(content);
	    			}
	    			if(null==union.getId()){
	    				User u=union.getUpdateBy()==null?user:union.getUpdateBy();
	    				union.setUpdateBy(u);
	    				erpStorePayUnionpayDao.insert(union);
	    			}else{
	    				User u=union.getUpdateBy()==null?user:union.getUpdateBy();
	    				union.setUpdateBy(u);
	    				erpStorePayUnionpayDao.update(union);
	    			}
    			}else{
    				union.setMachineToolNumber(machineToolNumber);
    				if(null==union.getId()){
    					User u=union.getUpdateBy()==null?user:union.getUpdateBy();
	    				union.setUpdateBy(u);
	    				erpStorePayUnionpayDao.insert(union);
	    			}else{
	    				User u=union.getUpdateBy()==null?user:union.getUpdateBy();
	    				union.setUpdateBy(u);
	    				erpStorePayUnionpayDao.update(union);
	    			}
    			}
    		}
    		return true;
    	}
    	return false;
    	
    }
}

