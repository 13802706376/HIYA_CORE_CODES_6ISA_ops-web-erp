package com.yunnex.ops.erp.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MD5加密工具类
 * 
 * @author SunQ
 * @date 2017年12月15日
 */
public final class MD5Util {

    private static final Logger LOGGER = LoggerFactory.getLogger(MD5Util.class);

    private static final char[] hexDigits = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static final int DEFAULT_NUMBER_2 = 2;
    
    private static final int DEFAULT_NUMBER_4 = 4;
    
    private static final int DEFAULT_NUMBER_15 = 15;
    
    private MD5Util() {
        throw new IllegalStateException("Utility class");
    }

    public static final String md5(String s) {
        return md5(s.getBytes());
    }

    public static final String md5(byte[] bytes) {
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(bytes);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * DEFAULT_NUMBER_2];
            int k = 0;

            for (int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> DEFAULT_NUMBER_4 & DEFAULT_NUMBER_15];
                str[k++] = hexDigits[byte0 & DEFAULT_NUMBER_15];
            }

            return new String(str);
        } catch (NoSuchAlgorithmException | RuntimeException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }
}
