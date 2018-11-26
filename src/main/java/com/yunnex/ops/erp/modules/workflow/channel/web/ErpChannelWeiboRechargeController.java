package com.yunnex.ops.erp.modules.workflow.channel.web;

import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.persistence.Pager;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.workflow.channel.constant.Constants;
import com.yunnex.ops.erp.modules.workflow.channel.dto.WeiboRechargeActualRequestDto;
import com.yunnex.ops.erp.modules.workflow.channel.dto.WeiboRechargeRequestDto;
import com.yunnex.ops.erp.modules.workflow.channel.dto.WeiboRechargeResponseDto;
import com.yunnex.ops.erp.modules.workflow.channel.entity.ErpChannelWeiboRecharge;
import com.yunnex.ops.erp.modules.workflow.channel.service.ErpChannelWeiboRechargeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;

/**
 * 微博通道充值Controller
 * 
 * @author yunnex
 * @version 2018-05-08
 */
@Controller
@RequestMapping(value = "${adminPath}/channel/erpChannelWeiboRecharge")
public class ErpChannelWeiboRechargeController extends BaseController {

    @Autowired
    private ErpChannelWeiboRechargeService erpChannelWeiboRechargeService;

    @ModelAttribute
    public ErpChannelWeiboRecharge get(@RequestParam(required = false) String id) {
        ErpChannelWeiboRecharge entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = erpChannelWeiboRechargeService.get(id);
        }
        if (entity == null) {
            entity = new ErpChannelWeiboRecharge();
        }
        return entity;
    }

    @RequiresPermissions("channel:erpChannelWeiboRecharge:view")
    @RequestMapping(value = "form")
    public String form(ErpChannelWeiboRecharge erpChannelWeiboRecharge, Model model) {
        model.addAttribute("erpChannelWeiboRecharge", erpChannelWeiboRecharge);
        return "modules/workflow/channel/erpChannelWeiboRechargeForm";
    }

    @RequiresPermissions("channel:erpChannelWeiboRecharge:edit")
    @RequestMapping(value = "save")
    public String save(ErpChannelWeiboRecharge erpChannelWeiboRecharge, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, erpChannelWeiboRecharge)) {
            return form(erpChannelWeiboRecharge, model);
        }
        erpChannelWeiboRechargeService.save(erpChannelWeiboRecharge);
        addMessage(redirectAttributes, "保存微博通道充值成功");
        return "redirect:" + Global.getAdminPath() + "/channel/erpChannelWeiboRecharge/?repage";
    }

    @RequiresPermissions("channel:erpChannelWeiboRecharge:edit")
    @RequestMapping(value = "delete")
    public String delete(ErpChannelWeiboRecharge erpChannelWeiboRecharge, RedirectAttributes redirectAttributes) {
        erpChannelWeiboRechargeService.delete(erpChannelWeiboRecharge);
        addMessage(redirectAttributes, "删除微博通道充值成功");
        return "redirect:" + Global.getAdminPath() + "/channel/erpChannelWeiboRecharge/?repage";
    }

    @RequiresPermissions("channel:erpChannelWeiboRecharge:view")
    @RequestMapping(value = {""})
    public String listPage() {
        return "modules/workflow/channel/erpChannelWeiboRechargeList";
    }

    /**
     * 分页列表
     * 
     * @param requestDto
     * @return
     */
    @RequiresPermissions("channel:erpChannelWeiboRecharge:view")
    @RequestMapping(value = {"list"})
    public @ResponseBody Pager<WeiboRechargeResponseDto> list(WeiboRechargeRequestDto requestDto) {
        return erpChannelWeiboRechargeService.findPage(requestDto);
    }

    /**
     * 检查指定分单是否已有指定微博账号充值记录
     * 
     * @param splitId
     * @param weiboAccountNo
     * @return
     */
    @RequestMapping("checkSplitWeiboExists")
    public @ResponseBody BaseResult checkSplitWeiboExists(String splitId, String weiboAccountNo, String weiboUid) {
        return erpChannelWeiboRechargeService.checkSplitWeiboExists(splitId, weiboAccountNo, weiboUid);
    }

    /**
     * 从管理界面新增充值
     * 
     * @param recharge
     * @return
     */
    @RequestMapping(value = "create")
    public @ResponseBody BaseResult create(@RequestBody ErpChannelWeiboRecharge recharge) {
        // 设置来源和默认充值状态
        return erpChannelWeiboRechargeService.create(recharge, Constants.SOURCE_MANAGE, Constants.STATUS_APPLYING);
    }

    /**
     * 修改实际充值金额
     * 
     * @return
     */
    @RequestMapping(value = "changeActualRecharge", method = RequestMethod.POST)
    public @ResponseBody BaseResult changeActualRecharge(@RequestBody WeiboRechargeActualRequestDto recharge) {
        return erpChannelWeiboRechargeService.changeActualRecharge(recharge);
    }

    /**
     * 取消充值
     * 
     * @param id
     * @return
     */
    @RequestMapping(value = "cancelRecharge", method = RequestMethod.POST)
    public @ResponseBody BaseResult cancelRecharge(String id) {
        return erpChannelWeiboRechargeService.changeRechargeStatus(id, Constants.STATUS_CANCEL);
    }

    /**
     * 重新充值
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "reRecharge", method = RequestMethod.POST)
    public @ResponseBody BaseResult reRecharge(String id) {
        return erpChannelWeiboRechargeService.changeRechargeStatus(id, Constants.STATUS_APPLYING);
    }

    /**
     * 导出充值列表
     * 
     * @param requestDto
     * @param response
     * @return
     */
    @RequestMapping("export")
    public @ResponseBody BaseResult export(WeiboRechargeRequestDto requestDto, HttpServletResponse response) {
        return erpChannelWeiboRechargeService.export(requestDto, response);
    }

}
