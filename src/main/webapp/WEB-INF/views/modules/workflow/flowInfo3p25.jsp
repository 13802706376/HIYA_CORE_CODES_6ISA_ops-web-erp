<%@ page contentType="text/html;charset=UTF-8" %>
<div class="act-process-right margin-top10 margin-right20">
	<c:if test="${flowInfo.flowName == 'delivery_service_flow' ||flowInfo.flowName == 'visit_service_zhct_flow'}">
	<!-- 订单模块 -->
	    <div class="border-wrapper paddings margin-bottom10 margin-left20 act-infos">
	        <c:set value="${flowInfo.erpOrderOriginalInfo}" var="orderInfo"/>
            <%-- 折叠 --%>
            <div class="padding-left0 info-item info-item-title">订单</div>
            <div class="padding-left0 info-item">订单：${orderInfo.orderNumber }</div>
            <div class="padding-left0 info-item">商户名称：${orderInfo.shopName }</div>
            <div class="padding-left0 info-item">订单备注：${orderInfo.remark }</div>
            <div class="hide more-info moreinfo">
                    <%-- 展开 --%>
                <div class="padding-left0 info-item">商户联系人：${orderInfo.contactName }</div>
                <div class="padding-left0 info-item">商户联系方式：${orderInfo.contactNumber }</div>
                <div class="padding-left0 info-item">服务商名称：${orderInfo.agentName }</div>
                <div class="padding-left0 info-item">服务商联系人：${orderInfo.promoteContact }</div>
                <div class="padding-left0 info-item">服务商联系方式：${orderInfo.promotePhone }</div>
                <div class="padding-left0 info-item">购买时间：<fmt:formatDate value="${orderInfo.buyDate }" pattern="yyyy-MM-dd HH:mm"/></div>
            </div>
            <div class="toggle-more-info">
                <a class="disblock chevron-down togglemoreinfo" href="javascript:;">
                    <i class="icon-chevron-down"></i>
                </a>
            </div>
	    </div>
