/**
 * 用户管理
 */
var pageCurr;

$(function() {
	layui
			.use(
					'table',
					function() {
						var table = layui.table;
						form = layui.form;

						tableIns = table
								.render({
									elem : '#shareList',
									url : '/share/getShareContentList',
									contentType : 'application/json',
									method : 'post', // 默认：get请求
									cellMinWidth : 80,
									id : 'queryList',
									page : true,
									request : {
										pageName : 'pageNum', // 页码的参数名称，默认：pageNum
										limitName : 'pageSize' // 每页数据量的参数名，默认：pageSize
									},
									response : {
										statusName : 'code', // 数据状态的字段名称，默认：code
										statusCode : 200, // 成功的状态码，默认：0
										countName : 'rowCount', // 数据总数的字段名称，默认：rowCount
										dataName : 'list' // 数据列表的字段名称，默认：list
									},
									cols : [ [
											{
												type : 'checkbox'
											},
											{
												type : 'numbers'
											},
											{
												field : 'id',
												title : '标识',
												align : 'center'
											},
											{
												field : 'username',
												title : '用户名',
												align : 'center'
											},
											{
												field : 'content',
												title : '内容',
												align : 'center'
											},
											{
												field : 'if_first',
												title : '是否置顶',
												align : 'center'
											},
											{
												field : 'publictime',
												title : '发布时间',
												align : 'center',
												templet : '<div>{{ layui.util.toDateString(d.publictime, "yyyy-MM-dd HH:mm:ss") }}</div>'
											}, {
												title : '操作',
												align : 'center',
												toolbar : '#optBar'
											} ] ],
									done : function(res, curr, count) {
										pageCurr = curr;
									}
								});

						// 监听工具条
						table.on('tool(shareTable)', function(obj) {
							var data = obj.data;
							if (obj.event === 'edit') {
								// 编辑
								open(data, "编辑");
							} else if (obj.event === 'detail') {
								// 查看
								open(data, "查看");
							}
						});

						// 监听提交
						form.on('submit(Submit)', function(data) {
							// TODO 校验
							formSubmit(data);
							return false;
						});

					});

	// 搜索框
	layui.use([ 'form', 'laydate' ], function() {
		var form = layui.form, layer = layui.layer, laydate = layui.laydate;
		// 日期
		laydate.render({
			elem : '#startTime'
		});
		laydate.render({
			elem : '#endTime'
		});
		// TODO 数据校验
		// 监听搜索框
		form.on('submit(searchSubmit)', function(data) {
			// 重新加载table
			load(data);
			return false;
		});
	});
});

// 提交表单
function formSubmit(obj) {
	var form = layui.form;
	$.ajax({
		type : "POST",
		data : JSON.stringify(obj.field),
		url : "/share/setShareContent",
		contentType : "application/json",
		async : true,
		success : function(data) {
			if (data.code == 1) {
				layer.alert(data.msg, function() {
					layer.closeAll();
					load("");
					
				});
			} else {
				layer.alert(data.msg);
			}
		},
		error : function() {
			layer.alert("操作请求错误，请您稍后再试", function() {
				layer.closeAll();
				// 加载load方法
				location.reload();
			});
		}
	});
}

// 新增
function add() {
	open(null, "新增");
}
function open(data, title) {
	var form = layui.form;
	layui.use('layedit', function() {
		layedit = layui.layedit;
		var cliengguid = uuid(32, false);
		layedit.set({
	        uploadImage: {
	            url: '/filecontroller/uploadinedit?cliengguid=' + cliengguid + '&cliengtag=动态' //接口url
	            ,type: 'post' //默认post
	        }
	    });
		
		
		index = layedit.build('content');
		// 自定义验证规则
		form.verify({
			content : function() {
				layedit.sync(index);
			}
		});

	});

	if (data == null || data == "") {
		$("#id").val("");
	} else {
		$("#id").val(data.id);
		$("#content").val(data.content);
		if ("是" == data.if_first) {
			$("#if_first").attr("checked", "true");
		} else {
			$("#if_first").removeAttr("checked");
		}
		form.render('checkbox'); // 这个很重要
		
		
	}
	
	if ("查看" == title){
		$("#operate").hide();
		$("#if_first").attr("disabled", "true");
		$("#if_first").next().addClass("layui-disabled")
	}
	else if ("编辑" == title || data == null){
		$("#operate").show();
		$("#if_first").removeAttr("disabled");
		$("#if_first").next().removeClass("layui-disabled")
	}

	layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '1000px', '600px' ],
		content : $('#setHtml'),
		end : function() {
			//clean();
		}
	});
}

function delSelect() {
	layer.confirm('您确定要删除吗？', {
		btn : [ '确认', '返回' ]
	// 按钮
	}, function() {
		// 获取选中
		var selectData = layui.table.checkStatus('queryList').data;
		if (selectData.length == 0) {
			layer.alert("请选择要删除的记录！");
		} else {
			var ids = getIds(selectData);
			$.post("/share/delShareContent", {
				"ids" : ids
			}, function(data) {
				if (null != data) {
					layer.alert(data, function() {
						layer.closeAll();
						load("");
					});
				} else {
					layer.alert("对不起，操作失败！");
				}
			});
		}

	}, function() {
		layer.closeAll();
	});
}

function getIds(selectData) {
	var ids = '';
	for (var i = 0; i < selectData.length; i++) {
		ids = ids + "'" + selectData[i].id + "' ,";
	}
	return ids.substr(0, ids.length - 1);
}

function load(obj) {
	// 重新加载table
	if (obj && obj.field) {
		tableIns.reload({
			where : obj.field,
			page : {
				curr : pageCurr
			// 从当前页码开始
			}
		});
	} else {
		tableIns.reload({
			where : null,
			page : {
				curr : pageCurr
			// 从当前页码开始
			}
		});
	}

}

function uuid(len, radix) {
    var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
    var uuid = [], i;
    radix = radix || chars.length;
 
    if (len) {
      // Compact form
      for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random()*radix];
    } else {
      // rfc4122, version 4 form
      var r;
 
      // rfc4122 requires these characters
      uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
      uuid[14] = '4';
 
      // Fill in random data.  At i==19 set the high bits of clock sequence as
      // per rfc4122, sec. 4.1.5
      for (i = 0; i < 36; i++) {
        if (!uuid[i]) {
          r = 0 | Math.random()*16;
          uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
        }
      }
    }
 
    return uuid.join('');
}

