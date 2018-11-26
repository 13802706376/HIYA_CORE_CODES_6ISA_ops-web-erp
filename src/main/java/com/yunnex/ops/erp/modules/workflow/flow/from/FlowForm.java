package com.yunnex.ops.erp.modules.workflow.flow.from;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunnex.ops.erp.modules.act.entity.Act;
import com.yunnex.ops.erp.modules.act.entity.TaskExt;
import com.yunnex.ops.erp.modules.act.utils.Variable;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shopdata.entity.ErpPayIntopieces;
import com.yunnex.ops.erp.modules.shopdata.entity.ErpShopDataInput;
import com.yunnex.ops.erp.modules.workflow.flow.dto.FlowTaskDto;
import com.yunnex.ops.erp.modules.workflow.flow.entity.JykFlow;
import com.yunnex.ops.erp.modules.workflow.flow.entity.SdiFlow;
import com.yunnex.ops.erp.modules.workflow.flow.entity.SubTask;

/**
 * 任务进度信息表
 * @author yunnex
 * @date 2017年11月2日
 */
public class FlowForm implements Comparable<FlowForm> {

    private Act act;
    /** 订单编号 */
    private String orderNo;
    /** 商户名称 */
    private String shopName;
    /** 商品类型 */
    private String goodType;
    /** 订单类型 */
    private String orderType;
    /** 商品数量 */
    private String goodCount;
    /** 商品名称 */
    private String goodName;
    /** 购买时间 */
    private Date orderTime;
    /** 投放时间 */
    private Date deliveryTime;
    /** 当前任务数 */
    private Integer taskCount = 0;
    /** 既将过期任务数 */
    private Integer taskExpiringCount = 0;
    /** 超时任务数 */
    private Integer taskOverTimeCount = 0;
    /** 关注任务数 */
    private Integer followCount = 0;
    /** 关注既将到期任务数 */
    private Integer followExpiringCount = 0;
    /** 关注超时任务数 */
    private Integer followOverTimeCount = 0;
    /** 加急状态 */
    private Integer hurryFlag;
    /** 暂停状态 */
    private String suspendFlag;
    /** 是否存在超时风险标识 */
    private String timeoutFlag;
    /** 联系方式 */
    private String contactWay;
    /** 子任务内容 */
    private String subTaskStr;

    private List<FlowTaskDto> flowTaskDtos;

    public List<FlowTaskDto> getFlowTaskDtos() {
        return flowTaskDtos;
    }

    public void setFlowTaskDtos(List<FlowTaskDto> flowTaskDtos) {
        this.flowTaskDtos = flowTaskDtos;
    }

    /** 是否折叠 */
    private Integer taskConsumTime;

    /** 流程变量 */
    private Variable vars; // 流程变量

    private Integer num;// 分单数

    private String agentName;// 服务商名称
    
    /** 流程标志 */
    private String flowMark;
    
    // 封装数据
    private TaskExt taskExt;
    private JykFlow jykFlow;
    private ErpOrderSplitInfo orderSplitInfo;
    private ErpOrderOriginalInfo orderOriginalInfo;
    private ErpShopInfo shop;
    private SdiFlow sdiFlow;
    private ErpShopDataInput shopDataInput;
    private ErpPayIntopieces payInto;
    private String shopDataInputId;
    private String payIntopiecesId;
    private String taskDisplay;
    private String nextContactTimeStr;
    private String taskStartDate;
    private String taskEndDate;
    private String assignee;

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getTaskStartDate() {
        return taskStartDate;
    }

    public void setTaskStartDate(String taskStartDate) {
        this.taskStartDate = taskStartDate;
    }

    public String getTaskEndDate() {
        return taskEndDate;
    }

    public void setTaskEndDate(String taskEndDate) {
        this.taskEndDate = taskEndDate;
    }

    /** 合并子任务内容 */
    private List<SubTask> subTaskStrList = new ArrayList<SubTask>();
    private Date orderDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
    public String getAgentName()
    {
        return agentName;
    }
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Variable getVars() {
        return vars;
    }

    public void setVars(Variable vars) {
        this.vars = vars;
    }

    public Integer getTaskConsumTime() {
        return taskConsumTime;
    }

    public void setTaskConsumTime(Integer taskConsumTime) {
        this.taskConsumTime = taskConsumTime;
    }

    public List<SubTask> getSubTaskStrList() {
        return subTaskStrList;
    }

    public void setSubTaskStrList(List<SubTask> subTaskStrList) {
        this.subTaskStrList = subTaskStrList;
    }

    public Act getAct() {
        return act;
    }

    public void setAct(Act act) {
        this.act = act;
    }

    public String getSubTaskStr() {
        return subTaskStr;
    }

    public void setSubTaskStr(String subTaskStr) {
        this.subTaskStr = subTaskStr;
    }

    public String getContactWay() {
        return contactWay;
    }

