/**
 * 菜单
 */
// 获取路径uri
var pathUri = window.location.href;
$(function() {
	layui.use('element', function() {
		var element = layui.element;
		// 左侧导航区域（可配合layui已有的垂直导航）
		$.get("/flypermission/getUserPerms", function(data) {
			if (data != null) {
				getMenus(data.perm);
				element.render('nav');
			} else {
				layer.alert("权限不足，请联系管理员", function() {
					// 退出
					window.location.href = "/logout";
				});
			}
		});
	});

	layui
			.use(
					'element',
					function() {
						var $ = layui.jquery, element = layui.element; // Tab的切换功能，切换事件监听等，需要依赖element模块
						// 触发事件
						var active = {
							tabAdd : function(url, id, name) {
								// 新增一个Tab项
								element
										.tabAdd(
												'demo',
												{
													title : name,
													content : '<iframe src="'
															+ url
															+ '" style="width: 100%; min-height: 600px;" scrolling="no" frameborder="no"></iframe>',
													id : id
												// 实际使用一般是规定好的id，这里以时间戳模拟下
												})
							},
							tabDelete : function(id) {
								// 删除指定Tab项
								element.tabDelete('demo', id); // 删除：“商品管理”

								othis.addClass('layui-btn-disabled');
							},
							tabChange : function(id) {
								// 切换到指定Tab项
								element.tabChange('demo', id); // 切换到：用户管理
							}
						};

						$('body').on('click', '.layui-nav-item a', function () {
							var dataid = $(this);
							if (dataid.attr("data-url") && dataid.attr("data-url") != ""){
								//判断已打开标签数目
								if ($(".layui-tab-title li[lay-id]").length < 1){
									active.tabAdd(dataid.attr("data-url"), dataid.attr("data-url"),dataid.text());
								}
								else{
									//否则判断该tab是否存在
									var isData = false;
									$.each($(".layui-tab-title li[lay-id]"), function(){
										if ($(this).attr("lay-id") == dataid.attr("data-url")){
											isData = true;
										}
										
									})
									
									if (isData == false){
										active.tabAdd(dataid.attr("data-url"), dataid.attr("data-url"),dataid.text());
									}
								}
								
								
								active.tabChange(dataid.attr("data-url"));
								
							}
							
							
						  	
						 });
						
					});

})


  
 


var getMenus = function(data) {
	// 回显选中
	var ul = $("<ul class='layui-nav layui-nav-tree' lay-filter='test'></ul>");
	for (var i = 0; i < data.length; i++) {
		var node = data[i];
		var li = $("<li class='layui-nav-item' flag='" + node.id + "'></li>");
		var a;
		//判断是否存在访问路径
		if (node.url && node.url != "") {
			//判断是否另外弹出窗口
			if (node.popup && node.popup == '1'){
				a = $("<a  href='" + node.url +  "' target='_blank'>" + node.name + "</a>");
			}
			else{
				a = $("<a  href='javascript:;' data-url='" + node.url
						+ "'>" + node.name + "</a>");
			}
			
			
			
		} else {
			a = $("<a class='' href='javascript:;'>" + node.name + "</a>");
		}
		li.append(a);
		// 获取子节点
		var childArry = node.childrens;
		if (childArry.length > 0) {
			a.append("<span class='layui-nav-more'></span>");
			var dl = $("<dl class='layui-nav-child'></dl>");
			for ( var y in childArry) {
				var dd;
				if (childArry[y].popup && childArry[y].popup == '1'){
					dd = $("<dd><a href='" + childArry[y].url+ "' target='_blank'>"
								+ childArry[y].name + "</a></dd>");
				}
				else{
					dd = $("<dd><a href='javascript:;' data-url='" + childArry[y].url
							+ "' >" + childArry[y].name + "</a></dd>");
				}
				 
				// 判断选中状态
				if (pathUri.indexOf(childArry[y].url) > 0) {
					li.addClass("layui-nav-itemed");
					dd.addClass("layui-this")
				}
				dl.append(dd);
			}
			li.append(dl);
		}
		ul.append(li);
	}
	$(".layui-side-scroll").append(ul);
}

// 根据菜单主键id获取下级菜单
// id：菜单主键id
// arry：菜单数组信息
function getParentArry(id, arry) {
	var newArry = new Array();
	for ( var x in arry) {
		if (arry[x].pId == id)
			newArry.push(arry[x]);
	}
	return newArry;
}

function updateUsePwd() {
	layer.open({
		type : 1,
		title : "修改密码",
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '450px' ],
		content : $('#pwdDiv')
	});
}