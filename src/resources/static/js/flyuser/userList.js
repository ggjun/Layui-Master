/**
 * 用户管理
 */
var pageCurr;
var form;
$(function() {
    layui.use('table', function(){
        var table = layui.table;
        form = layui.form;

        tableIns=table.render({
            elem: '#uesrList',
            url:'/flyuser/getUserList',
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
                ,{field:'sys_user_name', title:'用户名',align:'center'}
                ,{field:'role_name', title:'角色类型',align:'center'}
                ,{field:'user_phone', title:'手机号',align:'center'}
                ,{field:'reg_time', title: '注册时间',align:'center'}
                ,{field:'user_status', title: '是否有效',align:'center'}
                ,{title:'操作',align:'center', toolbar:'#optBar'}
            ]],
            done: function(res, curr, count){
               pageCurr=curr;
            }
        });

        //监听工具条
        table.on('tool(userTable)', function(obj){
            var data = obj.data;
            if (obj.event === 'edit') {
				// 编辑
            	openUser(data, "编辑");
			} else if (obj.event === 'detail') {
				// 查看
				openUser(data, "查看");
			}
        });

        //监听提交
        form.on('submit(userSubmit)', function(data){
            // TODO 校验
            formSubmit(data);
            return false;
        });
    });

    //搜索框
    layui.use(['form','laydate'], function(){
        var form = layui.form ,layer = layui.layer
            ,laydate = layui.laydate;
        //日期
        laydate.render({
            elem: '#startTime'
        });
        laydate.render({
            elem: '#endTime'
        });
        //TODO 数据校验
        //监听搜索框
        form.on('submit(searchSubmit)', function(data){
            //重新加载table
            load(data);
            return false;
        });
    });
    
    //头像上传
    layui.use('upload', function(){
    	  var upload = layui.upload;
    	  var fileIdMap = new Map(); 
    	  //执行实例
    	  var uploadInst = upload.render({
    	    elem: '#pic' //绑定元素
    	    ,url: '/filecontroller/upload' //上传接口
    	    ,accept: 'file' //普通文件 视频video  音频audio
    	    ,size: 2000 //限制文件大小，单位 KB
    	    ,before:function(obj){
    	    	//预读本地文件示例，不支持ie8
    	    	if ($("#picliengguid").val() && $("#picliengguid").val() != null){
    	    		this.data={'cliengguid':$("#picliengguid").val(),'cliengtag':'账号头像'};//关键代码
    	    	}
    	    	else{
    	    		/*var id = null;
    	    		if ($("#picsecond").val() && $("#picsecond").val() != null){
    	    			$("#picliengguid").val($("#picsecond").val());
    	    			id = $("#picsecond").val();
        	    	}
    	    		else{
    	    			id = uuid(32, false);
        	    		$("#picliengguid").val(id);
    	    		}*/
    	    		var id = uuid(32, false);
    	    		$("#picliengguid").val(id);
    	    		this.data={'cliengguid':id,'cliengtag':'账号头像'};//关键代码
    	    		
    	    		
    	    	}
    	    	var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
    	    	obj.preview(function(index, file, result){
    	    		$('#ImgPreview').append('<div class="image-container" style="float:left;" id="container'+index+'"><div class="delete-css"><div id="upload_img_'+index+'" class="layui-btn layui-btn-danger layui-btn-xs">删除</div></div>\n' +
    	                        '<img id="showImg'+index+'" style="width: 150px; margin:10px;cursor:pointer;" src="'+ result +'" alt="'+ file.name +'" ></div>')
    	                    $("#upload_img_" + index).bind('click', function () {
    	                    	$.post("/filecontroller/deleteByAttachguid", {attachguid:fileIdMap.get(index)}, function(data){
    	                    		if (data.code == 1){
    	                    			delete files[index];
    	    	                        $("#container"+index).remove();
    	                    		}
    	                    		
    	                    	})
    	                    	
    	                    	
    	                    });
    	        	
    	        });
    	    }
    	    ,multiple:false
    	    ,progress: function(n, elem){
    	        var percent = n + '%' //获取进度百分比
    	        element.progress('demo', percent); //可配合 layui 进度条元素使用
    	        
    	        //以下系 layui 2.5.6 新增
    	       // console.log(elem); //得到当前触发的元素 DOM 对象。可通过该元素定义的属性值匹配到对应的进度条。
    	      }
    	    ,done: function(res, index, upload){
    	    	delete this.files[index]
    	      //上传完毕回调
    	      if(res.code == 1){
    	    	  fileIdMap.set(index, res.data);
    	    	  return layer.msg('上传成功！');
    	      }
    	      else{
    	    	  return layer.msg('上传失败！');
    	      }
    	    }
    	    ,error: function(){
    	      //请求异常回调
    	       /* var demoText = $('#demoText');
    	        demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
    	        demoText.find('.demo-reload').on('click', function(){
    	        uploadInst.upload();
    	        });	*/
    	    }
    	  });
    	});
    
});


function cleanUser(){
    $("#username").val("");
    $("#mobile").val("");
    $("#password").val("");
    $('#roleId').html("");
    $('#ImgPreview').empty();
    if ($("#picliengguid").val() && $("#picliengguid").val() != null){
    	 $.post("/filecontroller/deleteByCliengguid", {cliengguid:$("#picliengguid").val()}, function(data){
    			if (data.code == 1){
    				if (data.data == 1){
    					//location.reload();
    				}
    				
    			}
    			
    		})
    }
    $("#picliengguid").val(null);
	$("#picsecond").val(null);
    
}

