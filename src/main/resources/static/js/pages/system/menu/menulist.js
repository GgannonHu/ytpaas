layui.config({
    base: '/js/core/' //指定 winui 路径
    , version: '1.0.0-beta'
}).extend({
    winui: 'winui/winui',
    treetable:'treetable/treetable',
    window: 'winui/js/winui.window'
}).define(['table', 'jquery', 'winui', 'window', 'layer','treetable', 'form', 'tree'], function (exports) {
    winui.renderColor();

    var msg = winui.window.msg, $ = layui.$;
    var form = layui.form;
    var thisForm = $.getUrlParam("id");
    
    var treeData = [];

    function initTree() {
    	$.ajax({
    		url: '/api/system/menu/treelist',
    		type: 'get',
    		dataType: 'json',
    		headers: {token: localStorage["token"]},
    		success: function(data) {
    			treeData = data.data;
    			bindTree();
    		}
    	});
    }
    function bindTree() {
    	layui.tree({
    		elem: '#menu' //传入元素选择器
    		,onlyIconControl: true  //是否仅允许节点左侧图标控制展开收缩
    		,click: function(obj){
    			treeClick(obj);
    		}
      	 	,nodes: treeData
      	});
    }
    
    function treeClick(obj) {
    	$("#cdbm").val(obj.cdbm);
    	$("#add_sjcd").val(obj.name);
    	
    	if(obj.cdbm != "0") {
    		$("#up_cdmc").val(obj.name);
        	$("#up_url").val(obj.pageurl);
        	$("#up_txt_icon").val(obj.bz);
        	$("#up_i_icon").attr("class", "fa " + obj.bz);
        	$("#up_px").val(obj.px);
        	$("#del_msg").html("确定要删除【" + obj.name + "】吗？");

    		$("#upSubmit").removeAttr("disabled").removeClass("layui-disabled");
    		$("#delSubmit").removeAttr("disabled").removeClass("layui-disabled");
    	} else {
    		$("#upSubmit").attr("disabled", true).addClass("layui-disabled");
    		$("#delSubmit").attr("disabled", true).addClass("layui-disabled");
    	}
    	form.render();
    }
    
    
    
    initTree();
    form.on('submit(addIconBtn)', function (data) {
    	top.winui.window.open({
            id: 'selectIcon',
            type: 2,
            title: '选择图标',
            content: '/system/icon?txtid=add_txt_icon&showid=add_i_icon&menuid=' + thisForm,
            maxOpen: true
        });
	        return false;
   	 });
    form.on('submit(upIconBtn)', function (data) {
    	top.winui.window.open({
            id: 'selectIcon',
            type: 2,
            title: '选择图标',
            content: '/system/icon?txtid=up_txt_icon&showid=up_i_icon&menuid=' + thisForm,
            maxOpen: true
        });
	        return false;
   	 });

    form.on('submit(addSubmit)', function (data) {
    	var CDMC = $("#add_cdmc").val();
    	var SJBM = $("#cdbm").val();
    	var URL = $("#add_url").val();
    	var PX = $("#add_px").val();
    	var BZ = $("#add_txt_icon").val();
    	
    	if(!CDMC) {
    		msg('请输入菜单名称', {
                icon: 2,
                time: 2000
            });
    		return false;
    	}
    	if(!SJBM) {
    		msg('请点击左侧菜单', {
                icon: 2,
                time: 2000
            });
    		return false;
    	}
    	if(!URL) {
    		msg('请输入URL', {
                icon: 2,
                time: 2000
            });
    		return false;
    	}
    	if(!PX) {
    		msg('请输入排序', {
                icon: 2,
                time: 2000
            });
    		return false;
    	}
    	if(!BZ) {
    		msg('请选择图标', {
                icon: 2,
                time: 2000
            });
    		return false;
    	}
    	
    	$.ajax({
    		url: '/api/system/menu/add',
    		data: {
    			CDMC: CDMC,
    			SJBM: SJBM,
    			URL: URL,
    			PX: PX,
    			BZ: BZ
    		},
    		type: 'post',
    		dataType: 'json',
    		headers: {token: localStorage["token"]},
    		success: function(data) {
    			if(data.code == "1"){
    				msg(data.msg, {
                        icon: 1,
                        time: 2000
                    });
                    setTimeout(() => {
                        location.href = location.href;
                    }, 2000);
                }else{
                	msg(data.msg, {
                        icon: 1,
                        time: 2000
                    });
                }
    		}
    	});
    	
	        return false;
   	 });
    form.on('submit(upSubmit)', function (data) {
    	var CDMC = $("#up_cdmc").val();
    	var CDBM = $("#cdbm").val();
    	var URL = $("#up_url").val();
    	var PX = $("#up_px").val();
    	var BZ = $("#up_txt_icon").val();
    	
    	if(!CDMC) {
    		msg('请输入菜单名称', {
                icon: 2,
                time: 2000
            });
    		return false;
    	}
    	if(!CDBM) {
    		msg('请点击左侧菜单', {
                icon: 2,
                time: 2000
            });
    		return false;
    	}
    	if(!URL) {
    		msg('请输入URL', {
                icon: 2,
                time: 2000
            });
    		return false;
    	}
    	if(!PX) {
    		msg('请输入排序', {
                icon: 2,
                time: 2000
            });
    		return false;
    	}
    	if(!BZ) {
    		msg('请选择图标', {
                icon: 2,
                time: 2000
            });
    		return false;
    	}
    	
    	$.ajax({
    		url: '/api/system/menu/update',
    		data: {
    			CDMC: CDMC,
    			CDBM: CDBM,
    			URL: URL,
    			PX: PX,
    			BZ: BZ
    		},
    		type: 'post',
    		dataType: 'json',
    		headers: {token: localStorage["token"]},
    		success: function(data) {
    			if(data.code == "1"){
    				msg(data.msg, {
                        icon: 1,
                        time: 2000
                    });
                    setTimeout(() => {
                        location.href = location.href;
                    }, 2000);
                }else{
                	msg(data.msg, {
                        icon: 1,
                        time: 2000
                    });
                }
    		}
    	});
    	
	        return false;
   	 });
    form.on('submit(delSubmit)', function (data) {
    	var CDBM = $("#cdbm").val();
    	
    	if(!CDBM) {
    		msg('请点击左侧菜单', {
                icon: 2,
                time: 2000
            });
    		return false;
    	}
    	var msgtxt = $("#del_msg").html();
    	winui.window.confirm(msgtxt, { icon: 3, title: '删除系统菜单' }, function (index) {
			layer.close(index);
    		$.ajax({
	    		url: '/api/system/menu/delete',
	    		data: {
	    			CDBM: CDBM
	    		},
	    		type: 'post',
	    		dataType: 'json',
	    		headers: {token: localStorage["token"]},
	    		success: function(data) {
	    	    	if(data.code == "1"){
	    				msg(data.msg, {
	                        icon: 1,
	                        time: 2000
	                    });
	                    setTimeout(() => {
	                        location.href = location.href;
	                    }, 2000);
	                }else{
	                	msg(data.msg, {
	                        icon: 1,
	                        time: 2000
	                    });
	                }
	    		}
	    	});
        });
    	
	        return false;
   	 });
    
    

    exports('menulist', {});
});

