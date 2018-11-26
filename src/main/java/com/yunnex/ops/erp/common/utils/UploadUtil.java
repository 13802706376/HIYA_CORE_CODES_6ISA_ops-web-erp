package com.yunnex.ops.erp.common.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.modules.file.entity.ErpFileInfo;
import com.yunnex.ops.erp.modules.file.service.ErpFileInfoService;

@Component
public class UploadUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadUtil.class);

    @Autowired
    private ErpFileInfoService erpFileInfoService;

    // 最大文件大小，10M
    private static final long MAX_SIZE = Long.parseLong(Global.getMaxUploadSize());
    // 定义允许上传的文件扩展名
    private static final List<String> extList;
    // 基础路径
    private static final String BASE_DIR = Global.getUserfilesBaseDir();
    private static final String RES_DOMAIN = Global.getResDomain();
    private static final String OEM_DOMAIN = Global.getOemDomain();
    private static final String UPLOAD_DIR = "/upload";
    // 门店资料存储目录
    private static final String STORE_DIR = "/erp_store";

    private static final String TOO_BIG = "文件过大";
    private static final String WRONG_FROMAT = "不支持的格式";
    private static final String UPLOAD_FAIL = "上传失败";


    static {
        extList = Arrays.asList("gif,jpg,jpeg,png,bmp,doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2".split(","));
    }

    /**
     * 上传门店相关文件，保存在 store 目录下
     */
    public UploadResult uploadStoreFiles(MultipartFile... files) {
        return uploadFiles(files);
    }

    public UploadResult uploadFiles(MultipartFile... files) {
        UploadResult result = new UploadResult();
        if (files == null) {
            return result;
        }
        for (MultipartFile file : files) {
            if (validate(file, result)) {
                uploadFile(file, result);
            }
        }
        // 上传成功了多少
        int successSize = result.getSuccessUrls().size();
        if (successSize == files.length) { // 全部成功
            result.setCode(0);
        } else if (successSize < files.length && successSize >= 1) { // 如果全部只有一个文件，不会进来这里
            result.setCode(1);
        }
        // 否则全部失败，默认
        return result;
    }

    private boolean validate(MultipartFile file, UploadResult result) {
        String filename = file.getOriginalFilename();
        if (file.getSize() > MAX_SIZE) {
            result.getErrorMsgs().add(filename + ", " + TOO_BIG);
            return false;
        }

        String ext = getFileExt(filename);
        if (!extList.contains(ext)) {
            result.getErrorMsgs().add(filename + ", " + WRONG_FROMAT);
            return false;
        }

        return true;
    }

    private static String getFileExt(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1, filename.length());
    }

    private void uploadFile(MultipartFile file, UploadResult result) {
        String filename = file.getOriginalFilename();
        try {
            String dateDir = getDateDir();
            String baseDir = UPLOAD_DIR + STORE_DIR + "/" + dateDir + "/";
            String savePath = BASE_DIR + baseDir; // 存放目录
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            String ext = getFileExt(filename);
            String generateFilename = IdGen.uuid() + "." + ext;
            String filePath = baseDir + generateFilename;
            // 保存到本地
            String pathName = BASE_DIR + filePath;
            LOGGER.info("本地path={}", pathName);
            file.transferTo(new File(pathName));
            // 保存到OEM
            JSONObject jsonObject = toOEM(RES_DOMAIN + filePath, baseDir, generateFilename);
            LOGGER.info("oem返回 : {}", jsonObject);
            if (jsonObject == null || !"0000".equals(jsonObject.getString("code"))) {
                File del = new File(pathName);
                del.delete();
                LOGGER.info("oem 失败，删除文件：{}", pathName);
                result.getErrorMsgs().add(filename + ",null ");
            } else {
                // 保存文件信息到数据库
                ErpFileInfo erpFileInfo = new ErpFileInfo(filename, ext, file.getSize(), filePath);
                erpFileInfoService.save(erpFileInfo);
                result.getSuccessUrls().add(filePath);
            }
        } catch (RuntimeException | IOException e) {
            LOGGER.error(e.getMessage(), e);
            result.getErrorMsgs().add(filename + ", " + UPLOAD_FAIL);
        }
    }

    public static JSONObject toOEM(String imgUrl, String fileDir, String fileName) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            JSONObject params = new JSONObject();
            params.put("imgUrl", imgUrl);
            params.put("fileDir", fileDir);
            params.put("fileName", fileName);
            LOGGER.info("imgUrl : {}, fileDir : {}, fileName : {}", imgUrl, fileDir, fileName);

            HttpPost post = new HttpPost(OEM_DOMAIN + "/marketing/erpImageUpload");
            post.addHeader("Content-type", "application/json; charset=utf-8");
            post.setHeader("Accept", "application/json");
            post.setEntity(new StringEntity(params.toJSONString(), "UTF-8"));
            CloseableHttpResponse response = httpClient.execute(post);
            Integer num=200;
            if (response.getStatusLine().getStatusCode() == num) {
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                JSONObject jsonObject = JSON.parseObject(result);
                return jsonObject;
            }
        } catch (RuntimeException | IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return null;
    }

    private static String getDateDir() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date());
    }

    public static class UploadResult {
        // -1：全部失败，0: 全部成功，1：部分成功，部分失败
        private int code = -1;
        private List<String> successUrls = new ArrayList<String>();
        private List<String> errorMsgs = new ArrayList<String>();

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public List<String> getSuccessUrls() {
            return successUrls;
        }

        public void setSuccessUrls(List<String> successUrls) {
            this.successUrls = successUrls;
        }

        public List<String> getErrorMsgs() {
            return errorMsgs;
        }

        public void setErrorMsgs(List<String> errorMsgs) {
            this.errorMsgs = errorMsgs;
        }
    }

}
