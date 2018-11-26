package com.yunnex.ops.erp.modules.material.web;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.result.ServiceErrorResult;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.material.dto.MaterialCreationRequestDto;
import com.yunnex.ops.erp.modules.material.dto.MaterialCreationResponseDto;
import com.yunnex.ops.erp.modules.material.entity.ErpOrderMaterialCreation;
import com.yunnex.ops.erp.modules.material.service.ErpOrderMaterialCreationService;
import com.yunnex.ops.erp.modules.order.exception.OrderMaterialException;
import com.yunnex.ops.erp.modules.order.service.ErpOrderMaterialApiService;

/**
 * 物料制作Controller
 * 
 * @author yunnex
 * @version 2018-05-25
 */
@Controller
@RequestMapping(value = "${adminPath}/material/erpOrderMaterialCreation")
public class ErpOrderMaterialCreationController extends BaseController {

    @Autowired
    private ErpOrderMaterialCreationService erpOrderMaterialCreationService;

    @RequiresPermissions("material:erpOrderMaterialCreation:view")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/material/erpOrderMaterialCreationList";
    }

    /**
     * 物料清单页面
     * 
     * @return
     */
    @RequiresPermissions("material:erpOrderMaterialCreation:view")
    @RequestMapping(value = {"view"})
    public String view() {
        return "modules/material/erpOrderMaterialCreationView";
    }

    /**
     * 查询分页列表
     *
     * @param requestDto
     * @return
     * @date 2018年5月29日
     */
    @RequiresPermissions("material:erpOrderMaterialCreation:view")
    @RequestMapping(value = "getPageList")
    @ResponseBody
    public MaterialCreationResponseDto getPageList(@RequestBody MaterialCreationRequestDto requestDto) {
        return erpOrderMaterialCreationService.findPage(requestDto);
    }

    /**
     * 更新数据
     *
     * @param requestDto
     * @return
     * @date 2018年5月29日
     */
    @RequiresPermissions("material:erpOrderMaterialCreation:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult update(@RequestBody ErpOrderMaterialCreation entity) {
        return erpOrderMaterialCreationService.update(entity);
    }

    /**
     * 保存数据
     *
     * @param requestDto
     * @return
     * @date 2018年5月29日
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult save(@RequestBody ErpOrderMaterialCreation entity) {
        erpOrderMaterialCreationService.save(entity);
        return new BaseResult();
    }

    /**
     * 确认已下单
     *
     * @return
     * @date 2018年5月28日
     */
    @RequiresPermissions("material:erpOrderMaterialCreation:operate")
    @RequestMapping(value = "confirmOrder", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult confirmOrder(@RequestBody ErpOrderMaterialCreation entity) {
        BaseResult baseResult;
        try {
            baseResult = erpOrderMaterialCreationService.confirmOrder(entity);
        } catch (OrderMaterialException e) {
            return new ServiceErrorResult(e.getMessage());
        }
        return baseResult;
    }

    /**
     * 确认已发货
     *
     * @return
     * @date 2018年5月28日
     */
    @RequiresPermissions("material:erpOrderMaterialCreation:operate")
    @RequestMapping(value = "confirmSendOff", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult confirmSendOff(@RequestBody ErpOrderMaterialCreation entity) {
        BaseResult baseResult;
        try {
            baseResult = erpOrderMaterialCreationService.confirmSendOff(entity);
        } catch (OrderMaterialException e) {
            return new ServiceErrorResult(e.getMessage());
        }
        return baseResult;
    }

    /**
     * 确认已到店
     *
     * @return
     * @date 2018年5月28日
     */
    @RequiresPermissions("material:erpOrderMaterialCreation:operate")
    @RequestMapping(value = "confirmArrived", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult confirmArrived(@RequestBody ErpOrderMaterialCreation entity) {
        BaseResult baseResult;
        try {
            baseResult = erpOrderMaterialCreationService.confirmArrived(entity);
        } catch (OrderMaterialException e) {
            return new ServiceErrorResult(e.getMessage());
        }
        return baseResult;
    }

    /**
     * 导出物料制作列表
     * 
     * @param requestDto
     * @param response
     * @return
     */
    @RequiresPermissions("material:erpOrderMaterialCreation:export")
    @RequestMapping("export")
    public void export(MaterialCreationRequestDto requestDto, HttpServletResponse response) {
        erpOrderMaterialCreationService.export(requestDto, response);
    }

    /**
     * 下载物料包
     * 
     * @param orderNumber
     * @param response
     */
    @RequestMapping("downloadMaterialPackage")
    public void downloadMaterialPackage(String orderNumber, HttpServletResponse response) throws IOException {
        erpOrderMaterialCreationService.downloadMaterialPackage(orderNumber, response);
    }

}
