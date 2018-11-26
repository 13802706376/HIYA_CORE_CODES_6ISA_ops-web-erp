/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.yunnex.ops.erp.common.config;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;

import com.ckfinder.connector.ServletContextFactory;
import com.yunnex.ops.erp.common.utils.StringUtils;

/**
 * 全局配置类
 * 
 * @author ThinkGem
 * @version 2014-06-25
 */
public class Global {

    /**
     * 当前对象实例
     */
    private static Global global = new Global();

    /**
     * 显示/隐藏
     */
    public static final String SHOW = "1";
    public static final String HIDE = "0";

    /**
     * 是/否
     */
    public static final String YES = "1";
    public static final String NO = "0";

    /**
     * 对/错
     */
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    /**
     * 上传文件基础虚拟路径
     */
    public static final String USERFILES_BASE_URL = "/upload/";


    private static final Logger LOGGER = LoggerFactory.getLogger(Global.class);

    /**
     * 获取当前对象实例
     */
    public static Global getInstance() {
        return global;
    }

    /**
     * 获取配置
     */
    public static String getConfig(String key) {
        String value = GlobalConfig.get(key);
        return value != null ? value : StringUtils.EMPTY;
    }

    /**
     * 获取管理端根路径
     */
    public static String getAdminPath() {
        return getConfig("adminPath");
    }

    /**
     * 获取前端根路径
     */
    public static String getFrontPath() {
        return getConfig("frontPath");
    }

    /**
     * 获取URL后缀
     */
    public static String getUrlSuffix() {
        return getConfig("urlSuffix");
    }

    /**
     * dwr request.contentpath路径
     */
    public static String getDwrPath() {
        return getConfig("dwrPath");
    }

    /**
     * 前端文件前缀
     */
    public static String getFileUrl() {
        return getConfig("domain.erp.res");
    }

    /**
     * 是否是演示模式，演示模式下不能修改用户、角色、密码、菜单、授权
     */
    public static Boolean isDemoMode() {
        String dm = getConfig("demoMode");
        return "true".equals(dm) || "1".equals(dm);
    }

    /**
     * 在修改系统用户和角色时是否同步到Activiti
     */
    public static Boolean isSynActivitiIndetity() {
        String dm = getConfig("activiti.isSynActivitiIndetity");
        return "true".equals(dm) || "1".equals(dm);
    }

    /**
     * 页面获取常量
     * 
     */
    public static Object getConst(String field) {
        try {
            return Global.class.getField(field).get(null);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            // 异常代表无配置，这里什么也不做
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取上传文件的根目录
     * 
     * @return
     */
    public static String getUserfilesBaseDir() {
        String dir = getConfig("userfiles.basedir");
        if (StringUtils.isBlank(dir)) {
            try {
                dir = ServletContextFactory.getServletContext().getRealPath("/");
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                return "";
            }
        }
        if (!dir.endsWith("/")) {
            dir += "/";
        }
        return dir;
    }

    /**
     * 获取工程路径
     * 
     * @return
     */
    public static String getProjectPath() {
        // 如果配置了工程路径，则直接返回，否则自动获取。
        String projectPath = Global.getConfig("projectPath");
        if (StringUtils.isNotBlank(projectPath)) {
            return projectPath;
        }
        try {
            File file = new DefaultResourceLoader().getResource("").getFile();
            if (file != null) {
                while (true) {
                    File f = new File(file.getPath() + File.separator + "src" + File.separator + "main");
                    if (f.exists()) {
                        break;
                    }
                    if (file.getParentFile() != null) {
                        file = file.getParentFile();
                    } else {
                        break;
                    }
                }
                projectPath = file.toString();
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return projectPath;
    }

    /**
     * 获取资源访问域名
     */
    public static String getResDomain() {
        return getConfig("domain.erp.res");
    }

    public static String getOemDomain() {
        return getConfig("domain.oem");
    }

    /**
     * 最大上传文件大小
     */
    public static String getMaxUploadSize() {
        return getConfig("web.maxUploadSize");
    }

    public static String getResOemDomain() {
        return getConfig("domain.oem.res");
    }

}
