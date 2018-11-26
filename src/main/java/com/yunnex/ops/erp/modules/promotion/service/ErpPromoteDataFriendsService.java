package com.yunnex.ops.erp.modules.promotion.service;

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
import com.yunnex.ops.erp.modules.promotion.dao.ErpPromoteDataFriendsDao;
import com.yunnex.ops.erp.modules.promotion.entity.ErpPromoteDataFriends;
import com.yunnex.ops.erp.modules.promotion.entity.ErpPromoteDataFriendsSum;

/**
 * 朋友圈推广数据Service
 * 
 * @author yunnex
 * @version 2018-05-09
 */
@Service
public class ErpPromoteDataFriendsService extends CrudService<ErpPromoteDataFriendsDao, ErpPromoteDataFriends> {

    /**
     * 依据ID查询朋友圈推广数据
     * 
     * @date 2018年5月9日
     * @author R/Q
     */
    public ErpPromoteDataFriends queryFriendsData(ErpPromoteDataFriends paramObj) {
        List<ErpPromoteDataFriends> friendsList = dao.queryFriendsData(paramObj);
        ErpPromoteDataFriends returnObj = CollectionUtils.isNotEmpty(friendsList) ? friendsList.get(0) : null;
        return returnObj;
    }

    /**
     * 
     * 业务定义：查询朋友圈推广数据
     * 
     * @date 2018年5月9日
     * @author R/Q
     */
    public ErpPromoteDataFriendsSum queryFriendsPromoteData(ErpPromoteDataFriends paramObj) {
        ErpPromoteDataFriendsSum returnObj = dao.queryFriendsDataSum(paramObj);
        if (returnObj != null) {
            returnObj.setFriendsDataList(dao.queryFriendsData(paramObj));// 查询列表数据
        }
        return returnObj;
    }

    /**
     * 
     * 业务定义：保存朋友圈数据
     * 
     * @date 2018年5月10日
     * @author R/Q
     */
    @Transactional(readOnly = false)
    public Map<String, Object> saveFriendsData(ErpPromoteDataFriends paramObj) {
        Map<String, Object> returnMap = Maps.newHashMap();
        try {
            if (paramObj.getIsNewRecord()) {
                dao.deleteBySplitIdAndDataTime(paramObj);
            }
            super.save(paramObj);
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_SUCCESS);
        } catch (ServiceException se) {
            logger.error("朋友圈推广数据保存失败，paramObj={}，错误信息={}", paramObj, se);
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_FAIL);
        }
        return returnMap;
    }

    /**
     * 
     * 业务定义：导入朋友圈数据
     * 
     * @date 2018年5月14日
     * @author R/Q
     */
    @Transactional(readOnly = false)
    public void importPrommteData(MultipartFile multipartFile, String splitOrderId) throws Exception {
        List<ErpPromoteDataFriends> list = null;
        String fileName = multipartFile != null ? multipartFile.getOriginalFilename() : StringUtils.EMPTY;
        if (StringUtils.isBlank(fileName)) {// 判断文件类型
            throw new ServiceException("导入文档为空。");
        } else if (fileName.toLowerCase().endsWith("xls") || fileName.toLowerCase().endsWith("xlsx")) {
            ImportExcel impObj = new ImportExcel(multipartFile, 2, 0);
            list = impObj.getDataList(ErpPromoteDataFriends.class);
        } else if (fileName.toLowerCase().endsWith("csv")) {
            ImportCSVUtil impObj = new ImportCSVUtil(multipartFile, 3);
            list = impObj.getDataList(ErpPromoteDataFriends.class);
        } else {
            throw new ServiceException("文档格式不正确。");
        }
        if (CollectionUtils.isEmpty(list)) {
            throw new ServiceException("上传文件无有效数据。");
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getDataTime() == null) {
                throw new ServiceException(String.format("上传失败，第%s行数据没有对应时间。", i + 3));
            }
        }
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("splitOrderId", splitOrderId);
        paramMap.put("list", list);
        List<ErpPromoteDataFriends> sumData = dao.calSumData(paramMap);// 利用数据库的数据特性和聚合函数计算朋友圈推广数据汇总
        dao.batchDelete(paramMap);// 批量删除导入数据中日期重复的数据
        dao.batchInsert(sumData);// 持久化上传数据
    }

    /**
     * 业务定义：删除朋友圈数据
     * 
     * @date 2018年5月10日
     * @author R/Q
     */
    public Map<String, Object> deleteFriendsData(ErpPromoteDataFriends erpPromoteDataFriends) {
        Map<String, Object> returnMap = Maps.newHashMap();
        try {
            super.delete(erpPromoteDataFriends);
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_SUCCESS);
        } catch (ServiceException se) {
            logger.error("朋友圈推广数据删除失败，paramObj={}，错误信息={}", erpPromoteDataFriends, se);
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_FAIL);
        }
        return returnMap;
    }

    @Override
    @Transactional(readOnly = false)
    public void save(ErpPromoteDataFriends erpPromoteDataFriends) {
        super.save(erpPromoteDataFriends);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ErpPromoteDataFriends erpPromoteDataFriends) {
        super.delete(erpPromoteDataFriends);
    }

}
