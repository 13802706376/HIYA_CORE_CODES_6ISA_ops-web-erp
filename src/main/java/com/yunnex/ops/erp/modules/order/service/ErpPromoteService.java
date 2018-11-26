package com.yunnex.ops.erp.modules.order.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.order.dao.ErpPromoteDao;
import com.yunnex.ops.erp.modules.order.entity.PromoteOrderSplit;

@Service
public class ErpPromoteService extends CrudService<ErpPromoteDao,PromoteOrderSplit> {
	
	/**
     * 获取订单推广金额明细
     */
    public List<PromoteOrderSplit> findPromoteOrder(String userId,String startDate, String endDate, String order_type){
        List<PromoteOrderSplit> list = dao.findPromoteOrder(userId,startDate, endDate, order_type);
        return list;

    }
    
    /**
     * 获取已完成推广服务明细
     */
    public List<PromoteOrderSplit> findPromoteSplit(String userId,String startDate, String endDate, String order_type){
        List<PromoteOrderSplit> po = dao.findPromoteSplit(userId,startDate, endDate, order_type);
        for(PromoteOrderSplit p:po){
        	Double py=p.getExpenditurePy();
        	Double wb=p.getExpenditureWb();
        	Double mm=p.getExpenditureMm();
        	if(py==null){
        		p.setExpenditurePy(0.0);
        	}
        	if(wb==null){
        		p.setExpenditureWb(0.0);
        	}
        	if(mm==null){
        		p.setExpenditureMm(0.0);
        	}
        }
        return po;

    }
}
