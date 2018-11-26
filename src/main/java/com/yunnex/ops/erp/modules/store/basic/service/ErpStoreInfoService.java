package com.yunnex.ops.erp.modules.store.basic.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.AESUtil;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.shop.constant.ShopConstant;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.store.basic.api.ErpFriendCheckApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpPayOpenApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpWeiboCheckApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpWxPayApi;
import com.yunnex.ops.erp.modules.store.basic.dao.ErpStoreCredentialsDao;
import com.yunnex.ops.erp.modules.store.basic.dao.ErpStoreInfoDao;
import com.yunnex.ops.erp.modules.store.basic.dao.ErpStoreLegalPersonDao;
import com.yunnex.ops.erp.modules.store.basic.dao.ErpStoreLinkmanDao;
import com.yunnex.ops.erp.modules.store.basic.dto.PublicAccountAndWeiboDto;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreCredentials;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreLegalPerson;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreLinkman;
import com.yunnex.ops.erp.modules.store.pay.dao.ErpStoreBankDao;
import com.yunnex.ops.erp.modules.store.pay.dao.ErpStorePayUnionpayDao;
import com.yunnex.ops.erp.modules.store.pay.dao.ErpStorePayWeixinDao;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStoreBank;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayUnionpay;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayWeixin;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStoreBankService;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStorePayWeixinService;
import com.yunnex.ops.erp.modules.workflow.flow.dto.PayAuditStatusDto;

/**
 * 门店基本信息Service
 * 
 * @author yunnex
 * @version 2017-12-09
 */
@Service
public class ErpStoreInfoService extends CrudService<ErpStoreInfoDao, ErpStoreInfo> {
    private static final int FOUR = 4;

	@Autowired
	private ErpStoreInfoDao erpstoreDao;
	@Autowired
	private ErpStoreLegalPersonDao erpStoreLegalPersondao;
	@Autowired
	private ErpStoreCredentialsDao erpStoreCredentialsDao;
	@Autowired
	private ErpStoreLinkmanDao erpStoreLinkmanDao;
	@Autowired
	private ErpStorePayWeixinDao erpStorePayWeixinDao;
	@Autowired
	private ErpStorePayUnionpayDao erpStorePayUnionpayDao;
	@Autowired
	private ErpStoreBankDao erpStoreBankDao;
    @Autowired
    private ErpShopInfoService shopInfoService;
    @Autowired
    private ErpStoreBankService bankService;
    @Autowired
    private ErpStorePayWeixinService wxpayService;

    @Override
	@Transactional(readOnly = false)
	public void save(ErpStoreInfo erpStoreInfo) {
		super.save(erpStoreInfo);
	}

    @Override
	@Transactional(readOnly = false)
	public void delete(ErpStoreInfo erpStoreInfo) {
		super.delete(erpStoreInfo);
	}
	

	public List<ErpStoreInfo> findAllListWhereShopId(String del,String id){
		return erpstoreDao.findAllListWhereShopId(del,id);
		
	}
	
	public int findCountWhereShopId(String del,String id){
		return erpstoreDao.findCountWhereShopId(del,id);
		
	}

    public ErpStoreInfo findOnetoManyAll(String del, String id) {
		return erpstoreDao.findOnetoManyAll(del, id);
	}
	
	public ErpStoreInfo findBasicInformation(String del,String id){
		return erpstoreDao.findBasicInformation(del, id);
	}
	
    public ErpStoreInfo findShopInfoByStoreId(String del,String id) {
        return erpstoreDao.findShopInfoByStoreId(del, id);
    }

	public ErpStoreInfo getIsmainStore(String del,String id,String ismain){
		return erpstoreDao.getIsmainStore(del, id, ismain);
	}
	
	public Integer countIsMain(String ismain,String id,String del){
		return erpstoreDao.countIsMain(ismain, id, del);
	}
	public 	List<ErpStoreInfo> findwhereshopidList(String shopid,String del){
		return erpstoreDao.findwhereshopidList(shopid, del);
	}
	
	public ErpStoreInfo findismain(String shopid,String del){
		return erpstoreDao.findismain(shopid, del);
	}
	
