package com.yunnex.ops.erp.modules.promotion.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.result.IllegalArgumentErrorResult;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.common.utils.FileUtils;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisForm;
import com.yunnex.ops.erp.modules.diagnosis.service.DiagnosisFormService;
import com.yunnex.ops.erp.modules.order.constant.OrderSplitConstants;
import com.yunnex.ops.erp.modules.order.dto.CouponOutputResponseDto;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderCouponOutput;
import com.yunnex.ops.erp.modules.order.entity.ErpPromotionMaterialLog;
import com.yunnex.ops.erp.modules.order.service.ErpOrderCouponOutputService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.order.service.ErpPromotionMaterialLogService;
import com.yunnex.ops.erp.modules.promotion.constant.PromotionConstants;
import com.yunnex.ops.erp.modules.promotion.dao.ErpPromoteDataFriendsDao;
import com.yunnex.ops.erp.modules.promotion.dto.DiagnosisResponseDto;
import com.yunnex.ops.erp.modules.promotion.dto.ErpOrderFileRequestDto;
import com.yunnex.ops.erp.modules.promotion.dto.ErpOrderInputDetailRequestDto;
import com.yunnex.ops.erp.modules.promotion.dto.PromotionMaterialsResponseDto;
import com.yunnex.ops.erp.modules.promotion.entity.ErpPromoteDataFriends;
import com.yunnex.ops.erp.modules.sys.entity.Dict;
import com.yunnex.ops.erp.modules.sys.utils.DictUtils;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderInputDetail;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpOrderFileService;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpOrderInputDetailService;
import com.yunnex.ops.erp.modules.workflow.user.entity.ErpOrderFlowUser;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

@Service
public class ErpPromoteMaterialService extends CrudService<ErpPromoteDataFriendsDao, ErpPromoteDataFriends> {
    @Autowired
    private ErpOrderSplitInfoService erpOrderSplitInfoService;
    @Autowired
    private ErpOrderFileService orderfileService;
    @Autowired
    private ErpOrderInputDetailService orderInputDetailService;
    @Autowired
    private ErpOrderCouponOutputService erpOrderCouponOutputService;
    @Autowired
    private DiagnosisFormService diagnosisFormService;
    @Autowired
    private ErpOrderFileService erpOrderFileService;
    @Autowired
    private ErpOrderInputDetailService erpOrderInputDetailService;
    @Autowired
    private ErpPromotionMaterialLogService erpPromotionMaterialLogService;
    @Autowired
    private ErpOrderFlowUserService erpOrderFlowUserService;
    // 图片保存目录
    @Value("${userfiles.basedir}")
    private String basedir;
    // 资源域名
    @Value("${domain.erp.res}")
    private String resDomain;

    /**
     * 获取推广资料管理列表
     *
     * @param orderNumber
     * @param shopName
     * @param status
     * @return
     * @date 2018年5月16日
     */
    @Transactional
    /* public BaseResult getPromotionMaterialList(String orderNumber, String shopName, String status) {
        boolean permitted = SecurityUtils.getSubject().isPermitted(PromotionConstants.VIEW_ALL_PROMOTION_MATERIALS);
        // 默认只查看负责该订单的推广资料
        String planningExpert = UserUtils.getUser().getId();
        if (permitted) {
            planningExpert = null;
        }
        logger.info("-----------获取推广资料管理列表 start-----------------");
        logger.info("入参情况:orderNumber={}，shopName={}，status={}，planningExpert={}", orderNumber, shopName, status, planningExpert);
        List<ErpOrderSplitInfo> promotionalMaterials = erpOrderSplitInfoService.promotionalMaterials(orderNumber, shopName, status, planningExpert);
    
        BaseResult res = new BaseResult();
        res.setAttach(promotionalMaterials);
        logger.info("-----------获取推广资料管理列表 end-----------------");
        return res;
    }*/

