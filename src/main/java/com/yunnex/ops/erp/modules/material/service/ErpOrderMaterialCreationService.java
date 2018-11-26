package com.yunnex.ops.erp.modules.material.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.persistence.Pager;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.result.IllegalArgumentErrorResult;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.utils.FileUtils;
import com.yunnex.ops.erp.common.utils.excel.FastExcel;
import com.yunnex.ops.erp.common.web.Servlets;
import com.yunnex.ops.erp.modules.material.constant.MaterialCreationConstant;
import com.yunnex.ops.erp.modules.material.dao.ErpOrderMaterialCreationDao;
import com.yunnex.ops.erp.modules.material.dto.MaterialCreationExcelResponseDto;
import com.yunnex.ops.erp.modules.material.dto.MaterialCreationRequestDto;
import com.yunnex.ops.erp.modules.material.dto.MaterialCreationResponseDto;
import com.yunnex.ops.erp.modules.material.entity.ErpOrderMaterialCreation;
import com.yunnex.ops.erp.modules.message.constant.ServiceMessageTemplateConstants;
import com.yunnex.ops.erp.modules.message.service.ErpServiceMessageService;
import com.yunnex.ops.erp.modules.order.constant.OrderMaterialSyncStatusEnum;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderMaterialSyncLog;
import com.yunnex.ops.erp.modules.order.exception.OrderMaterialException;
import com.yunnex.ops.erp.modules.order.service.ErpOrderMaterialApiService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderMaterialSyncLogService;
import com.yunnex.ops.erp.modules.sys.service.SystemService;
import com.yunnex.ops.erp.modules.sys.utils.DictUtils;
import com.yunnex.ops.erp.modules.team.service.ErpTeamService;

/**
 * 物料制作Service
 * 
 * @author yunnex
 * @version 2018-05-25
 */
@Service
public class ErpOrderMaterialCreationService extends CrudService<ErpOrderMaterialCreationDao, ErpOrderMaterialCreation> {

    // 文件存放路径
    @Value("${userfiles.basedir}")
    private String fileBaseDir;
    private static final String MATERIAL_TIMESTAMP_DIR = "yyyyMMddHHmmss";
    private static final String ZIP_SUFFIX = ".zip";
    private static final String MATERIAL_PACKAGE_CN = "-物料包-";

    @Autowired
    private ErpOrderMaterialCreationDao erpOrderMaterialCreationDao;
    @Autowired
    private SystemService systemService;
    @Autowired
    private ErpOrderMaterialApiService orderMaterialApiService;
    @Autowired
    private ErpServiceMessageService serviceMessageService;
    @Autowired
    private ErpOrderMaterialSyncLogService orderMaterialSyncLogService;
    @Autowired
    private ErpTeamService teamService;

    public ErpOrderMaterialCreation get(String id) {
        return super.get(id);
    }

    @Transactional(readOnly = false)
    public void save(ErpOrderMaterialCreation erpOrderMaterialCreation) {
        super.save(erpOrderMaterialCreation);
    }

    @Transactional(readOnly = false)
    public void delete(ErpOrderMaterialCreation erpOrderMaterialCreation) {
        super.delete(erpOrderMaterialCreation);
    }

    public List<ErpOrderMaterialCreation> findMaterialCreation(String procInsId) {
        List<ErpOrderMaterialCreation> data = erpOrderMaterialCreationDao.findMaterialCreation(procInsId);
        return data;
    }

    public MaterialCreationResponseDto findPage(MaterialCreationRequestDto requestDto) {
        MaterialCreationResponseDto dto = new MaterialCreationResponseDto();
        List<ErpOrderMaterialCreation> data = dao.findByPage(requestDto);

        requestDto.setPage(false); // 计算总数时不分页，不排序
        long count = dao.count(requestDto);
        Pager<ErpOrderMaterialCreation> pager = new Pager<>(data, count);

        dto.setPager(pager);
        dto.setOperationAdviserList(systemService.getUserByRoleName("ops_adviser"));// 查找运营顾问列表
        dto.setMaterialCreationStatusList(DictUtils.getDictList("material_creation_status"));// 物料制作状态字典信息
        return dto;
    }

