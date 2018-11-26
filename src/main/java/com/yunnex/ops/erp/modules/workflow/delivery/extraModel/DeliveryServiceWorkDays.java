package com.yunnex.ops.erp.modules.workflow.delivery.extraModel;

/**
 * 交付服务 中 工作天数类
 * 
 * @author linqunzhi
 * @date 2018年6月11日
 */
public class DeliveryServiceWorkDays {

    /** 聚引客相关数据 */
    private KeChangLai keChangLai;

    /** 客常来相关数据 */
    private JuYingKe juYingKe;

    /** 银联支付培训&测试（远程）任务应该完成的工作日天数 */
    private Integer shouldTrainTestDays;

    /** 物料制作跟踪任务应该完成的工作日天数 */
    private Integer shouldMaterielDays;

    /** 上门服务完成（首次营销策划服务）任务应该完成的工作日天数 */
    private Integer shouldVisitServiceDays;


    public KeChangLai getKeChangLai() {
        return keChangLai;
    }

    public JuYingKe getJuYingKe() {
        return juYingKe;
    }

    public void setJuYingKe(JuYingKe juYingKe) {
        this.juYingKe = juYingKe;
    }

    public class KeChangLai {
        /** 应完成交付的工作日天数 */
        private Integer shouldFlowEndDays;

        public Integer getShouldFlowEndDays() {
            return shouldFlowEndDays;
        }
        public void setShouldFlowEndDays(Integer shouldFlowEndDays) {
            this.shouldFlowEndDays = shouldFlowEndDays;
        }

    }

    public class JuYingKe {
        /** 应完成交付的工作日天数 */
        private Integer shouldFlowEndDays;

        public Integer getShouldFlowEndDays() {
            return shouldFlowEndDays;
        }

        public void setShouldFlowEndDays(Integer shouldFlowEndDays) {
            this.shouldFlowEndDays = shouldFlowEndDays;
        }
    }

    public Integer getShouldTrainTestDays() {
        return shouldTrainTestDays;
    }

    public void setShouldTrainTestDays(Integer shouldTrainTestDays) {
        this.shouldTrainTestDays = shouldTrainTestDays;
    }

    public Integer getShouldMaterielDays() {
        return shouldMaterielDays;
    }

    public void setShouldMaterielDays(Integer shouldMaterielDays) {
        this.shouldMaterielDays = shouldMaterielDays;
    }

    public Integer getShouldVisitServiceDays() {
        return shouldVisitServiceDays;
    }

    public void setShouldVisitServiceDays(Integer shouldVisitServiceDays) {
        this.shouldVisitServiceDays = shouldVisitServiceDays;
    }

    public void setKeChangLai(KeChangLai keChangLai) {
        this.keChangLai = keChangLai;
    }
}
