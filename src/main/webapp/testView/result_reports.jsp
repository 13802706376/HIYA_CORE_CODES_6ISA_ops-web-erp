<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="author" content="http://www.yunnex.com/">
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-store">
	<title>推广数据明细-效果报表</title>
	<link href="${ctxStatic}/bootstrap/2.3.1/css_cerulean/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/jquery-select2/3.4/select2.min.css" rel="stylesheet" />
	<link href="${ctxStatic}/common/jeesite.css?v=${staticVersion}" rel="stylesheet" type="text/css" />
		
	<style>
		ul,li{
			list-style:none;
		}
		.act-top{
			padding:10px 0;
			margin:0 20px;
		}
		.act-top .right-top{
			
		}
		.main-block{
			margin:10px 20px;
			border:1px solid #ddd;
			padding:15px;
		}
		.main-block .main-block-title{
			font-size:16px;
			font-weight:600;
			position:relative;
			margin-bottom:10px;
		}
		.main-block .main-block-title-sub{
			font-size:14px;
			font-weight:600;
			margin-bottom:10px;
		}
		.list-ul{
			margin-bottom:10px;
		}
		.list-ul>li{
			margin-bottom:5px;
			margin-right:10px;
		}
		.main-block .main-block-title .unfold-switch{
			position:absolute;
			right:0;
			top:0;
			display:block;
			width: 20px;
			height:20px;					
			cursor: pointer;
			/*background-color:#ddd;*/
			border: 1px solid #000;
			border-radius: 50%;
		}
		.main-block .main-block-title .unfold-switch:before{
			position:absolute;
			right:4px;							
		}
		.main-block .main-block-title .unfold-switch.unfold-up:before{
			top:8px;
			content:"";
			width: 0;
			height: 0;		
			border-width: 6px;			
			border-style: solid;	
			border-color: #2e2f2f transparent transparent transparent;
		}
		.main-block .main-block-title .unfold-switch.unfold-down:before{
			top:0;
			content:"";
			width: 0;
			height: 0;		
			border-width: 6px;			
			border-style: solid;	
			border-color: transparent transparent #2e2f2f transparent ;
		}
		.base-info{
		}
		.base-info>ul>li{
			margin-right:15px;
			padding-top:15px;
		}
		.main-block-tab{
			border-color:transparent;
			padding:0;
		}
		#myTab{
			margin-bottom:0;
		}
		#tab-content{
			padding:15px 10px 15px 10px;
			border-left:1px solid #ddd;
			border-right:1px solid #ddd;
			border-bottom:1px solid #ddd;
		}
		.list-title{
			color: #d47058;
		}
		.input-control{
			margin-bottom:0 !important;
			height:30px !important;
			line-height: 30px !important;
		}
		.form-list{
			padding:10px 0;
		}
		.error-text{
			font-style: normal;
			color: red;
		}
		/*效果报告*/
		.controls-text{
			margin-bottom:0;
			padding-top:3px;
		}
		.control-group{
			border-bottom-color: transparent;
		}
		.report-box .control-label{
			width: 180px;
			text-align: right;
		}
		.report-box .report-content{
			padding: 10px 20px;
		}
	</style>
	<script type="text/javascript">
		/*var ctx = '${ctx}', ctxStatic='${ctxStatic}';
		var pageDataObject = {
			uploadfileurl: "${fileUrl}",
			userid: "${fns:getUser().id}",
			username: "${fns:getUser().name}",
			hasPermission: '<shiro:hasPermission name="store:erpStoreInfo:syncOem">true</shiro:hasPermission>'
		};*/		
	</script>
