package com.yunnex.ops.erp.modules.visit.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.visit.constants.ErpVisitServiceConstants;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitCount;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceDetailInfo;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceInfo;
import com.yunnex.ops.erp.modules.visit.service.ErpVisitServiceInfoService;

/**
 * 上门服务Controller
 * 
 * @author R/Q
 * @version 2018-05-26
 */
@Controller
@RequestMapping(value = "${adminPath}/shopService/erpVisitServiceInfo")
public class ErpVisitServiceInfoController extends BaseController {

	@Autowired
    private ErpVisitServiceInfoService erpVisitServiceInfoService;
	
    @RequestMapping(value = "/index")
    @RequiresPermissions(ErpVisitServiceConstants.VISIT_SERVICE_DATA_QUERY)
    public String index(HttpServletRequest request, HttpServletResponse response) {
        return "modules/service/visit/visit-service";
    }

    /**
     * 业务定义：分页查询商户服务数据列表
     * 
     * @date 2018年5月26日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "/queryShopServiceDataList")
    @RequiresPermissions(ErpVisitServiceConstants.VISIT_SERVICE_DATA_QUERY)
    public Object queryShopServiceDataList(ErpVisitServiceInfo paramObj, HttpServletRequest request, HttpServletResponse response) {
        return erpVisitServiceInfoService.queryShopServiceDataList(new Page<ErpVisitServiceInfo>(request, response), paramObj);
    }

    /**
     * 业务定义：根据ID查询商户服务详情
     * 
     * @date 2018年5月28日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "/queryShopServiceDataById")
    public Object queryShopServiceDataById(String id) {
        return erpVisitServiceInfoService.get(id);
    }

    /**
     * 业务定义：查询对应服务项
     * 
     * @date 2018年5月29日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "/queryServiceItemData")
    @RequiresPermissions(ErpVisitServiceConstants.VISIT_SERVICE_DATA_EDIT_CREATE)
    public Object queryServiceItemData(ErpVisitServiceInfo paramObj) {
        return erpVisitServiceInfoService.queryServiceItemData(paramObj);
    }

    /**
     * 业务定义：查询上门目的列表
     * 
     * @date 2018年5月29日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "/queryServiceGoalData")
    public Object queryServiceGoalData(String serviceTypeCode) {
        return erpVisitServiceInfoService.queryServiceGoalData(serviceTypeCode);
    }

    /**
     * 业务定义：查询商户培训类型服务项记录，查询范围=首次营销策划上门服务+物料上门服务
     * 
     * @date 2018年5月31日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "/queryTrainItemRecord")
    public Object queryTrainItemRecord(String shopInfoId) {
        return erpVisitServiceInfoService.queryTrainItemRecord(shopInfoId);
    }

    /**
     * 业务定义：校验预约开始时间冲突
     * 
     * @date 2018年6月4日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "/checkAppointedDate")
    public List<ErpVisitServiceInfo> checkAppointedDate(ErpVisitServiceInfo paramObj) {
        return erpVisitServiceInfoService.checkAppointedDate(paramObj);
    }
    
    /**
     * 业务定义：校验预约开始时间冲突
     * 
     * @date 2018年6月4日
     * @author wangwei
     */
    @ResponseBody
    @RequestMapping(value = "/getHeader")
    public List<ErpVisitServiceInfo> getHeader() {
    	List<ErpVisitServiceInfo> list=erpVisitServiceInfoService.getHeader();
    	for(ErpVisitServiceInfo e:list){
    		String serviceGoal="";
    		List<ErpVisitServiceInfo> list1 =erpVisitServiceInfoService.getHeaderText(e.getServiceTypeCode());
    		for(ErpVisitServiceInfo e1:list1){
    			serviceGoal+=e1.getServiceGoal()+",";
    		}
    		e.setServiceGoal(serviceGoal.substring(0, serviceGoal.length()-1));
    	}
        return list;
    }

    /**
     * 业务定义：依据根据上门服务查询权限查询对应团队信息
     * 
     * @date 2018年7月5日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "/queryTeamByRole")
    public List<Map<String, Object>> queryTeamByRole() {
        return erpVisitServiceInfoService.queryTeamByRole();
    }

    /**
     * 业务定义：查询团队成员上门服务情况
     * 
     * @date 2018年7月12日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "/queryTeamUserServiceInfo", method = RequestMethod.POST)
    public Object queryTeamUserServiceInfo(@RequestBody ErpVisitServiceInfo paramObj, HttpServletRequest request, HttpServletResponse response) {
        return erpVisitServiceInfoService.queryTeamUserServiceCount(paramObj, new Page<Map<String, Object>>(request, response));
    }
    
    
    /**
     * 业务定义：分页查询商户服务数据列表
     * 
     * @date 2018年5月26日
     * @author R/Q	
     */
    @ResponseBody
    @RequestMapping(value = "/queryShopServiceDetailList",method = RequestMethod.POST)
    public Object queryShopServiceDetailList(@RequestBody ErpVisitServiceDetailInfo paramObj, HttpServletRequest request, HttpServletResponse response) {
        return erpVisitServiceInfoService.queryShopServiceDetailList(new Page<ErpVisitServiceDetailInfo>(request, response), paramObj);
    }
    
    @RequestMapping(value = "/openServiceDetail")
    public String openServiceDetail(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/statistics/openVisitService";
    }
    
    @RequestMapping(value = "/openServiceDetailByUser")
    public String openServiceDetailByUser(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/statistics/openVisitServiceByUser";
    }
}