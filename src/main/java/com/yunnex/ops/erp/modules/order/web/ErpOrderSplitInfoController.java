package com.yunnex.ops.erp.modules.order.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RuntimeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.utils.FileUtils;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.entity.SplitGoodEditNumForm;
import com.yunnex.ops.erp.modules.order.entity.SplitGoodForm;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.sys.utils.DwrUtils;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;
import com.yunnex.ops.erp.modules.workflow.flow.handler.ProcessStartContext;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpOrderFileService;

import yunnex.common.core.dto.ApiResult;

/**
 * 分单Controller
 * 
 * @author huanghaidong
 * @version 2017-10-24
 */
@Controller
@RequestMapping(value = "${adminPath}/order/erpOrderSplitInfo")
public class ErpOrderSplitInfoController extends BaseController {

    @Autowired
    private ErpOrderSplitInfoService erpOrderSplitInfoService;
    
    @Autowired
    private RuntimeService runService;
    
    @Autowired
    private ErpOrderFileService orderfileService;

    @Autowired
    private DwrUtils dwr;

    @ModelAttribute
    public ErpOrderSplitInfo get(@RequestParam(required = false) String id) {
        ErpOrderSplitInfo entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = erpOrderSplitInfoService.get(id);
        }
        if (entity == null) {
            entity = new ErpOrderSplitInfo();
        }
        return entity;
    }

    @RequiresPermissions("order:erpOrderSplitInfo:view")
    @RequestMapping(value = {"list", ""})
    public String list(ErpOrderSplitInfo erpOrderSplitInfo, HttpServletRequest request, HttpServletResponse response,
            Model model) {
        Page<ErpOrderSplitInfo> page = erpOrderSplitInfoService.findPage(new Page<ErpOrderSplitInfo>(request, response), erpOrderSplitInfo);
        model.addAttribute("page", page);
        return "modules/order/erpOrderSplitInfoList";
    }

    @RequiresPermissions("order:erpOrderSplitInfo:view")
    @RequestMapping(value = "form")
    public String form(ErpOrderSplitInfo erpOrderSplitInfo, Model model) {
        model.addAttribute("erpOrderSplitInfo", erpOrderSplitInfo);
        return "modules/order/erpOrderSplitInfoForm";
    }

    @RequiresPermissions("order:erpOrderSplitInfo:edit")
    @RequestMapping(value = "save")
    public String save(ErpOrderSplitInfo erpOrderSplitInfo, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, erpOrderSplitInfo)) {
            return form(erpOrderSplitInfo, model);
        }
        erpOrderSplitInfoService.save(erpOrderSplitInfo);
        addMessage(redirectAttributes, "保存分单成功");
        return "redirect:" + Global.getAdminPath() + "/order/erpOrderSplitInfo/?repage";
    }

    @RequiresPermissions("order:erpOrderSplitInfo:edit")
    @RequestMapping(value = "delete")
    public String delete(ErpOrderSplitInfo erpOrderSplitInfo, RedirectAttributes redirectAttributes) {
        erpOrderSplitInfoService.delete(erpOrderSplitInfo);
        addMessage(redirectAttributes, "删除分单成功");
        return "redirect:" + Global.getAdminPath() + "/order/erpOrderSplitInfo/?repage";
    }

    @RequiresPermissions("order:erpOrderSplitInfo:edit")
    @RequestMapping(value = "split")
    @ResponseBody
    public JSONObject split(@RequestParam(required = true, value = "goodId") String goodId,
                    @RequestParam(required = true, value = "num") Integer num,
                    @RequestParam(required = true, value = "planningExpert") String planningExpert,HttpServletRequest request) {
        JSONObject resObject = new JSONObject();
        List<SplitGoodForm> splitGoodLists = new ArrayList<SplitGoodForm>();
        SplitGoodForm splitGood = new SplitGoodForm();
        splitGood.setGoodId(goodId);
        splitGood.setNum(num);
        splitGood.setPlanningExpert(planningExpert);
        splitGoodLists.add(splitGood);
        ErpOrderSplitInfo result = erpOrderSplitInfoService.multiGoodSplitAndStartProcess(splitGoodLists, false, planningExpert);
        //ErpOrderSplitInfo result = erpOrderSplitInfoService.split(goodId, num, planningExpert);
        if (result != null) {
            // 分单成功后，启动工作流(新流程)
            //workFlowService.startJykWorkFlowNew(planningExpert, result.getOrderId(), result.getId());
            resObject.put("result", true);
        } else {
            resObject.put("result", false);
        }
        dwr.dwr(planningExpert);
        return resObject;
    }

    /**
     * 多商品分单
     *
     * @param splitGoodLists
     * @param request
     * @return
     * @date 2018年4月3日
     * @author zjq
     */
    @RequiresPermissions("order:detail:jykService:multiSplit")
    @RequestMapping(value = "multiSplit", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject multiSplit(@RequestBody(required = true) List<SplitGoodForm> splitGoodLists,
                    HttpServletRequest request) {

        logger.info("ErpOrderSplitInfoController.multiSplit param is ({})", splitGoodLists);

        JSONObject resObject = new JSONObject();

        if (null != splitGoodLists && !splitGoodLists.isEmpty()) {

            String PlanningExpert = splitGoodLists.get(0).getPlanningExpert();

            ErpOrderSplitInfo result = ProcessStartContext.startByOrderSplit(splitGoodLists);

            if (result != null) {
                resObject.put("result", true);
            } else {
                resObject.put("result", false);
            }
            dwr.dwr(PlanningExpert);
        }

        logger.info("ErpOrderSplitInfoController.multiSplit result JSONObject is ({})", resObject.toString());
        return resObject;
    }


    @RequiresPermissions("order:erpOrderSplitInfo:edit")
    @RequestMapping(value = "editNum")
    @ResponseBody
    public JSONObject editNum(@RequestParam(required = true, value = "id") String id, @RequestParam(required = true, value = "num") Integer num,String times) {

        JSONObject resObject = new JSONObject();
        boolean result = erpOrderSplitInfoService.editNum(id, num);
        resObject.put("result", result);
        return resObject;
    }

    /**
     * 编辑分单数量，支持多商品
     *
     * @param multiNumLists
     * @param times
     * @return
     * @date 2018年4月3日
     * @author zjq
     */
    @RequiresPermissions("order:erpOrderSplitInfo:editMultiNum")
    @RequestMapping(value = "editMultiNum")
    @ResponseBody
    public JSONObject editMultiNum(@RequestBody(required = true) List<SplitGoodEditNumForm> multiNumLists,
                    String times) {
        JSONObject resObject = new JSONObject();
        boolean result = erpOrderSplitInfoService.editMultiNum(multiNumLists);
        resObject.put("result", result);
        return resObject;
    }


    /**
     * 修改策划专家
     *
     * @param planningExpert
     * @param splitId
     * @return
     * @date 2018年4月8日
     * @author zjq
     */
    @RequiresPermissions("order:erpOrderSplitInfo:modifyPlanningExpert")
    @RequestMapping(value = "modifyPlanningExpert")
    @ResponseBody
    public JSONObject modifyPlanningExpert(String planningExpert, String splitId) {
        JSONObject resObject = new JSONObject();
        boolean result = erpOrderSplitInfoService.modifyPlanningExpert(planningExpert, splitId);
        resObject.put("result", result);
        return resObject;
    }

    @RequiresPermissions("order:erpOrderSplitInfo:hurry")
    @RequestMapping(value = "hurry")
    @ResponseBody
    public JSONObject hurry(@RequestParam(required = true, value = "id") String id) {
        JSONObject resObject = new JSONObject();
        boolean result = erpOrderSplitInfoService.updateHurryFlag(id, ErpOrderSplitInfo.HURRY_FLAG_YES);
        resObject.put("result", result);
        return resObject;
    }
    
    @RequiresPermissions("order:erpOrderSplitInfo:altertime")
    @RequestMapping(value = "altertime")
    @ResponseBody
    public JSONObject altertime(@RequestParam(required = true, value = "id") String id,String times) throws ParseException {
        JSONObject resObject = new JSONObject();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ErpOrderSplitInfo split=erpOrderSplitInfoService.get(id);
        Object o= runService.getVariable(split.getProcInsId(), "promotionTime");
        if(o!=null){
        	runService.setVariable(split.getProcInsId(), "promotionTime", times);
        	split.setPromotionTime(sdf.parse(times));
        	erpOrderSplitInfoService.save(split);
        	resObject.put("result", true);
        	return resObject;
        }
        return resObject;
    }

    @RequiresPermissions("order:detail:jykFlow:suspendOrRestart")
    @RequestMapping(value = "suspend")
    @ResponseBody
    public BaseResult suspend(ErpOrderSplitInfo orderSplitInfo) {
        return erpOrderSplitInfoService.suspend(orderSplitInfo);
    }

    @RequiresPermissions("order:detail:jykFlow:suspendOrRestart")
    @RequestMapping(value = "restart")
    @ResponseBody
    public BaseResult restart(@RequestParam(required = true, value = "id") String id) {
        return erpOrderSplitInfoService.restart(id);
    }

    @RequestMapping(value = "getSuspendData/{id}")
    @ResponseBody
    public BaseResult getSuspendData(@PathVariable("id") String id) {
        return erpOrderSplitInfoService.getSuspendData(id);
    }

    @RequestMapping(value = "updateSuspendData")
    @ResponseBody
    public BaseResult updateSuspendData(ErpOrderSplitInfo orderSplitInfo) {
        return erpOrderSplitInfoService.updateSuspendData(orderSplitInfo);
    }

    @RequestMapping(value = "promoteInfoUrl")
    public String promoteInfoUrl(ErpOrderSplitInfo erpOrderSplitInfo, Model model) {
        return "modules/diagnosis/promoteInfoUrl";
    }

    @RequestMapping(value = "promoteInfoDetail")
    public String promoteInfoDetail(ErpOrderSplitInfo erpOrderSplitInfo, Model model) {
        return "modules/diagnosis/promoteInfoDetail";
    }

    @RequestMapping(value = "promotionalMaterials")
    @ResponseBody
    public ApiResult<List<ErpOrderSplitInfo>> promotionalMaterials(String orderNumber, String shopName) {
        List<ErpOrderSplitInfo> list = erpOrderSplitInfoService.promotionalMaterials(orderNumber, shopName);
        if (null != list && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, String> diagnosisTaskInfo = erpOrderSplitInfoService.getDiagnosisTaskInfo(list.get(i).getId());
                if (diagnosisTaskInfo != null) {
                    list.get(i).setTaskId(diagnosisTaskInfo.get("taskId"));
                }
            }
            return ApiResult.build(list);
        }
        ApiResult<List<ErpOrderSplitInfo>> result = ApiResult.build(null);
        result.setCode("1");
        return result;
    }

    @RequestMapping(value = "promotionalMaterialsList")
    @ResponseBody
    public ApiResult<List<ErpOrderFile>> promotionalMaterialsList(String proId) throws ParseException {
        List<ErpOrderFile> list = orderfileService.findListByProcInsId(proId);
        if (null != list && !list.isEmpty()) {
            return ApiResult.build(list);
        }
        ApiResult<List<ErpOrderFile>> result = ApiResult.build(null);
        result.setCode("1");
        return result;
    }


    @RequestMapping(value = "downUrl")
    public String downUrl(String downloadUrl, String realFileName, HttpServletRequest request, HttpServletResponse response) { 	
        response.setContentType("text/html;charset=UTF-8");
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        
        try {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream");
            response.reset();// 清除response中的缓存
            
            // 根据网络文件地址创建URL
            URL url = new URL(downloadUrl);
            // 获取此路径的连接
            URLConnection conn = url.openConnection();
            
            String suffix=FileUtils.getFileExtension(downloadUrl);
            // 对文件名称进行转码操作
            String realFileName1 = URLDecoder.decode(realFileName+"."+suffix, "utf8");
            Long fileLength = conn.getContentLengthLong();// 获取文件大小
            // 设置reponse响应头，真实文件名重命名，就是在这里设置，设置编码
            response.setHeader("Content-disposition",
                    "attachment; filename=" + new String(realFileName1.getBytes("utf-8"), "ISO8859-1"));
            response.setHeader("Content-Length", String.valueOf(fileLength));
  
            bis = new BufferedInputStream(conn.getInputStream());// 构造读取流
            bos = new BufferedOutputStream(response.getOutputStream());// 构造输出流
            Integer num=1024;
            byte[] buff = new byte[num];
            int bytesRead;
            // 每次读取缓存大小的流，写到输出流
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            response.flushBuffer();// 将所有的读取的流返回给客户端
        } catch (RuntimeException | IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
        	try {
        		if(null!=bis)
        		{
        			bis.close();
        		}
        		if(null!=bos)
        		{
        			bos.close();
        		}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
        }
        return null;
    }

    /**
     * 根据订单号查找
     * 
     * @param orderNumber
     * @return
     */
    @RequestMapping("findByOrderNumber")
    public @ResponseBody List<ErpOrderSplitInfo> findByOrderNumber(String orderNumber) {
        return erpOrderSplitInfoService.findByOrderNumber(orderNumber);
    }

    /**
     * 业务定义：聚引客生产分单流程重启
     * 
     * @date 2018年8月30日
     * @author R/Q
     */
    @RequiresPermissions("order:detail:jykFlow:restartFlow")
    @ResponseBody
    @RequestMapping(value = "resetWorkFlow/{procInsId}", method = RequestMethod.POST)
    public Object resetWorkFlow(@PathVariable("procInsId") String procInsId, @RequestBody(required = true) List<SplitGoodForm> splitGoodLists) {
        return erpOrderSplitInfoService.resetWorkFlow(procInsId, splitGoodLists);
    }
}
