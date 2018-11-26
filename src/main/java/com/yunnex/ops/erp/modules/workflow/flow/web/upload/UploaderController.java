package com.yunnex.ops.erp.modules.workflow.flow.web.upload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.workflow.data.entity.JykDataPresentation;
import com.yunnex.ops.erp.modules.workflow.data.service.JykDataPresentationService;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpOrderFileService;


/**
 * 图片文件上传辅助类
 *
 */
@Controller
@RequestMapping(value = "${adminPath}")
public class UploaderController extends BaseController {
    // 输出文件地址
    protected String url = "";// NOSONAR
    // 上传文件名
    protected String fileName = "";// NOSONAR
    // 状态
    protected String state = "";// NOSONAR
    // 文件类型
    protected String type = "";// NOSONAR
    // 原始文件名
    protected String originalName = "";// NOSONAR
    // 文件大小
    protected String size = "";// NOSONAR
    protected String title = "";// NOSONAR
    // 保存路径
    protected String savePath = "";// NOSONAR
    // 文件允许格式
    protected String[] allowFiles = {".gif", ".png", ".jpg", ".jpeg", ".bmp"};// NOSONAR
    
    @Autowired
    protected ErpOrderFileService erpOrderFileService;
    @Autowired
    protected JykDataPresentationService jykDataPresentationService;
    @Autowired
    protected ErpOrderSplitInfoService erpOrderSplitInfoService;


    private HashMap<String, String> errorInfo = new HashMap<String, String>();//NOSONAR
    
    private static Logger logger = LoggerFactory.getLogger(UploaderController.class);

    public UploaderController() {
        HashMap<String, String> tmp = this.errorInfo;
        tmp.put("SUCCESS", "SUCCESS"); // 默认成功
        tmp.put("NOFILE", "未包含文件上传域");
        tmp.put("TYPE", "不允许的文件格式");
        tmp.put("SIZE", "文件大小不能超出10M");
        tmp.put("ENTYPE", "请求类型ENTYPE错误");
        tmp.put("REQUEST", "上传请求异常");
        tmp.put("IO", "IO异常");
        tmp.put("DIR", "目录创建失败");
        tmp.put("UNKNOWN", "未知错误");
        tmp.put("PARAMETERERROR", "参数错误");
    }

    @RequestMapping(value = "workfile/file/upload", method = RequestMethod.POST)
    @ResponseBody
    public Object upload(@RequestParam(required = false) MultipartFile upfile, @RequestParam(required = false) String procInsId,
                    @RequestParam(required = false) String fileTitle, HttpServletRequest request, Model model) {
        Response response = new Response();
        this.savePath = procInsId;
        if (upfile.isEmpty()) {
            this.state = this.errorInfo.get("NOFILE");
            return response;
        } else {
            long fileSize = upfile.getSize();
            // 上传文件大小判断
            if (fileSize > 52428800) {
                this.state = this.errorInfo.get("SIZE");
                return response;
            }
            this.originalName = upfile.getOriginalFilename().substring(
                            upfile.getOriginalFilename().lastIndexOf(System.getProperty("file.separator")) + 1);
            response.setOriginal(this.originalName);
            // 创建目录
            String savePath = this.getFolder(this.savePath);
            if ("".equals(savePath)) {
                return response;
            }
            this.fileName = this.getName(this.originalName);
            this.type = this.getFileExt(this.fileName);
            // 文件存放动态目录
            this.url = savePath + "/" + this.fileName;
            String filePath = savePath + "/" + this.fileName;
            try {
                // + Global.USERFILES_BASE_URL+ path;
                // upfile.transferTo(new
                // File(request.getSession().getServletContext().getRealPath("upload") +
                // Global.USERFILES_BASE_URL + filePath));
                upfile.transferTo(new File(Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL + filePath));
                this.state = this.errorInfo.get("SUCCESS");
                response.setState(this.state);
            } catch (IOException e) {
                this.state = this.errorInfo.get("UNKNOWN");
                return response;
            }
            this.url = Global.USERFILES_BASE_URL + "/" + savePath + "/" + this.fileName;
        }

        ErpOrderFile orderFile = new ErpOrderFile();
        orderFile.setFileName(this.originalName);
        orderFile.setProcInsId(procInsId);
        orderFile.setFilePath(this.url);
        orderFile.setFileTitle(fileTitle);
        // 默认为已删除状态 ，正常提交时，才更新这个信息
        orderFile.setDelFlag("1");
        this.erpOrderFileService.save(orderFile);
        response.setFileName(this.originalName);
        response.setOrderFileId(orderFile.getId());
        return response;
    }

