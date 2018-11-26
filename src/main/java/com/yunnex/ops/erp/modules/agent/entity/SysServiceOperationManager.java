package com.yunnex.ops.erp.modules.agent.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 服务商运营经关系对应表Entity
 * @author hanhan
 * @version 2018-06-01
 */
public class SysServiceOperationManager extends DataEntity<SysServiceOperationManager> {
	
	private static final long serialVersionUID = 1L;
	private String serviceNo;		// 服务商编号
	private String companyName;		// 分公司名称
	private String serverAddress;		// 服务区域
	private String roleName;		// 角色
	private String defaultManager;		// 默认运营经理人员
	private String defaultManagerId;		// 默认运营经理人员ID
	private String alternativeManager;		// 备选运营经理人员子任务处理人
	private String alternativeManagerId;		// 备选运营经理人员子任务处理人ID
	private String remark;		// 备注
	private Long sort;		// 排序字段
	
	public SysServiceOperationManager() {
		super();
	}

	public SysServiceOperationManager(String id){
		super(id);
	}

	@Length(min=1, max=10, message="服务商编号长度必须介于 1 和 10 之间")
	public String getServiceNo() {
		return serviceNo;
	}

	public void setServiceNo(String serviceNo) {
		this.serviceNo = serviceNo;
	}
	
	@Length(min=1, max=64, message="分公司名称长度必须介于 1 和 64 之间")
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	@Length(min=1, max=64, message="服务区域长度必须介于 1 和 64 之间")
	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}
	
	@Length(min=1, max=64, message="角色长度必须介于 1 和 64 之间")
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	@Length(min=0, max=64, message="默认运营经理人员长度必须介于 0 和 64 之间")
	public String getDefaultManager() {
		return defaultManager;
	}

	public void setDefaultManager(String defaultManager) {
		this.defaultManager = defaultManager;
	}
	
	@Length(min=0, max=64, message="默认运营经理人员ID长度必须介于 0 和 64 之间")
	public String getDefaultManagerId() {
		return defaultManagerId;
	}

	public void setDefaultManagerId(String defaultManagerId) {
		this.defaultManagerId = defaultManagerId;
	}
	
	@Length(min=1, max=64, message="备选运营经理人员子任务处理人长度必须介于 1 和 64 之间")
	public String getAlternativeManager() {
		return alternativeManager;
	}

	public void setAlternativeManager(String alternativeManager) {
		this.alternativeManager = alternativeManager;
	}
	
	@Length(min=1, max=64, message="备选运营经理人员子任务处理人ID长度必须介于 1 和 64 之间")
	public String getAlternativeManagerId() {
		return alternativeManagerId;
	}

	public void setAlternativeManagerId(String alternativeManagerId) {
		this.alternativeManagerId = alternativeManagerId;
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
	
}