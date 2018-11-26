package com.yunnex.ops.erp.modules.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 组织service
 * 
 * @author linqunzhi
 * @date 2018年10月22日
 */
@Service
public class OfficeApiService {

    @Autowired
    private UserApiService officeService;

    /**
     * 同步组织与人员
     *
     * @return
     * @date 2018年10月22日
     * @author linqunzhi
     */
    @Transactional
    public boolean sync() {
        return officeService.sync();
    }

}
