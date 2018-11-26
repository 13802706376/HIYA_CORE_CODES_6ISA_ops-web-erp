package com.yunnex.ops.erp.modules.sys.utils;

import java.util.Collection;

import javax.servlet.ServletContext;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ServerContext;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.proxy.dwr.Util;
import org.springframework.stereotype.Service;

import com.yunnex.ops.erp.common.config.Global;


@Service
public class DwrUtils {
	private ServletContext servletContext = null;//NOSONAR

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public void dwr(String title, String content, String url,String userid) {
		ServerContext ctx = ServerContextFactory.get(servletContext);// 未经过前端开启DWR，通过此获取链接。
		Collection<ScriptSession> sessions = ctx.getScriptSessionsByPage(Global.getDwrPath() + "/admin");
		Util util = new Util(sessions);
		ScriptBuffer sb = new ScriptBuffer();
		sb.appendScript("show(");
		sb.appendData(title);
		sb.appendScript(",");
		sb.appendData(content);
		sb.appendScript(",");
		sb.appendData(url);
		sb.appendScript(",");
		sb.appendData(userid);
		sb.appendScript(")");
		util.addScript(sb);
	}
	   public void dwr(String userid) {
	        ServerContext ctx = ServerContextFactory.get(servletContext);// 未经过前端开启DWR，通过此获取链接。
	        Collection<ScriptSession> sessions = ctx.getScriptSessionsByPage(Global.getDwrPath() + "/admin");
	        Util util = new Util(sessions);
	        ScriptBuffer sb = new ScriptBuffer();
	        sb.appendScript("show(");
	        sb.appendData("你有一个新任务");
	        sb.appendScript(",");
	        sb.appendData("指派给你一个新的订单，请处理");
	        sb.appendScript(",");
	        sb.appendData(Global.getDwrPath() +Global.getAdminPath()+"/workflow/tasklist");
	        sb.appendScript(",");
	        sb.appendData(userid);
	        sb.appendScript(")");
	        util.addScript(sb);
	    }


}
