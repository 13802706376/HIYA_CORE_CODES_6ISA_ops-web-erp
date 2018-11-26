package com.yunnex.ops.erp.modules.act.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.act.entity.ActDefExt;
import com.yunnex.ops.erp.modules.act.service.ActDefExtService;

/**
 * 流程节点扩展Controller
 * @author 1
 * @version 2017-11-30
 */
@Controller
@RequestMapping(value = "${adminPath}/act/actDefExt")
public class ActDefExtController extends BaseController {

	@Autowired
	private ActDefExtService actDefExtService;
	
	@ModelAttribute
	public ActDefExt get(@RequestParam(required=false) String id) {
		ActDefExt entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = actDefExtService.get(id);
		}
		if (entity == null){
			entity = new ActDefExt();
		}
		return entity;
	}
	
	@RequiresPermissions("act:actDefExt:view")
	@RequestMapping(value = {"list", ""})
	public String list(ActDefExt actDefExt, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ActDefExt> page = actDefExtService.findPage(new Page<ActDefExt>(request, response), actDefExt); 
		model.addAttribute("page", page);
		return "modules/act/actDefExtList";
	}

	@RequiresPermissions("act:actDefExt:view")
	@RequestMapping(value = "form")
	public String form(ActDefExt actDefExt, Model model) {
		model.addAttribute("actDefExt", actDefExt);
		return "modules/act/actDefExtForm";
	}

	@RequiresPermissions("act:actDefExt:edit")
	@RequestMapping(value = "save")
	public String save(ActDefExt actDefExt, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, actDefExt)){
			return form(actDefExt, model);
		}
		actDefExtService.save(actDefExt);
		addMessage(redirectAttributes, "保存流程节点扩展成功");
		return "redirect:"+Global.getAdminPath()+"/act/actDefExt/?repage";
	}
	
	@RequiresPermissions("act:actDefExt:edit")
	@RequestMapping(value = "delete")
	public String delete(ActDefExt actDefExt, RedirectAttributes redirectAttributes) {
		actDefExtService.delete(actDefExt);
		addMessage(redirectAttributes, "删除流程节点扩展成功");
		return "redirect:"+Global.getAdminPath()+"/act/actDefExt/?repage";
	}

}