	public ErpStoreInfo findzhangbeiaudit(String id,String del){
		return erpstoreDao.findzhangbeiaudit(id, del);
	}
	@Transactional(readOnly = false)
	public Integer updateWhereShopId(String shopid){
		return erpstoreDao.updateWhereShopId(shopid);
	}
	@Transactional(readOnly = false)
	public Integer updateWhereStoreId(String id){
		return erpstoreDao.updateWhereStoreId(id);
	}
	public List<ErpStoreInfo> findwxpayaudit(String shopid,String del){
		return erpstoreDao.findwxpayaudit(shopid, del);
	}
	public ErpStoreInfo wxpayaudit(String id,String del){
		return erpstoreDao.wxpayaudit(id, del);
	}
	public ErpStoreInfo unionaudit(String id,String del){
		return erpstoreDao.unionaudit(id, del);
	}
	
	public String getPayQualifyById(String id) {
	    return erpstoreDao.getPayQualifyById(id);
	}
	
	public List<ErpStoreInfo> findunionaudit(String shopid,String del){
		return erpstoreDao.findunionaudit(shopid, del);
	}
	
	public List<ErpStoreInfo> syncwxpayaudit(@Param("shopid") String shopid){
		return erpstoreDao.syncwxpayaudit(shopid);
	}

    /**
     * 某个商户的所有门店的所有微信进件审核状态
     *
     * @param shopId
     * @return
     */
    public List<PayAuditStatusDto> findWxpayAuditStatus(String shopId) {
        return dao.findWxpayAuditStatus(shopId);
    }

    /**
     * 某个商户的所有门店的所有银联进件审核状态
     *
     * @param shopId
     * @return
     */
    public List<PayAuditStatusDto> findUnionpayAuditStatus(String shopId) {
        return dao.findUnionpayAuditStatus(shopId);
    }

	public Integer findrolecount(String userId,String enname){
		return erpstoreDao.findrolecount(userId, enname);
	}

	public ErpStoreInfo getStorePayInfo(String id) {
	    return erpstoreDao.getStorePayInfo(id);
	}
	
    public List<ErpFriendCheckApi> findfriendaudit(String shopId) {
        return erpstoreDao.findfriendaudit(shopId);
    }
	public ErpStoreInfo friendaudit(String storeId){
		return erpstoreDao.friendaudit(storeId);
	}
	
    /**
     * 符合陌陌提交审核list
     * 
     * @author yunnex
     * @version 2017-12-09
     */
	public List<ErpStoreInfo> findmomoaudit(String shopId){
		return erpstoreDao.findmomoaudit(shopId);		
	}
	public ErpStoreInfo momoaudit(String storeId){
		return erpstoreDao.momoaudit(storeId);
	}

	/**
     * 符合朋友圈提交审核list
     * 
     * @author yunnex
     * @version 2017-12-09
     */
    public List<ErpWeiboCheckApi> findweiboaudit(String shopId) {
        return erpstoreDao.findweiboaudit(shopId);
    }
	public ErpStoreInfo weiboaudit(String storeId){
		return erpstoreDao.weiboaudit(storeId);	
	}
	
    /*
     * ======================add by SunQ 2018-4-3 14:27:51 对于从OEM同步回来的门店信息,单独使用查询方法，防止与之前的业务发生冲突
     * start======================
     */
    public List<ErpStoreInfo> findwhereshopidListForOEM(String shopid, String del, String isMain) {
        return erpstoreDao.findwhereshopidListForOEM(shopid, del, isMain);
    }
    
    public List<ErpStoreInfo> findwxpayauditForOEM(String shopid, String del) {
        return erpstoreDao.findwxpayauditForOEM(shopid, del);
    }
    
    public List<ErpStoreInfo> findunionauditForOEM(String shopid, String del) {
        return erpstoreDao.findunionauditForOEM(shopid, del);
    }
    
