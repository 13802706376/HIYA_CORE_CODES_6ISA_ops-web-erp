package com.yunnex.ops.erp.modules.statistics.excelUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;


public class ExportExcelUtil {
	/**
     * 日志对象
     */
	private static final Logger LOGGER = LoggerFactory.getLogger(ExportExcelUtil.class);
    private static final int COL1=15;
    private static final int COL2=13;
    private static final int COL_10=10;
    private static final int COL_15=15;
    private static final int COL_18=18;
    private static final int COL_8=8;
    private static final int COL_7=7;
    private static final int COL_17=17;
    private static final int COL_0=0;
    private static final int COL_1=1;
    private static final int COL_2=2;
    private static final int COL_3=3;


	/**
	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
	 * 
	 * @param title
	 *            表格标题名
	 * @param headerTitle
	 *            表格属性列名数组（标题）若为null，默认显示headers
	 * @param headers
	 *            表格属性列名数组
	 * @param dataset
	 *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
	 *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * @param out
	 *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param pattern
	 *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
	 */
	public void createExcel(String sheetname, String[] num, String[] num1,
			String[] num2, List<Map<String, Object>> dataset, List<Map<String, Object>> distinctData,
			OutputStream out) {
		String[] leaderTitle = { "订单基本信息", "已完成推广服务明细", "订单推广金额明细" };// ,"已完成推广服务明细","订单推广金额明细"
		String[] headerTitle = { "订单号", "商户全称", "购买时间", "联系方式", "订单类别", "分公司", "所属服务商", "已购聚引客服务", "已完成推广服务", "推广通道",
				"推广上线时间", "推广结束时间", "朋友圈推广金额", "微博推广金额", "套餐推广总金额", "朋友圈推广总金额", "微博推广总金额", "订单推广总金额" };
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 1.1创建合并单元格对象
		// 2.创建工作表
		HSSFSheet sheet = workbook.createSheet(sheetname);
		// 1.2头标题样式
		HSSFCellStyle headStyle = createCellStyle(workbook, (short) COL1);
		// 1.3列标题样式
		HSSFCellStyle colStyle = createCellStyle(workbook, (short) COL2);
		// 2.1加载合并单元格对象
		// 设置默认列宽
		sheet.setDefaultColumnWidth(COL_17);
		// 3.创建行
		// 3.1创建头标题行;并且设置头标题
		HSSFRow row = sheet.createRow(COL_0);
		// 加载单元格样式
		HSSFCell cell = row.createCell(Integer.valueOf(num[COL_3]));
		cell.setCellStyle(headStyle);
		HSSFCell cell1 = row.createCell(Integer.valueOf(num1[COL_3]));
		cell1.setCellStyle(headStyle);
		HSSFCell cell2 = row.createCell(Integer.valueOf(num2[COL_3]));
		cell2.setCellStyle(headStyle);
		cell.setCellValue(leaderTitle[COL_0]);
		cell.setCellValue(leaderTitle[COL_1]);
		cell.setCellValue(leaderTitle[COL_2]);
		CellRangeAddress callRangeAddress = new CellRangeAddress(Integer.valueOf(num[COL_0]), Integer.valueOf(num[COL_1]),
				Integer.valueOf(num[COL_2]), Integer.valueOf(num[COL_3]));// 起始行,结束行,起始列,结束列
		sheet.addMergedRegion(callRangeAddress);

		cell1.setCellValue(leaderTitle[1]);
		CellRangeAddress callRangeAddress1 = new CellRangeAddress(Integer.valueOf(num1[COL_0]), Integer.valueOf(num1[COL_1]),
				Integer.valueOf(num1[COL_2]), Integer.valueOf(num1[COL_3]));// 起始行,结束行,起始列,结束列
		sheet.addMergedRegion(callRangeAddress1);

		cell2.setCellValue(leaderTitle[COL_2]);
		CellRangeAddress callRangeAddress2 = new CellRangeAddress(Integer.valueOf(num2[COL_0]), Integer.valueOf(num2[COL_1]),
				Integer.valueOf(num2[COL_2]), Integer.valueOf(num2[COL_3]));// 起始行,结束行,起始列,结束列
		sheet.addMergedRegion(callRangeAddress2);
		// 3.2创建列标题;并且设置列标题
		HSSFRow row2 = sheet.createRow(COL_1);
		for (int i = 0; i < headerTitle.length; i++) {
			HSSFCell cell_ = row2.createCell(i);
			// 加载单元格样式
			cell_.setCellStyle(colStyle);
			cell_.setCellValue(headerTitle[i]);
		}

		try {
			generateSheet(dataset, distinctData, workbook, sheet);
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public void generateSheet(List<Map<String, Object>> dataList, List<Map<String, Object>> distinctData,
			HSSFWorkbook workbook, HSSFSheet sheet) {
		if (dataList==null) {
			return;
		}
		HSSFCellStyle colStyle = createCellStylebody(workbook, (short) COL_10);
		String[] header1 = { "orderNumber", "shopName", "buyDate", "contactNumber", "orderType", "companyName",
				"agentName", "goodNames", "goodName", "promotionChannel", "promoteStartDate", "promoteEndDate",
				"expenditurePy", "expenditureWb", "expenditureMm", "expenditurePyall", "expenditureWball", "expenditureMmall","promoteStatus"};
		Pattern p = Pattern.compile("^//d+(//.//d+)?$");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			int index = 0;
			int endindex = 0;
			int startindex = 0;
			for (Map<String, Object> m : distinctData) {
				startindex = index + COL_2;
				String order_number = (String) m.get("order_number");
				for (Map<String, Object> n : dataList) {
					String number = (String) n.get("orderNumber");
					String promoteStatus = (String) n.get("promoteStatus");
					if (order_number.equals(number)) {
						HSSFRow row = sheet.createRow(index + COL_2);
						if (index == 0) {
							endindex = index + COL_2;
						}
						for (int i = 0; i < header1.length; i++) {
							HSSFCell cell = row.createCell(i);
							cell.setCellStyle(colStyle);
							Object value = n.get(header1[i]);
							if(i>COL_7 && (StringUtils.isEmpty(promoteStatus) || promoteStatus.indexOf("running")!=-1 || promoteStatus.indexOf("notstart")!=-1)){
								value="--";
							}
							String textValue = null;
							if (value instanceof Integer) {
								value=value==null?0:value;
								cell.setCellValue((Integer) value);
							} else if (value instanceof Float) {
								value=value==null?0.0:value;
								textValue = String.valueOf((Float) value);
								cell.setCellValue(textValue);
							} else if (value instanceof Double) {
								value=value==null?0.0:value;
								textValue = String.valueOf((Double) value);
								cell.setCellValue(textValue);
							} else if (value instanceof Long) {
								value=value==null?0:value;
								cell.setCellValue((Long) value);
							} else if (value instanceof Date) {
								textValue = sdf.format((Date) value);
							} else {
								textValue=StringUtils.isEmpty(value)?"--":value.toString();
							}
							Matcher matcher = p.matcher(textValue);
							if (textValue != null && matcher.matches()) {
								// 是数字当作double处理
								cell.setCellValue(Double.parseDouble(textValue));
							} else {
								HSSFRichTextString richString = new HSSFRichTextString(textValue);
								cell.setCellValue(richString);
							}
						}
						index++;
						endindex++;
					}
				}
				for (int i = 0; i < COL_8; i++) {
					if(startindex!=endindex){
						CellRangeAddress callRangeAddress = new CellRangeAddress(startindex, endindex - 1, i, i);// 起始行,结束行,起始列,结束列
						sheet.addMergedRegion(callRangeAddress);
					}
				}
				for (int i = COL_15; i < COL_18; i++) {
					if(startindex!=endindex){
						CellRangeAddress callRangeAddress = new CellRangeAddress(startindex, endindex - 1, i, i);// 起始行,结束行,起始列,结束列
						sheet.addMergedRegion(callRangeAddress);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param workbook
	 * @param fontsize
	 * @return 单元格样式
	 */
	private static HSSFCellStyle createCellStyle(HSSFWorkbook workbook, short fontsize) {
		HSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints(fontsize);
		style.setFont(font);
		return style;
	}
	
	/**
	 * 
	 * @param workbook
	 * @param fontsize
	 * @return 单元格样式
	 */
	private static HSSFCellStyle createCellStylebody(HSSFWorkbook workbook, short fontsize) {
		HSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		style.setWrapText(true); 
		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints(fontsize);
		style.setFont(font);
		return style;
	}
	
	public static void exportTogetherExcel(HttpServletResponse response, String fileName, String sheetName, List<Map<String, Object>> dataset, List<Map<String, Object>> distinctData) throws IOException {
		response.reset();// 清空输出流
		response.setHeader("Content-disposition",
				"attachment; filename=" + new String((fileName + ".xls").getBytes("gbk"), "ISO8859-1"));
		response.setContentType("application/msexcel;charset=utf-8");
		ExportExcelUtil ex = new ExportExcelUtil();
		
		OutputStream fileOutputStream = response.getOutputStream();
		String[] num = { "0", "0", "0", "7" };
		String[] num1 = { "0", "0", "8", "14" };
		String[] num2 = { "0", "0", "15", "17" };
		ex.createExcel(sheetName,num, num1, num2, dataset, distinctData, fileOutputStream);
		fileOutputStream.close();
	}

}
