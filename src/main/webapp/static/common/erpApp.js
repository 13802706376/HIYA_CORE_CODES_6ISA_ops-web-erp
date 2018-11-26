(function(window, document, $, undefined) {
	'use strict';
	var erpApp = {
		//获取URL地址参数
		getQueryString: function(name, url) {
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
			if (!url || url == ""){
				url = window.location.search;
			}else{	
				url = url.substring(url.indexOf("?"));
			}
			var r = url.substr(1).match(reg);
			if (r != null) return unescape(r[2]);
			return null;
		},
		tip: function(content, type, o) {
			type = type || 'info';
			o = o || {};
			top.$.jBox.closeTip();
			top.$.jBox.tip(content, type, o);
		},
		closedTip: function() {
			$.jBox.closeTip();
			top.$.jBox.closeTip();
		},
		createStore: function(arg, cb) {
			var self = this;
			var html = '<div class="create-store-div">'+
					'<div class="form-horizontal">'+
						'<div class="control-group">'+
							'<label class="control-label">门店简称：</label>'+
							'<div class="controls">'+
								'<input type="text" class="store-name" id="newCreateStoreName" name="newCreateStoreName" />'+
							'</div>'+
						'</div>'+
					'</div></div>';
			var submit = function(v, h, f) {
				var newCreateStoreName = f.newCreateStoreName;
				if ($.trim(newCreateStoreName) === '') {
					self.tip("请填写门店简称！", 'error', { focusId: "newCreateStoreName" });
					return false;
				}
				typeof cb === 'function' && cb(newCreateStoreName);
			};
			top.$.jBox(html, { width:400, title: "创建门店", buttons: { '开始录入门店资料': 'ok' }, submit: submit });
		},
		addRemarks: function(c, cb) {
			var self = this;
			var cc = c === '-' ? '' : c;
			var html = '<div class="create-store-div"><textarea class="remk" id="remk" name="remk" style="width: 370px; height:60px; margin-top:8px; margin-left:10px;">'+cc+'</textarea></div>';
			var submit = function(v, h, f) {
				if (v === 'cancel') return true;
				var remk = f.remk;
				if ($.trim(remk) === '') {
					self.tip("请填写备注内容！", 'error', { focusId: "remk" });
					return false;
				}
				typeof cb === 'function' && cb(remk);
			};
			top.$.jBox(html, { width:400, title: "备注", buttons: { '保存': 'ok', '取消': 'cancel' }, submit: submit });
		},
		addContact: function(arg, cb) {
			arg = arg || {name: '', phoneNo: '', position: ''}
			var self = this;
			var html = '<div class="create-store-div create-contact-div">'+
					'<div class="form-horizontal">'+
						'<div class="control-group">'+
							'<label class="control-label">姓名：</label>'+
							'<div class="controls">'+
								'<input type="text" value="'+arg.name+'" class="stores-input" id="contactName" name="contactName" />'+
							'</div>'+
						'</div>'+
						'<div class="control-group">'+
							'<label class="control-label">联系人手机号：</label>'+
							'<div class="controls">'+
								'<input type="text" value="'+arg.phoneNo+'" class="stores-input" id="contactPhone" name="contactPhone" />'+
							'</div>'+
						'</div>'+
						'<div class="control-group">'+
							'<label class="control-label">职位：</label>'+
							'<div class="controls">'+
								'<input type="text" value="'+arg.position+'" class="stores-input" id="contactJob" name="contactJob" />'+
							'</div>'+
						'</div>'+
					'</div></div>';
			var submit = function(v, h, f) {
				if (v === 'cancel') return true;
				var name = $.trim(f.contactName);
				var phoneNo = $.trim(f.contactPhone);
				var position = $.trim(f.contactJob);
				if (!(/^[\u4e00-\u9fa5|a-zA-Z|\s]+$/).test(name)) {
					self.tip("请联系人姓名，不能包含特殊字符和数字！", 'error', { focusId: "contactName" });
					return false;
				}
				if (!(/^[13|14|15|16|17|18|19]+\d{9}$/).test(phoneNo)) {
					self.tip("请联系人11位数字的手机号！", 'error', { focusId: "contactPhone" });
					return false;
				}
				if (!(/^[\u4e00-\u9fa5|a-zA-Z|\s]+$/).test(position)) {
					self.tip("请联系人职位，不能包含特殊字符和数字！", 'error', { focusId: "contactJob" });
					return false;
				}
				self.tip("正在处理，请稍等...", 'loading', {
					timeout : 0,
					persistent : true
				});
				var o = {name: name, phoneNo: phoneNo, position: position}
				typeof cb === 'function' && cb(o);
			};
			top.$.jBox(html, { width:400, title: "添加联系人", buttons: { '保存': 'ok', '取消': 'cancel' }, submit: submit });
		},
		dialog: function(content, width, height, title, cb, buttons, text, registerName, registerNo) {
			var self = this;
			var html = content || '暂无数据';
			var buttons = buttons || { '保存': 'ok', '取消': 'cancel' };
			var title = title || '提示';
			var submit = function(v, h, f) {
				if (v === 'cancel') return true;
				var storeId = $(h).find('.on').attr('data-id');
				var storeids = $(h).find('.deletestore');
				var ids = '';
				if (text) {
					storeids.each(function() {
						ids += $(this).attr('data-id') + ',';
					});
				}
				setTimeout(function() {
					typeof cb === 'function' && cb(storeId, ids);
				}, 20);
			};
			var bindEvent = function(me) {
				var body = $('body', window.top.document);
				body.on('click', '.storelistTraget', function(event) {
					event.preventDefault();
					body.find('.storelistTraget').removeClass('on');
					$(this).addClass('on');
				}).on('click', '.storelistLi .deletestore', function(event) {
					event.preventDefault();
					var index = parseInt($(this).attr('data-index'));
					if (body.find('#jbox-content .storelistLi').size() === 1) {
						me.tip("只要一个门店了，不能删除！", "info");
					} else {
						$(this).parent('.storelistLi').remove();
					}
				});
			};
			if (html instanceof Array && html.length > 0) {
				if (text) {
					//微信支付进件提交审核
					var wrap = [];
					for (var i = 0, l = html.length; i < l; i++) {
						wrap.push('<li class="storelistLi"><a href="javascript:;" class="storelistLiTraget" title="'+html[i].shortName+'">'+html[i].shortName+'</a><a class="icon-trash deletestore" title="删除" data-id="'+html[i].id+'" data-index="'+i+'"></a></li>');
					}
					html = '<ul class="storeLi">'+wrap.join("")+'</ul>';
					html = wrap.length > 1 ?
					'<div class="storeinfoWrap"><div class="storeinfo"><p>当前支付进件信息：</p>'+
						'<p>1、营业执照：'+registerName+'</p><p>2、结算账号：'+registerNo+'</p><p>以下门店使用这个支付进件信息，将一并提交。</p>'+
						'</div></div>' + html
					:
					'<div class="storeinfoWrap"><div class="storeinfo"><p>当前支付进件信息：</p><p>1、营业执照：'+registerName+'</p>'+
						'<p>2、结算账号：'+registerNo+'</p></div></div>';
					if (wrap.length > 9) {
						height = 520;
					}
				} else {
					//设置掌贝进件主体
					var wrap = [];
					for (var i = 0, l = html.length; i < l; i++) {
						if (html[i].isMain !== 1) {
							wrap.push('<li class="storelist"><a href="javascript:;" class="storelistTraget" data-id="'+html[i].id+'" data-shopInfoId="'+html[i].shopInfoId+'" title="'+html[i].shortName+'">'+html[i].shortName+'</a></li>');
						}
					}
					html = '<ul class="store">'+wrap.join("")+'</ul>';
				}
			}
			html = '<div class="erpshopDialogWrapper">'+html+'</div>';
			top.$.jBox(html, { width: width || 400, height: height || 80, title: title, buttons: buttons, submit: submit });
			setTimeout(function() {bindEvent(self)}, 30);
		},
		checkConfirm: function(content, title, storeName, appId, cb, type) {
			var self = this;
			var html = content || '暂无数据';
			var buttons = { '确定': 'ok', '取消': 'cancel' };
			var title = title || '提示';
			var ctcx = type === 1 ? '公众号原始ID：' : type === 2 ? '微博登录账号：' : '商户陌陌号';
			var ctcs = type === 1 ? '朋友圈' : type === 2 ? '微博' : '陌陌';
			var submit = function(v, h, f) {
				if (v === 'cancel') return true;
				var storeids = $(h).find('.deletestore');
				var ids = '';
				storeids.each(function() {
					ids += $(this).attr('data-id') + ',';
				});
				setTimeout(function() {
					typeof cb === 'function' && cb(ids);
				}, 20);
			};
			var bindEvent = function(me) {
				var body = $('body', window.top.document);
				body.on('click', '.storelistLi .deletestore', function(event) {
					event.preventDefault();
					var index = parseInt($(this).attr('data-index'));
					if (body.find('#jbox-content .storelistLi').size() === 1) {
						me.tip("只要一个门店了，不能删除！", "info");
					} else {
						$(this).parent('.storelistLi').remove();
					}
				});
			};
			if (html instanceof Array && html.length > 0) {
				var wrap = [];
				for (var i = 0, l = html.length; i < l; i++) {
					wrap.push('<li class="storelistLi"><a href="javascript:;" class="storelistLiTraget" title="'+html[i].storeName+'">'+html[i].storeName+'</a><a class="icon-trash deletestore" title="删除" data-id="'+html[i].storeId+'" data-index="'+i+'"></a></li>');
				}
				html = '<ul class="storeLi">'+wrap.join("")+'</ul>';
				html = wrap.length > 1 ?
				'<div class="storeinfoWrap"><div class="storeinfo"><p>将使用以下信息开通门店“'+storeName+'”的'+ctcs+'广告主：</p>'+
					'<p>'+ctcx+appId+'</p><p>被投放的门店中，以下门店和“'+storeName+'”的'+ctcs+'广告主开通资料相同，请确认是否一并提交</p>'+
					'</div></div>' + html
				:
				'<div class="storeinfoWrap"><div class="storeinfo"><p>将使用以下信息开通门店“'+storeName+'”的'+ctcs+'广告主：</p>'+
					'<p>'+ctcx+appId+'</p>提交</div></div>';
				if (wrap.length > 9) {
					height = 520;
				}
			}
			html = '<div class="erpshopDialogWrapper">'+html+'</div>';
			top.$.jBox(html, {width: 400, height: 'auto', title: title, buttons: buttons, submit: submit});
			setTimeout(function() {bindEvent(self)}, 30);
		},
		confirm: function(title, cb, type) {
			var type = type || '提示';
			var self = this;
			var submit = function(v, h, f) {
				if (v == 'ok') {
					self.tip("正在处理，请稍等...", 'loading', {
						timeout : 0,
						persistent : true
					});
					typeof cb === 'function' && cb();
					return true;
				}
			};
			top.$.jBox.confirm(title, type, submit);
		},
		select2: function(selector) {
			if (selector) {
				$(selector).select2();
			} else {
				$('select').select2();
			}
		},
		emptySelect2: function(selector) {
			$(selector).select2('val', '');
		},
		ajax: function(url, paramsObj, successFun, errFun) {
			if (!url || !paramsObj) return false;
			$.ajax({
				url: url,
				type: 'POST',
				contentType: "application/json;charset=utf-8",
				dataType: 'json',
				data: paramsObj,
				success: function(res) {
					typeof successFun === 'function' && successFun(res)
				},
				error: function(err){
					typeof errFun === 'function' && errFun(err)
				}
			})
		},
		extend: function(deep, target, object) {
			return $.extend(deep, target, object);
		},
		showTaskPocess: function(url) {
			var w = $(window).width();
			top.$.jBox.open("iframe:" + url, "任务进度", w, 600, {
				buttons:{"关闭":true},
				loaded: function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
				}
			});
		},
		jobNumberAction: function(url, id) {
			var self = this
			var title = id ? '编辑工号' : '新增工号';
			top.$.jBox.open("iframe:" + url, title, 900, 600, {
				buttons:{"保存": "ok", "取消":true},
				loaded: function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
				},
				submit: function(v, h, f){	
					if(v === 'ok'){
						var doc = $(h).find("#jbox-iframe").contents();
						var data = doc.find('#formDataInput').val();
						var url =  id ? ctx+'/sys/jobNumber/update' : ctx+'/sys/jobNumber/save';
						var d = JSON.parse(data);
						if ($.trim(d.iconImg) === '') {
							self.tip('请选择头像！');
							return false;
						}
						if ($.trim(d.jobNumber) === '') {
							self.tip('请填写工号！');
							return false;
						}
						if ($.trim(d.roleName) === '') {
							self.tip('请选择角色！');
							return false;
						}
						if ($.trim(d.userName) === '') {
							self.tip('请选择人员！');
							return false;
						}
						if (!(/(\d{3}-\d{8}|\d{4}-\d{7,8})|([13|14|15|16|17|18|19]+\d{9})/.test($.trim(d.telephone)))) {
							self.tip('电话格式不正确！');
							return false;
						}
						if ($.trim(d.score) === '') {
							self.tip('请选择评分！');
							return false;
						}
						if (id) {
							d.id = id
						}
						$.post(url, d, function(data, textStatus, xhr) {
							if (data.code === '0') {
								self.tip(data.message || '保存成功！', 'info')
								location.reload();
							}
						});
						return true
					}
				}
			});
		},
		jobNumberDelete: function(id) {
			var self = this;
			this.confirm('确定要删除该工号吗？',function() {
				$.post(ctx + '/sys/jobNumber/delete', {id: id}, function(data, textStatus, xhr) {
					self.closedTip();
					if (data.code === '0') {
						self.tip(data.message || '删除成功！', 'info')
						location.reload();
					}
				})
			});
		},
		selectUser: function(m, url, pageSize, cb) { //m是否能多选, url获取人员列表的接口
			///tag/treeselect?url=%2Fteam%2FerpTeam%2FtreeData&module=&checked=true&extId=&isAll=
			top.$.jBox.open("iframe:"+ ctx + '/sys/jobNumber/selectUser?m='+m+'&url='+url+'&pageSize='+pageSize, "选择人员", 400, 500, {
				buttons:{"确定": "ok", "取消":true},
				submit: function(v, h, f){	
					if(v === 'ok') {
						var doc = $(h).find("#jbox-iframe").contents();
						var userList = doc.find('#userList');
						var users = userList.find('.on');//JSON.parse(data);
						var arr = [];
						for (var i = 0, l = users.size(); i < l; i++) {
							arr.push({user: $(users[i]).attr('data-u')})
						}
						typeof cb === 'function' && cb(arr);
						return true;
					}
				},
				loaded: function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
				}
			});
		}
	};
	window.erpApp = erpApp;

	$(function() {
		$('body').on('click', '.uploadWrappers img', function(event) {
			event.preventDefault();
			var src = $(this).attr('src');
			var html = '<div class="view-image-wrap"><img src="'+src+'" alt="图片预览" style="max-width:100%;" /></div>';
			top.$.jBox(html, {title:"图片预览", width: 900, buttons: {}});
		}).on('click', '.jobNumberAction', function() {
			var jobnumberid = $(this).attr('data-jobnumberid');
			erpApp.jobNumberAction(ctx + '/sys/jobNumber/toadd?id='+jobnumberid, jobnumberid);
		}).on('click', '.jobNumberDelete', function() {
			erpApp.jobNumberDelete($(this).attr('data-jobnumberid'));
		});
	});
})(this, document, jQuery);