    public ErpStoreInfo getWeiboPromotionInfobyProcInsId(String procInsId) {
        return erpstoreDao.getWeiboPromotionInfobyProcInsId(procInsId);
    }
    public ErpStoreInfo getFriendsPromotionInfobyProcInsId(String procInsId) {
        return erpstoreDao.getFriendsPromotionInfobyProcInsId(procInsId);
    }
    
    
    /**
     * 删除门店相关的信息(从OEM同步的)
     *
     * @date 2018年4月4日
     * @author SunQ
     */
    @Transactional(readOnly = false)
    public void deleteStoreInfo(String shopid) {
        // 获取商户从OEM同步的门店集合
        List<ErpStoreInfo> storeInfos = erpstoreDao.findwhereshopidListForOEM(shopid, Global.NO, null);
        if(CollectionUtils.isNotEmpty(storeInfos)){
            for(ErpStoreInfo storeInfo : storeInfos){
                
                if(StringUtils.isNotBlank(storeInfo.getLegalPersonId())){
                    ErpStoreLegalPerson erpStoreLegalPerson = erpStoreLegalPersondao.get(storeInfo.getLegalPersonId());
                    erpStoreLegalPersondao.delete(erpStoreLegalPerson);
                }
                
                if(StringUtils.isNotBlank(storeInfo.getCredentialsId())){
                    ErpStoreCredentials erpStoreCredentials = erpStoreCredentialsDao.get(storeInfo.getCredentialsId());
                    erpStoreCredentialsDao.delete(erpStoreCredentials);
                }
                
                if(StringUtils.isNotBlank(storeInfo.getId())){
                    ErpStoreLinkman erpStoreLinkman = erpStoreLinkmanDao.findWhereStoreId(storeInfo.getId(), Global.NO);
                    erpStoreLinkmanDao.delete(erpStoreLinkman);
                }
                
                if(StringUtils.isNotBlank(storeInfo.getWeixinPayId())){
                    ErpStorePayWeixin erpStorePayWeixin = erpStorePayWeixinDao.get(storeInfo.getWeixinPayId());
                    ErpStoreBank weixinBank = erpStoreBankDao.get(erpStorePayWeixin.getBankId());
                    erpStoreBankDao.delete(weixinBank);
                    erpStorePayWeixinDao.delete(erpStorePayWeixin);
                }
                
                if(StringUtils.isNotBlank(storeInfo.getUnionpayId())){
                    ErpStorePayUnionpay erpStorePayUnionpay = erpStorePayUnionpayDao.get(storeInfo.getUnionpayId());
                    ErpStoreBank unionBank = erpStoreBankDao.get(erpStorePayUnionpay.getBankId());
                    erpStoreBankDao.delete(unionBank);
                    erpStorePayUnionpayDao.delete(erpStorePayUnionpay);
                }
                
                erpstoreDao.delete(storeInfo);
            }
        }
    }
    
    public Integer countStoreForOEM(String shopid, String del) {
        return erpstoreDao.countStoreForOEM(shopid, del);
    }
    /*
     * ======================add by SunQ 2018-4-3 14:27:51 对于从OEM同步回来的门店信息,单独使用查询方法，防止与之前的业务发生冲突
     * end======================
     */

    @Transactional
    public void updateContactInfo(ErpStoreInfo storeInfo) {
        erpstoreDao.updateContactInfo(storeInfo);
    }

    public List<PublicAccountAndWeiboDto> findPublicAccountAndWeibo(List<String> storeInfoIds) {
        return erpstoreDao.findPublicAccountAndWeibo(storeInfoIds);
    }

