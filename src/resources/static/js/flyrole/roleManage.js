/**
 * 角色管理
 */
//'formSelects.render('permissions');
var pageCurr;
var form;

$(function() {

    layui.use('table', function(){
        var table = layui.table;
        form = layui.form;

        tableIns=table.render({
            elem: '#roleList',
            url:'/flyrole/getRoleList',
            contentType : 'application/json',
            method: 'post', //默认：get请求
            cellMinWidth: 80,
            id : 'queryList',
            page: true,
            request: {
                pageName: 'pageNum', //页码的参数名称，默认：pageNum
                limitName: 'pageSize' //每页数据量的参数名，默认：pageSize
            },
            response:{
                statusName: 'code', //数据状态的字段名称，默认：code
                statusCode: 200, //成功的状态码，默认：0
                countName: 'rowCount', //数据总数的字段名称，默认：count
                dataName: 'list' //数据列表的字段名称，默认：data
            },
            cols: [[
                {type:'checkbox'}
                ,{type:'numbers'}
                ,{field:'role_name', title:'角色名称',align:'center'}
                ,{field:'role_desc', title:'角色描述',align:'center'}
                ,{field:'permissionscontent', title:'权限',align:'center'}
                ,{field:'create_time', title:'创建时间',align:'center'}
                ,{field:'update_time', title:'更新时间',align:'center'}
                ,{field:'role_status', title:'是否有效',align:'center'}
                ,{fixed:'right',title:'操作',align:'center', toolbar:'#optBar'}
            ]],
            done: function(res, curr, count){
               pageCurr=curr;
            }
        });


        //监听工具条
        table.on('tool(roleTable)', function(obj){
            var data = obj.data;
            if (obj.event === 'edit') {
				// 编辑
            	edit(data, "编辑");
			} else if (obj.event === 'detail') {
				// 查看
				edit(data, "查看");
			}
        });

        //监听提交
        form.on('submit(roleSubmit)', function(data){
            formSubmit(data);
            return false;
        });

    });
    
  //搜索框
    layui.use(['form'], function(){
        var form = layui.form ,layer = layui.layer
        //TODO 数据校验
        //监听搜索框
        form.on('submit(searchSubmit)', function(data){
            //重新加载table
            load(data);
            return false;
        });
    });

});

//提交表单
function formSubmit(obj){
    $.ajax({
        type: "post",
        data: JSON.stringify(obj.field),
        url: "/flyrole/setRole",
        contentType : "application/json",
        success: function (data) {
            if (data.code == 1) {
                layer.alert(data.msg,function(){
                    layer.closeAll();
                    load("");
                  
                });
            } else {
                layer.alert(data.msg);
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试",function(){
                layer.closeAll();
                load("");
            });
        }
    });
}

//新增
function add() {
    edit(null,"新增");
}
//打开编辑框
function edit(data,title){
    var pid = null;
    if(data == null){
        $("#id").val("");
    }else{
        //回显数据
        $("#id").val(data.id);
        $("#roleName").val(data.role_name);
        $("#roleDesc").val(data.role_desc);
        pid = data.permissions;
    }

    formSelects.data('permissions', 'server', {
        url: '/flypermission/parentPermissionList',
        keyName: 'name',
        keyVal: 'id',
        success: function(id, url, searchVal, result){      //使用远程方式的success回调

            console.log(pid)
            if(pid != null){
                var assistAuditArry =pid.split(",");
                formSelects.value('permissions', assistAuditArry);
            }

           // console.log(result);    //返回的结果
        },
        error: function(id, url, searchVal, err){           //使用远程方式的error回调
                                                            //同上
            console.log(err);   //err对象
        },
    });


    if ("查看" == title){
		$("#operate").hide();
		$("#roleName").attr("readonly", "readonly");
		$("#roleDesc").attr("readonly", "readonly");
		
	}
	else if ("编辑" == title || data == null){
		$("#operate").show();
		$("#roleName").removeAttr("readonly");
		$("#roleDesc").removeAttr("readonly");
	}

    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['550px','400px'],
        content:$('#setRole'),
        end:function(){
            cleanRole();
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
			$.post("/flyrole/delRole", {
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

//重新加载table
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



function cleanRole() {
    $("#roleName").val("");
    $("#roleDesc").val("");
    $("#permissions").val("");
}

