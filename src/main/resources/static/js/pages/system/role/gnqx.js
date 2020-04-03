layui.config({
    base: '/js/core/' //指定 winui 路径
}).extend({
    winui: 'winui/winui',
    treetable:'treetable/treetable',
    window: 'winui/js/winui.window'
}).define(['table', 'jquery', 'winui','form','window', 'treetable'], function (exports) {
    var msg = winui.window.msg,
		table = layui.table,
    	treetable = layui.treetable,
        form = layui.form,
        $ = layui.$, 
        layer = layui.layer
        tableId = 'tableId';

    var jsbm = $.getUrlParam("id");
    var jsmc = $.getUrlParam("jsmc");
    var thisForm = "editGNQX";
    var parentForm = $.getUrlParam("menuid");
    
    var tree;
    
    if(jsbm) {
        $("#title").html(jsmc);
        
        $.ajax({
    		url: '/api/system/menu/treelist',
    		type: 'get',
    		dataType: 'json',
    		headers: {token: localStorage["token"]},
    		success: function(data) {
    			treeData = data.data;
    			bindTable();
    		}
    	});
    }
    function bindTable() {
    	layer.load(1);
	    
	    treetable.render({
	        id: tableId,
	        treeColIndex :1,
	        treeSpid: '0',
	        treeIdName: 'CDBM',
	        treePidName: 'SJBM',
	        treeDefaultClose: true,	//是否默认折叠
            treeLinkage: false,		//父级展开时是否自动展开所有子级
	        elem: '#gnqxTable',
	        url: '/api/system/menu/gnqx',
	        where: {jsbm: jsbm},
	        headers: {token: localStorage["token"]},
	        height: 'full-80', //自适应高度
	        even: false,  //隔行变色
	        page: false,
	        cols: [[
	        	{ field: 'CDBM',type:'checkbox' },
	            { field: 'CDMC', title: '菜单名称' },
	            { field: 'URL', title: '页面URL' },
	            { field: 'PX', title: '排序', width: 80 }
	        ]],
	        done:function(){
	        	//$("table").css("height","100%")
	        	layer.closeAll("loading");
	        }
	    });
    }
    
    
    
    $('#btnOK').click(function () {
    	var data = table.checkStatus(tableId).data;
    	var cdbm = [];
    	$.each(data, function() {
    		cdbm.push(this.CDBM);
    	});
    	
    	if(!cdbm) {
    		msg('请选择功能菜单', {
                icon: 2,
                time: 2000
            });
    		return;
    	}
    	
    	$.ajax({
    		url: '/api/system/role/save/gnqx',
    		type: 'post',
    		data: {jsbm: jsbm, cdbm: cdbm.join(',')},
    		dataType: 'json',
    		headers: {token: localStorage["token"]},
    		success: function(data) {
    			if(data && data.code == "1") {
    				msg('设置成功', {
                        icon: 1,
                        time: 2000
                    });
        			
                    setTimeout(() => {
                        top.winui.window.close(thisForm);
                    }, 2000);
    			} else {
    				msg('设置失败，请重试', {
                        icon: 1,
                        time: 2000
                    });
    			}
    		}
    	});
    });
    form.on("submit", function(data) {
    	return false;
    });

    exports('gnqx', {});
});