package com.yunnex.ops.erp.modules.statistics.entity;

import java.util.ArrayList;
import java.util.List;

import com.yunnex.ops.erp.modules.good.entity.ErpGoodInfo;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.team.entity.ErpTeam;

/**
 * 提供前台接口
 * @author yunnex
 * @version 2017-12-09
 * 封装
 */

public class ErpStatisticsSelected {
	private List<ErpTeam> team=new ArrayList<ErpTeam>();
	private List<ErpGoodInfo> good=new ArrayList<ErpGoodInfo>();
	
	private List<User> planningPersons = new ArrayList<User>();
	
	public List<ErpTeam> getTeam() {
		return team;
	}
	public void setTeam(List<ErpTeam> team) {
		this.team = team;
	}
	public List<ErpGoodInfo> getGood() {
		return good;
	}
	public void setGood(List<ErpGoodInfo> good) {
		this.good = good;
	}
    public List<User> getPlanningPersons() {
        return planningPersons;
    }
    public void setPlanningPersons(List<User> planningPersons) {
        this.planningPersons = planningPersons;
    }
	

}
