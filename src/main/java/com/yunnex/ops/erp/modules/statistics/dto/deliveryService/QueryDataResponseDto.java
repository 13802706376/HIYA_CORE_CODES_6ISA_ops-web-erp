package com.yunnex.ops.erp.modules.statistics.dto.deliveryService;

import java.util.List;
import java.util.Map;

import com.yunnex.ops.erp.common.persistence.ResponseDto;

/**
 * 查询数据 responseDto
 * 
 * @author linqunzhi
 * @date 2018年5月31日
 */
public class QueryDataResponseDto extends ResponseDto<QueryDataResponseDto> {

    private static final long serialVersionUID = -2164768192684470255L;
    
    /** 团队列表 */
    private List<Map<String, String>> teamList;

    /** 时间维度列表 */
    private List<Map<String, String>> dateTypeList;

    /** 服务列表 */
    private List<Map<String, String>> serviceTypeList;
    
    /** 服务列表 */
    private List<Map<String, String>> serviceCodeList;
    
    /** 服务列表 */
    private List<Map<String, String>> userList;
    

    public List<Map<String, String>> getServiceCodeList() {
		return serviceCodeList;
	}

	public void setServiceCodeList(List<Map<String, String>> serviceCodeList) {
		this.serviceCodeList = serviceCodeList;
	}

	public List<Map<String, String>> getUserList() {
		return userList;
	}

	public void setUserList(List<Map<String, String>> userList) {
		this.userList = userList;
	}

	public List<Map<String, String>> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<Map<String, String>> teamList) {
        this.teamList = teamList;
    }

    public List<Map<String, String>> getDateTypeList() {
        return dateTypeList;
    }

    public void setDateTypeList(List<Map<String, String>> dateTypeList) {
        this.dateTypeList = dateTypeList;
    }

    public List<Map<String, String>> getServiceTypeList() {
        return serviceTypeList;
    }

    public void setServiceTypeList(List<Map<String, String>> serviceTypeList) {
        this.serviceTypeList = serviceTypeList;
    }
}
