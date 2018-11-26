package com.yunnex.junit.sys.user;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yunnex.junit.BaseTest;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.service.UserService;

public class UserTest extends BaseTest {

    @Autowired
    private UserService userService;

    /**
     * 根据团队id获取用户信息列表
     *
     * @date 2018年5月16日
     * @author linqunzhi
     */
    @Test
    public void findByTeamId() {
        List<User> result = userService.findByTeamId("cfe09996444a475a82eb2d4a46de590b", 0);
        System.out.println(JSON.toJSONString(result));
    }

    @Test
    public void getUserByCondition() {
        List<User> result = userService.getUserByCondition("aa", "ops_adviser", "ops_adviser_agent");
        System.out.println(JSON.toJSONString(result));
    }
}
