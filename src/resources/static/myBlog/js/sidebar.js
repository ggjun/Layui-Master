$(function(){
	
	layui.use('laydate', function(){
		  var laydate = layui.laydate;
		 laydate.render({
			    elem: '#testdate',
			    position: 'static',
		        calendar: true,
		        showBottom: false,
		        theme: 'grid'
			   
			  });
		
	})
		
	
})