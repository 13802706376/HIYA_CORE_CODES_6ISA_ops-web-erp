(function(window, document, $, undefined) {
	$(function () {
		var urlParm=erpShopApp.getQueryString('name');  //取url参数
        var splitOrderId=erpShopApp.getQueryString('splitOrderId'); //分单id
        var loading;
        getPromotionInfo(splitOrderId); //获取推广信息
        if(!urlParm){  //当页面没有点过tab切换时，也需要初始化
            getStatisticsInfo(splitOrderId,'summarizing'); //获取对应的tab下的信息
        }
		$('#myTab a').each(function(index,ele){	//页面初始化 显示哪个tab		
			var hrefVal=$(ele).attr('href').replace('#', '')				
			if($(ele).attr('href').replace('#', '')==urlParm){
				$(ele).tab('show');
                console.log('这是初始化tab触发')
                getStatisticsInfo(splitOrderId,hrefVal); //获取对应的tab下的信息
			}
		});

		$('#myTab a').on('shown', function (e) { //点击tab后改写url参数
		  //e.target // activated tab
		  //e.relatedTarget // previous tab
		  var hrefVal=$(e.target).attr('href').replace('#', '');
		  getStatisticsInfo(splitOrderId,hrefVal); //获取对应的tab下的信息
		  ChangeParam('name',hrefVal);
			//window.location.reload()
            console.log('这是点击tab触发')
		});
		
		//全局ajax请求错误处理
		$(document).ajaxError(function(event, jqxhr, settings, exception) {
			$.jBox.closeTip(loading);

			$.jBox.info(exception+'.错误码：'+jqxhr.status, 'error');
		});

        //展开收缩基本信息
        $('.unfold-switch').off('click').on('click',function(e){
        	var $info_up=$('.info-up');
        	var $info_down=$('.info-down');
        	if($(this).hasClass('unfold-up')){
        		$(this).removeClass('unfold-up').addClass('unfold-down');           		
        		$info_down.slideDown(300);
        		$info_up.hide();
        	}else{
        		$(this).removeClass('unfold-down').addClass('unfold-up');
        		$info_down.slideUp(100);
        		$info_up.show();
        	}
        });
		//朋友圈、微博、陌陌 确定推广结束
		$('.over-popularize').off('click').on('click',function(){
			var $this=$(this);
			var tongdao=$(this).attr('data-tongdao');
			var $Status=$(this).closest('.popularize-box').find('.popularize-status'),
				$time=$(this).closest('.popularize-box').find('.popularize-time');

			var html = '<div style="padding:10px;"><label class="control-label" style="display:inline-block; width:100px;">推广结束时间：</label><input type="text" id="e_splitOrderPromotion" name="e_splitOrderPromotion" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd\',isShowClear:false});"/></div>';
			var submit = function (v, h, f) {
			    if (f.e_splitOrderPromotion == '') {
			        $.jBox.tip('请输入推广结束时间。', 'error'); // 关闭设置 yourname 为焦点
			        return false;
			    }

                loading=$.jBox.tip('请稍等...', 'loading', {
					timeout : 0,
					persistent : true
				});
			    
			    $.get('http://localhost:3000/tuigaung', {
					id : tongdao,
					times:f.e_splitOrderPromotion
				}, function(data) {
					if (data.result) {
						$.jBox.closeTip()
						$.jBox.tip('修改成功');
						$Status.html(data.status);
						$time.html(data.time);
						$this.text('修改');
						/*setTimeout(function(){
							//window.location.reload();
						} , 2000)*/
						
					} else {
						$.jBox.closeTip();
						$.jBox.tip(data.message,'error');
					}
				});
			    return true;
			};

			$.jBox(html, { title: '确定推广结束', submit: submit,width:400,height:200 });
		});
		//卡券核销、朋友圈、微博、陌陌 表格中的删除事件 
		$('#tab-content').on('click','.delete_btn',function(e){
			var $tr=$(this).closest('tr');
			var id=$tr.attr('data-id');
			var dataType=$(this).attr('data-type');
			var url='';
            if(dataType=='weixin'){
                url=ctx+'/promotion/erpPromoteDataInfo/deleteFriendsData';
            }else if(dataType=='weibo'){
                url=ctx+'/promotion/erpPromoteDataInfo/deleteWeiboData';
            }else{
                url=ctx+'/promotion/erpPromoteDataInfo/deleteMomoData';
            }
			var submit = function (v, h, f) {
			    if (v == 'ok') {
                    loading=$.jBox.tip('正在处理，请稍等...', 'loading', {
						timeout : 0,
						persistent : true
					});
			    	 $.post(url, {'id':id}, function(data) {
						if (data.returnCode=='success') {
							$.jBox.tip('删除成功');
							$tr.remove();
						} else {
							$.jBox.closeTip();
							$.jBox.tip('删除失败','error');
						}
					},'json');
			    }
			    return true;
			};
			$.jBox.confirm('确定要删除此项吗？', '提示', submit);
		});
		//卡券核销 中的新增和编辑领取卡券
		$('#tab-content').on('click','.get-coupon',function(e){
			var $this=$(this);
			var model=$(this).attr('data-model'); //add为新增 edit为编辑
			var title=model==='add'?'新增卡券量':'编辑卡券量';
			var couponNameId=model==='add'?$(this).attr('data-coupon'):'';
			var couponId=model==='add'?'':$(this).closest('tr').attr('data-id');
			var html='';
			
			html+='<div style="padding:10px;">';
			html+='<div class="form-list">';
			html+='<label class="control-label text-right" style="display:inline-block; width:100px;">时间：</label><input type="text" id="couponTime" name="couponTime" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd\',isShowClear:false});"/>';
			html+='</div>';
			
			html+='<div class="form-list">';
			html+='<label class="control-label text-right" style="display:inline-block; width:100px;">领券量：</label><input type="text" id="couponNum" name="couponNum" />'
			html+='</div>';
			
			html+='</div>';
						
			var loaded=function(dom){
				if(model==='edit'){
					//回显已有的数据
					var time=$this.closest('tr').find('.coupon-time').text(),    
						num=$this.closest('tr').find('.coupon-num').text();
					$(dom).find('#couponTime').val(time);//填充时间
					$(dom).find('#couponNum').val(num);//填充数量
				}
			}
			
			var submit = function (v, h, f) {
				var regNum = /^\+?[1-9]\d*$/;
			    if (f.couponTime == '') {
			        $.jBox.tip('请输入卡券领取时间。', 'error'); 
			        return false;
			    }
			    if (f.couponNum == '') {
			        $.jBox.tip('请输入卡券领取数量。', 'error'); 
			        return false;
			    }
			    if(!regNum.test(f.couponNum)){
			    	$.jBox.tip('请输入正确的卡券领取数量。', 'error'); 
			        return false;
			    }

                loading=$.jBox.tip('请稍等...', 'loading', {
					timeout : 0,
					persistent : true
				});
			    var params={};
			    if(model==='add'){
			    	params={
			    		'couponNameId':couponNameId,
			    		'couponTime':f.couponTime,
			    		'couponNum':f.couponNum
			    	}
			    }else{
			    	params={
			    		'couponId':couponId,
			    		'couponTime':f.couponTime,
			    		'couponNum':f.couponNum
			    	}
			    }
			    
			    $.post('http://localhost:3000/coupon', params, function(data) {
					if (data.result) {
						$.jBox.closeTip();
						if(model==='add'){
							$.jBox.tip('新增成功');
							var addDom='';
							addDom+='<tr data-id="'+data.addId+'">';
							addDom+='<td class="coupon-time">'+data.couponTime+'</td>';
							addDom+='<td class="coupon-num">'+data.couponNum+'</td>';	
							addDom+='<td><button type="button" class="btn get-coupon" data-model="edit">编辑</button>';	
							addDom+=' <button type="button" class="btn delete_btn">删除</button>';	
							addDom+='</td>';		
							addDom+='</tr>';
							$this.closest('.table').find('tbody').append(addDom);
						}else{
							$.jBox.tip('修改成功');
							$this.closest('tr').find('.coupon-time').html(data.couponTime);
							$this.closest('tr').find('.coupon-num').html(data.couponNum);
						}
						
						
					} else {
						$.jBox.closeTip();
						$.jBox.tip(data.message,'error');
					}
				},'json');
			    return true;
			};
			
			$.jBox(html, { title: title,loaded:loaded, submit: submit,width:400,height:200 });
		});
		//顶部新增添加数据、朋友圈、微博、陌陌 表格中的编辑事件 
		$(document).on('click','.editData,#addData',function (e) {
		    var $this=$(this);
			var type=$(this).attr('data-type');
			var html=dataDom(type).join('');

			var dataId=$(this).closest('tr').attr('data-id');
            var title=dataId ? '编辑数据':'新增数据';
            var loaded=function(dom){
            	var select2=$(dom).find('#tongdao');
                select2.select2();  //初始化select2下拉插件

                //input 失去焦点时 删除错误信息提示文字             
                $(dom).find('.controls>input').focus(function(){
                    $(this).closest('.controls').find('.error-text').html('');
                });

				if(dataId){ //编辑数据 需要禁用通道的选择
                    select2.val(type).trigger("change"); //设置选中
                    select2.change();
                    select2.select2("enable", false); //禁用
                    $(dom).find('#time').attr("disabled", true);//禁用时间控件
					//回显数据
                    var $dataList=$this.closest('tr').find('span'); //表格中的值
                    var $jboxInput=$(dom).find('.controls>input');
                    $($dataList).each(function (i,d) {
                        $($jboxInput).each(function (j,dom) {
                            if($(d).attr('class')==$(dom).attr('id')){
                                $(dom).val($(d).text());
                            }
                        });
                    })
                    
				}else{ //新增数据
                    select2.on("select2-selecting", function(e) {
                        //新增数据时，可以根据选择通道来切换输入的切换
                        var domStr=[];
						var newStr=transfeDom(domStr,e.val).join('');
                        $(dom).find('#transfeBox').html(newStr); //切换显示对应通道的dom
                        $(dom).find('.controls>input').focus(function(){ //切换元素后重新绑定事件
                            $(this).closest('.controls').find('.error-text').html('');
                        });
                    })
				}

            };
            var submit = function (v, h, f) {
                var regNum = /^\+?[0-9]\d*$/;  //整数
                var twoFloatNumReg=/^[0-9]+([.]{1}[0-9]{1,2})?$/;  //最多保留两位整数
                var valArr=[];
                var errorCount=0;
                $(h).find('.controls>input').each(function (i,d) {
                    var val=$(d).val();
                    var whichInput=$(d).attr('id');
                    switch (whichInput) {
                        //朋友圈
                        case 'time': //推广时间
                            if(!val){
                                $(d).closest('.controls').find('.error-text').html('请输入推广时间');
                                errorCount++;
                            }
                            valArr.push({'time':val});
                            break;
                        case 'money': //花费
                            if(!val || !twoFloatNumReg.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入数字，保留两位小数');
                                errorCount++;
                            }
                            valArr.push({'money':val});
                            break;
                        case 'detailViewNum': //详情查看量
                            if(!val || !regNum.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入大于0的正整数');
                                errorCount++;
                            }
                            valArr.push({'detailViewNum':val});
                            break;
                        case 'detailViewCost': //详情查看成本
                            if(!val || !twoFloatNumReg.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入数字，保留两位小数');
                                errorCount++;
                            }
                            valArr.push({'detailViewCost':val});
                            break;
                        case 'detailViewRatio': //详情查看率
                            if(!val || !twoFloatNumReg.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入数字，保留两位小数');
                                errorCount++;
                            }
                            valArr.push({'detailViewRatio':val});
                            break;
                        case 'exposureNum': //曝光量
                            if(!val || !regNum.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入大于0的正整数');
                                errorCount++;
                            }
                            valArr.push({'exposureNum':val});
                            break;
                        case 'yuanViewNum': //原生推广页查看量
                            if(!val || !regNum.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入大于0的正整数');
                                errorCount++;
                            }
                            valArr.push({'yuanViewNum':val});
                            break;
                        case 'yuanViewCost': //原生推广页查看成本
                            if(!val || !twoFloatNumReg.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入数字，保留两位小数');
                                errorCount++;
                            }
                            valArr.push({'yuanViewCost':val});
                            break;
                        case 'yuanViewRatio': //原生推广页查看率
                            if(!val || !twoFloatNumReg.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入数字，保留两位小数');
                                errorCount++;
                            }
                            valArr.push({'yuanViewRatio':val});
                            break;
                        case 'shopViewNum': //门店查看量
                            if(!val || !regNum.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入大于0的正整数');
                                errorCount++;
                            }
                            valArr.push({'shopViewNum':val});
                            break;
                        case 'zanCommentNum': //点赞评论量
                            if(!val || !regNum.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入大于0的正整数');
                                errorCount++;
                            }
                            valArr.push({'zanCommentNum':val});
                            break;
                        case 'concernNum': //关注量
                            if(!val || !regNum.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入大于0的正整数');
                                errorCount++;
                            }
                            valArr.push({'concernNum':val});
                            break;
                        case 'yuanViewTransmitNum': //原生推广页转发量
                            if(!val || !regNum.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入大于0的正整数');
                                errorCount++;
                            }
                            valArr.push({'yuanViewTransmitNum':val});
                            break;
                        case 'sellNum': //销售线索量
                            if(!val || !regNum.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入大于0的正整数');
                                errorCount++;
                            }
                            valArr.push({'sellNum':val});
                            break;
                        //微博（以下为微博特有，因为有些字段是与朋友圈相同的）
                        case 'advPlan': //广告计划
                            if(!val || val.length>50){
                                $(d).closest('.controls').find('.error-text').html('请输入小于50字的广告计划');
                                errorCount++;
                            }
                            valArr.push({'advPlan':val});
                            break;    
                        case 'thousandExposureCost': //千次曝光成本
                        	if(!val || !twoFloatNumReg.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入数字，保留两位小数');
                                errorCount++;
                            }
                        	valArr.push({'thousandExposureCost':val});
                            break;
                        case 'transmit': //转发
                            if(!val || !regNum.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入大于0的正整数');
                                errorCount++;
                            }
                            valArr.push({'transmit':val});
                            break;
                        case 'zan': //点赞
                            if(!val || !regNum.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入大于0的正整数');
                                errorCount++;
                            }
                            valArr.push({'zan':val});
                            break;
                        case 'comment': //评论
                            if(!val || !regNum.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入大于0的正整数');
                                errorCount++;
                            }
                            valArr.push({'comment':val});
                            break;
                        case 'diversionNum': //导流数
                            if(!val || !regNum.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入大于0的正整数');
                                errorCount++;
                            }
                            valArr.push({'diversionNum':val});
                            break;
                        case 'diversionRatio': //导流率
                        	if(!val || !twoFloatNumReg.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入数字，保留两位小数');
                                errorCount++;
                            }
                        	valArr.push({'diversionRatio':val});
                            break;
                        case 'diversionCost': //单次导流成本
                        	if(!val || !twoFloatNumReg.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入数字，保留两位小数');
                                errorCount++;
                            }
                        	valArr.push({'diversionCost':val});
                            break;
                        case 'attentionNum': //加关注数
                            if(!val || !regNum.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入大于0的正整数');
                                errorCount++;
                            }
                            valArr.push({'attentionNum':val});
                            break;
                        case 'attentionRatio': //加关注率
                        	if(!val || !twoFloatNumReg.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入数字，保留两位小数');
                                errorCount++;
                            }
                        	valArr.push({'attentionRatio':val});
                            break;
                        case 'attentionCost': //加关注成本
                        	if(!val || !twoFloatNumReg.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入数字，保留两位小数');
                                errorCount++;
                            }
                        	valArr.push({'attentionCost':val});
                            break;
                        case 'cardClickNum': //小card图文点击
                            if(!val || !regNum.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入大于0的正整数');
                                errorCount++;
                            }
                            valArr.push({'cardClickNum':val});
                            break;
                        case 'interactionNum': //互动数
                            if(!val || !regNum.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入大于0的正整数');
                                errorCount++;
                            }
                            valArr.push({'interactionNum':val});
                            break;
                        case 'interactionRatio': //互动率
                        	if(!val || !twoFloatNumReg.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入数字，保留两位小数');
                                errorCount++;
                            }
                        	valArr.push({'interactionRatio':val});
                            break;
                        case 'interactionCost': //单次互动成本
                        	if(!val || !twoFloatNumReg.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入数字，保留两位小数');
                                errorCount++;
                            }
                        	valArr.push({'interactionCost':val});
                            break;
                        //陌陌特有字段 
                        case 'consume': //加关注成本
                        	if(!val || !twoFloatNumReg.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入数字，保留两位小数');
                                errorCount++;
                            }
                        	valArr.push({'consume':val});
                            break;
                        case 'showNum': //互动数
                            if(!val || !regNum.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入大于0的正整数');
                                errorCount++;
                            }
                            valArr.push({'showNum':val});
                            break;
                        case 'clickNum': //互动数
                            if(!val || !regNum.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入大于0的正整数');
                                errorCount++;
                            }
                            valArr.push({'clickNum':val});
                            break;
                        case 'clickRatio': //点击率
                        	if(!val || !twoFloatNumReg.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入数字，保留两位小数');
                                errorCount++;
                            }
                        	valArr.push({'clickRatio':val});
                            break;
                        case 'thousandClickCost': //千次点击成本
                        	if(!val || !twoFloatNumReg.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入数字，保留两位小数');
                                errorCount++;
                            }
                        	valArr.push({'thousandClickCost':val});
                            break;
                        case 'averageClickCost': //千次点击单价
                        	if(!val || !twoFloatNumReg.test(val)){
                                $(d).closest('.controls').find('.error-text').html('请输入数字，保留两位小数');
                                errorCount++;
                            }
                        	valArr.push({'averageClickCost':val});
                            break;
                            
                    }
                });

                if(errorCount>0) {
                	$.jBox.tip('您有未填写或填写错误的项','error');
                	return false;
                }

                loading=$.jBox.tip('请稍等...', 'loading', {
                    timeout : 0,
                    persistent : true
                });

                //var dataPost=JSON.stringify(valArr);

                var postUrl='';
                var params={};
                var tongdao=$(h).find('#tongdao').val();

                if(tongdao=='weixin'){
                    postUrl=ctx+'/promotion/erpPromoteDataInfo/saveFriendsData'
                    if(dataId){
                        params={
                            splitOrderId:splitOrderId, //分单id
                            id:dataId, //编辑的数据id
                            //dataTime: $(h).find('#time').val(), //推广数据产生时间
                            attentionNum: Number($(h).find('#concernNum').val()), //关注量
                            detailsQueryCost: Number($(h).find('#detailViewCost').val()), //详情查看成本
                            detailsQueryNum: Number($(h).find('#detailViewNum').val()), //详情查看数量
                            detailsQueryPercent: Number($(h).find('#detailViewRatio').val()), //详情查看率
                            expenditure: Number($(h).find('#money').val()), //花费（元）
                            exposureNum: Number($(h).find('#exposureNum').val()), //曝光量
                            promotePagePropagateNum:Number($(h).find('#yuanViewTransmitNum').val()), //原生推广页转发量
                            promotePageQueryCost:Number($(h).find('#yuanViewCost').val()), //原生推广页查看成本
                            promotePageQueryNum:Number($(h).find('#yuanViewNum').val()), //原生推广页查看量
                            promotePageQueryPercent:Number($(h).find('#yuanViewRatio').val()), //原生推广页查看率
                            sellClewNum:Number($(h).find('#sellNum').val()), //销售线索量
                            storeQueryNum:Number($(h).find('#shopViewNum').val()), //门店查看量
                            upvoteCommentNum:Number($(h).find('#zanCommentNum').val())  //点赞评论数量
                        }
                    }else{
                        params={
                            splitOrderId:splitOrderId, //分单id
                            dataTime: $(h).find('#time').val(), //推广数据产生时间
                            attentionNum: Number($(h).find('#concernNum').val()), //关注量
                            detailsQueryCost: Number($(h).find('#detailViewCost').val()), //详情查看成本
                            detailsQueryNum: Number($(h).find('#detailViewNum').val()), //详情查看数量
                            detailsQueryPercent: Number($(h).find('#detailViewRatio').val()), //详情查看率
                            expenditure: Number($(h).find('#money').val()), //花费（元）
                            exposureNum: Number($(h).find('#exposureNum').val()), //曝光量
                            promotePagePropagateNum:Number($(h).find('#yuanViewTransmitNum').val()), //原生推广页转发量
                            promotePageQueryCost:Number($(h).find('#yuanViewCost').val()), //原生推广页查看成本
                            promotePageQueryNum:Number($(h).find('#yuanViewNum').val()), //原生推广页查看量
                            promotePageQueryPercent:Number($(h).find('#yuanViewRatio').val()), //原生推广页查看率
                            sellClewNum:Number($(h).find('#sellNum').val()), //销售线索量
                            storeQueryNum:Number($(h).find('#shopViewNum').val()), //门店查看量
                            upvoteCommentNum:Number($(h).find('#zanCommentNum').val())  //点赞评论数量
                        }
                    }

                }else if(tongdao=='weibo'){
                    postUrl=ctx+'/promotion/erpPromoteDataInfo/saveWeiboData'
                    if(dataId){
                        params={
                            splitOrderId:splitOrderId, //分单id
                            id:dataId, //编辑的数据id
                            //dataTime: $(h).find('#time').val(), //推广时间
                            advertisingPlan: $(h).find('#advPlan').val(), //计划
                            exposureNum: Number($(h).find('#exposureNum').val()), //曝光量
                            thousandsExposureCost: Number($(h).find('#thousandExposureCost').val()), //千次曝光成本
                            expenditure: Number($(h).find('#money').val()), //花费
                            propagateNum: Number($(h).find('#transmit').val()), //转发
                            upvoteNum: Number($(h).find('#zan').val()), //点赞
                            commentNum: Number($(h).find('#comment').val()), //评论

                            flowNum: Number($(h).find('#diversionNum').val()), //导流数
                            flowPercent: Number($(h).find('#diversionRatio').val()), //导流率
                            singleFlowCost: Number($(h).find('#diversionCost').val()), //单次导流成本
                            addAttentionNum: Number($(h).find('#attentionNum').val()), //加关注数
                            addAttentionPercent: Number($(h).find('#attentionRatio').val()), //加关注率
                            addAttentionCost: Number($(h).find('#attentionCost').val()), //加关注成本
                            cardClickNum: Number($(h).find('#cardClickNum').val()), //小card点击
                            interactionNum: Number($(h).find('#interactionNum').val()), //互动数
                            interactionPercent: Number($(h).find('#interactionRatio').val()), //互动率
                            singleInteractionCost: Number($(h).find('#interactionCost').val()), //单次互动成本
                        }
                    }else{
                        params={
                            splitOrderId:splitOrderId, //分单id
                            dataTime: $(h).find('#time').val(), //推广时间
                            advertisingPlan: Number($(h).find('#advPlan').val()), //计划
                            exposureNum: Number($(h).find('#exposureNum').val()), //曝光量
                            thousandsExposureCost: Number($(h).find('#thousandExposureCost').val()), //千次曝光成本
                            expenditure: Number($(h).find('#money').val()), //花费
                            propagateNum: Number($(h).find('#transmit').val()), //转发
                            upvoteNum: Number($(h).find('#zan').val()), //点赞
                            commentNum: Number($(h).find('#comment').val()), //评论

                            flowNum: Number($(h).find('#diversionNum').val()), //导流数
                            flowPercent: Number($(h).find('#diversionRatio').val()), //导流率
                            singleFlowCost: Number($(h).find('#diversionCost').val()), //单次导流成本
                            addAttentionNum: Number($(h).find('#attentionNum').val()), //加关注数
                            addAttentionPercent: Number($(h).find('#attentionRatio').val()), //加关注率
                            addAttentionCost: Number($(h).find('#attentionCost').val()), //加关注成本
                            cardClickNum: Number($(h).find('#cardClickNum').val()), //小card点击
                            interactionNum: Number($(h).find('#interactionNum').val()), //互动数
                            interactionPercent: Number($(h).find('#interactionRatio').val()), //互动率
                            singleInteractionCost: Number($(h).find('#interactionCost').val()), //单次互动成本
                        }
                    }
                }else{
                    postUrl=ctx+'/promotion/erpPromoteDataInfo/saveMomoData'
                    if(dataId){
                        params={
                            splitOrderId:splitOrderId, //分单id
                            id:dataId, //编辑的数据id
                            dataTime: $(h).find('#time').val(), //推广时间
                            expenditure: Number($(h).find('#consume').val()), //消耗
                            showNum: Number($(h).find('#showNum').val()), //展示量
                            clickNum: Number($(h).find('#clickNum').val()), //点击量
                            clickPercent: Number($(h).find('#clickRatio').val()), //点击率
                            cpm: Number($(h).find('#thousandClickCost').val()), //千次点击成本（元）
                            avgClickUnivalent: Number($(h).find('#averageClickCost').val()), //平均点击单价
                        }
                    }else{
                        params={
                            splitOrderId:splitOrderId, //分单id
                            //dataTime: $(h).find('#time').val(), //推广时间
                            expenditure: Number($(h).find('#consume').val()), //消耗
                            showNum: Number($(h).find('#showNum').val()), //展示量
                            clickNum: Number($(h).find('#clickNum').val()), //点击量
                            clickPercent: Number($(h).find('#clickRatio').val()), //点击率
                            cpm: Number($(h).find('#thousandClickCost').val()), //千次点击成本（元）
                            avgClickUnivalent: Number($(h).find('#averageClickCost').val()), //平均点击单价
                        }
                    }
                }

                $.post(postUrl, params, function(data) {
                    if (data.returnCode=='success') {
                        $.jBox.closeTip();
                        setTimeout(function(){
                            window.location.reload();
                        },2000);

                    } else {
                        $.jBox.closeTip();
                        $.jBox.tip('添加失败','error');
                    }
                },'json');
                return true;
            };

            $.jBox(html, { title: title,loaded:loaded, submit: submit,width:800,height:600,persistent:true });
        });
		//上传数据
        $(document).on('click','#addUploadData',function (e) {
            var html=[];
            var dom=[];
            dom.push('<div id="upload-box" class="form-horizontal" style="padding:10px;">');
            dom.push('<div class="control-group">');
            dom.push('<label class="control-label">推广通道:</label>');
            dom.push('<div class="controls">');
            dom.push('<select class="required input-medium" id="tongdao">');
            dom.push('<option value="weixin">朋友圈</option>');
            dom.push('<option value="weibo">微博</option>');
            dom.push('<option value="momo">陌陌</option>');
            dom.push('</select>');
            dom.push('</div>');
            dom.push('</div>');
            dom.push('<div class="control-group">');
            dom.push('<label class="control-label">选择数据文件:</label>');
            dom.push('<div class="controls">');
            dom.push('<div id="thelist" class="uploader-list"></div>');
            dom.push('<div id="picker">选择文件</div>');
            dom.push('</div>');
            dom.push('</div>');
            dom.push('<div class="control-group" id="progress-box" style="display: none">');
            dom.push('<label class="control-label">上传数据:</label>');
            dom.push('<div class="controls">');
            dom.push('<div id="progress"></div>');
            dom.push('<button type="button" id="cancel" class="btn">取消上传</button>');
            dom.push('</div>');
            dom.push('</div>');
            dom.push('<div class="control-group">');
            dom.push('<label class="control-label"></label>');
            dom.push('<div class="controls">');
            dom.push('<button type="button" id="ctlBtn" class="btn">上传</button>');
            dom.push('</div>');
            dom.push('</div>');
            dom.push('</div>');
            var loaded=function(dom){
                var uploadUrl='http://127.0.0.1:3000/upload';
                webUpload(uploadUrl,dom);
            };
            $.jBox(dom.join(''), { title: '上传推广数据',loaded:loaded,width:600,height:400,persistent:true,buttons: false});
        });
    });
	function dataDom(type){
		var dom=[];
        dom.push('<form id="inputForm" class="form-horizontal" style="padding:10px;">');
        dom.push('<div class="control-group">');
        dom.push('<label class="control-label">推广通道:</label>');
        dom.push('<div class="controls">');
        dom.push('<select class="required input-medium" id="tongdao">');
        dom.push('<option value="weixin">朋友圈</option>');
        dom.push('<option value="weibo">微博</option>');
        dom.push('<option value="momo">陌陌</option>');
        dom.push('</select>');
        dom.push('<span class="help-inline"><font color="red">*</font> </span>');
        dom.push('</div>');
        dom.push('</div>');

        dom.push('<div id="transfeBox">');

        transfeDom(dom,type);

        dom.push('</div>');

        dom.push('</form>');
		return dom;
	}
	function transfeDom(dom,type) {
        switch (type) {
            case 'momo':
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">推广时间:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="time" name="time" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd\',isShowClear:false});" class="required" readonly/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">消耗:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="consume" name="consume" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">展示量:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="showNum" name="showNum" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">点击量:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="clickNum" name="clickNum" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">点击率（%）:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="clickRatio" name="clickRatio" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">千次点击成本:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="thousandClickCost" name="thousandClickCost" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">平均点击单价:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="averageClickCost" name="averageClickCost" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                break;
            case 'weibo':
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">推广时间:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="time" name="time" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd\',isShowClear:false});" class="required" readonly/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">广告计划:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="advPlan" name="advPlan" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">曝光量:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="exposureNum" name="exposureNum" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">千次曝光成本:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="thousandExposureCost" name="thousandExposureCost" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">花费:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="money" name="money" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">转发:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="transmit" name="transmit" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">点赞:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="zan" name="zan" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">评论:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="comment" name="comment" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">导流数:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="diversionNum" name="diversionNum" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">导流率（%）:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="diversionRatio" name="diversionRatio" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">单次导流成本:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="diversionCost" name="diversionCost" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">加关注数:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="attentionNum" name="attentionNum" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">加关注率（%）:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="attentionRatio" name="attentionRatio" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">加关注成本:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="attentionCost" name="attentionCost" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">小card图文点击:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="cardClickNum" name="cardClickNum" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">互动数:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="interactionNum" name="interactionNum" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">互动率（%）:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="interactionRatio" name="interactionRatio" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">单次互动成本:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="interactionCost" name="interactionCost" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                break;
            default:
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">推广时间:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="time" name="time" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd\',isShowClear:false});" class="required" readonly/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">花费:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="money" name="money" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">详情查看量:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="detailViewNum" name="detailViewNum" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">详情查看成本:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="detailViewCost" name="detailViewCost" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">详情查看率（%）:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="detailViewRatio" name="detailViewRatio" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">曝光量:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="exposureNum" name="exposureNum" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">原生推广页查看量:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="yuanViewNum" name="yuanViewNum" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">原生推广页查看率（%）:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="yuanViewRatio" name="yuanViewRatio" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">原生推广页查看成本:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="yuanViewCost" name="yuanViewCost" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">门店查看量:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="shopViewNum" name="shopViewNum" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">点赞评论量:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="zanCommentNum" name="zanCommentNum" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">关注量:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="concernNum" name="concernNum" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">原生推广页转发量:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="yuanViewTransmitNum" name="yuanViewTransmitNum" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                dom.push('<div class="control-group">');
                dom.push('<label class="control-label">销售线索量:</label>');
                dom.push('<div class="controls">');
                dom.push('<input type="text" id="sellNum" name="sellNum" class="required"/>');
                dom.push('<span class="help-inline"><font color="red">*</font> <i class="error-text"></i></span>');
                dom.push('</div>');
                dom.push('</div>');
                break;
        }
        return dom;
    }
    /*
    * 页面初始化调用接口获取 推广信息
    * */
    function getPromotionInfo(splitOrderId){
        var url=ctx+'/promotion/erpPromoteDataInfo/queryPromotionpromoteData'
        console.log('这是推广信息接口，');
        $.post(url, {'splitOrderId':splitOrderId}, function(data) {
            if (data) {
                console.log('这是推广信息接口');
            } else {
                $.jBox.closeTip();
                $.jBox.tip('出错了','error');
            }
        },'json');
    };
    /*
    * 获取汇总、卡券和核销 、朋友圈、微博、陌陌 信息
    * */
    function getStatisticsInfo(splitOrderId,type){
        var url='';
        switch (type) {
            case 'coupon':
                url=ctx+'/promotion/erpPromoteDataInfo/querycouponpromoteData';
                break;
            case 'weixin':
                url=ctx+'/promotion/erpPromoteDataInfo/queryFriendspromoteData';
                break;
            case 'weibo':
                url=ctx+'/promotion/erpPromoteDataInfo/queryWeiboPromoteData';
                break;
            case 'momo':
                url=ctx+'/promotion/erpPromoteDataInfo/queryMomoPromoteData';
                break;
            default:
                url=ctx+'/promotion/erpPromoteDataInfo/queryhuizongpromoteData';
                break;
        }
        $.post(url, {'splitOrderId':splitOrderId}, function(data) {
            if (data) {
                switch (type) {
                    case 'coupon':

                        break;
                    case 'weixin':
                        //状态
                        $('#w-promotionStateTxt').html(data.promotionStateTxt?data.promotionStateTxt:'/'); //状态
                        $('#w-promotionBeginDate').html(data.promotionBeginDate?data.promotionBeginDate:'/'); //推广开始时间
                        $('#w-promotionEndDate').html(data.promotionEndDate?data.promotionEndDate:'/'); //推广结束时间

                        $('#w-detailsQueryNumSum').html(data.detailsQueryNumSum?data.detailsQueryNumSum:'/'); //详情查看量
                        $('#w-detailsQuerySumPercent').html(data.detailsQuerySumPercent?data.detailsQuerySumPercent:'/');//详情查看率
                        $('#w-expenditureSum').html(data.expenditureSum?data.expenditureSum:'/'); //花费（总消耗）
                        $('#w-exposureNumSum').html(data.exposureNumSum?data.exposureNumSum:'/'); //曝光量
                        $('#w-interactionNumSum').html(data.interactionNumSum?data.interactionNumSum:'/'); //互动量
                        $('#w-singleExposureCostSum').html(data.singleExposureCostSum?data.singleExposureCostSum:'/'); //单次曝光成本
                        var trStr=[];
                        if(data.friendsDataList){
                            var friendsDataList=data.friendsDataList; //表格数据 arry
                            $.each(friendsDataList,function(index,value){
                                trStr.push('<tr data-id="'+value.id+'">');
                                if(value.dataTimeTxt){
                                    trStr.push('<td><span class="time">'+value.dataTimeTxt+'</span></td>'); //时间
                                }else{
                                    trStr.push('<td><span class="time"></span></td>'); //时间
                                }
                                if(typeof(value.expenditure)!='undefined'){
                                    trStr.push('<td><span class="money">'+value.expenditure+'</span></td>');  //花费
                                }else{
                                    trStr.push('<td><span class="money"></span></td>');  //花费
                                }
                                if(typeof(value.detailsQueryNum) !='undefined'){
                                    trStr.push('<td><span class="detailViewNum">'+value.detailsQueryNum+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="detailViewNum"></span></td>');
                                }
                                if(typeof(value.detailsQueryCost) !='undefined'){
                                    trStr.push('<td><span class="detailViewCost">'+value.detailsQueryCost+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="detailViewCost"></span></td>');
                                }
                                if(typeof(value.detailsQueryPercent) !='undefined'){
                                    trStr.push('<td><span class="detailViewRatio">'+value.detailsQueryPercent+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="detailViewRatio"></span></td>');
                                }
                                if(typeof(value.exposureNum) !='undefined'){
                                    trStr.push('<td><span class="exposureNum">'+value.exposureNum+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="exposureNum"></span></td>');
                                }
                                if(typeof(value.promotePageQueryNum) !='undefined'){
                                    trStr.push('<td><span class="yuanViewNum">'+value.promotePageQueryNum+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="yuanViewNum"></span></td>');
                                }
                                if(typeof(value.promotePageQueryCost) !='undefined'){
                                    trStr.push('<td><span class="yuanViewCost">'+value.promotePageQueryCost+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="yuanViewCost"></span></td>');
                                }
                                if(typeof(value.promotePageQueryPercent) !='undefined'){
                                    trStr.push('<td><span class="yuanViewRatio">'+value.promotePageQueryPercent+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="yuanViewRatio"></span></td>');
                                }
                                if(typeof(value.storeQueryNum) !='undefined'){
                                    trStr.push('<td><span class="shopViewNum">'+value.storeQueryNum+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="shopViewNum"></span></td>');
                                }
                                if(typeof(value.upvoteCommentNum) !='undefined'){
                                    trStr.push('<td><span class="zanCommentNum">'+value.upvoteCommentNum+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="zanCommentNum"></span></td>');
                                }
                                if(typeof(value.attentionNum) !='undefined'){
                                    trStr.push('<td><span class="concernNum">'+value.attentionNum+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="concernNum"></span></td>');
                                }
                                if(typeof(value.promotePagePropagateNum) !='undefined'){
                                    trStr.push('<td><span class="yuanViewTransmitNum">'+value.promotePagePropagateNum+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="yuanViewTransmitNum"></span></td>');
                                }
                                if(typeof(value.sellClewNum) !='undefined'){
                                    trStr.push('<td><span class="sellNum">'+value.sellClewNum+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="sellNum"></span></td>');
                                }

                                trStr.push('<td><div style="width:120px;"><button type="button" class="btn editData" data-type="weixin">编辑</button> <button type="button" class="btn delete_btn" data-type="weixin">删除</button></td>');
                                trStr.push('</tr>');
                            });
                        }else{
                            trStr.push('<tr>');
                            trStr.push('<td colspan="15"><p class="text-center">暂无数据</p></td>');
                            trStr.push('</tr>');
                        }
                        $('#weixin').find('tbody').html(trStr.join(''));
                        break;
                    case 'weibo':
                        //状态
                        $('#wb-promotionStateTxt').html(data.promotionStateTxt?data.promotionStateTxt:'/'); //状态
                        $('#wb-promotionBeginDate').html(data.promotionBeginDate?data.promotionBeginDate:'/'); //推广开始时间
                        $('#wb-promotionEndDate').html(data.promotionEndDate?data.promotionEndDate:'/'); //推广结束时间

                        $('#wb-exposureNumSum').html(data.exposureNumSum?data.exposureNumSum:'/'); //曝光量
                        $('#wb-expenditureSum').html(data.expenditureSum?data.expenditureSum:'/'); //总消耗
                        $('#wb-flowSumPercent').html(data.flowSumPercent?data.flowSumPercent:'/'); //导流率
                        $('#wb-singleExposureCostSum').html(data.singleExposureCostSum?data.singleExposureCostSum:'/'); //单次曝光成本
                        $('#wb-flowNumSum').html(data.flowNumSum?data.flowNumSum:'/'); //导流量
                        $('#wb-interactionNumSum').html(data.interactionNumSum?data.interactionNumSum:'/'); //互动量
                        var trStr=[];
                        if(data.weiboDataList){
                            var weiboDataList=data.weiboDataList; //表格数据 arry
                            $.each(weiboDataList,function(index,value){
                                trStr.push('<tr data-id="'+value.id+'">');
                                if(typeof(value.dataTimeTxt)!='undefined'){
                                    trStr.push('<td><span class="time">'+value.dataTimeTxt+'</span></td>'); //时间
                                }else{
                                    trStr.push('<td><span class="time"></span></td>'); //时间
                                }
                                if(typeof(value.advertisingPlan)!='undefined'){
                                    trStr.push('<td><span class="advPlan">'+value.advertisingPlan+'</span></td>'); //广告计划
                                }else{
                                    trStr.push('<td><span class="advPlan"></span></td>'); //广告计划
                                }
                                if(typeof(value.exposureNum)!='undefined'){
                                    trStr.push('<td><span class="exposureNum">'+value.exposureNum+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="exposureNum"></span></td>'); //曝光量
                                }
                                if(typeof(value.thousandsExposureCost)!='undefined'){
                                    trStr.push('<td><span class="thousandExposureCost">'+value.thousandsExposureCost+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="thousandExposureCost"></span></td>'); //千次曝光成本
                                }
                                if(typeof(value.expenditure)!='undefined'){
                                    trStr.push('<td><span class="money">'+value.expenditure+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="money"></span></td>'); //花费
                                }
                                if(typeof(value.propagateNum)!='undefined'){
                                    trStr.push('<td><span class="transmit">'+value.propagateNum+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="transmit"></span></td>'); //转发量
                                }
                                if(typeof(value.upvoteNum)!='undefined'){
                                    trStr.push('<td><span class="zan">'+value.upvoteNum+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="zan"></span></td>');//点赞
                                }
                                if(typeof(value.commentNum)!='undefined'){
                                    trStr.push('<td><span class="comment">'+value.commentNum+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="comment"></span></td>'); //评论
                                }
                                if(typeof(value.flowNum)!='undefined'){
                                    trStr.push('<td><span class="diversionNum">'+value.flowNum+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="diversionNum"></span></td>'); //导流数
                                }
                                if(typeof(value.flowPercent)!='undefined'){
                                    trStr.push('<td><span class="diversionRatio">'+value.flowPercent+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="diversionRatio"></span></td>');  //导流率
                                }
                                if(typeof(value.singleFlowCost)!='undefined'){
                                    trStr.push('<td><span class="diversionCost">'+value.singleFlowCost+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="diversionCost">10</span></td>');//单次导流成本
                                }
                                if(typeof(value.addAttentionNum)!='undefined'){
                                    trStr.push('<td><span class="attentionNum">'+value.addAttentionNum+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="attentionNum"></span></td>'); //加关注数
                                }
                                if(typeof(value.addAttentionPercent)!='undefined'){
                                    trStr.push('<td><span class="attentionRatio">'+value.addAttentionPercent+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="attentionRatio"></span></td>');  //加关注率
                                }
                                if(typeof(value.addAttentionCost)!='undefined'){
                                    trStr.push('<td><span class="attentionCost">'+value.addAttentionCost+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="attentionCost"></span></td>'); //加关注成本
                                }
                                if(typeof(value.cardClickNum)!='undefined'){
                                    trStr.push('<td><span class="cardClickNum">'+value.cardClickNum+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="cardClickNum"></span></td>'); //小card图文点击
                                }
                                if(typeof(value.interactionNum)!='undefined'){
                                    trStr.push('<td><span class="interactionNum">'+value.interactionNum+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="interactionNum"></span></td>'); //互动数
                                }
                                if(typeof(value.interactionPercent)!='undefined'){
                                    trStr.push('<td><span class="interactionRatio">'+value.interactionPercent+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="interactionRatio"></span></td>'); //互动率
                                }
                                if(typeof(value.singleInteractionCost)!='undefined'){
                                    trStr.push('<td><span class="interactionCost">'+value.singleInteractionCost+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="interactionCost"></span></td>'); //单次互动成本
                                }
                                trStr.push('<td><div style="width:120px;"><button type="button" class="btn editData" data-type="weibo">编辑</button> <button type="button" class="btn delete_btn" data-type="weibo">删除</button></td>');
                                trStr.push('</tr>');
                            });
                        }else{
                            trStr.push('<tr>');
                            trStr.push('<td colspan="15"><p class="text-center">暂无数据</p></td>');
                            trStr.push('</tr>');
                        }
                        $('#weibo').find('tbody').html(trStr.join(''));
                        break;
                    case 'momo':
                        //状态
                        $('#mm-promotionStateTxt').html(data.promotionStateTxt?data.promotionStateTxt:'/'); //状态
                        $('#mm-promotionBeginDate').html(data.promotionBeginDate?data.promotionBeginDate:'/'); //推广开始时间
                        $('#mm-promotionEndDate').html(data.promotionEndDate?data.promotionEndDate:'/'); //推广结束时间

                        $('#mm-showNumSum').html(data.showNumSum?data.showNumSum:'/'); //曝光量
                        $('#mm-expenditureSum').html(data.expenditureSum?data.expenditureSum:'/'); //总消耗
                        $('#mm-clickSumPercent').html(data.clickSumPercent?data.clickSumPercent:'/'); //点击率
                        $('#mm-singleShowCostSum').html(data.singleShowCostSum?data.singleShowCostSum:'/'); //单次曝光成本
                        $('#wb-clickNumSum').html(data.clickNumSum?data.clickNumSum:'/'); //点击量
                        var trStr=[];
                        if(data.momoDataList){
                            var momoDataList=data.momoDataList; //表格数据 arry
                            $.each(momoDataList,function(index,value){
                                trStr.push('<tr data-id="'+value.id+'">');
                                if(value.dataTimeTxt){
                                    trStr.push('<td><span class="time">'+value.dataTimeTxt+'</span></td>'); //时间
                                }else{
                                    trStr.push('<td><span class="time"></span></td>'); //时间
                                }
                                if(typeof(value.expenditure)!='undefined'){
                                    trStr.push('<td><span class="consume">'+value.expenditure+'</span></td>');  //花费
                                }else{
                                    trStr.push('<td><span class="consume"></span></td>');  //花费
                                }
                                if(typeof(value.showNum) !='undefined'){
                                    trStr.push('<td><span class="showNum">'+value.showNum+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="showNum"></span></td>'); //展示量
                                }
                                if(typeof(value.clickNum) !='undefined'){
                                    trStr.push('<td><span class="clickNum">'+value.clickNum+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="clickNum"></span></td>');//点击量
                                }
                                if(typeof(value.clickPercent) !='undefined'){
                                    trStr.push('<td><span class="clickRatio">'+value.clickPercent+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="clickRatio"></span></td>'); //点击率
                                }
                                if(typeof(value.cpm) !='undefined'){
                                    trStr.push('<td><span class="thousandClickCost">'+value.cpm+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="thousandClickCost"></span></td>'); //cpm
                                }
                                if(typeof(value.avgClickUnivalent) !='undefined'){
                                    trStr.push('<td><span class="averageClickCost">'+value.avgClickUnivalent+'</span></td>');
                                }else{
                                    trStr.push('<td><span class="averageClickCost"></span></td>');//平均点击单价
                                }

                                trStr.push('<td><div style="width:120px;"><button type="button" class="btn editData" data-type="momo">编辑</button> <button type="button" class="btn delete_btn" data-type="momo">删除</button></td>');
                                trStr.push('</tr>');
                            });
                        }else{
                            trStr.push('<tr>');
                            trStr.push('<td colspan="15"><p class="text-center">暂无数据</p></td>');
                            trStr.push('</tr>');
                        }
                        $('#momo').find('tbody').html(trStr.join(''));
                        break;
                    default:

                        break;
                }
            } else {
                $.jBox.closeTip();
                $.jBox.tip('出错了','error');
            }
        },'json');
    };
    
    /*
    * 
    * 文件上传配置
    * */
    function webUpload(uploadUrl,dom) {
        var $uploadBtn=$(dom).find('#ctlBtn');
        var uploader = WebUploader.create({
            // swf文件路径
            //swf: BASE_URL + '/js/Uploader.swf',
            // 文件接收服务端。
            server: uploadUrl,
            // 选择文件的按钮。可选。
            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
            pick: {
                id:$(dom).find('#picker'),
                multiple:false
            },
            accept:{
                title: '.xlsx .xls .csv文件',
                extensions: 'xlsx,xls,csv',
                //mimeTypes: 'image/*'
            },
            fileNumLimit: 1,
            fileSingleSizeLimit: 50 * 1024 * 1024,    // 50 M
            // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
            resize: false
        });
        //添加文件
        uploader.on( 'fileQueued', function( file ) {
            var $list=$(dom).find('#thelist');
            $list.html( '<div id="' + file.id + '" class="item">' +
                '<h4 class="info">' + file.name + '</h4>' +
                '<p class="state">等待上传...</p>' +
                '</div>' );
        });
        // 文件上传过程中创建进度条实时显示。
        uploader.on( 'uploadProgress', function( file, percentage ) {
            $(dom).find('.upload-small-btn.cancel').show();
            var $li = $( '#'+file.id ),
                $percent = $li.find('.progress .bar');

            // 避免重复创建
            if ( !$percent.length ) {
                $percent = $(
                    '<div class="clearfix">'+
                    '<div class="progress progress-striped progress-warning active pull-left" style="width: 80%;margin-top: 4px;">' +
                    '<div class="bar" style="width: 0%">' +
                    '</div>' +
                    '</div>'+
                    '<div class="cancel pull-left"><i class="fa upload-small-btn fa-times-circle cancel" aria-hidden="true"></i></div>'+
                    '</div>'
                ).appendTo( $li ).find('.progress-bar');
                //<i class="fa fa-check-circle" aria-hidden="true"></i>
            }
            $li.find('p.state').text('上传中');
            $percent.css( 'width', percentage * 100 + '%' );
            $(document).on('click','.upload-small-btn.cancel',function () {
                //alert('点击了取消')
                uploader.cancelFile( file );
                uploader.reset()
                //还需要 删除队列 恢复上传前的状态
            });

        });
        //文件上传成功
        uploader.on( 'uploadSuccess', function( file ) {
            $( '#'+file.id ).find('p.state').text('已上传');
            $( '#'+file.id ).find('i.upload-small-btn').removeClass('fa-times-circle cancel').addClass('fa-check-circle');
            $uploadBtn.text('完成').attr('data-status','done');
            //标记一些已经完成的后台返回的东西，点击“完成时”  一并提交给后台，然后关闭窗口
        });
        //上传出错
        uploader.on( 'uploadError', function( file ) {
            $( '#'+file.id ).find('p.state').text('上传出错');
            //$(dom).find('.upload-small-btn.cancel').hide();
            $(dom).find('i.upload-small-btn').removeClass('cancel');
            $uploadBtn.text('重新上传')
            uploader.reset()
        });
        uploader.on( 'error', function( handler ) {
            if(handler=='Q_TYPE_DENIED'){
                $.jBox.tip('请添加.xlsx .xls .csv 的文件','error');
            }else if (handler=='F_EXCEED_SIZE'){
                $.jBox.tip('请选择50M以内的文件');
            }
            console.log(handler)
        });

        $uploadBtn.on('click',function () {
            var status=$(this).attr('data-status');
            if(!status){ //进行上传任务
                uploader.upload();
            }else{
                //此时已经上传，点击完成 提交后台返回的最终数据给后台 然后tab选项自动打开至相应的通道 ，关闭窗口。
                alert('已经完成，将会关闭窗口')
            }

        });

    }

	/*
	* 无刷新浏览器修改url参数
	* */
	function ChangeParam(name,value){
		var url=window.location.href ;
		var newUrl="";
		var reg = new RegExp("(^|)"+ name +"=([^&]*)(|$)");
		var tmp = name + "=" + value;
		if(url.match(reg) != null){
		  newUrl= url.replace(eval(reg),tmp);
		}else{
			 if(url.match("[\?]")){
			 newUrl= url + "&" + tmp;
			 }else{
			 	newUrl= url + "?" + tmp;
			 }
		}
	   //location.href=newUrl;
	   history.pushState(null,null,newUrl)
	}

})(this, document, jQuery);