function updateCliengguid(picliengguid, picsecond){
	 $.post("/filecontroller/updateCliengguid", {picliengguid:picliengguid,picsecond:picsecond}, function(data){
			if (data.code == 1){
				console.log(22);
			}
			
		})
}


//提交表单
function formSubmit(obj){
	if ($("#picliengguid").val() && $("#picliengguid").val() != null){
		if ($("#picsecond").val() && $("#picsecond").val() != $("#picliengguid").val()){
			//编辑时上传了新附件，将新附件的cliengguid更新为同一组
			obj.field.piccliengguid = $("#picsecond").val();
			//将新上传的图片cliengguid更正过来（之前防止上传新图片后直接取消对话框会删除所有的，所以之前未保持一致）
			updateCliengguid($("#picliengguid").val(), $("#picsecond").val());
			
		}
		else{
			obj.field.piccliengguid = $("#picliengguid").val();
		}
		
		$("#picliengguid").val(null);
		$("#picsecond").val(null);
	}
	
    $.ajax({
    	type: "post",
    	data: JSON.stringify(obj.field),
        url: "/flyuser/setUser",
        contentType : "application/json",
        success: function (data) {
            if (data.code == 1) {
                layer.alert(data.msg,function(){
                    layer.closeAll();
                    //load("");
                    location.reload();
                });
            } else {
                layer.alert(data.msg);
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试",function(){
                layer.closeAll();
                //加载load方法
                load("");
            });
        }
    });
}


//获取服务器路径
function basePath() {
    //获取当前网址，如： http://localhost:8080/ems/Pages/Basic/Person.jsp
    var curWwwPath = window.document.location.href;
    //获取主机地址之后的目录，如： /ems/Pages/Basic/Person.jsp
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8080
    var localhostPath = curWwwPath.substring(0, pos);
    //获取带"/"的项目名，如：/ems
    //var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    //获取项目的basePath   http://localhost:8080/ems/
    //var basePath = localhostPath + projectName + "/";
    return localhostPath;
};


//开通用户
function addUser(){
    openUser(null,"开通用户");
}
function openUser(data,title){
    var roleId = null;
    if(data==null || data==""){
        $("#id").val("");
    }else{
        $("#id").val(data.id);
        $("#username").val(data.sys_user_name);
        $("#mobile").val(data.user_phone);
        roleId = data.role_id;
        //获取attachguid
        if (data.piccliengguid && data.piccliengguid != null){
        	var clid = data.piccliengguid;
        	 var fileIdMap = new Map(); 
        	 $.post("/filecontroller/getAttachguidsByCliengguid", {cliengguid:data.piccliengguid}, function(data){
         		if (data.code == 1){
         			$.each(data.data.list, function (index,e) {
         			 fileIdMap.set(index, e.attachguid);
         			 result = basePath()+"/filecontroller/readAttach?attachguid="+e.attachguid;
         			 $("#picsecond").val(clid);//用于编辑时新增图片保证cliengguid唯一
         			 
         			//渲染图片
         			if ("编辑" == title){
         				 $('#ImgPreview').append('<div class="image-container" style="float:left;" id="container'+index+'"><div class="delete-css"><div id="upload_img_'+index+'" class="layui-btn layui-btn-danger layui-btn-xs">删除</div></div>\n' +
       	                        '<img id="showImg'+index+'" style="width: 150px; margin:10px;cursor:pointer;" src="'+ result +'" alt="'+ e.filename +'" ></div>')
         			
       	                     $("#upload_img_" + index).bind('click', function () {
       	                    	$.post("/filecontroller/deleteByAttachguid", {attachguid:fileIdMap.get(index)}, function(data){
       	                    		if (data.code == 1){
       	                    			//delete files[index];
       	    	                        $("#container"+index).remove();
       	                    		}
       	                    		
       	                    	})
       	                    	
       	                    });
         				$("#pic").show(); 
         				$("#pictext").show();
         			}
         			else if ("查看" == title){
         				 $('#ImgPreview').append('<div class="image-container" style="float:left;" id="container'+index+'">\n' +
       	                        '<img id="showImg'+index+'" style="width: 150px; margin:10px;cursor:pointer;" src="'+ result +'" alt="'+ e.filename +'" ></div>')
         			
       	                     $("#pic").hide();
         				 	 $("#pictext").hide();
         			}
         			   
         		  });
         		}
         		
         	})
        }
      
    }
   
    $.ajax({
        url:'/flyrole/getRoles',
        dataType:'json',
        async: true,
        success:function(data){
            $.each(data,function(index,item){
                if(!roleId){
                    var option = new Option(item.role_name,item.id);
                }else {
                    var option = new Option(item.role_name,item.id);
                    // // 如果是之前的parentId则设置选中
                    if(item.id == roleId) {
                        option.setAttribute("selected",'true');
                    }
                }
                $('#roleId').append(option);//往下拉菜单里添加元素
                form.render('select'); //这个很重要
            })
        }
    });

    if ("查看" == title){
		$("#operate").hide();
		$("#username").attr("readonly", "readonly");
		$("#mobile").attr("readonly", "readonly");
		$("#password").attr("readonly", "readonly");
		
	}
	else if ("编辑" == title || data == null){
		$("#operate").show();
		$("#username").removeAttr("readonly");
		$("#mobile").removeAttr("readonly");
		$("#password").removeAttr("readonly");
	}
    
    
    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['550px'],
        content:$('#setUser'),
        end:function(){
            cleanUser();
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
			$.post("/flyuser/delUser", {
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


