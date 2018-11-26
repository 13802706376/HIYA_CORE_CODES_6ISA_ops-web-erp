package com.yunnex.ops.erp.modules.sys.web;

import java.util.List;

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

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.sys.constant.SysConstant;
import com.yunnex.ops.erp.modules.sys.entity.JobNumberInfo;
import com.yunnex.ops.erp.modules.sys.entity.Role;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.service.JobNumberInfoService;
import com.yunnex.ops.erp.modules.sys.service.RoleService;
import com.yunnex.ops.erp.modules.sys.service.UserService;

/**
 * 工号管理Controller
 * 
 * @author SunQ
 * @date 2018年1月24日
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/jobNumber")
public class JobNumberInfoController extends BaseController {

    @Autowired
    private JobNumberInfoService jobNumberService;
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private UserService userService;
    
    @ModelAttribute
    public JobNumberInfo get(@RequestParam(required=false) String id) {
        if (StringUtils.isNotBlank(id)){
            return jobNumberService.get(id);
        }else{
            return new JobNumberInfo();
        }
    }
    
    /**
     * 列表页面
     *
     * @param request
     * @param response
     * @return
     * @date 2018年1月24日
     * @author SunQ
     */
    @RequiresPermissions("sys:jobNumber:view")
    @RequestMapping(value = {"list", ""})
    public String list(JobNumberInfo jobNumber, HttpServletRequest request, HttpServletResponse response, Model model,
                    @RequestParam(required = false, defaultValue = SysConstant.TYPE_ERP) String type) {
        jobNumber.setType(type);
        Page<JobNumberInfo> page = jobNumberService.findPage(new Page<JobNumberInfo>(request, response), jobNumber);
        model.addAttribute("page", page);
        return "modules/sys/jobNumberList";
    }
    
    /**
     * 异步获取列表数据
     *
     * @param jobNumber
     * @param request
     * @param response
     * @return
     * @date 2018年1月24日
     * @author SunQ
     */
    @ResponseBody
    @RequiresPermissions("sys:jobNumber:view")
    @RequestMapping(value = {"listData"})
    public BaseResult listData(JobNumberInfo jobNumber, HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        Page<JobNumberInfo> page = jobNumberService.findPage(new Page<JobNumberInfo>(request, response), jobNumber);
        result.setAttach(page);
        return result;
    }
    
    /**
     * 查询详情页面
     *
     * @param request
     * @param response
     * @return
     * @date 2018年1月24日
     * @author SunQ
     */
    @RequiresPermissions("sys:jobNumber:view")
    @RequestMapping(value = "form")
    public String form(HttpServletRequest request, HttpServletResponse response) {
        return "modules/sys/jobNumberForm";
    }
    
    /**
     * 异步获取工号信息
     *
     * @param id
     * @return
     * @date 2018年1月24日
     * @author SunQ
     */
    @ResponseBody
    @RequiresPermissions("sys:jobNumber:view")
    @RequestMapping(value = {"getData"})
    public BaseResult getData(@RequestParam("id") String id) {
        BaseResult result = new BaseResult();
        JobNumberInfo jobNumber = jobNumberService.get(id);
        result.setAttach(jobNumber);
        return result;
    }
    
    /**
     * 新增页面
     *
     * @param request
     * @param response
     * @return
     * @date 2018年1月24日
     * @author SunQ
     */
    @RequiresPermissions("sys:jobNumber:edit")
    @RequestMapping(value = "toadd")
    public String toadd(HttpServletRequest request, HttpServletResponse response) {
        return "modules/sys/jobNumberAdd";
    }

    /**
     * 选择人员
     *
     * @param request
     * @param response
     * @return
     * @date 2018年1月24日
     * @author SunQ
     */
    @RequestMapping(value = "selectUser")
    public String selectUser(HttpServletRequest request, HttpServletResponse response) {
        return "modules/sys/selectUser";
    }
    
    /**
     * 异步获取角色列表
     *
     * @return
     * @date 2018年1月24日
     * @author SunQ
     */
    @ResponseBody
    @RequestMapping(value = {"getRoleData"})
    public BaseResult getRoleData() {
        BaseResult result = new BaseResult();
        List<Role> roles = roleService.findByType(SysConstant.TYPE_ERP);
        result.setAttach(roles);
        return result;
    }
    
    /**
     * 异步获取用户列表
     *
     * @return
     * @date 2018年1月24日
     * @author SunQ
     */
    @ResponseBody
    @RequestMapping(value = {"getUserData"})
    public BaseResult getUserData(User user, HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        user.setType(SysConstant.TYPE_ERP); // ERP类型的用户
        Page<User> page = userService.findPage(new Page<User>(request, response), user);
        result.setAttach(page);
        return result;
    }
    
    /**
     * 异步保存工号信息(用于新增)
     *
     * @param jobNumber
     * @return
     * @date 2018年1月24日
     * @author SunQ
     */
    @ResponseBody
    @RequiresPermissions("sys:jobNumber:edit")
    @RequestMapping(value = {"save"})
    public BaseResult save(JobNumberInfo jobNumber) {
        return jobNumberService.createErpJobNumber(jobNumber);
    }
    
    /**
     * 异步保存工号信息(用于修改)
     *
     * @param jobNumber
     * @return
     * @date 2018年1月24日
     * @author SunQ
     */
    @ResponseBody
    @RequiresPermissions("sys:jobNumber:edit")
    @RequestMapping(value = {"update"})
    public BaseResult update(JobNumberInfo jobNumber) {
        BaseResult result = new BaseResult();
        jobNumberService.save(jobNumber);
        return result;
    }
    
    /**
     * 异步删除工号信息
     *
     * @param id
     * @return
     * @date 2018年1月24日
     * @author SunQ
     */
    @ResponseBody
    @RequiresPermissions("sys:jobNumber:edit")
    @RequestMapping(value = {"delete"})
    public BaseResult delete(@RequestParam("id") String id) {
        BaseResult result = new BaseResult();
        JobNumberInfo jobNumber = jobNumberService.get(id);
        jobNumberService.delete(jobNumber);
        return result;
    }
}
