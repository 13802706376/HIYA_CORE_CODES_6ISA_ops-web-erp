package com.yunnex.ops.erp.common.config;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;

/**
 * 由于代码中需要直接访问配置，所以将所有配置复制到一个全局对象中。 联调dubbo接口，本地注册的服务对联调会造成干扰,DUBBO_CONFIG=false
 * 配置到环境变量
 */
public class PropertyPlaceholder extends PropertyPlaceholderConfigurer
{
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)throws BeansException
	{
		if (props != null)
		{
            Config config = ConfigService.getAppConfig();
            // 向GlobalConfig注册
            config.getPropertyNames().forEach(entry -> GlobalConfig.put(entry, config.getProperty(entry, "")));
            props.putAll(GlobalConfig.getProps());
		}
		super.processProperties(beanFactoryToProcess, props);
	}
}