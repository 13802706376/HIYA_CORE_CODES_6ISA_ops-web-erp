package com.yunnex.ops.erp.modules.workflow.flow.strategy;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.SecurityUtils;

import com.yunnex.ops.erp.common.utils.StringUtils;

/**
 * 获取有菜单权限的运营服务tab页
 * 
 * @author Ejon
 * @date 2018年5月28日
 */
public class OperatingPermissionValdate implements IPermission {

    private static Map<String, String> act_permission = new LinkedHashMap<String, String>();

    static {
        // 有顺序 tasklis4p0.jsp
        act_permission.put("service_startup", "tab:service_startup:view");
        act_permission.put("account_pay_open", "tab:account_pay_open:view");
        act_permission.put("marketing_planning", "tab:marketing_planning:view");
        act_permission.put("training_service", "tab:training_service:view");
        act_permission.put("material_service", "tab:material_service:view");
        act_permission.put("aftersale_visit", "tab:aftersale_visit:view");
        act_permission.put("advertiser_open", "tab:advertiser_open:view");
        act_permission.put("jyk_delivery", "tab:jyk_delivery:view");
        act_permission.put("review_visit", "tab:review_visit:view");
        act_permission.put("wisdom_shop", "tab:wisdom_shop:view");
        // act_permission.put("jyk_pro", "tab:service_startup:view");
    }


    @Override
    public String getPermitted(String actType) {
        if (StringUtils.isEmpty(actType)) {

            for (String key : act_permission.keySet()) {
                if (SecurityUtils.getSubject().isPermitted(act_permission.get(key))) {
                    actType = key;
                    break;
                }

            }

        }

        if (StringUtils.isEmpty(act_permission.get(actType))) {
            return null;
        }

        if (SecurityUtils.getSubject().isPermitted(act_permission.get(actType))) {
            return actType;
        }

        return null;
    }

}
