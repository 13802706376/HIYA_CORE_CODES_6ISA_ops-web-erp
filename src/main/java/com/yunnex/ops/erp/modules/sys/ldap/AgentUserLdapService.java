package com.yunnex.ops.erp.modules.sys.ldap;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.modules.agent.entity.ErpAgentInfo;
import com.yunnex.ops.erp.modules.agent.service.ErpAgentInfoService;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.service.UserService;

@Service
public class AgentUserLdapService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgentUserLdapService.class);

    private static final String LDAP_DESC = "ERP创建";
    private static final int THOUSAND = 1000;
    private static final int PAGE_SIZE = 30;

    @Autowired
    private AgentUserLdapDao agentUserLdapDao;
    @Autowired
    private ErpAgentInfoService agentInfoService;
    @Autowired
    private UserService userService;

    /**
     * 创建服务商组织单元，如果服务商名称修改，先创建新服务商组织单元，然后把旧服务商用户移到新服务商下，最后删除旧服务商及其下所有用户
     * 
     * @param agentInfo
     */
    public void createOrMoveAgent(ErpAgentInfo agentInfo) {
        LOGGER.info("创建或移动组织");
        if (agentInfo == null) {
            return;
        }
        // LDAP中"/"有特殊意义，要转义
        String newAgentName = translate(agentInfo.getName());
        if (null == newAgentName) {
            return;
        }
        String oldAgentName = translate(agentInfo.getOldName());
        // 先根据agentId找服务商，如果不存在，则创建；没有旧服务商名称也表示不存在此服务商。
        ErpAgentInfo agentInfoDB = agentInfoService.findByAgentId(agentInfo.getAgentId());
        if (agentInfoDB == null || StringUtils.isBlank(oldAgentName)) {
            // 以服务商名称作为组织单元名称创建ou
            agentUserLdapDao.createOu(newAgentName);
            return;
        }

        // 如果存在，比对服务商名称，如果相同，无须再创建
        if (newAgentName.equals(oldAgentName)) {
            return;
        }

        // 如果不同，移动用户到新的服务商下，然后删掉旧的
        agentUserLdapDao.createOu(newAgentName);
        List<AgentUser> agentUsers = agentUserLdapDao.searchAgentUsers(oldAgentName);
        if (CollectionUtils.isEmpty(agentUsers)) {
            return;
        }

        LOGGER.info("移动组织开始！{}->{}", oldAgentName, newAgentName);
        // 复制用户到新服务商下
        agentUsers.forEach(agentUser -> {
            agentUser.setAgentName(newAgentName);
            String password = agentUser.getPassword();
            // 将字符串形式的字节数组转换为字节数组再转换为真正的密码
            if (StringUtils.isNotBlank(password)) {
                String[] strPwd = password.split(Constant.COMMA);
                byte[] bytePwd = new byte[strPwd.length];
                for (int i = 0; i < strPwd.length; i++) {
                    bytePwd[i] = Byte.parseByte(strPwd[i]);
                }
                agentUser.setPassword(new String(bytePwd));
            }
            agentUserLdapDao.create(agentUser);
        });

        // 删掉旧的服务商及其下的用户
        agentUserLdapDao.delete(oldAgentName);
        LOGGER.info("移动组织结束！{}->{}", oldAgentName, newAgentName);
    }

    /**
     * 在LDAP上创建/修改服务商对应的用户
     *
     * @param userInfo
     */
    public void saveAgentUser(User userInfo) {
        AgentUser agentUser = userToAgentUser(userInfo, null);
        if (agentUser == null) {
            return;
        }
        AgentUser result = agentUserLdapDao.findByDn(agentUser);
        if (result == null) {
            agentUser.setDescription(LDAP_DESC);
            // 创建用户
            agentUserLdapDao.create(agentUser);
        } else {
            // 更新
            AgentUser updateUser = userToAgentUser(userInfo, result);
            agentUserLdapDao.update(updateUser);
        }
    }

    /**
     * 注意：如果字段值为空，会把LDAP上相应的字段值置空
     * 
     * @param userInfo
     * @param agentUser
     * @return
     */
    private static AgentUser userToAgentUser(User userInfo, AgentUser agentUser) {
        if (userInfo == null) {
            return null;
        }
        if (agentUser == null) {
            agentUser = new AgentUser(); // NOSONAR
        }
        if (StringUtils.isNotBlank(userInfo.getName())) {
            agentUser.setDisplayName(userInfo.getName());
        }
        if (StringUtils.isNotBlank(userInfo.getAgentName())) {
            agentUser.setAgentName(userInfo.getAgentName());
        }
        if (StringUtils.isNotBlank(userInfo.getLoginName())) {
            agentUser.setFullName(userInfo.getLoginName());
            agentUser.setLastName(userInfo.getLoginName());
        }
        if (StringUtils.isNotBlank(userInfo.getPassword())) {
            agentUser.setPassword(userInfo.getPassword());
        }
        if (StringUtils.isNotBlank(userInfo.getMobile())) {
            agentUser.setMobile(userInfo.getMobile());
        }
        if (StringUtils.isNotBlank(userInfo.getEmail())) {
            agentUser.setMail(userInfo.getEmail());
        }
        return agentUser;
    }

    // 转义斜杠
    public static String translate(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return str.replaceAll("\\/", "\\\\/");
    }

    /**
     * 根据数据库数据在LDAP上创建所有服务商及其下所有用户
     *
     * @return
     */
    public void saveLdapAgentsAndUsers() {
        long start = System.currentTimeMillis();
        LOGGER.info("保存所有服务商组织单元及其下所有用户开始！");
        int pageNo = 1;
        while (true) { // NOSONAR
            Page<ErpAgentInfo> page = agentInfoService.findPage(new Page(pageNo++, PAGE_SIZE), new ErpAgentInfo());
            List<ErpAgentInfo> list;
            if (page == null || CollectionUtils.isEmpty((list = page.getList()))) {
                break;
            }
            list.forEach(agent -> {
                String agentName = translate(agent.getName());
                agentUserLdapDao.createOu(agentName);
                saveLdapAgentUsers(agent);
            });
            if (page.isLastPage()) {
                break;
            }
        }
        LOGGER.info("保存所有服务商组织单元及其下所有用户结束！耗时：{}s", (System.currentTimeMillis() - start) / THOUSAND);
    }

    public void saveLdapAgentUsers(ErpAgentInfo agentInfo) {
        if (agentInfo == null) {
            return;
        }
        Integer agentId = agentInfo.getAgentId();
        LOGGER.info("创建或更新服务商下的所有用户信息：agentId={}", agentId);
        List<User> users = userService.findAllByAgentId(agentId);
        if (CollectionUtils.isNotEmpty(users)) {
            users.forEach(user -> {
                user.setAgentName(agentInfo.getName());
                saveAgentUser(user);
            });
        }
    }

}
