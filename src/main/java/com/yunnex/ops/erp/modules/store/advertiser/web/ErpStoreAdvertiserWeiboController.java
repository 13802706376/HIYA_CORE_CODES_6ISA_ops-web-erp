package com.yunnex.ops.erp.modules.store.advertiser.web;

import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.store.advertiser.dto.ShopWeiboResponseDto;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserWeibo;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserWeiboService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * 微博广告主开通资料Controller
 * @author yunnex
 * @version 2017-12-09
 */
@Controller
@RequestMapping(value = "${adminPath}/store/advertiser/erpStoreAdvertiserWeibo")
public class ErpStoreAdvertiserWeiboController extends BaseController {

    @Autowired
    private ErpStoreAdvertiserWeiboService erpStoreAdvertiserWeiboService;

    @ModelAttribute
    public ErpStoreAdvertiserWeibo get(@RequestParam(required = false) String id) {
        ErpStoreAdvertiserWeibo entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = erpStoreAdvertiserWeiboService.get(id);
        }
        if (entity == null) {
            entity = new ErpStoreAdvertiserWeibo();
        }
        return entity;
    }

    @RequiresPermissions("store:advertiser:erpStoreAdvertiserWeibo:view")
    @RequestMapping(value = {"list", ""})
    public String list(ErpStoreAdvertiserWeibo erpStoreAdvertiserWeibo, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ErpStoreAdvertiserWeibo> page = erpStoreAdvertiserWeiboService.findPage(new Page<ErpStoreAdvertiserWeibo>(request, response),
                        erpStoreAdvertiserWeibo);
        model.addAttribute("page", page);
        return "modules/store/advertiser/erpStoreAdvertiserWeiboList";
    }

    @RequiresPermissions("store:advertiser:erpStoreAdvertiserWeibo:view")
    @RequestMapping(value = "form")
    public String form(ErpStoreAdvertiserWeibo erpStoreAdvertiserWeibo, Model model) {
        model.addAttribute("erpStoreAdvertiserWeibo", erpStoreAdvertiserWeibo);
        return "modules/store/advertiser/erpStoreAdvertiserWeiboForm";
    }

    @RequiresPermissions("store:advertiser:erpStoreAdvertiserWeibo:edit")
    @RequestMapping(value = "save")
    public String save(ErpStoreAdvertiserWeibo erpStoreAdvertiserWeibo, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, erpStoreAdvertiserWeibo)) {
            return form(erpStoreAdvertiserWeibo, model);
        }
        erpStoreAdvertiserWeiboService.save(erpStoreAdvertiserWeibo);
        addMessage(redirectAttributes, "保存微博广告主开通资料成功");
        return "redirect:" + Global.getAdminPath() + "/store/advertiser/erpStoreAdvertiserWeibo/?repage";
    }

    /**
     * 删除
     *
     * @param erpStoreAdvertiserWeibo
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("store:advertiser:erpStoreAdvertiserWeibo:edit")
    @RequestMapping(value = "delete")
    public String delete(ErpStoreAdvertiserWeibo erpStoreAdvertiserWeibo, RedirectAttributes redirectAttributes) {
        erpStoreAdvertiserWeiboService.delete(erpStoreAdvertiserWeibo);
        addMessage(redirectAttributes, "删除微博广告主开通资料成功");
        return "redirect:" + Global.getAdminPath() + "/store/advertiser/erpStoreAdvertiserWeibo/?repage";
    }

    @RequestMapping("findByZhangbeiId")
    public @ResponseBody Set<ShopWeiboResponseDto> findByZhangbeiId(String zhangbeiId) {
        return erpStoreAdvertiserWeiboService.findByZhangbeiId(zhangbeiId);
    }

}
