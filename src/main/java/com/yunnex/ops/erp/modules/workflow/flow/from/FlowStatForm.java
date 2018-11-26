package com.yunnex.ops.erp.modules.workflow.flow.from;

import java.util.Date;

import com.yunnex.ops.erp.modules.act.entity.Act;

/**
 * 任务统计Entity
 * 
 * @author Frank
 * @version 2017-10-27
 */
public class FlowStatForm {

    private int taskNormal = 0;
    private int taskOnlyWillOvertime = 0;
    private int taskOverTime = 0;
    private int followNormal = 0;
    private int followOnlyWillOvertime = 0;
    private int followOverTime = 0;

    public int getTaskNormal() {
        return taskNormal;
    }

    public void setTaskNormal(int taskNormal) {
        this.taskNormal = taskNormal;
    }

    public int getTaskOnlyWillOvertime() {
        return taskOnlyWillOvertime;
    }

    public void setTaskOnlyWillOvertime(int taskOnlyWillOvertime) {
        this.taskOnlyWillOvertime = taskOnlyWillOvertime;
    }

    public int getTaskOverTime() {
        return taskOverTime;
    }

    public void setTaskOverTime(int taskOverTime) {
        this.taskOverTime = taskOverTime;
    }

    public int getFollowNormal() {
        return followNormal;
    }

    public void setFollowNormal(int followNormal) {
        this.followNormal = followNormal;
    }

    public int getFollowOnlyWillOvertime() {
        return followOnlyWillOvertime;
    }

    public void setFollowOnlyWillOvertime(int followOnlyWillOvertime) {
        this.followOnlyWillOvertime = followOnlyWillOvertime;
    }

    public int getFollowOverTime() {
        return followOverTime;
    }

    public void setFollowOverTime(int followOverTime) {
        this.followOverTime = followOverTime;
    }

}
