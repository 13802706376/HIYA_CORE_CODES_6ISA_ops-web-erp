package com.yunnex.ops.erp.modules.promotion.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.result.IllegalArgumentErrorResult;
import com.yunnex.ops.erp.common.result.SystemErrorResult;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.UploadUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.diagnosis.dto.DiagnosisRequestDto;
import com.yunnex.ops.erp.modules.diagnosis.service.DiagnosisFormService;
import com.yunnex.ops.erp.modules.order.dto.CouponOutputRequestDto;
import com.yunnex.ops.erp.modules.order.service.ErpOrderCouponOutputService;
import com.yunnex.ops.erp.modules.order.service.ErpPromotionMaterialLogService;
import com.yunnex.ops.erp.modules.promotion.dto.ErpOrderFileRequestDto;
import com.yunnex.ops.erp.modules.promotion.dto.ErpOrderInputDetailRequestDto;
import com.yunnex.ops.erp.modules.promotion.service.ErpPromoteMaterialService;


@Controller
@RequestMapping(value = "${adminPath}/promotion/erpPromoteMaterial")
public class ErpPromoteMaterialController extends BaseController {

    @Autowired
    private ErpPromoteMaterialService erpPromoteMaterialService;
    @Autowired
    private ErpPromotionMaterialLogService erpPromotionMaterialLogService;
    @Autowired
    private DiagnosisFormService diagnosisFormService;
    @Autowired
    private ErpOrderCouponOutputService erpOrderCouponOutputService;
    @Value("${web.maxUploadSize}")
    private String maxUploadSize;


    /**
     * 获取推广资料管理列表数据
     *
     * @param orderNumber
     * @param shopName
     * @param status
     * @return
     * @date 2018年5月16日
     */
    /*@RequestMapping(value = "getPromotionMaterialList")
    @ResponseBody
    public BaseResult getPromotionMaterialList(String orderNumber, String shopName, String status) {
        return erpPromoteMaterialService.getPromotionMaterialList(orderNumber, shopName, status);
    }*/

    /**
     * 进入推广资料详情页面
     *
     * @param erpOrderSplitInfo
     * @param model
     * @return
     * @date 2018年5月16日
     */
    @RequestMapping(value = "promoteMaterialDetail")
    public String promoteInfoDetail() {
        return "modules/diagnosis/promoteInfoDetail";
    }

    /**
     * 获取推广资料详情列表数据
     *
     * @param procInsId
     * @param splitId
     * @return
     * @throws ServiceException
     * @date 2018年5月16日
     */
    @RequestMapping(value = "getPromotionalMaterialDetailList")
    @ResponseBody
    public BaseResult getPromotionalMaterialDetailList(String procInsId, String splitId) throws ServiceException {
        return erpPromoteMaterialService.getPromotionalMaterialDetailList(procInsId, splitId);
    }

    /**
     * 获取推广资料的内容
     *
     * @param id
     * @param splitId
     * @param procInsId
     * @return
     * @date 2018年5月16日
     */
    @RequestMapping(value = "getPromotiaoMaterialContent")
    @ResponseBody
    public BaseResult getPromotiaoMaterialContent(String id, String splitId, String procInsId) {
        return erpPromoteMaterialService.getPromotionMaterialContent(id, splitId, procInsId);
    }

    /**
     * 获取推广资料的操作日志
     *
     * @param splitId
     * @param promotionMaterialsId
     * @return
     * @date 2018年5月16日
     */
    @RequestMapping(value = "getPromotionMaterialLogs")
    @ResponseBody
    public BaseResult getPromotionMaterialLogs(String splitId, String promotionMaterialsId) {
        return erpPromotionMaterialLogService.getPromotionMaterialLogs(splitId, promotionMaterialsId);
    }

    /**
     * 修改的时候获取经营诊断推广资料的数据
     *
     * @param splitId
     * @return
     * @date 2018年5月16日
     */
    @RequestMapping(value = {"getDiagnosisDataForModifying"})
    @ResponseBody
    public BaseResult getDiagnosisDataForModifying(String splitId) {
        return diagnosisFormService.getDiagnosisDataForModifying(splitId);
    }

    /**
     * 点击修改完成的时候更新经营诊断推广资料数据
     *
     * @param diagnosisRequestDto
     * @return
     * @date 2018年5月16日
     */
    @RequestMapping(value = {"updateDiagnosisData"}, method = RequestMethod.POST)
    @ResponseBody
    public BaseResult updateDiagnosisData(@RequestBody DiagnosisRequestDto diagnosisRequestDto) {
        return diagnosisFormService.updateDiagnosisData(diagnosisRequestDto);
    }

    /**
     * 修改完成的时候更新卡券输出推广资料
     *
     * @param couponOutputRequestDto
     * @return
     * @date 2018年5月16日
     */
    @RequestMapping(value = "updateCouponOutputInfo", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult updateCouponOutputInfo(@RequestBody CouponOutputRequestDto couponOutputRequestDto) {
        return erpOrderCouponOutputService.updateCouponOutputInfo(couponOutputRequestDto);
    }

    /**
     * 修改完成的时候更新文本类型推广资料
     *
     * @param dto
     * @return
     * @throws ServiceException
     * @date 2018年5月16日
     */
    @RequestMapping(value = "updateErpOrderInputDetail", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult updateErpOrderInputDetail(@RequestBody ErpOrderInputDetailRequestDto dto) throws ServiceException {
        return erpPromoteMaterialService.updateErpOrderInputDetail(dto);
    }

    /**
     * 修改完成的时候更新文件类型的推广资料
     *
     * @param dto
     * @return
     * @throws ServiceException
     * @date 2018年5月16日
     */
    @RequestMapping(value = "updateErpOrderFile", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult updateErpOrderFile(@RequestBody ErpOrderFileRequestDto dto) throws ServiceException {
        return erpPromoteMaterialService.updateErpOrderFile(dto);
    }

    /**
     * 文件类型推广资料上传
     *
     * @param request
     * @param file
     * @return
     * @date 2018年5月16日
     */
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult uploadFile(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file) {
        if (null == file) {
            return new IllegalArgumentErrorResult();
        }

        BaseResult result = new BaseResult();
        try {
            long maxSize = Long.parseLong(maxUploadSize);
            if (file.getSize() > maxSize) {
                return result.error("-1", "请上传小于" + maxSize / 1024 + "kb的文件");
            }

            result.setAttach(UploadUtils.saveFile(request, file, maxSize));
        } catch (Exception e) {
            logger.error("系统异常", e);
            return new SystemErrorResult();
        }

        return result;
    }
}
