package com.yunnex.ops.erp.modules.workflow.flow.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.utils.AESUtil;
import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.common.utils.excel.FastExcel;
import com.yunnex.ops.erp.modules.file.entity.ErpFileInfo;
import com.yunnex.ops.erp.modules.file.service.ErpFileInfoService;
import com.yunnex.ops.erp.modules.promotion.dto.ZipOrderFileRequestDto;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserFriends;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserMomo;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserWeibo;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserFriendsService;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserMomoService;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserWeiboService;
import com.yunnex.ops.erp.modules.store.basic.entity.BusinessScope;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreCredentials;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreLinkman;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStorePromotePhotoMaterial;
import com.yunnex.ops.erp.modules.store.basic.service.BusinessScopeService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreCredentialsService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreLinkmanService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStorePromotePhotoMaterialService;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;
import com.yunnex.ops.erp.modules.workflow.store.excel.AdvFriends;
import com.yunnex.ops.erp.modules.workflow.store.excel.AdvMomo;
import com.yunnex.ops.erp.modules.workflow.store.excel.AdvWeibo;
import com.yunnex.ops.erp.modules.workflow.store.excel.FileName;
import com.yunnex.ops.erp.modules.workflow.store.excel.ZipPromotePhotoDto;

/**
 * 下载控制器
 * 
 * @author Ternence
 * @date 2016年7月22日
 */
