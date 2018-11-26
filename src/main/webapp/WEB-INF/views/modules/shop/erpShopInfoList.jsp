<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>商户管理管理</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
		.operationAdviserName{padding: 4px 0; width: 100px; height: 20px; line-height: 20px;}
	</style>
	<script type="text/javascript">
		$(document).ready(function() {
			/* $('.findstoreIdbyshopId').on('click', function(event) {
				event.preventDefault();
				var shopid = $(this).attr('data-shopid');
				$.get(ctx + '/store/basic/erpStoreInfo/findstoreIdbyshopId?shopid='+shopid, function(data) {
					if (data.storeid === false) {
						top.$.jBox.tip('该商户还没有主店', 'error');
					} else {
						location.href = ctx + '/store/basic/erpStoreInfo/urlStoreSynthesize/?shopId='+shopid+'&storeId='+data.storeid+'&isMain=1&from=erpStoreInfoList';
					}
				})
			}); */
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		function syncAll(){
			$.jBox.tip("正在后台同步数据，数据量较大，预计需要10分钟，请稍后再回来查看", 'loading', {
				timeout : 3000
			});
			$.post("${ctx}/shop/erpShopInfo/syncAll", {
				
			}, function(data) {
				
			});
		}

		function syncShopStores(){
			$.jBox.tip("同步商户门店请求已提交,请稍后查看", 'loading', {
				timeout : 3000
			});
			$.post("${ctx}/shop/erpShopInfo/syncShopStores", {
				
			}, function(data) {
				
			});
		}
		
		function syncAbnormalAll(){
			$.jBox.tip("正在后台同步数据，数据量较大，预计需要10分钟，请稍后再回来查看", 'loading', {
				timeout : 3000
			});
			$.post("${ctx}/shop/erpShopInfo/syncAbnormalAll", {
				
			}, function(data) {
				
			});
		}
		
		function syncAbnormal(zhangbeiId) {
			var flag = true;
			top.$.jBox.tip('正在同步...', 'loading', {
				timeout : 0,
				opacity: 0.6,
				// showClose: true,
				persistent: true
			});
			var closeBtn = $('<a style="position:fixed;top:45px;right:45px;font-size: 35px;color: #6f6f6f;text-decoration: none;cursor: pointer;font-weight: normal;z-index:99999999;" id="listPageCloseBtnjBox" title="关闭"><i class="icon-remove-circle"></i></a>').on('click', function(event) {
					event.preventDefault();
					$('body', top.document).find('#listPageCloseBtnjBox').remove();
					if (flag) {
						top.$.jBox.tip('正在后台同步商户资料...', 'info');
					}
				});
			var parent = $('body', top.document);
			parent.append(closeBtn);
			$.post("${ctx}/shop/erpShopInfo/syncAbnormal", 
				{
					'zhangbeiId' : zhangbeiId
				}, 
				function(data) {
					$('body', top.document).find('#listPageCloseBtnjBox').remove();
					if (data.code === '0') {
						flag = false;
						top.$.jBox.closeTip();
						if (data.attach.status === 'conflict') {
							conflict(zhangbeiId);
						} else if (data.attach.status === 'fail') {
							top.$.jBox.tip(data.attach.message, 'error');
						} else {
							top.$.jBox.tip(data.attach.message, 'info');
							location.reload();
						}			
					}
				});
		}
		/*
		*同步时 进件主体冲突
		*/
		function conflict(id){
			var submit = function (v, h, f) {
				var typeStr='';
			    if (v == true){
			    	//保留两者
			    	typeStr='Retain'
			    }else{
			    	 //直接覆盖
			    	typeStr='Cover'
			    }
			    $.post("${ctx}/shop/erpShopInfo/syncType",
					{
						'zhangbeiId' : id,
						'type':typeStr
					}, 
					function(data) {							
						if(data.code === '0'){
							//成功
							top.$.jBox.tip("同步成功", 'info');
							location.reload();
						}else{
							top.$.jBox.tip("同步异常", 'error');
						}
					});
			    return true;
			};
			$.jBox("<div style='padding:10px;'>OEM上的  “掌贝进件主体” 与ERP上 “掌贝进件主体” 不一致，请选择将OEM上  “掌贝进件主体” 的资料同步到ERP上的方式</div>", {
			    title: "掌贝进件主体冲突！",				    
			    buttons: { '保留两者': true, '直接覆盖': false},
			    submit: submit
			});
		}

		function exportInfo() {
			var url = ctx+'/shop/erpShopInfo/export';
			var formdata = {
				zhangbeiId: $('#searchForm').find('input[name="zhangbeiId"]').val(),
				name: $('#searchForm').find('input[name="name"]').val(),
				abbreviation: $('#searchForm').find('input[name="abbreviation"]').val(),
				contactName: $('#searchForm').find('input[name="contactName"]').val(),
				contactPhone: $('#searchForm').find('input[name="contactPhone"]').val(),
				operationAdviserId: $('#opsAdvisers').val()
			};
            var downUrl = url + '?jsonObject=' + encodeURIComponent(JSON.stringify(formdata));
            window.open(downUrl);
		}

		$(function() {
			$(document).on('mouseover', '.operationAdviserName', function() {
				$(this).children('.changeoperationAdviserName').show();
			}).on('mouseleave', '.operationAdviserName', function() {
				$(this).children('.changeoperationAdviserName').hide();
			}).on('click', '.changeoperationAdviserName', function(event) {
				event.preventDefault();
				var shopInfoId = $(this).attr('data-shopInfoId');
				var iframePage = '${ctx}/shop/erpShopInfo/findOperationAdviserList?shopInfoId='+shopInfoId;
				var zhangbeiId = $(this).attr('data-zbid');
				var trag = $(this).siblings('.operationAdviserName');
				top.$.jBox.open("iframe:" + iframePage, "选择运营顾问", 400, 550, {
					buttons:{"确定":"ok", "关闭":true},
					submit: function(v, h, f){
						if (v === 'ok') {
							var frame = $(h).find("#jbox-iframe").contents();
							var adviserId = frame.find("#adviserId").val();
							var adviserName = frame.find("#adviserName").val();
							if (adviserId && adviserName) {
								updateOpsAdviserOfShop(adviserId, adviserName, zhangbeiId, trag)
							}
						}
					},
					loaded: function(h){
						$(".jbox-content", top.document).css("overflow-y","hidden");
					}
				});
			}).on('click', '.jumplink', function(event) {
				event.preventDefault();
				var href = $(this).attr('href');
				var o = {
					zhangbeiId: getQueryString('zhangbeiId', href),
					operationAdviserName: $(this).attr('data-operationAdviserName') || '',
					shopId: $(this).attr('data-shopId')
				};
				sessionStorage.setItem('erpShopInfos', JSON.stringify(o));
				location.href = href;
			});

			function updateOpsAdviserOfShop(adviserId, adviserName, zhangbeiId, trag) {
				$.get(ctx+'/shop/erpShopInfo/updateOpsAdviserOfShop', {
					operationAdviserId: adviserId,
					zhangbeiId: zhangbeiId
				}, function(res) {
					if (res.code === '0') {
						trag.text(adviserName)
						$.jBox.tip('绑定新的运营顾问成功！')
					} else {
						$.jBox.tip('绑定新的运营顾问失败！')
					}
				})
			}

            $.get('${ctx}/shop/erpShopInfo/getOpsAdvisers', function (data) {
                var $opsAdviserId = $('#opsAdviserId').val();
                if (data) {
                    var html;
                    $.each(data, function (idx, val) {
                        html += '<option value="' + val.id + '"';
                        if (val.id == $opsAdviserId) {
                            html += ' selected';
                        }
                        html += '>' + val.name + '</option>';
                    });
                    $('#opsAdvisers').append(html).select2();
                }
            })
		})
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/shop/erpShopInfo/">商户管理列表</a></li>
	</ul>
	<div style="padding: 10px;">
		<form:form id="searchForm" modelAttribute="erpShopInfo" action="${ctx}/shop/erpShopInfo/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<input id="opsAdviserId" name="opsAdviserId" type="hidden"
				   value="${erpShopInfo.operationAdviserId}"/>
			<ul class="ul-form">
				<li><label>掌贝账号：</label>
					<form:input path="zhangbeiId" htmlEscape="false" maxlength="50" class="input-medium"/>
				</li>
				<li><label>商户名称：</label>
					<form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
				</li>
				<li><label>商户简称：</label>
					<form:input path="abbreviation" htmlEscape="false" maxlength="50" class="input-medium"/>
				</li>
				<li><label>商户联系人：</label>
					<form:input path="contactName" htmlEscape="false" maxlength="50" class="input-medium"/>
				</li>
				<li><label>联系电话：</label>
					<form:input path="contactPhone" htmlEscape="false" maxlength="20" class="input-medium"/>
				</li>
					<li><label>运营顾问：</label>
					<select id="opsAdvisers" name="operationAdviserId" style="width: 160px;">
						<option value= "">全部</option>
							<%--<c:forEach items="${opsAdvisers }" var="opsAdviser">
                                <option value="${opsAdviser.id }" <c:if test="${opsAdviser.id == erpShopInfo.operationAdviserId}"> selected</c:if>>${opsAdviser.name } </option>
                            </c:forEach>--%>
					</select>
				</li>
				<li class="btns">
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
					<input class="btn btn-warning" type="reset" value="重置"/>
					<shiro:hasPermission name="shop:list:button:batchSyncShop">
						<input class="btn btn-primary" type="button" value="同步" title="从OEM同步所有商户的基本信息" onclick="syncAll();"/>
						<!-- <input class="btn btn-primary" type="button" value="同步门店" onclick="syncShopStores();"/> -->
					</shiro:hasPermission>
					<shiro:hasPermission name="shop:list:button:batchSyncStore">
						<input class="btn btn-primary" type="button" value="同步门店" title="从OEM同步运营系统不存在的新商户的掌贝进件、微信进件、银联进件资料" onclick="syncAbnormalAll();"/>
					</shiro:hasPermission>
					<input class="btn btn-primary" type="button" value="导出" onclick="exportInfo()"/>
				</li>
				<li class="clearfix"></li>
			</ul>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>掌贝账号</th>
					<th>商户名称</th>
					<th>商户简称</th>
					<th>当前状态</th>
					<th>已添加门店数</th>
					<th>运营服务待处理</th>
					<th>聚引客待处理</th>
					<th>运营顾问</th>
					<shiro:hasPermission name="shop:erpShopInfo:view"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="erpShopInfo">
				<tr>
					<td>
						<a href="${ctx}/shop/erpShopInfo/form?id=${erpShopInfo.id}">
						${erpShopInfo.zhangbeiId}
						</a>
					</td>
					<td>
						<a href="${ctx}/shop/erpShopInfo/form?id=${erpShopInfo.id}">
						${erpShopInfo.name}
						</a>
					</td>
					<td>
						${erpShopInfo.abbreviation}
					</td>
					<td>
						<c:if test="${'Y' eq erpShopInfo.currentStatus}">可用</c:if>
						<c:if test="${'N' eq erpShopInfo.currentStatus}">停用</c:if>
					</td>
					<td>
							${erpShopInfo.storeCount}
					</td>
					<td>
							${erpShopInfo.pendingServiceNum != null ? erpShopInfo.pendingServiceNum : 0}
					</td>
					<td>
							${erpShopInfo.pendingJykNum != null ? erpShopInfo.pendingJykNum : 0}
					</td>
					<td>
						<div class="operationAdviserName">
							<span class="operationAdviserName">${erpShopInfo.operationAdviserName}</span>
							<shiro:hasPermission name="shop:bindingOperationAdviser">
							   <a href="javascript:;" class="changeoperationAdviserName hide" data-shopInfoId="${erpShopInfo.id}" data-zbid="${erpShopInfo.zhangbeiId}"><i class="icon-pencil"></i></a>
							</shiro:hasPermission>
						</div>
					</td>
					<td>
		    			<a href="${ctx}/store/basic/erpStoreInfo/serviceManage?id=${erpShopInfo.id}&zhangbeiId=${erpShopInfo.zhangbeiId}&operationAdviserName=${erpShopInfo.operationAdviserName}" class="jumplink" data-operationAdviserName="${erpShopInfo.operationAdviserName}" data-shopId="${erpShopInfo.id}">服务管理</a>&nbsp;&nbsp;&nbsp;
						   <shiro:hasPermission name="shop:list:button:auditManage">
		    			<a href="${ctx}/store/basic/erpStoreInfo/zhangbei?id=${erpShopInfo.id}&zhangbeiId=${erpShopInfo.zhangbeiId}" class="jumplink" data-operationAdviserName="${erpShopInfo.operationAdviserName}" data-shopId="${erpShopInfo.id}">进件管理</a>&nbsp;&nbsp;&nbsp;
		    			</shiro:hasPermission>
		    			<shiro:hasPermission name="shop:list:button:storeDataManage">
		    				<a href="${ctx}/store/basic/erpStoreInfo/urlErpShopInfoList?id=${erpShopInfo.id}&zhangbeiId=${erpShopInfo.zhangbeiId}" data-operationAdviserName="${erpShopInfo.operationAdviserName}" data-shopId="${erpShopInfo.id}"class="jumplink">门店资料管理</a>
	    				</shiro:hasPermission>
	    				<shiro:hasPermission name="shop:list:button:singleSyncStore">
		    				&nbsp;&nbsp;&nbsp;
		    				<a href="#" onclick="syncAbnormal(${erpShopInfo.zhangbeiId});" title="从OEM同步这个商户的掌贝进件、微信进件、银联进件资料">同步</a>
		    				<c:if test="${erpShopInfo.isAbnormal=='Y'}">
		    					<i class="icon-warning-sign" title="进件资料同步异常" onclick="conflict(${erpShopInfo.zhangbeiId});"> </i>
		    				</c:if>
	    				</shiro:hasPermission>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<div class="pagination">${page}</div>
	</div>
</body>
</html>
