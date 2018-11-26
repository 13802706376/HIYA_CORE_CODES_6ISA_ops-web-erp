package com.yunnex.ops.erp.modules.material.web;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.result.IllegalArgumentErrorResult;
import com.yunnex.ops.erp.common.result.SystemErrorResult;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.web.Servlets;
import com.yunnex.ops.erp.modules.material.entity.ErpOrderMaterialContent;
import com.yunnex.ops.erp.modules.material.service.ErpOrderMaterialContentService;

/**
 * 订单物料内容Controller
 * @author yunnex
 * @version 2018-07-13
 */
@Controller
@RequestMapping(value = "${adminPath}/material/erpOrderMaterialContent")
public class ErpOrderMaterialContentController extends BaseController {

    private static final String MATERIAL_ZIP = "物料资料.zip";

	@Autowired
	private ErpOrderMaterialContentService erpOrderMaterialContentService;
	
	@ModelAttribute
	public ErpOrderMaterialContent get(@RequestParam(required=false) String id) {
		ErpOrderMaterialContent entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = erpOrderMaterialContentService.get(id);
		}
		if (entity == null){
			entity = new ErpOrderMaterialContent();
		}
		return entity;
	}
	
	@RequiresPermissions("material:erpOrderMaterialContent:view")
	@RequestMapping(value = {"list", ""})
	public String list(ErpOrderMaterialContent erpOrderMaterialContent, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ErpOrderMaterialContent> page = erpOrderMaterialContentService.findPage(new Page<ErpOrderMaterialContent>(request, response), erpOrderMaterialContent); 
		model.addAttribute("page", page);
		return "modules/material/erpOrderMaterialContentList";
	}

	@RequiresPermissions("material:erpOrderMaterialContent:view")
	@RequestMapping(value = "form")
	public String form(ErpOrderMaterialContent erpOrderMaterialContent, Model model) {
		model.addAttribute("erpOrderMaterialContent", erpOrderMaterialContent);
		return "modules/material/erpOrderMaterialContentForm";
	}

	@RequiresPermissions("material:erpOrderMaterialContent:edit")
	@RequestMapping(value = "save")
	public String save(ErpOrderMaterialContent erpOrderMaterialContent, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, erpOrderMaterialContent)){
			return form(erpOrderMaterialContent, model);
		}
		erpOrderMaterialContentService.save(erpOrderMaterialContent);
		addMessage(redirectAttributes, "保存订单物料内容成功");
		return "redirect:"+Global.getAdminPath()+"/material/erpOrderMaterialContent/?repage";
	}
	
	@RequiresPermissions("material:erpOrderMaterialContent:edit")
	@RequestMapping(value = "delete")
	public String delete(ErpOrderMaterialContent erpOrderMaterialContent, RedirectAttributes redirectAttributes) {
		erpOrderMaterialContentService.delete(erpOrderMaterialContent);
		addMessage(redirectAttributes, "删除订单物料内容成功");
		return "redirect:"+Global.getAdminPath()+"/material/erpOrderMaterialContent/?repage";
	}

    /**
     * 查看订单物料清单
     * 
     * @param orderNumber
     * @return
     */
    @RequestMapping("findOrderMaterials")
    public @ResponseBody BaseResult findOrderMaterials(String orderNumber) {
        return erpOrderMaterialContentService.findOrderMaterials(orderNumber);
    }

    /**
     * 下载订单物料内容/清单
     * 
     * @param orderNumber
     * @param response
     * @return
     */
    @RequestMapping("downloadOrderMaterials")
    public @ResponseBody BaseResult downloadOrderMaterials(String orderNumber, HttpServletResponse response) {
        if (StringUtils.isBlank(orderNumber)) {
            return new IllegalArgumentErrorResult("订单号不能为空！");
        }
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            Servlets.setFileDownloadHeader(response, MATERIAL_ZIP);
            return erpOrderMaterialContentService.compressOrderMaterials(orderNumber, outputStream);
        } catch (IOException e) {
            String msg = "下载失败！";
            logger.error(msg, e);
            return new SystemErrorResult(msg);
        }
    }

}
