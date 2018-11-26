<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>任务流模型管理</title>
	<meta name="decorator" content="default"/>
	<style>
		.mask-widget-singer .operate-up {
	   	height: 40px;
	    margin: 0 15px;
	    margin-top: 50px;
	    cursor: pointer;
		}
		#cacheClear,#overJyk{
		float: right;
		display:none;
	    padding-left: 20px;
		}
		.mask-widget-singercache{
		    width: 1300px;
		    border: 1px solid #ccc;
		    max-height: 600px;
		    position: fixed;
		    top: 200px;
		    left: 50%;
		    margin-left: -730px;
		    background: #fff;
		    padding-bottom: 40px;
		    z-index: 9999;
		}
		#cacheClear>button{
		    color: #fff;
		    text-shadow: 0 -1px 0 rgba(0,0,0,0.25);
		    background-color: #3daae9;
		    background-repeat: repeat-x;
		    background-image: linear-gradient(to bottom,#46aeea,#2fa4e7);
		    border-color: rgba(0,0,0,0.1) rgba(0,0,0,0.1) rgba(0,0,0,0.25);
		}
		#overJyk>button{
		    color: #fff;
		    text-shadow: 0 -1px 0 rgba(0,0,0,0.25);
		    background-color: #3daae9;
		    background-repeat: repeat-x;
		    background-image: linear-gradient(to bottom,#46aeea,#2fa4e7);
		    border-color: rgba(0,0,0,0.1) rgba(0,0,0,0.1) rgba(0,0,0,0.25);
		}
		.cacheclose {
		    float: right;
		    font-size: 30px;
		    font-weight: bold;
		    line-height: 20px;
		    color: #000;
		    text-shadow: 0 1px 0 #fff;
		    opacity: .2;
		    /* top: 25px; */
		    margin-top: -59px;
		    filter: alpha(opacity=20);
	        cursor: pointer;
		}
	</style>
	<script type="text/javascript">
		$(document).keydown(function(event){
			var sty=$("#cacheClear").attr("data");
			if(event.keyCode==192&&sty=="hide"){
				$("#cacheClear").removeAttr("data");
				$("#cacheClear").attr("data","show");
				$("#overJyk").show();
				$("#cacheClear").show();
			}else if(event.keyCode==192&&sty=="show"){
				$("#cacheClear").removeAttr("data");
				$("#cacheClear").attr("data","hide");
				$("#cacheClear").hide();
				$("#overJyk").hide();
			}
		})
		
		$(document).ready(function(){
			top.$.jBox.tip.mess = null;
			$("#cacheClear button").click(function(){
				var h=createCacheHtml();
				$(h).appendTo($("#cacheClear"));
			})
		});
		
		function colseCache(){
			$("#yinCangCache").remove();
		}
		function cacheService(){
			var cacheName=$("#cacheName").val();
			var cacheKey=$("#cacheKey").val();
			var url=ctx + "/sys/cache/clearCacheByKey?"+"cacheName="+cacheName+"&cacheKey="+cacheKey;
			$.ajax({url:url,success:function(result){
		        $(".cacheclose").prev().html(result);
		    }});
		}
		function createCacheHtml(){
			return "<div id='yinCangCache' class='mask-widget-singercache'><div class='operate-up'><span>输入key，清理缓存</span></div><p class='padding-20'>cacheName : <input id='cacheName'> cacheKey : <input id='cacheKey'><button type='button' onclick='cacheService()' class='btn btn-primary' style='margin-left: 20px;'>清理</button></p> <div class='act-top positionrelative overview'></div> <div class='cacheclose' onclick='colseCache()'>×</div></div>";
		}
		
		function page(n,s){
        	location = '${ctx}/act/model/?pageNo='+n+'&pageSize='+s;
        }
		function updateCategory(id, category){
			$.jBox($("#categoryBox").html(), {title:"设置分类", buttons:{"关闭":true}, submit: function(){}});
			$("#categoryBoxId").val(id);
			$("#categoryBoxCategory").val(category);
		}
		function overJyk(){
			var url = ctx + "/team/erpTeamTotal/overJyk";
			$.ajax({
				url : url,
				type : 'get',
				contentType : "application/json",
				success : function(data) {
					$("#overJyk>button").html("批量执行中...");
					setTimeout(function(){$("#overJyk>button").html("结束已完成聚引客分支");},5000);
				},
			});
		}
	</script>
	<script type="text/template" id="categoryBox">
		<form id="categoryForm" action="${ctx}/act/model/updateCategory" method="post" enctype="multipart/form-data"
			style="text-align:center;" class="form-search" onsubmit="loading('正在分类，请稍等...');"><br/>
			<input id="categoryBoxId" type="hidden" name="id" value="" />
			<select id="categoryBoxCategory" name="category">
				<option value="">无分类</option>
				<c:forEach items="${fns:getDictList('act_category')}" var="dict">
					<option value="${dict.value}">${dict.label}</option>
				</c:forEach>
			</select>
			<br/><br/>　　
			<input id="categorySubmit" class="btn btn-primary" type="submit" value="   保    存   "/>　　
		</form>
	</script>
	
	<link href="${ctxStatic}/css/hiddenfunction/hiddenfunction.min.css?v=${staticVersion}" rel="stylesheet" type="text/css" />
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/act/model/">任务流模型管理</a></li>
		<li><a href="${ctx}/act/model/create">新建任务流模型</a></li>
	</ul>
	<form id="searchForm" action="${ctx}/act/model/" method="post" class="breadcrumb form-search">
		<select id="category" name="category" class="input-medium">
			<option value="">全部分类</option>
			<c:forEach items="${fns:getDictList('act_category')}" var="dict">
				<option value="${dict.value}" ${dict.value==category?'selected':''}>${dict.label}</option>
			</c:forEach>
		</select>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		<div id="cacheClear" data="hide"><button type="button" class="btn">缓存清理</button></div>
		<div id="overJyk" data="hide"><button type="button" class="btn" onclick="overJyk()">结束已完成聚引客分支</button></div>
		<div id="rootNode"></div>
	</form>
	<sys:message content="${message}"/>
	<table class="table table-striped table-bordered table-condensed table-nowrap">
		<thead>
			<tr>
				<th>流程分类</th>
				<th>模型ID</th>
				<th>模型标识</th>
				<th>模型名称</th>
				<th>版本号</th>
				<th>创建时间</th>
				<th>最后更新时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="model">
				<tr>
					<td><a href="javascript:updateCategory('${model.id}', '${model.category}')" title="设置分类">${fns:getDictLabel(model.category,'act_category','无分类')}</a></td>
					<td>${model.id}</td>
					<td>${model.key}</td>
					<td>${model.name}</td>
					<td><b title='流程版本号'>V: ${model.version}</b></td>
					<td><fmt:formatDate value="${model.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td><fmt:formatDate value="${model.lastUpdateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td>
						<a href="${pageContext.request.contextPath}/act/process-editor/modeler.jsp?modelId=${model.id}" target="_blank">编辑</a>
						<a href="${ctx}/act/model/deploy?id=${model.id}" onclick="return confirmx('确认要部署该模型吗？', this.href)">部署</a>
						
						<a href="${ctx}/act/model/export?id=${model.id}" target="_blank">导出</a>
	                    <a href="${ctx}/act/model/delete?id=${model.id}" onclick="return confirmx('确认要删除该模型吗？', this.href)">删除</a>
	                    <!--  -->
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	
	<script src="${ctxStatic}/scripts/manifest.min.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/scripts/vendor.min.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/scripts/hiddenfunction/hiddenfunction.min.js?v=${staticVersion}" type="text/javascript"></script>
<%-- 	<script src="http://localhost:7777/hiddenfunction/hiddenfunction.min.js?v=${staticVersion}" type="text/javascript"></script> --%>
</body>
</html>
