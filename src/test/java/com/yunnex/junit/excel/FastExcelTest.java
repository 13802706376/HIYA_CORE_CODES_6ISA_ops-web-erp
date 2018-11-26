package com.yunnex.junit.excel;

import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.utils.excel.FastExcel;
import com.yunnex.ops.erp.common.utils.excel.ImportExcel;
import com.yunnex.ops.erp.modules.workflow.channel.constant.Constants;
import com.yunnex.ops.erp.modules.workflow.channel.dto.WeiboRechargeResponseDto;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FastExcelTest {

    @Test
    public void exportExcel() throws IOException {
        List<WeiboRechargeResponseDto> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            WeiboRechargeResponseDto dto = new WeiboRechargeResponseDto();
            dto.setSplitOrderNo("123456-" + i);
            dto.setWeiboAccountNo("ad234321" + i);
            dto.setWeiboUid("fwojf9238" + i);
            dto.setShopName("暗淡无光" + i);
            dto.setApplyAmount(32.53 + i);
            dto.setApplyDate(new Date());
            dto.setActualAmount(432.34 + i);
            dto.setFinishDate(new Date());
            list.add(dto);
        }
        FastExcel.exportExcel(new MockHttpServletResponse(), "abc.xls", Constants.STATUS_APPLYING, list);
    }

    @Test
    public void test() {
        try (InputStream is = new FileInputStream("E:/student.xls")) {
            BaseResult result = ImportExcel.readOneColumn(is, "xls", 2, 10, 2);
            System.out.println(result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
