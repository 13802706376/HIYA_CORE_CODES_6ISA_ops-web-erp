package com.yunnex.ops.erp.modules.sys.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunnex.junit.BaseTest;

public class OfficeApiServiceTest extends BaseTest {

    @Autowired
    private OfficeApiService officeApiService;

    @Test
    public void sync() {
        officeApiService.sync();
    }
}
