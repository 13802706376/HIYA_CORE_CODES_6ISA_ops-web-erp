package com.yunnex.ops.erp.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http get请求下载文件 add by huangwei
 */
public class HttpGetUploadUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpGetUploadUtil.class);

    public static final int BYTE_SIZE = 1024;

    public static final int TIMEOUT_NUMBER = 30000;

    public static final int SUCCESS_CODE = 200;

    private static final String UA_KEY = "User-Agent";
    private static final String ACCEPT_ENCODING_KEY = "Accept-Encoding";
    private static final String ACCEPT_ENCODING_VALUE = "identity";
    private static final String UA_VALUE = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36";

    /**
     * 下载保存图片
     *
     * @param imageUrl 图片http地址
     * @param fileDisk 保存的路径
     */
    public static boolean saveImageToDisk(String imageUrl, String fileDisk, String fileName) {
        LOGGER.info("http get请求下载文件>>http请求地址: {};文件保存地址: {};文件名称: {};", imageUrl, fileDisk, fileName);

        InputStream inputStream = getInputStream(imageUrl);
        if (inputStream == null) {
            LOGGER.info("http get请求下载文件>>文件不存在：{}", imageUrl);
            return false;
        }
        byte[] data = new byte[BYTE_SIZE];
        int len = 0;
        FileOutputStream fileoutputStream = null;
        try {
            File dir = new File(fileDisk);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String saveDisk = dir + "/" + fileName;
            LOGGER.info("http get请求下载文件>>保存的目录地址: {} ", saveDisk);

            fileoutputStream = new FileOutputStream(saveDisk);
            while ((len = inputStream.read(data)) != -1) {
                fileoutputStream.write(data, 0, len);
            }
            fileoutputStream.flush();
            LOGGER.info("http get请求下载文件>>下载成功: {} ", saveDisk);
        } catch (Exception e) {
            LOGGER.error("http get请求下载文件>>下载失败: {} ", imageUrl, e);
            return false;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
            if (fileoutputStream != null) {
                try {
                    fileoutputStream.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
        return true;
    }

    /**
     * 获取服务端的数据,以InputStream形式返回
     *
     * @param imageUrl
     * @return
     */
    public static InputStream getInputStream(String imageUrl) {
        LOGGER.info("获取服务端的数据,以InputStream形式返回>>imageUrl: {}", imageUrl);
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(imageUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            // 设置连接网络超时时间
            httpURLConnection.setConnectTimeout(TIMEOUT_NUMBER);
            httpURLConnection.setDoInput(true);
            // 表示设置本次http请求使用的GET方式请求
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty(UA_KEY, UA_VALUE);
            httpURLConnection.setRequestProperty(ACCEPT_ENCODING_KEY, ACCEPT_ENCODING_VALUE);
            int responseCode = httpURLConnection.getResponseCode();
            if (SUCCESS_CODE == responseCode) {
                // 从服务器中获得一个输入流
                inputStream = httpURLConnection.getInputStream();
                long contentLengthLong = httpURLConnection.getContentLengthLong();
                LOGGER.info("获取服务端的数据,以InputStream形式返回>>imageUrl: {}，文件大小: {}bytes", imageUrl, contentLengthLong);
            }
        } catch (MalformedURLException e) {
            LOGGER.info("获取服务端的数据,以InputStream形式返回>>异常>>imageUrl: {}", imageUrl, e);
        } catch (IOException e) {
            LOGGER.info("获取服务端的数据,以InputStream形式返回>>异常>>imageUrl: {}", imageUrl, e);
        }
        return inputStream;
    }
}
