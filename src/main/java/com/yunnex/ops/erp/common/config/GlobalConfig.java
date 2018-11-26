package com.yunnex.ops.erp.common.config;

import java.util.Properties;

/**
 * 全局配置，存放spring启动时加载的所有properties配置，通过{@link PropertyPlaceholder}复制。用于在代码中直接访问。
 */
public final class GlobalConfig {
    private static final Properties props = new Properties();

    private GlobalConfig() {}

    public static Properties getProps() {
        return props;
    }

    public static void put(String key, String value) {
        props.put(key, value);
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
