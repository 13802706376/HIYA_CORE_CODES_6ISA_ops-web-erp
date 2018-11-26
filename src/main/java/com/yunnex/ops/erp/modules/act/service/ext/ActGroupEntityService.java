/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yunnex.ops.erp.modules.act.service.ext;

import java.util.List;
import java.util.Map;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.SpringContextHolder;
import com.yunnex.ops.erp.modules.act.utils.ActUtils;
import com.yunnex.ops.erp.modules.sys.entity.Role;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.service.SystemService;

/**
 * Activiti Group Entity Service
 * @author ThinkGem
 * @version 2013-12-05
 */
@Service
public class ActGroupEntityService extends GroupEntityManager {

	private SystemService systemService;

	public SystemService getSystemService() {
		if (systemService == null){
			systemService = SpringContextHolder.getBean(SystemService.class);
		}
		return systemService;
	}
	
	@Override
	public Group createNewGroup(String groupId) {
		return new GroupEntity(groupId);
	}

    @Override
	public void insertGroup(Group group) {
		throw new ServiceException("not implement method.");
	}

	public void updateGroup(GroupEntity updatedGroup) {
		throw new ServiceException("not implement method.");
	}

	@Override
	public void deleteGroup(String groupId) {
		throw new ServiceException("not implement method.");
	}

	@Override
	public GroupQuery createNewGroupQuery() {
		throw new ServiceException("not implement method.");
	}

	@Override
	public List<Group> findGroupByQueryCriteria(GroupQueryImpl query, Page page) {
		throw new ServiceException("not implement method.");
	}

	@Override
	public long findGroupCountByQueryCriteria(GroupQueryImpl query) {
		throw new ServiceException("not implement method.");
	}

    /**
     * 根据用户ID找到对应的组
     */
	@Override
	public List<Group> findGroupsByUser(String userId) {
		List<Group> list = Lists.newArrayList();
		User user = getSystemService().getUserByLoginName(userId);
		if (user != null && user.getRoleList() != null){
			for (Role role : user.getRoleList()){
				list.add(ActUtils.toActivitiGroup(role));
			}
		}
		return list;
	}

	@Override
	public List<Group> findGroupsByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
		throw new ServiceException("not implement method.");
	}

	@Override
	public long findGroupCountByNativeQuery(Map<String, Object> parameterMap) {
		throw new ServiceException("not implement method.");
	}

}