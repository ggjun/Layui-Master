/**
 * 权限管理
 */
var pageCurr;
var form;
$(function() {
    layui.use('table', function(){
        var table = layui.table;
        form = layui.form;

        tableIns=table.render({
            id:'id',
            elem: '#permissionList',
            url:'/flypermission/permissionList',
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
                ,{field:'pname', title:'父级菜单',align:'center'/*,width:"10%"*/}
                ,{field:'name', title:'菜单名称',align:'center'/*,width:"10%"*/}
                ,{field:'descpt', title:'描述',align:'center'/*,width:"15%"*/}
                ,{field:'url', title:'菜单url',align:'center'/*,width:"15%"*/}
                ,{field:'popup', title:'是否弹出',align:'center'/*,width:"15%"*/}
                ,{field:'create_time', title:'创建时间',align:'center'/*,width:"10%"*/}
                ,{field:'update_time', title:'更新时间',align:'center'/*,width:"10%"*/}
                ,{fixed:'right',title:'操作',align:'center', toolbar:'#optBar'/*,width:"25%"*/}
            ]],
            done: function(res, curr, count){
            	pageCurr=curr;

            }
        });


        //监听工具条
        table.on('tool(permissionTable)', function(obj){
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
        form.on('submit(permissionSubmit)', function(data){
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
        url: "/flypermission/setPermission",
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
    var parentId = null;
    if(data == null){
        $("#id").val("");
    }else{
        //回显数据
        $("#id").val(data.id);
        $("#cdname").val(data.name);
        $("#descpt").val(data.descpt);
        $("#url").val(data.url);
        if ("是" == data.popup) {
			$("#popup").attr("checked", "true");
		} else {
			$("#popup").removeAttr("checked");
		}
		form.render('checkbox'); // 这个很重要
        parentId = data.pid;
    }
   
    $.ajax({
        url:'/flypermission/parentPermissionList',
        dataType:'json',
        async: true,
        success:function(data){
            $("#pid").html("<option value='0'>根目录</option>");
            $.each(data,function(index,item){
                if(!parentId){
                    var option = new Option(item.name,item.id);
                }else {
                    var option = new Option(item.name,item.id);
                    // // 如果是之前的parentId则设置选中
                    if(item.id == parentId) {
                        option.setAttribute("selected",'true');
                    }
                }
                $('#pid').append(option);//往下拉菜单里添加元素
                form.render('select'); //这个很重要
            })
        }
    });
    
    if ("查看" == title){
		$("#operate").hide();
		$("#cdname").attr("readonly", "readonly");
		$("#descpt").attr("readonly", "readonly");
		$("#url").attr("readonly", "readonly");
		$("#popup").attr("disabled", "true");
		$("#popup").next().addClass("layui-disabled")
		
	}
	else if ("编辑" == title || data == null){
		$("#operate").show();
		$("#cdname").removeAttr("readonly");
		$("#descpt").removeAttr("readonly");
		$("#url").removeAttr("readonly");
		$("#popup").removeAttr("disabled");
		$("#popup").next().removeClass("layui-disabled")
	}

    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['550px'],
        content:$('#setPermission'),
        end:function(){
            cleanPermission();
        }
    });
}

function cleanPermission() {
    $("#name").val("");
    $("#descpt").val("");
    $("#url").val("");
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
			$.post("/flypermission/delPermission", {
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
