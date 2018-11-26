package com.yunnex.ops.erp.modules.visit.web;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.collections.CollectionUtils;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.statistics.dto.deliveryService.DeliveryServiceDetailRequestDto;
import com.yunnex.ops.erp.modules.statistics.service.DeliveryServiceStatisticsService;
import com.yunnex.ops.erp.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.team.entity.ErpTeamExp;
import com.yunnex.ops.erp.modules.team.entity.ErpUserTotal;
import com.yunnex.ops.erp.modules.team.web.ErpTeamTotalController;
import com.yunnex.ops.erp.modules.visit.entity.ErpServiceCount;
import com.yunnex.ops.erp.modules.visit.entity.ErpServiceExp;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitCount;
import com.yunnex.ops.erp.modules.visit.service.ErpServiceCountService;
import com.yunnex.ops.erp.modules.visit.service.ErpVisitCountService;

/**
 * 上门服务Controller
 * 
 * @author R/Q
 * @version 2018-05-26
 */
@Controller
@RequestMapping(value = "${adminPath}/shopService/erpVisitCount")
public class ErpVisitCountController extends BaseController {
	private Logger log = LoggerFactory.getLogger(ErpVisitCountController.class);
	@Autowired
	private ErpVisitCountService erpVisitCountService;
	@Autowired
	private ErpServiceCountService erpServiceCountService;
	@Autowired
    private DeliveryServiceStatisticsService statisticsService;

