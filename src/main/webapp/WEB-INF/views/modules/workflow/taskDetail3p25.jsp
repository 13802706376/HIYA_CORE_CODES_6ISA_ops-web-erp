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
	        taskDefKey: '${taskDefKey}',
	        serviceType: '${serviceType}',
	        visitType: '${visitType}',
	        assignee: '${flowInfo.assignee}',
	        flowStartDate: '${flowInfo.startDate}',
	        flowEndDate: '${flowInfo.endDate}',
	        procInsId:'${procInsId}'
		};
		
		 localStorage.setItem('taskDefKey', '${taskDefKey}');
		
		function updatePage() {
			// console.log(location.href);
			// return;
			var fromHref = top.globalUtil.sessionStorageGet('beiyiTaskFlowToWhere');
			var urlParams = function() {
			    var taskListFormDataBeiyi = top.globalUtil.sessionStorageGet('taskListFormDataBeiyi');
			    var taskListFormData = top.globalUtil.sessionStorageGet('taskListFormData');
			    var str = '';
			    var o = location.pathname.indexOf('3p25') !== -1 ? taskListFormDataBeiyi : taskListFormData;
			    if (o) {
			        for (var i in o) {
			            str += i + '=' + o[i] + '&'
			        }
			        str = '?' + str
			    }
			    return str.slice(0, -1);
			}();
			var detailType = getQueryString('detailType');
	        switch (detailType) {
	            case 'tasklist':
	                location.href = ctx+'/workflow/tasklist' + urlParams;
	                break;
	            case 'teamtasklist':
	                location.href = ctx+'/workflow/teamtasklist' + urlParams;
	                break;
	            case 'followTaskList':
	                location.href = ctx+'/workflow/followTaskList' + urlParams;
	                break;
	            case 'productionTeamTaskList':
	                location.href = ctx+'/workflow/productionTeamTaskList' + urlParams;
	                break;
	            case 'pendingProductionTaskList':
	                location.href = ctx+'/workflow/pendingProductionTaskList' + urlParams;
	                break;
	            case 'operatingServices':
	            	location.href = fromHref + urlParams;
	                break;
	            default:
	                location.href = ctx+'/workflow/tasklist' + updatePage;
	                break;
	        }
		}

		$(function() {
			$('body').on('click', '#comeback', function() {
				updatePage();
			})
		})
	</script>
</head>
<body>
	<div class="act-top positionrelative">
	    <div class="act-top-left positionrabsolute">
	        <a href="javascript:;" class="comeback" id="comeback">返回</a>
	    </div>
	    <ul class="nav nav-tabs">
	        <li class="marginleft60 acttitle active">
	            <a href="javascript:;">任务</a>
	        </li>
	        <li class="acttitle">
	            <a href="${ctx}/workflow/toTaskHistoicFlow?procInstId=${procInsId}&taskId=${taskId}&flowMark=flowMark3p25"
	               class="toTaskHistoicFlowPage">进度</a>
	        </li>
	    </ul>
	</div>
	<div class="beiyiTaskDetail act-process padding10">
 		<div class="act-process-left floatleft">
 			<div class="act-rs-item task-detail-items">
 				<div id="rootNode"></div>
 			</div>
 		</div>
	</div>
	<!-- taskId==> ${taskId } <br/>
	taskDefKey ==> ${taskDefKey } <br/>
	procInsId ==>${procInsId } <br/>
	actType ==>${actType } <br/> -->
    <%@ include file="/WEB-INF/views/modules/workflow/flowInfo3p25.jsp" %>
</div>
<script src="${ctxStatic}/common/erpshop.js?v=${staticVersion}" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/scripts/manifest.min.js?v=${staticVersion}"></script>
<script type="text/javascript" src="${ctxStatic}/scripts/vendor.min.js?v=${staticVersion}"></script>
<script type="text/javascript" src="${ctxStatic}/common/injectscripts.js?v=${staticVersion}"></script>
</body>
</html>