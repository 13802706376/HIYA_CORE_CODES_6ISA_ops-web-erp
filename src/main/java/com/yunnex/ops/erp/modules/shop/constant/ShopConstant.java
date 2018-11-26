package com.yunnex.ops.erp.modules.shop.constant;

import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.modules.store.constant.StoreConstant;

public interface ShopConstant {

    /**
     * 判断值
     */
    public interface whether {
        
        /**
         * 是
         */
        String YES = "Y";
        
        /**
         * 否
         */
        String NO = "N";
    }
    
    /**
     * 返回状态
     */
    public interface returnStatus {
        
        /**
         * 成功
         */
        String SUCCESS = "0";
        
        /**
         * 失败
         */
        String FAIL = "1";
    }
    
    /**
     * 同步类型
     */
    public interface syncType {
        
        /**
         * 直接覆盖
         */
        String COVER = "Cover";
        
        /**
         * 保留两者
         */
        String RETAIN = "Retain";
    }

    enum AlipayState {
        // open：开通，notOpen
        OPEN("open", "开通"), NOT_OPEN("notOpen", "未开通");

        private String name;
        private String displayName;

        AlipayState() {}

        AlipayState(String name, String displayName) {
            this.name = name;
            this.displayName = displayName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public static String getByName(String name) {
            if (name == null) {
                return Constant.SPRIT;
            }
            for (AlipayState alipayState : values()) {
                if (name.equals(alipayState.getName())) {
                    return alipayState.getDisplayName();
                }
            }
            return Constant.SPRIT;
        }
    }

    /**
     * 排序
     */
    interface Sort {
        // 当前状态可用排序在前
        Long ENABLE = 100000L;
        // 当前状态不可用排序在后
        Long DISENABLED = 0L;
    }
}
