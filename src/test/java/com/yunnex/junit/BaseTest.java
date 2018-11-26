package com.yunnex.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import com.yunnex.ops.erp.modules.sys.constant.SysConstant;

@ActiveProfiles(SysConstant.SystemEnvironment.PROFLIE_DEVELOPMENT)
@RunWith(Junit4ClassRunner.class)
@WebAppConfiguration
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@ContextConfiguration(value = {"classpath:config/spring/spring-context-env.xml", "classpath:config/spring/spring-context.xml", "classpath:config/spring/spring-context-shiro.xml", "classpath:config/spring/spring-context-activiti.xml", "classpath:config/spring/spring-context-ldap.xml", "classpath:config/spring/spring-context-jedis.xml"})
public class BaseTest extends AbstractTransactionalJUnit4SpringContextTests
{
	@Before
	public void before()
	{}

	@After
	public void after()
	{}
}
