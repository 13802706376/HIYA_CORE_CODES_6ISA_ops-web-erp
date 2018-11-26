package com.yunnex.junit.ldap;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunnex.junit.BaseTest;
import com.yunnex.ops.erp.modules.sys.ldap.LdapDao;

public class LdapDaoTest extends BaseTest {

    @Autowired
    private LdapDao ldapDao;

    @Test
    public void test1() {
        Boolean flag = ldapDao.erpCnExists("abc123");
        System.out.println(flag);
    }

}
