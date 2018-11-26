package com.yunnex.ops.erp.modules.team.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 成员Entity
 * @author huanghaidong
 * @version 2017-10-26
 */
public class ErpTeamUser extends DataEntity<ErpTeamUser> {

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public static final int LEADER_FLAG_YES = 1;

    public static final int LEADER_FLAG_NO = 0;
	
	private static final long serialVersionUID = 1L;
	private String teamId;		// 团队编号
    private Integer leaderFlag; // 管理员标识（0不是）
	private String userId;		// 成员id
	
	public ErpTeamUser() {
		super();
	}

	public ErpTeamUser(String id){
		super(id);
	}

	@Length(min=1, max=64, message="团队编号长度必须介于 1 和 64 之间")
	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	
    @Length(min = 1, max = 1, message = "管理员标识（0不是）长度必须介于 1 和 1 之间")
    public Integer getLeaderFlag() {
		return leaderFlag;
	}

    public void setLeaderFlag(Integer leaderFlag) {
		this.leaderFlag = leaderFlag;
	}
	
	@Length(min=1, max=64, message="成员id长度必须介于 1 和 64 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	
}