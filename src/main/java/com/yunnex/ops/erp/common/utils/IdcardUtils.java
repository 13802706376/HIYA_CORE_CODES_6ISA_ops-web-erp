package com.yunnex.ops.erp.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 身份证工具类
 * 
 * @author June
 * @version 1.0, 2010-06-17
 */
public class IdcardUtils extends StringUtils {
    private static final Logger LOGGER=LoggerFactory.getLogger(CookieUtils.class);

    /** 中国公民身份证号码最小长度。 */
    public static final int CHINA_ID_MIN_LENGTH = 15;

    /** 中国公民身份证号码最大长度。 */
    public static final int CHINA_ID_MAX_LENGTH = 18;

    /** 省、直辖市代码表 */
    public static final String cityCode[] = { "11", "12", "13", "14", "15",
            "21", "22", "23", "31", "32", "33", "34", "35", "36", "37", "41",
            "42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61",
            "62", "63", "64", "65", "71", "81", "82", "91" };

    /** 每位加权因子 */
    public static final int power[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9,
            10, 5, 8, 4, 2 };

	/** 第18位校检码 */
	public static final String verifyCode[] = { "1", "0", "X", "9", "8", "7",
			"6", "5", "4", "3", "2" };
	/** 最低年限 */
	public static final int MIN = 1930;
	public static Map<String, String> cityCodes = new HashMap<String, String>();//NOSONAR
	/** 台湾身份首字母对应数字 */
	public static Map<String, Integer> twFirstCode = new HashMap<String, Integer>();//NOSONAR
	/** 香港身份首字母对应数字 */
	public static Map<String, Integer> hkFirstCode = new HashMap<String, Integer>();//NOSONAR
	static {
		cityCodes.put("11", "北京");
		cityCodes.put("12", "天津");
		cityCodes.put("13", "河北");
		cityCodes.put("14", "山西");
		cityCodes.put("15", "内蒙古");
		cityCodes.put("21", "辽宁");
		cityCodes.put("22", "吉林");
		cityCodes.put("23", "黑龙江");
		cityCodes.put("31", "上海");
		cityCodes.put("32", "江苏");
		cityCodes.put("33", "浙江");
		cityCodes.put("34", "安徽");
		cityCodes.put("35", "福建");
		cityCodes.put("36", "江西");
		cityCodes.put("37", "山东");
		cityCodes.put("41", "河南");
		cityCodes.put("42", "湖北");
		cityCodes.put("43", "湖南");
		cityCodes.put("44", "广东");
		cityCodes.put("45", "广西");
		cityCodes.put("46", "海南");
		cityCodes.put("50", "重庆");
		cityCodes.put("51", "四川");
		cityCodes.put("52", "贵州");
		cityCodes.put("53", "云南");
		cityCodes.put("54", "西藏");
		cityCodes.put("61", "陕西");
		cityCodes.put("62", "甘肃");
		cityCodes.put("63", "青海");
		cityCodes.put("64", "宁夏");
		cityCodes.put("65", "新疆");
		cityCodes.put("71", "台湾");
		cityCodes.put("81", "香港");
		cityCodes.put("82", "澳门");
		cityCodes.put("91", "国外");
		twFirstCode.put("A", 10);
		twFirstCode.put("B", 11);
		twFirstCode.put("C", 12);
		twFirstCode.put("D", 13);
		twFirstCode.put("E", 14);
		twFirstCode.put("F", 15);
		twFirstCode.put("G", 16);
		twFirstCode.put("H", 17);
		twFirstCode.put("J", 18);
		twFirstCode.put("K", 19);
		twFirstCode.put("L", 20);
		twFirstCode.put("M", 21);
		twFirstCode.put("N", 22);
		twFirstCode.put("P", 23);
		twFirstCode.put("Q", 24);
		twFirstCode.put("R", 25);
		twFirstCode.put("S", 26);
		twFirstCode.put("T", 27);
		twFirstCode.put("U", 28);
		twFirstCode.put("V", 29);
		twFirstCode.put("X", 30);
		twFirstCode.put("Y", 31);
		twFirstCode.put("W", 32);
		twFirstCode.put("Z", 33);
		twFirstCode.put("I", 34);
		twFirstCode.put("O", 35);
		hkFirstCode.put("A", 1);
		hkFirstCode.put("B", 2);
		hkFirstCode.put("C", 3);
		hkFirstCode.put("R", 18);
		hkFirstCode.put("U", 21);
		hkFirstCode.put("Z", 26);
		hkFirstCode.put("X", 24);
		hkFirstCode.put("W", 23);
		hkFirstCode.put("O", 15);
		hkFirstCode.put("N", 14);
	}
}
