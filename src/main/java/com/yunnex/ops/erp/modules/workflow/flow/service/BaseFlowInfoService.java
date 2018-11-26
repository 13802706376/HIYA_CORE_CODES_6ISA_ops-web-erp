package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.persistence.BaseEntity;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.shop.constant.ShopConstant;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.store.constant.StoreConstant;
import com.yunnex.ops.erp.modules.workflow.flow.dto.AuditStatusInfoDto;
import com.yunnex.ops.erp.modules.workflow.flow.dto.FlowInfoDto;
import com.yunnex.ops.erp.modules.workflow.flow.dto.PayAuditStatusDto;

/**
 * 流程信息公共接口
 */
public interface BaseFlowInfoService {

    /**
     * 创建结果对象
     *
     * @param flowInfoDto
     * @return
     */
    default FlowInfoDto createFlowInfoDto(FlowInfoDto flowInfoDto) {
        if (flowInfoDto == null) {
            flowInfoDto = new FlowInfoDto();
        }
        return flowInfoDto;
    }

    /**
     * 商户的进件信息
     *
     * @param shop
     */
    default void wrapPayInfo(ErpStoreInfoService erpStoreInfoService, FlowInfoDto flowInfoDto, ErpShopInfo shop) {
        if (shop == null) {
            return;
        }
        String shopId = shop.getId();
        flowInfoDto = createFlowInfoDto(flowInfoDto);
        AuditStatusInfoDto auditStatusInfoDto = flowInfoDto.getAuditStatusInfoDto();
        if (auditStatusInfoDto == null) {
            auditStatusInfoDto = new AuditStatusInfoDto();
            flowInfoDto.setAuditStatusInfoDto(auditStatusInfoDto);
        }

        // 口碑进件审核状态
        auditStatusInfoDto.setAliPayStatus(ShopConstant.AlipayState.getByName(shop.getAlipaState()));
        if (StringUtils.isBlank(auditStatusInfoDto.getAliPayStatus())) {
            auditStatusInfoDto.setAliPayStatus(Constant.SPRIT);
        }

        // 掌贝进件状态
        ErpStoreInfo mainStore = erpStoreInfoService.findismain(shop.getId(), BaseEntity.DEL_FLAG_NORMAL);
        if (mainStore != null) {
            auditStatusInfoDto.setMainStoreStatus(StoreConstant.StoreAuditStatus.getByStatus(mainStore.getAuditStatus()));
        }
        if (StringUtils.isBlank(auditStatusInfoDto.getMainStoreStatus())) {
            auditStatusInfoDto.setMainStoreStatus(Constant.SPRIT);
        }

        // 微信进件审核状态
        List<PayAuditStatusDto> wxpayAuditStatus = erpStoreInfoService.findWxpayAuditStatus(shopId);
        processAuditStatus(wxpayAuditStatus);
        auditStatusInfoDto.setWxPayAuditStatus(wxpayAuditStatus);

        // 银联进件审核状态
        List<PayAuditStatusDto> unionpayAuditStatus = erpStoreInfoService.findUnionpayAuditStatus(shopId);
        processAuditStatus(unionpayAuditStatus);
        auditStatusInfoDto.setUnionPayAuditStatus(unionpayAuditStatus);
    }

    /**
     * 审核状态显示处理
     *
     * @param auditStatusDtos
     */
    static void processAuditStatus(List<PayAuditStatusDto> auditStatusDtos) {
        if (!CollectionUtils.isEmpty(auditStatusDtos)) {
            auditStatusDtos.forEach(dto -> {
                if (StringUtils.isBlank(dto.getStoreName())) {
                    dto.setStoreName(Constant.SPRIT);
                }
                String auditStatusName = StoreConstant.PayAuditStatus.getByStatus(dto.getAuditStatus());
                
                if("1".equals(dto.getIsMain())&& Constant.YES.equals(dto.getNotOpenUnionpayFlag())&&StoreConstant.PayAuditStatus.getByStatus(0).equals(auditStatusName))
                    dto.setAuditStatusName("未提交（商户暂不开通）");
                else
                	dto.setAuditStatusName(auditStatusName);
                	
                if (StringUtils.isBlank(dto.getAuditStatusName())) {
                    dto.setAuditStatusName(Constant.SPRIT);
                }
            });
        }
    }

}
