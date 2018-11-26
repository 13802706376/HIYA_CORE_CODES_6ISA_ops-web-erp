package com.yunnex.ops.erp.modules.team.web;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.act.dao.ActDao;
import com.yunnex.ops.erp.modules.statistics.dto.deliveryService.DeliveryServiceStatisticsAllResponseDto;
import com.yunnex.ops.erp.modules.statistics.dto.deliveryService.DeliveryServiceStatisticsRequestDto;
import com.yunnex.ops.erp.modules.statistics.dto.deliveryService.DeliveryServiceStatisticsResponseDto;
import com.yunnex.ops.erp.modules.statistics.enums.DeliveryServiceStatisticsColumn;
import com.yunnex.ops.erp.modules.statistics.web.DeliveryServiceStatisticsController;
import com.yunnex.ops.erp.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.team.entity.ErpTeamExp;
import com.yunnex.ops.erp.modules.team.entity.ErpTeamTotal;
import com.yunnex.ops.erp.modules.team.entity.ErpUserTotal;
import com.yunnex.ops.erp.modules.team.service.ErpTeamTotalService;
import com.yunnex.ops.erp.modules.team.service.ErpUserTotalService;
import com.yunnex.ops.erp.modules.visit.dao.ErpVisitServiceInfoDao;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitCount;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceInfo;
import com.yunnex.ops.erp.modules.visit.service.ErpVisitCountService;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowMonitorService;

/**
 * 团队Controller
 * 
 * @author huanghaidong
 * @version 2017-10-26
 */
@Controller
@RequestMapping(value = "${adminPath}/team/erpTeamTotal")
public class ErpTeamTotalController extends BaseController {
	private Logger log = LoggerFactory.getLogger(ErpTeamTotalController.class);

	@Autowired
	private ErpTeamTotalService erpTeamTotalService;
	
	@Autowired
	private ErpUserTotalService erpUserTotalService;
	
	@Autowired
	private ErpVisitCountService erpVisitCountService;
	
	@Autowired
	private ErpVisitServiceInfoDao erpVisitServiceInfoDao;
	
	@Autowired
	private ActDao actDao;
	
	@Autowired
	private WorkFlowMonitorService workFlowMonitorService;
	
	

