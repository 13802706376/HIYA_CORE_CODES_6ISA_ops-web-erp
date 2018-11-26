/**
 * @date 2018年1月19日
 * @author liyuanxing
 */
package com.yunnex.ops.erp.common.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import com.yunnex.ops.erp.common.config.Global;

/**
 * @author liyuanxing
 * @date 2018年1月19日
 */
public class FileUploadUtil {

    private static final String STR2 = ".";
    private static final String IMAGE = "image";
    private static final String STR = "/";
    private static final String YYYY_MM_DD = "yyyy/MM/dd";

    private static final String IMG_EXTENSIONS_REG = "^.(gif|jpg|jpeg|png)$";

    /**
     * 构造方法
     * 
     * @date 2018年2月7日
     * @author liyuanxing
     */
    private FileUploadUtil(){

    }
    
    /**
     * 
     *
     * @param file
     * @return
     * @throws IOException
     * @date 2018年2月7日
     * @author liyuanxing
     */
    public static String saveFile(MultipartFile file) throws IOException {
        
        String originalName = file.getOriginalFilename();
        int lastDot = originalName.lastIndexOf(STR2);
        String extension = originalName.substring(lastDot).toLowerCase();

        String subDirName = "file";
        if (Pattern.matches(IMG_EXTENSIONS_REG, extension)) {
            subDirName = IMAGE;
        }

        String rootPath = Global.getUserfilesBaseDir();
        // 文件存放动态目录
        String extPath = "upload/" + subDirName + STR + new SimpleDateFormat(YYYY_MM_DD).format(new Date()) + STR;
        File dir = new File(rootPath + extPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = extPath + UUID.randomUUID() + extension;
        FileUtils.copyInputStreamToFile(file.getInputStream(), new File(rootPath + filePath));
        return filePath;
    }
    
    /**
     * 
     * @param @param file
     * @param @param rootPath
     * @param @param fileName 带后缀名
     * @param @return
     * @param @throws IOException
     * @return String 返回类型
     * @author Frank 营销平台部
     * @date
     * @throws
     */
    public static String saveFile(MultipartFile file, String fileName, Long shopId) throws IOException {
       
        String originalName = file.getOriginalFilename();
        int lastDot = originalName.lastIndexOf(STR2);
        String extension = originalName.substring(lastDot).toLowerCase();

        String subDirName = "file";
        if (Pattern.matches(IMG_EXTENSIONS_REG, extension)) {
            subDirName = IMAGE;
        }

        String rootPath = Global.getUserfilesBaseDir();
        // 文件存放动态目录
        String extPath = shopId + "/upload/" + subDirName + STR + new SimpleDateFormat(YYYY_MM_DD).format(new Date()) + STR;
        File dir = new File(rootPath + extPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = extPath + fileName;
        FileUtils.copyInputStreamToFile(file.getInputStream(), new File(rootPath + filePath));
        return filePath;
    }

    /**
     * 指定存放目录
     * 
     * @param file
     * @param rootPath
     * @param extPath
     * @return
     * @throws IOException
     */
    public static String saveFileByURL(MultipartFile file) throws IOException {

        String originalName = file.getOriginalFilename();
        int lastDot = originalName.lastIndexOf(STR2);
        String extension = originalName.substring(lastDot).toLowerCase();

        String rootPath = Global.getUserfilesBaseDir();
        // 文件存放动态目录
        File dir = new File(rootPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = UUID.randomUUID() + extension;
        FileUtils.copyInputStreamToFile(file.getInputStream(), new File(rootPath + filePath));
        return filePath;
    }

    public static String saveFile(HttpServletRequest request, MultipartFile file, long limit) throws IOException {
        long fileSize = file.getSize();
        if (-1 != limit && fileSize > limit)
            throw new IOException("文件不能大于" + (limit / 1024) + "KB");

        String originalName = file.getOriginalFilename();
        int lastDot = originalName.lastIndexOf(".");
        String extension = originalName.substring(lastDot).toLowerCase();

        String subDirName = "file";
        Pattern pattern = Pattern.compile(IMG_EXTENSIONS_REG, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(extension);
        if (matcher.matches()) {
            subDirName = "image";
        }

        String rootPath = Global.getUserfilesBaseDir();
        // 文件存放动态目录
        String extPath = "upload/" + subDirName + "/" + new SimpleDateFormat("yyyy/MM/dd").format(new Date()) + "/";
        File dir = new File(rootPath + extPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = extPath + UUID.randomUUID() + extension;
        FileUtils.copyInputStreamToFile(file.getInputStream(), new File(rootPath + filePath));
        return filePath;
    }
}
