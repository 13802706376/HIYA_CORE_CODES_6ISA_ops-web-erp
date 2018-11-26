package com.yunnex.ops.erp.modules.holiday.web;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.holiday.entity.ErpHolidays;
import com.yunnex.ops.erp.modules.holiday.service.ErpHolidaysService;

/**
 * 节假日配置Controller
 * @author pch
 * @version 2017-11-02
 */
@Controller
@RequestMapping(value = "${adminPath}/holiday/erpHolidays")
public class ErpHolidaysController extends BaseController {

    /**
     * 节假日配置Service
     */
	@Autowired
	private ErpHolidaysService erpHolidaysService;
	

	//通过ID获取节假日信息
	@ModelAttribute
	public ErpHolidays get(@RequestParam(required=false) String id) { 
		ErpHolidays entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = erpHolidaysService.get(id);
		}
		if (entity == null){
			entity = new ErpHolidays();
		}
		return entity;
	}
	
	@RequiresPermissions("holiday:erpHolidays:view")
	@RequestMapping(value = {"list", ""})
	public String list(ErpHolidays erpHolidays, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<ErpHolidays> page = erpHolidaysService.findPage(new Page<ErpHolidays>(request, response), erpHolidays); 
		model.addAttribute("page", page);
		return "modules/holiday/erpHolidaysList";
	}

	@RequiresPermissions("holiday:erpHolidays:view")
	@RequestMapping(value = "form")
	public String form(ErpHolidays erpHolidays, Model model) {
		model.addAttribute("erpHolidays", erpHolidays);
		return "modules/holiday/erpHolidaysForm";
	}

	@RequiresPermissions("holiday:erpHolidays:edit")
	@RequestMapping(value = "save")
	public String save(HttpServletRequest request, ErpHolidays erpHolidays, Model model,
			RedirectAttributes redirectAttributes) {
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		if (erpHolidaysService.wheredate(s.format(erpHolidays.getHolidayDate()),Global.NO) > 0) {
			addMessage(redirectAttributes, "请勿重复添加相同节假日");
		} else {
			if (!beanValidator(model, erpHolidays)) {
				return form(erpHolidays, model);
			}
			erpHolidaysService.save(erpHolidays);
			addMessage(redirectAttributes, "保存新增节假日成功");


		}
		return "redirect:" + Global.getAdminPath() + "/holiday/erpHolidays/?repage";
	}
	
	@RequiresPermissions("holiday:erpHolidays:edit")
	@RequestMapping(value = "delete")
	public String delete(ErpHolidays erpHolidays, RedirectAttributes redirectAttributes) {
		erpHolidaysService.delete(erpHolidays);
		addMessage(redirectAttributes, "删除新增节假日成功");
		return "redirect:"+Global.getAdminPath()+"/holiday/erpHolidays/?repage";
	}
	
	//周六日导入接口
	@RequestMapping(value = "sync")
	@ResponseBody
	public String sync(){
		 Calendar c=Calendar.getInstance();
		 ErpHolidays saturday=null;
		 ErpHolidays sunday=null;
	     int year = c.get(Calendar.YEAR);
	        Calendar calendar = new GregorianCalendar(year, 0, 1);
	        int i = 1;
	        while (calendar.get(Calendar.YEAR) < year + 1) {
	            calendar.set(Calendar.WEEK_OF_YEAR, i++);
	            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
	            if (calendar.get(Calendar.YEAR) == year) {
	            	sunday=new ErpHolidays();
	            	sunday.setRemark("周末");
	            	sunday.setHolidayDate(calendar.getTime());
	            	erpHolidaysService.save(sunday);
	            }
	            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
	            if (calendar.get(Calendar.YEAR) == year) {
	            	saturday=new ErpHolidays();
	            	saturday.setRemark("周末");
	            	saturday.setHolidayDate(calendar.getTime());
	            	erpHolidaysService.save(saturday);
	            }
	        }
			return "导入周六日成功";
	        
	}

}