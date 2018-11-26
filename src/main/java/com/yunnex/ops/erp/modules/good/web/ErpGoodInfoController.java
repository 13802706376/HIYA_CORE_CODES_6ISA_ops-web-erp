package com.yunnex.ops.erp.modules.good.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
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

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.good.category.entity.ErpGoodCategory;
import com.yunnex.ops.erp.modules.good.category.service.ErpGoodCategoryService;
import com.yunnex.ops.erp.modules.good.entity.ErpGoodInfo;
import com.yunnex.ops.erp.modules.good.entity.ErpGoodService;
import com.yunnex.ops.erp.modules.good.service.ErpGoodInfoApiService;
import com.yunnex.ops.erp.modules.good.service.ErpGoodInfoService;

/**
 * 商品信息管理Controller
 * 
 * @author Frank
 * @version 2017-10-21
 */
@Controller
@RequestMapping(value = "${adminPath}/good/erpGoodInfo")
public class ErpGoodInfoController extends BaseController {

    @Autowired
    private ErpGoodInfoService erpGoodInfoService;

    @Autowired
    private ErpGoodCategoryService cateGoryService;

    @Autowired
    private ErpGoodInfoApiService erpGoodInfoApiService;

    @ModelAttribute
    public ErpGoodInfo get(@RequestParam(required = false) String id) {
        ErpGoodInfo entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = erpGoodInfoService.get(id);
        }
        if (entity == null) {
            entity = new ErpGoodInfo();
        }
        return entity;
    }

    @RequiresPermissions("good:erpGoodInfo:view")
    @RequestMapping(value = {"list", ""})
    public String list(ErpGoodInfo erpGoodInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ErpGoodInfo> page = erpGoodInfoService.findPage(new Page<ErpGoodInfo>(request, response), erpGoodInfo);
        List<ErpGoodInfo> list = page.getList();
        if (CollectionUtils.isNotEmpty(list)) {
            for (ErpGoodInfo good : list) {
                List<ErpGoodService> serviceList = good.getServiceList();
                if (CollectionUtils.isNotEmpty(serviceList)) {
                    for (ErpGoodService erpGoodService : serviceList) {
                        if (Constant.YES.equals(erpGoodService.getIsPackage())) {
                            good.getPackageServiceList().add(erpGoodService);
                        }
                        if (Constant.NO.equals(erpGoodService.getIsPackage())) {
                            good.getSingleServiceList().add(erpGoodService);
                        }
                    }
                }
                good.setServiceList(null);
            }
        }
        model.addAttribute("page", page);
        List<ErpGoodCategory> goodCateGoryList = cateGoryService.findList(new ErpGoodCategory());
        model.addAttribute("goodCateGoryList", goodCateGoryList);
        return "modules/good/erpGoodInfoList";
    }

    /**
     * 去编辑页面
     *
     * @param id
     * @return
     * @date 2018年5月29日
     */
    @RequestMapping("toEditPage")
    public String toEditPage() {
        return "modules/good/erpGoodEdit";
    }

    /**
     * 编辑
     *
     * @param id
     * @return
     * @date 2018年5月29日
     */
    @RequestMapping("edit")
    @ResponseBody
    public BaseResult edit(String id) {
        return erpGoodInfoService.edit(id);
    }

    /**
     * 点击确定保存或者更新商品对应的服务项目
     *
     * @param entity
     * @return
     * @date 2018年5月29日
     */
    @RequestMapping(value = "saveGoodService", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult saveGoodService(@RequestBody ErpGoodInfo entity) {
        return erpGoodInfoService.saveGoodService(entity);
    }

    @RequiresPermissions("good:erpGoodInfo:view")
    @RequestMapping(value = "form")
    public String form(ErpGoodInfo erpGoodInfo, Model model) {
        model.addAttribute("erpGoodInfo", erpGoodInfo);
        List<ErpGoodCategory> goodCateGoryList = cateGoryService.findList(new ErpGoodCategory());
        model.addAttribute("goodCateGoryList", goodCateGoryList);
        return "modules/good/erpGoodInfoForm";
    }

    @RequiresPermissions("good:erpGoodInfo:edit")
    @RequestMapping(value = "save")
    public String save(ErpGoodInfo erpGoodInfo, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, erpGoodInfo)) {
            return form(erpGoodInfo, model);
        }
        erpGoodInfoService.save(erpGoodInfo);
        addMessage(redirectAttributes, "保存商品信息成功");
        return "redirect:" + Global.getAdminPath() + "/good/erpGoodInfo/?repage";
    }

    @RequiresPermissions("good:erpGoodInfo:edit")
    @RequestMapping(value = "delete")
    public String delete(ErpGoodInfo erpGoodInfo, RedirectAttributes redirectAttributes) {
        erpGoodInfoService.delete(erpGoodInfo);
        addMessage(redirectAttributes, "删除商品信息成功");
        return "redirect:" + Global.getAdminPath() + "/good/erpGoodInfo/?repage";
    }


    @RequiresPermissions("good:erpGoodInfo:edit")
    @RequestMapping(value = "updateCategoryId")
    @ResponseBody
    public JSONObject updateCategoryId(ErpGoodInfo erpGoodInfo) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", erpGoodInfoService.updateCategoryId(erpGoodInfo) > 0);
        return jsonObject;
    }
    
   
    @RequiresPermissions("good:erpGoodInfo:edit")
    @RequestMapping(value = "sync")
    @ResponseBody
    public JSONObject sync() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", erpGoodInfoApiService.sync());
        return jsonObject;
    }
}
