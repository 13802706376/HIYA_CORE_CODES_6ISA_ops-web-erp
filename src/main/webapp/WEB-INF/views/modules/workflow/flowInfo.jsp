<%@ page contentType="text/html;charset=UTF-8" %>
<div class="act-process-right margin-top10 margin-right20">
    <div class="border-wrapper paddings margin-bottom10 margin-left20 act-infos">
        <c:set value="${flowInfo.orderInfoDto}" var="orderInfo"/>
        <c:if test="${orderInfo.hurryFlag =='1'}">
            <div class="padding-left0 act-infos-title">
                <span class="act-rword">急</span>
            </div>
        </c:if>
        <c:if test="${flowInfo.flowName=='jyk_flow'}">
            <%-- 折叠 --%>
            <div class="padding-left0 info-item info-item-title">
                订单（${orderInfo.orderTypeName}）
            </div>
            <div class="padding-left0 info-item">订单：${orderInfo.orderNo }</div>
            <div class="padding-left0 info-item">商户名称：${orderInfo.shopName }</div>
            <div class="padding-left0 info-item">购买的服务类型（${orderInfo.serviceTypeInfo }）</div>

            <div class="padding-left0 info-item">订单备注：${orderInfo.orderRemarks }</div>
            <div class="hide more-info moreinfo">
                    <%-- 展开 --%>
                <div class="padding-left0 info-item">商户联系人：${orderInfo.shopContactPerson }</div>
                <div class="padding-left0 info-item">商户联系方式：${orderInfo.shopContactPhone }</div>

                <div class="padding-left0 info-item">服务商名称：${orderInfo.agentName }</div>
                <div class="padding-left0 info-item">服务商联系人：${orderInfo.agentContactPerson }</div>
                <div class="padding-left0 info-item">服务商联系方式：${orderInfo.agentContactPhone }</div>

                <div class="padding-left0 info-item">运营顾问：${orderInfo.operationAdviser }</div>
                    <%-- v3.2没有运营专员 --%>
                <div class="padding-left0 info-item">购买时间：<fmt:formatDate value="${orderInfo.buyDate }"
                                                                          pattern="yyyy-MM-dd HH:mm"/></div>
            </div>
            <div class="toggle-more-info">
                <a class="disblock chevron-down togglemoreinfo" href="javascript:;">
                    <i class="icon-chevron-down"></i>
                </a>
            </div>
        </c:if>
        <c:if test="${flowInfo.flowName=='sdi_flow'}">
            <%-- 折叠 --%>
            <div class="padding-left0 info-item info-item-title">
                订单（${orderInfo.orderTypeName}）
            </div>
            <div class="padding-left0 info-item">订单：${orderInfo.orderNo }</div>
            <div class="padding-left0 info-item">商户名称：${orderInfo.shopName }</div>
            <div class="padding-left0 info-item">购买的服务类型（${orderInfo.serviceTypeInfo }）</div>
            <div class="padding-left0 info-item">订单备注：${orderInfo.orderRemarks }</div>
            <div class="hide more-info moreinfo">
                    <%-- 展开 --%>
                <div class="padding-left0 info-item">商户联系人：${orderInfo.shopContactPerson }</div>
                <div class="padding-left0 info-item">商户联系方式：${orderInfo.shopContactPhone }</div>
                <div class="padding-left0 info-item">服务商名称：${orderInfo.agentName }</div>
                <div class="padding-left0 info-item">服务商联系人：${orderInfo.agentContactPerson }</div>
                <div class="padding-left0 info-item">服务商联系方式：${orderInfo.agentContactPhone }</div>
                <div class="padding-left0 info-item">运营顾问：${orderInfo.operationAdviser }</div>
                    <%-- v3.2没有运营专员 --%>
                <div class="padding-left0 info-item">购买时间：<fmt:formatDate value="${orderInfo.buyDate }"
                                                                          pattern="yyyy-MM-dd HH:mm"/></div>
            </div>
            <div class="toggle-more-info">
                <a class="disblock chevron-down togglemoreinfo" href="javascript:;">
                    <i class="icon-chevron-down"></i>
                </a>
            </div>
        </c:if>
        <c:if test="${flowInfo.flowName=='payInto_flow'}">
            <div class="padding-left0 info-item">订单：${orderInfo.orderNo }</div>
            <div class="padding-left0 info-item">${orderInfo.shopName }</div>
            <div class="padding-left0 info-item">${orderInfo.serviceTypeInfo }</div>
            <div class="padding-left0 info-item">服务商名称：${orderInfo.agentName }</div>
        </c:if>
         <c:if test="${flowInfo.flowName=='promote_info_flow'}">
            <div class="padding-left0 info-item">订单：${orderInfo.orderNo }</div>
            <div class="padding-left0 info-item">${orderInfo.shopName }</div>
            <div class="padding-left0 info-item">${orderInfo.serviceTypeInfo }</div>
            <div class="padding-left0 info-item">服务商名称：${orderInfo.agentName }</div>
        </c:if>
    </div>

    <c:if test="${flowInfo.flowName=='jyk_flow'}">
        <c:set var="jykInfo" value="${flowInfo.jykInfoDto}"/>
        <div class="border-wrapper paddings margin-bottom10 margin-left20 act-infos">
            <div class="padding-left0 info-item info-item-title">
                聚引客
            </div>
            <c:forEach items="${jykInfo.splitGoods}" var="good">
                <div class="padding-left0 info-item">${good.goodName} * ${good.num}</div>
            </c:forEach>
            <div class="padding-left0 info-item">推广时间：<fmt:formatDate
                    value="${jykInfo.promotionTime}" pattern="yyyy-MM-dd HH:mm"/></div>
            <div class="padding-left0 info-item">推广通道：${jykInfo.promotionChannels}</div>
            <div class="padding-left0 info-item">策划专家：${jykInfo.planningExpert}</div>
            <div class="hide more-info moreinfo">
                <c:forEach items="${jykInfo.advAuditStatus}" var="adv" varStatus="status">
                    <div class="padding-left0 info-item">投放门店${status.count}：${adv.storeName}</div>
                    <div class="padding-left0 info-item">朋友圈广告开户：${adv.friendsAuditName}</div>
                    <div class="padding-left0 info-item">微博广告开户：${adv.weiboAuditName}</div>
                    <div class="padding-left0 info-item">陌陌广告开户：${adv.momoAuditName}</div>
                </c:forEach>
            </div>
            <div class="toggle-more-info">
                <a class="disblock chevron-down togglemoreinfo" href="javascript:;">
                    <i class="icon-chevron-down"></i>
                </a>
            </div>
        </div>
        <div class="border-wrapper paddings margin-bottom10 margin-left20 act-infos">
            <h3 class="act-infos-subtitle"><a href="${ctx}/promotion/erpPromoteMaterial/promoteMaterialDetail?procInsId=${procInstId}&id=${flowInfo.splitId}">推广资料</a>
            </h3>
        </div>
    </c:if>
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
    <div class="border-wrapper paddings margin-bottom10 margin-left20 act-infos-other">
        <c:set value="${fn:length(flowInfo.remarksInfo)}" var="remarksInfosSize"/>
        <h3 class="remarks-title">流程备注(<span id="remarksSize">${remarksInfosSize}</span>)
            <c:if test="${empty from}">
                <a href="javascript:;" title="添加备注" class="addRemarks add-remarks"><i class="icon-plus-sign"></i></a>
                <!-- <img alt="添加备注"
                     class="addRemarks"
                     align="right"
                     style="padding: 8px; cursor: pointer;"
                     width="20px"
                     height="20px"
                     src="${ctxStatic}/images/add_icon.png"/> -->
            </c:if>
        </h3>
        <div id="flowRemarks">
            
        </div>
    </div>
    <div class="padding-left20 act-infos-other">
        <!-- <h3>任务相关资料</h3> -->
        <c:forEach items="${flowInfo.orderInputDetails}" var="i" varStatus="status">
            <div class="accordion taskdetail" id="accordion1${status.index}">
                <div class="accordion-group">
                    <div class="accordion-heading">
                        <a class="accordion-toggle" data-toggle="collapse"
                           data-parent="#accordionInput${status.index}" href="#accordionInput${status.index}">
                            <span>${i.inputTaskName }</span>
                            <i class="icon-chevron-down"></i>
                        </a>
                    </div>
                    <div id="accordionInput${status.index}" class="accordion-body collapse">
                        <div class="accordion-inner">
                            <p>${i.inputDetail }</p>
                            <div class="padding-bottom10 act-infos-time">
                                <span>${fns:getUserById(i.createBy.id).name}</span>
                                <span class="inline-block floatright"><fmt:formatDate value="${i.createDate }"
                                                                                      type="both"
                                                                                      pattern="yyyy-MM-dd HH:mm:ss"/></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
        <!--
				<h4>${i.inputTaskName }</h4>
				<p>${i.inputDetail }</p>
				<div class="border-bottom-gray padding-bottom10 act-infos-time">
					<span>${fns:getUserById(i.createBy.id).name}</span>
					<span class="inline-block floatright">
					<fmt:formatDate value="${i.createDate }" type="both" pattern="yyyy-MM-dd HH:mm:ss"/>
					
					</span>
				</div>
			-->
        <!-- <h3>任务相关文件</h3> -->
        <c:forEach items="${flowInfo.orderFiles}" var="i" varStatus="status">
            <div class="accordion taskdetail" id="accordion${status.index}">
                <div class="accordion-group">
                    <div class="accordion-heading">
                        <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion${status.index}"
                           href="#accordioner${status.index}">
                            <span>${i.fileTitle }</span>
                            <i class="icon-chevron-down"></i>
                        </a>
                    </div>
                    <div id="accordioner${status.index}" class="accordion-body collapse">
                        <div class="accordion-inner">
                            <c:if test="${(fn:substringAfter(i.fileName,'.') !='jpg') && (fn:substringAfter(i.fileName,'.') !='png') && (fn:substringAfter(i.fileName,'.') !='JPEG')  }">
                                <!-- <p><a href="${fns:getConfig('domain.erp.res')}${i.filePath }" class="act-link">${i.fileName}</a></p> -->
                                <p>
                                    <a href="javascript: download('${fns:getConfig('domain.erp.res')}${i.filePath }', '${i.fileName}');"
                                       class="act-link">${i.fileName}</a>
                                </p>
                            </c:if>
                            <c:if test="${(fn:substringAfter(i.fileName,'.') =='jpg') || (fn:substringAfter(i.fileName,'.') =='png') || (fn:substringAfter(i.fileName,'.') =='JPEG')  }">
                                <p class="clreanbothp">
                                    <a href="${fns:getConfig('domain.erp.res')}${i.filePath }"
                                       class="act-link viewImage">${i.fileName }</a>
                                    <a class="downloadImage"
                                       data-href="${fns:getConfig('domain.erp.res')}${i.filePath }"
                                       data-filename="${i.fileName}">下载</a>
                                </p>
                            </c:if>
                            <div class="padding-bottom10 act-infos-time">
                                <span>${fns:getUserById(i.createBy.id).name}</span>
                                <span class="inline-block floatright"><fmt:formatDate value="${i.createDate }"
                                                                                      type="both"
                                                                                      pattern="yyyy-MM-dd HH:mm:ss"/></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- <h3>${i.fileTitle }</h3>
            <p><a href="${i.filePath }" class="act-link viewImage">${i.fileName }</a></p>
            <div class="border-bottom-gray padding-bottom10 act-infos-time">
            <span>${i.createBy.id }</span>
            <span class="inline-block floatright">2017-12-29 16:35:59${i.createDate }</span>
            </div> -->
        </c:forEach>
    </div>
</div>