@Controller
public class DownloadController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadController.class);
    private static final int ONE_KB = 1024;
    private static final String SEP = ";";

    @Value("${domain.erp.res}")
    private String resDomain;
    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private ErpStoreAdvertiserFriendsService erpStoreAdvertiserFriendsService;
    @Autowired
    private ErpStoreCredentialsService erpStoreCredentialsService;
    @Autowired
    private ErpStoreAdvertiserWeiboService erpStoreAdvertiserWeiboService;
    @Autowired
    private ErpStoreLinkmanService erpStoreLinkmanService;
    @Autowired
    private BusinessScopeService businessScopeService;
    @Autowired
    private ErpStoreAdvertiserMomoService erpStoreAdvertiserMomoService;
    @Autowired
    private ErpStorePromotePhotoMaterialService photoService;
    @Autowired
    private ErpFileInfoService erpFileInfoService;

    /**
     * 从永久目录下载资源
     * 
     * @param fileName
     * @param response
     * @date 2015年4月8日
     * @author Ternence
     * @throws Exception 
     */
    @RequestMapping(value = "download2", method = RequestMethod.GET)
    public void download2(@RequestParam("fileName") String fileName, HttpServletResponse response, HttpServletRequest request) throws Exception {
        downloadFromPath(FilenameUtils.concat(Global.USERFILES_BASE_URL + "/", fileName), response);
    }

    private static void downloadFromPath(String fileName, HttpServletResponse response) throws IOException {
        String rspName = fileName;
        File file = new File(rspName);
        if (!file.exists()) {
            // 如果文件不存在，不进行下载操作
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        int nLen = 0;
        byte[] buff = new byte[ONE_KB];
        OutputStream sos = null;

        FileInputStream fis = new FileInputStream(file);
        try {
            response.setContentLength((int) file.length());
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("name", file.getName());

            response.setHeader("Content-disposition", "attachment; filename*=utf-8'zh_cn'" + URLEncoder.encode(file.getName(), "UTF-8") + "");
            sos = response.getOutputStream();
            sos.flush();
            while ((nLen = fis.read(buff)) > 0) {
                sos.write(buff, 0, nLen);
            }
            sos.flush();
            sos.close();
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }
    
    /**
     * 下载任务相关文件
     *
     * @param dto
     * @param request
     * @param response
     * @return
     * @date 2018年5月17日
     */
    @RequestMapping("/download/orderFile")
    public @ResponseBody Map<String, Object> downloadOrderFile(@RequestBody ZipOrderFileRequestDto dto, HttpServletRequest request,
                    HttpServletResponse response) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("success", true);
        if (StringUtils.isBlank(dto.getShopName()) || StringUtils.isBlank(dto.getPromotionMaterialName())) {
            result.put("success", false);
            result.put("msg", "参数不合法");
            return result;
        }

        if (CollectionUtils.isEmpty(dto.getErpOrderFiles())) {
            result.put("success", false);
            result.put("msg", "没有资源需要打包下载");
            return result;
        }

        // 商户名称+文件资料
        String name = dto.getShopName() + "+" + dto.getPromotionMaterialName();
        LOGGER.info("下载：{}，入参：ZipOrderFileRequestDto = {}", name, dto);
        ZipOutputStream zos = null;
        try { // NOSONAR
            zos = new ZipOutputStream(response.getOutputStream(), Charset.forName("UTF-8"));
            setResponseHeader(request, response, name + ".zip");

            copy(zos, dto.getErpOrderFiles());
            zos.flush();
        } catch (Exception e) {
            result.put("success", false);
            result.put("msg", "下载失败!");
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    LOGGER.error("zip流关闭异常！", e);
                }
            }
        }
        LOGGER.info("下载：{}，结果：{}，数据：{}", name, result, dto.getErpOrderFiles());
        return result;
    }

    private void copy(ZipOutputStream zos, List<ErpOrderFile> erpOrderFiles) throws IOException {
        if (CollectionUtils.isNotEmpty(erpOrderFiles)) {
            for (ErpOrderFile erpOrderFile : erpOrderFiles) {
                copy(zos, getInputStream(erpOrderFile.getFilePath()), erpOrderFile.getFileName());
            }
        }
    }

    /**
     * 下载推广图片素材
     *
     * @param storeId
     * @param request
     * @param response
     * @return
     * @date 2018年5月18日
     */
    @RequestMapping("/download/promotePhotoMaterial")
    public @ResponseBody Map<String, Object> promotePhotoMaterial(String storeId, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("success", true);
        
        if (StringUtils.isBlank(storeId)) {
            result.put("success", false);
            result.put("msg", "参数不合法");
            return result;
        }
        
        ErpStoreInfo erpStoreInfo = erpStoreInfoService.findShopInfoByStoreId(Global.NO, storeId);
        if (erpStoreInfo == null) {
            LOGGER.error("-----------找不到该门店ID为{}的门店信息--------------", storeId);
            result.put("success", false);
            result.put("msg", "找不到该门店信息");
            return result;
        }

        String shopAbbreviation = erpStoreInfo.getShopAbbreviation();
        if (StringUtils.isBlank(shopAbbreviation)) {
            shopAbbreviation = "";
        }

        // 推广图片素材_【商户简称】_【下载日期】（例如：推广图片素材_萌芽食堂_20180508）
        String dowloadDate = DateUtils.formatDate(new Date(), "yyyyMMdd");
        String name = "推广图片素材_" + shopAbbreviation + "_" + dowloadDate;
        LOGGER.info("下载：{}，入参：storeId = {}", name, storeId);

        ErpStorePromotePhotoMaterial promotePhotoMaterial = photoService.findlistWhereStoreId(Global.NO, storeId);
        if (promotePhotoMaterial == null) {
            LOGGER.error("-----------找不到门店ID为{}的推广素材信息--------------", storeId);
            result.put("success", false);
            result.put("msg", "找不到该门店的推广素材");
            return result;
        }

        List<ZipPromotePhotoDto> list = getZipPhotoList(promotePhotoMaterial);

        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(response.getOutputStream(), Charset.forName("UTF-8"));
            setResponseHeader(request, response, name + ".zip");
            copyPromotePhotos(zos, list);
            zos.flush();
        } catch (Exception e) {
            result.put("success", false);
            result.put("msg", "下载失败!");
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    LOGGER.error("zip流关闭异常！", e);
                }
            }
        }
        LOGGER.info("下载：{}，结果：{}，数据：{}", name, result, list);
        return result;
    }

    private void copyPromotePhotos(ZipOutputStream zos, List<ZipPromotePhotoDto> list) throws IOException {
        if (CollectionUtils.isNotEmpty(list)) {
            for (ZipPromotePhotoDto promotePhoto : list) {
                copy(zos, getInputStream(promotePhoto.getPath()), promotePhoto.getFileName());
            }
        }

    }


    private List<ZipPromotePhotoDto> getZipPhotoList(ErpStorePromotePhotoMaterial promotePhotoMaterial) {
        if (promotePhotoMaterial == null) {
            return null;
        }

        List<ZipPromotePhotoDto> photoList = new ArrayList<>();
        ZipPromotePhotoDto dto = null;
        // 门店环境图
        String environmentPhoto = promotePhotoMaterial.getEnvironmentPhoto();
        if (StringUtils.isNotBlank(environmentPhoto)) {
            dto = new ZipPromotePhotoDto();
            String[] environmentPhotos = environmentPhoto.split(Constant.SEMICOLON);
            for (String path : environmentPhotos) {
                ErpFileInfo erpFileInfo = erpFileInfoService.findByPath(path);
                String name = erpFileInfo.getName();
                dto.setPath(path);
                dto.setFileName("门店环境图/" + name);
                photoList.add(dto);
            }
        }

        // 产品图
        String productPhoto = promotePhotoMaterial.getProductPhoto();
        if (StringUtils.isNotBlank(productPhoto)) {
            dto = new ZipPromotePhotoDto();
            String[] productPhotos = productPhoto.split(Constant.SEMICOLON);
            for (String path : productPhotos) {
                ErpFileInfo erpFileInfo = erpFileInfoService.findByPath(path);
                String name = erpFileInfo.getName();
                dto.setPath(path);
                dto.setFileName("产品图/" + name);
                photoList.add(dto);
            }
        }

        // 菜单或服务类目表
        String menuPhoto = promotePhotoMaterial.getMenuPhoto();
        if (StringUtils.isNotBlank(menuPhoto)) {
            dto = new ZipPromotePhotoDto();
            String[] menuPhotos = menuPhoto.split(Constant.SEMICOLON);
            for (String path : menuPhotos) {
                ErpFileInfo erpFileInfo = erpFileInfoService.findByPath(path);
                String name = erpFileInfo.getName();
                dto.setPath(path);
                dto.setFileName("菜单或服务类目表/" + name);
                photoList.add(dto);
            }
        }

        return photoList;
    }

    /**
     * 下载商户朋友圈推广开户资料
     *
     * @param storeId
     * @param request
     * @param response
     * @return
     * @date 2018年1月22日
     */
    @RequestMapping("/download/adv/friends")
    public @ResponseBody Map<String, Object> downloadStoreFriendsInfomationZhixiao(String storeId, 
                                     HttpServletRequest request, HttpServletResponse response) {
        String name = "商户朋友圈推广开户资料";
        LOGGER.info("下载：{}，入参：storeId = {}", name, storeId);
        Map<String, Object> result = Maps.newHashMap();
        result.put("success", true);
        ErpStoreAdvertiserFriends advertiserFriends = erpStoreAdvertiserFriendsService.getByStoreId(storeId);
        advertiserFriends = advertiserFriends != null ? advertiserFriends : new ErpStoreAdvertiserFriends();
        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
        storeInfo = storeInfo != null ? storeInfo : new ErpStoreInfo();
        ErpStoreCredentials credentials = erpStoreCredentialsService.get(storeInfo.getCredentialsId());
        credentials = credentials != null ? credentials : new ErpStoreCredentials();
        
        AdvFriends advFriends = new AdvFriends(advertiserFriends.getAccountOriginalId(), 
            advertiserFriends.getAdvertiserScreenshot(), advertiserFriends.getStoreScreenshot(), "");
        
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(response.getOutputStream(), Charset.forName("UTF-8"));
            setResponseHeader(request, response, name + ".zip");
            
            String advertiserScreenshot = advertiserFriends.getAdvertiserScreenshot();
            advFriends.setAdvertiserScreenshot(advertiserScreenshot);
            copy(zos, AdvFriends.class, advertiserScreenshot, "advertiserScreenshot");
            
            String storeScreenshot = advertiserFriends.getStoreScreenshot();
            advFriends.setStoreScreenshot(storeScreenshot);
            copy(zos, AdvFriends.class, storeScreenshot, "storeScreenshot");

            String specialCertificate = credentials.getSpecialCertificate();
            advFriends.setIndustryPhoto(specialCertificate);
            copy(zos, AdvFriends.class, specialCertificate, "industryPhoto");
            
            String excelName = name + ".xls";
            copy(zos, FastExcel.exportExcelPortrait(excelName, advFriends), excelName);
            
            zos.flush();
        } catch (Exception e) {
            result.put("success", false);
            result.put("msg", "下载失败!");
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    LOGGER.error("zip流关闭异常！", e);
                }
            }
        }
        LOGGER.info("下载：{}，结果：{}，数据：{}", name, result, advFriends);
        return result;
    }
    
    /**
     * 下载商户微博推广开户资料
     *
     * @param storeId
     * @param request
     * @param response
     * @return
     * @date 2018年1月22日
     */
    @RequestMapping("/download/adv/weibo")
    public @ResponseBody Map<String, Object> downloadStoreWeiboInfomationZhixiao(String storeId, 
                                HttpServletRequest request, HttpServletResponse response) {
        String name = "商户微博推广开户资料";
        LOGGER.info("下载：{}，入参：storeId = {}", name, storeId);
        Map<String, Object> result = Maps.newHashMap();
        result.put("success", true);
        ErpStoreAdvertiserWeibo advertiserWeibo = erpStoreAdvertiserWeiboService.getByStoreId(storeId);
        advertiserWeibo = advertiserWeibo != null ? advertiserWeibo : new ErpStoreAdvertiserWeibo();
        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
        storeInfo = storeInfo != null ? storeInfo : new ErpStoreInfo();
        ErpStoreCredentials credentials = erpStoreCredentialsService.get(storeInfo.getCredentialsId());
        credentials = credentials != null ? credentials : new ErpStoreCredentials();
        ErpStoreLinkman linkMan = erpStoreLinkmanService.findWhereStoreId("0", storeId);
        linkMan = linkMan != null ? linkMan : new ErpStoreLinkman();
        List<BusinessScope> scopelist = businessScopeService.findAllList();
        
        AdvWeibo advWeibo = new AdvWeibo();
        advWeibo.setAccountNo(advertiserWeibo.getAccountNo());
        advWeibo.setAccountPassword(AESUtil.decrypt(advertiserWeibo.getAccountPassword()));
        advWeibo.setNickName(advertiserWeibo.getNickName());
        advWeibo.setUid(advertiserWeibo.getUid());
        
        advWeibo.setRegisterName(credentials.getRegisterName());
        advWeibo.setRegisterNo(credentials.getRegisterNo());
        if (credentials.getStartDate() != null && credentials.getEndDate() != null) {
            advWeibo.setValidDate(DateUtils.formatDate(credentials.getStartDate()) + " 至 " + DateUtils.formatDate(credentials.getEndDate()));
        }
        StringBuilder sb = new StringBuilder();
        if (CollectionUtils.isNotEmpty(scopelist) && credentials.getBusinessScope() != null) {
            for (BusinessScope businessScope : scopelist) {
                if (businessScope.getId().equals(Integer.toString(credentials.getBusinessScope()))) {
                    sb.append(businessScope.getText()).append(",");
                }
            }
        }
        String bizScope = sb.toString();
        if (bizScope.endsWith(",")) {
            bizScope = bizScope.substring(0, bizScope.lastIndexOf(','));
        }
        advWeibo.setBusinessScope(bizScope);
        
        advWeibo.setName(linkMan.getName());
        advWeibo.setPhone(linkMan.getPhone());
        advWeibo.setEmail(linkMan.getEmail());
        advWeibo.setAddress(linkMan.getAddress());
        
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(response.getOutputStream(), Charset.forName("UTF-8"));
            setResponseHeader(request, response, name + ".zip");
            
            String relationProveLetter = advertiserWeibo.getRelationProveLetter();
            advWeibo.setRelationProveLetter(relationProveLetter);
            copy(zos, AdvWeibo.class, relationProveLetter, "relationProveLetter");
            
            String advAuthLetter = advertiserWeibo.getAdvAuthLetter();
            advWeibo.setAdvAuthLetter(advAuthLetter);
            copy(zos, AdvWeibo.class, advAuthLetter, "advAuthLetter");
            
            String promotePromiseLetter = advertiserWeibo.getPromotePromiseLetter();
            advWeibo.setPromotePromiseLetter(promotePromiseLetter);
            copy(zos, AdvWeibo.class, promotePromiseLetter, "promotePromiseLetter");
            
            String businessLicence = credentials.getBusinessLicence();
            advWeibo.setBusinessLicence(businessLicence);
            copy(zos, AdvWeibo.class, businessLicence, "businessLicence");
            
            String specialCertificate = credentials.getSpecialCertificate();
            advWeibo.setSpecialCertificate(specialCertificate);
            copy(zos, AdvWeibo.class, specialCertificate, "specialCertificate");
            
            String excelName = name + ".xls";
            copy(zos, FastExcel.exportExcelPortrait(excelName, advWeibo), excelName);
            
            zos.flush();
        } catch (Exception e) {
            result.put("success", false);
            result.put("msg", "下载失败!");
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    LOGGER.error("zip流关闭异常！", e);
                }
            }
        }
        LOGGER.info("下载：{}，结果：{}，数据：{}", name, result, advWeibo);
        return result;
    }
    
    /**
     * 下载商户陌陌推广开户资料
     *
     * @param storeId
     * @return
     * @date 2018年1月22日
     */
    @RequestMapping("/download/adv/momo")
    public @ResponseBody Map<String, Object> downloadStoreMomoInfomationZhixiao(String storeId, 
                                     HttpServletRequest request, HttpServletResponse response) {
        String name = "商户陌陌推广开户资料";
        LOGGER.info("下载：{}，入参：storeId = {}", name, storeId);
        Map<String, Object> result = Maps.newHashMap();
        result.put("success", true);
        ErpStoreAdvertiserMomo advertiserMomo = erpStoreAdvertiserMomoService.getByStoreId(storeId);
        advertiserMomo = advertiserMomo != null ? advertiserMomo : new ErpStoreAdvertiserMomo();
        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
        storeInfo = storeInfo != null ? storeInfo : new ErpStoreInfo();
        ErpStoreCredentials credentials = erpStoreCredentialsService.get(storeInfo.getCredentialsId());
        credentials = credentials != null ? credentials : new ErpStoreCredentials();
        ErpStoreLinkman linkMan = erpStoreLinkmanService.findWhereStoreId("0", storeId);
        linkMan = linkMan != null ? linkMan : new ErpStoreLinkman();
        
        AdvMomo advMomo = new AdvMomo();
        advMomo.setAccountNo(advertiserMomo.getAccountNo());
        advMomo.setBrandName(advertiserMomo.getBrandName());
        if (advertiserMomo.getExpectedDeliveryTime() != null) {
            advMomo.setExpectedDeliveryTime(DateUtils.formatDate(advertiserMomo.getExpectedDeliveryTime()));
        }
        advMomo.setDeliveryUrl(advertiserMomo.getDeliveryUrl());
        
        advMomo.setRegisterName(credentials.getRegisterName());
        advMomo.setRegisterNo(credentials.getRegisterNo());
        
        advMomo.setName(linkMan.getName());
        advMomo.setPhone(linkMan.getPhone());
        
        advMomo.setProductName(storeInfo.getProductName());
        advMomo.setProductConcreteInfo(storeInfo.getProductConcreteInfo());
        
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(response.getOutputStream(), Charset.forName("UTF-8"));
            setResponseHeader(request, response, name + ".zip");
            
            String icpMessage = storeInfo.getIcpMessage();
            advMomo.setIcpMessage(icpMessage);
            copy(zos, AdvMomo.class, icpMessage, "icpMessage");
            
            String businessLicence = credentials.getBusinessLicence();
            advMomo.setBusinessLicence(businessLicence);
            copy(zos, AdvMomo.class, businessLicence, "businessLicence");
            
            String specialCertificate = credentials.getSpecialCertificate();
            advMomo.setSpecialCertificate(specialCertificate);
            copy(zos, AdvMomo.class, specialCertificate, "specialCertificate");
            
            String followZhangbeiScreenshot = advertiserMomo.getFollowZhangbeiScreenshot();
            advMomo.setFollowZhangbeiScreenshot(followZhangbeiScreenshot);
            copy(zos, AdvMomo.class, followZhangbeiScreenshot, "followZhangbeiScreenshot");
            
            String deliveryPic = advertiserMomo.getDeliveryPic();
            advMomo.setDeliveryPic(deliveryPic);
            copy(zos, AdvMomo.class, deliveryPic, "deliveryPic");
            
            String copywritingPlan = advertiserMomo.getCopywritingPlan();
            advMomo.setCopywritingPlan(copywritingPlan);
            copy(zos, AdvMomo.class, copywritingPlan, "copywritingPlan");
            
            String excelName = name + ".xls";
            copy(zos, FastExcel.exportExcelPortrait(excelName, advMomo), excelName);
            
            zos.flush();
        } catch (Exception e) {
            result.put("success", false);
            result.put("msg", "下载失败!");
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    LOGGER.error("zip流关闭异常！", e);
                }
            }
        }
        LOGGER.info("下载：{}，结果：{}，数据：{}", name, result, advMomo);
        return result;
    }
    
    public void setResponseHeader(HttpServletRequest request, HttpServletResponse response, String downloadFilename) throws UnsupportedEncodingException {
        response.setContentType("application/octet-stream;charset=utf-8");// 指明response的返回对象是文件流
        String userAgent = request.getHeader("User-Agent");  
        String downloadName = "";
        // 针对IE或者以IE为内核的浏览器：  
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {  
            downloadName = URLEncoder.encode(downloadFilename, "UTF-8");
        } else {  
            // 非IE浏览器的处理：  
            downloadName = new String(downloadFilename.getBytes("UTF-8"), "ISO-8859-1");
        }  
        response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", downloadName));
        response.setCharacterEncoding("UTF-8");
    }

    private void copy(ZipOutputStream zos, Class<?> clazz, String filePaths, String fieldName) throws NoSuchFieldException, IOException {
        if (zos == null || clazz == null || StringUtils.isBlank(filePaths) || fieldName == null) {
            return;
        }

        filePaths = removeSep(filePaths, SEP);

        // 将图片保存到压缩文件中
        String[] paths = filePaths.split(SEP);
        String order = "";
        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];
            if (i > 0) {
                order = String.valueOf(i);
            }
            copy(zos, getInputStream(path), getFileName(path, clazz, fieldName, order));
        }
    }

    // 去除首尾空格和分号";"
    private String removeSep(String filePaths, String sep) {
        if (StringUtils.isBlank(filePaths)) {
            return "";
        }

        for (int i = 0; i < filePaths.length(); i++) {
            filePaths = filePaths.trim();
            if (filePaths.startsWith(sep)) {
                filePaths = filePaths.substring(1, filePaths.length());
            } else {
                break;
            }
        }

        for (int i = 0; i < filePaths.length(); i++) {
            filePaths = filePaths.trim();
            if (filePaths.endsWith(sep)) {
                filePaths = filePaths.substring(0, filePaths.length() - 1);
            } else {
                break;
            }
        }

        return filePaths;
    }

    public String getFileName(String filePath, Class<?> clazz, String field, String order) throws NoSuchFieldException {
        if (StringUtils.isBlank(filePath)) return null;
        String ext = FilenameUtils.getExtension(filePath);
        return clazz.getDeclaredField(field).getAnnotation(FileName.class).value() + order + "." + ext;
    }
    
    public InputStream getInputStream(String filePath) throws IOException {
        if (StringUtils.isBlank(filePath)) return null;
        URL url = new URL(resDomain + filePath);
        return url.openConnection().getInputStream();
    }

    public void copy(ZipOutputStream zos, InputStream inputStream, String fileName) {
        if (zos == null || inputStream == null || StringUtils.isBlank(fileName)) {
            return;
        }
        try {
            zos.putNextEntry(new ZipEntry(fileName));
            byte[] buff = new byte[ONE_KB];
            int len = 0;
            while ((len = inputStream.read(buff)) != -1) {
                zos.write(buff, 0, len);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
    
}
