package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yunnex.ops.erp.common.persistence.BaseEntity;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.result.IllegalArgumentErrorResult;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.AESUtil;
import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.utils.excel.ImportExcel;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserMomo;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserWeibo;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserMomoService;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserWeiboService;
import com.yunnex.ops.erp.modules.store.basic.entity.BusinessScope;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreCredentials;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreLinkman;
import com.yunnex.ops.erp.modules.store.basic.service.BusinessScopeService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreCredentialsService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreLinkmanService;

@Service
public class JykFlowStoreInfoService {

    private final Logger LOGGER = LoggerFactory.getLogger(JykFlowStoreInfoService.class);

    private static final String TO = "至";

    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;
    private static final int SIX = 6;
    private static final int SEVEN = 7;
    private static final int NINE = 9;
    private static final int TEN = 10;
    private static final int TWELVE = 12;
    private static final int THIRTEEN = 13;

    @Autowired
    private ErpStoreInfoService storeInfoService;
    @Autowired
    private ErpStoreAdvertiserWeiboService weiboService;
    @Autowired
    private ErpStoreCredentialsService credentialsService;
    @Autowired
    private BusinessScopeService businessScopeService;
    @Autowired
    private ErpStoreLinkmanService erpStoreLinkmanService;
    @Autowired
    private ErpStoreAdvertiserMomoService momoService;


    /**
     * 上传微博推广开户资料（Excel）
     *
     * @param file
     * @param storeId
     * @return
     */
    @Transactional
    public BaseResult uploadStoreWeiboInfomationService(MultipartFile file, String storeId) {
        LOGGER.info("上传微博推广开户资料（Excel）入参：storeId = {}", storeId);
        if (StringUtils.isBlank(storeId)) {
            throw new ServiceException("门店ID不能为空！");
        }

        // 获取Excel数据
        BaseResult result = getExcelData(file, FIVE, 19, FOUR);
        if (!BaseResult.isSuccess(result)) {
            return result;
        }
        List<Object> list = (List<Object>) result.getAttach();
        LOGGER.info("上传微博推广开户资料（Excel）数据：data = {}", list);

        ErpStoreInfo erpStoreInfo = storeInfoService.get(storeId);
        if (erpStoreInfo == null) {
            return new IllegalArgumentErrorResult("不存在此门店！");
        }

        // 保存微博信息
        ErpStoreAdvertiserWeibo weibo = saveWeibo(list, erpStoreInfo.getAdvertiserWeiboId());
        erpStoreInfo.setAdvertiserWeiboId(weibo != null ? weibo.getId() : null);

        // 保存营业执照信息
        ErpStoreCredentials credentials = saveCredentialsWeibo(list, erpStoreInfo.getCredentialsId());
        erpStoreInfo.setCredentialsId(credentials != null ? credentials.getId() : null);

        // 保存联系人信息
        saveLinkmanWeibo(list, erpStoreInfo.getId());

        // 保存门店的外键信息
        storeInfoService.save(erpStoreInfo);

        return result;
    }

    /**
     * 保存联系人信息（微博）
     * 
     * @param list
     * @param storeId
     */
    private void saveLinkmanWeibo(List<Object> list, String storeId) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        ErpStoreLinkman linkman = null;
        if (StringUtils.isNotBlank(storeId)) {
            linkman = erpStoreLinkmanService.findWhereStoreId(BaseEntity.DEL_FLAG_NORMAL, storeId);
        }
        if (linkman == null) {
            linkman = new ErpStoreLinkman();
        }

        Object o = null;
        o = list.get(NINE);
        if (null != o) {
            linkman.setName(o.toString());
        }
        o = list.get(TEN);
        if (null != o) {
            linkman.setPhone(o.toString());
        }
        // 读取联系人通讯地址而不是所在地
        o = list.get(TWELVE);
        if (null != o) {
            linkman.setAddress(o.toString());
        }
        o = list.get(THIRTEEN);
        if (null != o) {
            linkman.setEmail(o.toString());
        }

