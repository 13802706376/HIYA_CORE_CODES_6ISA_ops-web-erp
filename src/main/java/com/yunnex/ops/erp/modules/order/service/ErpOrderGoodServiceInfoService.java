package com.yunnex.ops.erp.modules.order.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.modules.order.dao.ErpOrderGoodServiceInfoDao;
import com.yunnex.ops.erp.modules.order.dto.FlowServiceItemLinkDto;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderGoodServiceInfo;

/**
 * 订单商品服务Service
 * 
 * @author yunnex
 * @version 2018-06-02
 */
@Service
public class ErpOrderGoodServiceInfoService extends CrudService<ErpOrderGoodServiceInfoDao, ErpOrderGoodServiceInfo> {
    @Autowired
    private ErpOrderGoodServiceInfoDao erpOrderGoodServiceInfoDao;
    @Autowired
    @Lazy(true)
    private ErpOrderOriginalInfoService erpOrderOriginalInfoService;

	public ErpOrderGoodServiceInfo get(String id) {
		return super.get(id);
	}
	
	public List<ErpOrderGoodServiceInfo> findList(ErpOrderGoodServiceInfo erpOrderGoodServiceInfo) {
		return super.findList(erpOrderGoodServiceInfo);
	}
	
	public Page<ErpOrderGoodServiceInfo> findPage(Page<ErpOrderGoodServiceInfo> page, ErpOrderGoodServiceInfo erpOrderGoodServiceInfo) {
		return super.findPage(page, erpOrderGoodServiceInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(ErpOrderGoodServiceInfo erpOrderGoodServiceInfo) {
		super.save(erpOrderGoodServiceInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(ErpOrderGoodServiceInfo erpOrderGoodServiceInfo) {
		super.delete(erpOrderGoodServiceInfo);
	}
	
    /**
     * 保存订单商品服务信息
     *
     * @param orderId
     * @date 2018年6月2日
     */
    @Transactional(readOnly = false)
    public void saveOrderGoodServiceInfo(String orderId) {
        if (StringUtils.isBlank(orderId)) {
            throw new ServiceException("订单数据有误，无法生成对应商品服务信息");
        }
        super.dao.addByOrderId(orderId);
    }

    /**
     * 根据订单编号查询订单下面的商品服务信息
     *
     * @param orderId
     * @return
     * @date 2018年6月2日
     */
    public List<Map<String, Object>> getDeliveryServiceByOrderId(String orderId) {
        if (StringUtils.isBlank(orderId)) {
            throw new ServiceException("orderId不能为空");
        }
        return erpOrderGoodServiceInfoDao.getDeliveryServiceByOrderId(orderId);
    }

    /**
     * 根据订单编号查询订单下面的商品服务信息
     *
     * @param orderId
     * @return
     * @date 2018年6月2日
     */
    public List<ErpOrderGoodServiceInfo> getOrderGoodServiceInfo(String orderId) {
        if (StringUtils.isBlank(orderId)) {
            throw new ServiceException("orderId不能为空");
        }

        List<ErpOrderGoodServiceInfo> list = erpOrderGoodServiceInfoDao.getOrderGoodServiceByOrderId(orderId);
        if (CollectionUtils.isNotEmpty(list)) {
            Iterator<ErpOrderGoodServiceInfo> iterator = list.iterator();
            while (iterator.hasNext()) {
                ErpOrderGoodServiceInfo next = iterator.next();
                if ("聚引客生产服务".equals(next.getServiceItemName())) {
                    iterator.remove();
                    continue;
                }

                Date expirationTime = next.getExpirationTime();
                if (expirationTime.before(new Date())) {
                    next.setIsValid(false);
                } else {
                    next.setIsValid(true);
                }
            }
            
            list.forEach(erpOrderGoodServiceInfo->{
        		if(erpOrderGoodServiceInfo!=null&&erpOrderGoodServiceInfo.getServiceTerm()!=null&&erpOrderGoodServiceInfo.getServiceTerm()==1200) {
        			erpOrderGoodServiceInfo.setServiceTerm(null);
        			erpOrderGoodServiceInfo.setExpirationTime(null);
        		}
        	});
        }
        return list;
    }

    /**
     * 查询统计结果
     *
     * @param orderId
     * @return
     * @date 2018年6月4日
     */
    public Map<String, Object> querySum(String orderId) {
        if (StringUtils.isBlank(orderId)) {
            throw new ServiceException("找不到orderId");
        }
        return erpOrderGoodServiceInfoDao.querySum(orderId);
    }

    /**
     * 根据订单id和服务项id查询待处理的服务项信息
     *
     * @param orderId
     * @param itemId
     * @return
     * @date 2018年6月4日
     * @author zjq
     */
    public ErpOrderGoodServiceInfo getOrderGoodServiceByOrderIdSingle(String orderId, String itemId) {
        return erpOrderGoodServiceInfoDao.getOrderGoodServiceByOrderIdSingle(orderId, itemId);
    }

    /**
     * 根据订单验证服务项 是否存在
     *
     * @param orderId
     * @param itemId
     * @return
     * @date 2018年7月11日
     * @author zjq
     */
    public ErpOrderGoodServiceInfo getOrderGoodServiceExists(String orderId, String itemId) {
        return erpOrderGoodServiceInfoDao.getOrderGoodServiceExists(orderId, itemId);
    }

    /**
     * 业务定义：根据订单ID删除服务记录
     * 
     * @date 2018年7月3日
     * @author R/Q
     */
    public void deleteRecordByOrderId(String orderId) {
        super.dao.deleteRecordByOrderId(orderId);
    }

    public int addErpFlowServiceItemLink(FlowServiceItemLinkDto dto) {
        if (dto == null || StringUtils.isBlank(dto.getServiceSourceId()) || StringUtils.isBlank(dto.getProcInsId()) || StringUtils.isBlank(dto.getDelFlag())) {
            throw new ServiceException("FlowServiceItemLinkDto数据有误，插入数据失败");
        }

        return erpOrderGoodServiceInfoDao.insertErpFlowServiceItemLink(dto);
    }

    /**
     * 业务定义：根据流程实例ID获取数据
     * 
     * @date 2018年9月6日
     * @author R/Q
     */
    public List<ErpOrderGoodServiceInfo> getOrderGoodServiceByProcInsId(String procInsId) {
        return dao.getOrderGoodServiceByProcInsId(procInsId);
    }

    /**
     * 业务定义：复制服务项关联数据
     * 
     * @date 2018年9月6日
     * @author R/Q
     */
    @Transactional
    public void copyServiceItemLink(String oldProcInsId, String newProcInsId) {
        dao.copyServiceItemLink(oldProcInsId, newProcInsId);
    }
}