<!-- 订单处理进度-->
<!-- 		<div class="padding-left20 act-infos"> -->
<!--             <div class="accordion taskdetail" id="taskProcstStatus"> -->
<!--                 <div class="accordion-group"> -->
<!--                     <div class="accordion-heading"> -->
<!--                         <a class="accordion-toggle" data-toggle="collapse" data-parent="#taskProcst" href="#taskProcst"> -->
<!--                             <span>订单处理进度</span> -->
<!--                             <i class="icon-chevron-down"></i> -->
<!--                         </a> -->
<!--                     </div> -->
<!--                     <div id="taskProcst" class="accordion-body collapse"> -->
<%--                         <c:set value="${flowInfo.flowMap}" var="flowMap"/> --%>
<!--                         <div class="accordion-inner"> -->
<!--                             <div class="padding-left0 info-item">服务启动： -->
<%--                             	<c:if test="${flowMap.service_startup=='finish' }"><i class="icon-ok"></i></c:if> --%>
<%--                             	<c:if test="${flowMap.service_startup=='running' }"><i class=" icon-asterisk"></i></c:if> --%>
<!--                             </div> -->
<!--                             <div class="padding-left0 info-item">账号和支付开通： -->
<%--                             	<c:if test="${flowMap.account_pay_open=='finish' }"><i class="icon-ok"></i></c:if> --%>
<%--                             	<c:if test="${flowMap.account_pay_open=='running' }"><i class=" icon-asterisk"></i></c:if> --%>
<!--                             </div> -->
<!--                             <div class="padding-left0 info-item">首次营销策划： -->
<%--                             	<c:if test="${flowMap.marketing_planning=='finish' }"><i class="icon-ok"></i></c:if> --%>
<%--                             	<c:if test="${flowMap.marketing_planning=='running' }"><i class=" icon-asterisk"></i></c:if> --%>
<!--                             </div> -->
<!--                             <div class="padding-left0 info-item">物料服务： -->
<%--                             	<c:if test="${flowMap.material_service=='finish' }"><i class="icon-ok"></i></c:if> --%>
<%--                             	<c:if test="${flowMap.material_service=='running' }"><i class=" icon-asterisk"></i></c:if> --%>
<!--                             </div> -->
<!--                         </div> -->
<!--                     </div> -->
<!--                 </div> -->
<!--             </div> -->
<!-- 		</div> -->
<!-- 商户进件状态 -->
	    <div class="padding-left20 margin-top10 act-infos">
	        <div class="accordion taskdetail" id="accordionStatus">
	            <div class="accordion-group">
	                <div class="accordion-heading">
	                    <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordionInputStatus" href="#accordionInputStatus">
	                        <span>商户进件状态</span>
	                        <i class="icon-chevron-down"></i>
	                    </a>
	                </div>
	                <div id="accordionInputStatus" class="accordion-body collapse">
	                    <c:set value="${flowInfo.auditStatusInfoDto}" var="auditStatusInfo"/>
	                    <div class="accordion-inner">
                                <%--<div class="padding-left0 info-item">掌贝进件：${auditStatusInfo.mainStoreStatus}</div>
                                <div class="padding-left0 info-item">微信支付：${auditStatusInfo.wxPayStatus}</div>
                                <div class="padding-left0 info-item">银联支付：${auditStatusInfo.unionPayStatus}</div>--%>
                            <div class="info-sub-items">掌贝进件：${auditStatusInfo.mainStoreStatus}</div>
                            <div class="info-sub-items">
                                <div class="left-text">微信支付：</div>
                                <div class="right-table">
                                    <table class="table table-striped table-bordered table-condensed">
                                        <tr>
                                            <td>门店名称</td>
                                            <td>进件状态</td>
                                        </tr>
                                        <c:forEach items="${auditStatusInfo.wxPayAuditStatus}" var="wxPay">
                                            <tr>
                                                <td>${wxPay.storeName}</td>
                                                <td>${wxPay.auditStatusName}</td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </div>
                            </div>
                            <div class="info-sub-items">
                                <div class="left-text">银联支付：</div>
                                <div class="right-table">
                                    <table class="table table-striped table-bordered table-condensed">
                                        <tr>
                                            <td>门店名称</td>
                                            <td>进件状态</td>
                                        </tr>
                                        <c:forEach items="${auditStatusInfo.unionPayAuditStatus}" var="unionPay">
                                            <tr>
                                                <td>${unionPay.storeName}</td>
                                                <td>${unionPay.auditStatusName}</td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </div>
                            </div>
                            <div class="padding-left0 info-item">口碑账号：${auditStatusInfo.aliPayStatus }</div>
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
        <c:if test="${flowUsers!=null && flowUsers!=undefined}">
        <div class="padding-left20 margin-top10 act-infos">
            <div class="accordion taskdetail" id="accordionStatus">
                <div class="accordion-group">
                    <div class="accordion-heading">
                        <a class="accordion-toggle" data-toggle="collapse" data-parent="#oderUserLists" href="#oderUserLists">
                            <span>订单处理人</span>
                            <i class="icon-chevron-down"></i>
                        </a>
                    </div>
                    <div id="oderUserLists" class="accordion-body in collapse">
                        <div class="accordion-inner">
                            <div class="info-sub-items">
                                <table class="table table-bordered table-condensed">
                                    <tr>
                                        <th>角色</th>
                                        <th>任务处理人</th>
                                    </tr>
                                    <tr>
                                        <td>策划专家接口人</td>
                                        <td>${flowUsers.PlanningExpertInterfaceMan.name}</td>
                                    </tr>
                                    <tr>
                                        <td>策划专家</td>
                                        <td>${flowUsers.PlanningExpert.name}</td>
                                    </tr>
                                    <tr>
                                        <td>文案设计接口人</td>
                                        <td>${flowUsers.assignTextDesignInterfacePerson.name}</td>
                                    </tr>
                                    <tr>
                                        <td>文案策划</td>
                                        <td>${flowUsers.assignTextDesignPerson.name}</td>
                                    </tr>
                                    <tr>
                                        <td>设计师</td>
                                        <td>${flowUsers.designer.name}</td>
                                    </tr>
                                    <tr>
                                        <td>投放顾问接口人</td>
                                        <td>${flowUsers.assignConsultantInterface.name}</td>
                                    </tr>
                                    <tr>
                                        <td>投放顾问</td>
                                        <td>${flowUsers.assignConsultant.name}</td>
                                    </tr>
                                    <tr>
                                        <td>运营顾问</td>
                                        <td>${flowUsers.OperationAdviser.name}</td>
                                    </tr>
                                    <tr>
                                        <td>开户顾问</td>
                                        <td>${flowUsers.accountAdviser.name}</td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        </c:if>
        <!-- 订单处理人员 -->
        <div class="padding-left20 act-infos">
            <div class="accordion taskdetail" id="taskAssignee">
                <div class="accordion-group">
                    <div class="accordion-heading">
                        <a class="accordion-toggle" data-toggle="collapse" data-parent="#taskAssigneeWrapper" href="#taskAssigneeWrapper">
                            <span>订单处理人员</span>
                            <i class="icon-chevron-down"></i>
                        </a>
                    </div>
                    <div id="taskAssigneeWrapper" class="accordion-body in collapse">
                        <c:set value="${flowInfo.erpOrderFlowUsers}" var="users"/>
                        <div class="accordion-inner">
                            <div class="padding-left0 info-item">运营顾问：${users.OperationAdviser }</div>
                            <div class="padding-left0 info-item">开户顾问：${users.accountAdviser }</div>
                            <div class="padding-left0 info-item">物料顾问：${users.materialAdviser }</div>
                            <c:if test="${!empty users.operationManager }">
                                <div class="padding-left0 info-item">运营经理：${users.operationManager }</div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
    <!-- 流程备注 -->
    <div class="border-wrapper paddings margin-bottom10 margin-left20 act-infos-other">
        <c:set value="${fn:length(flowInfo.remarksInfo)}" var="remarksInfosSize"/>
        <h3 class="remarks-title info-item-title">流程备注(<span id="remarksSize">${remarksInfosSize}</span>)
            <c:if test="${empty from}">
                <a href="javascript:;" title="添加备注" class="addRemarks add-remarks"><i class="icon-plus-sign"></i></a>
            </c:if>
        </h3>
        <div id="flowRemarks">
            
        </div>
    </div>
