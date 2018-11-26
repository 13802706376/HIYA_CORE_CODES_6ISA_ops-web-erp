package com.yunnex.ops.erp.modules.sys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.sys.constant.SysConstant;
import com.yunnex.ops.erp.modules.sys.entity.Area;
import com.yunnex.ops.erp.modules.sys.entity.Office;
import com.yunnex.ops.erp.modules.sys.entity.Role;
import com.yunnex.ops.erp.modules.sys.entity.SysOfficeUser;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.ldap.LdapApiService;

/**
 * 从LDAP同步公司员工
 */
@Service
public class UserApiService {

    @Autowired
    private LdapApiService ldapApiService;

    @Autowired
    private OfficeService officeService;

    @Autowired
    private UserService userService;

    @Autowired
    private SysOfficeUserService officeUserService;

    @Value("${default_user_role_id}")
    private String DEFAULT_USER_ROLE_ID;

    @Value("${default_user_password}")
    private String DEFAULT_USER_PASSWORD;

    /** 根组织查询路径值 */
    private static final String ROOT_OFFICE_SEARCH_BASE = "ou=weixin,ou=Group,dc=yunnex,dc=com";
    /** 根组织的parentId值 */
    private static final String ROOT_PARENT_ID = "0";
    /** 根用户查询路径值 */
    private static final String ROOT_USER_SEARCH_BASE = "ou=weixin,ou=User,dc=yunnex,dc=com";
    /** 指定的组织部门 */
    private static final String OFFICE_FILTER_BASE = "cn=云移科技";
    /** 组织类型 */
    private static final String OFFICE_TYPE = "ou=Group,dc=yunnex,dc=com";
    /** 组织返回值集合 */
    private static final String[] OFFICE_RETURNING_ATTRIBUTES = {"member", "cn"};
    /** 用户返回值集合 */
    private static final String[] USER_RETURNING_ATTRIBUTES = {"cn", "displayName", "mail", "uid", "mobile"};



    private static Logger logger = LoggerFactory.getLogger(UserApiService.class);

    @Transactional(readOnly = false)
    public boolean sync() {
        logger.info("同步组织与人员。。。start");
        long start=System.currentTimeMillis();
        // 获取ldap
        LdapContext ldapContext = ldapApiService.getLdapContext();
        // 获取Ldap所有的组织结构 和 用户与组织的关系 用户集合
        LdapSyncResult ldapSyncResult = getLdapSyncResult(ldapContext);
        // 整合所有数据，进行处理
        managerData(ldapSyncResult);
        long diffTime = (System.currentTimeMillis() - start)/1000;
        logger.info("同步组织与人员。。。。end |耗时 ：{} 秒", diffTime);
        return true;
    }


    /**
     * 获取ldap所有用户数据
     *
     * @param ldapContext
     * @return
     * @date 2018年10月18日
     * @author linqunzhi
     */
    private Map<String, User> getLdapUserMap(LdapContext ldapContext) {
        Map<String, User> result = new HashMap<>();
        User user = null;
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(USER_RETURNING_ATTRIBUTES);
        try {
            NamingEnumeration<SearchResult> answer = ldapContext.search(ROOT_USER_SEARCH_BASE, "(objectClass=person)", searchControls);
            while (answer.hasMore()) {
                SearchResult searchResult = answer.next();
                NamingEnumeration<? extends Attribute> attrs = searchResult.getAttributes().getAll();
                user = new User();
                // 组装user对象
                while (attrs.hasMore()) {
                    Attribute attr = attrs.next();
                    if ("cn".equals(attr.getID())) {
                        user.setLoginName(attr.get().toString());
                        // 将user放入map中
                        result.put(user.getLoginName(), user);
                    } else if ("displayName".equals(attr.getID())) {
                        user.setName(attr.get().toString());
                    } else if ("mail".equals(attr.getID())) {
                        user.setEmail(attr.get().toString());
                    } else if ("uid".equals(attr.getID())) {
                        user.setNo(attr.get().toString());
                    } else if ("mobile".equals(attr.getID())) {
                        user.setMobile(attr.get().toString());
                    }
                }
            }
        } catch (NamingException e) {
            logger.error("同步组织人员异常：{}", e);
            throw new ServiceException("同步组织人员异常");
        }
        return result;
    }


