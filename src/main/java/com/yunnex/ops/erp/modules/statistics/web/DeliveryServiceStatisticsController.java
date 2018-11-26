package com.yunnex.ops.erp.modules.statistics.web;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.statistics.dto.deliveryService.DeliveryServiceDetailRequestDto;
import com.yunnex.ops.erp.modules.statistics.dto.deliveryService.DeliveryServiceStatisticsAllResponseDto;
import com.yunnex.ops.erp.modules.statistics.dto.deliveryService.DeliveryServiceStatisticsDetailDto;
import com.yunnex.ops.erp.modules.statistics.dto.deliveryService.DeliveryServiceStatisticsRequestDto;
import com.yunnex.ops.erp.modules.statistics.dto.deliveryService.DeliveryServiceStatisticsResponseDto;
import com.yunnex.ops.erp.modules.statistics.dto.deliveryService.QueryDataResponseDto;
import com.yunnex.ops.erp.modules.statistics.enums.DeliveryServiceStatisticsColumn;
import com.yunnex.ops.erp.modules.statistics.service.DeliveryServiceStatisticsService;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.team.entity.ErpUserTotal;

/**
 * 生产服务 报表 controller
 * 
 * @author linqunzhi
 * @date 2018年5月28日
 */
@Controller
@RequestMapping(value = "${adminPath}/statistics/deliveryService")
public class DeliveryServiceStatisticsController extends BaseController {

    private Logger log = LoggerFactory.getLogger(DeliveryServiceStatisticsController.class);

    @Autowired
    private DeliveryServiceStatisticsService statisticsService;

    /**
     * 团队绩效跳转
     * 
     */
    @RequiresPermissions("order:deliveryServicePerformanceTeamUrl:view")
    @RequestMapping(value = "teamPerformanceUrl")
    public String teamPerformance(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/statistics/deliveryService/deliveryServiceTeamPerformance";
    }
    
    /**
     * 团队绩效跳转
     * 
     */
    @RequiresPermissions("order:deliveryServicePerformanceUserUrl:view")
    @RequestMapping(value = "userPerformanceUrl")
    public String userPerformance(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/statistics/deliveryService/deliveryServiceUserPerformance";
    }
    
    /**
     * 团队管理跳转
     * 
     */
    @RequiresPermissions("order:deliveryServiceStatisticsTeamUrl:view")
    @RequestMapping(value = "teamStatisticsUrl")
    public String teamStatistics(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/statistics/deliveryService/deliveryServiceTeamStatistics";
    }
    
    /**
     * 团队管理跳转
     * 
     */
    @RequiresPermissions("order:deliveryServiceSuccessTeamUrl:view")
    @RequestMapping(value = "serviceSuccessStatisticsUrl")
    public String serviceSuccessStatisticsUrl(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/statistics/deliveryService/deliveryServiceSuccess";
    }

    /**
     * 个人管理跳转
     * 
     */
    @RequiresPermissions("order:deliveryServiceStatisticsUserUrl:view")
    @RequestMapping(value = "userStatisticsUrl")
    public String personalStatistics(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/statistics/deliveryService/deliveryServiceUserStatistics";
    }