    /**
     * 微信进件列表
     * 
     * @param shopid
     * @return
     */
    public ErpPayOpenApi wxpayOpen(String shopid) {
        ErpShopInfo shop = shopInfoService.get(shopid);
        List<ErpStoreInfo> store = findwxpayaudit(shopid, Global.NO);
        ErpPayOpenApi payopenApi = new ErpPayOpenApi();
        payopenApi.setShopid(shop.getId());
        payopenApi.setShopname(shop.getName());
        String rstr = "";
        String cno = "";
        if (null != store && !store.isEmpty()) {
            for (int i = 0; i < store.size(); i++) {
                if (null != store.get(i).getCredentials() && null != store.get(i).getWxPay() && null != store.get(i).getWxPay().getBank()) {
                    rstr = store.get(i).getCredentials().getRegisterNo();
                    cno = store.get(i).getWxPay().getBank().getCreditCardNo();
                    if (null != rstr && !"".equals(rstr) && (null != cno && !"".equals(cno))) {
                        if (rstr.length() >= FOUR && cno.length() >= FOUR) {
                            store.get(i).setPayOpenName("微信支付进件_" + rstr.substring(rstr.length() - FOUR) + "_" + cno.substring(cno.length() - FOUR));
                        } else {
                            if (rstr.length() >= FOUR) {
                                store.get(i).setPayOpenName("微信支付进件_" + rstr.substring(rstr.length() - FOUR) + "_" + cno);
                            } else {
                                store.get(i).setPayOpenName("微信支付进件_" + rstr + "_" + cno.substring(cno.length() - FOUR));
                            }

                        }
                    }
                }
            }
        }
        payopenApi.getStore().addAll(store);
        return payopenApi;
    }

    /**
     * 银联进件列表
     * 
     * @param shopid
     * @return
     */
    public ErpPayOpenApi unionOpen(String shopid) {
        ErpShopInfo shop = shopInfoService.get(shopid);
        List<ErpStoreInfo> store = findunionaudit(shopid, Global.NO);
        ErpPayOpenApi payopenApi = new ErpPayOpenApi();
        payopenApi.setShopid(shop.getId());
        payopenApi.setShopname(shop.getName());
        String rstr = "";
        String cno = "";
        if (null != store && !store.isEmpty()) {
            for (int i = 0; i < store.size(); i++) {
                if (null != store.get(i).getCredentials() && null != store.get(i).getUnionPay() && null != store.get(i).getUnionPay().getBank()) {
                    rstr = store.get(i).getCredentials().getRegisterNo();
                    cno = store.get(i).getUnionPay().getBank().getCreditCardNo();
                    if (null != rstr && !"".equals(rstr) && (null != cno && !"".equals(cno))) {
                        if (rstr.length() >= FOUR && cno.length() >= FOUR) {
                            store.get(i).setPayOpenName("银联支付进件_" + rstr.substring(rstr.length() - FOUR) + "_" + cno.substring(cno.length() - FOUR));
                        } else {
                            if (rstr.length() >= FOUR) {
                                store.get(i).setPayOpenName("银联支付进件_" + rstr.substring(rstr.length() - FOUR) + "_" + cno);
                            } else {
                                store.get(i).setPayOpenName("银联支付进件_" + rstr + "_" + cno.substring(cno.length() - FOUR));
                            }
                        }
                    }
                }
            }
        }
        payopenApi.getStore().addAll(store);
        return payopenApi;
    }


