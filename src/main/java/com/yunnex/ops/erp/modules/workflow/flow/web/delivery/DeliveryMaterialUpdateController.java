package com.yunnex.ops.erp.modules.workflow.flow.web.delivery;

import java.util.List;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.material.entity.ErpOrderMaterialCreation;
import com.yunnex.ops.erp.modules.material.service.ErpOrderMaterialCreationService;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowBeiyiService;


/**
 * 物料更新服务Controller
 * 
 * @author yunnex
 * @date 2018年7月9日
 */
@Controller
@RequestMapping(value = "${adminPath}/visit/flow")
public class DeliveryMaterialUpdateController extends BaseController {
	@Autowired
	private JykFlowBeiyiService jykFlowBeiyiService;
	@Autowired
	private ErpOrderMaterialCreationService materialCreationService;

	/**
	 * 物料制作下单并进度同步（物料更新服务）
	 * 
	 * @param taskId
	 * @param procInsId
	 */
	@RequestMapping(value = "material_progress_sync_update")
	@ResponseBody
	public JSONObject materialProgressSyncUpdate(String taskId, String procInsId) {
	    JSONObject resObject = new JSONObject();
        if (StringUtil.isBlank(procInsId)) {
            resObject.put("message", "该物料流程不存在{}");
            resObject.put("result", false);
        } else {
            List<ErpOrderMaterialCreation> list = this.materialCreationService.findMaterialCreation(procInsId);
            if (list.size() > 1) {
                resObject.put("message", procInsId + "下的物料流程有多个{}");
                resObject.put("result", false);
            } else {
                if (list.size() == 1) {
                    resObject = this.jykFlowBeiyiService.materialContentSync(taskId, procInsId, list.get(0));
                } else {
                    resObject.put("message", procInsId + "下的物料流程不存在{}");
                    resObject.put("result", false);
                }
            }
        }
        return resObject;
    }

    /**
     *  物料制作跟踪(物料更新服务)
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    @RequestMapping(value = "material_make_follow_update")
    @ResponseBody
    public JSONObject materialMakingTracking(String taskId, String procInsId, String channelType) {
        JSONObject resObject = new JSONObject();
        List<ErpOrderMaterialCreation> list = this.materialCreationService.findMaterialCreation(procInsId);
        if (list.size() > 1) {
            resObject.put("message", procInsId + "下的物料流程有多个{}");
            resObject.put("result", false);
        } else {
            if (list.size() == 1) {
                resObject = this.jykFlowBeiyiService.materialMakingTracking(taskId, procInsId, list.get(0),
                        channelType);
            } else {
                resObject.put("message", procInsId + "下的物料流程不存在{}");
                resObject.put("result", false);
            }
        }
        return resObject;
    }
	
    /**
     *  物料部署（物料更新服务）
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    @RequestMapping(value = "material_deploy_service_update")
    @ResponseBody
    public JSONObject materialDeployServiceUpdate(String taskId, String procInsId,String completeMaterialDeployVideoShop) {
        JSONObject resObject = new JSONObject();
        List<ErpOrderMaterialCreation> list = this.materialCreationService.findMaterialCreation(procInsId);
        if (list.size() > 1) {
            resObject.put("message", procInsId + "下的物料流程有多个{}");
            resObject.put("result", false);
        } else {
            if (list.size() == 1) {
                String node="material_deploy_service_update";
                resObject = this.jykFlowBeiyiService.materialDeployServiceUpdate(taskId, procInsId, list.get(0),
                    completeMaterialDeployVideoShop,node);
            } else {
                resObject.put("message", procInsId + "下的物料流程不存在{}");
                resObject.put("result", false);
            }
        }
        return resObject;
    }
    
}