    /**
     * 根据ladp 和 数据库数据 进行处理
     * 
     *
     * @param ldapSyncResult
     * @param ldapUserMap
     * @date 2018年10月19日
     * @author linqunzhi
     */
    private void managerData(LdapSyncResult ldapSyncResult) {
        // 用户数据处理
        Map<String, User> finallyUserMap = managerUserData(ldapSyncResult.getUserResult());
        // 组织数据处理
        Map<String, Office> finallyOfficeMap = mangerOfficeData(ldapSyncResult.getOfficeResult());
        // 用户与组织关系处理
        managerOfficeUserData(finallyOfficeMap, finallyUserMap, ldapSyncResult.getOfficeUserResult());
    }


    private void managerOfficeUserData(Map<String, Office> finallyOfficeMap, Map<String, User> finallyUserMap,
                    Map<String, SysOfficeUser> officeUserResult) {
        // 数据库所有 用户与组织的关系数据
        Map<String, SysOfficeUser> officeUserDbMap = getAllDbOfficeUserMap();
        // ldap无数据，进行删除db数据
        for (Entry<String, SysOfficeUser> entry : officeUserDbMap.entrySet()) {
            String key = entry.getKey();
            if (officeUserDbMap.get(key) == null) {
                officeUserService.delete(entry.getValue());
            }
        }

        // ldap有数据，db无，进行新增数据
        for (Entry<String, SysOfficeUser> entry : officeUserResult.entrySet()) {
            String key = entry.getKey();
            if (officeUserDbMap.get(key) == null) {
                addOfficeUser(entry.getValue(), finallyOfficeMap, finallyUserMap);
            }
        }

    }


    private void addOfficeUser(SysOfficeUser ldapOfficeUser, Map<String, Office> finallyOfficeMap, Map<String, User> finallyUserMap) {
        String userCn = ldapOfficeUser.getUserCn();
        String officeDn = ldapOfficeUser.getOfficeDn();
        String userId = finallyUserMap.get(userCn).getId();
        String officeId = finallyOfficeMap.get(officeDn).getId();
        SysOfficeUser obj = new SysOfficeUser();
        obj.setOffice(new Office(officeId));
        obj.setUser(new User(userId));
        obj.setUserCn(userCn);
        obj.setOfficeDn(officeDn);
        officeUserService.save(obj);
    }


    private Map<String, SysOfficeUser> getAllDbOfficeUserMap() {
        Map<String, SysOfficeUser> result = new HashMap<>();
        List<SysOfficeUser> list = officeUserService.findList(new SysOfficeUser());
        if (CollectionUtils.isNotEmpty(list)) {
            for (SysOfficeUser obj : list) {
                String key = obj.getUserCn() + CommonConstants.Sign.DUN_HAO + obj.getOfficeDn();
                result.put(key, obj);
            }
        }
        return result;
    }


    private Map<String, Office> mangerOfficeData(Map<String, Office> ldapOfficeResult) {
        Map<String, Office> result = new HashMap<>();
        Map<String, Office> dbMap = getAllDbOfficeMap();
        Office dbOffice = null;
        for (Entry<String, Office> entry : dbMap.entrySet()) {
            dbOffice = entry.getValue();
            String dn = dbOffice.getLdapDn();
            // 如果ldap无数据，进行逻辑删除
            if (ldapOfficeResult.get(dn) == null) {
                officeService.delete(dbOffice);;
            }
        }
        Office ldapOffice = null;
        for (Entry<String, Office> entry : ldapOfficeResult.entrySet()) {
            ldapOffice = entry.getValue();
            dbOffice = dbMap.get(ldapOffice.getLdapDn());
            if (dbOffice == null) {
                // 新增部门信息
                dbOffice = addOffice(ldapOffice);
            }
        }
        // 再次查询数据库修改
        dbMap = getAllDbOfficeMap();
        if (dbMap.size() != ldapOfficeResult.size()) {
            logger.error("组织 ：ldap 和 数据库 数量不一致");
            throw new ServiceException(CommonConstants.FailMsg.DATA);
        }
        for (Entry<String, Office> entry : ldapOfficeResult.entrySet()) {
            ldapOffice = entry.getValue();
            dbOffice = updateOffice(ldapOffice, dbMap);
            result.put(dbOffice.getLdapDn(), dbOffice);
        }
        return result;
    }

