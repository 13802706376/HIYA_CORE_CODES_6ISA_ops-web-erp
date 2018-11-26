package com.yunnex.ops.erp.common.utils.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.utils.excel.annotation.MapperCell;

public final class FastExcel implements Closeable {

    private static final Logger LOG = LoggerFactory.getLogger(FastExcel.class);

    private static final int WIDTH_TWO = 2;
    private static final int WIDTH_THREE_HUNDRED = 300;

    /**
     * 时日类型的数据默认格式化方式
     */
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private int startRow;
    private String sheetName;
    private final String excelFilePath;
    private final Workbook workbook;

    public FastExcel() {
        this.workbook = new HSSFWorkbook();
        excelFilePath = "";
    }

    /**
     * 构造方法，传入需要操作的excel文件路径
     *
     * @param excelFilePath 需要操作的excel文件的路径
     * @throws IOException IO流异常
     * @throws InvalidFormatException 非法的格式异常
     */
    public FastExcel(String excelFilePath) throws IOException, InvalidFormatException {
        this.startRow = 0;
        this.sheetName = "Sheet1";
        this.excelFilePath = excelFilePath;
        this.workbook = createWorkbook();
    }

    public FastExcel(MultipartFile file) throws IOException, InvalidFormatException {
        this.startRow = 0;
        this.excelFilePath = "";
        this.workbook = WorkbookFactory.create(file.getInputStream());
    }

    /**
     * 通过数据流操作excel，仅用于读取数据
     *
     * @param inputStream excel数据流
     * @throws IOException IO流异常
     * @throws InvalidFormatException 非法的格式异常
     */
    public FastExcel(InputStream inputStream) throws IOException, InvalidFormatException {
        this.startRow = 0;
        this.sheetName = "Sheet1";
        this.excelFilePath = "";
        this.workbook = WorkbookFactory.create(inputStream);
    }

    /**
     * 通过数据流操作excel
     *
     * @param inputStream excel数据流
     * @param outFilePath 输出的excel文件路径
     * @throws IOException IO流异常
     * @throws InvalidFormatException 非法的格式异常
     */
    public FastExcel(InputStream inputStream, String outFilePath) throws IOException, InvalidFormatException {
        this.startRow = 0;
        this.sheetName = "Sheet1";
        this.excelFilePath = outFilePath;
        this.workbook = WorkbookFactory.create(inputStream);
    }

    /**
     * 开始读取的行数，这里指的是标题内容行的行数，不是数据开始的那行
     *
     * @param startRow 开始行数
     */
    public void setStartRow(int startRow) {
        if (startRow < 1) {
            throw new IllegalArgumentException("最小为1");
        }
        this.startRow = startRow - 1;
    }

    public int getStartRow() {
        return startRow;
    }

    /**
     * 设置需要读取的sheet名字，不设置默认的名字是Sheet1，也就是excel默认给的名字，所以如果文件没有自已修改，这个方法也就不用调了
     *
     * @param sheetName 需要读取的Sheet名字
     */
    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    /**
     * 设置时间数据格式
     *
     * @param format 格式
     */
    public void setFormat(String format) {
        this.format = new SimpleDateFormat(format);
    }



    private Workbook createWorkbook() throws IOException, InvalidFormatException {
        Workbook wb = null;
        File file = new File(this.excelFilePath);
        if (!file.exists()) {
            LOG.warn("文件:{} 不存在！创建此文件！", this.excelFilePath);
            if (!file.createNewFile()) {
                throw new IOException("文件创建失败");
            }
            wb = new XSSFWorkbook();
        } else {
            wb = WorkbookFactory.create(file);
        }
        return wb;
    }

