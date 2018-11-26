package com.yunnex.junit;

import java.io.FileNotFoundException;

import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Log4jConfigurer;

/**
 * 自定义该类是为了加载指定位置的日志文件
 */
public class Junit4ClassRunner extends SpringJUnit4ClassRunner {

    static {
        try {
            Log4jConfigurer.initLogging("classpath:config/common/log4j.properties");
        } catch (FileNotFoundException e) {
            // 加载日志前无法使用日志记录！！
            e.printStackTrace();
        }
    }

    public Junit4ClassRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

}