    /**
     * 获取推广资料详情列表
     *
     * @param procInsId
     * @param splitId
     * @return
     * @date 2018年5月16日
     */
    public BaseResult getPromotionalMaterialDetailList(String procInsId, String splitId) {
        logger.info("-----------获取推广资料详情列表 start-----------------");
        logger.info("入参情况:procInsId={},splitId={}", procInsId, splitId);
        if (StringUtils.isBlank(procInsId) || StringUtils.isBlank(splitId)) {
            return new IllegalArgumentErrorResult();
        }

        PromotionMaterialsResponseDto responseDto = new PromotionMaterialsResponseDto();
        responseDto.setSplitId(splitId);

        // 任务相关文件（由于文件标题存在2个相同的需要处理一下）
        List<ErpOrderFile> erpOrderFiles = orderfileService.findListByProcInsId(procInsId);
        erpOrderFiles = processOrderFiles(erpOrderFiles);
        responseDto.setErpOrderFiles(erpOrderFiles);

        // 任务相关资料
        List<ErpOrderInputDetail> erpOrderInputDetails = orderInputDetailService.findListBySplitId(splitId);
        if (CollectionUtils.isNotEmpty(erpOrderInputDetails)) {
            processOrderInputDetails(erpOrderInputDetails);
            responseDto.setErpOrderInputDetails(erpOrderInputDetails);
        }

        Map<String, String> map;
        // 卡券信息
        List<ErpOrderCouponOutput> erpOrderCouponOutputs = erpOrderCouponOutputService.findListBySplitId(splitId);
        if (CollectionUtils.isNotEmpty(erpOrderCouponOutputs)) {
            // 卡券信息推广资料的创建人,创建时间,卡券信息推广资料标识
            map = new HashMap<>();
            map.put("createrName", erpOrderCouponOutputs.get(0).getCreaterName());
            map.put("createTime", DateUtils.formatDate(erpOrderCouponOutputs.get(0).getCreateDate(), "yyyy-MM-dd HH:mm"));
            responseDto.setCouponInfoPromotionMaterialInfo(map);
        }

        // 经营诊断推广资料
        DiagnosisForm diagnosisForm = diagnosisFormService.findBySplitIdWithCreaterName(splitId);
        if (null != diagnosisForm) {
            // 经营诊断推广资料标识
            map = new HashMap<>();
            map.put(PromotionConstants.DIAGNOSIS_COPYWRITING, "经营诊断与方案策划（文案）");
            map.put(PromotionConstants.DIAGNOSIS_CONSULTANT, "经营诊断与方案策划（投放顾问）");
            map.put(PromotionConstants.DIAGNOSIS_DESIGN, "经营诊断与方案策划（设计）");
            map.put(PromotionConstants.DIAGNOSIS_MERCHANT, "经营诊断与方案策划（商户）");
            responseDto.setDiagnosisPromotionMaterialType(map);
            map = new HashMap<>();
            map.put("createrName", diagnosisForm.getCreaterName());
            map.put("createTime", DateUtils.formatDate(diagnosisForm.getCreateDate(), "yyyy-MM-dd HH:mm"));
            responseDto.setDiagnosisPromotionMaterialInfo(map);
        }
        BaseResult res = new BaseResult();
        res.setAttach(responseDto);
        logger.info("出参情况:BaseResult={}", res);
        logger.info("-----------获取推广资料详情列表 end-----------------");
        return res;
    }