    /**
     * 文件上传方法,返回上传的文件名称以及路径
     *
     * @param upfile
     * @param split
     * @param request
     * @param model
     * @return
     * @date 2018年1月26日
     * @author Administrator
     */
    @RequestMapping(value = "workfile/file/onlyUpload", method = RequestMethod.POST)
    @ResponseBody
    public Object onlyUpload(@RequestParam(value = "file", required = false)  MultipartFile upfile, @RequestParam(required = false) String split,
                    HttpServletRequest request, Model model) {
        Response response = new Response();
        this.savePath = split;
        if (upfile.isEmpty()) {
            this.state = this.errorInfo.get("NOFILE");
            return response;
        } else {
            long fileSize = upfile.getSize();
            // 上传文件大小判断
            if (fileSize > 52428800) {
                this.state = this.errorInfo.get("SIZE");
                return response;
            }
            this.originalName = upfile.getOriginalFilename().substring(
                            upfile.getOriginalFilename().lastIndexOf(System.getProperty("file.separator")) + 1);
            response.setOriginal(this.originalName);
            // 创建目录
            String savePath = this.getFolder(this.savePath);
            if ("".equals(savePath)) {
                return response;
            }
            this.fileName = this.getName(this.originalName);
            this.type = this.getFileExt(this.fileName);
            // 文件存放动态目录
            this.url = savePath + "/" + this.fileName;
            String filePath = savePath + "/" + this.fileName;
            try {
                upfile.transferTo(new File(Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL + filePath));
                this.state = this.errorInfo.get("SUCCESS");
                response.setState(this.state);
            } catch (IOException e) {
                this.state = this.errorInfo.get("UNKNOWN");
                return response;
            }
            this.url = Global.USERFILES_BASE_URL + "/" + savePath + "/" + this.fileName;
        }
        response.setFileName(this.originalName);
        response.setUrl(this.url);
        return response;
    }
    
