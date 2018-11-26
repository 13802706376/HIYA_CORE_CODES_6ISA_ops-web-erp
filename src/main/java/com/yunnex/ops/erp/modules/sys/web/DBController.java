package com.yunnex.ops.erp.modules.sys.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.util.FileUtil;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.utils.FileUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.sys.entity.FileMage;
import com.yunnex.ops.erp.modules.sys.entity.SqlEntity;
import com.yunnex.ops.erp.modules.sys.service.DBService;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceInfo;

@Controller
@RequestMapping(value = "${adminPath}/sys/dbutil")
public class DBController extends BaseController {

	@Autowired
	DBService dBService;

	@Value("${jdbc.url}")
	private String url;
	
	@Value("${jdbc.username}")
	private String username;
	
	@Value("${jdbc.password}")
	private String password;
	
	@Value("${userfiles.basedir}")
	///home/test/data/apache/opserp/upload/avatar
	private String filePath;
	
	/**
     * 管理基础路径
     */
    @Value("${adminPath}")
    protected String adminPath;

	/**
	 * 保存门店基本资料
	 * 
	 * @throws SQLException
	 * 
	 * @date 2018年4月3日
	 */
	@RequestMapping(value = "excuteSql", method = RequestMethod.POST)
	@ResponseBody
	public BaseResult excuteSql(@RequestBody SqlEntity sqls){
		List<List<Map<String, Object>>> result = dBService.excuteSql(sqls.getSqlParam(),
				sqls.getCurPage() < 0 ? 0 : sqls.getCurPage(), sqls.getPageSize() > 1000 ? 1000 : sqls.getPageSize());
		return new BaseResult(result);
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(String filePath, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(StringUtil.isBlank(filePath)){
			filePath=this.filePath+"upload/avatar";
		}
		File f=new File(filePath);
		List<FileMage> files1=new ArrayList<FileMage>();
		List<FileMage> list=ergodic(f,new ArrayList<String>(),1,files1);
        request.setAttribute("list", list);
        request.setAttribute("filePath", filePath);
        return "modules/sys/fileList";
	}
	
	@RequestMapping(value = "list1", method = RequestMethod.GET)
	@ResponseBody
	public List<FileMage> list1(String filePath) {
		if(StringUtil.isBlank(filePath)){
			filePath=this.filePath+"upload/avatar";
		}
		File f=new File(filePath);
		List<FileMage> files1=new ArrayList<FileMage>();
		List<FileMage> list=ergodic(f,new ArrayList<String>(),1,files1);
        return list;
	}
	
	@RequestMapping(value = "addFile", method = RequestMethod.POST)
	@ResponseBody
	public String addFile(@RequestBody(required = false) MultipartFile file,
			@RequestParam(required = false) String type,
			@RequestParam(required = false) String filePath) throws IOException {
		filePath=filePath.replaceAll("\\*", "\\/");
		if(StringUtil.isBlank(filePath)){
			filePath=this.filePath+"upload/avatar";
		}
		boolean isrun=add(file,filePath);
		if(isrun){
			return "添加成功.";
		}else{
			return "添加失败.";
		}
	}
	
	@RequestMapping(value = "delFile", method = RequestMethod.GET)
	@ResponseBody
	public String aditFile(@RequestParam(required = false, value = "type") String type,
            @RequestParam(required = false, value = "filePath") String filePath) throws IOException {
		filePath=filePath.replaceAll("\\*", "\\/");
		if(StringUtil.isBlank(filePath)){
			filePath=this.filePath+"upload/avatar";
		}
		boolean isrun=delete(filePath);
		if(isrun){
			return "删除成功.";
		}else{
			return "删除失败.";
		}
	}
	
	private static List<FileMage> ergodic(File file,List<String> resultFileName,int lever,List<FileMage> files1){
	    File[] files = file.listFiles();
	    if(files==null){
	    	return files1;// 判断目录下是不是空的
	    }
	    for (File f : files) {
	    	FileMage ff=new FileMage();
        	ff.setFileName(f.getName());
        	ff.setFilePath(f.getPath().replaceAll("\\\\","*"));
        	ff.setPath(f.getPath());
        	ff.setLeve(lever);
        	files1.add(ff);  
	        if(f.isDirectory()){// 判断是否文件夹
	            ergodic(f,resultFileName,lever+1,files1);//递归， 调用自身,查找子目录
	        }
	    }
	    return files1;
	}
	
	public static boolean add(MultipartFile file,String filePath) throws IOException {
		FileUtils.createDirectory(filePath);
		String fileCurPath=filePath+File.separator+file.getOriginalFilename();
		file.transferTo(new File(fileCurPath));
        return true;
    }
	
	public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()){
            	return deleteFile(fileName);
            }else {
            	return deleteDirectory(fileName);
            }
        }
    }
	
	public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator)){
        	dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

	
	public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