    /**
     * 将数据写入excel文件
     *
     * @param list 数据列表
     * @param <T> 泛型
     * @return 写入结果
     */
    public <T> boolean createExcel(List<T> list) {// NOSONAR
        if (null == this.excelFilePath || "".equals(this.excelFilePath))
            throw new IllegalArgumentException("excelFilePath is null");
        boolean result = false;
        FileOutputStream fileOutputStream = null;
        if (null != list && !list.isEmpty()) {
            T test = list.get(0);
            Map<String, Field> fieldMap = new HashMap<String, Field>();
            Map<Integer, String> titleMap = new TreeMap<Integer, String>();
            Field[] fields = test.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(MapperCell.class)) {
                    MapperCell mapperCell = field.getAnnotation(MapperCell.class);
                    fieldMap.put(mapperCell.cellName(), field);
                    titleMap.put(mapperCell.order(), mapperCell.cellName());
                }
            }
            try { // NOSONAR
                Sheet sheet = workbook.createSheet(this.sheetName);
                Collection<String> values = titleMap.values();
                String[] s = new String[values.size()];
                values.toArray(s);
                // 生成标题行
                Row titleRow = sheet.createRow(0);
                for (int i = 0; i < s.length; i++) {
                    Cell cell = titleRow.createCell(i);
                    cell.setCellValue(s[i]);
                }
                // 生成数据行
                for (int i = 0, length = list.size(); i < length; i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < s.length; j++) {
                        Cell cell = row.createCell(j);
                        for (Map.Entry<String, Field> data : fieldMap.entrySet()) {
                            if (data.getKey().equals(s[j])) {
                                Field field = data.getValue();
                                field.setAccessible(true);
                                cell.setCellValue(field.get(list.get(i)).toString());
                                break;
                            }
                        }
                    }
                }
                File file = new File(this.excelFilePath);
                if (!file.exists() && !file.createNewFile()) {
                    throw new IOException("文件创建失败");
                }
                fileOutputStream = new FileOutputStream(file);
                workbook.write(fileOutputStream);
            } catch (IOException e) {
                LOG.error("流异常", e);
            } catch (IllegalAccessException e) {
                LOG.error("反射异常", e);
            } finally {
                if (null != fileOutputStream) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        LOG.error("关闭流异常", e);
                    }
                }
            }
            result = true;
        }
        return result;
    }

    /**
     * 导出Excel（所有注解字段）
     * 
     * @param response
     * @param fileName 文件名
     * @param list 导出的列表
     * @return
     * @throws IOException
     */
    public static <T> boolean exportExcel(HttpServletResponse response, String fileName, List<T> list) throws IOException {// NOSONAR
        HSSFWorkbook workbook = new HSSFWorkbook();
        boolean result = false;
        OutputStream fileOutputStream = null;
        response.reset();// 清空输出流
        response.setHeader("Content-disposition", "attachment; filename=" + new String((fileName + ".xls").getBytes("gbk"), "ISO8859-1"));
        response.setContentType("application/msexcel;charset=utf-8");
        if (null != list && !list.isEmpty()) {
            T test = list.get(0);
            Map<String, Field> fieldMap = new HashMap<String, Field>();
            Map<Integer, String> titleMap = new TreeMap<Integer, String>();
            Field[] fields = test.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(MapperCell.class)) {
                    MapperCell mapperCell = field.getAnnotation(MapperCell.class);
                    fieldMap.put(mapperCell.cellName(), field);
                    titleMap.put(mapperCell.order(), mapperCell.cellName());
                }
            }
            try {
                Sheet sheet = workbook.createSheet(fileName);
                Collection<String> values = titleMap.values();
                String[] s = new String[values.size()];
                values.toArray(s);
                // 生成标题行
                Row titleRow = sheet.createRow(0);
                for (int i = 0; i < s.length; i++) {
                    Cell cell = titleRow.createCell(i);
                    cell.setCellValue(s[i]);
                }
                // 生成数据行
                for (int i = 0, length = list.size(); i < length; i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < s.length; j++) {
                        Cell cell = row.createCell(j);
                        for (Map.Entry<String, Field> data : fieldMap.entrySet()) {
                            if (data.getKey().equals(s[j])) {
                                Field field = data.getValue();
                                field.setAccessible(true);
                                cell.setCellValue(field.get(list.get(i)) != null ? field.get(list.get(i)).toString() : "");
                                break;
                            }
                        }
                    }
                }
                // 设置列自适应宽度
                for (int i = 0; i < s.length; i++) {
                    sheet.autoSizeColumn(i, true);
                }
                fileOutputStream = response.getOutputStream();
                workbook.write(fileOutputStream);
            } catch (IOException e) {
                LOG.error("流异常", e);
            } catch (IllegalAccessException e) {
                LOG.error("反射异常", e);
            } finally {
                if (null != fileOutputStream) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        LOG.error("关闭流异常", e);
                    }
                }
            }
            result = true;
        }
        return result;
    }

    /**
     * 导出excel
     * 
     * @param response
     * @param fileName 文件名
     * @param titleMap 排序和需要导出的列
     * @param list 列表
     * @return
     * @throws IOException
     */
    public static <T> boolean exportExcel(HttpServletResponse response, String fileName, Map<Integer, String> titleMap, List<T> list)// NOSONAR
                    throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        boolean result = false;
        OutputStream fileOutputStream = null;
        response.reset();// 清空输出流
        response.setHeader("Content-disposition", "attachment; filename=" + new String((fileName + ".xls").getBytes("GB2312"), "ISO8859-1"));
        response.setContentType("application/msexcel");
        if (null != list && !list.isEmpty()) {
            T test = list.get(0);
            Map<String, Field> fieldMap = new HashMap<String, Field>();
            Field[] fields = test.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(MapperCell.class)) {
                    MapperCell mapperCell = field.getAnnotation(MapperCell.class);
                    fieldMap.put(mapperCell.cellName(), field);
                }
            }
            try {
                Sheet sheet = workbook.createSheet(fileName);

                Collection<String> values = titleMap.values();
                String[] s = new String[values.size()];
                values.toArray(s);
                // 生成标题行
                Row titleRow = sheet.createRow(0);
                for (int i = 0; i < s.length; i++) {
                    Cell cell = titleRow.createCell(i);
                    cell.setCellValue(s[i]);
                }
                // 生成数据行
                for (int i = 0, length = list.size(); i < length; i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < s.length; j++) {
                        Cell cell = row.createCell(j);
                        for (Map.Entry<String, Field> data : fieldMap.entrySet()) {
                            if (data.getKey().equals(s[j])) {
                                Field field = data.getValue();
                                field.setAccessible(true);
                                cell.setCellValue(field.get(list.get(i)) != null ? field.get(list.get(i)).toString() : "");
                                break;
                            }
                        }
                    }
                }
                // 设置列自适应宽度
                for (int i = 0; i < s.length; i++) {
                    sheet.autoSizeColumn(i, false);
                }
                fileOutputStream = response.getOutputStream();
                workbook.write(fileOutputStream);
            } catch (IOException e) {
                LOG.error("流异常", e);
            } catch (IllegalAccessException e) {
                LOG.error("反射异常", e);
            } finally {
                if (null != fileOutputStream) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        LOG.error("关闭流异常", e);
                    }
                }
            }
            result = true;
        }
        return result;
    }



    public static <T> boolean dlImportModel(HttpServletResponse response, String fileName, Class<T> clazz) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        boolean result = false;
        OutputStream fileOutputStream = null;
        response.reset();// 清空输出流
        response.setHeader("Content-disposition", "attachment; filename=" + new String((fileName + ".xls").getBytes("GB2312"), "ISO8859-1"));
        response.setContentType("application/msexcel");

        Map<String, Field> fieldMap = new HashMap<String, Field>();
        Map<Integer, String> titleMap = new TreeMap<Integer, String>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(MapperCell.class)) {
                MapperCell mapperCell = field.getAnnotation(MapperCell.class);
                fieldMap.put(mapperCell.cellName(), field);
                titleMap.put(mapperCell.order(), mapperCell.cellName());
            }
        }
        try {
            Sheet sheet = workbook.createSheet(fileName);
            Collection<String> values = titleMap.values();
            String[] s = new String[values.size()];
            values.toArray(s);
            // 生成标题行
            Row titleRow = sheet.createRow(0);
            for (int i = 0; i < s.length; i++) {
                Cell cell = titleRow.createCell(i);
                cell.setCellValue(s[i]);
            }

            // 设置列自适应宽度
            for (int i = 0; i < s.length; i++) {
                sheet.autoSizeColumn(i, true);
            }
            fileOutputStream = response.getOutputStream();
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            LOG.error("流异常", e);
        } finally {
            if (null != fileOutputStream) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    LOG.error("关闭流异常", e);
                }
            }
        }
        result = true;

        return result;
    }


    /**
     * 获取指定单元格的值
     *
     * @param rowNumber 行数，从1开始
     * @param cellNumber 列数，从1开始
     * @return 该单元格的值
     */
    public String getCellValue(int rowNumber, int cellNumber) {
        String result;
        checkRowAndCell(rowNumber, cellNumber);
        Sheet sheet = this.workbook.getSheet(this.sheetName);
        Row row = sheet.getRow(rowNumber - 1);
        Cell cell = row.getCell(cellNumber - 1);
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                result = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                result = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_ERROR:
                result = String.valueOf(cell.getErrorCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                result = cell.getCellFormula();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    result = format.format(cell.getDateCellValue());
                } else {
                    result = String.valueOf(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_STRING:
                result = cell.getRichStringCellValue().getString();
                break;
            default:
                result = cell.getStringCellValue();
                break;
        }
        return result;
    }



    private static void checkRowAndCell(int rowNumber, int cellNumber) {
        if (rowNumber < 1) {
            throw new IllegalArgumentException("rowNumber less than 1");
        }
        if (cellNumber < 1) {
            throw new IllegalArgumentException("cellNumber less than 1");
        }
    }

    @Override
    public void close() throws IOException {
        // 暂无处理逻辑
    }

    /**
     * 将excel生成到内存中（横向）
     *
     * @param fileName
     * @param ts
     * @return
     * @throws IOException
     * @date 2018年1月22日
     */
    public static <T> InputStream exportExcel(String fileName, T... ts) throws IOException {// NOSONAR
        HSSFWorkbook workbook = null;
        ByteArrayOutputStream outputStream = null;
        ByteArrayInputStream inputStream = null;
        if (null != ts) {
            workbook = new HSSFWorkbook();
            T test = ts[0];
            Map<String, Field> fieldMap = new HashMap<String, Field>();
            Map<Integer, String> titleMap = new TreeMap<Integer, String>();
            Field[] fields = test.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(MapperCell.class)) {
                    MapperCell mapperCell = field.getAnnotation(MapperCell.class);
                    fieldMap.put(mapperCell.cellName(), field);
                    titleMap.put(mapperCell.order(), mapperCell.cellName());
                }
            }
            try { // NOSONAR
                Sheet sheet = workbook.createSheet(fileName);
                Collection<String> values = titleMap.values();
                String[] s = new String[values.size()];
                values.toArray(s);
                // 生成标题行
                Row titleRow = sheet.createRow(0);
                for (int i = 0; i < s.length; i++) {
                    Cell cell = titleRow.createCell(i);
                    cell.setCellValue(s[i]);
                }
                // 生成数据行
                for (int i = 0, length = ts.length; i < length; i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < s.length; j++) {
                        Cell cell = row.createCell(j);
                        for (Map.Entry<String, Field> data : fieldMap.entrySet()) {
                            if (data.getKey().equals(s[j])) {
                                Field field = data.getValue();
                                field.setAccessible(true);
                                cell.setCellValue(field.get(ts[i]) != null ? field.get(ts[i]).toString() : "");
                                break;
                            }
                        }
                    }
                }
                // 设置列自适应宽度
                for (int i = 0; i < s.length; i++) {
                    sheet.autoSizeColumn(i, true);
                }
                outputStream = new ByteArrayOutputStream();
                workbook.write(outputStream);

                inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            } catch (IOException e) {
                LOG.error("流异常", e);
            } catch (IllegalAccessException e) {
                LOG.error("反射异常", e);
            } finally {
                if (null != outputStream) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        LOG.error("关闭流异常", e);
                    }
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            }
        }
        return inputStream;
    }

    /**
     * 将excel生成到内存中（纵向）
     *
     * @param fileName
     * @param ts
     * @return
     * @throws IOException
     * @date 2018年1月22日
     */
    public static <T> InputStream exportExcelPortrait(String fileName, T... ts) throws IOException {// NOSONAR
        HSSFWorkbook workbook = null;
        ByteArrayOutputStream outputStream = null;
        ByteArrayInputStream inputStream = null;
        if (null != ts) {
            workbook = new HSSFWorkbook();
            T test = ts[0];
            Map<String, Field> fieldMap = new HashMap<String, Field>();
            Map<Integer, String> titleMap = new TreeMap<Integer, String>();
            Field[] fields = test.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(MapperCell.class)) {
                    MapperCell mapperCell = field.getAnnotation(MapperCell.class);
                    fieldMap.put(mapperCell.cellName(), field);
                    titleMap.put(mapperCell.order(), mapperCell.cellName());
                }
            }
            try { // NOSONAR
                Sheet sheet = workbook.createSheet(fileName);
                Collection<String> values = titleMap.values();
                String[] s = new String[values.size()];
                values.toArray(s);

                for (int i = 0; i < s.length; i++) {
                    // 生成行
                    Row row = sheet.createRow(i);

                    // 生成第一列
                    Cell cell = row.createCell(0);
                    cell.setCellValue(s[i]);

                    String cellValue = "";
                    for (int j = 0, length = ts.length; j < length; j++) {
                        // 生成列
                        Cell cell2 = row.createCell(j + 1);
                        for (Map.Entry<String, Field> data : fieldMap.entrySet()) {
                            if (data.getKey().equals(s[i])) {
                                Field field = data.getValue();
                                field.setAccessible(true);
                                cellValue = field.get(ts[j]) != null ? field.get(ts[j]).toString() : "";
                                break;
                            }
                        }
                        cell2.setCellValue(cellValue);
                    }
                }

                // 设置列自适应宽度
                for (int i = 0; i < s.length; i++) {
                    sheet.autoSizeColumn(i, true);
                }
                outputStream = new ByteArrayOutputStream();
                workbook.write(outputStream);

                inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            } catch (IOException e) {
                LOG.error("流异常", e);
            } catch (IllegalAccessException e) {
                LOG.error("反射异常", e);
            } finally {
                if (null != outputStream) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        LOG.error("关闭流异常", e);
                    }
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            }
        }
        return inputStream;
    }

    /**
     * 按分组导出列
     *
     * @param fileName
     * @param list
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> boolean exportExcel(HttpServletResponse response, String fileName, String group, List<T> list)// NOSONAR
                    throws IOException {
        if (StringUtils.isBlank(fileName) || StringUtils.isBlank(group) || CollectionUtils.isEmpty(list)) {
            return false;
        }

        T t = list.get(0);
        Field[] fields = t.getClass().getDeclaredFields();
        if (fields == null) {
            return false;
        }
        // 排序
        Map<MapperCell, Field> fieldMapper = new TreeMap<>((MapperCell o1, MapperCell o2) -> Integer.compare(o1.order(), o2.order()));

        for (Field field : fields) {
            if (field.isAnnotationPresent(MapperCell.class)) {
                MapperCell mapperCell = field.getAnnotation(MapperCell.class);
                String[] groups = mapperCell.groups();
                // 忽略没有指定分组的字段
                if (groups == null) {
                    continue;
                }
                // 只取指定分组的字段
                List<String> gs = Arrays.asList(groups);
                if (gs.contains(group)) {
                    fieldMapper.put(mapperCell, field);
                }
            }
        }

        boolean result = false;
        String sheetName = FilenameUtils.getBaseName(fileName);
        HSSFWorkbook workbook = new HSSFWorkbook();
        OutputStream fileOutputStream = null;
        response.reset();// 清空输出流
        response.setHeader("Content-disposition", "attachment; filename=" + new String((fileName).getBytes("GB2312"), "ISO8859-1"));
        response.setContentType("application/msexcel");

        try {
            Sheet sheet = workbook.createSheet(sheetName);

            // 生成标题行
            Row titleRow = sheet.createRow(0);
            Set<MapperCell> mapperCells = fieldMapper.keySet();
            int c = 0;
            for (MapperCell mapperCell : mapperCells) {
                Cell cell = titleRow.createCell(c);
                cell.setCellValue(mapperCell.cellName());
                c++;
            }

            // 遍历行
            for (int i = 0, size = list.size(); i < size; i++) {
                Row row = sheet.createRow(i + 1);
                T o = list.get(i);
                int j = 0;
                // 遍历列
                for (MapperCell mapperCell : mapperCells) {
                    Cell cell = row.createCell(j);
                    Field field = fieldMapper.get(mapperCell);
                    field.setAccessible(true);
                    Object v = field.get(o);
                    String val = v != null ? v.toString() : "";
                    // 日期格式化
                    if (field.getGenericType().toString().equals(Date.class.toString())) {
                        val = DateUtils.formatDate((Date) v);
                    }
                    cell.setCellValue(val);
                    j++;
                }
            }

            // 设置列宽。长度乘以2是为了解决纯数字列宽度不足会显示科学计数法问题，乘以256得到的数据才是excel真实列宽。
            int l = 0;
            for (MapperCell mapperCell : mapperCells) {
                sheet.setColumnWidth(l, mapperCell.cellName().getBytes().length * WIDTH_TWO * WIDTH_THREE_HUNDRED);
                l++;
            }
            fileOutputStream = response.getOutputStream();
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            LOG.error("流异常", e);
        } catch (IllegalAccessException e) {
            LOG.error("反射异常", e);
        } finally {
            if (null != fileOutputStream) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    LOG.error("关闭流异常", e);
                }
            }
        }
        result = true;
        return result;
    }

}