    private Office updateOffice(Office ldapOffice, Map<String, Office> dbMap) {
        Office dbOffice = dbMap.get(ldapOffice.getLdapDn());
        String parentId = getIdByDn(ldapOffice.getParent().getLdapDn(), dbMap);
        String parentDns = ldapOffice.getParentIds();
        StringBuilder parentIdsBuilder = new StringBuilder();
        for (String dn : parentDns.split(CommonConstants.Sign.DUN_HAO)) {
            String id = getIdByDn(dn, dbMap);
            parentIdsBuilder.append(id).append(CommonConstants.Sign.COMMA);
        }
        String parentIds = parentIdsBuilder.toString();
        // 如果parentId值不同 或者 name 不同，则进行修改
        if (!parentIds.equals(parentIds) || stringEquals(ldapOffice.getName(), dbOffice.getName())) {
            dbOffice = updateOffice(dbOffice.getId(), parentId, parentIds, ldapOffice);

        }
        return dbOffice;
    }


    private Office updateOffice(String id, String parentId, String parentIds, Office ldapOffice) {
        Office dbOffice = new Office(id);
        dbOffice.setLdapDn(ldapOffice.getLdapDn());
        dbOffice.setParent(new Office(parentId));
        dbOffice.setParentIds(parentIds);
        dbOffice.setName(ldapOffice.getName());
        dbOffice.setUseable(Global.YES);
        String grade = "0";
        if (StringUtils.isNotBlank(parentIds)) {
            grade = String.valueOf(parentIds.split(CommonConstants.Sign.COMMA).length - 1);
        }
        dbOffice.setGrade(grade);
        String type = "0".equals(grade) ? "1" : "2";
        dbOffice.setType(type);
        officeService.saveOrUpdate(dbOffice);
        return dbOffice;
    }


    private String getIdByDn(String dn, Map<String, Office> dbMap) {
        String result = ROOT_PARENT_ID;
        Office obj = dbMap.get(dn);
        if (obj != null) {
            result = obj.getId();
        }
        return result;
    }


    private Map<String, Office> getAllDbOfficeMap() {
        Map<String, Office> result = new HashMap<>();
        List<Office> newDbList = officeService.findAllList(new Office());
        if (CollectionUtils.isNotEmpty(newDbList)) {
            for (Office office : newDbList) {
                result.put(office.getLdapDn(), office);
            }
        }
        return result;
    }


    private Office addOffice(Office ldapOffice) {
        String dn = ldapOffice.getLdapDn();
        String parentIds = ldapOffice.getParentIds();
        String grade = "0";
        if (StringUtils.isNotBlank(parentIds)) {
            grade = String.valueOf(parentIds.split(CommonConstants.Sign.DUN_HAO).length - 1);
        }
        Office office = new Office();
        office.setLdapDn(dn);
        office.setCode("");
        office.setName(ldapOffice.getName());
        office.setGrade(grade);
        office.setUseable(Global.YES);
        office.setParent(ldapOffice.getParent());
        office.setParentIds(parentIds);
        String type = "0".equals(grade) ? "1" : "2";
        office.setType(type);
        office.setArea(new Area("2"));
        officeService.saveOrUpdate(office);
        return office;
    }


