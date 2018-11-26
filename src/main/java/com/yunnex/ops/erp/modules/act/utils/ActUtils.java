/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.yunnex.ops.erp.modules.act.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.annotation.FieldName;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.utils.Encodes;
import com.yunnex.ops.erp.common.utils.ObjectUtils;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.act.entity.Act;
import com.yunnex.ops.erp.modules.sys.entity.Role;
import com.yunnex.ops.erp.modules.sys.entity.User;

/**
 * 流程工具
 *
 * @author ThinkGem
 * @version 2013-11-03
 */
public class ActUtils {


    /**
     * 定义流程定义KEY，必须以“PD_”开头 组成结构：string[]{"流程标识","业务主表表名"}
     */
    public static final String[] PD_LEAVE = new String[] {"leave", "oa_leave"}; // NOSONAR
    public static final String[] PD_TEST_AUDIT = new String[] {"test_audit", "oa_test_audit"}; // NOSONAR

    /**
     * 流程中文名
     */
    public static final String[] FLOW_CN_NAME = new String[] {"聚引客", "商户资料录入", "微信支付进件", "银联支付进件","朋友圈开户","微博开户"};// NOSONAR

    /**
     * 流程别名
     */
    public static final String[] FLOW_ALIAS = new String[] {"jyk_flow", "sdi_flow", "payInto_flow", "promote_info_flow"};// NOSONAR

    /**
     * 聚引客开户流程
     */
    public static final String[] JYK_OPEN_ACCOUNT_FLOW = new String[] {"jyk_flow", "erp_order_split_info"}; // NOSONAR

    /**
     * 聚引客流程(3.0)
     */
    public static final String[] JYK_FLOW_NEW = new String[] {"jyk_flow_new", "erp_order_split_info"}; // NOSONAR
    /**
     * 微博提审流程
     */
    public static final String[] MICROBLOG_PROMOTION_FLOW = new String[] {"microblog_promotion_flow", "erp_store_advertiser_weibo"};// NOSONAR
    /**
     * 朋友圈提审流程
     */
    public static final String[] FRIENDS_PROMOTION_FLOW = new String[] {"friends_promotion_flow", "erp_store_advertiser_friends"};// NOSONAR
    /**
     * 聚引客最新流程
     */
    public static final String[] JYK_FLOW_LAST = new String[] {JYK_FLOW_NEW[0], JYK_FLOW_NEW[1], "301"}; // NOSONAR

    /**
     * 商户资料录入流程
     */
    public static final String[] SHOP_DATA_INPUT_FLOW = new String[] {"shop_data_input_flow", "erp_shop_data_input"}; // NOSONAR

    /**
     * 微信支付进件流程
     */
    public static final String[] WECHAT_PAY_INTOPIECES_FLOW = new String[] {"wechatpay_intopieces_flow", "erp_pay_intopieces"}; // NOSONAR

    /**
     * 银联支付进件流程
     */
    public static final String[] UNION_PAY_INTOPIECES_FLOW = new String[] {"unionpay_intopieces_flow", "erp_pay_intopieces"}; // NOSONAR

    public static final String[] VISIT_SERVICE_FLOW = new String[] {"visit_service_flow", "erp_visit_service_info"};

    public static final String[] DELIVERY_SERVICE_FLOW = new String[] {"delivery_service_flow", "erp_delivery_service"};
    
    public static final String[] ORDER_AUDIT_FLOW = new String[] {"order_audit_flow", "erp_order_original_info"};
    /**
     * 智慧餐厅老商户流程
     */
	public static final String VISIT_SERVICE_ZHCT_FLOW = "visit_service_zhct_flow";

