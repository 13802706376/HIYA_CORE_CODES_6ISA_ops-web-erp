package com.yunnex.ops.erp.modules.promotion.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.ImportCSVUtil;
import com.yunnex.ops.erp.common.utils.excel.ImportExcel;
import com.yunnex.ops.erp.modules.promotion.dao.ErpPromoteDataWeiboDao;
import com.yunnex.ops.erp.modules.promotion.entity.ErpPromoteDataWeibo;
import com.yunnex.ops.erp.modules.promotion.entity.ErpPromoteDataWeiboSum;

/**
 * 推广数据-微博Service
 * 
 * @author yunnex
 * @version 2018-05-10
 */
@Service
public class ErpPromoteDataWeiboService extends CrudService<ErpPromoteDataWeiboDao, ErpPromoteDataWeibo> {

    /**
     * 依据ID查询微博推广数据
     * 
     * @date 2018年5月9日
     * @author R/Q
     */
    public ErpPromoteDataWeibo queryWeiboPromoteDataById(ErpPromoteDataWeibo paramObj) {
        List<ErpPromoteDataWeibo> weiboList = dao.queryWeiboData(paramObj);
        ErpPromoteDataWeibo returnObj = CollectionUtils.isNotEmpty(weiboList) ? weiboList.get(0) : null;
        return returnObj;
    }

    /**
     * 
     * 业务定义：查询微博推广数据
     * 
     * @date 2018年5月9日
     * @author R/Q
     */
    public ErpPromoteDataWeiboSum queryWeiboPromoteData(ErpPromoteDataWeibo paramObj) {
        ErpPromoteDataWeiboSum returnObj = dao.queryWeiboDataSum(paramObj);
        if (returnObj != null) {
            returnObj.setWeiboDataList(dao.queryWeiboData(paramObj));// 查询列表数据
        }
        return returnObj;
    }

    /**
     * 
     * 业务定义：保存微博数据
     * 
     * @date 2018年5月10日
     * @author R/Q
     */
    @Transactional(readOnly = false)
    public Map<String, Object> saveWeiboData(ErpPromoteDataWeibo paramObj) {
        Map<String, Object> returnMap = Maps.newHashMap();
        try {
            if (paramObj.getIsNewRecord()) {
                dao.deleteBySplitIdAndDataTime(paramObj);
            }
            super.save(paramObj);
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_SUCCESS);
        } catch (ServiceException se) {
            logger.error("微博推广数据保存失败，paramObj={}，错误信息={}", paramObj, se);
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_FAIL);
        }
        return returnMap;
    }

    /**
     * 
     * 业务定义：导入微博数据
     * 
     * @date 2018年5月14日
     * @author R/Q
     */
    @Transactional(readOnly = false)
    public void importPrommteData(MultipartFile multipartFile, String splitOrderId) throws Exception {
        List<ErpPromoteDataWeibo> list = null;
        String fileName = multipartFile != null ? multipartFile.getOriginalFilename() : StringUtils.EMPTY;
        if (StringUtils.isBlank(fileName)) {// 判断文件类型
            throw new ServiceException("导入文档为空。");
        } else if (fileName.toLowerCase().endsWith("xls") || fileName.toLowerCase().endsWith("xlsx")) {
            ImportExcel impObj = new ImportExcel(multipartFile, 0, 0);
            list = impObj.getDataList(ErpPromoteDataWeibo.class);
        } else if (fileName.toLowerCase().endsWith("csv")) {
            ImportCSVUtil impObj = new ImportCSVUtil(multipartFile, 1);
            list = impObj.getDataList(ErpPromoteDataWeibo.class);
        } else {
            throw new ServiceException("文档格式不正确。");
        }
        if (CollectionUtils.isEmpty(list)) {
            throw new ServiceException("上传文件无有效数据。");
        }
        Map<Date, ErpPromoteDataWeibo> dataMap = Maps.newHashMap();
        for (int i = 0; i < list.size(); i++) {// 数据校验及数据操作
            if (list.get(i).getDataTime() == null) {
                throw new ServiceException(String.format("上传失败，第%s行数据没有对应时间。", i + 3));
            }
            // 手工计算'率'相关字段
            list.get(i).setFlowPercent(list.get(i).getFlowPercent() != null ? (list.get(i).getFlowPercent() * 100) : list.get(i).getFlowPercent());
            list.get(i).setAddAttentionPercent(list.get(i).getAddAttentionPercent() != null ? (list.get(i).getAddAttentionPercent() * 100) : list
                            .get(i).getAddAttentionPercent());
            list.get(i).setInteractionPercent(list.get(i).getInteractionPercent() != null ? (list.get(i).getInteractionPercent() * 100) : list.get(i)
                            .getInteractionPercent());
            dataMap.put(list.get(i).getDataTime(), list.get(i));// 用MAP去重
        }
        Collection<ErpPromoteDataWeibo> valueCollection = dataMap.values();
        List<ErpPromoteDataWeibo> weiboList = new ArrayList<ErpPromoteDataWeibo>(valueCollection);// Map转换List
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("splitOrderId", splitOrderId);
        paramMap.put("list", weiboList);
        dao.batchDelete(paramMap);// 批量删除导入数据中日期重复的数据
        dao.batchInsert(paramMap);// 持久化上传数据
    }

    /**
     * 
     * 业务定义：删除微博数据
     * 
     * @date 2018年5月10日
     * @author R/Q
     */
    public Map<String, Object> deleteWeiboData(ErpPromoteDataWeibo paramObj) {
        Map<String, Object> returnMap = Maps.newHashMap();
        try {
            super.delete(paramObj);
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_SUCCESS);
        } catch (ServiceException se) {
            logger.error("微博推广数据删除失败，paramObj={}，错误信息={}", paramObj, se);
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_FAIL);
        }
        return returnMap;
    }

    @Override
    @Transactional(readOnly = false)
    public void save(ErpPromoteDataWeibo erpPromoteDataWeibo) {
        super.save(erpPromoteDataWeibo);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ErpPromoteDataWeibo erpPromoteDataWeibo) {
        super.delete(erpPromoteDataWeibo);
    }

}