    @RequestMapping(value = "workfile/file/uploadDataReportFile", method = RequestMethod.POST)
    @ResponseBody
    public Object uploadDataReportFile(@RequestParam(value = "file", required = false)  MultipartFile upfile, String procInsId,
                              String id,String dataType) {
        Response response = new Response();
        ErpOrderSplitInfo splitInfo = erpOrderSplitInfoService.getByProsIncId(procInsId);
        if(StringUtils.isBlank(procInsId)||StringUtils.isBlank(dataType)||null==splitInfo){
            this.state = this.errorInfo.get("PARAMETERERROR");
            return response;
        }
        this.savePath = procInsId;
        if (upfile.isEmpty()) {
            this.state = this.errorInfo.get("NOFILE");
            return response;
        } else {
            long fileSize = upfile.getSize();
            // 上传文件大小判断
            if (fileSize > 52428800) {
                this.state = this.errorInfo.get("SIZE");
                return response;
            }
            this.originalName = upfile.getOriginalFilename().substring(
                            upfile.getOriginalFilename().lastIndexOf(System.getProperty("file.separator")) + 1);
            response.setOriginal(this.originalName);
            // 创建目录
            String savePath = this.getFolder(this.savePath);
            if ("".equals(savePath)) {
                return response;
            }
            this.fileName = this.getName(this.originalName);
            this.type = this.getFileExt(this.fileName);
            // 文件存放动态目录
            this.url = savePath + "/" + this.fileName;
            String filePath = savePath + "/" + this.fileName;
            try {
                // + Global.USERFILES_BASE_URL+ path;
                // upfile.transferTo(new
                // File(request.getSession().getServletContext().getRealPath("upload") +
                // Global.USERFILES_BASE_URL + filePath));
                upfile.transferTo(new File(Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL + filePath));
                this.state = this.errorInfo.get("SUCCESS");
                response.setState(this.state);
            } catch (IOException e) {
                this.state = this.errorInfo.get("UNKNOWN");
                return response;
            }
            this.url = Global.USERFILES_BASE_URL + "/" + savePath + "/" + this.fileName;
        }

        if(StringUtils.isNotBlank(id)){
            JykDataPresentation dataPresentation = jykDataPresentationService.get(id);
            dataPresentation.setProcInsId(procInsId);
            dataPresentation.setPdfUrl(this.url);
            dataPresentation.setPdfName( this.fileName);
            dataPresentation.setDataType(dataType);
            dataPresentation.setState("2");
            jykDataPresentationService.save(dataPresentation);
              }else{
                  JykDataPresentation dataPresentation = new JykDataPresentation();
                  dataPresentation.setOrderId(splitInfo.getOrderId());
                  dataPresentation.setSplitId(splitInfo.getId());
                  dataPresentation.setProcInsId(procInsId);
                  dataPresentation.setPdfUrl(this.url);
                  dataPresentation.setPdfName(this.fileName);
                  dataPresentation.setDataType(dataType);
                  dataPresentation.setState("2");
                  jykDataPresentationService.deleteBefore(splitInfo.getId(), dataType);
                  jykDataPresentationService.save(dataPresentation);
                  id=dataPresentation.getId();
              }
        response.setFileName(this.originalName);
        response.setId(id);;
        return response;
    }
    
    
    
    
    
    /**
     * 多文件上传
     *
     * @param files
     * @param request
     * @param procInsId
     * @param fileTitle
     * @param model
     * @return
     * @date 2017年11月23日
     * @author SunQ
     */
    @RequestMapping(value = "workfile/file/uploads", method = RequestMethod.POST)
    @ResponseBody
    public Object uploads(@RequestParam("file") MultipartFile[] files, HttpServletRequest request,
                    @RequestParam(required = false) String procInsId, @RequestParam(required = false) String taskDefKey,
                    @RequestParam(required = false) String subTaskId,
                    @RequestParam(required = false) String fileTitle, Model model) {

        Response response = new Response();
        this.savePath = procInsId;
        if (files == null) {
            this.state = this.errorInfo.get("NOFILE");
            return response;
        } else {
            MultipartFile upfile = files[0];
            long fileSize = upfile.getSize();
            // 上传文件大小判断
            if (fileSize > 52428800) {
                this.state = this.errorInfo.get("SIZE");
                return response;
            }
            this.originalName = upfile.getOriginalFilename()
                            .substring(upfile.getOriginalFilename().lastIndexOf(System.getProperty("file.separator")) + 1);
            response.setOriginal(this.originalName);
            // 创建目录
            String savePath = this.getFolder(this.savePath);
            if ("".equals(savePath)) {
                return response;
            }
            this.fileName = this.getName(this.originalName);
            this.type = this.getFileExt(this.fileName);
            // 文件存放动态目录
            this.url = savePath + "/" + this.fileName;
            String filePath = savePath + "/" + this.fileName;
            try {
                // + Global.USERFILES_BASE_URL+ path;
                // upfile.transferTo(new
                // File(request.getSession().getServletContext().getRealPath("upload") +
                // Global.USERFILES_BASE_URL + filePath));
                upfile.transferTo(new File(Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL + filePath));
                this.state = this.errorInfo.get("SUCCESS");
                response.setState(this.state);
            } catch (IOException e) {
                this.state = this.errorInfo.get("UNKNOWN");
                return response;
            }
            this.url = Global.USERFILES_BASE_URL + "/" + savePath + "/" + this.fileName;
        }

        ErpOrderFile orderFile = new ErpOrderFile();
        orderFile.setFileName(this.originalName);
        orderFile.setProcInsId(procInsId);
        orderFile.setTaskDefKey(taskDefKey);
        orderFile.setSubTaskId(subTaskId);
        orderFile.setFilePath(this.url);
        orderFile.setFileTitle(fileTitle);
        // 默认为已删除状态 ，正常提交时，才更新这个信息
        orderFile.setDelFlag("1");
        this.erpOrderFileService.save(orderFile);
        response.setFileName(this.originalName);
        response.setFileUrl(this.url);
        response.setOrderFileId(orderFile.getId());
        return response;
    }

