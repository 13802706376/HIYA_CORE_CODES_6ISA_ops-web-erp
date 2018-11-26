/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.yunnex.ops.erp.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.utils.CacheUtils;
import com.yunnex.ops.erp.modules.sys.dao.DictDao;
import com.yunnex.ops.erp.modules.sys.entity.Dict;
import com.yunnex.ops.erp.modules.sys.utils.DictUtils;

/**
 * 字典Service
 * @author ThinkGem
 * @version 2014-05-16
 */
@Service
public class DictService extends CrudService<DictDao, Dict> {

    /**
     * 查询字段类型列表
     * 
     * @return
     */
    public List<String> findTypeList() {
        return dao.findTypeList(new Dict());
    }

    public List<Dict> findListByType(String type) {
        return dao.findLisByType(type);
    }


    @Transactional(readOnly = false)
    public void save(Dict dict) {
        super.save(dict);
        CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
    }

    @Transactional(readOnly = false)
    public void delete(Dict dict) {
        super.delete(dict);
        CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
    }

}
