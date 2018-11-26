package com.yunnex.ops.erp.modules.sys.ldap;

/**
 * LDAP常量
 */
public interface LdapConstant {

    // User
    String USER = "User";
    // 服务商
    String AGENT = "服务商";

    // 运营中心组织
    String BASE_ERP_OPS = "ou=运营中心,ou=User";

    // yunnex base dn
    String BASE_DN_YUNNEX = "dc=yunnex,dc=com";
    // dn: ou
    String DN_OU = "ou";

    // objectClass
    String ATTR_OBJECT_CLASS = "objectClass";
    // 属性：cn
    String ATTR_CN = "cn";

    // top
    String OBJECT_CLASS_TOP = "top";
    // organizationalUnit
    String OBJECT_CLASS_OU = "organizationalUnit";
    // person
    String OBJECT_CLASS_PERSON = "person";

}
