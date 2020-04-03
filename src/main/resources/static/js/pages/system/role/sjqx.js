layui.config({
    base: '/js/core/' //指定 winui 路径
}).extend({
    winui: 'winui/winui',
    treetable:'treetable/treetable',
    window: 'winui/js/winui.window'
}).define(['table', 'jquery', 'winui','form','window', 'tree'], function (exports) {
    var msg = winui.window.msg,
		table = layui.table,
    	treetable = layui.treetable,
        form = layui.form,
        $ = layui.$, 
        layer = layui.layer
        tableId = 'tableId';

    var jsbm = $.getUrlParam("id");
    var jsmc = $.getUrlParam("jsmc");
    var thisForm = "editSJQX";
    var parentForm = $.getUrlParam("menuid");

    var treeData = [];
    
    if(jsbm) {
        $("#title").html(jsmc);
        
        $.ajax({
    		url: '/api/system/role/qxcd',
    		data: {jsbm: jsbm},
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
    	$("#cdmc").html(obj.name);
    	$("#cdurl").html(obj.pageurl);
    	
    	$.ajax({
    		url: '/api/system/sjqx',
    		data: {url: obj.pageurl},
    		type: 'get',
    		dataType: 'json',
    		headers: {token: localStorage["token"]},
    		success: function(data) {
    			var cxqx,tjqx,xgqx, scqx;
    			$.each(data.data, function() {
    				// 初始化查询权限、添加权限、修改权限、删除权限
    				if(this.QXLB == "CXQX01" || this.QXLB == "CXQX02" || this.QXLB == "CXQX03" || 
    						this.QXLB == "CXQX04" || this.QXLB == "CXQX05") {
    					cxqx = this.QXLB;
    				} else if (this.QXLB == "TJQX") {
    					tjqx = this.QXLB;
    					$("#url2").val(this.QXURL);
    				} else if (this.QXLB == "XGQX") {
    					xgqx = this.QXLB;
    					$("#url3").val(this.QXURL);
    				} else if (this.QXLB == "SCQX") {
    					scqx = this.QXLB;
    					$("#url4").val(this.QXURL);
    				}
    				
    			});


				if(cxqx) {
					$("#chk1").prop("checked", true);
					$("#cxqx").val(cxqx);
				} else {
					$("#chk1").prop("checked", false);
					$("#cxqx").val("CXQX02");
				}
				if(tjqx) {
					$("#chk2").prop("checked", true);
				} else {
					$("#chk2").prop("checked", false);
					$("#url2").val("");
				}
				if(xgqx) {
					$("#chk3").prop("checked", true);
				} else {
					$("#chk3").prop("checked", false);
					$("#url3").val("");
				}
				if(scqx) {
					$("#chk4").prop("checked", true);
				} else {
					$("#chk4").prop("checked", false);
					$("#url4").val("");
				}
				form.render();
    		}
    	});
    }
    
    
    $('#btnSave1').click(function () {
    	$.ajax({
    		url: '/api/system/role/save/sjqx',
    		type: 'post',
    		data: {jsbm: jsbm, type: '1'},
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
    $('#btnSave2').click(function () {
    	var cdurl = $("#cdurl").html();
    	var cdmc = $("#cdmc").html();
    	
    	if(!cdurl) {
    		msg('请选择左侧菜单', {
                icon: 2,
                time: 2000
            });
    		return false;
    	}
    	
    	var sjqx = [];
    	if($("#chk1").is(':checked')) {
    		sjqx.push({
        		SJCDBM: cdurl,
        		JSBM: jsbm,
        		QXMC: "查询权限-" + cdmc,
        		QXURL: cdurl,
        		QXLB: $("#cxqx").find("option:selected").val(),
        		QXNR: ""
        	});
    	}
    	if($("#chk2").is(':checked')) {
    		sjqx.push({
        		SJCDBM: cdurl,
        		JSBM: jsbm,
        		QXMC: "添加权限-" + cdmc,
        		QXURL: $("#url2").val(),
        		QXLB: "TJQX",
        		QXNR: ""
        	});
    	}
    	if($("#chk3").is(':checked')) {
    		sjqx.push({
        		SJCDBM: cdurl,
        		JSBM: jsbm,
        		QXMC: "修改权限-" + cdmc,
        		QXURL: $("#url3").val(),
        		QXLB: "XGQX",
        		QXNR: ""
        	});
    	}
    	if($("#chk4").is(':checked')) {
    		sjqx.push({
        		SJCDBM: cdurl,
        		JSBM: jsbm,
        		QXMC: "删除权限-" + cdmc,
        		QXURL: $("#url4").val(),
        		QXLB: "SCQX",
        		QXNR: ""
        	});
    	}
    	
    	$.ajax({
    		url: '/api/system/role/save/sjqx',
    		type: 'post',
    		data: {jsbm: jsbm, cdurl: cdurl, type: '2', sjqx: JSON.stringify(sjqx)},
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

    exports('sjqx', {});
});