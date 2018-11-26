package com.yunnex.ops.erp.modules.workflow.data.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.workflow.data.entity.JykDataPresentation;
import com.yunnex.ops.erp.modules.workflow.data.service.JykDataPresentationService;

/**
 * 数据报告Controller
 * 
 * @author SunQ
 * @date 2018年1月23日
 */
@Controller
@RequestMapping(value = "${adminPath}/dataPresentation/")
public class JykDataPresentationController extends BaseController {
    
    @Autowired
    private JykDataPresentationService jykDataPresentationService;
    @Autowired
    private ErpOrderSplitInfoService erpOrderSplitInfoService;

    /**
     * 
     *
     * @param procInsId
     * @param dataType
     * @return
     * @date 2018年1月25日
     * @author SunQ
     */
    @ResponseBody
    @RequestMapping(value = "getInfo")
    public BaseResult getInfo(String procInsId, String dataType) {
        BaseResult result = new BaseResult();
        JykDataPresentation dataPresentation = jykDataPresentationService.getByProcInsId(procInsId, dataType);
        if(dataPresentation==null){
            dataPresentation = new JykDataPresentation();
        }
        result.setAttach(dataPresentation);
        return result;
    }
    
    /**
     * 上传数据报告PDF页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     * @date 2018年1月26日
     * @author SunQ
     */
    @RequestMapping(value = "uploadDataPresentation")
    public String uploadDataPresentation(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/workflow/data/uploadDataPresentation";
    }
    
    /**
     * 保存数据报告对象
     * state : 保存操作，对应的值传“1”；发布到小程序，对应值传“2”。
     * dataType : 首日报告，对应的值传“1”；过程中，对应值传“2”；最终，对应值传“3”。
     * @param request
     * @param response
     * @return
     * @date 2018年1月25日
     * @author SunQ
     */
    @ResponseBody
    @RequestMapping(value = "save")
    public BaseResult save(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        String data = request.getParameter("data");
        JSONObject infomation = JSONObject.parseObject(data);
        String id = infomation.getString("id");
        String procInsId = infomation.getString("procInsId");
        String state = infomation.getString("state");
        String pdfUrl = infomation.getString("pdfUrl");
        String pdfName = infomation.getString("pdfName");
        String dataType = infomation.getString("dataType");
        
        if(StringUtils.isBlank(procInsId)){
            result.setCode("1");
            result.setMessage("procInsId不能为空");
            return result;
        }
        
        if(StringUtils.isBlank(state)){
            result.setCode("1");
            result.setMessage("state不能为空");
            return result;
        }
        
        if(StringUtils.isBlank(pdfUrl)){
            result.setCode("1");
            result.setMessage("PDF文件不能为空");
            return result;
        }
        
        if(StringUtils.isBlank(dataType)){
            result.setCode("1");
            result.setMessage("dataType不能为空");
            return result;
        }
        
        if(StringUtils.isNotBlank(id)){
            JykDataPresentation dataPresentation = jykDataPresentationService.get(id);
            dataPresentation.setProcInsId(procInsId);
            dataPresentation.setPdfUrl(pdfUrl);
            dataPresentation.setPdfName(pdfName);
            dataPresentation.setDataType(dataType);
            dataPresentation.setState(state);
            jykDataPresentationService.save(dataPresentation);
        }else{
            ErpOrderSplitInfo splitInfo = erpOrderSplitInfoService.getByProsIncId(procInsId);
            JykDataPresentation dataPresentation = new JykDataPresentation();
            dataPresentation.setOrderId(splitInfo.getOrderId());
            dataPresentation.setSplitId(splitInfo.getId());
            dataPresentation.setProcInsId(procInsId);
            dataPresentation.setPdfUrl(pdfUrl);
            dataPresentation.setPdfName(pdfName);
            dataPresentation.setDataType(dataType);
            dataPresentation.setState(state);
            jykDataPresentationService.deleteBefore(splitInfo.getId(), dataType);
            jykDataPresentationService.save(dataPresentation);
        }
        return result;
    }
    
    /**
     * 推送数据报告对象到小程序
     *
     * @param id
     * @return
     * @date 2018年1月25日
     * @author SunQ
     */
    @ResponseBody
    @RequestMapping(value = "pushWxapp")
    public BaseResult pushWxapp(String id) {
        BaseResult result = new BaseResult();
        jykDataPresentationService.updateState(id, "2");
        return result;
    } 
}