(function(window, document, $, undefined) {
	$(function () {
		var urlParm=erpShopApp.getQueryString('name');  //取url参数		
		$('#myTab a').each(function(index,ele){	//页面初始化 显示哪个tab		
			var hrefVal=$(ele).attr('href').replace('#', '')				
			if($(ele).attr('href').replace('#', '')==urlParm){
				$(ele).tab('show');
			}
		});

		$('#myTab a').on('shown', function (e) { //点击tab后改写url参数
		  //e.target // activated tab
		  //e.relatedTarget // previous tab
		  var hrefVal=$(e.target).attr('href').replace('#', '')		  
		  ChangeParam('name',hrefVal);
			//window.location.reload() 
		});
		
		//全局ajax请求错误处理
		$(document).ajaxError(function(event, jqxhr, settings, exception) {
			$.jBox.closeTip();
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
		    
			    $.jBox.tip('请稍等...', 'loading', {
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
			
			var submit = function (v, h, f) {
			    if (v == 'ok') {
			    	 $.jBox.tip('正在处理，请稍等...', 'loading', {
						timeout : 0,
						persistent : true
					});
			    	 $.get('http://localhost:3000/delete', {'id':id}, function(data) {
						if (data.result) {
							$.jBox.tip('删除成功');
							$tr.remove();
						} else {
							$.jBox.closeTip();
							$.jBox.tip(data.message,'error');
						}
					});
			    }
			    return true; //close
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
		    
			    $.jBox.tip('请稍等...', 'loading', {
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
			    
			    $.get('http://localhost:3000/coupon', params, function(data) {
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
                var regNum = /^\+?[1-9]\d*$/;  //正整数
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
                
                $.jBox.tip('请稍等...', 'loading', {
                    timeout : 0,
                    persistent : true
                });
                             
                var params={};
                var tongdao=$(h).find('#tongdao').val();
                if(dataId){ //编辑
                	params={
                			'dataId':dataId,
                            'tongdao':tongdao,
                            inputList:valArr
                            
                        }
                }else{ //新增
                	params={
                            'tongdao':tongdao,
                            inputList:valArr
                            
                        }
                }
                
                console.log(JSON.stringify(params))
                
                $.get('http://localhost:3000/adddata', JSON.stringify(params), function(data) {
                    if (data.result) {
                        $.jBox.closeTip();
                        
                        setTimeout(function(){
                        	window.location.reload();
                        },2000);


                    } else {
                        $.jBox.closeTip();
                        $.jBox.tip(data.message,'error');
                    }
                },'json');
                return true;
            };

            $.jBox(html, { title: title,loaded:loaded, submit: submit,width:800,height:600 });
        })
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
	//无刷新浏览器修改url参数
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