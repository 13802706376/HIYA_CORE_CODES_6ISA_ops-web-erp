package com.yunnex.ops.erp.modules.message.extraModel;

import java.util.Date;

import com.yunnex.ops.erp.modules.message.entity.ErpServiceProgressTemplate;

/**
 * 服务进度 扩展类
 * 
 */
public class ServiceProgressExtra extends ErpServiceProgressTemplate {

    private static final long serialVersionUID = -5767979523463800182L;

    /** 进度id */
    private String progressId;

    /** 启动时间 */
    private Date startTime;

    /** 流程id */
    private String procInsId;

    /** 交互入口类型 */
    private String linkType;

    /** 交互入口参数 */
    private String linkParam;

    /** 掌贝id */
    private String zhangbeiId;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getLinkParam() {
        return linkParam;
    }

    public void setLinkParam(String linkParam) {
        this.linkParam = linkParam;
    }

    public String getZhangbeiId() {
        return zhangbeiId;
    }

    public void setZhangbeiId(String zhangbeiId) {
        this.zhangbeiId = zhangbeiId;
    }

    public String getProgressId() {
        return progressId;
    }

    public void setProgressId(String progressId) {
        this.progressId = progressId;
    }
}
