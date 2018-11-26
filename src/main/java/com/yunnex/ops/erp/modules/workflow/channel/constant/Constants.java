package com.yunnex.ops.erp.modules.workflow.channel.constant;

public interface Constants {
    /** 微博充值状态：提交 */
    String STATUS_COMMIT = "Commit";
    /** 微博充值状态：申请中 */
    String STATUS_APPLYING = "Applying";
    /** 微博充值状态：成功 */
    String STATUS_SUCCESS = "Success";
    /** 微博充值状态：取消 */
    String STATUS_CANCEL = "Cancel";

    /** 微博充值来源：流程 */
    String SOURCE_FLOW = "Flow";
    /** 微博充值来源：管理界面 */
    String SOURCE_MANAGE = "Manage";

    /** 推广状态 未开始 */
    String NOTSTART = "notstart";
    /** 推广状态：推广中 */
    String RUNNING = "running";
    /** 推广状态：推广结束 */
    String SUCCESS = "success";
}