    @Transactional(readOnly = false)
    public BaseResult confirmOrder(ErpOrderMaterialCreation entity) {
        if (entity == null || StringUtils.isBlank(entity.getId()) || StringUtils.isBlank(entity.getProviderName()) || entity
                        .getCost() == null || entity.getPlaceOrderTime() == null) {
            return new IllegalArgumentErrorResult();
        }
        entity.setStatus(MaterialCreationConstant.PLACED_ORDER);
        entity.setStatusName(MaterialCreationConstant.PLACED_ORDER_CN);
        entity.preUpdate();
        erpOrderMaterialCreationDao.update(entity);
        syncMaterialStatus(entity.getYsOrderId());
        return new BaseResult();
    }

    @Transactional(readOnly = false)
    public BaseResult confirmSendOff(ErpOrderMaterialCreation entity) {
        if (StringUtils.isBlank(entity.getId()) || StringUtils.isBlank(entity.getLogisticsNumber())) {
            return new IllegalArgumentErrorResult();
        }

        entity.setStatus(MaterialCreationConstant.IN_TRANSIT);
        entity.setStatusName(MaterialCreationConstant.IN_TRANSIT_CN);
        entity.preUpdate();
        erpOrderMaterialCreationDao.update(entity);
        syncMaterialStatus(entity.getYsOrderId());
        return new BaseResult();
    }

    @Transactional(readOnly = false)
    public BaseResult confirmArrived(ErpOrderMaterialCreation entity) {
        if (StringUtils.isBlank(entity.getId()) || entity.getDeliverTime() == null) {
            return new IllegalArgumentErrorResult();
        }
        entity.setStatus(MaterialCreationConstant.ARRIVED);
        entity.setStatusName(MaterialCreationConstant.ARRIVED_CN);
        entity.preUpdate();
        erpOrderMaterialCreationDao.update(entity);
        syncMaterialStatus(entity.getYsOrderId());
        // 获取物料制作信息
        ErpOrderMaterialCreation materialCreation = erpOrderMaterialCreationDao.get(entity.getId());
        // 发送服务通知
        serviceMessageService.managerDeliveryMessageByProcInsId(materialCreation.getProcInsId(),
                        ServiceMessageTemplateConstants.NodeType.MATERIAL_PRODUCTION_ARRIVE, ServiceMessageTemplateConstants.STATUS_BEGIN);
        return new BaseResult();
    }

    /**
     * 同步物料制作状态到易商平台
     * 
     * @param ysOrderId
     */
    @Transactional
    public void syncMaterialStatus(Long ysOrderId) {
        String msg = "同步物料制作状态到易商平台！";
        logger.info("{}ysOrderId={}", msg, ysOrderId);
        // 为了向后兼容，易商订单ID可为空，此时这里不作处理
        if (ysOrderId == null) {
            return;
        }

        try {
            BaseResult result = orderMaterialApiService.syncMaterialStatus(ysOrderId);
            if (BaseResult.isSuccess(result)) {
                // 如果原来的状态为物料制作状态同步失败, 则改为正常
                ErpOrderMaterialSyncLog syncLog = new ErpOrderMaterialSyncLog();
                syncLog.setYsOrderId(ysOrderId);
                syncLog.setRecoverDate(new Date());
                String status = OrderMaterialSyncStatusEnum.NORMAL.getName();
                syncLog.setSyncStatus(status);
                syncLog.setSyncStatusName(OrderMaterialSyncStatusEnum.getByName(status));
                syncLog.preUpdate();
                orderMaterialSyncLogService.updateStatus(syncLog, OrderMaterialSyncStatusEnum.SYNC_MATERIAL_STATUS_FAILED.getName());
                logger.info("{}成功！", msg);
            }
        } catch (RuntimeException e) {
            logger.error("{}失败!", msg, e);
            throw new OrderMaterialException(OrderMaterialSyncStatusEnum.SYNC_MATERIAL_STATUS_FAILED.getDisplayName());
        }
    }

    public BaseResult export(MaterialCreationRequestDto requestDto, HttpServletResponse response) {
        logger.info("物料制作 - Excel导出入参：requestDto = {}", JSON.toJSON(requestDto));
        // 按条件查找，但不分页
        requestDto.setPageNo(1);
        requestDto.setPageSize(Integer.MAX_VALUE);
        List<MaterialCreationExcelResponseDto> list = erpOrderMaterialCreationDao.findByPageWithExcel(requestDto);

        BaseResult result = new BaseResult();
        if (CollectionUtils.isEmpty(list)) {
            result.setCode(BaseResult.CODE_ERROR_ARG);
            result.setMessage("在此条件下查无数据！");
            return result;
        }

        try {
            FastExcel.exportExcel(response, "物料制作管理", list);
        } catch (IOException e) {
            String msg = "物料制作列表导出失败！";
            logger.error(msg, e);
            result.setCode(BaseResult.CODE_ERROR_ARG);
            result.setMessage(msg);
            return result;
        }
        return result;
    }