	@RequestMapping(value = "/findTeamServiceCount", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject findTeamServiceCount(@RequestBody ErpVisitCount requestDto, HttpServletRequest request,
			HttpServletResponse response) {
		Principal principal = UserUtils.getPrincipal();
		JSONObject erpTeamUsers = erpVisitCountService.findTeamServiceCount(new Page<ErpVisitCount>(request, response),
				requestDto, principal.getId());
		return erpTeamUsers;
	}

	@RequestMapping(value = "/findTeamServiceAllCount", method = RequestMethod.POST)
	@ResponseBody
	public List<ErpVisitCount> findTeamServiceAllCount(@RequestBody ErpVisitCount requestDto,
			HttpServletRequest request, HttpServletResponse response) {
		Principal principal = UserUtils.getPrincipal();
		List<ErpVisitCount> erpTeamUsers = erpVisitCountService.findTeamServiceAllCount(requestDto, principal.getId());
		return erpTeamUsers;
	}

	@RequestMapping(value = "/findServiceSuccessCount", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject findServiceSuccessCount(@RequestBody ErpServiceCount requestDto, HttpServletRequest request,
			HttpServletResponse response) {
		Principal principal = UserUtils.getPrincipal();
		JSONObject erpTeamUsers = erpServiceCountService
				.findServiceSuccessCount(new Page<ErpServiceCount>(request, response), requestDto, principal.getId());
		return erpTeamUsers;
	}

	@RequestMapping(value = "/findServiceServiceAllCount", method = RequestMethod.POST)
	@ResponseBody
	public List<ErpServiceCount> findServiceServiceAllCount(@RequestBody ErpServiceCount requestDto,
			HttpServletRequest request, HttpServletResponse response) {
		Principal principal = UserUtils.getPrincipal();
		List<ErpServiceCount> erpTeamUsers = erpServiceCountService.findServiceServiceAllCount(requestDto,
				principal.getId());
		return erpTeamUsers;
	}

	@RequestMapping(value = "/exportServiceSuccess")
	@ResponseBody
	public BaseResult exportServiceSuccess(ErpServiceExp dto, HttpServletResponse response) throws ParseException {
		Principal principal = UserUtils.getPrincipal();
		List<ErpServiceCount> list1 = null;
		List<DeliveryServiceDetailRequestDto> list2=null;
		if (dto.getExpType().indexOf("order") != -1) {
			ErpServiceCount requestDto = new ErpServiceCount();
			requestDto.setStartDateStr(dto.getStartDateStr());
			requestDto.setEndDateStr(dto.getEndDateStr());
			requestDto.setTeamIds(dto.getTeamIds());
			requestDto.setTeamName(dto.getTeamName());
			requestDto.setTeamType(dto.getTeamType());
			list1 = erpServiceCountService.findServiceSuccessCount(requestDto, principal.getId());
		}
//		DeliveryServiceDetailRequestDto
		if(dto.getExpType().indexOf("service") != -1){
			DeliveryServiceDetailRequestDto requestDto =new DeliveryServiceDetailRequestDto();
			requestDto.setStartDateStr(dto.getStartDateStr());
			requestDto.setEndDateStr(dto.getEndDateStr());
			requestDto.setShopName(dto.getShopName());
			requestDto.setTeamType(dto.getTeamType());
			requestDto.setServiceType(dto.getServiceType());
			requestDto.setTeamIds(dto.getTeamIds());
			requestDto.setUserIds(dto.getUserIds());
			list2=statisticsService.getPageServiceDetail(requestDto);
		}

		boolean isExport = false;
		if (CollectionUtils.isNotEmpty(list1) && dto.getExpType().indexOf("order")!=-1) {
			isExport = exportServiceSuccess("服务完成数统计", new String[] {"服务完成统计"}, list1,list2,response,"order");
		}
		if (CollectionUtils.isNotEmpty(list2) && dto.getExpType().indexOf("service")!=-1) {
			isExport = exportServiceSuccess("服务完成数统计", new String[] {"服务完成数明细统计" }, list1,list2, response,"service");
		}
		BaseResult br = new BaseResult();
		if (isExport) {
			br.setMessage(CommonConstants.SuccessMsg.EXPORT);
		} else {
			br.setMessage(CommonConstants.FailMsg.EXPORT);
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
	private boolean exportServiceSuccess(String tileNames, String[] fileNames, List<ErpServiceCount> list1,
			List<DeliveryServiceDetailRequestDto> list2, HttpServletResponse response,String type) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		boolean result = false;
		if("order".equals(type)){
			result = createServiceSuccess(workbook, fileNames[0], list1);
		}
		if("service".equals(type)){
			result=createServiceDetail(workbook,fileNames[0],list2);
		}
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
	
	private static boolean createServiceDetail(HSSFWorkbook workbook, String filename, List<DeliveryServiceDetailRequestDto> list) {
		boolean result = false;
		// 隐藏列map
		if (null != list && !list.isEmpty()) {
			Sheet sheet = workbook.createSheet(filename);
			Row titleRow1 = sheet.createRow(0);
			// 列下标
			int columnIndex = -1;
			int endTimeI = columnIndex + 1;
			++columnIndex;
			String endTimeC = "服务完成日期";
			titleRow1.createCell(endTimeI).setCellValue(endTimeC);

			int serviceTypeI = columnIndex + 1;
			++columnIndex;
			String serviceTypeC = "服务项目";
			titleRow1.createCell(serviceTypeI).setCellValue(serviceTypeC);

			int shopNameI = columnIndex + 1;
			++columnIndex;
			String shopNameC = "商户全称";
			titleRow1.createCell(shopNameI).setCellValue(shopNameC);

			int teamNameI = columnIndex + 1;
			++columnIndex;
			String teamNameC = "团队";
			titleRow1.createCell(teamNameI).setCellValue(teamNameC);

			int agentTypeI = columnIndex + 1;
			++columnIndex;
			String agentTypeC = "团队类别";
			titleRow1.createCell(agentTypeI).setCellValue(agentTypeC);

			int operationAdviserI = columnIndex + 1;
			++columnIndex;
			String operationAdviserC = "运营顾问";
			titleRow1.createCell(operationAdviserI).setCellValue(operationAdviserC);

			int materialAdviserI = columnIndex + 1;
			++columnIndex;
			String materialAdviserC = "物料顾问";
			titleRow1.createCell(materialAdviserI).setCellValue(materialAdviserC);
			
			int accountAdviserI = columnIndex + 1;
			++columnIndex;
			String accountAdviserC = "开户顾问";
			titleRow1.createCell(accountAdviserI).setCellValue(accountAdviserC);

			// ##### 多列延期时长 end #############
			int maxColumnIndex = columnIndex;

			// 生成数据
			for (int i = 0; i < list.size(); i++) {
				DeliveryServiceDetailRequestDto test = list.get(i);
				Row r = sheet.createRow(i + 1);
				for (int col = 0; col <= maxColumnIndex; col++) {
					Cell cell = r.createCell(col);
					if (col == endTimeI) {
						cell.setCellValue(test.getEndTime());
						continue;
					}
					if (col == serviceTypeI) {
						cell.setCellValue(test.getServiceType());
						continue;
					}
					if (col == shopNameI) {
						cell.setCellValue(test.getShopName());
						continue;
					}
					if (col == teamNameI) {
						cell.setCellValue(test.getTeamName());
						continue;
					}
					if (col == agentTypeI) {
						cell.setCellValue(test.getAgentType().equals("0")?"服务商":"分公司");
						continue;
					}
					if (col == operationAdviserI) {
						cell.setCellValue(test.getOperationAdviser());
						continue;
					}
					if (col == materialAdviserI) {
						cell.setCellValue(test.getMaterialAdviser());
						continue;
					}
					if (col == accountAdviserI) {
						cell.setCellValue(test.getAccountAdviser());
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

	private static boolean createServiceSuccess(HSSFWorkbook workbook, String filename, List<ErpServiceCount> list) {
		boolean result = false;
		// 隐藏列map
		if (null != list && !list.isEmpty()) {
			Sheet sheet = workbook.createSheet(filename);
			Row titleRow1 = sheet.createRow(0);
			Row titleRow2 = sheet.createRow(1);
			// 列下标
			int columnIndex = -1;
			int teamNameI = columnIndex + 1;
			++columnIndex;
			String teamNameC = "团队";
			titleRow1.createCell(teamNameI).setCellValue(teamNameC);
			titleRow2.createCell(teamNameI).setCellValue(teamNameC);

			int teamTypeI = columnIndex + 1;
			++columnIndex;
			String teamTypeC = "团队类别";
			titleRow1.createCell(teamTypeI).setCellValue(teamTypeC);
			titleRow2.createCell(teamTypeI).setCellValue(teamTypeC);
			
			int firstVisitCountI = columnIndex + 1;
			++columnIndex;
			String firstVisitCountC = "首次上门营销策划服务";
			titleRow1.createCell(firstVisitCountI).setCellValue("服务项目完成次数");
			titleRow2.createCell(firstVisitCountI).setCellValue(firstVisitCountC);
			
			int fmpsbasicCountI = columnIndex + 1;
			++columnIndex;
			String fmpsbasicCountC = "首次上门服务（基础）";
			titleRow1.createCell(fmpsbasicCountI).setCellValue("服务项目完成次数");
			titleRow2.createCell(fmpsbasicCountI).setCellValue(fmpsbasicCountC);

			int zhangbeiCountI = columnIndex + 1;
			++columnIndex;
			String zhangbeiCountC = "掌贝平台交付服务";
			titleRow1.createCell(zhangbeiCountI).setCellValue("服务项目完成次数");
			titleRow2.createCell(zhangbeiCountI).setCellValue(zhangbeiCountC);

			int jykCountI = columnIndex + 1;
			++columnIndex;
			String jykCountC = "聚引客上门交付服务";
			titleRow1.createCell(jykCountI).setCellValue("服务项目完成次数");
			titleRow2.createCell(jykCountI).setCellValue(jykCountC);

			int zhctCountI = columnIndex + 1;
			++columnIndex;
			String zhctCountC = "智慧餐厅安装交付";
			titleRow1.createCell(zhctCountI).setCellValue("服务项目完成次数");
			titleRow2.createCell(zhctCountI).setCellValue(zhctCountC);
			
			int updateMatraCountI = columnIndex + 1;
			++columnIndex;
			String updateMatraCountC = "物料更新服务";
			titleRow1.createCell(updateMatraCountI).setCellValue("增值服务次数");
			titleRow2.createCell(updateMatraCountI).setCellValue(updateMatraCountC);

			int afterVisitCountI = columnIndex + 1;
			++columnIndex;
			String afterVisitCountC = "售后上门培训服务";
			titleRow1.createCell(afterVisitCountI).setCellValue("服务项目完成次数");
			titleRow2.createCell(afterVisitCountI).setCellValue(afterVisitCountC);

			sheet.addMergedRegion((new CellRangeAddress(0, 1, 0, 0)));
			sheet.addMergedRegion((new CellRangeAddress(0, 1, 1, 1)));
			sheet.addMergedRegion((new CellRangeAddress(0, 0, 2, 8)));

			// ##### 多列延期时长 end #############
			int maxColumnIndex = columnIndex;

			// 生成数据
			for (int i = 0; i < list.size(); i++) {
				ErpServiceCount test = list.get(i);
				Row r = sheet.createRow(i + 1);
				for (int col = 0; col <= maxColumnIndex; col++) {
					Cell cell = r.createCell(col);
					if (col == teamNameI) {
						cell.setCellValue(test.getTeamName());
						continue;
					}
					if (col == teamTypeI) {
						cell.setCellValue(test.getTeamType());
						continue;
					}
					if (col == firstVisitCountI) {
						cell.setCellValue(test.getFirstVisitCount());
						continue;
					}
					if (col == fmpsbasicCountI) {
						cell.setCellValue(test.getFmpsbasicCount());
						continue;
					}
					if (col == zhangbeiCountI) {
						cell.setCellValue(test.getZhangbeiCount());
						continue;
					}
					if (col == jykCountI) {
						cell.setCellValue(test.getJykCount());
						continue;
					}
					if (col == zhctCountI) {
						cell.setCellValue(test.getZhctCount());
						continue;
					}
					if (col == afterVisitCountI) {
						cell.setCellValue(test.getAfterVisitCount());
						continue;
					}
					if (col == updateMatraCountI) {
						cell.setCellValue(test.getUpdateMatraCount());
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