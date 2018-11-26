package com.yunnex.ops.erp.common.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.util.UriUtils;

import com.yunnex.ops.erp.common.config.Global;

/**
 * 查看CK上传的图片
 * @author ThinkGem
 * @version 2014-06-25
 */
public class UserfilesDownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(UserfilesDownloadServlet.class);

	public void fileOutputStream(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String filepath = req.getRequestURI();
		int index = filepath.indexOf(Global.USERFILES_BASE_URL);
		if(index >= 0) {
			filepath = filepath.substring(index + Global.USERFILES_BASE_URL.length());
		}
		try {
			filepath = UriUtils.decode(filepath, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			LOGGER.error(String.format("解释文件路径失败，URL地址为%s", filepath), e1);
		}
		File file = new File(Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL + filepath);
		 FileInputStream fs = new FileInputStream(file);
		try {
			FileCopyUtils.copy(fs, resp.getOutputStream());
			resp.setHeader("Content-Type", "application/octet-stream");
			return;
		} catch (FileNotFoundException e) {
			LOGGER.info(e.getMessage());
			req.setAttribute("exception", new FileNotFoundException("请求的文件不存在"));
			req.getRequestDispatcher("/WEB-INF/views/error/404.jsp").forward(req, resp);
		}
		finally
		{
			if(null!=fs)
			{
				fs.close();
			}
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			fileOutputStream(req, resp);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			fileOutputStream(req, resp);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
	}
}
