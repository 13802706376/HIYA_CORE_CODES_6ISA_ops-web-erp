package com.yunnex.ops.erp.modules.diagnosis.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.result.IllegalArgumentErrorResult;
import com.yunnex.ops.erp.common.result.SystemErrorResult;
import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.common.utils.FileUtils;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.utils.UploadUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.diagnosis.dto.CouponsPlanRequestDto;
import com.yunnex.ops.erp.modules.diagnosis.dto.PreparationStageRequestDto;
import com.yunnex.ops.erp.modules.diagnosis.dto.PropagandaKeyRequestDto;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisForm;
import com.yunnex.ops.erp.modules.diagnosis.service.DiagnosisFormService;
import com.yunnex.ops.erp.modules.diagnosis.service.DiagnosisFormValidateService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpHisSplitServiceApi;
import com.yunnex.ops.erp.modules.workflow.store.service.JykOrderChoiceStoreService;

/**
 * 经营诊断营销策划表单Controller
 * 
 * @author yunnex
 * @version 2018-03-29
 */
@Controller
@RequestMapping(value = "${adminPath}/diagnosis/diagnosisForm")
public class DiagnosisFormController extends BaseController {

    @Autowired
    private DiagnosisFormService diagnosisFormService;
    @Autowired
    private DiagnosisFormValidateService formValidateService;
    @Autowired
    private ErpOrderSplitInfoService erpOrderSplitInfoService;
    @Autowired
    private ErpHisSplitServiceApi erpHisSplitServiceApi;
    @Autowired
    private JykOrderChoiceStoreService jykOrderChoiceStoreService;
    @Autowired
    private ErpStoreInfoService erpStoreInfoService;

    @Value("${web.maxUploadSize}")
    private String maxUploadSize;
    // 图片保存目录
    @Value("${userfiles.basedir}")
    private String basedir;


