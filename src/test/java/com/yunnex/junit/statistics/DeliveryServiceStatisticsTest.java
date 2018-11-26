package com.yunnex.junit.statistics;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yunnex.junit.BaseTest;
import com.yunnex.ops.erp.modules.statistics.dto.OrderSecurityRequestDto;
import com.yunnex.ops.erp.modules.statistics.dto.deliveryService.DeliveryServiceStatisticsRequestDto;
import com.yunnex.ops.erp.modules.statistics.dto.deliveryService.QueryDataResponseDto;
import com.yunnex.ops.erp.modules.statistics.dto.deliveryService.ServiceTypeDto;
import com.yunnex.ops.erp.modules.statistics.service.DeliveryServiceStatisticsService;

public class DeliveryServiceStatisticsTest extends BaseTest {

    @Autowired
    private DeliveryServiceStatisticsService deliveryServiceStatisticsService;

    @Test
    public void findTeam() {
        String principalId = "1";// 系统管理员
        DeliveryServiceStatisticsRequestDto dto = new DeliveryServiceStatisticsRequestDto();
        dto.setStartDateStr("2017-11-20");
        dto.setEndDateStr("2018-08-08");
        // 服务类型过滤
        List<ServiceTypeDto> serviceTypeList = new ArrayList<>();
        ServiceTypeDto serviceType1 = new ServiceTypeDto();
        List<Integer> includeList1 = new ArrayList<>();
        List<Integer> notIncludeList1 = new ArrayList<>();
        includeList1.add(5);
        notIncludeList1.add(6);
        serviceType1.setIncludeList(includeList1);
        serviceType1.setNotIncludeList(notIncludeList1);
        ServiceTypeDto serviceType2 = new ServiceTypeDto();
        List<Integer> includeList2 = new ArrayList<>();
        List<Integer> notIncludeList2 = new ArrayList<>();
        includeList2.add(5);
        notIncludeList2.add(6);
        serviceType2.setIncludeList(includeList2);
        serviceType2.setNotIncludeList(notIncludeList2);
        serviceTypeList.add(serviceType1);
        serviceTypeList.add(serviceType2);
        dto.setServiceTypeList(serviceTypeList);
        // 是否延期
        dto.setDelayFlag("N");
        // 是否完成
        dto.setFlowEndFlag("N");

        // 订单类型
        List<Integer> orderTypeList = new ArrayList<>();
        orderTypeList.add(1);
        dto.setOrderTypeList(orderTypeList);

        // 权限配置
        OrderSecurityRequestDto orderSecurity = new OrderSecurityRequestDto();
        List<Integer> agentIdLists = new ArrayList<>();
        agentIdLists.add(31);
        orderSecurity.setAgentIdList(agentIdLists);

        List<Integer> orderTypeLists = new ArrayList<>();
        orderTypeLists.add(1);
        orderTypeLists.add(2);
        orderSecurity.setOrderTypeList(orderTypeLists);
        dto.setOrderSecurity(orderSecurity);

        deliveryServiceStatisticsService.findTeamStatistics(null, dto, principalId);
    }

    @Test
    public void getQueryData() {
        QueryDataResponseDto dto = deliveryServiceStatisticsService.getTeamQueryData("1");
        System.err.println(JSON.toJSONString(dto));
    }

    public static void main(String[] args) {
        ServiceTypeDto serviceType1 = new ServiceTypeDto();
        List<Integer> includeList1 = new ArrayList<>();
        List<Integer> notIncludeList1 = new ArrayList<>();
        includeList1.add(5);
        notIncludeList1.add(6);
        serviceType1.setIncludeList(includeList1);
        serviceType1.setNotIncludeList(notIncludeList1);

        System.err.println(JSON.toJSONString(serviceType1));

        String abc = "{\"includeList\":[5,6]}";
        serviceType1 = JSON.parseObject(abc, ServiceTypeDto.class);
        System.err.println(JSON.toJSONString(serviceType1));
    }

}