    @Transactional
    public BaseResult update(ErpOrderMaterialCreation entity) {
        logger.info("更新物料制作内容入参：{}", JSON.toJSON(entity));
        if (entity == null || StringUtils.isBlank(entity.getId())) {
            return new IllegalArgumentErrorResult();
        }

        entity.preUpdate();
        erpOrderMaterialCreationDao.update(entity);
        return new BaseResult();
    }

    public ErpOrderMaterialCreation findByOrderNumber(String orderNumber) {
        return dao.findByOrderNumber(orderNumber);
    }

    public ErpOrderMaterialCreation findByYsOrderId(Long ysOrderId) {
        return dao.findByYsOrderId(ysOrderId);
    }

    @Transactional
    public void updateAdviser(String procInsId, String userId) {
        dao.updateAdviser(procInsId, userId);
    }

    /**
     * 根据流程id获取 物料信息
     *
     * @param procInsId
     * @return
     * @date 2018年7月24日
     * @author linqunzhi
     */
    public ErpOrderMaterialCreation getByProcInsId(String procInsId) {
        ErpOrderMaterialCreation result = dao.getByProcInsId(procInsId);
        return result;
    }

    /**
     * 下载物料包
     * 
     * @param orderNumber
     * @param response
     */
    public void downloadMaterialPackage(String orderNumber, HttpServletResponse response) throws IOException {
        logger.info("下载物料包入参：orderNumber={}", orderNumber);
        ErpOrderMaterialCreation materialCreation = dao.findByOrderNumber(orderNumber);
        String materialPath;
        if (materialCreation == null || StringUtils.isBlank(materialPath = materialCreation.getMaterialPath())) {
            String msg = "该订单没有物料包！";
            logger.error("{}, orderNumber={}", msg, orderNumber);
            Servlets.setHtmlHeader(response);
            response.getWriter().print(msg);
            return;
        }

        String dateDir = new SimpleDateFormat(MATERIAL_TIMESTAMP_DIR).format(new Date());
        String newDir = materialPath.substring(Constant.ZERO, materialPath.length() - Constant.ONE);
        String newSrcDir = newDir.substring(Constant.ZERO, newDir.lastIndexOf(Constant.SPRIT) + Constant.ONE);
        // 新文件夹名称: ${商户名称}-物料包-${yyyyMMddHHmmss}
        String destDir = newSrcDir + materialCreation.getShopName() + MATERIAL_PACKAGE_CN + dateDir;
        String zipName = destDir + ZIP_SUFFIX;
        File srcDirFile = new File(materialPath);
        File destDirFile = new File(destDir);
        File zipFile = new File(zipName);

        if (!srcDirFile.exists()) {
            String msg = "物料包不存在！";
            logger.error("{}, orderNumber={}", msg, orderNumber);
            Servlets.setHtmlHeader(response);
            response.getWriter().print(msg);
            return;
        }

        try (OutputStream os = response.getOutputStream()) {
            // 复制源文件夹并起一个带当前时间戳的新名称，然后压缩
            FileUtils.copyDirectory(srcDirFile, destDirFile);
            FileUtils.zipCompress(destDir, zipName);
            // 下载
            Servlets.setFileDownloadHeader(response, FilenameUtils.getName(zipName));
            FileUtils.copyFile(zipFile, os);
        } catch (IOException e) {
            String msg = "下载失败！";
            logger.error("{}, orderNumber={}", msg, orderNumber, e);
            Servlets.setHtmlHeader(response);
            response.getWriter().print(msg);
        } finally {
            try {
                // 下载完成后删掉
                FileUtils.deleteDirectory(destDirFile);
                FileUtils.forceDelete(zipFile);
            } catch (IOException e) {
                logger.error("删除文件失败！orderNumber={}", orderNumber, e);
            }
        }
    }
    
    @Transactional(readOnly = false)
    public void changeRoleUser(String procInsId, String userId, String roleName) {
    	erpOrderMaterialCreationDao.changeRoleUser(procInsId, userId, roleName);
    }

}
