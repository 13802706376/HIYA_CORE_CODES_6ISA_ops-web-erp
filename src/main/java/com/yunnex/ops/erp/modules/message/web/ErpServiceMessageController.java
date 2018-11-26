package com.yunnex.ops.erp.modules.message.web;

import java.util.Map;

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

import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.message.entity.ErpServiceMessage;
import com.yunnex.ops.erp.modules.message.service.ErpServiceMessageService;

/**
 * 服务通知表Controller
 * 
 * @author yunnex
 * @version 2018-07-04
 */
@Controller
@RequestMapping(value = "${adminPath}/message/erpServiceMessage")
public class ErpServiceMessageController extends BaseController {

	@Autowired
	private ErpServiceMessageService erpServiceMessageService;
	
	@ModelAttribute
	public ErpServiceMessage get(@RequestParam(required=false) String id) {
		ErpServiceMessage entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = erpServiceMessageService.get(id);
		}
		if (entity == null){
			entity = new ErpServiceMessage();
		}
		return entity;
	}
	
	@RequiresPermissions("message:erpServiceMessage:view")
	@RequestMapping(value = {"list", ""})
	public String list(ErpServiceMessage erpServiceMessage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ErpServiceMessage> page = erpServiceMessageService.findPage(new Page<ErpServiceMessage>(request, response), erpServiceMessage); 
		model.addAttribute("page", page);
		return "modules/message/erpServiceMessageList";
	}

	@RequiresPermissions("message:erpServiceMessage:view")
	@RequestMapping(value = "form")
	public String form(ErpServiceMessage erpServiceMessage, Model model) {
		model.addAttribute("erpServiceMessage", erpServiceMessage);
		return "modules/message/erpServiceMessageForm";
	}

	@RequiresPermissions("message:erpServiceMessage:edit")
	@RequestMapping(value = "save")
	public String save(ErpServiceMessage erpServiceMessage, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, erpServiceMessage)){
			return form(erpServiceMessage, model);
		}
		erpServiceMessageService.save(erpServiceMessage);
        addMessage(redirectAttributes, "保存服务通知表成功");
		return "redirect:"+Global.getAdminPath()+"/message/erpServiceMessage/?repage";
	}
	
	@RequiresPermissions("message:erpServiceMessage:edit")
	@RequestMapping(value = "delete")
	public String delete(ErpServiceMessage erpServiceMessage, RedirectAttributes redirectAttributes) {
		erpServiceMessageService.delete(erpServiceMessage);
        addMessage(redirectAttributes, "删除服务通知表成功");
		return "redirect:"+Global.getAdminPath()+"/message/erpServiceMessage/?repage";
	}

    /**
     * 业务定义：保存通知信息
     * 
     * @date 2018年7月23日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "saveMessageRecord")
    public Object saveMessageRecord(String paramJson) {
        Map<String, Object> returnMap = Maps.newHashMap();
        try {
            erpServiceMessageService.saveMessageRecord(paramJson);
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_SUCCESS);
        } catch (Exception e) {// NOSONAR
            logger.error("保存服务进度数据出错，paramJson={}，error={}", paramJson, e);// NOSONAR
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_FAIL);
            returnMap.put(CommonConstants.RETURN_MESSAGE, CommonConstants.SYSTEM_ERROR_MESSAGE);
        }
        return returnMap;
    }

}