package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.List;

import org.activiti.editor.language.json.converter.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunnex.ops.erp.modules.act.dao.ActDao;

/**
 * 聚引客流程判断Service
 * 
 * @author SunQ
 * @date 2018年1月31日
 */
@Service
public class JykFlowJudgeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JykFlowJudgeService.class);

    @Autowired
    private ActDao actDao;

    private static final String[] KEY_GROUP_ZHIXIAO = new String[] {"create_stores_on_official_zhixiao", "perfect_friends_promote_info_zhixiao", "friends_promote_info_review_zhixiao", "modify_friends_promote_info_zhixiao", "start_friends_authorization_zhixiao", "confirmed_friends_authorization_sucess_zhixiao", "decide_microblog_extension_zhixiao", "perfect_microblog_promote_info_zhixiao", "microblog_promote_info_review_zhixiao", "modify_microblog_promote_info_zhixiao", "microblog_promote_info_review_result_zhixiao", "decide_momo_extension_zhixiao", "perfect_momo_promote_info_zhixiao", "momo_promote_info_review_zhixiao", "modify_momo_promote_info_zhixiao", "promote_material_internal_review_zhixiao", "perfect_momo_account_info_zhixiao", "confirm_momo_account_sucess_zhixiao", "friends_promote_info_review", "modify_friends_promote_info", "confirmed_friends_authorization_sucess", "microblog_promote_info_review", "modify_microblog_promote_info"};

    private static final String[] KEY_GROUP_SERVICE = new String[] {"create_stores_on_official_service", "perfect_friends_promote_info_service", "friends_promote_info_review_service", "confirmed_friends_authorization_sucess_service", "decide_microblog_extension_service", "perfect_microblog_promote_info_service", "perfect_microblog_review_info_service", "decide_momo_extension_service", "perfect_momo_promote_info_service", "momo_promote_info_in_service", "promote_material_internal_review_service", "perfect_momo_account_info_service", "momo_promote_info_review_service", "friends_promote_info_review", "modify_friends_promote_info", "confirmed_friends_authorization_sucess", "microblog_promote_info_review", "modify_microblog_promote_info"};

    /**
     * 判断是否完成推广开户(直销)
     *
     * @param procInsId
     * @return
     * @date 2018年1月31日
     * @author SunQ
     */
    public boolean isFinishOpenAccountZhixiao(String procInsId) {
        try {
            List<String> keyList = actDao.findTaskKeyByProcInsId(procInsId);
            if (!CollectionUtils.isEmpty(keyList)) {
                for (String key : keyList) {
                    if (useLoop(KEY_GROUP_ZHIXIAO, key)) {
                        return false;
                    }
                }
            }
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return true;
    }

    /**
     * 判断是否完成推广开户(服务商)
     *
     * @param procInsId
     * @return
     * @date 2018年1月31日
     * @author SunQ
     */
    public boolean isFinishOpenAccountService(String procInsId) {
        try {
            List<String> keyList = actDao.findTaskKeyByProcInsId(procInsId);
            if (!CollectionUtils.isEmpty(keyList)) {
                for (String key : keyList) {
                    if (useLoop(KEY_GROUP_SERVICE, key)) {
                        return false;
                    }
                }
            }
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return true;
    }

    /**
     * 循环遍历，判断数组是否包含值
     *
     * @param arr
     * @param targetValue
     * @return
     * @date 2018年1月31日
     * @author SunQ
     */
    protected boolean useLoop(String[] arr, String targetValue) {
        for (String s : arr) {
            if (s.equals(targetValue))
                return true;
        }
        return false;
    }
}
