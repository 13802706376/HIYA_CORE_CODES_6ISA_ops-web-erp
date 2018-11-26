package com.yunnex.ops.erp.modules.sys.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.sys.service.AgentUserService;

import yunnex.common.core.dto.ApiResult;

/**
 * 用户对外部系统接口
 */
@Controller
@RequestMapping("api/user")
public class UserApiController extends BaseController {
    private static final String SYS_ERROR_CODE = "20000";

    @Autowired
    private AgentUserService agentUserService;

    /**
     * 修改服务商管理员用户密码
     * 
     * @param params
     * @return
     */
    @RequestMapping(value = "agent/pwd/modify", method = RequestMethod.POST)
    public @ResponseBody ApiResult<Void> modifyAgentUserPwd(@RequestBody Map<String, String> params) {
        ApiResult apiResult = ApiResult.build();
        String msg = "API接口-修改服务商用户密码！";
        logger.info("{}入参：{}", msg, params);
        try {
            apiResult = agentUserService.modifyAgentAdminUserPwd(params);
        } catch (ServiceException e) {
            logger.error("{}出错！{}", e.getMessage(), e);
            return apiResult.error(SYS_ERROR_CODE, e.getMessage());
        } catch (RuntimeException e) {
            String err = "其他错误！";
            logger.error("{}{}", msg, err, e);
            return apiResult.error(SYS_ERROR_CODE, err);
        }
        logger.info("{}成功！", msg);
        return apiResult;
    }

}
