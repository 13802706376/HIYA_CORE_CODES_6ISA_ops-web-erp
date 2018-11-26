package com.yunnex.ops.erp.modules.order.util;

import com.yunnex.ops.erp.common.constant.CommonConstants;

/**
 * 分单工具类
 * 
 * @author linqunzhi
 * @date 2018年5月11日
 */
public final class OrderSplitUtil {

    /** 转换流程截取字符的长度 */
    private static final int PROCESS_VERSION_DIFF = 2;

    /**
     * 工具类不需要被new出来
     */
    private OrderSplitUtil() {

    }


    /**
     * 转换流程版本号名称 (301 -> V3.1)
     *
     * @param processVersion 分单流程版本号
     * @return
     * @date 2018年5月11日
     * @author linqunzhi
     */
    public static String convertProcessVersionName(Integer processVersion) {
        if (processVersion == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        String version = String.valueOf(processVersion);
        int len = version.length();
        // 每次截取的 字符个数
        int diff = PROCESS_VERSION_DIFF;
        int lastIndex = len;
        // 每次截取的字符串(后面开始截取，每次截取 diff 个)
        String subStr = null;
        while (lastIndex > 0) {
            subStr = version.substring((lastIndex - diff) < 0 ? 0 : (lastIndex - diff), lastIndex);
            lastIndex = lastIndex - diff;
            int value = Integer.parseInt(subStr);
            builder.insert(0, value);
            // 不是最开始的位置
            if (lastIndex > 0) {
                builder.insert(0, CommonConstants.Sign.POINT);
            }
        }
        // 在最前面添加 版本V
        builder.insert(0, "V");
        return builder.toString();
    }

}
