package com.yunnex.ops.erp.modules.workflow.delivery.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * erp_delivery_serviceEntity
 * @author hanhan
 * @version 2018-05-26
 */
public class ErpDeliveryService extends DataEntity<ErpDeliveryService> {
	
	private static final long serialVersionUID = 1L;
	private String orderId;		// 订单ID
	private String shopId;		// 商户ID
	private String shopName;		// 商户名称
	private String source;		// 订单来源
	private String procInsId;		// 流程编号
	private String remark;		// 备注
	private Date flowEndTime;		// 订单结束时间
	private String orderNumber;
	private String serviceType;
    private Date shouldFlowEndTime;
	
    /** 应完成交付时间 */
    private String excptionLogo;

    /** 启动时间（运营经理（运营经理_服务商）“查看订单信息，指派订单处理人员”任务完成的时间） */
    private Date startTime;

    /** 银联支付培训&测试（远程）任务完成时间 */
    private Date trainTestTime;

    /** 银联支付培训&测试（远程）任务应该完成时间 */
    private Date shouldTrainTestTime;

    /** 物料制作跟踪任务完成时间 */
    private Date materielTime;

    /** 物料制作跟踪任务应该完成时间 */
    private Date shouldMaterielTime;

    /** 上门服务完成（首次营销策划服务）任务完成时间 */
    private Date visitServiceTime;

    /** 上门服务完成（首次营销策划服务）任务应该完成时间 */
    private Date shouldVisitServiceTime;
    
    /**是否包含智慧餐厅，新旧商户*/
    private String zhctType;

    /** 升级状态（Y ：是 N：否） */
    private String upgradeFlag;

    /** 处理状态，0：其他，2：已取消。相当于erp_order_split_info表的status，但由于历史原因，0和1状态现在保留。 */
    private Integer status;

	public String getServiceType() {
		return serviceType;
	}

	public String getExcptionLogo() {
		return excptionLogo;
	}

	public void setExcptionLogo(String excptionLogo) {
		this.excptionLogo = excptionLogo;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public ErpDeliveryService() {
		super();
	}

	public ErpDeliveryService(String id){
		super(id);
	}

	@Length(min=1, max=64, message="订单ID长度必须介于 1 和 64 之间")
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	
	public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    @Length(min=1, max=50, message="商户名称长度必须介于 1 和 50 之间")
	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	
	@Length(min=1, max=20, message="订单来源长度必须介于 1 和 20 之间")
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	@Length(min=1, max=64, message="流程编号长度必须介于 1 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
	
	@Length(min=0, max=256, message="备注长度必须介于 0 和 256 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getFlowEndTime() {
		return flowEndTime;
	}

	public void setFlowEndTime(Date flowEndTime) {
		this.flowEndTime = flowEndTime;
	}

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getShouldFlowEndTime() {
        return shouldFlowEndTime;
    }

    public void setShouldFlowEndTime(Date shouldFlowEndTime) {
        this.shouldFlowEndTime = shouldFlowEndTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getTrainTestTime() {
        return trainTestTime;
    }

    public void setTrainTestTime(Date trainTestTime) {
        this.trainTestTime = trainTestTime;
    }

    public Date getShouldTrainTestTime() {
        return shouldTrainTestTime;
    }

    public void setShouldTrainTestTime(Date shouldTrainTestTime) {
        this.shouldTrainTestTime = shouldTrainTestTime;
    }

    public Date getMaterielTime() {
        return materielTime;
    }

    public void setMaterielTime(Date materielTime) {
        this.materielTime = materielTime;
    }

    public Date getShouldMaterielTime() {
        return shouldMaterielTime;
    }

    public void setShouldMaterielTime(Date shouldMaterielTime) {
        this.shouldMaterielTime = shouldMaterielTime;
    }

    public Date getVisitServiceTime() {
        return visitServiceTime;
    }

    public void setVisitServiceTime(Date visitServiceTime) {
        this.visitServiceTime = visitServiceTime;
    }

    public Date getShouldVisitServiceTime() {
        return shouldVisitServiceTime;
    }

    public void setShouldVisitServiceTime(Date shouldVisitServiceTime) {
        this.shouldVisitServiceTime = shouldVisitServiceTime;
    }

    public String getZhctType() {
        return zhctType;
    }

    public void setZhctType(String zhctType) {
        this.zhctType = zhctType;
    }

    public String getUpgradeFlag() {
        return upgradeFlag;
    }

    public void setUpgradeFlag(String upgradeFlag) {
        this.upgradeFlag = upgradeFlag;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