</div>
<script type="text/javascript">
	
	//备注流程类型转换
	function flowTypeTrans(flowType){
		
		 if(flowType == 'shop_data_input_flow')
         {
         	flowType = "sdi_flow";
         }
         if(flowType == 'visit_service_flow')
         {
         	flowType = "V";
         }
         if(flowType == 'delivery_service_flow')
         {
         	flowType = "E";
         }
         
         return flowType;
	};
	
	
	
	//加载备注信息
	renderRemark(pageDataObject.procInsId);    
    function renderRemark(procInsId) {
    	console.log('流程Id:',procInsId);
        var htmlText = "";
        $.post("${ctx}/remarks/workflowRemarksInfo/findRemarksInfos?procInsId="+procInsId,
            function (data) {
	        	 var remarksList = data;
	             $("#remarksSize").text(remarksList.length);
	             for (var i = 0; i < remarksList.length; i++) {
	          	   if(i > 2){
	          		   htmlText += '<div class="accordion taskdetail remarkcontent hasMore" style="overflow:hidden;">';
	          	   }else{
	          		   htmlText += '<div class="accordion taskdetail remarkcontent" style="overflow:hidden;">';
	          	   }
	          	   htmlText += '<div class="remark-content floatleft"><div class="remark-text">';
	                 if(remarksList[i].remarkItemType === '1'){
	              	   htmlText += remarksList[i].remarkItemName+'-'+remarksList[i].remarkText + '</div>';
	                 }else if(remarksList[i].remarkItemType === '3'){
	              	   htmlText += remarksList[i].remarkItemName+ '</div>';
	                 }else if(remarksList[i].remarkItemType === '4'){
	                	 htmlText += remarksList[i].remarkItemName+ '</div><div>';
                         if(remarksList[i].remarkText){
                      	   var remarkiImg = remarksList[i].remarkText.split(';');
                      	   for(var j = 0;j < remarkiImg.length;j++){
                                 htmlText += '<a href="javascript:;" class="phosheng-class" imgUrl="${fileUrl}/'+remarkiImg[j]+'" elementId="phosheng'+i+j+'"><img id="phosheng'+i+j+'" style="width:58px;height:58px;margin-right:10px;" src="${fileUrl}/'+remarkiImg[j]+'" /></a>';
                             }
                         }
                         
                         htmlText += '</div>';
	                 }
	                 
	                 htmlText += '<div class="remark-rmk">';
	                 htmlText += '<span>' + remarksList[i].createUserName + '</span>';
	                 htmlText += '<span class="inline-block floatright">' + remarksList[i].createDate + '</span></div></div><div class="remove-remark"><a href="javascript:;" class="removeremark" data-id="' + remarksList[i].id + ',' + remarksList[i].createUserId + '" title="删除"><i class="icon-remove"></i></a></div></div>';
	             }
	             if (remarksList.length > 3) {
	                 htmlText += '<div id="toggleMore" style="padding-right: 4px;"><a href="javascript:;" class="toggleMoreRemark toggleMoreRemark-sheng"><i class="icon-chevron-down"></i></a></div>';
	             }
	             $("#flowRemarks").html(htmlText);
            });
    }
    
    $('body').off('click', '.phosheng-class').on('click', '.phosheng-class', function () {
        var tar = $(this).attr('elementId');
        var src = $(this).attr('imgUrl');
        
        const o = {
          title: '查看大图',
          width: tar.naturalWidth || window.innerWidth,
          height: tar.naturalHeight || window.innerHeight,
          opacity: 0,
          buttons: null,
          text: '<img src="'+src+'" alt="大图" />'
        };
        erpShopApp.showDialog(o);
    });
	
	$(document).ready(function () {
		var market = {
			init: function() {
				this.bindEvent();
			},
			bindEvent: function() {
				var self = this;
				$('body').off('click', '.addRemarks').on('click', '.addRemarks', function (event) {
                    // 正常打开
                    var task_def_key = pageDataObject.taskDefKey || erpApp.getQueryString('taskDefKey');
                    top.$.jBox.open("iframe:" + '${ctx}/remarks/workflowRemarksInfo/add?task_def_key='+task_def_key+'&procInstId='+pageDataObject.procInsId+'&serviceType='+pageDataObject.serviceType+'&visitType='+pageDataObject.visitType, "新增备注", 600, 500, {
                        buttons: {"确定": "ok", "关闭": true},
                        submit: function (v, h, f) {
                        	if (v === 'ok') {
                                var frame = $(h).find("#jbox-iframe").contents();
                                var paramJson = $.trim(frame.find("#remarks").val());
                                if (JSON.parse(paramJson).length > 0) {
                                	var jsonParse = JSON.parse(paramJson);
                                	for(var i = 0;i < jsonParse.length;i++){
                                		if(jsonParse[i].remarkItemType === '1' && !jsonParse[i].remarkText){
                                			erpShopApp.tip("必须输入备注内容！", 'error');
                                            return false;
                                		}
                                		
                                		if(jsonParse[i].remarkItemType === '4' && !jsonParse[i].remarkText){
                                			erpShopApp.tip("必须上传图片!", 'error');
                                            return false;
                                		}
                                	}
                                	$.get('${ctx}/remarks/workflowRemarksInfo/save?paramJson='+encodeURIComponent(paramJson), function (data) {
                                    	if (data.returnCode === 'success') {
                                    		renderRemark(pageDataObject.procInsId);
	                                        $.jBox.tip("添加备注成功", 'info');
	                                     } else {
	                                    	$.jBox.tip("添加备注失败", 'error');
	                                        return false;
	                                     }
                                    });
                                } else {
                                	erpShopApp.tip("至少要选择一条信息！", 'error');
                                    return false;
                                }
                            }
                        },
                        loaded: function (h) {
                            $(".jbox-content", top.document).css("overflow-y", "hidden");
                        }
                    });
                }).off('click', '.removeremark').on('click', '.removeremark', function (event) {
                    event.preventDefault();
                    var type = '提示';
                    var parents = $(this).parents('.remarkcontent');

                    var dataid = $(this).attr('data-id');
                    var id = dataid.split(',')[0];
                    var uid = dataid.split(',')[1];
//                     if (uid !== pageDataObject.userid) {
//                         $.jBox.tip("您不是该备注的创建人，不能删除！", 'info');
//                         return;
//                     }
                    var submit = function (v, h, f) {
                        if (v == 'ok') {
                            $.get('${ctx}/remarks/workflowRemarksInfo/delete', {id: id}, function (data) {
                                if (data.returnCode === 'success') {
                                    $.jBox.tip("删除备注成功！", 'info');
                                    parents.remove();
                                    var rm = $('#flowRemarks').find('.remarkcontent');
                                    $('#remarksSize').text(rm.size());
                                    if (rm.size() < 4) {
                                        $('#toggleMore').remove();
                                        rm.removeClass('hasMore').show();
                                    }else{
                                    	$("#flowRemarks .remarkcontent:lt(3)").removeClass('hasMore').show();
                                    }
                                } else {
                                    $.jBox.tip(data.returnMessage || "删除备注失败！", 'error');
                                }
                            });
                            return true;
                        }
                    };
                    top.$.jBox.confirm('确定要删除该备注吗？', type, submit);
                }).off('click', '.toggleMoreRemark').on('click', '.toggleMoreRemark', function (event) {
                    event.preventDefault();
                    if ($(this).hasClass('show')) {
                        $(this).removeClass('show');
                        $('#flowRemarks .hasMore').hide();
                    } else {
                        $('#flowRemarks .hasMore').show();
                        $(this).addClass('show');
                    }
                }).on('click', '.toggle-more-info .togglemoreinfo', function(event) {
                    event.preventDefault();
                    $(this).parent('.toggle-more-info').prev('.more-info').toggle();
                    $(this).toggleClass('show');
                });
			}		
		};
		market.init();
		
	});
</script>
