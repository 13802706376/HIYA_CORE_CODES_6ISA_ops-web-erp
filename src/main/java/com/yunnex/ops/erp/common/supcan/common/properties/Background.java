/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yunnex.ops.erp.common.supcan.common.properties;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.yunnex.ops.erp.common.supcan.annotation.common.properties.SupBackground;
import com.yunnex.ops.erp.common.utils.ObjectUtils;

/**
 * 硕正TreeList Properties Background
 * @author WangZhen
 * @version 2013-11-04
 */
@XStreamAlias("Background")
public class Background {
    
    /**
     * 背景颜色
     */
    @XStreamAsAttribute
    private String bgColor = "#FDFDFD";
    
    public Background() {
        
    }
    
    public Background(SupBackground supBackground) {
        this();
        ObjectUtils.annotationToObject(supBackground, this);
    }
    
    public Background(String bgColor) {
        this();
        this.bgColor = bgColor;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

}