    @ModelAttribute
    public DiagnosisForm get(@RequestParam(required = false) String id) {
        DiagnosisForm entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = diagnosisFormService.get(id);
        }
        if (entity == null) {
            entity = new DiagnosisForm();
        }
        return entity;
    }

    @RequiresPermissions("diagnosis:diagnosisForm:view")
    @RequestMapping(value = {"list", ""})
    public String list(DiagnosisForm diagnosisForm, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<DiagnosisForm> page = diagnosisFormService.findPage(new Page<DiagnosisForm>(request, response), diagnosisForm);
        model.addAttribute("page", page);
        return "modules/diagnosis/diagnosisFormList";
    }

    @RequiresPermissions("diagnosis:diagnosisForm:view")
    @RequestMapping(value = "form")
    public String form(DiagnosisForm diagnosisForm, Model model) {
        model.addAttribute("diagnosisForm", diagnosisForm);
        return "modules/diagnosis/diagnosisFormForm";
    }

    @RequiresPermissions("diagnosis:diagnosisForm:edit")
    @RequestMapping(value = "save")
    public String save(DiagnosisForm diagnosisForm, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, diagnosisForm)) {
            return form(diagnosisForm, model);
        }
        diagnosisFormService.save(diagnosisForm);
        addMessage(redirectAttributes, "保存经营诊断营销策划表单成功");
        return "redirect:" + Global.getAdminPath() + "/diagnosis/diagnosisForm/?repage";
    }

    @RequiresPermissions("diagnosis:diagnosisForm:edit")
    @RequestMapping(value = "delete")
    public String delete(DiagnosisForm diagnosisForm, RedirectAttributes redirectAttributes) {
        diagnosisFormService.delete(diagnosisForm);
        addMessage(redirectAttributes, "删除经营诊断营销策划表单成功");
        return "redirect:" + Global.getAdminPath() + "/diagnosis/diagnosisForm/?repage";
    }

    /**
     * 获取电话诊断阶段数据
     *
     * @param splitId
     * @return
     * @date 2018年3月30日
     */
    @RequestMapping(value = "byPhone/{splitId}", method = RequestMethod.GET)
    public @ResponseBody BaseResult findByPhone(@PathVariable String splitId) {
        DiagnosisForm form = diagnosisFormService.findByPhone(splitId);
        BaseResult result = new BaseResult();
        result.setAttach(form);
        return result;
    }

    /**
     * 保存电话诊断阶段数据
     *
     * @param splitId
     * @return
     */
    @RequestMapping(value = "byPhone", method = RequestMethod.POST)
    public @ResponseBody BaseResult saveByPhone(@RequestBody DiagnosisForm form) {
        return diagnosisFormService.saveByPhone(form);
    }

    /**
     * 获取电话后补充阶段数据
     *
     * @return
     * @date 2018年4月3日
     */
    @RequestMapping(value = "afterPhone/{splitId}", method = RequestMethod.GET)
    public @ResponseBody BaseResult findAfterPhone(@PathVariable String splitId) {
        DiagnosisForm form = diagnosisFormService.findAfterPhone(splitId);
        BaseResult result = new BaseResult();
        result.setAttach(form);
        return result;
    }

    /**
     * 保存电话后补充阶段数据
     *
     * @param splitId
     * @return
     */
    @RequestMapping(value = "afterPhone", method = RequestMethod.POST)
    public @ResponseBody BaseResult saveAfterPhone(@RequestBody DiagnosisForm form) {
        return diagnosisFormService.saveAfterPhone(form);
    }

    /**
     * 获取优惠形式和内容数据
     *
     * @param splitId
     * @return
     * @date 2018年3月29日
     */
    @RequestMapping(value = "discountTypeContent/{splitId}", method = RequestMethod.GET)
    public @ResponseBody BaseResult findDiscountTypeContent(@PathVariable String splitId) {
        DiagnosisForm form = diagnosisFormService.findDiscountTypeContent(splitId);
        BaseResult result = new BaseResult();
        result.setAttach(form);
        return result;
    }

    /**
     * 保存优惠形式和内容数据
     *
     * @param splitId
     * @return
     * @date 2018年3月29日
     */
    @RequestMapping(value = "discountTypeContent", method = RequestMethod.POST)
    public @ResponseBody BaseResult saveDiscountTypeContent(@RequestBody DiagnosisForm form) {
        return diagnosisFormService.saveDiscountTypeContent(form);
    }

    @ResponseBody
    @RequestMapping(value = "getPreparationStageData/{splitId}")
    public BaseResult getPreparationStageData(@PathVariable("splitId") String splitId) {
        // 获取经营诊断-电话前准备阶段详细数据
        return diagnosisFormService.getPreparationStageData(splitId);
    }

    @ResponseBody
    @RequestMapping(value = "savePreparationStageData", method = RequestMethod.POST)
    public BaseResult savePreparationStageData(@RequestBody PreparationStageRequestDto preparationStageDto) {
        // 保存经营诊断-电话前准备阶段详细数据
        return diagnosisFormService.savePreparationStageData(preparationStageDto);
    }

    @ResponseBody
    @RequestMapping(value = "getCouponsPlanData/{splitId}")
    public BaseResult getCouponsPlanData(@PathVariable String splitId) {
        // 策划方案-活动策划2（卡券策划）
        return diagnosisFormService.getCouponsPlanData(splitId);
    }

    @ResponseBody
    @RequestMapping(value = "saveCouponsPlanData", method = RequestMethod.POST)
    public BaseResult saveCouponsPlanData(@RequestBody CouponsPlanRequestDto couponsPlanDto) {
        // 保存 活动策划2（卡券策划）数据
        return diagnosisFormService.saveCouponsPlanData(couponsPlanDto);
    }

    @ResponseBody
    @RequestMapping(value = "getPropagandaKeyData/{splitId}")
    public BaseResult getPropagandaKeyData(@PathVariable String splitId) {
        // 策划方案-活动策划3（宣传重点）
        return diagnosisFormService.getPropagandaKeyData(splitId);
    }

    @ResponseBody
    @RequestMapping(value = "savePropagandaKeyData", method = RequestMethod.POST)
    public BaseResult savePropagandaKeyData(@RequestBody PropagandaKeyRequestDto propagandaKeyDto) {
        // 保存 活动策划2（卡券策划）数据
        return diagnosisFormService.savePropagandaKeyData(propagandaKeyDto);
    }

    /**
     * 删除图片
     *
     * @param removedImgUrl
     * @return
     * @date 2018年4月19日
     */
    @ResponseBody
    @RequestMapping(value = "deleteImage/{removedImgUrl}")
    public BaseResult deleteImage(@PathVariable String removedImgUrl) {
        if (StringUtils.isNotBlank(removedImgUrl)) {
            boolean flag = FileUtils.deleteFile(basedir + "/" + removedImgUrl);
            if (!flag) {
                return new BaseResult().error("-1", "删除图片失败");
            }
        }
        return new BaseResult();
    }

    /**
     * 上传重点宣传文案的图片
     * 
     * @param request
     * @param file
     * @return
     * @date 2018年1月31日
     * @author liyuanxing
     */
    @ResponseBody
    @RequestMapping(value = "upload")
    public BaseResult uploadImg(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file) {
        if (null == file) {
            return new IllegalArgumentErrorResult();
        }

        BaseResult result = new BaseResult();
        try {
            long maxSize = Long.parseLong(maxUploadSize);
            if (file.getSize() > maxSize) {
                return result.error("-1", "请上传小于" + maxSize / 1024 + "kb的图片");
            }

            result.setAttach(UploadUtils.saveFile(request, file, maxSize));
        } catch (Exception e) {
            logger.error("diagnosisForm上传图片系统异常", e);
            return new SystemErrorResult();
        }

        return result;
    }

    // 发布到小程序
    @RequestMapping(value = "/publishToWxapp")
    public @ResponseBody BaseResult publishToWxapp(String splitId) {
        logger.info("发布到小程序入参：splitId = {}", splitId);
        String msg = "发布到小程序结果：{}";

        BaseResult baseResult = formValidateService.validateForm(splitId);
        if (!BaseResult.isSuccess(baseResult)) {
            logger.info(msg, baseResult);
            return baseResult;
        }

        boolean flag = erpOrderSplitInfoService.publishToWxapp(splitId);
        if (!flag) {
            baseResult.error("-1", "发布到小程序失败！");
            logger.info(msg, baseResult);
            return baseResult;
        } else {
            String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(splitId);
            ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            if (storeInfo != null) {
                // 确认推广提案
                erpHisSplitServiceApi.marketingPlanning(splitId, DateUtils.formatDateTime(new Date()), storeInfo.getId(), storeInfo.getShortName());
            } else {
                logger.info("门店不存在！ storeId={}", storeId);
            }


        }

        logger.info(msg, baseResult);
        return baseResult;
    }

}