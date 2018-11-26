package com.yunnex.ops.erp.modules.good.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 商品服务Entity
 * @author yunnex
 * @version 2018-05-29
 */
public class ErpGoodService extends DataEntity<ErpGoodService> {
	
	private static final long serialVersionUID = 1L;
	private String goodId;		// 所属商品id
	private String serviceItemId;		// 商品服务项目ID
	private String serviceItemName;		// 商品服务项目名称
    private Integer times; // 商品服务次数
	private String isDeadline;		// 是否有期限,N没有,Y有
    private Integer serviceTerm; // 商品服务期限(单位月)
	private String isPackage;		// 是否属于套餐里面的服务,N否,Y是
	
	public ErpGoodService() {
		super();
	}

	public ErpGoodService(String id){
		super(id);
	}

	@Length(min=1, max=64, message="所属商品id长度必须介于 1 和 64 之间")
	public String getGoodId() {
		return goodId;
	}

	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}
	
	@Length(min=1, max=64, message="商品服务项目ID长度必须介于 1 和 64 之间")
	public String getServiceItemId() {
		return serviceItemId;
	}

	public void setServiceItemId(String serviceItemId) {
		this.serviceItemId = serviceItemId;
	}
	
	@Length(min=1, max=64, message="商品服务项目名称长度必须介于 1 和 64 之间")
	public String getServiceItemName() {
		return serviceItemName;
	}

	public void setServiceItemName(String serviceItemName) {
		this.serviceItemName = serviceItemName;
	}
	
	
	@Length(min=1, max=1, message="是否有期限,N没有,Y有长度必须介于 1 和 1 之间")
	public String getIsDeadline() {
		return isDeadline;
	}

	public void setIsDeadline(String isDeadline) {
		this.isDeadline = isDeadline;
	}
	
	@Length(min=1, max=1, message="是否属于套餐里面的服务,N否,Y是长度必须介于 1 和 1 之间")
	public String getIsPackage() {
		return isPackage;
	}

	public void setIsPackage(String isPackage) {
		this.isPackage = isPackage;
	}

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getServiceTerm() {
        return serviceTerm;
    }

    public void setServiceTerm(Integer serviceTerm) {
        this.serviceTerm = serviceTerm;
    }
	
}