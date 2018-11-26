<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%String taskDefKey = request.getParameter("taskDefKey");%>
<html>
<head>
    <title>我的任务</title>
    <meta name="decorator" content="default"/>
    <link href="${ctxStatic}/common/act.css?v=${staticVersion}sd1521ds5" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" href="${ctxStatic}/css/promotion/promotion.min.css?v=${staticVersion}">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/common/shopentry.min.css">
    <script type="text/javascript" src="${ctxStatic}/common/act.js?v=${staticVersion}"></script>
    <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${ctxStatic}/ueditor/lang/zh-cn/zh-cn.js"></script>
    <style type="text/css">
        .act-rs-item.task-detail-items .act-rs-item .act-process-left-footer {
            float: inherit;
            clear: both;
        }

        .act-rs-item.task-detail-items .act-rs-item .act-process-left-footer .footer-info {
            float: inherit;
        }

        .act-rs-item.task-detail-items .act-rs-item .act-process-left-footer .footer-btn {
            margin-top: -60px;
        }

        .act-rs-item.task-detail-items .act-rs-item .act-rs-left, .act-rs-item.task-detail-items .act-rs-item .act-rs-right {
            margin-bottom: 10px;
        }

        .act-rs-item.task-detail-items .act-rs-item .act-rs-item {
            padding-bottom: 0px;
        }

        .task-detail-items .taskDetail .act-rs-title, .task-detail-items .taskDetail .act-rs-form {
            padding-left: 0px;
            padding-bottom: 0px;
        }

        .act-infos-other h3 {
            line-height: 36px;
            font-size: 16px;
            color: #157ab5;
            font-weight: normal;
        }

        .act-infos-other h4 {
            line-height: 30px;
            font-size: 16px;
            color: #555;
            font-weight: normal;
        }

        .act-infos-other .remarks-title {
            line-height: 40px;
        }

        .remarkcontent {
            min-height: 50px;
            border-bottom: #ededed solid 1px;
            overflow: hidden;
        }

        #flowRemarks .hasMore {
            display: none;
        }

        .remark-content .remark-text {
            margin-bottom: 5px;
            word-break: break-word;
        }

        .remark-content {
            width: 89%;
        }

        .remove-remark {
            margin-left: 90%;
            line-height: 36px;
            overflow: hidden;
            text-indent: 1em;
        }

        .removeremark {
            padding-top: 5px;
            font-size: 16px;
            color: #777;
            text-decoration: none;
        }

        #toggleMore {
            padding-right: 20px;
            height: 40px;
        }

        .toggleMoreRemark {
            border: #157ab5 solid 1px;
            border-radius: 50%;
            width: 20px;
            height: 20px;
            line-height: 20px;
            display: inline-block;
            float: right;
            text-align: center;
            transition: all .3s;
        }

        #restart-box {
            position: relative;
        }

        .mask-status #selectReason, .mask-status #detailReason {
            background-color: #f3f3f3;
        }

        #restart-mask {
            display: none;
            position: absolute;
            top: 0;
            right: 0;
            left: 0;
            bottom: 0;
            height: inherit;
            width: inherit;
        }

        .control-label {
            padding-right: 5px;
        }

        .control-label .require {
            vertical-align: middle;
        }
        
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
            username: "${fns:getUser().name}",
            staticVersion: '${staticVersion}',
            runSaveMessageRecord: true
        };
        
        localStorage.setItem('taskDefKey', '<%=request.getParameter("taskDefKey") %>');
        
        function updatePage() {
            var detailType = getQueryString('detailType', location.href);
            var beiyi_task_list_page = top.globalUtil.sessionStorageGet('beiyi_task_list_page');
            var taskListFormDataBeiyi = top.globalUtil.sessionStorageGet('taskListFormDataBeiyi');
            var taskListFormData = top.globalUtil.sessionStorageGet('taskListFormData');
            var teamTaskListFormData = top.globalUtil.sessionStorageGet('teamTaskListFormData');
            var beiyiTaskFlowToWhere = top.globalUtil.sessionStorageGet('beiyiTaskFlowToWhere');
            var followTaskListData = top.globalUtil.sessionStorageGet('followTaskListData');
            var is_beiyi_task_flow = location.pathname.indexOf('3p25') !== -1;
            var is_team_task_flow = detailType === 'teamtasklist';
            var urlParams = function(type) {
                var str = '';
                var sgMap = {
                    beiyi_task_list_page: taskListFormDataBeiyi,
                    is_team_task_flow: teamTaskListFormData,
                    task_list_form_data: taskListFormData,
                    follow_task_list_data: followTaskListData
                };
                //var o = is_beiyi_task_flow || beiyi_task_list_page ? taskListFormDataBeiyi : is_team_task_flow ? teamTaskListFormData : taskListFormData;
                var o = sgMap[type];
                if (o) {
                    for (var i in o) {
                        str += i + '=' + o[i] + '&'
                    }
                    str = '?' + str
                }
                return str.slice(0, -1);
            };
            if (beiyi_task_list_page) {
                location.href = beiyiTaskFlowToWhere + urlParams('beiyi_task_list_page');
            } else {
                switch (detailType) {
                    case 'tasklist':
                        location.href = '${ctx}/workflow/tasklist' + urlParams('task_list_form_data');
                        break;
                    case 'teamtasklist':
                        location.href = '${ctx}/workflow/teamtasklist' + urlParams('is_team_task_flow');
                        break;
                    case 'followTaskList':
                        location.href = '${ctx}/workflow/followTaskList' + urlParams('follow_task_list_data');
                        break;
                    case 'productionTeamTaskList':
                        location.href = '${ctx}/workflow/productionTeamTaskList' + urlParams('task_list_form_data');
                        break;
                    case 'pendingProductionTaskList':
                        location.href = '${ctx}/workflow/pendingProductionTaskList' + urlParams('task_list_form_data');
                        break;
                    case 'operatingServices':
                        location.href = beiyiTaskFlowToWhere + urlParams('beiyi_task_list_page');
                        break;
                    default:
                        location.href = '${ctx}/workflow/tasklist' + urlParams('task_list_form_data');
                        break;
                }
            }
        };

        function sureAccomplish(id) {
            var isRestart = $('input[name="isRestart"]:checked').val();
            var selectReason = $.trim($('#selectReason').val());
            var detailReason = $.trim($('#detailReason').val());
            var nextDatereason = $.trim($('#nextDatereason').val());

            if (!selectReason) {
                $.jBox.tip("请选择暂停理由。", "info");
                return;
            }
            if (!nextDatereason) {
                $.jBox.tip("请选择下次与商户沟通是否进行服务的时间。", "info");
                return;
            }
            if (!isRestart) {
                $.jBox.tip("请选择是否重启服务。", "info");
                return;
            }
            if (isRestart == 'N') {
                //选择了否的时候 不确认弹框
                $.post(ctx + '/order/erpOrderSplitInfo/updateSuspendData', {
                        id: id,
                        nextContactTime: nextDatereason,
                        suspendReason: selectReason,
                        suspendReasonContent: detailReason
                    },
                    function (data) {
                        $.jBox.closeTip();
                        if (data.code === '0') {
                            $.jBox.tip('处理成功！');
                            window.location.reload();
                        } else {
                            $.jBox.tip('处理失败，请稍后重试！');
                        }
                    });
                return false;
            }
            var submit = function (v, h, f) {
                var url = isRestart === 'Y' ? ctx + "/order/erpOrderSplitInfo/restart" : ctx + "/order/erpOrderSplitInfo/updateSuspendData";
                if (v === 'ok') {
                    $.jBox.tip("正在修改，请稍等...", 'loading', {
                        timeout: 3000,
                        persistent: true
                    });
                    $.post(url, {
                            id: id,
                            nextContactTime: nextDatereason,
                            suspendReason: selectReason,
                            suspendReasonContent: detailReason
                        },
                        function (data) {
                            $.jBox.closeTip();
                            if (data.code === '0') {
                                $.jBox.tip('处理成功！');
                                if (isRestart === 'Y') {
                                    updatePage();
                                } else {
                                    window.location.reload();
                                }
                            } else {
                                $.jBox.tip('处理失败，请稍后重试！');
                            }
                        });
                }
                return true;
            };
            top.$.jBox.confirm("确定处理与商户沟通启动服务吗？", "提示", submit);
        }

        $(document).ready(function () {
            $(".listSubmit").hide();
            if ('${readOnly}' == 'true' || '${userMatch}' == 'false') {
                $("button").attr("disabled","disabled");
                $("input").attr("disabled","disabled");
                $("select").attr("disabled","disabled");
                $("radio").attr("disabled","disabled");
                $("textarea").attr("disabled","disabled");
            }
            
            var act = {
                viewImage: function (imgUrl) {
                    var html = '<div class="view-image-wrap"><img src="' + imgUrl + '" alt="图片预览" style="max-width:100%;" /></div>';
                    top.$.jBox(html, {title: "图片预览", width: 900, buttons: {}});
                },
                downloadFile: function (fileUrl) {
                    var a = document.createElement('a');
                    var url = window.URL.createObjectURL(fileUrl);
                    var filename = fileUrl;
                    a.href = url;
                    a.download = filename;
                    a.click();
                    window.URL.revokeObjectURL(url);
                },
                bindEvent: function () {
                    var self = this;

                    $('body').on('click', '.dorpdownbotm', function (event) {
                        event.preventDefault();
                        $(this).toggleClass('up');
                        $(this).parents('.act-content-box').toggleClass('uppansl');
                        $(this).siblings('.act-rs-item').eq(0).show().toggleClass('show');
                    }).on('click', '.viewImage', function (event) {
                        event.preventDefault();
                        var imgUrl = $(this).attr('href');
                        self.viewImage(imgUrl);
                    }).on('click', '.downloadImage', function (event) {
                        event.preventDefault();
                        var imgUrl = $(this).attr('data-href');
                        var filename = $(this).attr('data-filename');
                        download(imgUrl, filename);
                    }).on('click', '#comeback', function (event) {
                        event.preventDefault();
                        updatePage();
                    }).on('click', '.toTaskHistoicFlowPage', function (event) {
                        event.preventDefault();
                        location.href = '${ctx}/workflow/toTaskHistoicFlow?procInstId=${procInstId}&flowMark=${flowFrom.flowMark}&taskId=${taskId}';
                        // location.href = '${ctx}/workflow/toTaskHistoicFlow?procInstId=${procInstId}&flowMark=${flowFrom.flowMark}&' + urlParams.slice(1, urlParams.length) + '&' + location.search.slice(1, location.search.length);
                    }).off('click', '.addRemarks').on('click', '.addRemarks', function (event) {
                        // 正常打开
                        console.log('1234: ',pageDataObject.taskDefKey);
                        var task_def_key = pageDataObject.taskDefKey || erpApp.getQueryString('taskDefKey');
                        top.$.jBox.open("iframe:" + '${ctx}/remarks/workflowRemarksInfo/add?task_def_key='+task_def_key+'&procInstId=${procInstId}'+'&serviceType='+pageDataObject.serviceType+'&visitType='+pageDataObject.visitType, "新增备注", 600, 500, {
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
                        
                        console.log(pageDataObject.userid,uid);
                        
//                         if (uid !== pageDataObject.userid) {
//                             $.jBox.tip("您不是该备注的创建人，不能删除！", 'info');
//                             return;
//                         }
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
            act.bindEvent();
          	//加载备注信息
            renderRemark('${procInstId}');    
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

            $('body').on('click', 'input[name="isRestart"]', function () {
                var v = $(this).val();
                if (v == 'N') { //点击了否，去掉表单中的disabled
                    $('#restart-mask').hide();
                    $('#restart-box').removeClass('mask-status');
                } else {
                    $('#restart-mask').show();
                    $('#restart-box').addClass('mask-status');
                }
            });
        });

        //文件下载
        function download(downloadpath, filename) {
            filename = encodeURIComponent(encodeURIComponent(filename));
            window.location.href = "${ctx}/workfile/file/downloadAmachment?downloadUrl=" + downloadpath + "&realFileName=" + filename;
        }

        function showInfoTip(msg, btn, cb) {
            var buttons = btn ? {'知道了': true} : {'确定': true};
            top.$.jBox.closeTip();
            $.jBox.closeTip();
            top.$.jBox.info(msg, '提示', {
                buttons: buttons, closed: function () {
                    typeof cb === 'function' && cb();
                }
            });
        }
    </script>
</head>
<body>
<div class="act-top positionrelative">
    <div class="act-top-left positionrabsolute">
        <a href="javascript:;" class="comeback" id="comeback">返回</a>
    </div>
    <ul class="nav nav-tabs">
        <li class="marginleft60 acttitle active">
            <!-- <a href="${ctx}/workflow/taskDetail?taskId=${taskId}">任务</a> -->
            <a href="javascript:;">任务</a>
        </li>
        <li class="acttitle">
            <a href="${ctx}/workflow/toTaskHistoicFlow?procInstId=${procInstId}&taskId=${taskId}&flowMark=${flowFrom.flowMark}&taskDefKey=<%=request.getParameter("taskDefKey") %>" class="toTaskHistoicFlowPage">进度</a>
            <%--<c:if test="${flowFrom.flowMark=='jyk_flow'}"><a href="${ctx}/workflow/toTaskHistoicFlow?taskId=${taskId}&flowMark=jyk_flow" class="toTaskHistoicFlowPage">进度</a></c:if>
            <c:if test="${flowFrom.flowMark=='sdi_flow'}"><a href="${ctx}/workflow/toTaskHistoicFlow?taskId=${taskId}&flowMark=sdi_flow" class="toTaskHistoicFlowPage">进度</a></c:if>
            <c:if test="${flowFrom.flowMark=='payInto_flow'}"><a href="${ctx}/workflow/toTaskHistoicFlow?taskId=${taskId}&flowMark=payInto_flow" class="toTaskHistoicFlowPage">进度</a></c:if>--%>
        </li>
    </ul>
</div>
<div class="act-process padding10">
    <div class="act-process-left floatleft">
        <c:choose>
            <c:when test="${flowFrom.orderSplitInfo.suspendFlag =='Y'}">
                <div class="act-rs-item taskDetail">
                    <h3 style="text-align:center"><a href="">与商户沟通启动服务</a></h3>
                    <div class="form-horizontal" style="width: 580px;margin: 30px auto 20px auto;">
                        <div id="restart-box">
                            <div class="control-group">
                                <label class="control-label" style="width: 250px;">暂停原因：<span
                                        class="require redword">*</span></label>
                                <div class="controls" style="margin-left: 253px;">
                                    <select id="selectReason" name="selectReason" style="width:300px;">
                                        <option value="">请选择</option>
                                        <c:forEach items="${suspendReasons }" var="reason">
                                            <option value="${reason.value}" <c:if
                                                test="${flowFrom.orderSplitInfo.suspendReason ==reason.value }">selected="selected"</c:if>">${reason.label }</option>
                                        </c:forEach>
                                    </select>
                                    <div style="margin-top:10px;">
                                        <textarea id="detailReason" name="detailReason" rows="3"
                                                  style="padding: 5px; width:300px; margin-top: 5px; box-sizing: border-box;"
                                                  placeholder="输入具体暂停原因">${flowFrom.orderSplitInfo.suspendReasonContent }</textarea>
                                    </div>
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label" style="width: 250px;">下次与商户沟通是否进行服务的时间：<span
                                        class="require redword">*</span></label>
                                <div class="controls" style="margin-left: 253px;">
                                    <input id="nextDatereason" name="nextDate" value="${flowFrom.nextContactTimeStr}"
                                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false,readOnly:true});"
                                           type="text" class="required input-medium Wdate" maxlength="20"
                                           readonly="readonly" style="width: 286px;">
                                </div>
                            </div>
                            <div id="restart-mask"></div>
                        </div>

                        <div class="control-group">
                            <label class="control-label" style="width: 250px;">是否重启服务：<span
                                    class="require redword">*</span></label>
                            <div class="controls" style="margin-left: 253px;">
                                <label class="checkbox inline"><input type="radio" id="isRestart2" name="isRestart"
                                                                      value="N" checked>否</label>
                                <label class="checkbox inline"><input type="radio" id="isRestart1" name="isRestart"
                                                                      value="Y">是</label>
                            </div>
                        </div>
                    </div>
                    <div class="act-process-left-footer">
                        <div class="footer-info floatleft">
                            <p></p>
                            <!-- <p>负责人：系统管理员</p> -->
                        </div>
                        <div class="footer-btn floatright">
                            <button type="button" class="btn btn-large"
                                    onclick="sureAccomplish('${flowFrom.orderSplitInfo.id }')">确定
                            </button>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach items="${flowFrom.subTaskStrList }" var="i">
                    <div class="act-rs-item task-detail-items">${i.subTaskDetail}</div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>

    <%@ include file="/WEB-INF/views/modules/workflow/flowInfo.jsp" %>

</div>
<script src="${ctxStatic}/common/erpshop.js?v=${staticVersion}" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/scripts/manifest.min.js?v=${staticVersion}"></script>
<script type="text/javascript" src="${ctxStatic}/scripts/vendor.min.js?v=${staticVersion}"></script>
<script type="text/javascript" src="${ctxStatic}/common/injectscripts.js?v=${staticVersion}"></script>
</body>
</html>