    @SuppressWarnings({"unused"})
    public static Map<String, Object> getMobileEntity(Object entity, String spiltType) {
        if (spiltType == null) {
            spiltType = "@";
        }
        Map<String, Object> map = Maps.newHashMap();

        List<String> field = Lists.newArrayList();
        List<String> value = Lists.newArrayList();
        List<String> chinesName = Lists.newArrayList();

        try {
            for (Method m : entity.getClass().getMethods()) {
                if (m.getAnnotation(JsonIgnore.class) == null && m.getAnnotation(JsonBackReference.class) == null && m.getName().startsWith("get")) {
                    if (m.isAnnotationPresent(FieldName.class)) {
                        Annotation p = m.getAnnotation(FieldName.class);
                        FieldName fieldName = (FieldName) p;
                        chinesName.add(fieldName.value());
                    } else {
                        chinesName.add("");
                    }
                    if (m.getName().equals("getAct")) {
                        Object act = m.invoke(entity, new Object[] {});
                        Method actMet = act.getClass().getMethod("getTaskId");
                        map.put("taskId", ObjectUtils.toString(m.invoke(act, new Object[] {}), ""));
                    } else {
                        field.add(StringUtils.uncapitalize(m.getName().substring(3)));
                        value.add(ObjectUtils.toString(m.invoke(entity, new Object[] {}), ""));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        map.put("beanTitles", StringUtils.join(field, spiltType));
        map.put("beanInfos", StringUtils.join(value, spiltType));
        map.put("chineseNames", StringUtils.join(chinesName, spiltType));

        return map;
    }

    /**
     * 获取流程表单URL
     *
     * @param formKey
     * @param act 表单传递参数
     * @return
     */
    public static String getFormUrl(String formKey, Act act) {

        StringBuilder formUrl = new StringBuilder();

        String formServerUrl = Global.getConfig("activiti.form.server.url");
        if (StringUtils.isBlank(formServerUrl)) {
            formUrl.append(Global.getAdminPath());
        } else {
            formUrl.append(formServerUrl);
        }

        formUrl.append(formKey).append(formUrl.indexOf("?") == -1 ? "?" : "&");
        formUrl.append("act.taskId=").append(act.getTaskId() != null ? act.getTaskId() : "");
        formUrl.append("&act.taskName=").append(act.getTaskName() != null ? Encodes.urlEncode(act.getTaskName()) : "");
        formUrl.append("&act.taskDefKey=").append(act.getTaskDefKey() != null ? act.getTaskDefKey() : "");
        formUrl.append("&act.procInsId=").append(act.getProcInsId() != null ? act.getProcInsId() : "");
        formUrl.append("&act.procDefId=").append(act.getProcDefId() != null ? act.getProcDefId() : "");
        formUrl.append("&act.status=").append(act.getStatus() != null ? act.getStatus() : "");
        formUrl.append("&id=").append(act.getBusinessId() != null ? act.getBusinessId() : "");

        return formUrl.toString();
    }

    /**
     * 转换流程节点类型为中文说明
     *
     * @param type 英文名称
     * @return 翻译后的中文名称
     */
    public static String parseToZhType(String type) {
        Map<String, String> types = new HashMap<String, String>();
        types.put("userTask", "用户任务");
        types.put("serviceTask", "系统任务");
        types.put("startEvent", "开始节点");
        types.put("endEvent", "结束节点");
        types.put("exclusiveGateway", "条件判断节点(系统自动根据条件处理)");
        types.put("inclusiveGateway", "并行处理任务");
        types.put("callActivity", "子流程");
        return types.get(type) == null ? type : types.get(type);
    }

    public static UserEntity toActivitiUser(User user) {
        if (user == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getLoginName());
        userEntity.setFirstName(user.getName());
        userEntity.setLastName(StringUtils.EMPTY);
        userEntity.setPassword(user.getPassword());
        userEntity.setEmail(user.getEmail());
        userEntity.setRevision(1);
        return userEntity;
    }

    public static GroupEntity toActivitiGroup(Role role) {
        if (role == null) {
            return null;
        }
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(role.getEnname());
        groupEntity.setName(role.getName());
        groupEntity.setType(role.getRoleType());
        groupEntity.setRevision(1);
        return groupEntity;
    }

    /**
     * 获取聚引客最新版本号
     *
     * @date 2018年5月16日
     * @author linqunzhi
     */
    public static int getLastJuYingKeProcessVersion() {
        return Integer.parseInt(JYK_FLOW_LAST[2]);
    }
}
