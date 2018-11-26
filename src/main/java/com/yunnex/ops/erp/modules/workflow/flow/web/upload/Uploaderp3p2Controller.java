package com.yunnex.ops.erp.modules.workflow.flow.web.upload;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.baidu.ueditor.ConfigManager;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;


/**
 * 图片文件上传辅助类 与ueditor插件集成
 *
 */
@Controller
@RequestMapping(value = "static")
public class Uploaderp3p2Controller extends UploaderController {

    @Value("${domain.erp.res}")
    private String resDomain;
    
    @Value("${dwrPath}")
    private String dwrPath;

    private HashMap<String, String> errorInfo = new HashMap<String, String>();// NOSONAR


    public Uploaderp3p2Controller() {
        super();
    }


    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/ueditor/file/uploads", method = RequestMethod.GET)
    @ResponseBody
    public Object uploads(HttpServletRequest request, @RequestParam(required = false) String procInsId,
                    @RequestParam(required = false) String taskDefKey, @RequestParam(required = false) String subTaskId,
                    @RequestParam(required = false) String fileTitle, Model model) throws JSONException {

        String uri = request.getRequestURI().replace("file/uploads", "jsp/controller.jsp");
        JSONObject jsonObject = ConfigManager.getInstance(request.getRealPath("/"), request.getContextPath(), uri).getAllConfig();
        jsonObject.put("imageUrlPrefix", resDomain);
        jsonObject.put("fileUrlPrefix", resDomain);
        return jsonObject.toString();
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
    @RequestMapping(value = "/ueditor/file/uploads", method = RequestMethod.POST)
    @ResponseBody
    public Object uploads(@RequestParam("upfile") MultipartFile[] upfile, HttpServletRequest request,
                    @RequestParam(required = false) String procInsId,
                    @RequestParam(required = false) String taskDefKey, @RequestParam(required = false) String subTaskId,
                    @RequestParam(required = false) String fileTitle, Model model) {

        Response response = new Response();
        this.savePath = procInsId;
        if (upfile == null) {
            this.state = this.errorInfo.get("NOFILE");
            return response;
        } else {
            MultipartFile upfiles = upfile[0];
            long fileSize = upfiles.getSize();
            // 上传文件大小判断
            if (fileSize > 52428800) {
                this.state = this.errorInfo.get("SIZE");
                return response;
            }
            this.originalName = upfiles.getOriginalFilename()
                            .substring(upfiles.getOriginalFilename().lastIndexOf(System.getProperty("file.separator")) + 1);
            response.setOriginal(this.originalName);
            // 创建目录
            String savePath = getFolder(this.savePath);
            if ("".equals(savePath)) {
                return response;
            }
            this.fileName = getName(this.originalName);
            this.type = this.getFileExt(this.fileName);
            // 文件存放动态目录
            this.url = savePath + "/" + this.fileName;
            String filePath = savePath + "/" + this.fileName;
            try {
                upfiles.transferTo(new File(Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL + filePath));
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
        response.setOrderFileId(orderFile.getId());
        response.setState("SUCCESS");
        response.setOriginal(originalName);
        response.setTitle(originalName);
        response.setUrl(url);


        return response;
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
     * 根据传入的虚拟路径获取物理路径
     *
     * @param path
     * @return
     */
    private static String getPhysicalPath(String path) {
        return Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL + path;
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
