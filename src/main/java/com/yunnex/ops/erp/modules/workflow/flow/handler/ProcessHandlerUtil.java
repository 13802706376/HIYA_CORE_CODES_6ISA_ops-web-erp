package com.yunnex.ops.erp.modules.workflow.flow.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;

public class ProcessHandlerUtil {

    private ProcessHandlerUtil() {

    }

    /**
     * 不过期
     *
     * @param date
     * @return
     * @date 2018年7月5日
     * @author zjq
     */
    public static boolean notExpired(Date expireDate) {
        return new Date(System.currentTimeMillis()).before(expireDate);
    }


    public static Long emptyDefault(Long val) {
        return null == val ? 0 : val;
    }

    public static String emptyDefault(String val, String defVal) {
        return StringUtils.isBlank(val) ? defVal : val;
    }

    /**
     * 为空
     *
     * @param t
     * @return
     * @date 2018年7月11日
     * @author zjq
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean allEmpty(T... t) {
        List<T> infos = Arrays.asList(t);
        long count = infos.stream().filter((item) -> Optional.ofNullable(item).isPresent()).count();
        return count == 0;
    }

    /**
     * 正数 val>0
     * 
     * @param val
     * @return
     * @date 2018年7月11日
     * @author zjq
     */
    public static boolean positiveInteger(long val) {
        return val > 0;
    }
    
    /**
     *map中的KEY=has_start_flow存在 且value值为Y
     * 
     * @param map
     * @return
     * @date 2018年7月11日
     * @author zjq
     */
    public static boolean isStartFlow(Map<String, Object>map) {
        return map!=null&&map.containsKey(FlowConstant.HAS_START_FLOW)&&map.get(FlowConstant.HAS_START_FLOW).equals(Constant.YES);
    }
    
}


