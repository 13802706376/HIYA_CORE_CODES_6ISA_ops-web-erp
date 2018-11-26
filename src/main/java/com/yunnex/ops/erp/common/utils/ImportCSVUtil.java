package com.yunnex.ops.erp.common.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.excel.annotation.ExcelField;

public class ImportCSVUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportCSVUtil.class);

    /**
     * 标题行号
     */
    private int dataStartNum;

    /**
     * 读取数据
     */
    private List<String[]> csvDataList;


    /**
     * 构造函数
     * 
     * @param path 导入文件
     * @param dataStartNum 数据开始行索引，0开始
     * @throws InvalidFormatException
     * @throws IOException
     */
    public ImportCSVUtil(String fileName, int dataStartNum) throws InvalidFormatException, IOException {
        this(new File(fileName), dataStartNum);
    }

    /**
     * 构造函数
     * 
     * @param path 导入文件对象
     * @param dataStartNum 数据开始行索引，0开始
     * @throws InvalidFormatException
     * @throws IOException
     */
    public ImportCSVUtil(File file, int dataStartNum) throws InvalidFormatException, IOException {
        this(file.getName(), new FileInputStream(file), dataStartNum);
    }

    /**
     * 构造函数
     * 
     * @param file 导入文件对象
     * @param dataStartNum 数据开始行索引，0开始
     * @throws InvalidFormatException
     * @throws IOException
     */
    public ImportCSVUtil(MultipartFile multipartFile, int dataStartNum) throws InvalidFormatException, IOException {
        this(multipartFile.getOriginalFilename(), multipartFile.getInputStream(), dataStartNum);
    }

    /**
     * 构造函数
     * 
     * @param path 导入文件对象
     * @param dataStartNum 数据开始行索引，0开始
     * @throws IOException
     */
    public ImportCSVUtil(String fileName, InputStream is, int dataStartNum) throws IOException {
        if (StringUtils.isBlank(fileName)) {
            throw new ServiceException("导入文档为空!");
        } else if (!fileName.toLowerCase().endsWith("csv")) {
            throw new ServiceException("文档格式不正确!");
        } else if (is == null) {
            throw new ServiceException("上传文件无有效信息！");
        }
        this.dataStartNum = dataStartNum;
        this.csvDataList = getCSVData(is);
        if (this.dataStartNum > (this.csvDataList.size() - 1)) {
            throw new ServiceException("上传文件无有效信息！");
        }
    }

    public int getDataStartNum() {
        return dataStartNum;
    }

    public void setCsvDataList(List<String[]> csvDataList) {
        this.csvDataList = csvDataList;
    }

    /**
     * 
     * 业务定义：获取CSV文件数据
     * 
     * @date 2018年5月15日
     * @author R/Q
     */
    public static List<String[]> getCSVData(InputStream is) throws IOException {
        DataInputStream in = new DataInputStream(is);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
        String lineStr;
        List<String[]> dataList = Lists.newArrayList();
        while ((lineStr = reader.readLine()) != null) {
            if (StringUtils.isNotBlank(lineStr)) {
                dataList.add(lineStr.split(","));
            }
        }
        return dataList;
    }

    /**
     * 
     * 业务定义：返回转换后的数据集合
     * 
     * @date 2018年5月15日
     * @author R/Q
     */
    public <E> List<E> getDataList(Class<E> cls) throws Exception {// NOSONAR
        JSONArray jsonArray = new JSONArray();
        List<Object[]> annotationList = Lists.newArrayList();
        boolean isCellIndex = false;
        ExcelField objef = cls.getAnnotation(ExcelField.class);
        if (objef.isCellIndex()) {// 添加自主选择导入列判断
            isCellIndex = objef.isCellIndex();
        }
        // 获取字段信息及对应注解信息
        Field[] fs = cls.getDeclaredFields();
        for (Field f : fs) {
            ExcelField ef = f.getAnnotation(ExcelField.class);
            if (ef != null && (ef.type() == 0 || ef.type() == 2)) {
                annotationList.add(new Object[] {ef, f});
            }
        }
        // 获取字段对应get/set方法上的信息及对应注解信息
        Method[] ms = cls.getDeclaredMethods();
        for (Method m : ms) {
            ExcelField ef = m.getAnnotation(ExcelField.class);
            if (ef != null && (ef.type() == 0 || ef.type() == 2)) {
                annotationList.add(new Object[] {ef, m});
            }
        }
        // 按注解sort字段排序
        Collections.sort(annotationList, new Comparator<Object[]>() {// NOSONAR
            public int compare(Object[] o1, Object[] o2) {
                return Integer.compare(((ExcelField) o1[0]).sort(), ((ExcelField) o2[0]).sort());
            }
        });
        List<E> dataList = Lists.newArrayList();
        for (int i = this.dataStartNum; i < this.csvDataList.size(); i++) {// 遍历CSV读取的数据，塞入对象集合
            E e = (E) cls.newInstance();
            int column = 0;
            String[] rowData = this.csvDataList.get(i);
            StringBuilder sb = new StringBuilder();
            for (Object[] os : annotationList) {
                ExcelField ef = (ExcelField) os[0];
                column = isCellIndex ? ef.cellIndex() : column;// 如果为自选列导入，则按注解索引导入列数据，否则轮询导入
                String val = rowData.length > column ? rowData[column] : StringUtils.EMPTY;
                column++;
                if (StringUtils.isNotBlank(val)) {
                    // 获取属性类型
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
                    // 依据字段类型转换属性类型
                    Object objVal = null;
                    try {
                        if (valType == String.class) {
                            String s = String.valueOf(val);
                            if (StringUtils.endsWith(s, ".0")) {
                                objVal = StringUtils.substringBefore(s, ".0");
                            } else {
                                objVal = String.valueOf(val);
                            }
                        } else if (valType == Integer.class) {
                            objVal = this.returnNumericValue(val).intValue();
                        } else if (valType == Long.class) {
                            objVal = this.returnNumericValue(val).longValue();
                        } else if (valType == Double.class) {
                            objVal = this.returnNumericValue(val);
                        } else if (valType == Float.class) {
                            objVal = this.returnNumericValue(val).floatValue();
                        } else if (valType == Date.class) {
                            objVal = DateUtils.parseDate(val);
                        } else {
                            if (ef.fieldType() != Class.class) {
                                objVal = ef.fieldType().getMethod("getValue", String.class).invoke(null, val);
                            } else {
                                objVal = Class.forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(), // NOSONAR
                                                "fieldtype." + valType.getSimpleName() + "Type")).getMethod("getValue", String.class)
                                                .invoke(null, val);
                            }
                        }
                        // 设置对象值
                        if (os[1] instanceof Field) {
                            Reflections.invokeSetter(e, ((Field) os[1]).getName(), objVal);
                        } else if (os[1] instanceof Method) {
                            String mthodName = ((Method) os[1]).getName();
                            if ("get".equals(mthodName.substring(0, 3))) {
                                mthodName = "set" + StringUtils.substringAfter(mthodName, "get");
                            }
                            Reflections.invokeMethod(e, mthodName, new Class[] {valType}, new Object[] {objVal});
                        }
                    } catch (Exception ex) {
                        jsonArray.add("Get cell value [" + i + "," + column + "] error: " + ex.toString());
                        objVal = null;
                    }
                    sb.append(objVal + ", ");
                }
            }
            dataList.add(e);
            LOGGER.debug("Read success: [{}]-{} ", i, sb.toString());// NOSONAR
        }
        if (!jsonArray.isEmpty()) {
            LOGGER.error("excel文件导入错误：{}", jsonArray.toJSONString());// NOSONAR
        }
        return dataList;
    }


    /**
     * 
     * 业务定义：获取数值对象
     * 
     * @date 2018年5月15日
     * @author R/Q
     */
    private Double returnNumericValue(String val) {
        Double returnDou = null;
        if (val.indexOf("%") > -1) {// 百分比获取实际小数，与excel导入获取数据一致
            String douVal = val.replaceAll("%", "");
            returnDou = Double.valueOf(douVal) / 100;
        } else {
            returnDou = Double.valueOf(val);
        }
        return returnDou;

    }

}
