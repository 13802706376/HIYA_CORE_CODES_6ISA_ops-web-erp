package com.yunnex.ops.erp.modules.sys.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunnex.junit.BaseTest;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.modules.sys.dto.AgentUserSaveDto;
import com.yunnex.ops.erp.modules.sys.entity.Role;

public class AgentUserServiceTest extends BaseTest {

    @Autowired
    private AgentUserService agentUserService;

    @Test
    public void save() {
        AgentUserSaveDto saveDto = new AgentUserSaveDto();
        saveDto.setUserName("a地");
        saveDto.setMobile("1234567891a");
        saveDto.setLoginName("fdsa");
        saveDto.setEmail("fdjsi@fdsa.com");
        saveDto.setJobIconImg("fds");
        saveDto.setJobNumber("fjieow");
        saveDto.setJobScore("3");
        List<Role> roles = new ArrayList<>();
        roles.add(new Role());
        saveDto.setRoles(roles);
        BaseResult result = agentUserService.save(saveDto);
        System.out.println(result);
    }

}