	@RequestMapping(value = "/findTeamCount", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject findTeamCount(@RequestBody ErpTeamTotal requestDto, HttpServletRequest request,
			HttpServletResponse response) {
		Principal principal = UserUtils.getPrincipal();
//		Page<ErpTeam> teamList = erpTeamService.findPage(new Page<ErpTeam>(request, response), new ErpTeam());
		JSONObject erpTeamUsers = erpTeamTotalService.findListByTeam(new Page<ErpTeamTotal>(request, response),
				requestDto, principal.getId());
		return erpTeamUsers;
	}
	
	@RequestMapping(value = "/findTeamAllCount", method = RequestMethod.POST)
	@ResponseBody
	public List<ErpTeamTotal> findTeamAllCount(@RequestBody ErpTeamTotal requestDto, HttpServletRequest request,
			HttpServletResponse response) {
		Principal principal = UserUtils.getPrincipal();
		List<ErpTeamTotal> erpTeamUsers = erpTeamTotalService.findTeamAllCount(requestDto, principal.getId());
		return erpTeamUsers;
	}
	
	@RequestMapping(value = "/findUserCount", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject findUserCount(@RequestBody ErpUserTotal requestDto, HttpServletRequest request,
			HttpServletResponse response) {
		Principal principal = UserUtils.getPrincipal();
		JSONObject erpTeamUsers = erpUserTotalService.findUserCount(new Page<ErpUserTotal>(request, response),
				requestDto, principal.getId());
		return erpTeamUsers;
	}
	
	@RequestMapping(value = "/exportUser")
    @ResponseBody
    public BaseResult reportUser(ErpTeamExp dto,HttpServletResponse response) throws ParseException {
		Principal principal = UserUtils.getPrincipal();
		List<ErpUserTotal> list1= null;
		List<Map<String, Object>> list2= null;
		if(dto.getExpType().indexOf("order")!=-1){
			ErpUserTotal eu=new ErpUserTotal();
			eu.setTeamIds(dto.getTeamIds());
			eu.setStartDateStr(dto.getStartDateStr());
			eu.setEndDateStr(dto.getEndDateStr());
			eu.setUserIds(dto.getUserIds()==null?dto.getServiceUsers():dto.getUserIds());
			eu.setUserId(UserUtils.getUser().getId());
			eu.setFlowUserId(dto.getFlowUserId());
			list1=erpUserTotalService.findUserCount(eu, principal.getId());
		}
		if(dto.getExpType().indexOf("service")!=-1){
			ErpVisitServiceInfo eu=new ErpVisitServiceInfo();
			eu.setTeamIds(dto.getTeamIds());
			eu.setStartDateStr(dto.getStartDateStr());
			eu.setEndDateStr(dto.getEndDateStr());
			eu.setUserId(UserUtils.getUser().getId());
			eu.setServiceUsers(dto.getServiceUsers()==null?dto.getUserIds():dto.getServiceUsers());
			list2= erpVisitServiceInfoDao.queryTeamUserServiceCount(eu,UserUtils.getUser().getAgentId());
		}
		boolean isExport=false;
		if(CollectionUtils.isNotEmpty(list1)||CollectionUtils.isNotEmpty(list2)){
			isExport = exportUser("团队成员绩效统计",new String[] { "订单完成服务度统计", "上门服务数据统计"},list1,list2,response);
		}
		BaseResult br = new BaseResult();
        if (isExport) {
            br.setMessage(CommonConstants.FailMsg.EXPORT);
        } else {
            br.setMessage(CommonConstants.SuccessMsg.EXPORT);
        }
        return br;
	}
	
	@RequestMapping(value = "/overJyk", method = RequestMethod.GET)
    @ResponseBody
    public boolean overJyk(HttpServletRequest request,HttpServletResponse response) {
		new Thread(() -> overJyk()).start();
        return true;
	}
	
	public void overJyk() {
		List<String> list = actDao.overJyk();
        if (list.size() == 0) {
            return;
        }
        list.forEach(ll -> overJyk(ll));
    }
	
	public void overJyk(String flowId) {
		try {
			log.info("-----run Jyk----->"+flowId);
			workFlowMonitorService.endProcess(flowId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("-----over Jyk----->"+e.getMessage());
		}
    }
	
	/**
     * 导出报表
     *
     * @param result
     * @param response
     * @date 2018年5月29日
     * @author linqunzhi
     */
    private boolean exportUser(String tileNames,String[] fileNames, List<ErpUserTotal> list1,
    		List<Map<String, Object>> list2,
			HttpServletResponse response) {
    	HSSFWorkbook workbook = new HSSFWorkbook();
		boolean result=false;
		result=createOrderExl(workbook,fileNames[0],list1);
		result=createServiceExl(workbook,fileNames[1],list2);
		OutputStream fileOutputStream = null;
		try {
			response.reset();// 清空输出流
			response.setHeader("Content-disposition",
					"attachment; filename=" + new String((tileNames + ".xls").getBytes("gbk"), "ISO8859-1"));
			response.setContentType("application/msexcel;charset=utf-8");
			fileOutputStream = response.getOutputStream();
			workbook.write(fileOutputStream);
			result = true;
		} catch (IOException e) {
			log.error("导出报表异常:", e);
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					log.error("关闭流异常：", e);
				}
			}
		}
		return result;
    }
    
    private static boolean createOrderExl(HSSFWorkbook workbook,String filename,List<ErpUserTotal> list){
		boolean result = false;
		// 隐藏列map
		if (null != list && !list.isEmpty()) {
			Sheet sheet = workbook.createSheet(filename);
			Row titleRow1 = sheet.createRow(0);
			// 列下标
			int columnIndex = -1;
			int userNameI = columnIndex + 1;
			++columnIndex;
			String userNameC = "团队成员";
			titleRow1.createCell(userNameI).setCellValue(userNameC);
			
			int flowNameI = columnIndex + 1;
			++columnIndex;
			String flowNameC = "角色";
			titleRow1.createCell(flowNameI).setCellValue(flowNameC);
			
			int teamNameI = columnIndex + 1;
			++columnIndex;
			String teamNameC = "所属团队";
			titleRow1.createCell(teamNameI).setCellValue(teamNameC);
			
			int newCountI = columnIndex + 1;
			++columnIndex;
			String newCountC = "新接入订单数";
			titleRow1.createCell(newCountI).setCellValue(newCountC);
			
			int shouldflowCountI = columnIndex + 1;
			++columnIndex;
			String shouldflowCountC = "应完成服务订单数";
			titleRow1.createCell(shouldflowCountI).setCellValue(shouldflowCountC);
			
			int flowEndCountI = columnIndex + 1;
			++columnIndex;
			String flowEndCountC = "实际完成服务订单数";
			titleRow1.createCell(flowEndCountI).setCellValue(flowEndCountC);
			
			int noCompleteCountI = columnIndex + 1;
			++columnIndex;
			String noCompleteCountC = "延期未完成服务订单数";
			titleRow1.createCell(noCompleteCountI).setCellValue(noCompleteCountC);
			
			int completeCountI = columnIndex + 1;
			++columnIndex;
			String completeCountC = "延期完成服务订单数";
			titleRow1.createCell(completeCountI).setCellValue(completeCountC);
			
			int completeExCountI = columnIndex + 1;
			++columnIndex;
			String completeExCountC = "延期完成服务正常订单数";
			titleRow1.createCell(completeExCountI).setCellValue(completeExCountC);
			
			int percentageI = columnIndex + 1;
			++columnIndex;
			String percentageC = "交付及时率";
			titleRow1.createCell(percentageI).setCellValue(percentageC);
			
			// ##### 多列延期时长 end #############
			int maxColumnIndex = columnIndex;
			
			// 生成数据
			for (int i = 0; i < list.size(); i++) {
				ErpUserTotal test=list.get(i);
				Row r = sheet.createRow(i + 1);
				for (int col = 0; col <= maxColumnIndex; col++) {
					Cell cell = r.createCell(col);
					if (col == userNameI) {
						cell.setCellValue(test.getUserName());
						continue;
					}
					if (col == flowNameI) {
						cell.setCellValue(test.getFlowName());
						continue;
					}
					if (col == teamNameI) {
						cell.setCellValue(test.getTeamName());
						continue;
					}
					if (col == newCountI) {
						cell.setCellValue(test.getNewCount());
						continue;
					}
					if (col == shouldflowCountI) {
						cell.setCellValue(test.getShouldflowCount());
						continue;
					}
					if (col == flowEndCountI) {
						cell.setCellValue(test.getFlowEndCount());
						continue;
					}
					if (col == noCompleteCountI) {
						cell.setCellValue(test.getNoCompleteCount());
						continue;
					}
					if (col == completeCountI) {
						cell.setCellValue(test.getCompleteCount());
						continue;
					}
					if (col == completeExCountI) {
						cell.setCellValue(test.getCompleteExCount());
						continue;
					}
					if (col == percentageI) {
						cell.setCellValue(test.getPercentage());
						continue;
					}
				}
			}
			// 设置列自适应宽度
			for (int i = 0; i <= maxColumnIndex; i++) {
				sheet.autoSizeColumn(i, true);
			}
		}
		return result;
	}
    
    private static boolean createServiceExl(HSSFWorkbook workbook,String filename,List<Map<String, Object>> list){
		boolean result = false;
		// 隐藏列map
		if (null != list && !list.isEmpty()) {
			Sheet sheet = workbook.createSheet(filename);
			Row titleRow1 = sheet.createRow(0);
			Row titleRow2 = sheet.createRow(1);
			// 列下标
			int columnIndex = -1;
			int teamTxtI = columnIndex + 1;
			++columnIndex;
			String teamTxtC = "团队";
			titleRow1.createCell(teamTxtI).setCellValue(teamTxtC);
			titleRow2.createCell(teamTxtI).setCellValue(teamTxtC);
			
			int serviceUserNameI = columnIndex + 1;
			++columnIndex;
			String serviceUserNameC = "运营顾问";
			titleRow1.createCell(serviceUserNameI).setCellValue(serviceUserNameC);
			titleRow2.createCell(serviceUserNameI).setCellValue(serviceUserNameC);
			
			int service_jykjfI = columnIndex + 1;
			++columnIndex;
			String service_jykjfC = "聚引客上门交付服务";
			titleRow1.createCell(service_jykjfI).setCellValue("基础服务");
			titleRow2.createCell(service_jykjfI).setCellValue(service_jykjfC);
			
			int service_scsmchyxI = columnIndex + 1;
			++columnIndex;
			String service_scsmchyxC = "智能客流运营全套落地服务";
			titleRow1.createCell(service_scsmchyxI).setCellValue("基础服务");
			titleRow2.createCell(service_scsmchyxI).setCellValue(service_scsmchyxC);
			
			int service_wlfwI = columnIndex + 1;
			++columnIndex;
			String service_wlfwC = "物料服务";
			titleRow1.createCell(service_wlfwI).setCellValue("基础服务");
			titleRow2.createCell(service_wlfwI).setCellValue(service_wlfwC);
			
			int service_scsmjcI = columnIndex + 1;
			++columnIndex;
			String service_scsmjcC = "首次上门服务（基础版）";
			titleRow1.createCell(service_scsmjcI).setCellValue("基础服务");
			titleRow2.createCell(service_scsmjcI).setCellValue(service_scsmjcC);
			
			int service_zhctjfI = columnIndex + 1;
			++columnIndex;
			String service_zhctjfC = "智慧餐厅安装交付";
			titleRow1.createCell(service_zhctjfI).setCellValue("基础服务");
			titleRow2.createCell(service_zhctjfI).setCellValue(service_zhctjfC);
			
			int service_shsmsfI = columnIndex + 1;
			++columnIndex;
			String service_shsmsfC = "售后上门培训（收费）";
			titleRow1.createCell(service_shsmsfI).setCellValue("售后服务");
			titleRow2.createCell(service_shsmsfI).setCellValue(service_shsmsfC);
			
			int  service_smpxmfI = columnIndex + 1;
			++columnIndex;
			String  service_smpxmfC = "上门培训（免费）";
			titleRow1.createCell(service_smpxmfI).setCellValue("售后服务");
			titleRow2.createCell( service_smpxmfI).setCellValue( service_smpxmfC);
			
			int service_shsmtsmfI = columnIndex + 1;
			++columnIndex;
			String service_shsmtsmfC = "售后上门培训-投诉处理(免费)";
			titleRow1.createCell(service_shsmtsmfI).setCellValue("售后服务");
			titleRow2.createCell(service_shsmtsmfI).setCellValue(service_shsmtsmfC);
			
			int service_wlgxI = columnIndex + 1;
			++columnIndex;
			String service_wlgxC = "物料更新服务";
			titleRow1.createCell(service_wlgxI).setCellValue("增值服务");
			titleRow2.createCell(service_wlgxI).setCellValue(service_wlgxC);
			
			sheet.addMergedRegion((new CellRangeAddress(0, 1, 0, 0)));
			sheet.addMergedRegion((new CellRangeAddress(0, 1, 1, 1)));
			sheet.addMergedRegion((new CellRangeAddress(0, 0, 2, 6)));
			sheet.addMergedRegion((new CellRangeAddress(0, 0, 7, 9)));
			
			// ##### 多列延期时长 end #############
			int maxColumnIndex = columnIndex;
			
			// 生成数据
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> test=list.get(i);
				Row r = sheet.createRow(i + 2);
				for (int col = 0; col <= maxColumnIndex; col++) {
					Cell cell = r.createCell(col);
					if (col == teamTxtI) {
						cell.setCellValue(test.get("teamTxt")==null?"":test.get("teamTxt").toString());
						continue;
					}
					if (col == serviceUserNameI) {
						cell.setCellValue(test.get("serviceUserName")==null?"":test.get("serviceUserName").toString());
						continue;
					}
					if (col == service_jykjfI) {
						cell.setCellValue(test.get("service_jykjf")==null?"0":test.get("service_jykjf").toString());
						continue;
					}
					if (col == service_scsmchyxI) {
						cell.setCellValue(test.get("service_scsmchyx")==null?"0":test.get("service_scsmchyx").toString());
						continue;
					}
					if (col == service_wlfwI) {
						cell.setCellValue(test.get("service_wlfw")==null?"0":test.get("service_wlfw").toString());
						continue;
					}
					if (col == service_scsmjcI) {
						cell.setCellValue(test.get("service_scsmjc")==null?"0":test.get("service_scsmjc").toString());
						continue;
					}
					if (col == service_zhctjfI) {
						cell.setCellValue(test.get("service_zhctjf")==null?"0":test.get("service_zhctjf").toString());
						continue;
					}
					if (col == service_shsmsfI) {
						cell.setCellValue(test.get("service_shsmsf")==null?"0":test.get("service_shsmsf").toString());
						continue;
					}
					if (col == service_smpxmfI) {
						cell.setCellValue(test.get("service_smpxmf")==null?"0":test.get("service_smpxmf").toString());
						continue;
					}
					if (col == service_shsmtsmfI) {
						cell.setCellValue(test.get("service_shsmtsmf")==null?"0":test.get("service_shsmtsmf").toString());
						continue;
					}
					if (col == service_wlgxI) {
						cell.setCellValue(test.get("service_wlgx")==null?"0":test.get("service_wlgx").toString());
						continue;
					}
				}
			}
			// 设置列自适应宽度
			for (int i = 0; i <= maxColumnIndex; i++) {
				sheet.autoSizeColumn(i, true);
			}
		}
		return result;
	}
	
