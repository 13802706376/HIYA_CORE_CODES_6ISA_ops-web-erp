package com.yunnex.ops.erp.modules.hat.web;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.result.IllegalArgumentErrorResult;
import com.yunnex.ops.erp.modules.hat.entity.HatArea;
import com.yunnex.ops.erp.modules.hat.entity.HatCity;
import com.yunnex.ops.erp.modules.hat.entity.HatProvince;
import com.yunnex.ops.erp.modules.hat.service.HatAreaService;
import com.yunnex.ops.erp.modules.hat.service.HatCityService;
import com.yunnex.ops.erp.modules.hat.service.HatProvinceService;

/**
 * 地址位置Controller
 * 
 * @author zhangjl
 * @version 2018-01-15
 */
@Controller
@RequestMapping(value = "${adminPath}/location")
public class LocationController {
    public static final Logger LOGGER = LoggerFactory.getLogger(LocationController.class);

    @Autowired
    private HatProvinceService hatProvinceService;
    @Autowired
    private HatCityService hatCityService;
    @Autowired
    private HatAreaService hatAreaService;

    /**
     * 获取省份列表
     *
     * @return
     * @date 2018年1月15日
     * @author zhangjl
     */
    @ResponseBody
    @RequestMapping(value = "get-province-list")
    public BaseResult getProvinceList() {
        HatProvince condition = new HatProvince();
        List<HatProvince> list = hatProvinceService.findList(condition);

        BaseResult result = new BaseResult();
        result.setAttach(list);
        return result;
    }

    /**
     * 获取城市列表
     * 
     * @param city father 省份ID
     *
     * @return
     * @date 2018年1月15日
     * @author zhangjl
     */
    @ResponseBody
    @RequestMapping(value = "get-city-list")
    public BaseResult getCityList(@RequestBody HatCity city) {
        if (null == city || StringUtils.isBlank(city.getFather())) {
            LOGGER.error("非法参数");
            return new IllegalArgumentErrorResult();
        }

        List<HatCity> list = hatCityService.findList(city);

        BaseResult result = new BaseResult();
        result.setAttach(list);
        return result;
    }

    /**
     * 获取县区列表
     * 
     * @param area father 城市ID
     *
     * @return
     * @date 2018年1月15日
     * @author zhangjl
     */
    @ResponseBody
    @RequestMapping(value = "get-area-list")
    public BaseResult getAreaList(@RequestBody HatArea area) {
        if (null == area || StringUtils.isBlank(area.getFather())) {
            LOGGER.error("非法参数");
            return new IllegalArgumentErrorResult();
        }

        List<HatArea> list = hatAreaService.findList(area);

        BaseResult result = new BaseResult();
        result.setAttach(list);
        return result;
    }
}
