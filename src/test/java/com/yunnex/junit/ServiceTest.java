package com.yunnex.junit;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunnex.ops.erp.common.utils.SpringContextHolder;
import com.yunnex.ops.erp.modules.act.dao.TaskExtDao;
import com.yunnex.ops.erp.modules.act.service.ActDefExtService;
import com.yunnex.ops.erp.modules.act.service.TaskExtService;

public class ServiceTest extends BaseTest
{
	
	@Autowired
	ActDefExtService actDefExtService;
	
	

	@Autowired
	TaskExtService taskExtService;
	
	@Test
     public void test() {
		/*List<ActDefExt> list = actDefExtService.findList(new ActDefExt());
		
		System.out.println(list);
		
		
		TaskExt ext = new TaskExt();
		ext.setTaskId("101");
		ext.setPendingProdFlag("N");
		ext.setStatus("1");
		taskExtService.save(ext);*/
		
		
		TaskExtDao taskExtDao = SpringContextHolder.getBean(TaskExtDao.class);
		taskExtDao.deleteTaskExtsByProcInsId("44444");
		
		
	}

}