	//, method = RequestMethod.POST      @RequestBody ErpTeamExp dto,
	@RequestMapping(value = "/exportTeam")
    @ResponseBody
    public BaseResult reportTeam(ErpTeamExp dto,HttpServletResponse response) {
		Principal principal = UserUtils.getPrincipal();
		List<ErpTeamTotal> list1= null;
		List<ErpVisitCount> list2= null;
		if(dto.getExpType().indexOf("order")!=-1){
			ErpTeamTotal requestDto =new ErpTeamTotal();
			requestDto.setStartDateStr(dto.getStartDateStr());
			requestDto.setEndDateStr(dto.getEndDateStr());
			requestDto.setTeamIds(dto.getTeamIds());
			list1=erpTeamTotalService.findListByTeam(requestDto, principal.getId());
		}
		if(dto.getExpType().indexOf("service")!=-1){
			ErpVisitCount requestDto =new ErpVisitCount();
			requestDto.setStartDateStr(dto.getStartDateStr());
			requestDto.setEndDateStr(dto.getEndDateStr());
			requestDto.setTeamIds(dto.getTeamIds());
			list2=erpVisitCountService.findTeamServiceCount(requestDto, principal.getId());
		}
		boolean isExport=false;
		if(CollectionUtils.isNotEmpty(list1)||CollectionUtils.isNotEmpty(list2)){
			isExport = export("团队绩效统计",new String[] { "订单完成服务度统计", "上门服务数据统计"},list1,list2,response);
		}
		BaseResult br = new BaseResult();
        if (isExport) {
            br.setMessage(CommonConstants.FailMsg.EXPORT);
        } else {
            br.setMessage(CommonConstants.SuccessMsg.EXPORT);
        }
        return br;
	}
	