    /**
     * 根据推广资料类型ID和分单ID或流程ID查询推广资料内容
     *
     * @param id
     * @param splitId
     * @return
     * @date 2018年5月14日
     */
    public BaseResult getPromotionMaterialContent(String id, String splitId, String procInsId) {
        logger.info("-----------根据推广资料类型ID和分单ID或流程ID查询推广资料内容 start-----------------");
        logger.info("入参情况:id={}，splitId={},procInsId={}", id, splitId, procInsId);

        // 入参校验
        if (StringUtils.isBlank(id) || StringUtils.isBlank(splitId)||StringUtils.isBlank(procInsId)) {
            return new IllegalArgumentErrorResult();
        }

        // 返回结果
        BaseResult result = new BaseResult();
        // 经营诊断类型
        if (id.startsWith(PromotionConstants.DIAGNOSIS)) {
            DiagnosisForm diagnosisForm = diagnosisFormService.getDiagnosisDataBySplitId(splitId);
            DiagnosisResponseDto dto = new DiagnosisResponseDto();
            dto.setDiagnosisForm(diagnosisForm);
            // 负责该订单的策划专家有修改权限
            dto.setModifyFlag(hasRight(splitId, JykFlowConstants.Planning_Expert));
            result.setAttach(dto);
        }
        // 卡券类型
        else if (id.equals(PromotionConstants.COUPON_INFO)) {
            List<ErpOrderCouponOutput> erpOrderCouponOutputs = erpOrderCouponOutputService.findListBySplitId(splitId);
            CouponOutputResponseDto dto = new CouponOutputResponseDto();
            dto.setErpOrderCouponOutputs(erpOrderCouponOutputs);

            // 获取卡券链接类型字典信息
            List<Dict> dictList = DictUtils.getDictList(PromotionConstants.COUPON_LINK_CATEGORY);
            dto.setCouponLinkCategoryDics(dictList);
            // 当前登录用户是否有修改权限(负责该订单的策划专家有修改权限资格)
            dto.setModifyFlag(hasRight(splitId, JykFlowConstants.Planning_Expert));
            result.setAttach(dto);
        }
        // 其他类型（任务相关文件，任务相关资料）
        else {
            String[] split = id.split(PromotionConstants.COLON);
            // 任务相关文件
            if (PromotionConstants.ERP_ORDER_FILE.equals(split[0])) {
                List<ErpOrderFile> erpOrderFiles = orderfileService.findByProcInsIdAndTileName(split[1], procInsId);
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("resDomain", resDomain);
                resultMap.put("erpOrderFiles", erpOrderFiles);

                result.setAttach(resultMap);
            }
            // 任务相关资料
            else if (PromotionConstants.ERP_ORDER_INPUT_DETAIL.equals(split[0])) {
                ErpOrderInputDetail erpOrderInputDetail = orderInputDetailService.get(split[1]);
                result.setAttach(erpOrderInputDetail);
            }
        }

        logger.info("出参情况:BaseResult={}", result);
        logger.info("-----------根据推广资料类型ID和分单ID或流程ID查询推广资料内容 end-----------------");
        return result;
    }

    private Boolean hasRight(String splitId, String flowUserId) {
        Boolean flag = false;
        ErpOrderFlowUser erpOrderFlowUser = erpOrderFlowUserService.findBySplitIdAndFlowUser(splitId, flowUserId);
        if (erpOrderFlowUser != null && StringUtils.isNotBlank(erpOrderFlowUser.getUser().getId())) {
            String userId = UserUtils.getUser().getId();
            if (erpOrderFlowUser.getUser().getId().equals(userId)) {
                flag = true;
            }
        }
        return flag;
    }

