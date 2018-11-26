package com.yunnex.ops.erp.modules.statistics.dto.deliveryService;

import java.util.List;

import com.yunnex.ops.erp.modules.statistics.dto.PageResponseDto;

/**
 * 生产服务报表数据
 * 
 * @author linqunzhi
 * @date 2018年5月28日
 */
public class DeliveryServiceStatisticsAllResponseDto extends PageResponseDto<DeliveryServiceStatisticsResponseDto> {

    private static final long serialVersionUID = -2074271740961382112L;

    /** 不展示的列 */
    private List<String> notShowColumnList;

    /** 不展示查询条件 */
    private List<String> notShowQueryList;

    /** 延迟服务 提示内容数组 */
    private String[] delayServiceContentArr;
    
    /** 延迟服务 提示内容数组 */
    private String[] delayServiceMUArr;
    
    /** 延迟服务 提示内容数组 */
    private String[] delayServiceVCArr;
    
    public String[] getDelayServiceMUArr() {
		return delayServiceMUArr;
	}

	public void setDelayServiceMUArr(String[] delayServiceMUArr) {
		this.delayServiceMUArr = delayServiceMUArr;
	}

	public String[] getDelayServiceVCArr() {
		return delayServiceVCArr;
	}

	public void setDelayServiceVCArr(String[] delayServiceVCArr) {
		this.delayServiceVCArr = delayServiceVCArr;
	}

	public List<String> getNotShowColumnList() {
        return notShowColumnList;
    }

    public void setNotShowColumnList(List<String> notShowColumnList) {
        this.notShowColumnList = notShowColumnList;
    }

    public List<String> getNotShowQueryList() {
        return notShowQueryList;
    }

    public void setNotShowQueryList(List<String> notShowQueryList) {
        this.notShowQueryList = notShowQueryList;
    }

    public String[] getDelayServiceContentArr() {
        return delayServiceContentArr;
    }

    public void setDelayServiceContentArr(String[] delayServiceContentArr) {
        this.delayServiceContentArr = delayServiceContentArr;
    }
}
