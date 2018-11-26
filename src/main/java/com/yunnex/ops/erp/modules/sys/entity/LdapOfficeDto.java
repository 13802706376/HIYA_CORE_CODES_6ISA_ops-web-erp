package com.yunnex.ops.erp.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 组织机构DTO
 */
public class LdapOfficeDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String oldOfficeId; // id可能会变
    private String newOfficeId;
    private String name;
    private String ldapDn;
    private Date updateDate;

    public LdapOfficeDto() {}

    public LdapOfficeDto(String oldOfficeId, String newOfficeId, String name, String ldapDn, Date updateDate) {
        this.oldOfficeId = oldOfficeId;
        this.newOfficeId = newOfficeId;
        this.name = name;
        this.ldapDn = ldapDn;
        this.updateDate = updateDate;
    }

    public String getOldOfficeId() {
        return oldOfficeId;
    }

    public void setOldOfficeId(String oldOfficeId) {
        this.oldOfficeId = oldOfficeId;
    }

    public String getNewOfficeId() {
        return newOfficeId;
    }

    public void setNewOfficeId(String newOfficeId) {
        this.newOfficeId = newOfficeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLdapDn() {
        return ldapDn;
    }

    public void setLdapDn(String ldapDn) {
        this.ldapDn = ldapDn;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
