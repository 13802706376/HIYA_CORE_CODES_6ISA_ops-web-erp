package com.yunnex.ops.erp.modules.material.service;

import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunnex.junit.BaseTest;

public class ErpOrderMaterialContentServiceTest extends BaseTest {

    @Autowired
    private ErpOrderMaterialContentService orderMaterialContentService;

    @Test
    public void downloadZip() throws IOException {
        try (FileOutputStream fos = new FileOutputStream("D:/material.zip")) {
            orderMaterialContentService.compressOrderMaterials("201807160003", fos);
        }
    }

}
