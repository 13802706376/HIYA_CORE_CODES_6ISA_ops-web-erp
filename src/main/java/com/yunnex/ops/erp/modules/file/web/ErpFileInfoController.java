package com.yunnex.ops.erp.modules.file.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.utils.UploadUtil;
import com.yunnex.ops.erp.common.utils.UploadUtil.UploadResult;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.file.entity.ErpFileInfo;
import com.yunnex.ops.erp.modules.file.service.ErpFileInfoService;

import yunnex.common.core.dto.ApiResult;

/**
 * 文件信息Controller
 * @author yunnex
 * @version 2017-12-16
 * 
 * 
 * 
 * update 2017-12-16
 * 
 * 
 * 
 * 
 */
@Controller
@RequestMapping(value = "${adminPath}/file/erpFileInfo")
public class ErpFileInfoController extends BaseController {

	@Autowired
	private ErpFileInfoService erpFileInfoService;
	@Autowired
	private UploadUtil uploadUtil;
	
	@ModelAttribute
	public ErpFileInfo get(@RequestParam(required=false) String id) {
		ErpFileInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = erpFileInfoService.get(id);
		}
		if (entity == null){
			entity = new ErpFileInfo();
		}
		return entity;
	}
	
	@RequiresPermissions("file:erpFileInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(ErpFileInfo erpFileInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ErpFileInfo> page = erpFileInfoService.findPage(new Page<ErpFileInfo>(request, response), erpFileInfo); 
		model.addAttribute("page", page);
		return "modules/file/erpFileInfoList";
	}

	@RequiresPermissions("file:erpFileInfo:view")
	@RequestMapping(value = "form")
	public String form(ErpFileInfo erpFileInfo, Model model) {
		model.addAttribute("erpFileInfo", erpFileInfo);
		return "modules/file/erpFileInfoForm";
	}

	@RequiresPermissions("file:erpFileInfo:edit")
	@RequestMapping(value = "save")
	public String save(ErpFileInfo erpFileInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, erpFileInfo)){
			return form(erpFileInfo, model);
		}
		erpFileInfoService.save(erpFileInfo);
		addMessage(redirectAttributes, "保存文件信息成功");
		return "redirect:"+Global.getAdminPath()+"/file/erpFileInfo/?repage";
	}
	
	@RequiresPermissions("file:erpFileInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(ErpFileInfo erpFileInfo, RedirectAttributes redirectAttributes) {
		erpFileInfoService.delete(erpFileInfo);
		addMessage(redirectAttributes, "删除文件信息成功");
		return "redirect:"+Global.getAdminPath()+"/file/erpFileInfo/?repage";
	}
	
	/**
	 * 文件上传
	 * @author yunnex
	 * @version 2017-12-16
	 * 
	 */
	@RequestMapping(value = "uploadFile", method = RequestMethod.POST)
	public ResponseEntity<ApiResult<UploadResult>> upload(@RequestParam("file") MultipartFile[] file) {
	    ApiResult<UploadResult> apiResult = ApiResult.build();
        try {
            UploadResult uploadResult = uploadUtil.uploadStoreFiles(file);
            apiResult.setCode(Integer.toString(uploadResult.getCode()));
            apiResult.setEntry(uploadResult);
            if (uploadResult.getCode() == 0) {
                apiResult.setMessage("文件上传成功");
            } else if (uploadResult.getCode() == -1) {
                apiResult.error("文件上传失败");
            } else if (uploadResult.getCode() == 1) {
                apiResult.setMessage("部分文件上传成功");
            }
        } catch (RuntimeException e) {
            String msg = "文件上传失败";
            apiResult.error(msg);
            logger.error(msg, e);
        }
        return ResponseEntity.ok(apiResult);
    }

}