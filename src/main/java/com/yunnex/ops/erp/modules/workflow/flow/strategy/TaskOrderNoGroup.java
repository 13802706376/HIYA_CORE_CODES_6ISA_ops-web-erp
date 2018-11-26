package com.yunnex.ops.erp.modules.workflow.flow.strategy;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunnex.ops.erp.modules.workflow.flow.dto.FlowTaskDto;
import com.yunnex.ops.erp.modules.workflow.flow.dto.FlowTaskGroupDto;

public class TaskOrderNoGroup implements IGroup<FlowTaskDto, FlowTaskGroupDto> {


    private static final String SNAME = "sName";
    private static final String HURRYFLAG = "hurryFlag";
    private static final String PROCINSKEY = "procInsKey";
    private static final String NUMBER = "number";
    private static final String NAME = "name";
    private static Map<String, String> map = new HashMap<String, String>();

    static {
        map.put("friends_promotion_flow", "朋友圈推广提审");
        map.put("microblog_promotion_flow", "微博推广提审");
        map.put("unionpay_intopieces_flow", "银联支付进件");
        map.put("wechatpay_intopieces_flow", "微信支付进件");
        map.put("shop_data_input_flow", "商户资料录入");
        map.put("jyk_flow_new", "聚引客");
        map.put("jyk_flow_new_3.2", "聚引客");
        map.put("jyk_flow", "聚引客");
        map.put("jyk_flow_3.2", "聚引客");
    }


    private static final Logger logger = LoggerFactory.getLogger(TaskOrderNoGroup.class);


    /**
     * 分组
     */
    @Override
    public List<FlowTaskGroupDto> group(List<FlowTaskDto> list) {

        List<FlowTaskGroupDto> dtos = new ArrayList<FlowTaskGroupDto>();

        FlowTaskGroupDto flowTaskGroupDto = null;

        for (FlowTaskDto flowTaskDto : list) {

            try {
                flowTaskGroupDto = new FlowTaskGroupDto();
                BeanUtils.copyProperty(flowTaskGroupDto, NAME, map.get(flowTaskDto.getProcessDefineKey()));
                BeanUtils.copyProperty(flowTaskGroupDto, NUMBER, flowTaskDto.getOrderNumber());
                BeanUtils.copyProperty(flowTaskGroupDto, PROCINSKEY, flowTaskDto.getProcessDefineKey());
                BeanUtils.copyProperty(flowTaskGroupDto, HURRYFLAG, flowTaskDto.getHurryFlag());
                BeanUtils.copyProperty(flowTaskGroupDto, SNAME, flowTaskDto.getShopName());
                int _index = dtos.indexOf(flowTaskGroupDto);
                if (-1 == _index) {
                    dtos.add(flowTaskGroupDto);
                } else {
                    flowTaskGroupDto = dtos.get(_index);
                }
                flowTaskGroupDto.add(flowTaskDto);
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage(), e);
            } catch (InvocationTargetException e) {
                logger.error(e.getMessage(), e);
            }

        }

        return dtos;
    }

}
