package com.yunnex.ops.erp.modules.order.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 订单商品Entity
 * @author huanghaidong
 * @version 2017-10-24
 */
public class ErpOrderOriginalGood extends DataEntity<ErpOrderOriginalGood> {
	
	private static final long serialVersionUID = 1L;
    private String orderId; // 所属订单id
	private String goodName;		// 商品名称
    private Long goodId; // 商品id;
	private Long goodTypeId;		// 商品类型id
	private String goodTypeName;		// 商品类型名称
	private Long prePrice;		// 预计价格(单位：分)
	private Long realPrice;		// 实际价格(单位：分)
	private Integer num;		// 商品总共数量
	private Integer processNum;		// 处理中的商品数量
	private Integer pendingNum;		// 待处理的商品数量
	private Integer finishNum;		// 已完成的商品数量
	private String remark;		// 备注
	private Long sort;		// 排序字段
    private String packId; // 套餐id
    private String bizType; 

    public String getPackId() {
        return packId;
    }

    public void setPackId(String packId) {
        this.packId = packId;
    }

    public ErpOrderOriginalGood() {
		super();
	}

	public ErpOrderOriginalGood(String id){
		super(id);
	}

    public String getOrderId() {
		return orderId;
	}

    public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	@Length(min=0, max=50, message="商品名称长度必须介于 0 和 50 之间")
	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	
	public Long getGoodTypeId() {
		return goodTypeId;
	}

	public void setGoodTypeId(Long goodTypeId) {
		this.goodTypeId = goodTypeId;
	}
	
	@Length(min=0, max=50, message="商品类型名称长度必须介于 0 和 50 之间")
	public String getGoodTypeName() {
		return goodTypeName;
	}

	public void setGoodTypeName(String goodTypeName) {
		this.goodTypeName = goodTypeName;
	}
	
	public Long getPrePrice() {
		return prePrice;
	}

	public void setPrePrice(Long prePrice) {
		this.prePrice = prePrice;
	}
	
	public Long getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(Long realPrice) {
		this.realPrice = realPrice;
	}
	
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	
	public Integer getProcessNum() {
		return processNum;
	}

	public void setProcessNum(Integer processNum) {
		this.processNum = processNum;
	}
	
	public Integer getPendingNum() {
		return pendingNum;
	}

	public void setPendingNum(Integer pendingNum) {
		this.pendingNum = pendingNum;
	}
	
	public Integer getFinishNum() {
		return finishNum;
	}

	public void setFinishNum(Integer finishNum) {
        if (pendingNum >= 0)
		this.finishNum = finishNum;
	}
	
	@Length(min=0, max=256, message="备注长度必须介于 0 和 256 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}

    public Long getGoodId() {
        return goodId;
    }

    public void setGoodId(Long goodId) {
        this.goodId = goodId;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }
    
}