package com.yunnex.ops.erp.common.utils;

import com.alibaba.fastjson.JSONObject;

public class ResponeUtil {

    public static boolean isResponeValid(JSONObject data) {
        return null != data && data.getIntValue("code") == 0;
    }
}
