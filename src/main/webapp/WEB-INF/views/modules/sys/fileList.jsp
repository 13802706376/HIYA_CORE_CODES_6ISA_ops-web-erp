<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="com.yunnex.ops.erp.modules.sys.entity.FileMage"%>
<html>
<%
	List<FileMage> list = (List<FileMage>) request.getAttribute("list");
%>
<head>
<title>文件管理</title>
<meta name="decorator" content="default" />
<style>
.cursor-pointer {
	cursor: pointer;
}
</style>
<script type="text/javascript">
	$(function() {
		$(document).keydown(function(event) {
			var filePath = $("#pathAll").val();
			if(event.keyCode==192){
				findList(filePath);
			}
		})

		function findList(filePath) {
			var url = ctx + "/sys/dbutil/list1?" + "filePath=" + filePath;
			debugger;
			$
					.ajax({
						url : url,
						type : 'get',
						contentType : "application/json",
						success : function(data) {
							$("#contentTable tbody").empty();
							for (var i = 0; i < data.length; i++) {
								var tr="";
								if(data[i].path.indexOf(".")!=-1){
									tr = $("<tr class='cursor-pointer'><td>"
											+ data[i].fileName
											+ "</td><td>"
											+ data[i].path
											+ "</td><td><a filepath='"
											+ data[i].path
											+ "' class='deleteImg'>删除</a><a style='padding-left:5px;' filepath='"
											+ data[i].path
											+ "' class='checkImg'>查看</a></td></tr>");
								}else{
									tr = $("<tr class='cursor-pointer'><td>"
											+ data[i].fileName
											+ "</td><td>"
											+ data[i].path
											+ "</td><td></td></tr>");
								}
								$(tr).appendTo($("#contentTable tbody"));
								$(".checkImg").bind(
										"click",
										function() {
											var url = $(this).attr('filePath')
													.replace(/\\/g, '/').split(
															'upload')[1];
											window.open('${fileUrl}/upload'
													+ url);
										})
								$(".deleteImg")
										.bind(
												"click",
												function() {
													var filePath = $(this)
															.attr('filePath')
															.replace(/\\/g, '/')
															.replace(/\//g, '*');
													var url = ctx
															+ "/sys/dbutil/delFile?filePath="
															+ filePath
															+ "&type=del";
													$
															.ajax({
																url : url,
																type : 'get',
																contentType : "application/json",
																success : function(
																		data) {
																	var f = $(
																			"#pathAll")
																			.val();
																	findList(f);
																},
															});
												})
							}
						},
					});
		}

		$(".checkImg").bind(
				"click",
				function() {
					var url = $(this).attr('filePath').replace(/\\/g, '/')
							.split('upload')[1];
					window.open('${fileUrl}/upload' + url);
				})

		$(".deleteImg").bind(
				"click",
				function() {
					var filePath = $(this).attr('filePath').replace(/\\/g, '/')
							.replace(/\//g, '*');
					var url = ctx + "/sys/dbutil/delFile?filePath=" + filePath
							+ "&type=del";
					$.ajax({
						url : url,
						type : 'get',
						contentType : "application/json",
						success : function(data) {
							var f = $("#pathAll").val();
							findList(f);
						},
					});
				})

		$("input[type='file']").change(
				function() {
					var filePath = $("#pathAll").val().replace(/\//g, '*');
					var url = ctx + "/sys/dbutil/addFile?filePath=" + filePath
							+ "&type=add";

					var fileList = new FormData();
					fileList.append("file", $(this)[0].files[0]);

					$.ajax({
						url : url,
						type : 'post',
						processData : false,
						contentType : false,
						data : fileList,
						success : function(data) {
							var f = $("#pathAll").val();
							findList(f);
						}
					})
				});
	});
</script>
</head>
<body>
	<div class="uploadWrapper">
		<div>
			<a>上传文件路径 ：<input id="pathAll" value="${filePath }" readonly="readonly"/></a>
		</div>
		<div class="ivu-upload" style="display: inline-block; width: 76px;">
			<div class="ivu-upload ivu-upload-drag">
				<input type="file" accept="image/*" class="ivu-upload-input">
			</div>
		</div>
	</div>
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>文件名称</th>
				<th>文件路径</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list }" var="fileItem" varStatus="fileStatus">
				<tr>
					<td>${fileItem.fileName }</td>
					<td>${fileItem.path }</td>
					<td class="cursor-pointer">
					<c:if test="${fn:contains(fileItem.fileName,'.')}">
						<a filePath="${fileItem.path }" class="deleteImg">删除</a> 
						<a filePath="${fileItem.path }" class="checkImg">查看</a></td>
					</c:if>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>