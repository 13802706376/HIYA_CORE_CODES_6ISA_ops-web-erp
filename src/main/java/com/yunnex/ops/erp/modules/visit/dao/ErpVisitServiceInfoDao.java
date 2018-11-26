package com.yunnex.ops.erp.modules.visit.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceDetailInfo;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceInfo;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceItem;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceItemRecord;

/**
 * 上门服务DAO接口
 * 
 * @author R/Q
 * @version 2018-05-26
 */
@MyBatisDao
public interface ErpVisitServiceInfoDao extends CrudDao<ErpVisitServiceInfo> {

    /**
     * 业务定义：查询上门服务数据列表
     * 
     * @date 2018年5月26日
     * @param paramObj 查询条件对象
     * @param user 当前登录用户对象
     * @param page 分页对象
     * @author R/Q
     */
    List<ErpVisitServiceInfo> queryVisitServiceDataList(@Param("paramObj") ErpVisitServiceInfo paramObj,
                    @Param("page") Page<ErpVisitServiceInfo> page);
    
    /**
     * 业务定义：查询上门服务数据列表
     * 
     * @date 2018年5月26日
     * @param paramObj 查询条件对象
     * @param user 当前登录用户对象
     * @param page 分页对象
     * @author R/Q
     */
    List<ErpVisitServiceDetailInfo> queryShopServiceDetailList(@Param("paramObj") ErpVisitServiceDetailInfo paramObj,
                    @Param("page") Page<ErpVisitServiceDetailInfo> page);
    
    /**
     * 业务定义：通过流程实例ID+上门目的code获取唯一上门服务数据
     * 
     * @date 2018年5月29日
     * @param procInsId 流程实例ID
     * @param serviceGoalCode 上门目的code
     * @author R/Q
     */
    ErpVisitServiceInfo get(@Param("procInsId") String procInsId, @Param("serviceGoalCode") String serviceGoalCode);
    
    ErpVisitServiceInfo getVisitDetail(String serviceGoalCode);
    
    /**
     * 业务定义：根据上门服务ID查询对应的服务项记录
     * 
     * @date 2018年5月28日
     * @param serviceInfoId 上门服务数据ID
     * @author R/Q
     */
    List<ErpVisitServiceItemRecord> queryServiceItemRecordDataByServiceId(@Param("serviceInfoId") String serviceInfoId);

    /**
     * 业务定义：批量新增服务项目记录数据
     * 
     * @date 2018年5月28日
     * @param list 服务项目记录数据集合
     * @param serviceInfoId 上门服务数据ID
     * @param userId 当前登录用户ID
     * @author R/Q
     */
    void batchInsertServiceItemRecordData(@Param("list") List<ErpVisitServiceItemRecord> list, @Param("serviceInfoId") String serviceInfoId,
                    @Param("userId") String userId);

    /**
     * 业务定义：根据上门服务数据ID删除对应的服务项目记录数据，物理删除
     * 
     * @date 2018年5月28日
     * @param serviceInfoId 上门服务数据ID
     * @author R/Q
     */
    void deleteServiceItemRecordDataByServiceId(@Param("serviceInfoId") String serviceInfoId);

    /**
     * 业务定义：查询服务项目
     * 
     * @date 2018年5月29日
     * @author R/Q
     */
    List<ErpVisitServiceItem> queryServiceItemData(ErpVisitServiceInfo paramObj);
    
    /**
     * 业务定义：查询服务id
     * 
     * @date 2018年5月29日
     * @author R/Q
     */
    List<String> findVisitIdByProcInsId(@Param("procInsId") String procInsId,@Param("serviceGoalCode") String serviceGoalCode);
    
    /**
     * 业务定义：查询服务id
     * 
     * @date 2018年5月29日
     * @author R/Q
     */
    List<Map<String,String>> findDoorVisitIdByProcInsId(@Param("procInsId") String procInsId);

    /**
     * 业务定义：查询上门目的列表
     * 
     * @date 2018年5月29日
     * @author R/Q
     */
    List<Map<String, Object>> queryServiceGoalData(@Param("serviceTypeCode") String serviceTypeCode);

    /**
     * 业务定义：查询商户培训类型服务项记录，查询范围=首次营销策划上门服务+物料上门服务
     * 
     * @date 2018年5月31日
     * @author R/Q
     */
    List<ErpVisitServiceItemRecord> queryTrainItemRecord(@Param("shopInfoId") String shopInfoId);

    /**
     * 业务定义：查询商户培训类型服务项，查询范围=首次营销策划上门服务+物料上门服务
     * 
     * @date 2018年5月31日
     * @author R/Q
     */
    List<ErpVisitServiceItem> queryTrainItem();

    /**
     * 业务定义：校验预约开始时间冲突
     * 
     * @date 2018年6月4日
     * @author R/Q
     */
    List<ErpVisitServiceInfo> checkAppointedDate(ErpVisitServiceInfo paramObj);
    
    
    List<ErpVisitServiceInfo> getHeader();
    
    List<ErpVisitServiceInfo> getHeaderText(@Param("serviceGoalTxt") String serviceGoalTxt);

    /**
     * 业务定义：依据根据上门服务查询权限查询对应团队信息
     * 
     * @date 2018年7月5日
     * @author R/Q
     */
    List<Map<String, Object>> queryTeamByRole(@Param("userId") String userId);

    /**
     * 业务定义：查询团队成员上门服务数量-分页
     * 
     * @date 2018年7月12日
     * @author R/Q
     */
    List<Map<String, Object>> queryTeamUserServiceCount(@Param("paramObj") ErpVisitServiceInfo paramObj, @Param("agentId") Integer agentId,
                    Page<Map<String, Object>> page);
    
    List<Map<String, Object>> queryTeamUserServiceCount(@Param("paramObj") ErpVisitServiceInfo paramObj, @Param("agentId") Integer agentId);

    /**
     * 业务定义：查询团队成员上门服务数量总计
     * 
     * @date 2018年7月12日
     * @author R/Q
     */
    Map<String, Object> queryTeamUserServiceTotal(@Param("paramObj") ErpVisitServiceInfo paramObj, @Param("agentId") Integer agentId);


    /**
     * 根据流程id 和 上门目的code 获取唯一上门信息
     *
     * @param procInsId
     * @param serviceGoalCode
     * @return
     * @date 2018年7月23日
     * @author linqunzhi
     */
    ErpVisitServiceInfo getByGoalCode(@Param("procInsId") String procInsId, @Param("serviceGoalCode") String serviceGoalCode);

    /**
     * 根据流程id 和 上门目的code 获取流程其他上门信息
     *
     * @param procInsId
     * @param serviceGoalCode
     * @return
     * @date 2018年7月23日
     */
    List<ErpVisitServiceInfo> getListByProcIdAndGoalCode(@Param("procInsId") String procInsId, @Param("serviceGoalCode") String serviceGoalCode);
    
    /**
     * 业务定义：重置
     * 
     * @date 2018年9月12日
     * @author R/Q
     */
    void resetByProcInsId(@Param("oldProcInsId") String oldProcInsId, @Param("newProcInsId") String newProcInsId);

    /**
     * 业务定义：修改服务人员
     * 
     * @date 2018年9月18日
     * @author R/Q
     */
    void updateServiceUser(@Param("serviceUser") String serviceUser, @Param("procInsId") String procInsId);
}
