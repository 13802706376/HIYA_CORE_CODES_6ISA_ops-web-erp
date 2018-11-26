<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${workFlowQueryForm.pageTotal > 1 }">	<!-- 大于1页时才显示分页栏 -->
	<div class="container-fluid">
		<div class="row" style="padding-top: 40px;">
			<div class="pagination">
				<ul>
					<c:choose>
						<c:when test="${workFlowQueryForm.pageNo > 1 }">
							<li><a href="#" data-no="1">首页</a></li>
							<li><a href="#" data-no="${workFlowQueryForm.pageNo-1 }">上一页</a></li>
						</c:when>
						<c:otherwise>
							<li class="disabled"><a>首页</a></li>
							<li class="disabled"><a>上一页</a></li>
						</c:otherwise>
					</c:choose>
					
					<c:choose>
						<c:when test="${workFlowQueryForm.pageTotal <= 5 }">
							<c:set var="begin" value="1" scope="page"></c:set>
							<c:set var="end" value="${workFlowQueryForm.pageTotal }" scope="page"></c:set>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${workFlowQueryForm.pageNo <=3 }">
									<c:set var="begin" value="1" scope="page"></c:set>
									<c:set var="end" value="5" scope="page"></c:set>
								</c:when>
								<c:when test="${workFlowQueryForm.pageNo >= workFlowQueryForm.pageTotal -2 }">
									<c:set var="begin" value="${workFlowQueryForm.pageTotal - 4 }" scope="page"></c:set>
									<c:set var="end" value="${workFlowQueryForm.pageTotal }" scope="page"></c:set>
								</c:when>
								<c:otherwise>
									<c:set var="begin" value="${workFlowQueryForm.pageNo - 2 }" scope="page"></c:set>
									<c:set var="end" value="${workFlowQueryForm.pageNo + 2 }" scope="page"></c:set>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
					
					<c:forEach begin="${begin }" end="${end }" var="i">
						<c:if test="${workFlowQueryForm.pageNo == i }"><li class="disabled"><a>${i }</a></li></c:if>
						<c:if test="${workFlowQueryForm.pageNo != i }"><li><a href="#" data-no="${i }">${i }</a></li></c:if>
					</c:forEach>
					
					<c:choose>
						<c:when test="${workFlowQueryForm.pageNo < workFlowQueryForm.pageTotal }">
							<li><a href="#" data-no="${workFlowQueryForm.pageNo+1 }">下一页</a></li>
							<li><a href="#" data-no="${workFlowQueryForm.pageTotal }">尾页</a></li>
						</c:when>
						<c:otherwise>
							<li class="disabled"><a>下一页</a></li>
							<li class="disabled"><a>尾页</a></li>
						</c:otherwise>
					</c:choose>

					<li class="disabled controls">
						<a href="javascript:">共 <span id="pageTotal">${workFlowQueryForm.pageTotal }</span> 页，当前 
							<input id="jumpNo" type="text" value="${workFlowQueryForm.pageNo}"/> / 
							<input id="jumpSize" type="text" value="${workFlowQueryForm.pageSize }"> 条，共 ${workFlowQueryForm.total} 条
						</a>
					</li>
				</ul>
			
				<script type="text/javascript">
					$(function(){
						function jump(id, no) {
							if (isNaN(no)) return false;
							$(id).val(no);
							submit(true);
						}
						
						$(".pagination a").click(function(){
							var no = $(this).data("no");
							jump('#pageNo', no);
							return false;
						});
						
						$("#jumpNo").keyup(function(e){
							if (e.which == 13) {
								var no = $(this).val();
								if (isNaN(no)) return false;
								no = parseInt(no);
								var pageTotal = parseInt($("#pageTotal").text());
								if (no > pageTotal) {
									no = pageTotal;
								}
								jump('#pageNo', no);
							}
						});
						
						$("#jumpSize").keyup(function(e){
							if (e.which == 13) {
								var no = $(this).val();
								jump('#pageSize', no);
							}
						});
					});
				</script>
			</div>
		</div>
	</div>
</c:if>