	/**
     * 导出报表
     *
     * @param result
     * @param response
     * @date 2018年5月29日
     * @author linqunzhi
     */
    private boolean export(String tileNames,String[] fileNames, List<ErpTeamTotal> list1,
    		List<ErpVisitCount> list2,
			HttpServletResponse response) {
    	HSSFWorkbook workbook = new HSSFWorkbook();
		boolean result=false;
		result=createDeliveryExl(workbook,fileNames[0],list1);
		result=createVCExl(workbook,fileNames[1],list2);
		OutputStream fileOutputStream = null;
		try {
			response.reset();// 清空输出流
			response.setHeader("Content-disposition",
					"attachment; filename=" + new String((tileNames + ".xls").getBytes("gbk"), "ISO8859-1"));
			response.setContentType("application/msexcel;charset=utf-8");
			fileOutputStream = response.getOutputStream();
			workbook.write(fileOutputStream);
			result = true;
		} catch (IOException e) {
			log.error("导出报表异常:", e);
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					log.error("关闭流异常：", e);
				}
			}
		}
		return result;
    }
    private static boolean createVCExl(HSSFWorkbook workbook,String filename,List<ErpVisitCount> list){
		boolean result = false;
		// 隐藏列map
		if (null != list && !list.isEmpty()) {
			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			
			Sheet sheet = workbook.createSheet(filename);
			
			Row titleRow1 = sheet.createRow(0);
			int columnIndex_ = -1;
			int teamNameI_ = columnIndex_ + 1;
			++columnIndex_;
			String teamNameC_ = "团队";
			titleRow1.createCell(teamNameI_).setCellValue(teamNameC_);
			
			Row titleRow2 = sheet.createRow(1);
			// 列下标
			int columnIndex = -1;
			int teamNameI = columnIndex + 1;
			++columnIndex;
			String teamNameC = "团队";
			titleRow2.createCell(teamNameI).setCellValue(teamNameC);
			
			int jykVisitCountI = columnIndex + 1;
			++columnIndex;
			String jykVisitCountC = "聚引客上门交付服务";
			titleRow2.createCell(jykVisitCountI).setCellValue(jykVisitCountC);
			titleRow1.createCell(jykVisitCountI).setCellValue("基础服务");
			
			int firstVisitCountI = columnIndex + 1;
			++columnIndex;
			String firstVisitCountC = "智能客流运营全套落地服务";
			titleRow2.createCell(firstVisitCountI).setCellValue(firstVisitCountC);
			titleRow1.createCell(firstVisitCountI).setCellValue("基础服务");
			
			int materialImplCountI = columnIndex + 1;
			++columnIndex;
			String materialImplCountC = "物料服务";
			titleRow2.createCell(materialImplCountI).setCellValue(materialImplCountC);
			titleRow1.createCell(materialImplCountI).setCellValue("基础服务");
			
			int firstBasicVisitCountI = columnIndex + 1;
			++columnIndex;
			String firstBasicVisitCountC = "首次上门服务（基础版）";
			titleRow2.createCell(firstBasicVisitCountI).setCellValue(firstBasicVisitCountC);
			titleRow1.createCell(firstBasicVisitCountI).setCellValue("基础服务");
			
			int zhctServiceCountI = columnIndex + 1;
			++columnIndex;
			String zhctServiceCountC = "智慧餐厅安装交付";
			titleRow2.createCell(zhctServiceCountI).setCellValue(zhctServiceCountC);
			titleRow1.createCell(zhctServiceCountI).setCellValue("基础服务");
			
			int trainingCountI = columnIndex + 1;
			++columnIndex;
			String trainingCountC = "售后上门培训（收费）";
			titleRow2.createCell(trainingCountI).setCellValue(trainingCountC);
			titleRow1.createCell(trainingCountI).setCellValue("售后服务");
			
			int freeTrainingCountI = columnIndex + 1;
			++columnIndex;
			String freeTrainingCountC = "上门培训（免费）";
			titleRow2.createCell(freeTrainingCountI).setCellValue(freeTrainingCountC);
			titleRow1.createCell(freeTrainingCountI).setCellValue("售后服务");
			
			int comHandCountI = columnIndex + 1;
			++columnIndex;
			String comHandCountC = "售后上门培训-投诉处理(免费)";
			titleRow2.createCell(comHandCountI).setCellValue(comHandCountC);
			titleRow1.createCell(comHandCountI).setCellValue("售后服务");
			
			int materialUpdateCountI = columnIndex + 1;
			++columnIndex;
			String materialUpdateCountC = "物料更新服务";
			titleRow2.createCell(materialUpdateCountI).setCellValue(materialUpdateCountC);
			titleRow1.createCell(materialUpdateCountI).setCellValue("增值服务");
			
			sheet.addMergedRegion((new CellRangeAddress(0, 1, 0, 0)));
			sheet.addMergedRegion((new CellRangeAddress(0, 0, 1, 5)));
			sheet.addMergedRegion((new CellRangeAddress(0, 0, 6, 8)));
			// ##### 多列延期时长 end #############
			int maxColumnIndex = columnIndex;
			
			// 生成数据
			for (int i = 0; i < list.size(); i++) {
				ErpVisitCount test=list.get(i);
				Row r = sheet.createRow(i + 2);
				for (int col = 0; col <= maxColumnIndex; col++) {
					Cell cell = r.createCell(col);
					if (col == teamNameI) {
						cell.setCellValue(test.getTeamName());
						continue;
					}
					if (col == jykVisitCountI) {
						cell.setCellValue(test.getJykVisitCount());
						continue;
					}
					if (col == firstVisitCountI) {
						cell.setCellValue(test.getFirstVisitCount());
						continue;
					}

					if (col == materialImplCountI) {
						cell.setCellValue(test.getMaterialImplCount());
						continue;
					}
					
					if (col == firstBasicVisitCountI) {
						cell.setCellValue(test.getFirstBasicVisitCount());
						continue;
					}
					
					
					if (col == zhctServiceCountI) {
						cell.setCellValue(test.getZhctServiceCount());
						continue;
					}
					if (col == trainingCountI) {
						cell.setCellValue(test.getTrainingCount());
						continue;
					}

					if (col == freeTrainingCountI) {
						cell.setCellValue(test.getFreeTrainingCount());
						continue;
					}
					
					if (col == comHandCountI) {
						cell.setCellValue(test.getComHandCount());
						continue;
					}
					
					if (col == materialUpdateCountI) {
						cell.setCellValue(test.getMaterialUpdateCount());
						continue;
					}
				}
			}
			// 设置列自适应宽度
			for (int i = 0; i <= maxColumnIndex; i++) {
				sheet.autoSizeColumn(i, true);
			}
		}
		return result;
	}
    
    private static boolean createDeliveryExl(HSSFWorkbook workbook,String filename,List<ErpTeamTotal> list){
		boolean result = false;
		// 隐藏列map
		if (null != list && !list.isEmpty()) {
			Sheet sheet = workbook.createSheet(filename);
			Row titleRow1 = sheet.createRow(0);
			// 列下标
			int columnIndex = -1;
			int teamNameI = columnIndex + 1;
			++columnIndex;
			String teamNameC = "团队";
			titleRow1.createCell(teamNameI).setCellValue(teamNameC);
			
			int newCountI = columnIndex + 1;
			++columnIndex;
			String newCountC = "新接入订单";
			titleRow1.createCell(newCountI).setCellValue(newCountC);
			
			int shouldflowCountI = columnIndex + 1;
			++columnIndex;
			String shouldflowCountC = "应完成交付订单数";
			titleRow1.createCell(shouldflowCountI).setCellValue(shouldflowCountC);
			
			int flowEndCountI = columnIndex + 1;
			++columnIndex;
			String flowEndCountC = "实际完成交付订单数";
			titleRow1.createCell(flowEndCountI).setCellValue(flowEndCountC);
			
			int noCompleteCountI = columnIndex + 1;
			++columnIndex;
			String noCompleteCountC = "延期未完成交付订单数";
			titleRow1.createCell(noCompleteCountI).setCellValue(noCompleteCountC);
			
			int completeCountI = columnIndex + 1;
			++columnIndex;
			String completeCountC = "延期完成交付订单数";
			titleRow1.createCell(completeCountI).setCellValue(completeCountC);
			
			int completeExCountI = columnIndex + 1;
			++columnIndex;
			String completeExCounC = "延期完成交付正常订单数";
			titleRow1.createCell(completeExCountI).setCellValue(completeExCounC);
			
			int percentageI = columnIndex + 1;
			++columnIndex;
			String percentageC = "交付及时率";
			titleRow1.createCell(percentageI).setCellValue(percentageC);
			
			// ##### 多列延期时长 end #############
			int maxColumnIndex = columnIndex;
			
			// 生成数据
			for (int i = 0; i < list.size(); i++) {
				ErpTeamTotal test=list.get(i);
				Row r = sheet.createRow(i + 1);
				for (int col = 0; col <= maxColumnIndex; col++) {
					Cell cell = r.createCell(col);
					if (col == teamNameI) {
						cell.setCellValue(test.getTeamName());
						continue;
					}
					if (col == newCountI) {
						cell.setCellValue(test.getNewCount());
						continue;
					}
					if (col == shouldflowCountI) {
						cell.setCellValue(test.getShouldflowCount());
						continue;
					}
					if (col == flowEndCountI) {
						cell.setCellValue(test.getFlowEndCount());
						continue;
					}
					if (col == noCompleteCountI) {
						cell.setCellValue(test.getNoCompleteCount());
						continue;
					}
					if (col == completeCountI) {
						cell.setCellValue(test.getCompleteCount());
						continue;
					}
					if (col == completeExCountI) {
						cell.setCellValue(test.getCompleteExCount());
						continue;
					}

					if (col == percentageI) {
						cell.setCellValue(test.getPercentage());
						continue;
					}
				}
			}
			// 设置列自适应宽度
			for (int i = 0; i <= maxColumnIndex; i++) {
				sheet.autoSizeColumn(i, true);
			}
		}
		return result;
	}
}