    private Map<String, User> managerUserData(Map<String, User> ldapUserResult) {
        Map<String, User> result = new HashMap<>();
        Map<String, User> dbUserMap = getAllDbUserMap();
        User dbUser = null;
        for (Entry<String, User> entry : dbUserMap.entrySet()) {
            dbUser = entry.getValue();
            String loginName = dbUser.getLoginName();
            // 如果ldap无数据，进行逻辑删除
            if (ldapUserResult.get(loginName) == null) {
                userService.delete(dbUser);
            }
        }

        User ldapUser = null;
        for (Entry<String, User> entry : ldapUserResult.entrySet()) {
            ldapUser = entry.getValue();
            dbUser = dbUserMap.get(ldapUser.getLoginName());
            // ldap 有数据，数据库没数据，进行新增
            if (dbUser == null) {
                dbUser = addUser(ldapUser);
            } else if (!equalsUser(ldapUser, dbUser)) {
                // 如果ladp与数据库中user数据不一致，则进行修改
                dbUser = updateUser(ldapUser, dbUser.getId());
            }
            result.put(dbUser.getLoginName(), dbUser);
        }
        return result;
    }


    private Map<String, User> getAllDbUserMap() {
        Map<String, User> result = new HashMap<>();
        // 数据库所有erp用户
        User userParam = new User();
        userParam.setType(SysConstant.TYPE_ERP);
        List<User> userDbList = userService.findAllList(userParam);
        if (CollectionUtils.isNotEmpty(userDbList)) {
            for (User user : userDbList) {
                // 排除系统管理员
                if (!"1".equals(user.getId())) {
                    result.put(user.getLoginName(), user);
                }
            }
        }
        return result;
    }


    /**
     * 修改用户数据
     *
     * @param ldapUser
     * @param id
     * @return
     * @date 2018年10月19日
     * @author linqunzhi
     */
    private User updateUser(User ldapUser, String id) {
        User result = new User();
        result.setId(id);
        result.setLoginName(ldapUser.getLoginName());
        result.setName(ldapUser.getName());
        result.setEmail(ldapUser.getEmail());
        result.setNo(ldapUser.getNo());
        result.setMobile(ldapUser.getMobile());
        userService.save(result);
        return result;
    }


    /**
     * 判断user是否一样
     *
     * @param ldapUser
     * @param dbUser
     * @return
     * @date 2018年10月19日
     * @author linqunzhi
     */
    private boolean equalsUser(User ldapUser, User dbUser) {
        boolean nameEquals = stringEquals(ldapUser.getName(), dbUser.getName());
        boolean emailEquals = stringEquals(ldapUser.getEmail(), dbUser.getEmail());
        boolean noEquals = stringEquals(ldapUser.getNo(), dbUser.getNo());
        boolean mobileEquals = stringEquals(ldapUser.getMobile(), dbUser.getMobile());
        return nameEquals && emailEquals && noEquals && mobileEquals;
    }


    /**
     * 字符串比较是否一样
     *
     * @param ldapName
     * @param dbName
     * @return
     * @date 2018年10月19日
     * @author linqunzhi
     */
    private boolean stringEquals(String ldapName, String dbName) {
        return (StringUtils.isEmpty(ldapName) && StringUtils.isEmpty(dbName)) || (ldapName != null && ldapName.equals(dbName));
    }


    private User addUser(User ldapUser) {
        User user = new User();
        List<Role> roleList = new ArrayList<Role>();
        roleList.add(new Role(DEFAULT_USER_ROLE_ID));
        user.setRoleList(roleList);
        user.setCompany(new Office("1"));
        user.setUserType("3");
        user.setLoginFlag(Global.YES);
        user.setRemarks("Ldap导入");
        user.setPassword(SystemService.entryptPassword(DEFAULT_USER_PASSWORD));
        // 复制 ldapUser数据
        user.setLoginName(ldapUser.getLoginName());
        user.setName(ldapUser.getName());
        user.setEmail(ldapUser.getEmail());
        user.setMobile(ldapUser.getMobile());
        user.setNo(ldapUser.getNo());
        // 新增user
        userService.save(user);
        return user;
    }



