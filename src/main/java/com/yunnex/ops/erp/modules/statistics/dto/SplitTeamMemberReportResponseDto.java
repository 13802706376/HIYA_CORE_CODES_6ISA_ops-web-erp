package com.yunnex.ops.erp.modules.statistics.dto;

import com.yunnex.ops.erp.common.persistence.ResponseDto;

/**
 * 团队成员跟进分单统计
 * 
 * @author linqunzhi
 * @date 2018年5月15日
 */
public class SplitTeamMemberReportResponseDto extends ResponseDto<SplitTeamMemberReportResponseDto> {

    private static final long serialVersionUID = 7275027180365128954L;

    /** 用户id */
    private String userId;

    /** 用户名称 */
    private String userName;

    /** 角色名称 */
    private String userRole;

    /** 当前跟进分单总数 */
    private int followOrder;

    /** 有任务正在处理的分单总数 */
    private int taskOrder;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public int getFollowOrder() {
        return followOrder;
    }

    public void setFollowOrder(int followOrder) {
        this.followOrder = followOrder;
    }

    public int getTaskOrder() {
        return taskOrder;
    }

    public void setTaskOrder(int taskOrder) {
        this.taskOrder = taskOrder;
    }
}