</head>
<body>
<!-- 	<div id="rootNode"></div> -->
	<div class="act-top positionrelative clearfix">				
		<div class="pull-left" style="line-height:30px;">
			<a href="javascript:window.history.back();" class="comeback btn" id="comeback">返回</a>
			<span style="font-size:16px;font-weight:700">效果报告</span>
		</div>
		
	</div>	
	
	<div class="main-block main-block-tab">
		<ul class="nav nav-tabs" id="myTab">
		  <li class="active"><a href="#firstReport"  data-toggle="tab">首日报告</a></li>
		  <li><a href="#finalReport"  data-toggle="tab">最终报告</a></li>

		</ul>
		 <!-- 这是tab内容 -->
		<div class="tab-content" id="tab-content">
			<!-- 首日报告 start-->
		  <div class="tab-pane active" id="firstReport">
				<form class="form-horizontal" style="padding:10px;">
			        <div class="control-group">
			            <label class="control-label">
			                	选择统计截止时间:
			            </label>
			            <div class="controls">
			                <input type="text" id="time" name="time" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
			                class="required" readonly value="2018-05-09">			                
			            </div>
			        </div>
			        <div class="control-group">
			            <label class="control-label">
			                	推广开始时间:
			            </label>
			            <div class="controls">
			                <p class="controls-text">2018-04-11</p>			                
			            </div>
			        </div>
			        <div class="control-group">
			            <label class="control-label">
			                	商户名称:
			            </label>
			            <div class="controls">
			                <p class="controls-text">深圳花满天花卉管理公司</p>		
			            </div>
			        </div>
			        <div class="control-group">
			            <label class="control-label">
			                	投放门店:
			            </label>
			            <div class="controls">
			                <p class="controls-text">深圳宝安灵芝店</p>		
			            </div>
			        </div>
			        <div class="control-group">
			            <label class="control-label">
			               	投放门店数:
			            </label>
			            <div class="controls">
			               <p class="controls-text">1</p>		
			            </div>
			        </div>
			        <div class="control-group">
			            <label class="control-label">
			                	城市:
			            </label>
			            <div class="controls">
			                <p class="controls-text">深圳市</p>		
			            </div>
			        </div>
			        <div class="control-group">
			            <label class="control-label">
			                	投放通道:
			            </label>
			            <div class="controls">
			                <p class="controls-text">朋友圈|陌陌|微博</p>		
			            </div>
			        </div>		
			        <div class="control-group">
			            <label class="control-label">
			                	总曝光量:
			            </label>
			            <div class="controls">
			                <p class="controls-text">164,300</p>		
			            </div>
			        </div>	
			        <div class="control-group">
			            <label class="control-label">
			                	详情查看量:
			            </label>
			            <div class="controls">
			                <p class="controls-text">164,300</p>	
			            </div>
			        </div>	 
			        <div class="control-group">
			            <label class="control-label">
			                	互动量:
			            </label>
			            <div class="controls">
			                <p class="controls-text">164,300</p>	
			            </div>
			        </div>   
			        <div class="control-group">
			            <label class="control-label">
			                	派券数:
			            </label>
			            <div class="controls">
			                <p class="controls-text">164,300</p>	
			            </div>
			        </div>
				</form>
		  </div>
		  <!-- 首日报告 end -->
		  <!-- 最终报告 start-->
		  <div class="tab-pane" id="finalReport">
			  <div class="row">
				  <div class="span6">
					  <form class="form-horizontal" style="padding:10px;">
						  <div class="control-group">
							  <label class="control-label">
								  商户名称:
							  </label>
							  <div class="controls">
								  <p class="controls-text">深圳花满天花卉管理公司</p>
							  </div>
						  </div>
						  <div class="control-group">
							  <label class="control-label">
								  投放通道:
							  </label>
							  <div class="controls">
								  <p class="controls-text">朋友圈|陌陌|微博</p>
							  </div>
						  </div>
						  <div class="control-group">
							  <label class="control-label">
								  投放周期:
							  </label>
							  <div class="controls">
								  <p class="controls-text">2018-03-12 ———— 2018-05-05</p>
							  </div>
						  </div>
						  <div class="control-group">
							  <label class="control-label">
								  总曝光量:
							  </label>
							  <div class="controls">
								  <p class="controls-text">2,322,122</p>
							  </div>
						  </div>
						  <div class="control-group">
							  <label class="control-label">
								  超出承诺曝光:
							  </label>
							  <div class="controls">
								  <p class="controls-text">121</p>
							  </div>
						  </div>
						  <div class="control-group">
							  <label class="control-label">
								  详情查看量:
							  </label>
							  <div class="controls">
								  <p class="controls-text">123</p>
							  </div>
						  </div>
						  <div class="control-group">
							  <label class="control-label">
								  派券数:
							  </label>
							  <div class="controls">
								  <p class="controls-text">2333</p>
							  </div>
						  </div>
					  </form>
				  </div>
				  <div class="span6">
					  <form class="form-horizontal" style="padding:10px;">

						  <div class="control-group">
							  <label class="control-label">
								  互动量:
							  </label>
							  <div class="controls">
								  <p class="controls-text">164,300</p>
							  </div>
						  </div>
						  <div class="control-group">
							  <label class="control-label">
								  曝光成本:
							  </label>
							  <div class="controls">
								  <p class="controls-text">164,300</p>
							  </div>
						  </div>
						  <div class="control-group">
							  <label class="control-label">
								  详情查看成本:
							  </label>
							  <div class="controls">
								  <p class="controls-text">164,300</p>
							  </div>
						  </div>
						  <div class="control-group">
							  <label class="control-label">
								  朋友圈点击率:
							  </label>
							  <div class="controls">
								  <p class="controls-text">40%</p>
							  </div>
						  </div>
						  <div class="control-group">
							  <label class="control-label">
								  微博点击率:
							  </label>
							  <div class="controls">
								  <p class="controls-text">20%</p>
							  </div>
						  </div>
						  <div class="control-group">
							  <label class="control-label">
								  微博领券率:
							  </label>
							  <div class="controls">
								  <p class="controls-text">10%</p>
							  </div>
						  </div>
					  </form>
				  </div>
			  </div>
			  <div class="report-box">
				  <div class="report-list">
					  <label class="control-label">
						  单日曝光情况:
					  </label>
					  <div class="report-content" id="onedayExposure">

					  </div>
				  </div>
				  <div class="report-list">
					  <label class="control-label">
						  单日详情查看:
					  </label>
					  <div class="report-content" id="onedayViewNum">

					  </div>
				  </div>
				  <div class="report-list">
					  <label class="control-label">
						  单日互动情况:
					  </label>
					  <div class="report-content" id="onedayInteraction">

					  </div>
				  </div>
				  <div class="report-list">
					  <label class="control-label">
						  单日派券情况:
					  </label>
					  <div class="report-content" id="onedaySendCoupon">
						  
					  </div>
				  </div>
			  </div>
		  </div>
		  <!-- 最终报告 end-->
		  
		</div>
	</div>

	<script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/bootstrap/2.3.1/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-select2/3.4/select2.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/highcharts/highcharts.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/erpshop.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="./tuiguang.js?v=${staticVersion}" type="text/javascript"></script>
	<script>
		$(function () {
            onedayExposure();
			onedayViewNum();
        });
		/*
		* 单日曝光情况
		* */
		function onedayExposure() {
            var exposure = Highcharts.chart('onedayExposure', {
                chart: {
                    zoomType: 'xy'
                },
                title: {
                    text: '单日详情查看情况'
                },
                plotOptions: {
                    column: {
                        dataLabels: {
                            enabled: true
                        }
                    }
                },
                /*subtitle: {
                    text: '数据来源: WorldClimate.com'
                },*/
                xAxis: [{
                    categories: ['2018-06-01', '2018-06-02', '2018-06-03', '2018-06-04', '2018-06-05', '2018-06-06',
                        '2018-06-07'],
                    crosshair: true
                }],
                yAxis: [{ // Primary yAxis
                    labels: {
                        format: '{value}',
                        style: {
                            color: Highcharts.getOptions().colors[1]
                        }
                    },
                    title: {
                        text: '次数',
                        style: {
                            color: Highcharts.getOptions().colors[1]
                        }
                    }
                }],
                tooltip: {
                    shared: true
                },
                legend: {
                    layout: 'vertical',
                    align: 'left',
                    x: 120,
                    verticalAlign: 'top',
                    y: 100,
                    floating: true,
                    backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
                },
                series: [ {
                    name: '次数',
                    type: 'column',
                    data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2],
                    tooltip: {
                        valueSuffix: '次'
                    }
                }]
            });
        }
        /*
        * 单日详情查看
        * */
		function onedayViewNum() {
            var oneViewNum = Highcharts.chart('onedayViewNum', {
                chart: {
                    zoomType: 'xy'
                },
                title: {
                    text: '单日详情查看情况'
                },
                plotOptions: {
                    column: {
                        dataLabels: {
                            enabled: true
                        }
                    },
                    spline: {
                        dataLabels: {
                            enabled: true,
                            formatter: function()
                            {
                                return this.y + '%'
                            }
                        },

                    }
                },
                /*subtitle: {
                    text: '数据来源: WorldClimate.com'
                },*/
                xAxis: [{
                    categories: ['2018-06-01', '2018-06-02', '2018-06-03', '2018-06-04', '2018-06-05', '2018-06-06',
                        '2018-06-07'],
                    crosshair: true
                }],
                yAxis: [{ // Primary yAxis
                    labels: {
                        format: '{value}',
                        style: {
                            color: Highcharts.getOptions().colors[1]
                        }
                    },
                    title: {
                        text: '次数',
                        style: {
                            color: Highcharts.getOptions().colors[1]
                        }
                    }
                }, { // Secondary yAxis
                    title: {
                        text: '查看率',
                        style: {
                            color: Highcharts.getOptions().colors[0]
                        }
                    },
                    labels: {
                        format: '{value} mm',
                        style: {
                            color: Highcharts.getOptions().colors[0]
                        }
                    },
                    opposite: true
                }],
                tooltip: {
                    shared: true
                },
                legend: {
                    layout: 'vertical',
                    align: 'left',
                    x: 120,
                    verticalAlign: 'top',
                    y: 100,
                    floating: true,
                    backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
                },
                series: [{
                    name: '查看率',
                    type: 'spline',
                    yAxis: 1,
                    data: [49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6],
                    tooltip: {
                        valueSuffix: '%'
                    },
                    color: '#01161f',
                }, {
                    name: '次数',
                    type: 'column',
                    data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2],
                    tooltip: {
                        valueSuffix: '次'
                    },
                    color: '#7CB5EC',
                }]
            });
        }
	</script>
</body>
</html>