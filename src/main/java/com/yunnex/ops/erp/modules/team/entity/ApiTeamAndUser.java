package com.yunnex.ops.erp.modules.team.entity;

import java.util.List;
/**
 * 团队和团队成员封装方法
 * @author ThinkGem
 * @version 2014-7-5.
 * 
 * 
 */
public class ApiTeamAndUser {
	private String teamId;

	private String teamName;
	
	private String userName;

	private List<ErpTeamUser> teamUser;

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public List<ErpTeamUser> getTeamUser() {
		return teamUser;
	}

	public void setTeamUser(List<ErpTeamUser> teamUser) {
		this.teamUser = teamUser;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	

}
