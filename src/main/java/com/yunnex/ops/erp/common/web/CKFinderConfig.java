/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yunnex.ops.erp.common.web;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;

import com.ckfinder.connector.configuration.Configuration;
import com.ckfinder.connector.data.AccessControlLevel;
import com.ckfinder.connector.utils.AccessControlUtil;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.utils.FileUtils;
import com.yunnex.ops.erp.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;

/**
 * CKFinder配置
 * @author ThinkGem
 * @version 2014-06-25
 */
public class CKFinderConfig extends Configuration {

    public CKFinderConfig(ServletConfig servletConfig) {
        super(servletConfig);  
    }

    @Override
    protected Configuration createConfigurationInstance() {
        Principal principal = UserUtils.getPrincipal();
        if (principal == null) {
            return new CKFinderConfig(this.servletConf);
        }
        boolean isView = true;
        boolean isUpload = true;
        boolean isEdit = true;
        AccessControlLevel alc = this.getAccessConrolLevels().get(0);
        alc.setFolderView(isView);
        alc.setFolderCreate(isEdit);
        alc.setFolderRename(isEdit);
        alc.setFolderDelete(isEdit);
        alc.setFileView(isView);
        alc.setFileUpload(isUpload);
        alc.setFileRename(isEdit);
        alc.setFileDelete(isEdit);
        AccessControlUtil.getInstance(this).loadACLConfig();
        try {
            this.baseURL = FileUtils.path(Servlets.getRequest().getContextPath() + Global.USERFILES_BASE_URL + principal + "/");
            this.baseDir = FileUtils.path(Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL + principal + "/");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new CKFinderConfig(this.servletConf);
    }

    @Override  
    public boolean checkAuthentication(final HttpServletRequest request) {
        return UserUtils.getPrincipal()!=null;
    }

}
