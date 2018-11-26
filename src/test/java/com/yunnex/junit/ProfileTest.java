package com.yunnex.junit;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import com.yunnex.ops.erp.common.config.Global;

public class ProfileTest extends BaseTest {

    @Value("${productName}")
    private String productName;
    @Value("${jdbc.url}")
    private String jdbcUrl;
    @Value("${cas.server.url}")
    private String casServerUrl;
    @Value("${sync_agent_info_scheduler_cron}")
    private String sync_agent_info_scheduler_cron;
    @Value("${domain.erp.res}")
    private String domainErpRes;

    @Value("${api_agent_info_url}")
    private String api_agent_info_url;
    @Value("${api_shop_password_url}")
    private String api_shop_password_url;
    @Value("${api_storeaudit_url}")
    private String api_storeaudit_url;
    @Value("${api_wxpayaudit_url}")
    private String api_wxpayaudit_url;
    @Value("${api_agent_url}")
    private String api_agent_url;
    @Value("${api_agent_password_modify_url}")
    private String api_agent_password_modify_url;
    @Value("${api_good_info_url}")
    private String api_good_info_url;
    @Value("${api_order_info_url}")
    private String api_order_info_url;

    @Test
    public void test() {
        System.out.println(productName);
        System.out.println(jdbcUrl);
        System.out.println(casServerUrl);
        System.out.println(sync_agent_info_scheduler_cron);
        System.out.println(domainErpRes);
    }

    @Test
    public void test1() {
        System.out.println(Global.getAdminPath());
        System.out.println(Global.getUserfilesBaseDir());
        System.out.println(Global.getConfig("api_agent_url"));
        System.out.println(Global.getConfig("domain.erp.res"));
        System.out.println(Global.getMaxUploadSize());
        System.out.println(Global.getOemDomain());
        System.out.println(Global.getResOemDomain());
        System.out.println(Global.getProjectPath());
        System.out.println(Global.getOemDomain());
    }

    @Test
    public void test2() {
        System.out.println(api_agent_info_url);
        System.out.println(api_shop_password_url);
        System.out.println(api_storeaudit_url);
        System.out.println(api_wxpayaudit_url);
        System.out.println(api_agent_url);
        System.out.println(api_agent_password_modify_url);
        System.out.println(api_good_info_url);
        System.out.println(api_order_info_url);
    }

}
