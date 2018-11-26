/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.yunnex.tools;

import java.util.HashMap;
import java.util.Map;

import com.yunnex.ops.erp.common.utils.HttpUtil;

public class HttpclientTest 
{
    public static void main(String[] args)
    {
    	 Map<String,String> map = new HashMap<String,String>();
		 map.put("jsonStr", "3456345634");

		 String fff =  HttpUtil.sendHttpPostReqToServerByParams("http://127.0.0.1:8080/wxapp-web-erp/api/test/qry",map);
		 System.out.println(fff);
    }
}
