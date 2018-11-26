<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>流程进度</title>
	<meta name="decorator" content="default"/>
	<link href="${ctxStatic}/common/act.css?v=df215d15sd1521ds5" type="text/css" rel="stylesheet" />
	<style type="text/css">
		.act-rs-item.task-detail-items .act-rs-item .act-process-left-footer{ float: inherit; clear: both; }
		.act-rs-item.task-detail-items .act-rs-item .act-process-left-footer .footer-info{ float: inherit; }
		.act-rs-item.task-detail-items .act-rs-item .act-process-left-footer .footer-btn{ margin-top: -60px; }
		.act-rs-item.task-detail-items .act-rs-item .act-rs-left,.act-rs-item.task-detail-items .act-rs-item .act-rs-right{margin-bottom: 10px;}
		.act-rs-item.task-detail-items .act-rs-item .act-rs-item{ padding-bottom: 0px; }
		.aps .act-process-timeline-left{ width: 60%; }
		.act-process-timeline-title .ptt-name:nth-of-type(1){ width: 50%; }
		.act-process-timeline-title .ptt-name:nth-of-type(2){ width: 20%; }
		.act-process-timeline-title .ptt-name:nth-of-type(3){ width: 20%; }
		.timeline-wrap-title .twt-title{ width: 50%; }
		.timeline-wrap-title .twt-name:nth-of-type(1){ width: 20%; }
		.timeline-wrap-title .twt-name:nth-of-type(2){ width: 20%; }
		.timeline-wrap-content .floatleft{ width: 69.5%; }
		.act-infos-other .remarks-title{ line-height: 40px;font-size: 18px;color: #757575;font-weight: normal;}
		.remarkcontent{ min-height: 50px;border-bottom: #ededed solid 1px;overflow: hidden; }
		#flowRemarks .hasMore{ display: none; }
		.remark-content .remark-text{margin-bottom: 5px; word-break: break-word;}
		.remark-content{ width: 89%; }
		.remove-remark{ margin-left: 90%; line-height: 36px; overflow:hidden; text-indent: 1em;}
		.removeremark{ padding-top: 5px; font-size: 16px; color: #777; text-decoration: none; }
		#toggleMore{ padding-right: 20px; height: 40px;}
		.toggleMoreRemark{ border:#157ab5 solid 1px; border-radius: 50%; width: 20px; height: 20px; line-height: 20px; display: inline-block; float: right; text-align: center; transition: all .3s;}
		.toggleMoreRemark.show{-ms-transform:rotate(180deg); -moz-transform:rotate(180deg);-webkit-transform:rotate(180deg);-o-transform:rotate(180deg);transform: rotate(180deg)}
		.add-remarks{padding: 8px; cursor: pointer; width:20px;height:20px}
		
		.toggleMoreRemark-sheng {
		    display: inline-block;
		    background-color: rgb(47, 164, 231);
		    width: 20px;
		    height: 19px;
		    color: rgb(255, 255, 255);
		    text-align: center;
		    padding-top: 1px;
		    text-decoration: none;
		    border-radius: 50%;
		}
	</style>
	
	<script type="text/javascript">
		var pageDataObject = {
			uploadfileurl: "${fileUrl}",
			userid: "${fns:getUser().id}",
			procInsId:'${procInsId}'
		};
	</script>
</head>
<body>
	<input type="hidden" id="taskHistoicFlowTaskId" value="${procInsId}">
	<input type="hidden" id="taskHistoicFlowTaskUrl" value="${ctx}/workflow/taskHistoicFlowByProcInsId?procInsId=${procInsId}">
	<div class="act-top positionrelative">
		<div class="act-top-left positionrabsolute">
			<a href="javascript:;" class="comeback" onclick="history.back();">返回</a>
		</div>
	 	<ul class="nav nav-tabs">
			<li class="marginleft60 acttitle active">
				<a href="javascript:;">流程进度</a>
			</li>
		</ul>
	</div>
	<div id="taskHistoricFlowPage"></div>
	<script type="text/javascript">
		window.__taskHistoicFlowTaskUrl__ = '${ctx}/workflow/taskHistoicFlowByProcInsId?procInsId=${procInsId}';
		window.__flow = 'jyk_flow';
        window.__configuploadurl__ = '${fns:getConfig("domain.erp.res")}';
		function tipNew(content, type, o) {
			type = type || 'info';
			o = o || {};
			closedTipNew();
			top.$.jBox.tip(content, type, o);
		}

		function closedTipNew() {
			$.jBox.closeTip();
			top.$.jBox.closeTip();
		}
		
		function confirmNew(title, cb, type) {
			type = type || '提示';
			var self = this;
			var submit = function(v, h, f) {
				if (v == 'ok') {
					$.jBox.tip("正在处理，请稍等...", 'loading', {
						timeout : 0,
						persistent : true
					});
					typeof cb === 'function' && cb();
					return true;
				}
			};
			top.$.jBox.confirm(title, type, submit);
		}

		function addWorkflowRemarks(taskId, flowType, cb) {
			top.$.jBox.open("iframe:" + '${ctx}/remarks/workflowRemarksInfo/add', "新增备注", 550, 230, {
				buttons:{"确定":"ok", "关闭":true},
				submit: function(v, h, f){	
					if (v === 'ok') {
						var frame = $(h).find("#jbox-iframe").contents();
						var remarks = $.trim(frame.find("#remarks").val());
						if (remarks !== '') {
							if (remarks.length > 100) {
								$.jBox.tip("备注内容不能超过100个字符！", 'error');
								return false;
							}
							$.post("${ctx}/remarks/workflowRemarksInfo/save", 
								{
									taskId: taskId,
									flowType: flowType,
									remarkText: remarks
								}, 
								function(data) {
									if (data.code === '1') {
										$.jBox.tip(data.message, 'error');
										return false;
									} else {
										$.jBox.tip("添加备注成功", 'info');
										typeof cb === 'function' && cb()
									}
								});
						} else {
							$.jBox.tip("请输入有效的备注内容！", 'error');
							return false;
						}
					}
				},
				loaded: function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
				}
			});
		}

		$(function() {
			var fromPage = getQueryString('from', location.href);
			var fromPageArr = ['statistics', 'serviceManage'];
			if(fromPageArr.indexOf(fromPage) !== -1) {
				$('.act-top > .act-top-left').hide();
			}
			var viewImage = function(imgUrl) {
				var html = '<div class="view-image-wrap"><img src="'+imgUrl+'" alt="图片预览" style="max-width:100%;" /></div>';
				top.$.jBox(html, {title:"图片预览", width: 900, buttons: {}});
			};
			$('body').on('click', '.act-link', function(event) {
				event.preventDefault();
				var imgUrl = $(this).attr('href');
				if ($(this).hasClass('viewImage')) {
					viewImage(imgUrl);
				} else {
					var filename = $(this).attr('data-filename');
					download(imgUrl, filename);
				}
			}).on('click', '.downloadImage', function(event) {
				event.preventDefault();
				var imgUrl = $(this).attr('data-href');
				var filename = $(this).attr('data-filename');
				download(imgUrl, filename);
			}).off('click', '.addRemarks').on('click', '.addRemarks', function (event) {
				// 正常打开
                var task_def_key = pageDataObject.taskDefKey || erpApp.getQueryString('taskDefKey');
                console.log("222:",task_def_key);
                top.$.jBox.open("iframe:" + '${ctx}/remarks/workflowRemarksInfo/add?task_def_key='+task_def_key+'&procInstId=${procInstId}', "新增备注", 600, 500, {
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
                                        renderRemark('${procInstId}');
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
//                 if (uid !== pageDataObject.userid) {
//                     $.jBox.tip("您不是该备注的创建人，不能删除！", 'info');
//                     return;
//                 }
                
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
            }).on('click', '.togglemoreinfo', function(event) {
            	event.preventDefault();
            	$(this).parent('.toggle-more-info').prev('.more-info').toggle();
            	$(this).toggleClass('show');
            });
			
			renderRemark('${procInstId}');    
			function renderRemark(procInsId) {
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
			//文件下载
			function download(downloadpath, filename) {
		        filename = encodeURIComponent(encodeURIComponent(filename));
		        window.location.href = "${ctx}/workfile/file/downloadAmachment?downloadUrl=" + downloadpath + "&realFileName=" + filename;
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
		});
	</script>
	<script src="${ctxStatic}/scripts/manifest.min.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/scripts/vendor.min.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/scripts/workflow/taskhistoricflow.min.js?v=${staticVersion}" type="text/javascript"></script>
	<!-- <script src="//localhost:7777/workflow/taskhistoricflow.min.js?v=${staticVersion}" type="text/javascript"></script> -->
	<!-- <script type="text/javascript" src="${ctxStatic}/workflow/manifest.min.js?2b399e61cdcd520fbc4d"></script>
	<script type="text/javascript" src="${ctxStatic}/workflow/vendor.min.js?2b399e61cdcd520fbc4d"></script>
	<script type="text/javascript" src="${ctxStatic}/workflow/module/taskhistoricflow.min.js?2b399e61cdcd5209152"></script> -->
</body>
