package com.yunnex.ops.erp.common.utils;

import com.yunnex.ops.erp.common.constant.Constant;

/**
 * 数值工具类
 */
public final class NumberUtils {

    private NumberUtils() {}

    /**
     * Integer: null转0
     * 
     * @param num
     * @return
     */
    public static Integer nullToZero(Integer num) {
        return num == null ? Constant.ZERO : num;
    }

    /**
     * Long: null转0
     *
     * @param num
     * @return
     */
    public static Long nullToZero(Long num) {
        return num == null ? Long.valueOf(Constant.ZERO) : num;
    }


}