        erpStoreLinkmanService.save(linkman);
    }

    /**
     * 保存营业执照信息（微博）
     *
     * @param list
     * @param credentialsId
     */
    private ErpStoreCredentials saveCredentialsWeibo(List<Object> list, String credentialsId) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        ErpStoreCredentials credentials = null;
        if (StringUtils.isNotBlank(credentialsId)) {
            credentials = credentialsService.get(credentialsId);
        }
        if (credentials == null) {
            credentials = new ErpStoreCredentials();
        }

        Object o = null;
        o = list.get(FOUR);
        if (null != o) {
            credentials.setRegisterName(o.toString());
        }
        o = list.get(FIVE);
        if (null != o) {
            credentials.setRegisterNo(o.toString());
        }
        // 日期，导入格式：yyyy-MM-dd到yyyy-MM-dd
        o = list.get(SIX);
        if (null != o) {
            String period = o.toString();
            String[] dates = period.split(TO);
            if (dates.length > ZERO) {
                credentials.setStartDate(parseDate(dates[ZERO]));
            }
            if (dates.length > ONE) {
                credentials.setEndDate(parseDate(dates[ONE]));
            }
        }
        o = list.get(SEVEN);
        if (null != o) {
            String scope = o.toString();
            BusinessScope businessScope = businessScopeService.findByText(scope);
            if (businessScope == null) {
                throw new ServiceException("不存在此经营范围：" + scope);
            }
            credentials.setBusinessScope(Integer.parseInt(businessScope.getId()));
        }
        // 不读所属行业
        credentialsService.save(credentials);

        return credentials;
    }

    private Date parseDate(String date) {
        Date result = null;
        try {
            result = DateUtils.parseDateWithException(date);
        } catch (ParseException e) {
            LOGGER.error("日期解析出错", e);
            // 转换异常，添加提示信息
            throw new ServiceException("日期解析出错：" + date);
        }
        return result;
    }

    /**
     * 保存微博信息
     *
     * @param list
     * @param weiboId
     */
    private ErpStoreAdvertiserWeibo saveWeibo(List<Object> list, String weiboId) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        ErpStoreAdvertiserWeibo weibo = null;
        if (StringUtils.isNotBlank(weiboId)) {
            weibo = weiboService.get(weiboId);
        }
        if (weibo == null) {
            weibo = new ErpStoreAdvertiserWeibo();
        }

        Object o = null;
        o = list.get(ZERO);
        if (null != o) {
            weibo.setAccountNo(o.toString());
        }
        o = list.get(ONE);
        if (null != o) {
            weibo.setAccountPassword(AESUtil.encrypt(o.toString()));
        }
        o = list.get(TWO);
        if (null != o) {
            weibo.setNickName(o.toString());
        }
        o = list.get(THREE);
        if (null != o) {
            weibo.setUid(o.toString());
        }

        weiboService.save(weibo);
        return weibo;
    }

    /**
     * 上传陌陌推广开户资料（Excel）
     * 
     * @param file
     * @param storeId
     * @return
     */
    @Transactional
    public BaseResult uploadStoreMomoInfomationService(MultipartFile file, String storeId) {
        LOGGER.info("上传陌陌推广开户资料（Excel）入参：storeId = {}", storeId);
        if (StringUtils.isBlank(storeId)) {
            throw new ServiceException("门店ID不能为空！");
        }

        // 获取Excel数据
        BaseResult result = getExcelData(file, 22, 29, FOUR);
        if (!BaseResult.isSuccess(result)) {
            return result;
        }
        List<Object> list = (List<Object>) result.getAttach();
        LOGGER.info("上传陌陌推广开户资料（Excel）数据：data = {}", list);

        ErpStoreInfo erpStoreInfo = storeInfoService.get(storeId);
        if (erpStoreInfo == null) {
            return new IllegalArgumentErrorResult("不存在此门店！");
        }

        // 保存陌陌信息
        ErpStoreAdvertiserMomo momo = saveMomo(list, erpStoreInfo.getAdvertiserMomoId());
        if (momo != null) {
            erpStoreInfo.setAdvertiserMomoId(momo.getId());
        }

        // 保存营业执照信息
        ErpStoreCredentials credentials = saveCredentialsMomo(list, erpStoreInfo.getCredentialsId());
        if (credentials != null) {
            erpStoreInfo.setCredentialsId(credentials.getId());
        }

        // 保存陌陌信息
        saveLinkmanMomo(list, storeId);

        // 保存门店的外键信息
        storeInfoService.save(erpStoreInfo);

        return result;
    }

    /**
     * 保存陌陌信息
     * 
     * @param list
     * @param momoId
     * @return
     */
    private ErpStoreAdvertiserMomo saveMomo(List<Object> list, String momoId) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        ErpStoreAdvertiserMomo momo = null;
        if (StringUtils.isNotBlank(momoId)) {
            momo = momoService.get(momoId);
        }
        if (momo == null) {
            momo = new ErpStoreAdvertiserMomo();
        }

        Object o = null;
        o = list.get(ZERO);
        if (null != o) {
            momo.setAccountNo(o.toString());
        }
        o = list.get(TWO);
        if (null != o) {
            momo.setBrandName(o.toString());
        }
        o = list.get(FOUR);
        if (null != o) {
            momo.setIcp(o.toString());
        }

        momoService.save(momo);
        return momo;
    }

    /**
     * 保存营业执照信息（陌陌）
     * 
     * @param list
     * @param credentialsId
     * @return
     */
    private ErpStoreCredentials saveCredentialsMomo(List<Object> list, String credentialsId) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        ErpStoreCredentials credentials = null;
        if (StringUtils.isNotBlank(credentialsId)) {
            credentials = credentialsService.get(credentialsId);
        }
        if (credentials == null) {
            credentials = new ErpStoreCredentials();
        }

        Object o = null;
        o = list.get(ONE);
        if (null != o) {
            credentials.setRegisterName(o.toString());
        }
        o = list.get(THREE);
        if (null != o) {
            credentials.setRegisterNo(o.toString());
        }

        credentialsService.save(credentials);
        return credentials;
    }

    /**
     * 保存联系人信息（陌陌）
     *
     * @param list
     * @param storeId
     */
    private void saveLinkmanMomo(List<Object> list, String storeId) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        ErpStoreLinkman linkman = null;
        if (StringUtils.isNotBlank(storeId)) {
            linkman = erpStoreLinkmanService.findWhereStoreId(BaseEntity.DEL_FLAG_NORMAL, storeId);
        }
        if (linkman == null) {
            linkman = new ErpStoreLinkman();
        }

        Object o = null;
        o = list.get(FIVE);
        if (null != o) {
            linkman.setName(o.toString());
        }
        o = list.get(SIX);
        if (null != o) {
            linkman.setPhone(o.toString());
        }

        erpStoreLinkmanService.save(linkman);
    }

    /**
     * 解析Excel数据
     *
     * @param file
     * @return
     */
    public BaseResult getExcelData(MultipartFile file, int startRow, int endRow, int column) {
        if (file == null) {
            return new IllegalArgumentErrorResult("请上传Excel文件！");
        }

        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            return new IllegalArgumentErrorResult("文件名不能为空！");
        }

        InputStream is = null;
        try {
            is = file.getInputStream();
        } catch (IOException e) {
            String msg = "Excel读取失败！";
            LOGGER.error(msg, e);
            return new IllegalArgumentErrorResult(msg);
        }

        String suffix = FilenameUtils.getExtension(originalFilename);
        BaseResult result = ImportExcel.readOneColumn(is, suffix, startRow, endRow, column);

        return result;
    }

}
