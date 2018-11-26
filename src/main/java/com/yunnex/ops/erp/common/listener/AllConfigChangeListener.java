package com.yunnex.ops.erp.common.listener;

import org.springframework.stereotype.Component;

import com.yunnex.ops.erp.common.config.GlobalConfig;

import yunnex.common.config.ConfigNamespace;
import yunnex.common.config.change.AbstractConfigChangeListener;
import yunnex.common.config.change.ConfigChangeEvent;

@Component
public class AllConfigChangeListener extends AbstractConfigChangeListener {

    @Override
    public void onChange(ConfigChangeEvent changeEvent) {

        for (String key : changeEvent.changedKeys()) {
            // 同步更新全局变量的值
            GlobalConfig.put(key, changeEvent.getChange(key).getNewValue());
        }
    }

    @Override
    public void setNamespace() {
        this.namespace = ConfigNamespace.ALL.getNamespace();
    }
}
