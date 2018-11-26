package com.yunnex.ops.erp.modules.sys.entity;

/**
 * LDAP信息
 */
public class LdapADHelperDto {
    private String id;
    private String searchBase; // dn
    private String parentId;
    private String parentIds;
    private String name;

    public LdapADHelperDto() {
        super();
    }

    public LdapADHelperDto(String id, String searchBase, String parentId, String parentIds, String name) {
        super();
        this.id = id;
        this.searchBase = searchBase;
        this.parentId = parentId;
        this.parentIds = parentIds;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSearchBase() {
        return searchBase;
    }

    public void setSearchBase(String searchBase) {
        this.searchBase = searchBase;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "LdapADHelperDto{" + "id='" + id + '\'' + ", searchBase='" + searchBase + '\'' + ", parentId='" + parentId + '\'' + ", parentIds='" + parentIds + '\'' + ", name='" + name + '\'' + '}';
    }
}
