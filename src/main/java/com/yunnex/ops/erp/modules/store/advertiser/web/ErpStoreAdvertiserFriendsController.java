package com.yunnex.ops.erp.modules.store.advertiser.web;

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
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserFriends;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserFriendsService;

/**
 * 朋友圈广告主开通资料Controller
 * @author yunnex
 * @version 2017-12-09
 */
@Controller
@RequestMapping(value = "${adminPath}/store/advertiser/erpStoreAdvertiserFriends")
public class ErpStoreAdvertiserFriendsController extends BaseController {

    @Autowired
    private ErpStoreAdvertiserFriendsService erpStoreAdvertiserFriendsService;

    @ModelAttribute
    public ErpStoreAdvertiserFriends get(@RequestParam(required = false) String id) {
        ErpStoreAdvertiserFriends entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = erpStoreAdvertiserFriendsService.get(id);
        }
        if (entity == null) {
            entity = new ErpStoreAdvertiserFriends();
        }
        return entity;
    }

    @RequiresPermissions("store:advertiser:erpStoreAdvertiserFriends:view")
    @RequestMapping(value = {"list", ""})
    public String list(ErpStoreAdvertiserFriends erpStoreAdvertiserFriends, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ErpStoreAdvertiserFriends> page = erpStoreAdvertiserFriendsService.findPage(new Page<ErpStoreAdvertiserFriends>(request, response),
                        erpStoreAdvertiserFriends);
        model.addAttribute("page", page);
        return "modules/store/advertiser/erpStoreAdvertiserFriendsList";
    }

    @RequiresPermissions("store:advertiser:erpStoreAdvertiserFriends:view")
    @RequestMapping(value = "form")
    public String form(ErpStoreAdvertiserFriends erpStoreAdvertiserFriends, Model model) {
        model.addAttribute("erpStoreAdvertiserFriends", erpStoreAdvertiserFriends);
        return "modules/store/advertiser/erpStoreAdvertiserFriendsForm";
    }

    @RequiresPermissions("store:advertiser:erpStoreAdvertiserFriends:edit")
    @RequestMapping(value = "save")
    public String save(ErpStoreAdvertiserFriends erpStoreAdvertiserFriends, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, erpStoreAdvertiserFriends)) {
            return form(erpStoreAdvertiserFriends, model);
        }
        erpStoreAdvertiserFriendsService.save(erpStoreAdvertiserFriends);
        addMessage(redirectAttributes, "保存朋友圈广告主开通资料成功");
        return "redirect:" + Global.getAdminPath() + "/store/advertiser/erpStoreAdvertiserFriends/?repage";
    }

    /**
     * 删除
     *
     * @param erpStoreAdvertiserFriends
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("store:advertiser:erpStoreAdvertiserFriends:edit")
    @RequestMapping(value = "delete")
    public String delete(ErpStoreAdvertiserFriends erpStoreAdvertiserFriends, RedirectAttributes redirectAttributes) {
        erpStoreAdvertiserFriendsService.delete(erpStoreAdvertiserFriends);
        addMessage(redirectAttributes, "删除朋友圈广告主开通资料成功");
        return "redirect:" + Global.getAdminPath() + "/store/advertiser/erpStoreAdvertiserFriends/?repage";
    }

}