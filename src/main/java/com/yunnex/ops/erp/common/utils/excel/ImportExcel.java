/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.yunnex.ops.erp.common.utils.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.result.IllegalArgumentErrorResult;
import com.yunnex.ops.erp.common.result.SystemErrorResult;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.common.utils.Reflections;
import com.yunnex.ops.erp.common.utils.excel.annotation.ExcelField;
import com.yunnex.ops.erp.modules.sys.utils.DictUtils;


/**
 * 导入Excel文件（支持“XLS”和“XLSX”格式）
 * 
 * @author ThinkGem
 * @version 2013-03-10
 */
public class ImportExcel {

    private static Logger LOGGER = LoggerFactory.getLogger(ImportExcel.class);

    /**
     * 工作薄对象
     */
    private Workbook wb;

    /**
     * 工作表对象
     */
    private Sheet sheet;

    /**
     * 标题行号
     */
    private int headerNum;

    /**
     * 构造函数
     * 
     * @param path 导入文件，读取第一个工作表
     * @param headerNum 标题行号，数据行号=标题行号+1
     * @throws InvalidFormatException
     * @throws IOException
     */
    public ImportExcel(String fileName, int headerNum) throws InvalidFormatException, IOException {
        this(new File(fileName), headerNum);
    }

    /**
     * 构造函数
     * 
     * @param path 导入文件对象，读取第一个工作表
     * @param headerNum 标题行号，数据行号=标题行号+1
     * @throws InvalidFormatException
     * @throws IOException
     */
    public ImportExcel(File file, int headerNum) throws InvalidFormatException, IOException {
        this(file, headerNum, 0);
    }

    /**
     * 构造函数
     * 
     * @param path 导入文件
     * @param headerNum 标题行号，数据行号=标题行号+1
     * @param sheetIndex 工作表编号
     * @throws InvalidFormatException
     * @throws IOException
     */
    public ImportExcel(String fileName, int headerNum, int sheetIndex) throws InvalidFormatException, IOException {
        this(new File(fileName), headerNum, sheetIndex);
    }

    /**
     * 构造函数
     * 
     * @param path 导入文件对象
     * @param headerNum 标题行号，数据行号=标题行号+1
     * @param sheetIndex 工作表编号
     * @throws InvalidFormatException
     * @throws IOException
     */
    public ImportExcel(File file, int headerNum, int sheetIndex) throws InvalidFormatException, IOException {
        this(file.getName(), new FileInputStream(file), headerNum, sheetIndex);
    }

    /**
     * 构造函数
     * 
     * @param file 导入文件对象
     * @param headerNum 标题行号，数据行号=标题行号+1
     * @param sheetIndex 工作表编号
     * @throws InvalidFormatException
     * @throws IOException
     */
    public ImportExcel(MultipartFile multipartFile, int headerNum, int sheetIndex) throws InvalidFormatException, IOException {
        this(multipartFile.getOriginalFilename(), multipartFile.getInputStream(), headerNum, sheetIndex);
    }

    /**
     * 构造函数
     * 
     * @param path 导入文件对象
     * @param headerNum 标题行号，数据行号=标题行号+1
     * @param sheetIndex 工作表编号
     * @throws IOException
     */
    public ImportExcel(String fileName, InputStream is, int headerNum, int sheetIndex) throws IOException {
        if (StringUtils.isBlank(fileName)) {
            throw new ServiceException("导入文档为空!");
        } else if (fileName.toLowerCase().endsWith("xls")) {
            this.wb = new HSSFWorkbook(is);
        } else if (fileName.toLowerCase().endsWith("xlsx")) {
            this.wb = new XSSFWorkbook(is);
        } else {
            throw new ServiceException("文档格式不正确!");
        }
        if (this.wb.getNumberOfSheets() < sheetIndex) {
            throw new ServiceException("文档中没有工作表!");
        }
        this.sheet = this.wb.getSheetAt(sheetIndex);
        this.headerNum = headerNum;
        LOGGER.debug("Initialize success.");
    }

    /**
     * 获取行对象
     * 
     * @param rownum
     * @return
     */
    public Row getRow(int rownum) {
        return this.sheet.getRow(rownum);
    }

    /**
     * 获取数据行号
     * 
     * @return
     */
    public int getDataRowNum() {
        return headerNum + 1;
    }

