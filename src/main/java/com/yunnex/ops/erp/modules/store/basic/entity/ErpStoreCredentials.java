package com.yunnex.ops.erp.modules.store.basic.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 商户营业资质信息Entity
 * @author yunnex
 * @version 2017-12-09
 */
public class ErpStoreCredentials extends DataEntity<ErpStoreCredentials> {
	
	private static final long serialVersionUID = 1L;
	private String registerName;		// 注册名称
	private String registerNo;		// 注册号
	private String registerCity;		// 注册城市
	private String registerAddress;		// 注册地址
	private Integer businessScope;		// 经营范围
	private Date startDate;		// 资质有效期起始日期
	private Date endDate;		// 资质有效期结束日期
	private Integer isLongTime=0;		// 资质是否是长期有效，0：否，1：是，默认0
	private String organizationCodeCertificateNo;		// 组织机构代码证号
	private String organizationCodeCertificate;		// 组织机构代码证
	private String businessLicence;		// 营业执照
	private String specialCertificate;		// 特殊资质
	
	public ErpStoreCredentials() {
		super();
	}

	public ErpStoreCredentials(String id){
		super(id);
	}

	@Length(min=0, max=64, message="注册名称长度必须介于 0 和 64 之间")
	public String getRegisterName() {
		return registerName;
	}

	public void setRegisterName(String registerName) {
		this.registerName = registerName;
	}
	
	@Length(min=0, max=30, message="注册号长度必须介于 0 和 30 之间")
	public String getRegisterNo() {
		return registerNo;
	}

	public void setRegisterNo(String registerNo) {
		this.registerNo = registerNo;
	}
	
	@Length(min=0, max=30, message="注册城市长度必须介于 0 和 30 之间")
	public String getRegisterCity() {
		return registerCity;
	}

	public void setRegisterCity(String registerCity) {
		this.registerCity = registerCity;
	}
	
	@Length(min=0, max=64, message="注册地址长度必须介于 0 和 64 之间")
	public String getRegisterAddress() {
		return registerAddress;
	}

	public void setRegisterAddress(String registerAddress) {
		this.registerAddress = registerAddress;
	}
	
	@Length(min=0, max=255, message="经营范围长度必须介于 0 和 255 之间")
	public Integer getBusinessScope() {
		return businessScope;
	}

	public void setBusinessScope(Integer businessScope) {
		this.businessScope = businessScope;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@NotNull(message="资质是否是长期有效，0：否，1：是，默认0不能为空")
	public Integer getIsLongTime() {
		return isLongTime;
	}

	public void setIsLongTime(Integer isLongTime) {
		this.isLongTime = isLongTime;
	}
	
	@Length(min=0, max=64, message="组织机构代码证号长度必须介于 0 和 64 之间")
	public String getOrganizationCodeCertificateNo() {
		return organizationCodeCertificateNo;
	}

	public void setOrganizationCodeCertificateNo(String organizationCodeCertificateNo) {
		this.organizationCodeCertificateNo = organizationCodeCertificateNo;
	}
	
	@Length(min=0, max=128, message="组织机构代码证长度必须介于 0 和 128 之间")
	public String getOrganizationCodeCertificate() {
		return organizationCodeCertificate;
	}

	public void setOrganizationCodeCertificate(String organizationCodeCertificate) {
		this.organizationCodeCertificate = organizationCodeCertificate;
	}
	
	@Length(min=0, max=512, message="营业执照长度必须介于 0 和 512 之间")
	public String getBusinessLicence() {
		return businessLicence;
	}

	public void setBusinessLicence(String businessLicence) {
		this.businessLicence = businessLicence;
	}
	
	@Length(min=0, max=1024, message="特殊资质长度必须介于 0 和 1024 之间")
	public String getSpecialCertificate() {
		return specialCertificate;
	}

	public void setSpecialCertificate(String specialCertificate) {
		this.specialCertificate = specialCertificate;
	}

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
