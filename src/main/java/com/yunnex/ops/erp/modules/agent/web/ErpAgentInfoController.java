package com.yunnex.ops.erp.modules.agent.web;

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
import com.yunnex.ops.erp.common.persistence.Pager;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.agent.dto.AgentInfoRequestDto;
import com.yunnex.ops.erp.modules.agent.dto.AgentInfoResponseDto;
import com.yunnex.ops.erp.modules.agent.entity.ErpAgentInfo;
import com.yunnex.ops.erp.modules.agent.service.ErpAgentInfoApiService;
import com.yunnex.ops.erp.modules.agent.service.ErpAgentInfoService;
import com.yunnex.ops.erp.modules.sys.dto.AgentUserResponseDto;
import com.yunnex.ops.erp.modules.sys.service.AgentUserService;

/**
 * 服务商信息Controller
 * @author yunnex
 * @version 2018-05-28
 */
@Controller
@RequestMapping(value = "${adminPath}/agent/erpAgentInfo")
public class ErpAgentInfoController extends BaseController {

    @Autowired
    private ErpAgentInfoService erpAgentInfoService;
    @Autowired
    private AgentUserService agentUserService;

    @Autowired
    private ErpAgentInfoApiService erpAgentInfoApiService;

    @ModelAttribute
    public ErpAgentInfo get(@RequestParam(required = false) String id) {
        ErpAgentInfo entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = erpAgentInfoService.get(id);
        }
        if (entity == null) {
            entity = new ErpAgentInfo();
        }
        return entity;
    }

    @RequiresPermissions("agent:erpAgentInfo:view")
    @RequestMapping(value = "form")
    public String form(ErpAgentInfo erpAgentInfo, Model model) {
        model.addAttribute("erpAgentInfo", erpAgentInfo);
        return "modules/agent/erpAgentInfoForm";
    }

    @RequiresPermissions("agent:erpAgentInfo:edit")
    @RequestMapping(value = "save")
    public String save(ErpAgentInfo erpAgentInfo, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, erpAgentInfo)) {
            return form(erpAgentInfo, model);
        }
        erpAgentInfoService.save(erpAgentInfo);
        addMessage(redirectAttributes, "保存服务商信息成功");
        return "redirect:" + Global.getAdminPath() + "/agent/erpAgentInfo/?repage";
    }

    @RequiresPermissions("agent:erpAgentInfo:edit")
    @RequestMapping(value = "delete")
    public String delete(ErpAgentInfo erpAgentInfo, RedirectAttributes redirectAttributes) {
        erpAgentInfoService.delete(erpAgentInfo);
        addMessage(redirectAttributes, "删除服务商信息成功");
        return "redirect:" + Global.getAdminPath() + "/agent/erpAgentInfo/?repage";
    }

    /**
     * 同步服务商
     * 
     * @return
     */
    @RequiresPermissions("agent:erpAgentInfo:syncAll")
    @RequestMapping("syncAll")
    public @ResponseBody Boolean syncAll() {
        return erpAgentInfoApiService.syncAll();
    }

    @RequiresPermissions("agent:erpAgentInfo:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/agent/erpAgentInfoList";
    }

    /**
     * 服务商列表
     */
    @RequiresPermissions("agent:erpAgentInfo:list")
    @RequestMapping("findByPage")
    public @ResponseBody Pager<AgentInfoResponseDto> findByPage(AgentInfoRequestDto requestDto) {
        return erpAgentInfoService.findByPage(requestDto);
    }

    /**
     * 服务商对应的用户信息
     */
    @RequiresPermissions("agent:erpAgentInfo:user:view")
    @RequestMapping("findAgentUserInfo")
    public @ResponseBody AgentUserResponseDto findAgentUserInfo(Integer agentId) {
        return agentUserService.findAgentUserInfo(agentId);
    }

}