    private List<ErpOrderFile> processOrderFiles(List<ErpOrderFile> erpOrderFiles) {
        Map<String, ErpOrderFile> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(erpOrderFiles)) {
            for (ErpOrderFile erpOrderFile : erpOrderFiles) {
                if (map.containsKey(erpOrderFile.getFileTitle())) {
                    continue;
                }
                erpOrderFile.setId(PromotionConstants.ERP_ORDER_FILE + PromotionConstants.COLON + erpOrderFile.getFileTitle());
                erpOrderFile.setCreateTime(DateUtils.formatDate(erpOrderFile.getCreateDate(), "yyyy-MM-dd HH:mm"));
                map.put(erpOrderFile.getFileTitle(), erpOrderFile);
            }
        }
        return new ArrayList<ErpOrderFile>(map.values());
    }

    private void processOrderInputDetails(List<ErpOrderInputDetail> erpOrderInputDetails) {
        for (ErpOrderInputDetail inputDetail : erpOrderInputDetails) {
            inputDetail.setId(PromotionConstants.ERP_ORDER_INPUT_DETAIL + PromotionConstants.COLON + inputDetail.getId());
            inputDetail.setCreateTime(DateUtils.formatDate(inputDetail.getCreateDate(), "yyyy-MM-dd HH:mm"));
        }
    }

    @Transactional
    public BaseResult updateErpOrderInputDetail(ErpOrderInputDetailRequestDto dto) {
        logger.info("-----------更新文本类型推广资料（任务相关资料） start-----------------");
        logger.info("入参情况:ErpOrderInputDetailRequestDto={}", dto);

        if (StringUtils.isBlank(dto.getSplitId()) || StringUtils.isBlank(dto.getPromotionMaterialId()) || StringUtils
                        .isBlank(dto.getErpOrderInputDetail().getId())) {
            return new IllegalArgumentErrorResult();
        }
        ErpOrderInputDetail erpOrderInputDetailDB = erpOrderInputDetailService.get(dto.getErpOrderInputDetail().getId());
        erpOrderInputDetailDB.setInputDetail(dto.getErpOrderInputDetail().getInputDetail());
        erpOrderInputDetailService.save(dto.getErpOrderInputDetail());

        // 新增推广资料操作日志
        ErpPromotionMaterialLog log = new ErpPromotionMaterialLog(dto.getSplitId(), dto.getPromotionMaterialId(), OrderSplitConstants.MODIFYING);
        erpPromotionMaterialLogService.save(log);

        BaseResult res = new BaseResult();
        logger.info("出参情况:BaseResult={}", res);
        logger.info("-----------更新文本类型推广资料（任务相关资料） end-----------------");
        return res;

    }

    @Transactional
    public BaseResult updateErpOrderFile(ErpOrderFileRequestDto dto) {
        logger.info("-----------更新任务相关文件信息-修改完成 start-----------------");
        logger.info("入参情况:ErpOrderFileRequestDto={}", dto);

        if (StringUtils.isBlank(dto.getSplitId()) || StringUtils.isBlank(dto.getPromotionMaterialId())) {
            return new IllegalArgumentErrorResult();
        }

        // 新增任务相关文件
        List<ErpOrderFile> addedErpOrderFiles = dto.getAddedErpOrderFiles();
        if (CollectionUtils.isNotEmpty(addedErpOrderFiles)) {
            for (ErpOrderFile erpOrderFile : addedErpOrderFiles) {
                erpOrderFileService.save(erpOrderFile);
            }
        }

        // 删除任务相关文件
        List<String> removedErpOrderFileIds = dto.getRemovedErpOrderFileIds();
        if (CollectionUtils.isNotEmpty(removedErpOrderFileIds)) {
            List<ErpOrderFile> removedErpOrderFiles = erpOrderFileService.findListByIds(removedErpOrderFileIds);
            int value = erpOrderFileService.batchDelete(removedErpOrderFileIds);
            if (value > 0 && CollectionUtils.isNotEmpty(removedErpOrderFiles)) {
                for (ErpOrderFile erpOrderFile : removedErpOrderFiles) {
                    if (StringUtils.isNotEmpty(erpOrderFile.getFilePath())) {
                        FileUtils.deleteFile(basedir + "/" + erpOrderFile.getFilePath());
                    }
                }
            }
        }

        // 新增推广资料操作日志
        ErpPromotionMaterialLog log = new ErpPromotionMaterialLog(dto.getSplitId(), dto.getPromotionMaterialId(), OrderSplitConstants.MODIFYING);
        erpPromotionMaterialLogService.save(log);

        BaseResult res = new BaseResult();
        logger.info("出参情况:BaseResult={}", res);
        logger.info("-----------更新任务相关文件信息-修改完成 end-----------------");
        return res;
    }
}