    private LdapSyncResult getLdapSyncResult(LdapContext ldapContext) {
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(OFFICE_RETURNING_ATTRIBUTES);
        Office parentOffice = new Office();
        parentOffice.setLdapDn(ROOT_OFFICE_SEARCH_BASE);
        parentOffice.setParentIds(CommonConstants.Sign.EMPTY_STRING);
        Office office = new Office();
        office.setLdapDn(OFFICE_FILTER_BASE + "," + ROOT_OFFICE_SEARCH_BASE);
        office.setParent(parentOffice);
        office.setParentIds(ROOT_OFFICE_SEARCH_BASE);
        // 获取ldap 所有用户数据
        Map<String, User> ldapUserMap = getLdapUserMap(ldapContext);
        LdapSyncResult result = new LdapSyncResult();
        try {
            // 获取ldap数据
            ldapDataResult(result, ldapContext, OFFICE_FILTER_BASE, searchControls, office, ldapUserMap);
        } catch (NamingException e) {
            logger.error("同步组织人员异常：{}", e);
            throw new ServiceException("同步组织人员异常");
        }
        return result;
    }


    private void ldapDataResult(LdapSyncResult result, LdapContext ldapContext, String filter, SearchControls searchControls, Office office,
                    Map<String, User> ldapUserMap) throws NamingException {
        NamingEnumeration<SearchResult> answer = ldapContext.search(ROOT_OFFICE_SEARCH_BASE, filter, searchControls);
        while (answer.hasMore()) {
            SearchResult searchResult = answer.next();
            // 获取组织名称
            Attribute officeCn = searchResult.getAttributes().get("cn");
            String name = officeCn.get(0).toString();
            office.setName(name);
            // 加入组织集合
            result.getOfficeResult().put(office.getLdapDn(), office);
            Attribute officeMember = searchResult.getAttributes().get("member");;
            NamingEnumeration<?> entryList = officeMember.getAll();
            while (entryList.hasMore()) {
                String entryStr = entryList.next().toString();
                // 如果是 组织就递归查询
                if (entryStr.contains(OFFICE_TYPE)) {
                    Office lastOffice = new Office();
                    lastOffice.setLdapDn(entryStr);
                    lastOffice.setParent(office);
                    String pids = office.getParentIds() + CommonConstants.Sign.DUN_HAO + office.getLdapDn();
                    lastOffice.setParentIds(pids);
                    String lastFilter = entryStr.split(CommonConstants.Sign.COMMA)[0];
                    ldapDataResult(result, ldapContext, lastFilter, searchControls, lastOffice, ldapUserMap);
                } else {
                    // 用户处理
                    User user = new User();
                    String loginName = entryStr.split(CommonConstants.Sign.COMMA)[0].split(CommonConstants.Sign.EQUALS)[1];
                    user = ldapUserMap.get(loginName);
                    result.getUserResult().put(loginName, user);

                    // 用户组织关联数据
                    SysOfficeUser officeUser = new SysOfficeUser();
                    officeUser.setUserCn(loginName);
                    officeUser.setOfficeDn(office.getLdapDn());
                    result.getOfficeUserResult().put(loginName + CommonConstants.Sign.DUN_HAO + office.getLdapDn(), officeUser);
                }
            }
        }
    }


    /**
     * 组织同步返回结果集
     * 
     * @author linqunzhi
     * @date 2018年10月18日
     */
    class LdapSyncResult {
        private Map<String, Office> officeResult = new HashMap<>();
        private Map<String, User> userResult = new HashMap<>();
        private Map<String, SysOfficeUser> officeUserResult = new HashMap<>();

        public Map<String, Office> getOfficeResult() {
            return officeResult;
        }

        public void setOfficeResult(Map<String, Office> officeResult) {
            this.officeResult = officeResult;
        }

        public Map<String, User> getUserResult() {
            return userResult;
        }

        public void setUserResult(Map<String, User> userResult) {
            this.userResult = userResult;
        }

        public Map<String, SysOfficeUser> getOfficeUserResult() {
            return officeUserResult;
        }

        public void setOfficeUserResult(Map<String, SysOfficeUser> officeUserResult) {
            this.officeUserResult = officeUserResult;
        }

    }

}