    /**
     * 文件类型判断
     *
     * @param fileName
     * @return
     */
    private boolean checkFileType(String fileName) {
        Iterator<String> type = Arrays.asList(this.allowFiles).iterator();
        while (type.hasNext()) {
            String ext = type.next();
            if (fileName.toLowerCase().endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件扩展名
     *
     * @return string
     */
    private static String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.'));
    }

    /**
     * 依据原始文件名生成新文件名
     * 
     * @return
     */
    protected String getName(String fileName) {
        Random random = new Random();
        return this.fileName = "" + random.nextInt(10000) + System.currentTimeMillis() + this.getFileExt(fileName);
    }

    /**
     * 根据字符串创建本地目录 并按照日期建立子目录返回
     * 
     * @param path
     * @return
     */
    protected String getFolder(String path) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
        path += "/" + formater.format(new Date());
        File dir = new File(this.getPhysicalPath(path));
        if (!dir.exists()) {
            try {
                dir.mkdirs();
            } catch (Exception e) {
                this.state = this.errorInfo.get("DIR");
                return "";
            }
        }
        return path;
    }

    /**
     * 根据传入的虚拟路径获取物理路径
     *
     * @param path
     * @return
     */
    private static String getPhysicalPath(String path) {
        return Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL + path;
    }
    
    /**
     * 下载文件
     * @param downloadUrl
     * @param realFileName
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "workfile/file/downloadAmachment", method = RequestMethod.GET)
    public String downloadAmachment(String downloadUrl, String realFileName, HttpServletRequest request, HttpServletResponse response) {
    	
        response.setContentType("text/html;charset=UTF-8");
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        
        try {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream");
            response.reset();//清除response中的缓存
            
            //根据网络文件地址创建URL
            URL url = new URL(downloadUrl);
            //获取此路径的连接
            URLConnection conn = url.openConnection();
            
            //对文件名称进行转码操作
            realFileName = URLDecoder.decode(realFileName, "utf8");
            Long fileLength = conn.getContentLengthLong();//获取文件大小
            //设置reponse响应头，真实文件名重命名，就是在这里设置，设置编码
            response.setHeader("Content-disposition",
                    "attachment; filename=" + new String(realFileName.getBytes("utf-8"), "ISO8859-1"));
            response.setHeader("Content-Length", String.valueOf(fileLength));
  
            bis = new BufferedInputStream(conn.getInputStream());//构造读取流
            bos = new BufferedOutputStream(response.getOutputStream());//构造输出流
            byte[] buff = new byte[1024];
            int bytesRead;
            //每次读取缓存大小的流，写到输出流
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            response.flushBuffer();//将所有的读取的流返回给客户端
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
        	try {
        		if(null!=bis)
        		{
        			bis.close();
        		}
        		if(null!=bos)
        		{
        			bos.close();
        		}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
        }
        return null;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public void setAllowFiles(String[] allowFiles) {
        this.allowFiles = allowFiles;
    }

    public String getSize() {
        return this.size;
    }

    public String getUrl() {
        return this.url;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getState() {
        return this.state;
    }

    public String getTitle() {
        return this.title;
    }

    public String getType() {
        return this.type;
    }

    public String getOriginalName() {
        return this.originalName;
    }

    class Response {

        private String fileName;

        private String orderFileId;

        private String original;

        private String url;

        private String title;

        private String state;

        private String fileUrl;
        
        private String id;

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getOrderFileId() {
            return orderFileId;
        }

        public void setOrderFileId(String orderFileId) {
            this.orderFileId = orderFileId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }


    }
}