    /**
     * 获取最后一个数据行号
     * 
     * @return
     */
    public int getLastDataRowNum() {
        return this.sheet.getLastRowNum();
    }

    /**
     * 获取最后一个列号
     * 
     * @return
     */
    public int getLastCellNum() {
        return this.getRow(headerNum).getLastCellNum();
    }

    /**
     * 获取单元格值
     * 
     * @param row 获取的行
     * @param column 获取单元格列号
     * @return 单元格值
     */
    public Object getCellValue(Row row, int column) {
        Object val = "";
        try {
            Cell cell = row.getCell(column);
            if (cell != null) {
                if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        val = cell.getDateCellValue();
                    } else {
                        val = cell.getNumericCellValue();
                    }
                } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    val = cell.getStringCellValue();
                } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                    val = cell.getCellFormula();
                } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
                    val = cell.getBooleanCellValue();
                } else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
                    val = cell.getErrorCellValue();
                }
            }
        } catch (Exception e) {
            return val;
        }
        return val;
    }

    /**
     * 获取导入数据列表
     * 
     * @param cls 导入对象类型
     * @param groups 导入索引，不传则导入所有
     */
    public <E> List<E> getDataList(Class<E> cls, int... groups) throws InstantiationException, IllegalAccessException {
        JSONArray jsonArray = new JSONArray();
        List<Object[]> annotationList = Lists.newArrayList();
        // Get annotation field
        boolean isCellIndex = false;
        ExcelField objef = cls.getAnnotation(ExcelField.class);
        if (objef.isCellIndex()) {// 添加自主选择导入列判断
            isCellIndex = objef.isCellIndex();
        }
        Field[] fs = cls.getDeclaredFields();
        for (Field f : fs) {
            ExcelField ef = f.getAnnotation(ExcelField.class);
            if (ef != null && (ef.type() == 0 || ef.type() == 2)) {
                if (groups != null && groups.length > 0) {
                    boolean inGroup = false;
                    for (int g : groups) {
                        if (inGroup) {
                            break;
                        }
                        for (int efg : ef.groups()) {
                            if (g == efg) {
                                inGroup = true;
                                annotationList.add(new Object[] {ef, f});
                                break;
                            }
                        }
                    }
                } else {
                    annotationList.add(new Object[] {ef, f});
                }
            }
        }
        // Get annotation method
        Method[] ms = cls.getDeclaredMethods();
        for (Method m : ms) {
            ExcelField ef = m.getAnnotation(ExcelField.class);
            if (ef != null && (ef.type() == 0 || ef.type() == 2)) {
                if (groups != null && groups.length > 0) {
                    boolean inGroup = false;
                    for (int g : groups) {
                        if (inGroup) {
                            break;
                        }
                        for (int efg : ef.groups()) {
                            if (g == efg) {
                                inGroup = true;
                                annotationList.add(new Object[] {ef, m});
                                break;
                            }
                        }
                    }
                } else {
                    annotationList.add(new Object[] {ef, m});
                }
            }
        }
        // Field sorting
        Collections.sort(annotationList, new Comparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return Integer.compare(((ExcelField) o1[0]).sort(), ((ExcelField) o2[0]).sort());
            };
        });
        List<E> dataList = Lists.newArrayList();
        for (int i = this.getDataRowNum(); i <= this.getLastDataRowNum(); i++) {
            E e = (E) cls.newInstance();
            int column = 0;
            Row row = this.getRow(i);
            StringBuilder sb = new StringBuilder();
            for (Object[] os : annotationList) {
                ExcelField ef = (ExcelField) os[0];
                column = isCellIndex ? ef.cellIndex() : column;// 如果为自选列导入，则按注解索引导入列数据，否则轮询导入
                Object val = this.getCellValue(row, column);
                column++;
                if (val != null) {
                    // If is dict type, get dict value
                    if (StringUtils.isNotBlank(ef.dictType())) {
                        val = DictUtils.getDictValue(val.toString(), ef.dictType(), "");
                    }
                    // Get param type and type cast
                    Class<?> valType = Class.class;
                    if (os[1] instanceof Field) {
                        valType = ((Field) os[1]).getType();
                    } else if (os[1] instanceof Method) {
                        Method method = ((Method) os[1]);
                        if ("get".equals(method.getName().substring(0, 3))) {
                            valType = method.getReturnType();
                        } else if ("set".equals(method.getName().substring(0, 3))) {
                            valType = ((Method) os[1]).getParameterTypes()[0];
                        }
                    }
                    try {
                        if (valType == String.class) {
                            String s = String.valueOf(val.toString());
                            if (StringUtils.endsWith(s, ".0")) {
                                val = StringUtils.substringBefore(s, ".0");
                            } else {
                                val = String.valueOf(val.toString());
                            }
                        } else if (valType == Integer.class) {
                            val = Double.valueOf(val.toString()).intValue();
                        } else if (valType == Long.class) {
                            val = Double.valueOf(val.toString()).longValue();
                        } else if (valType == Double.class) {
                            val = Double.valueOf(val.toString());
                        } else if (valType == Float.class) {
                            val = Float.valueOf(val.toString());
                        } else if (valType == Date.class) {
                            if (val instanceof Date) {
                                val = (Date) val;
                            } else {
                                val = DateUtils.parseDate(NumberToTextConverter.toText(Double.valueOf(val.toString()).intValue()));
                            }
                        } else {
                            if (ef.fieldType() != Class.class) {
                                val = ef.fieldType().getMethod("getValue", String.class).invoke(null, val.toString());
                            } else {
                                val = Class.forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(),
                                                "fieldtype." + valType.getSimpleName() + "Type")).getMethod("getValue", String.class)
                                                .invoke(null, val.toString());// NOSONAR
                            }
                        }
                    } catch (Exception ex) {
                        jsonArray.add("Get cell value [" + i + "," + column + "] error: " + ex.toString());
                        val = null;
                    }
                    // set entity value
                    if (os[1] instanceof Field) {
                        Reflections.invokeSetter(e, ((Field) os[1]).getName(), val);
                    } else if (os[1] instanceof Method) {
                        String mthodName = ((Method) os[1]).getName();
                        if ("get".equals(mthodName.substring(0, 3))) {
                            mthodName = "set" + StringUtils.substringAfter(mthodName, "get");
                        }
                        Reflections.invokeMethod(e, mthodName, new Class[] {valType}, new Object[] {val});
                    }
                }
                sb.append(val + ", ");
            }
            dataList.add(e);
            LOGGER.debug("Read success: [" + i + "] " + sb.toString());
        }
        if (!jsonArray.isEmpty() && LOGGER.isErrorEnabled()) {
            LOGGER.error("excel文件导入错误：" + jsonArray.toJSONString());
        }
        return dataList;
    }

    /**
     * 读取指定区间的一列数据
     *
     * @param is
     * @param suffix
     * @param startRow
     * @param endRow
     * @param column
     * @return
     */
    @SuppressWarnings("unused")
    public static BaseResult readOneColumn(InputStream is, String suffix, int startRow, int endRow, int column) {
        if (StringUtils.isBlank(suffix)) {
            return new IllegalArgumentErrorResult("文件后缀名不能为空！");
        }

        Workbook workbook = null;
        try {
            if ("xls".equals(suffix)) {
                workbook = new HSSFWorkbook(is);
            } else if ("xlsx".equals(suffix)) {
                workbook = new XSSFWorkbook(is);
            } else {
                return new IllegalArgumentErrorResult("只支持 xls 和 xlsx 格式！");
            }
        } catch (IOException e) {
            String msg = "Excel导入失败！";
            LOGGER.error(msg, e);
            return new IllegalArgumentErrorResult(msg);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("流关闭失败！", e);
                }
            }
        }

        if (workbook == null) {
            return new SystemErrorResult("无法读取Excel!");
        }
        // 读取第1个表格
        Sheet sheet = workbook.getSheetAt(0);
        List<Object> list = new ArrayList<>();
        // 解决手机号码转换成科学记数法问题
        DecimalFormat df = new DecimalFormat("#");
        // 读取指定范围的行
        for (int i = startRow; i < endRow; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }

            Object object = null;
            // 读取指定列
            Cell cell = row.getCell(column);
            if (cell != null) {
                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    object = cell.getStringCellValue();
                } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    object = df.format(cell.getNumericCellValue());
                }
            }

            list.add(object);
        }

        BaseResult result = new BaseResult();
        result.setAttach(list);
        return result;
    }

}