    public void setContactWay(String contactWay) {
        this.contactWay = contactWay;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Integer getHurryFlag() {
        return hurryFlag;
    }

    public void setHurryFlag(Integer hurryFlag) {
        this.hurryFlag = hurryFlag;
    }

    public String getTimeoutFlag() {
        return timeoutFlag;
    }

    public void setTimeoutFlag(String timeoutFlag) {
        this.timeoutFlag = timeoutFlag;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getGoodType() {
        return goodType;
    }

    public void setGoodType(String goodType) {
        this.goodType = goodType;
    }

    public String getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(String goodCount) {
        this.goodCount = goodCount;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Integer getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }

    public Integer getTaskExpiringCount() {
        return taskExpiringCount;
    }

    public void setTaskExpiringCount(Integer taskExpiringCount) {
        this.taskExpiringCount = taskExpiringCount;
    }

    public Integer getTaskOverTimeCount() {
        return taskOverTimeCount;
    }

    public void setTaskOverTimeCount(Integer taskOverTimeCount) {
        this.taskOverTimeCount = taskOverTimeCount;
    }

    public Integer getFollowCount() {
        return followCount;
    }

    public void setFollowCount(Integer followCount) {
        this.followCount = followCount;
    }

    public Integer getFollowExpiringCount() {
        return followExpiringCount;
    }

    public void setFollowExpiringCount(Integer followExpiringCount) {
        this.followExpiringCount = followExpiringCount;
    }

    public Integer getFollowOverTimeCount() {
        return followOverTimeCount;
    }

    public void setFollowOverTimeCount(Integer followOverTimeCount) {
        this.followOverTimeCount = followOverTimeCount;
    }

    public String getFlowMark() {
        return flowMark;
    }

    public void setFlowMark(String flowMark) {
        this.flowMark = flowMark;
    }

    public TaskExt getTaskExt() {
        return taskExt;
    }

    public void setTaskExt(TaskExt taskExt) {
        this.taskExt = taskExt;
    }

    public JykFlow getJykFlow() {
        return jykFlow;
    }

    public void setJykFlow(JykFlow jykFlow) {
        this.jykFlow = jykFlow;
    }

    public ErpOrderSplitInfo getOrderSplitInfo() {
        return orderSplitInfo;
    }

    public void setOrderSplitInfo(ErpOrderSplitInfo orderSplitInfo) {
        this.orderSplitInfo = orderSplitInfo;
    }

    public ErpOrderOriginalInfo getOrderOriginalInfo() {
        return orderOriginalInfo;
    }

    public void setOrderOriginalInfo(ErpOrderOriginalInfo orderOriginalInfo) {
        this.orderOriginalInfo = orderOriginalInfo;
    }

    public ErpShopInfo getShop() {
        return shop;
    }

    public void setShop(ErpShopInfo shop) {
        this.shop = shop;
    }

    public SdiFlow getSdiFlow() {
        return sdiFlow;
    }

    public void setSdiFlow(SdiFlow sdiFlow) {
        this.sdiFlow = sdiFlow;
    }

    public ErpShopDataInput getShopDataInput() {
        return shopDataInput;
    }

    public void setShopDataInput(ErpShopDataInput shopDataInput) {
        this.shopDataInput = shopDataInput;
    }

    public ErpPayIntopieces getPayInto() {
        return payInto;
    }

    public void setPayInto(ErpPayIntopieces payInto) {
        this.payInto = payInto;
    }

    public String getShopDataInputId() {
        return shopDataInputId;
    }

    public void setShopDataInputId(String shopDataInputId) {
        this.shopDataInputId = shopDataInputId;
    }

    public String getPayIntopiecesId() {
        return payIntopiecesId;
    }

    public void setPayIntopiecesId(String payIntopiecesId) {
        this.payIntopiecesId = payIntopiecesId;
    }

    public String getTaskDisplay() {
        return taskDisplay;
    }

    public void setTaskDisplay(String taskDisplay) {
        this.taskDisplay = taskDisplay;
    }

    @Override
    public String toString() {
        if (act != null) {
            return "FlowForm [任务ID=" + act.getTaskId() + "，任务名称=" + act.getTaskName() + "]";
        }
        return super.toString();
    }

    /**
     * 按完成进度百分比排序
     *
     * @param o
     * @return
     * @date 2017年11月10日
     * @author yunnex
     */
    @Override
    public int compareTo(FlowForm o) {
        int i =  o.getTaskConsumTime()-this.getTaskConsumTime();
        return i;
    }

    public String getSuspendFlag() {
        return suspendFlag;
    }

    public void setSuspendFlag(String suspendFlag) {
        this.suspendFlag = suspendFlag;
    }

    public String getNextContactTimeStr() {
        return nextContactTimeStr;
    }

    public void setNextContactTimeStr(String nextContactTimeStr) {
        this.nextContactTimeStr = nextContactTimeStr;
    }
}