    /**
     * 新增修改 微信支付 信息
     *
     * @param eWxPayApi
     * @date 2018年6月5日
     * @author linqunzhi
     */
    @Transactional(readOnly = false)
    public void addwxpay(ErpWxPayApi eWxPayApi) {
        String eWxPayApiStr = JSON.toJSONString(eWxPayApi);
        logger.info("addwxpay start | eWxPayApi={}", eWxPayApiStr);
        if (eWxPayApi == null) {
            logger.error("eWxPayApi is null");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        String storeId = eWxPayApi.getStoreid();
        if (StringUtils.isBlank(storeId)) {
            logger.error("storeId 不能为空");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        ErpStoreInfo store = this.get(storeId);
        if (store == null) {
            logger.error("门店不存在! storeId={}", storeId);
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        ErpStorePayWeixin wxpay = new ErpStorePayWeixin();
        ErpStorePayWeixin wxpayParam = eWxPayApi.getWeixinPay();
        // 银行参数信息
        ErpStoreBank bankParam = null;
        // 微信支付信息装配
        if (wxpayParam != null) {
            bankParam = wxpayParam.getBank();
            String wxpayId = wxpayParam.getId();
            if (StringUtils.isNotBlank(wxpayId)) {
                wxpay = wxpayService.get(wxpayId);
            }
            wxpay.setBankId(wxpayParam.getBankId());
            wxpay.setProvideAccountInfo(wxpayParam.getProvideAccountInfo());
            wxpay.setPublicAccountNo(wxpayParam.getPublicAccountNo());
            // 公总号密码 加密
            String publicAccountPassword = wxpayParam.getPublicAccountPassword();
            if (StringUtils.isNotBlank(publicAccountPassword)) {
                publicAccountPassword = AESUtil.encrypt(publicAccountPassword);
            } else {
                publicAccountPassword = CommonConstants.Sign.EMPTY_STRING;
            }
            wxpay.setPublicAccountPassword(publicAccountPassword);
            wxpay.setPublicAccountAppid(wxpayParam.getPublicAccountAppid());
            wxpay.setPublicAccountName(wxpayParam.getPublicAccountName());
            wxpay.setAuditContent(wxpayParam.getAuditContent());
            wxpay.setEmailNo(wxpayParam.getEmailNo());
            // 邮箱密码加密
            String emailPassword = wxpayParam.getEmailPassword();
            if (StringUtils.isNotBlank(emailPassword)) {
                emailPassword = AESUtil.encrypt(emailPassword);
            }
            wxpay.setEmailPassword(emailPassword);
            wxpay.setOperatorIdcard(wxpayParam.getOperatorIdcard());
            wxpay.setOperatorEmail(wxpayParam.getOperatorEmail());
            wxpay.setOperatorMobile(wxpayParam.getOperatorMobile());
            wxpay.setOperatorName(wxpayParam.getOperatorName());
            wxpay.setWeixinNo(wxpayParam.getWeixinNo());
            wxpay.setMultiAccountApplicationForm(wxpayParam.getMultiAccountApplicationForm());
        } else {
            wxpay = new ErpStorePayWeixin();
        }
        wxpay.setSyncOem(ShopConstant.whether.NO);
        // 银行信息 装配
        ErpStoreBank bank = new ErpStoreBank();;
        if (null != bankParam) {
            String bankId = bankParam.getId();
            if (StringUtils.isNotBlank(bankId)) {
                bank = bankService.get(bankId);
            }
            bank.setBankId(bankParam.getBankId());
            bank.setAccountType(bankParam.getAccountType());
            bank.setBankName(bankParam.getBankName());
            bank.setAccountType(bankParam.getAccountType());
            bank.setOpenAccountLicence(bankParam.getOpenAccountLicence());
            bank.setOpenAccountName(bankParam.getOpenAccountName());
            bank.setBranchBankName(bankParam.getBranchBankName());
            bank.setCreditCardNo(bankParam.getCreditCardNo());
            bank.setBankNo(bankParam.getBankNo());
            bank.setZhangbeiBindCount(bankParam.getZhangbeiBindCount());
            bank.setProvince(bankParam.getProvince());
            bank.setProvinceName(bankParam.getProvinceName());
            bank.setCity(bankParam.getCity());
            bank.setCityName(bankParam.getCityName());
            bank.setArea(bankParam.getArea());
            bank.setAreaName(bankParam.getAreaName());
            bank.setCreditCardFrontPhoto(bankParam.getCreditCardFrontPhoto());
        }
        bank.setPayWay(0);
        bankService.save(bank);
        wxpay.setBankId(bank.getId());
        wxpayService.save(wxpay);
        store.setWeixinPayId(wxpay.getId());
        this.save(store);
        logger.info("addwxpay end");
    }
    
    public List<String> findTaskIdByStoreId(String storeId){
    	return erpstoreDao.findTaskIdByStoreId(storeId);
    }

	public void updateNotOpenUnionpayFlag(String storeId, String notOpenUnionpayFlag) {
		if(StringUtils.isBlank(storeId)) {
            throw new ServiceException("主门店ID不能为空");
		}
		
		if(StringUtils.isBlank(notOpenUnionpayFlag)) {
            throw new ServiceException("notOpenUnionpayFlag不能为空");
		}
		
		dao.updateNotOpenUnionpayFlag(storeId,notOpenUnionpayFlag);
	}

    /**
     * 业务定义：充值门店推广状态
     * 
     * @date 2018年9月27日
     * @author R/Q
     */
    public void resetExtension(String storeId) {
        dao.resetExtension(storeId);
    }
}
