package com.yunnex.junit;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.utils.SpringContextHolder;
import com.yunnex.ops.erp.modules.act.dao.TaskExtDao;
import com.yunnex.ops.erp.modules.act.entity.TaskExt;
import com.yunnex.ops.erp.modules.act.service.TaskExtService;
import com.yunnex.ops.erp.modules.sys.constant.SysConstant;
import com.yunnex.ops.erp.modules.sys.dao.UserDao;
import com.yunnex.ops.erp.modules.sys.entity.Office;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.service.AgentUserService;

public class CrudInterceptorTest extends BaseTest
{

    @Autowired
    AgentUserService agentUserService;

    @Autowired
    UserDao userDao;

    @Autowired
    TaskExtService taskExtService;

    @Test
    public void test()
    {
        TaskExtDao actDao = SpringContextHolder.getBean(TaskExtDao.class);

        // 默认修改
        TaskExt taskExt = new TaskExt();
        taskExt.setId("1001");
        taskExt.setTaskId("24b0358a4d0e4835b6bb8ae309961934");
        taskExt.setStatus("5");
        taskExt.setPendingProdFlag("H");
        actDao.update(taskExt);
        actDao.updateTaskState(taskExt);

        // actDao.updateTaskStateMap("Y", "5", "24b0358a4d0e4835b6bb8ae309961900");

        actDao.insert(taskExt);
        actDao.delete(taskExt);

        User agentUser = new User();
        // 公司和部门字段不允许为空
        agentUser.setCompany(new Office(Constant.BLANK));
        agentUser.setOffice(new Office(Constant.BLANK));
        // 用户姓名只在创建用户时设置，不更新
        agentUser.setName("4567");
        // 员工ID = 服务商编号 + 序号
        agentUser.setEmployeeId("40098");
        agentUser.setRemarks("444");
        // 排序
        agentUser.setSort(SysConstant.SORT_ADMIN);
        agentUser.setType(SysConstant.TYPE_AGENT);
        agentUser.setAgentUserFlag(Constant.YES);

        agentUser.setAgentName("52353");
        agentUser.setLoginName("52353");
        agentUser.setPassword("52353");
        agentUser.setSalt("52353");
        agentUser.setAgentId(5235);
        // userDao.insert(entity);
        agentUserService.save(agentUser);
    }
}