    /**
     * 获取生产服务团队明细列表
     *
     * @param requestDto
     * @param request
     * @param response
     * @return
     * @date 2018年5月29日
     * @author linqunzhi
     */
    @RequestMapping(value = "/findTeamStatistics", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult findTeamStatistics(@RequestBody DeliveryServiceStatisticsRequestDto requestDto, HttpServletRequest request,
                    HttpServletResponse response) {
        Principal principal = UserUtils.getPrincipal();
        DeliveryServiceStatisticsAllResponseDto result = statisticsService
                        .findTeamStatistics(new Page<DeliveryServiceStatisticsRequestDto>(request, response), requestDto, principal.getId());
        return new BaseResult(result);
    }

    /**
     * 获取生产服务个人明细列表
     *
     * @param requestDto
     * @return
     * @date 2018年5月29日
     * @author linqunzhi
     */
    @RequestMapping(value = "findUserStatistics", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult findUserStatistics(@RequestBody DeliveryServiceStatisticsRequestDto requestDto, HttpServletRequest request,
                    HttpServletResponse response) {
        Principal principal = UserUtils.getPrincipal();
        DeliveryServiceStatisticsAllResponseDto result = statisticsService
                        .findUserStatistics(new Page<DeliveryServiceStatisticsRequestDto>(request, response), requestDto, principal.getId());
        return new BaseResult(result);
    }
    
    /**
     * 获取服务完成列表明细
     *
     * @param requestDto
     * @return
     * @date 2018年5月29日
     * @author linqunzhi
     */
    @RequestMapping(value = "findServiceDetail", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject findServiceDetail(@RequestBody DeliveryServiceDetailRequestDto requestDto, HttpServletRequest request,
                    HttpServletResponse response) {
        Principal principal = UserUtils.getPrincipal();
        JSONObject result = statisticsService
                        .findServiceDetail(new Page<DeliveryServiceDetailRequestDto>(request, response), requestDto, principal.getId());
        return result;
    }
    
    /**
     * 根据多个团队id 获取团队人员信息
     *
     * @param teamIdList
     * @param leaderFlag
     * @return
     * @date 2018年5月31日
     * @author linqunzhi
     */
    @ResponseBody
    @RequestMapping(value = "findFlow", method = RequestMethod.POST)
    public List<Map> findFlow(@RequestBody ErpUserTotal erpUserTotal, HttpServletRequest request,
			HttpServletResponse response) {
        List<Map> list = statisticsService.findFlow(erpUserTotal);
        return list;
    }

    /**
     * 导出团队列表
     *
     * @param requestDto
     * @return
     * @date 2018年5月29日
     * @author linqunzhi
     */
    @RequestMapping(value = "exportTeam", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult reportTeam(@RequestParam String jsonObject, HttpServletResponse response) {
        // 转换数据
        DeliveryServiceStatisticsRequestDto dto = JSON.parseObject(StringEscapeUtils.unescapeHtml4(jsonObject),
                        DeliveryServiceStatisticsRequestDto.class);
        Principal principal = UserUtils.getPrincipal();
        List<String> serviceType1=new ArrayList();
        serviceType1.add("JYK");
        serviceType1.add("FMPS");
        serviceType1.add("FMPS_BASIC");
        serviceType1.add("INTO_PIECES");
        dto.setServiceTypes(serviceType1);
        DeliveryServiceStatisticsAllResponseDto result1=new DeliveryServiceStatisticsAllResponseDto();
        if(dto.getExpType().indexOf("first")!=-1){
        	result1 = statisticsService.findTeamStatistics(null, dto, principal.getId());
        }else{
        	result1.setList(null);
        }
        List<String> serviceType2=new ArrayList();
        serviceType2.add("VC");
        dto.setServiceTypes(serviceType2);
        DeliveryServiceStatisticsAllResponseDto result2 = new DeliveryServiceStatisticsAllResponseDto();
        if(dto.getExpType().indexOf("vc")!=-1){
        	dto.setServiceCodes(null);
        	result2 = statisticsService.findTeamStatistics(null, dto, principal.getId());
        }else{
        	result2.setList(null);
        }
        List<String> serviceType3=new ArrayList();
        serviceType3.add("MU");
        dto.setServiceTypes(serviceType3);
        DeliveryServiceStatisticsAllResponseDto result3 = new DeliveryServiceStatisticsAllResponseDto();
        if(dto.getExpType().indexOf("mu")!=-1){
        	dto.setServiceCodes(null);
        	result3 = statisticsService.findTeamStatistics(null, dto, principal.getId());
        }else{
        	result3.setList(null);
        }
        boolean isExport = export("团队订单管理",new String[] { "上门交付服务", "售后上门培训", "物料更新服务" }, result1, result2, result3,
				response);
        BaseResult br = new BaseResult();
        if (isExport) {
            br.setMessage(CommonConstants.FailMsg.EXPORT);
        } else {
            br.setMessage(CommonConstants.SuccessMsg.EXPORT);
        }
        return br;
    }

    /**
     * 导出个人列表
     *
     * @param jsonObject
     * @param response
     * @return
     * @date 2018年5月30日
     * @author linqunzhi
     */
    @RequestMapping(value = "exportUser", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult reportUser(@RequestParam String jsonObject, HttpServletResponse response) {
        // 转换数据
        DeliveryServiceStatisticsRequestDto dto = JSON.parseObject(StringEscapeUtils.unescapeHtml4(jsonObject),
                        DeliveryServiceStatisticsRequestDto.class);
        Principal principal = UserUtils.getPrincipal();
        List<String> serviceType1=new ArrayList();
        serviceType1.add("JYK");
        serviceType1.add("FMPS");
        serviceType1.add("FMPS_BASIC");
        serviceType1.add("INTO_PIECES");
        dto.setServiceTypes(serviceType1);
        DeliveryServiceStatisticsAllResponseDto result1 = new DeliveryServiceStatisticsAllResponseDto();
        if(dto.getExpType().indexOf("first")!=-1){
        	result1 = statisticsService.findUserStatistics(null, dto, principal.getId());
        }else{
        	result1.setList(null);
        }
        List<String> serviceType2=new ArrayList();
        serviceType2.add("VC");
        dto.setServiceTypes(serviceType2);
        DeliveryServiceStatisticsAllResponseDto result2 = new DeliveryServiceStatisticsAllResponseDto();
        if(dto.getExpType().indexOf("vc")!=-1){
        	dto.setServiceCodes(null);
        	result2 = statisticsService.findUserStatistics(null, dto, principal.getId());
        }else{
        	result2.setList(null);
        }
        List<String> serviceType3=new ArrayList();
        serviceType3.add("MU");
        dto.setServiceTypes(serviceType3);
        DeliveryServiceStatisticsAllResponseDto result3 = new DeliveryServiceStatisticsAllResponseDto();
        if(dto.getExpType().indexOf("mu")!=-1){
        	dto.setServiceCodes(null);
        	result3 = statisticsService.findUserStatistics(null, dto, principal.getId());
        }else{
        	result3.setList(null);
        }
        boolean isExport = export("个人订单管理",new String[] { "首次上门营销策划服务", "售后上门培训", "物料更新服务" }, result1, result2, result3,
				response);
        BaseResult br = new BaseResult();
        if (isExport) {
            br.setMessage(CommonConstants.FailMsg.EXPORT);
        } else {
            br.setMessage(CommonConstants.SuccessMsg.EXPORT);
        }
        return br;
    }
    
    private static boolean createMUExl(HSSFWorkbook workbook,String filename,DeliveryServiceStatisticsAllResponseDto dto){
		boolean result = false;
		List<DeliveryServiceStatisticsResponseDto> list = dto.getList();
		// 隐藏列map
		Map<String, String> notShowColumnMap = getNotShowColumnMap(dto.getNotShowColumnList());
		if (null != list && !list.isEmpty()) {
			Sheet sheet = workbook.createSheet(filename);
			Row titleRow1 = sheet.createRow(0);
			Row titleRow2 = sheet.createRow(1);
			// 列下标
			int columnIndex = -1;
			// 订单号
			int orderNumberI = columnIndex + 1;
			++columnIndex;
			String orderNumberC = "订单号";
			titleRow1.createCell(orderNumberI).setCellValue(orderNumberC);
			// 购买时间
			int buyDateI = columnIndex + 1;
			++columnIndex;
			String buyDateC = "购买时间";
			titleRow1.createCell(buyDateI).setCellValue(buyDateC);
			// 商户简称
			int shopNameI = columnIndex + 1;
			++columnIndex;
			String shopNameC = "商户简称";
			titleRow1.createCell(shopNameI).setCellValue(shopNameC);
			// 已购服务类型
//			int serviceTypeNamesI = columnIndex + 1;
//			++columnIndex;
//			String serviceTypeNamesC = "已购服务类型";
//			titleRow1.createCell(serviceTypeNamesI).setCellValue(serviceTypeNamesC);
			// 所属服务商
			int agentNameI = -1;
			String agentNameC = null;
			if (notShowColumnMap.get(DeliveryServiceStatisticsColumn.AGENT_NAME.getCode()) == null) {
				agentNameI = columnIndex + 1;
				++columnIndex;
				agentNameC = DeliveryServiceStatisticsColumn.AGENT_NAME.getName();
				titleRow1.createCell(agentNameI).setCellValue(agentNameC);
			}
			// 运营顾问
			int operationAdviserNameI = columnIndex + 1;
			++columnIndex;
			String operationAdviserNameC = "运营顾问";
			titleRow1.createCell(operationAdviserNameI).setCellValue(operationAdviserNameC);
			// 启动时间
			int startTimeI = columnIndex + 1;
			++columnIndex;
			String startTimeC = "启动时间";
			titleRow1.createCell(startTimeI).setCellValue(startTimeC);
			// 应完成交付时间
			int shouldFlowEndTimeI = columnIndex + 1;
			++columnIndex;
			String shouldFlowEndTimeC = "应完成交付时间";
			titleRow1.createCell(shouldFlowEndTimeI).setCellValue(shouldFlowEndTimeC);
			// 是否已完成交付
			int flowEndFlagI = columnIndex + 1;
			++columnIndex;
			String flowEndFlagC = "是否已完成交付";
			titleRow1.createCell(flowEndFlagI).setCellValue(flowEndFlagC);
			// 实际完成交付时间
			int flowEndTimeI = columnIndex + 1;
			++columnIndex;
			String flowEndTimeC = "实际完成交付时间";
			titleRow1.createCell(flowEndTimeI).setCellValue(flowEndTimeC);
			// 是否延期交付
			int delayFlagI = columnIndex + 1;
			++columnIndex;
			String delayFlagC = "是否延期交付";
			titleRow1.createCell(delayFlagI).setCellValue(delayFlagC);
			// ##### 多列延期合并处理 start #############
			// 多列延期最小列下标
			int multipleDelayMinI = -1;
			// 多列延期最大列下标
			int multipleDelayMaxI = -1;
			// 开户顾问延期
//			int openDelayFlagI = -1;
//			String openDelayFlagC = null;
//			if (notShowColumnMap.get(DeliveryServiceStatisticsColumn.OPEN_DELAY_FLAG.getCode()) == null) {
//				openDelayFlagI = columnIndex + 1;
//				++columnIndex;
//				openDelayFlagC = "开户顾问";
//				if (multipleDelayMinI != -1) {
//					multipleDelayMinI = openDelayFlagI < multipleDelayMinI ? openDelayFlagI : multipleDelayMinI;
//				} else {
//					multipleDelayMinI = openDelayFlagI;
//				}
//				multipleDelayMaxI = openDelayFlagI > multipleDelayMaxI ? openDelayFlagI : multipleDelayMaxI;
//				titleRow2.createCell(openDelayFlagI).setCellValue(openDelayFlagC);
//			}
			// 物料顾问是否延迟
			int materielDelayFlagI = -1;
			String materielDelayFlagC = null;
			if (notShowColumnMap.get(DeliveryServiceStatisticsColumn.MATERIEL_DELAY_FLAG.getCode()) == null) {
				materielDelayFlagI = columnIndex + 1;
				++columnIndex;
				materielDelayFlagC = "物料顾问";
				if (multipleDelayMinI != -1) {
					multipleDelayMinI = materielDelayFlagI < multipleDelayMinI ? materielDelayFlagI : multipleDelayMinI;
				} else {
					multipleDelayMinI = materielDelayFlagI;
				}
				multipleDelayMaxI = materielDelayFlagI > multipleDelayMaxI ? materielDelayFlagI : multipleDelayMaxI;
				titleRow2.createCell(materielDelayFlagI).setCellValue(materielDelayFlagC);
			}
			// 运营顾问是否延迟
			int operationDelayFlagI = -1;
			String operationDelayFlagC = null;
			if (notShowColumnMap.get(DeliveryServiceStatisticsColumn.OPERATION_DELAY_FLAG.getCode()) == null) {
				operationDelayFlagI = columnIndex + 1;
				++columnIndex;
				operationDelayFlagC = "运营顾问";
				if (multipleDelayMinI != -1) {
					multipleDelayMinI = operationDelayFlagI < multipleDelayMinI ? operationDelayFlagI
							: multipleDelayMinI;
				} else {
					multipleDelayMinI = operationDelayFlagI;
				}
				multipleDelayMaxI = operationDelayFlagI > multipleDelayMaxI ? operationDelayFlagI : multipleDelayMaxI;
				titleRow2.createCell(operationDelayFlagI).setCellValue(operationDelayFlagC);
			}
			if (multipleDelayMinI != -1) {
				titleRow1.createCell(multipleDelayMinI).setCellValue("是否延迟完成服务");
			}
			// ##### 多列延期合并 end #############

			// ##### 多列延期时长合并处理 start #############
			// 多列延期时长最小列下标
			int multipleDelayDurationMinI = -1;
			// 多列延期时长最大列下标
			int multipleDelayDurationMaxI = -1;
			// 开户顾问延期时长
//			int openDelayDurationI = -1;
//			String openDelayDurationC = null;
//			if (notShowColumnMap.get(DeliveryServiceStatisticsColumn.OPEN_DELAY_DURATION.getCode()) == null) {
//				openDelayDurationI = columnIndex + 1;
//				++columnIndex;
//				openDelayDurationC = "开户顾问";
//				if (multipleDelayDurationMinI != -1) {
//					multipleDelayDurationMinI = openDelayDurationI < multipleDelayDurationMinI ? openDelayDurationI
//							: multipleDelayDurationMinI;
//				} else {
//					multipleDelayDurationMinI = openDelayDurationI;
//				}
//
//				multipleDelayDurationMaxI = openDelayDurationI > multipleDelayDurationMaxI ? openDelayDurationI
//						: multipleDelayDurationMaxI;
//				titleRow2.createCell(openDelayDurationI).setCellValue(openDelayDurationC);
//			}
			// 物料顾问延迟时长
			int materielDelayDurationI = -1;
			String materielDelayDurationC = null;
			if (notShowColumnMap.get(DeliveryServiceStatisticsColumn.MATERIEL_DELAY_DURATION.getCode()) == null) {
				materielDelayDurationI = columnIndex + 1;
				++columnIndex;
				materielDelayDurationC = "物料顾问";
				if (multipleDelayDurationMinI != -1) {
					multipleDelayDurationMinI = materielDelayDurationI < multipleDelayDurationMinI
							? materielDelayDurationI : multipleDelayDurationMinI;
				} else {
					multipleDelayDurationMinI = materielDelayDurationI;
				}
				multipleDelayDurationMaxI = materielDelayDurationI > multipleDelayDurationMaxI ? materielDelayDurationI
						: multipleDelayDurationMaxI;
				titleRow2.createCell(materielDelayDurationI).setCellValue(materielDelayDurationC);
			}
			// 运营顾问延迟时长
			int operationDelayDurationI = -1;
			String operationDelayDurationC = null;
			if (notShowColumnMap.get(DeliveryServiceStatisticsColumn.OPERATION_DELAY_DURATION.getCode()) == null) {
				operationDelayDurationI = columnIndex + 1;
				++columnIndex;
				operationDelayDurationC = "运营顾问";
				if (multipleDelayDurationMinI != -1) {
					multipleDelayDurationMinI = operationDelayDurationI < multipleDelayDurationMinI
							? operationDelayDurationI : multipleDelayDurationMinI;
				} else {
					multipleDelayDurationMinI = operationDelayDurationI;
				}
				multipleDelayDurationMaxI = operationDelayDurationI > multipleDelayDurationMaxI
						? operationDelayDurationI : multipleDelayDurationMaxI;
				titleRow2.createCell(operationDelayDurationI).setCellValue(operationDelayDurationC);
			}
			if (multipleDelayDurationMinI != -1) {
				titleRow1.createCell(multipleDelayDurationMinI).setCellValue("延迟完成服务时长");
			}
			int excptionLogoI = columnIndex + 1;
			++columnIndex;
			String excptionLogoC = "服务异常原因";
			titleRow1.createCell(excptionLogoI).setCellValue(excptionLogoC);
			// ##### 多列延期时长 end #############
			int maxColumnIndex = columnIndex;
			// 生成合并
			for (int i = 0; i <= maxColumnIndex; i++) {
				if (i == multipleDelayMinI) {
					sheet.addMergedRegion((new CellRangeAddress(0, 0, multipleDelayMinI, multipleDelayMaxI)));
					i = multipleDelayMaxI;
				} else if (i == multipleDelayDurationMinI) {
					sheet.addMergedRegion(
							(new CellRangeAddress(0, 0, multipleDelayDurationMinI, multipleDelayDurationMaxI)));
					i = multipleDelayDurationMaxI;
				} else {
					sheet.addMergedRegion((new CellRangeAddress(0, 1, i, i)));
				}
			}
			// 生成数据
			for (int i = 0; i < list.size(); i++) {
				Row r = sheet.createRow(i + 2);
				DeliveryServiceStatisticsResponseDto test = list.get(i);
				for (int col = 0; col <= maxColumnIndex; col++) {
					Cell cell = r.createCell(col);
					if (col == orderNumberI) {
						cell.setCellValue(test.getOrderNum());
						continue;
					}
					if (col == buyDateI) {
						cell.setCellValue(test.getBuyDate());
						continue;
					}
					if (col == shopNameI) {
						cell.setCellValue(test.getShopName());
						continue;
					}
//					if (col == serviceTypeNamesI) {
//						cell.setCellValue(test.getServiceTypeNames());
//						continue;
//					}
					if (col == agentNameI) {
						cell.setCellValue(test.getAgentName());
						continue;
					}
					if (col == operationAdviserNameI) {
						cell.setCellValue(test.getOperationAdviserName());
						continue;
					}
					if (col == startTimeI) {
						cell.setCellValue(test.getStartTime());
						continue;
					}
					if (col == shouldFlowEndTimeI) {
						cell.setCellValue(test.getShouldFlowEndTime());
						continue;
					}

					if (col == flowEndFlagI) {
						cell.setCellValue(test.getFlowEndFlag());
						continue;
					}
					if (col == flowEndTimeI) {
						cell.setCellValue(test.getFlowEndTime());
						continue;
					}
					if (col == delayFlagI) {
						cell.setCellValue(test.getDelayFlag());
						continue;
					}
//					if (col == openDelayFlagI) {
//						cell.setCellValue(test.getOpenDelayFlag());
//						continue;
//					}
					if (col == materielDelayFlagI) {
						cell.setCellValue(test.getMaterielDelayFlag());
						continue;
					}
					if (col == operationDelayFlagI) {
						cell.setCellValue(test.getOperationDelayFlag());
						continue;
					}
//					if (col == openDelayDurationI) {
//						cell.setCellValue(test.getOpenDelayDuration());
//						continue;
//					}
					if (col == materielDelayDurationI) {
						cell.setCellValue(test.getMaterielDelayDuration());
						continue;
					}
					if (col == operationDelayDurationI) {
						cell.setCellValue(test.getOperationDelayDuration());
						continue;
					}
					if (col == excptionLogoI) {
						cell.setCellValue(test.getExcptionLogo());
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
    
    private static boolean createVCExl(HSSFWorkbook workbook,String filename,DeliveryServiceStatisticsAllResponseDto dto){
		boolean result = false;
		List<DeliveryServiceStatisticsResponseDto> list = dto.getList();
		// 隐藏列map
		Map<String, String> notShowColumnMap = getNotShowColumnMap(dto.getNotShowColumnList());
		if (null != list && !list.isEmpty()) {
			Sheet sheet = workbook.createSheet(filename);
			Row titleRow1 = sheet.createRow(0);
			Row titleRow2 = sheet.createRow(1);
			// 列下标
			int columnIndex = -1;
			// 订单号
			int orderNumberI = columnIndex + 1;
			++columnIndex;
			String orderNumberC = "订单号";
			titleRow1.createCell(orderNumberI).setCellValue(orderNumberC);
			// 购买时间
			int buyDateI = columnIndex + 1;
			++columnIndex;
			String buyDateC = "购买时间";
			titleRow1.createCell(buyDateI).setCellValue(buyDateC);
			// 商户简称
			int shopNameI = columnIndex + 1;
			++columnIndex;
			String shopNameC = "商户简称";
			titleRow1.createCell(shopNameI).setCellValue(shopNameC);
			// 所属服务商
			int agentNameI = -1;
			String agentNameC = null;
			if (notShowColumnMap.get(DeliveryServiceStatisticsColumn.AGENT_NAME.getCode()) == null) {
				agentNameI = columnIndex + 1;
				++columnIndex;
				agentNameC = DeliveryServiceStatisticsColumn.AGENT_NAME.getName();
				titleRow1.createCell(agentNameI).setCellValue(agentNameC);
			}
			// 运营顾问
			int operationAdviserNameI = columnIndex + 1;
			++columnIndex;
			String operationAdviserNameC = "运营顾问";
			titleRow1.createCell(operationAdviserNameI).setCellValue(operationAdviserNameC);
			// 启动时间
			int startTimeI = columnIndex + 1;
			++columnIndex;
			String startTimeC = "启动时间";
			titleRow1.createCell(startTimeI).setCellValue(startTimeC);
			// 应完成交付时间
			int shouldFlowEndTimeI = columnIndex + 1;
			++columnIndex;
			String shouldFlowEndTimeC = "应完成交付时间";
			titleRow1.createCell(shouldFlowEndTimeI).setCellValue(shouldFlowEndTimeC);
			// 是否已完成交付
			int flowEndFlagI = columnIndex + 1;
			++columnIndex;
			String flowEndFlagC = "是否已完成交付";
			titleRow1.createCell(flowEndFlagI).setCellValue(flowEndFlagC);
			// 实际完成交付时间
			int flowEndTimeI = columnIndex + 1;
			++columnIndex;
			String flowEndTimeC = "实际完成交付时间";
			titleRow1.createCell(flowEndTimeI).setCellValue(flowEndTimeC);
			// 是否延期交付
			int delayFlagI = columnIndex + 1;
			++columnIndex;
			String delayFlagC = "是否延期交付";
			titleRow1.createCell(delayFlagI).setCellValue(delayFlagC);
			int isDelayFlagI = columnIndex + 1;
			++columnIndex;
			String isDelayFlagC = "是否延迟完成服务";
			titleRow1.createCell(isDelayFlagI).setCellValue(isDelayFlagC);
			
			int delayDurationI = columnIndex + 1;
			++columnIndex;
			String delayDurationC = "延迟完成服务时长";
			titleRow1.createCell(delayDurationI).setCellValue(delayDurationC);
			// 服务异常原因
			int excptionLogoI = columnIndex + 1;
			++columnIndex;
			String excptionLogoC = "服务异常原因";
			titleRow1.createCell(excptionLogoI).setCellValue(excptionLogoC);
			// 生成数据
			for (int i = 0; i < list.size(); i++) {
				Row r = sheet.createRow(i + 1);
				DeliveryServiceStatisticsResponseDto test = list.get(i);
				for (int col = 0; col <= columnIndex; col++) {
					Cell cell = r.createCell(col);
					if (col == orderNumberI) {
						cell.setCellValue(test.getOrderNum());
						continue;
					}
					if (col == buyDateI) {
						cell.setCellValue(test.getBuyDate());
						continue;
					}
					if (col == shopNameI) {
						cell.setCellValue(test.getShopName());
						continue;
					}
					if (col == agentNameI) {
						cell.setCellValue(test.getAgentName());
						continue;
					}
					if (col == operationAdviserNameI) {
						cell.setCellValue(test.getOperationAdviserName());
						continue;
					}
					if (col == startTimeI) {
						cell.setCellValue(test.getStartTime());
						continue;
					}
					if (col == shouldFlowEndTimeI) {
						cell.setCellValue(test.getShouldFlowEndTime());
						continue;
					}

					if (col == flowEndFlagI) {
						cell.setCellValue(test.getFlowEndFlag());
						continue;
					}
					if (col == flowEndTimeI) {
						cell.setCellValue(test.getFlowEndTime());
						continue;
					}
					if (col == delayFlagI) {
						cell.setCellValue(test.getDelayFlag());
						continue;
					}
					if (col == isDelayFlagI) {
						cell.setCellValue(test.getDelayFlag());
						continue;
					}
					if (col == delayDurationI) {
						cell.setCellValue(test.getDelayDuration());
						continue;
					}
					if (col == excptionLogoI) {
						cell.setCellValue(test.getExcptionLogo());
						continue;
					}
				}
			}
			// 设置列自适应宽度
			for (int i = 0; i <= columnIndex; i++) {
				sheet.autoSizeColumn(i, true);
			}
		}
		return result;
	}
    
    private static boolean createDeliveryExl(HSSFWorkbook workbook,String filename,DeliveryServiceStatisticsAllResponseDto dto){
		boolean result = false;
		List<DeliveryServiceStatisticsResponseDto> list = dto.getList();
		// 隐藏列map
		Map<String, String> notShowColumnMap = getNotShowColumnMap(dto.getNotShowColumnList());
		if (null != list && !list.isEmpty()) {
			Sheet sheet = workbook.createSheet(filename);
			Row titleRow1 = sheet.createRow(0);
			Row titleRow2 = sheet.createRow(1);
			// 列下标
			int columnIndex = -1;
			// 订单号
			int orderNumberI = columnIndex + 1;
			++columnIndex;
			String orderNumberC = "订单号";
			titleRow1.createCell(orderNumberI).setCellValue(orderNumberC);
			// 购买时间
			int buyDateI = columnIndex + 1;
			++columnIndex;
			String buyDateC = "购买时间";
			titleRow1.createCell(buyDateI).setCellValue(buyDateC);
			// 商户简称
			int shopNameI = columnIndex + 1;
			++columnIndex;
			String shopNameC = "商户简称";
			titleRow1.createCell(shopNameI).setCellValue(shopNameC);
			// 已购服务类型
			int serviceTypeNamesI = columnIndex + 1;
			++columnIndex;
			String serviceTypeNamesC = "已购服务类型";
			titleRow1.createCell(serviceTypeNamesI).setCellValue(serviceTypeNamesC);
			
			// 已购服务类型
			int serviceTypeI = columnIndex + 1;
			++columnIndex;
			String serviceTypeC = "服务项目";
			titleRow1.createCell(serviceTypeI).setCellValue(serviceTypeC);
			
			// 所属服务商
			int agentNameI = -1;
			String agentNameC = null;
			if (notShowColumnMap.get(DeliveryServiceStatisticsColumn.AGENT_NAME.getCode()) == null) {
				agentNameI = columnIndex + 1;
				++columnIndex;
				agentNameC = DeliveryServiceStatisticsColumn.AGENT_NAME.getName();
				titleRow1.createCell(agentNameI).setCellValue(agentNameC);
			}
			// 运营顾问
			int operationAdviserNameI = columnIndex + 1;
			++columnIndex;
			String operationAdviserNameC = "运营顾问";
			titleRow1.createCell(operationAdviserNameI).setCellValue(operationAdviserNameC);
			// 启动时间
			int startTimeI = columnIndex + 1;
			++columnIndex;
			String startTimeC = "启动时间";
			titleRow1.createCell(startTimeI).setCellValue(startTimeC);
			// 应完成交付时间
			int shouldFlowEndTimeI = columnIndex + 1;
			++columnIndex;
			String shouldFlowEndTimeC = "应完成交付时间";
			titleRow1.createCell(shouldFlowEndTimeI).setCellValue(shouldFlowEndTimeC);
			// 是否已完成交付
			int flowEndFlagI = columnIndex + 1;
			++columnIndex;
			String flowEndFlagC = "是否已完成交付";
			titleRow1.createCell(flowEndFlagI).setCellValue(flowEndFlagC);
			// 实际完成交付时间
			int flowEndTimeI = columnIndex + 1;
			++columnIndex;
			String flowEndTimeC = "实际完成交付时间";
			titleRow1.createCell(flowEndTimeI).setCellValue(flowEndTimeC);
			// 是否延期交付
			int delayFlagI = columnIndex + 1;
			++columnIndex;
			String delayFlagC = "是否延期交付";
			titleRow1.createCell(delayFlagI).setCellValue(delayFlagC);
			// ##### 多列延期合并处理 start #############
			// 多列延期最小列下标
			int multipleDelayMinI = -1;
			// 多列延期最大列下标
			int multipleDelayMaxI = -1;
			// 开户顾问延期
			int openDelayFlagI = -1;
			String openDelayFlagC = null;
			if (notShowColumnMap.get(DeliveryServiceStatisticsColumn.OPEN_DELAY_FLAG.getCode()) == null) {
				openDelayFlagI = columnIndex + 1;
				++columnIndex;
				openDelayFlagC = "开户顾问";
				if (multipleDelayMinI != -1) {
					multipleDelayMinI = openDelayFlagI < multipleDelayMinI ? openDelayFlagI : multipleDelayMinI;
				} else {
					multipleDelayMinI = openDelayFlagI;
				}
				multipleDelayMaxI = openDelayFlagI > multipleDelayMaxI ? openDelayFlagI : multipleDelayMaxI;
				titleRow2.createCell(openDelayFlagI).setCellValue(openDelayFlagC);
			}
			// 物料顾问是否延迟
			int materielDelayFlagI = -1;
			String materielDelayFlagC = null;
			if (notShowColumnMap.get(DeliveryServiceStatisticsColumn.MATERIEL_DELAY_FLAG.getCode()) == null) {
				materielDelayFlagI = columnIndex + 1;
				++columnIndex;
				materielDelayFlagC = "物料顾问";
				if (multipleDelayMinI != -1) {
					multipleDelayMinI = materielDelayFlagI < multipleDelayMinI ? materielDelayFlagI : multipleDelayMinI;
				} else {
					multipleDelayMinI = materielDelayFlagI;
				}
				multipleDelayMaxI = materielDelayFlagI > multipleDelayMaxI ? materielDelayFlagI : multipleDelayMaxI;
				titleRow2.createCell(materielDelayFlagI).setCellValue(materielDelayFlagC);
			}
			// 运营顾问是否延迟
			int operationDelayFlagI = -1;
			String operationDelayFlagC = null;
			if (notShowColumnMap.get(DeliveryServiceStatisticsColumn.OPERATION_DELAY_FLAG.getCode()) == null) {
				operationDelayFlagI = columnIndex + 1;
				++columnIndex;
				operationDelayFlagC = "运营顾问";
				if (multipleDelayMinI != -1) {
					multipleDelayMinI = operationDelayFlagI < multipleDelayMinI ? operationDelayFlagI
							: multipleDelayMinI;
				} else {
					multipleDelayMinI = operationDelayFlagI;
				}
				multipleDelayMaxI = operationDelayFlagI > multipleDelayMaxI ? operationDelayFlagI : multipleDelayMaxI;
				titleRow2.createCell(operationDelayFlagI).setCellValue(operationDelayFlagC);
			}
			if (multipleDelayMinI != -1) {
				titleRow1.createCell(multipleDelayMinI).setCellValue("是否延迟完成服务");
			}
			// ##### 多列延期合并 end #############

			// ##### 多列延期时长合并处理 start #############
			// 多列延期时长最小列下标
			int multipleDelayDurationMinI = -1;
			// 多列延期时长最大列下标
			int multipleDelayDurationMaxI = -1;
			// 开户顾问延期时长
			int openDelayDurationI = -1;
			String openDelayDurationC = null;
			if (notShowColumnMap.get(DeliveryServiceStatisticsColumn.OPEN_DELAY_DURATION.getCode()) == null) {
				openDelayDurationI = columnIndex + 1;
				++columnIndex;
				openDelayDurationC = "开户顾问";
				if (multipleDelayDurationMinI != -1) {
					multipleDelayDurationMinI = openDelayDurationI < multipleDelayDurationMinI ? openDelayDurationI
							: multipleDelayDurationMinI;
				} else {
					multipleDelayDurationMinI = openDelayDurationI;
				}

				multipleDelayDurationMaxI = openDelayDurationI > multipleDelayDurationMaxI ? openDelayDurationI
						: multipleDelayDurationMaxI;
				titleRow2.createCell(openDelayDurationI).setCellValue(openDelayDurationC);
			}
			// 物料顾问延迟时长
			int materielDelayDurationI = -1;
			String materielDelayDurationC = null;
			if (notShowColumnMap.get(DeliveryServiceStatisticsColumn.MATERIEL_DELAY_DURATION.getCode()) == null) {
				materielDelayDurationI = columnIndex + 1;
				++columnIndex;
				materielDelayDurationC = "物料顾问";
				if (multipleDelayDurationMinI != -1) {
					multipleDelayDurationMinI = materielDelayDurationI < multipleDelayDurationMinI
							? materielDelayDurationI : multipleDelayDurationMinI;
				} else {
					multipleDelayDurationMinI = materielDelayDurationI;
				}
				multipleDelayDurationMaxI = materielDelayDurationI > multipleDelayDurationMaxI ? materielDelayDurationI
						: multipleDelayDurationMaxI;
				titleRow2.createCell(materielDelayDurationI).setCellValue(materielDelayDurationC);
			}
			// 运营顾问延迟时长
			int operationDelayDurationI = -1;
			String operationDelayDurationC = null;
			if (notShowColumnMap.get(DeliveryServiceStatisticsColumn.OPERATION_DELAY_DURATION.getCode()) == null) {
				operationDelayDurationI = columnIndex + 1;
				++columnIndex;
				operationDelayDurationC = "运营顾问";
				if (multipleDelayDurationMinI != -1) {
					multipleDelayDurationMinI = operationDelayDurationI < multipleDelayDurationMinI
							? operationDelayDurationI : multipleDelayDurationMinI;
				} else {
					multipleDelayDurationMinI = operationDelayDurationI;
				}
				multipleDelayDurationMaxI = operationDelayDurationI > multipleDelayDurationMaxI
						? operationDelayDurationI : multipleDelayDurationMaxI;
				titleRow2.createCell(operationDelayDurationI).setCellValue(operationDelayDurationC);
			}
			if (multipleDelayDurationMinI != -1) {
				titleRow1.createCell(multipleDelayDurationMinI).setCellValue("延迟完成服务时长");
			}
			// 是否延期交付
			int excptionLogoI = columnIndex + 1;
			++columnIndex;
			String excptionLogoC = "服务异常原因";
			titleRow1.createCell(excptionLogoI).setCellValue(excptionLogoC);
			// ##### 多列延期时长 end #############
			int maxColumnIndex = columnIndex;
			// 生成合并
			for (int i = 0; i <= maxColumnIndex; i++) {
				if (i == multipleDelayMinI) {
					sheet.addMergedRegion((new CellRangeAddress(0, 0, multipleDelayMinI, multipleDelayMaxI)));
					i = multipleDelayMaxI;
				} else if (i == multipleDelayDurationMinI) {
					sheet.addMergedRegion(
							(new CellRangeAddress(0, 0, multipleDelayDurationMinI, multipleDelayDurationMaxI)));
					i = multipleDelayDurationMaxI;
				} else {
					sheet.addMergedRegion((new CellRangeAddress(0, 1, i, i)));
				}
			}
			// 生成数据
			for (int i = 0; i < list.size(); i++) {
				Row r = sheet.createRow(i + 2);
				DeliveryServiceStatisticsResponseDto test = list.get(i);
				for (int col = 0; col <= maxColumnIndex; col++) {
					Cell cell = r.createCell(col);
					if (col == orderNumberI) {
						cell.setCellValue(test.getOrderNum());
						continue;
					}
					if (col == buyDateI) {
						cell.setCellValue(test.getBuyDate());
						continue;
					}
					if (col == shopNameI) {
						cell.setCellValue(test.getShopName());
						continue;
					}
					if (col == serviceTypeNamesI) {
						cell.setCellValue(test.getServiceTypeNames());
						continue;
					}
					
					if (col == serviceTypeI) {
						String st=test.getServiceType();
						if(!StringUtil.isBlank(st)){
							st=st.replace("JYK", "聚引客上门交付服务").replace("FMPS_BASIC", "首次上门服务（基础版）")
							.replace("FMPS", "智能客流运营全套落地服务").replace("MU", "物料服务").replace("VC", "售后上门培训（收费）")
							.replace("INTO_PIECES", "掌贝平台交付服务").replace("ZHCT_OLD", "智慧餐厅安装交付(老商户)").replace("ZHCT", "智慧餐厅安装交付");
						}else{
							st="";
						}
						cell.setCellValue(st);
						continue;
					}
					
					if (col == agentNameI) {
						cell.setCellValue(test.getAgentName());
						continue;
					}
					if (col == operationAdviserNameI) {
						cell.setCellValue(test.getOperationAdviserName());
						continue;
					}
					if (col == startTimeI) {
						cell.setCellValue(test.getStartTime());
						continue;
					}
					if (col == shouldFlowEndTimeI) {
						cell.setCellValue(test.getShouldFlowEndTime());
						continue;
					}

					if (col == flowEndFlagI) {
						cell.setCellValue(test.getFlowEndFlag());
						continue;
					}
					if (col == flowEndTimeI) {
						cell.setCellValue(test.getFlowEndTime());
						continue;
					}
					if (col == delayFlagI) {
						cell.setCellValue(test.getDelayFlag());
						continue;
					}
					
					if (col == openDelayFlagI) {
						cell.setCellValue(test.getOpenDelayFlag());
						continue;
					}
					if (col == materielDelayFlagI) {
						cell.setCellValue(test.getMaterielDelayFlag());
						continue;
					}
					if (col == operationDelayFlagI) {
						cell.setCellValue(test.getOperationDelayFlag());
						continue;
					}
					if (col == openDelayDurationI) {
						cell.setCellValue(test.getOpenDelayDuration());
						continue;
					}
					if (col == materielDelayDurationI) {
						cell.setCellValue(test.getMaterielDelayDuration());
						continue;
					}
					if (col == operationDelayDurationI) {
						cell.setCellValue(test.getOperationDelayDuration());
						continue;
					}
					if (col == excptionLogoI) {
						cell.setCellValue(test.getExcptionLogo());
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

    /**
     * 获取团队列表查询所需数据
     *
     * @param response
     * @return
     * @date 2018年5月31日
     * @author linqunzhi
     */
    @RequestMapping(value = "getTeamQueryData", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult getTeamQueryData(HttpServletResponse response) {
        Principal principal = UserUtils.getPrincipal();
        QueryDataResponseDto dto = statisticsService.getTeamQueryData(principal.getId());
        return new BaseResult(dto);

    }
    
    /**
     * 初始化 表头信息
     *
     * @param response
     * @return
     * @date 2018年5月31日
     * @author linqunzhi
     */
    @RequestMapping(value = "getServiceQueryData")
    @ResponseBody
    public JSONObject getServiceQueryData(HttpServletResponse response) {
        Principal principal = UserUtils.getPrincipal();
        JSONObject dto = statisticsService.getServiceQueryData(principal.getId());
        return dto;

    }
    
    /**
     * 根据服务项找人
     *
     * @param response
     * @return
     * @date 2018年5月31日
     * @author linqunzhi
     */
    @RequestMapping(value = "getServiceByServiceType")
    @ResponseBody
    public List<Map<String, String>> getServiceByServiceType(String serviceType) {
        Principal principal = UserUtils.getPrincipal();
        List<Map<String, String>> dto = statisticsService.getServiceByServiType(serviceType,principal.getId());
        return dto;

    }
    
    /**
     * 根据团队找人
     *
     * @param response
     * @return
     * @date 2018年5月31日
     * @author linqunzhi
     */
    @RequestMapping(value = "getServiceTypeByTeam")
    @ResponseBody
    public List<Map<String, String>> getServiceTypeByTeam(String teamId) {
        Principal principal = UserUtils.getPrincipal();
        List<Map<String, String>> dto = statisticsService.getServiceTypeByTeam(teamId);
        return dto;

    }

    /**
     * 获取个人列表查询所需数据
     *
     * @param response
     * @return
     * @date 2018年5月31日
     * @author linqunzhi
     */
    @RequestMapping(value = "getUserQueryData", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult getUserQueryData(HttpServletResponse response) {
        QueryDataResponseDto dto = statisticsService.getUserQueryData();
        return new BaseResult(dto);
    }

    /**
     * 导出报表
     *
     * @param result
     * @param response
     * @date 2018年5月29日
     * @author linqunzhi
     */
    private boolean export(String tileNames,String[] fileNames, DeliveryServiceStatisticsAllResponseDto dto1,
			DeliveryServiceStatisticsAllResponseDto dto2, DeliveryServiceStatisticsAllResponseDto dto3,
			HttpServletResponse response) {
    	HSSFWorkbook workbook = new HSSFWorkbook();
		boolean result=false;
		result=createDeliveryExl(workbook,fileNames[0],dto1);
		result=createVCExl(workbook,fileNames[1],dto2);
		result=createMUExl(workbook,fileNames[2],dto3);
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
    
    /**
     * 导出报表
     *
     * @param result
     * @param response
     * @date 2018年5月29日
     * @author linqunzhi
     */
    private boolean export(String fileName, DeliveryServiceStatisticsAllResponseDto dto, HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        boolean result = false;
        List<DeliveryServiceStatisticsResponseDto> list = dto.getList();
        // 隐藏列map
        Map<String, String> notShowColumnMap = getNotShowColumnMap(dto.getNotShowColumnList());
        if (null != list && !list.isEmpty()) {
            Sheet sheet = workbook.createSheet(fileName);
            Row titleRow1 = sheet.createRow(0);
            Row titleRow2 = sheet.createRow(1);
            // 列下标
            int columnIndex = -1;
            // 订单号
            int orderNumberI = columnIndex + 1;
            ++columnIndex;
            String orderNumberC = "订单号";
            titleRow1.createCell(orderNumberI).setCellValue(orderNumberC);
            // 购买时间
            int buyDateI = columnIndex + 1;
            ++columnIndex;
            String buyDateC = "购买时间";
            titleRow1.createCell(buyDateI).setCellValue(buyDateC);
            // 商户简称
            int shopNameI = columnIndex + 1;
            ++columnIndex;
            String shopNameC = "商户简称";
            titleRow1.createCell(shopNameI).setCellValue(shopNameC);
            // 已购服务类型
            int serviceTypeNamesI = columnIndex + 1;
            ++columnIndex;
            String serviceTypeNamesC = "已购服务类型";
            titleRow1.createCell(serviceTypeNamesI).setCellValue(serviceTypeNamesC);
            // 所属服务商
            int agentNameI = -1;
            String agentNameC = null;
            if (notShowColumnMap.get(DeliveryServiceStatisticsColumn.AGENT_NAME.getCode()) == null) {
                agentNameI = columnIndex + 1;
                ++columnIndex;
                agentNameC = DeliveryServiceStatisticsColumn.AGENT_NAME.getName();
                titleRow1.createCell(agentNameI).setCellValue(agentNameC);
            }
            // 运营顾问
            int operationAdviserNameI = columnIndex + 1;
            ++columnIndex;
            String operationAdviserNameC = "运营顾问";
            titleRow1.createCell(operationAdviserNameI).setCellValue(operationAdviserNameC);
            // 启动时间
            int startTimeI = columnIndex + 1;
            ++columnIndex;
            String startTimeC = "启动时间";
            titleRow1.createCell(startTimeI).setCellValue(startTimeC);
            // 应完成交付时间
            int shouldFlowEndTimeI = columnIndex + 1;
            ++columnIndex;
            String shouldFlowEndTimeC = "应完成交付时间";
            titleRow1.createCell(shouldFlowEndTimeI).setCellValue(shouldFlowEndTimeC);
            // 是否已完成交付
            int flowEndFlagI = columnIndex + 1;
            ++columnIndex;
            String flowEndFlagC = "是否已完成交付";
            titleRow1.createCell(flowEndFlagI).setCellValue(flowEndFlagC);
            // 实际完成交付时间
            int flowEndTimeI = columnIndex + 1;
            ++columnIndex;
            String flowEndTimeC = "实际完成交付时间";
            titleRow1.createCell(flowEndTimeI).setCellValue(flowEndTimeC);
            // 是否延期交付
            int delayFlagI = columnIndex + 1;
            ++columnIndex;
            String delayFlagC = "是否延期交付";
            titleRow1.createCell(delayFlagI).setCellValue(delayFlagC);
            // ##### 多列延期合并处理 start #############
            // 多列延期最小列下标
            int multipleDelayMinI = -1;
            // 多列延期最大列下标
            int multipleDelayMaxI = -1;
            // 开户顾问延期
            int openDelayFlagI = -1;
            String openDelayFlagC = null;
            if (notShowColumnMap.get(DeliveryServiceStatisticsColumn.OPEN_DELAY_FLAG.getCode()) == null) {
                openDelayFlagI = columnIndex + 1;
                ++columnIndex;
                openDelayFlagC = "开户顾问";
                if (multipleDelayMinI != -1) {
                    multipleDelayMinI = openDelayFlagI < multipleDelayMinI ? openDelayFlagI : multipleDelayMinI;
                } else {
                    multipleDelayMinI = openDelayFlagI;
                }
                multipleDelayMaxI = openDelayFlagI > multipleDelayMaxI ? openDelayFlagI : multipleDelayMaxI;
                titleRow2.createCell(openDelayFlagI).setCellValue(openDelayFlagC);
            }
            // 物料顾问是否延迟
            int materielDelayFlagI = -1;
            String materielDelayFlagC = null;
            if (notShowColumnMap.get(DeliveryServiceStatisticsColumn.MATERIEL_DELAY_FLAG.getCode()) == null) {
                materielDelayFlagI = columnIndex + 1;
                ++columnIndex;
                materielDelayFlagC = "物料顾问";
                if (multipleDelayMinI != -1) {
                    multipleDelayMinI = materielDelayFlagI < multipleDelayMinI ? materielDelayFlagI : multipleDelayMinI;
                } else {
                    multipleDelayMinI = materielDelayFlagI;
                }
                multipleDelayMaxI = materielDelayFlagI > multipleDelayMaxI ? materielDelayFlagI : multipleDelayMaxI;
                titleRow2.createCell(materielDelayFlagI).setCellValue(materielDelayFlagC);
            }
            // 运营顾问是否延迟
            int operationDelayFlagI = -1;
            String operationDelayFlagC = null;
            if (notShowColumnMap.get(DeliveryServiceStatisticsColumn.OPERATION_DELAY_FLAG.getCode()) == null) {
                operationDelayFlagI = columnIndex + 1;
                ++columnIndex;
                operationDelayFlagC = "运营顾问";
                if (multipleDelayMinI != -1) {
                    multipleDelayMinI = operationDelayFlagI < multipleDelayMinI ? operationDelayFlagI : multipleDelayMinI;
                } else {
                    multipleDelayMinI = operationDelayFlagI;
                }
                multipleDelayMaxI = operationDelayFlagI > multipleDelayMaxI ? operationDelayFlagI : multipleDelayMaxI;
                titleRow2.createCell(operationDelayFlagI).setCellValue(operationDelayFlagC);
            }
            if (multipleDelayMinI != -1) {
                titleRow1.createCell(multipleDelayMinI).setCellValue("是否延迟完成服务");
            }
            // ##### 多列延期合并 end #############

            // ##### 多列延期时长合并处理 start #############
            // 多列延期时长最小列下标
            int multipleDelayDurationMinI = -1;
            // 多列延期时长最大列下标
            int multipleDelayDurationMaxI = -1;
            // 开户顾问延期时长
            int openDelayDurationI = -1;
            String openDelayDurationC = null;
            if (notShowColumnMap.get(DeliveryServiceStatisticsColumn.OPEN_DELAY_DURATION.getCode()) == null) {
                openDelayDurationI = columnIndex + 1;
                ++columnIndex;
                openDelayDurationC = "开户顾问";
                if (multipleDelayDurationMinI != -1) {
                    multipleDelayDurationMinI = openDelayDurationI < multipleDelayDurationMinI ? openDelayDurationI : multipleDelayDurationMinI;
                } else {
                    multipleDelayDurationMinI = openDelayDurationI;
                }

                multipleDelayDurationMaxI = openDelayDurationI > multipleDelayDurationMaxI ? openDelayDurationI : multipleDelayDurationMaxI;
                titleRow2.createCell(openDelayDurationI).setCellValue(openDelayDurationC);
            }
            // 物料顾问延迟时长
            int materielDelayDurationI = -1;
            String materielDelayDurationC = null;
            if (notShowColumnMap.get(DeliveryServiceStatisticsColumn.MATERIEL_DELAY_DURATION.getCode()) == null) {
                materielDelayDurationI = columnIndex + 1;
                ++columnIndex;
                materielDelayDurationC = "物料顾问";
                if (multipleDelayDurationMinI != -1) {
                    multipleDelayDurationMinI = materielDelayDurationI < multipleDelayDurationMinI ? materielDelayDurationI : multipleDelayDurationMinI;
                } else {
                    multipleDelayDurationMinI = materielDelayDurationI;
                }
                multipleDelayDurationMaxI = materielDelayDurationI > multipleDelayDurationMaxI ? materielDelayDurationI : multipleDelayDurationMaxI;
                titleRow2.createCell(materielDelayDurationI).setCellValue(materielDelayDurationC);
            }
            // 运营顾问延迟时长
            int operationDelayDurationI = -1;
            String operationDelayDurationC = null;
            if (notShowColumnMap.get(DeliveryServiceStatisticsColumn.OPERATION_DELAY_DURATION.getCode()) == null) {
                operationDelayDurationI = columnIndex + 1;
                ++columnIndex;
                operationDelayDurationC = "运营顾问";
                if (multipleDelayDurationMinI != -1) {
                    multipleDelayDurationMinI = operationDelayDurationI < multipleDelayDurationMinI ? operationDelayDurationI : multipleDelayDurationMinI;
                } else {
                    multipleDelayDurationMinI = operationDelayDurationI;
                }
                multipleDelayDurationMaxI = operationDelayDurationI > multipleDelayDurationMaxI ? operationDelayDurationI : multipleDelayDurationMaxI;
                titleRow2.createCell(operationDelayDurationI).setCellValue(operationDelayDurationC);
            }
            if (multipleDelayDurationMinI != -1) {
                titleRow1.createCell(multipleDelayDurationMinI).setCellValue("延迟完成服务时长");
            }
            // ##### 多列延期时长 end #############
            int maxColumnIndex = columnIndex;
            // 生成合并
            for (int i = 0; i <= maxColumnIndex; i++) {
                if (i == multipleDelayMinI) {
                    sheet.addMergedRegion((new CellRangeAddress(0, 0, multipleDelayMinI, multipleDelayMaxI)));
                    i = multipleDelayMaxI;
                } else if (i == multipleDelayDurationMinI) {
                    sheet.addMergedRegion((new CellRangeAddress(0, 0, multipleDelayDurationMinI, multipleDelayDurationMaxI)));
                    i = multipleDelayDurationMaxI;
                } else {
                    sheet.addMergedRegion((new CellRangeAddress(0, 1, i, i)));
                }
            }
            // 生成数据
            for (int i = 0; i < list.size(); i++) {
                Row r = sheet.createRow(i + 2);
                DeliveryServiceStatisticsResponseDto test = list.get(i);
                for (int col = 0; col <= maxColumnIndex; col++) {
                    Cell cell = r.createCell(col);
                    if (col == orderNumberI) {
                        cell.setCellValue(test.getOrderNum());
                        continue;
                    }
                    if (col == buyDateI) {
                        cell.setCellValue(test.getBuyDate());
                        continue;
                    }
                    if (col == shopNameI) {
                        cell.setCellValue(test.getShopName());
                        continue;
                    }
                    if (col == serviceTypeNamesI) {
                        cell.setCellValue(test.getServiceTypeNames());
                        continue;
                    }
                    if (col == agentNameI) {
                        cell.setCellValue(test.getAgentName());
                        continue;
                    }
                    if (col == operationAdviserNameI) {
                        cell.setCellValue(test.getOperationAdviserName());
                        continue;
                    }
                    if (col == startTimeI) {
                        cell.setCellValue(test.getStartTime());
                        continue;
                    }
                    if (col == shouldFlowEndTimeI) {
                        cell.setCellValue(test.getShouldFlowEndTime());
                        continue;
                    }

                    if (col == flowEndFlagI) {
                        cell.setCellValue(test.getFlowEndFlag());
                        continue;
                    }
                    if (col == flowEndTimeI) {
                        cell.setCellValue(test.getFlowEndTime());
                        continue;
                    }
                    if (col == delayFlagI) {
                        cell.setCellValue(test.getDelayFlag());
                        continue;
                    }
                    if (col == openDelayFlagI) {
                        cell.setCellValue(test.getOpenDelayFlag());
                        continue;
                    }
                    if (col == materielDelayFlagI) {
                        cell.setCellValue(test.getMaterielDelayFlag());
                        continue;
                    }
                    if (col == operationDelayFlagI) {
                        cell.setCellValue(test.getOperationDelayFlag());
                        continue;
                    }
                    if (col == openDelayDurationI) {
                        cell.setCellValue(test.getOpenDelayDuration());
                        continue;
                    }
                    if (col == materielDelayDurationI) {
                        cell.setCellValue(test.getMaterielDelayDuration());
                        continue;
                    }
                    if (col == operationDelayDurationI) {
                        cell.setCellValue(test.getOperationDelayDuration());
                        continue;
                    }
                }
            }
            // 设置列自适应宽度
            for (int i = 0; i <= maxColumnIndex; i++) {
                sheet.autoSizeColumn(i, true);
            }
            OutputStream fileOutputStream = null;
            try {
                response.reset();// 清空输出流
                response.setHeader("Content-disposition", "attachment; filename=" + new String((fileName + ".xls").getBytes("gbk"), "ISO8859-1"));
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
        }
        return result;
    }

    /**
     * 获取隐藏列map
     *
     * @param notShowColumnList
     * @return
     * @date 2018年5月30日
     * @author linqunzhi
     */
    private static Map<String, String> getNotShowColumnMap(List<String> notShowColumnList) {
        Map<String, String> result = new HashMap<>();
        if (CollectionUtils.isNotEmpty(notShowColumnList)) {
            for (String code : notShowColumnList) {
                result.put(code, code);
            }
        }
        return result;
    }
    
    /**
     * 获取生产服务团队明细列表
     *
     * @param requestDto
     * @param request
     * @param response
     * @return
     * @date 2018年5月29日
     * @author linqunzhi
     */
    @RequestMapping(value = "/findTeamStatisticsDetail", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult findTeamStatisticsDetail(@RequestBody DeliveryServiceStatisticsDetailDto requestDto,
    				@RequestParam(required = false, value = "teamId") String teamId,
    				@RequestParam(required = false, value = "teamName") String teamName,
    				HttpServletRequest request,
                    HttpServletResponse response) {
        Principal principal = UserUtils.getPrincipal();
        DeliveryServiceStatisticsAllResponseDto result = statisticsService
                        .findTeamStatisticsDetail(new Page<DeliveryServiceStatisticsDetailDto>(request, response), requestDto, principal.getId());
        return new BaseResult(result);
    }
    
    @RequestMapping(value = "/openServiceDetail")
    public String openServiceDetail(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/statistics/openServiceDetail";
    }
    
    @RequestMapping(value = "/openServiceDetailByUser")
    public String openServiceDetailByUser(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/statistics/openServiceDetailByUser";
    }
    
    /**
     * 获取生产服务团队明细列表
     *
     * @param requestDto
     * @param request
     * @param response
     * @return
     * @date 2018年5月29日
     * @author linqunzhi
     */
    @RequestMapping(value = "/findStatisticsDetailByUser", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult findStatisticsDetailByUser(@RequestBody DeliveryServiceStatisticsDetailDto requestDto,
    				@RequestParam(required = false, value = "userId") String userId,
    				@RequestParam(required = false, value = "userName") String userName,
    				HttpServletRequest request,
                    HttpServletResponse response) {
        Principal principal = UserUtils.getPrincipal();
        DeliveryServiceStatisticsAllResponseDto result = statisticsService
                        .findTeamStatisticsDetailByUser(new Page<DeliveryServiceStatisticsDetailDto>(request, response), requestDto, principal.getId());
        return new BaseResult(result);
    }
    
    /**
     * 获取生产服务团队明细列表
     *
     * @param requestDto
     * @param request
     * @param response
     * @return
     * @date 2018年5月29日
     * @author linqunzhi
     */
    @RequestMapping(value = "/findStatisticsByUser", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult findStatisticsByUser(@RequestBody DeliveryServiceStatisticsDetailDto requestDto,
    				HttpServletRequest request,
                    HttpServletResponse response) {
        Principal principal = UserUtils.getPrincipal();
        DeliveryServiceStatisticsAllResponseDto result = statisticsService
                        .findTeamStatisticsByUser(new Page<DeliveryServiceStatisticsDetailDto>(request, response), requestDto, principal.getId());
        return new BaseResult(result);
    }
    
    /**
     * 获取生产服务团队明细列表
     *
     * @param requestDto
     * @param request
     * @param response
     * @return
     * @date 2018年5月29日
     * @author linqunzhi
     */
    @RequestMapping(value = "/findStatisticsCompleteDetail", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult findStatisticsCompleteDetail(@RequestBody DeliveryServiceStatisticsDetailDto requestDto,
    				@RequestParam(required = false, value = "teamId") String teamId,
    				@RequestParam(required = false, value = "teamName") String teamName,
    				HttpServletRequest request,
                    HttpServletResponse response) {
        Principal principal = UserUtils.getPrincipal();
        DeliveryServiceStatisticsAllResponseDto result = statisticsService
                        .findStatisticsCompleteDetail(new Page<DeliveryServiceStatisticsDetailDto>(request, response), requestDto, principal.getId());
        return new BaseResult(result);
    }
    
    @RequestMapping(value = "/findStatisticsCompleteURL")
    public String findStatisticsCompleteURL(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/statistics/findStatisticsCompleteURL";
    }
}