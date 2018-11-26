package com.yunnex.ops.erp.modules.good.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.result.IllegalArgumentErrorResult;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.good.dao.ErpGoodInfoDao;
import com.yunnex.ops.erp.modules.good.entity.ErpGoodInfo;
import com.yunnex.ops.erp.modules.good.entity.ErpGoodService;

/**
 * 商品信息管理Service
 * 
 * @author Frank
 * @version 2017-10-21
 */
@Service
public class ErpGoodInfoService extends CrudService<ErpGoodInfoDao, ErpGoodInfo> {
    @Autowired
    private ErpGoodServiceService erpGoodServiceService;

    @Autowired
    private ErpGoodInfoDao erpGoodInfoDao;

    @Override
    public ErpGoodInfo get(String id) {
        return super.get(id);
    }

    @Override
    public List<ErpGoodInfo> findList(ErpGoodInfo erpGoodInfo) {
        return super.findList(erpGoodInfo);
    }

    @Override
    public Page<ErpGoodInfo> findPage(Page<ErpGoodInfo> page, ErpGoodInfo erpGoodInfo) {
        return super.findPage(page, erpGoodInfo);
    }

    @Transactional(readOnly = false)
    public void save(ErpGoodInfo erpGoodInfo) {
        super.save(erpGoodInfo);
    }

    @Transactional(readOnly = false)
    @Override
    public void delete(ErpGoodInfo erpGoodInfo) {
        super.delete(erpGoodInfo);
    }

    @Transactional(readOnly = false)
    public int updateCategoryId(ErpGoodInfo erpGoodInfo) {
        return erpGoodInfoDao.updateCategoryId(erpGoodInfo);
    }

    public ErpGoodInfo getDetail(String id) {
        return erpGoodInfoDao.getDetail(id);
    }

    @Transactional(readOnly = false)
    public boolean updateDetail(ErpGoodInfo erpGoodInfo) {
        return erpGoodInfoDao.updateDetail(erpGoodInfo) > 0;
    }
    
    public List<ErpGoodInfo> findwherecategory(){
    	return erpGoodInfoDao.findwherecategory();
    }

    public BaseResult edit(String id) {
        if (StringUtils.isBlank(id)) {
            return new IllegalArgumentErrorResult();
        }

        ErpGoodInfo entity = new ErpGoodInfo();
        entity.setId(id);
        // 查询该商品信息以及关联的服务配置信息
        List<ErpGoodInfo> list = erpGoodInfoDao.findList(entity);

        // 根据isPackage字段来分成两组
        processServiceList(list);

        BaseResult result = new BaseResult();
        result.setAttach(list);
        return result;
    }

    private void processServiceList(List<ErpGoodInfo> list) {
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
    }

    @Transactional(readOnly = false)
    public BaseResult saveGoodService(ErpGoodInfo entity) {
        if(entity==null||StringUtils.isBlank(entity.getId())) {
            return new IllegalArgumentErrorResult();
        }

        // 先删除再保存
        erpGoodServiceService.deleteByGoodId(entity.getId());

        // 添加商品配置的服务
        List<ErpGoodService> packageServiceList = entity.getPackageServiceList();
        if (CollectionUtils.isNotEmpty(packageServiceList)) {
            for (ErpGoodService erpGoodService : packageServiceList) {
                erpGoodService.setGoodId(entity.getId());
                erpGoodServiceService.save(erpGoodService);
            }
        }

        List<ErpGoodService> singleServiceList = entity.getSingleServiceList();
        if (CollectionUtils.isNotEmpty(singleServiceList)) {
            for (ErpGoodService erpGoodService : singleServiceList) {
                erpGoodService.setGoodId(entity.getId());
                erpGoodServiceService.save(erpGoodService);
            }
        }
        return new BaseResult();
    }

    public ErpGoodInfo findGoodAndCategoryById(String id) {
        return dao.findGoodAndCategoryById(id);
    }
}
