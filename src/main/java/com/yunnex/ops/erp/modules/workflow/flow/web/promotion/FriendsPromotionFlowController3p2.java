package com.yunnex.ops.erp.modules.workflow.flow.web.promotion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.workflow.flow.service.FriendsPromotionFlow3P2Service;


/**
 * 朋友圈推广提审流程
 * 
 * @author zjq
 * @date 2018年5月16日
 */
@Controller
@RequestMapping(value = "${adminPath}/friends/flow/3p2")
public class FriendsPromotionFlowController3p2 extends BaseController {

    @Autowired
    private FriendsPromotionFlow3P2Service friendsPromotionFlow3P2Service;
    /**
     * 朋友圈推广开户资料复审
     *
     * @param taskId
     * @param procInsId
     * @param isPass 是否通过  Y：通过N：不通过
     * @return
     * @date 2018年5月17日
     * @author hanhan
     */
    @RequestMapping(value = "friends_promote_info_review")
    @ResponseBody
    public JSONObject friendsPromoteInfoReviewV1(String taskId, String procInsId, String isPass, String reason){
        return friendsPromotionFlow3P2Service.friendsPromoteInfoReviewV1(taskId, procInsId, isPass, reason);
    }
    
    /**
     * 修改朋友圈推广开户资料
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2018年5月17日
     * @author hanhan
     */
    @RequestMapping(value = "modify_friends_promote_info")
    @ResponseBody
    public JSONObject modifyFriendsPromoteInfoV1(String taskId, String procInsId){
        return friendsPromotionFlow3P2Service.modifyFriendsPromoteInfoV1(taskId, procInsId);
    }

    
    /**
     * 确认朋友圈授权成功
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2018年5月17日
     * @author hanhan
     */
    @RequestMapping(value = "confirmed_friends_authorization_sucess")
    @ResponseBody
    public JSONObject confirmedFriendsAuthorizationSucessV1(String taskId, String procInsId) {
        return friendsPromotionFlow3P2Service.confirmedFriendsAuthorizationSucessV1(taskId, procInsId);
    }
}
