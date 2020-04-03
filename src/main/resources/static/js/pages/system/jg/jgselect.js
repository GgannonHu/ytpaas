layui.config({
    base: '/js/core/' //指定 winui 路径
    , version: '1.0.0-beta'
}).extend({
    winui: 'winui/winui',
    treetable:'treetable/treetable',
    window: 'winui/js/winui.window'
}).define(['table', 'jquery', 'winui', 'window', 'layer','treetable', 'form'], function (exports) {

    winui.renderColor();

    var table = layui.table,treetable = layui.treetable,
    $ = layui.$, tableId = 'jgTable';
    var form = layui.form;
    
    var menuid = $.getUrlParam("menuid"),
    newMenuid = $.getUrlParam("newMenuid")

    var cxqx = $.getUrlParam("sjqx"), jgdm = $.getUrlParam("jgdm"), closeReset = $.getUrlParam("closeReset");
    var selJgdm = "", selJgmc = "";
    
    /**
     * CXQX01 查询全部
     * CXQX02 查询本单位及下属所有单位
     * CXQX03 查询本单位
     */
    
    function init() {
    	$.ajax({
    		url: '/api/system/jg/qxjg',
    		data: {sjqx: cxqx, jgdm: jgdm},
    		type: 'get',
    		dataType: 'json',
    		headers: {token: localStorage["token"]},
    		success: function(data) {
    			var items = "<option value='-1'>-请选择-</option>";
    			$.each(data.data, function() {
    				items += "<option value='" + this.JGDM + "'>" + this.JGMC + "</option>";
    			});
    			$("#sel1").empty();
    			$("#sel1").append(items);
    	    	selJgdm = $("#sel1").find("option:selected").val();
    	    	selJgmc = $("#sel1").find("option:selected").text();
    			
    			form.render();
    		}
    	});
    }
    function bindChild(sjdm, sel1) {
    	$.ajax({
    		url: '/api/system/jg/zjg',
    		data: {jgdm: sjdm},
    		type: 'get',
    		dataType: 'json',
    		headers: {token: localStorage["token"]},
    		success: function(data) {
    			var items = "<option value='-1'>-请选择-</option>";
    			$.each(data.data, function() {
    				items += "<option value='" + this.JGDM + "'>" + this.JGMC + "</option>";
    			});
    			$(sel1).empty();
    			$(sel1).append(items);
    			
    			form.render();
    		}
    	});
    }
    
    init();
    
    form.on('select(sel1)', function (data) {
    	if(data.value == "-1") {
        	$("#div2").hide();
    	} else {
        	if(cxqx == "CXQX03") {
        		var items = "<option value='-1'>本单位</option>";
        		$('#sel2').empty();
    			$('#sel2').append(items);
        	} else {
        		bindChild(data.value, '#sel2');
        	}
        	$("#div2").show();
    	}

    	selJgdm = $("#sel1").find("option:selected").val();
    	selJgmc = $("#sel1").find("option:selected").text();
    	$("#div3").hide();
    	$("#div4").hide();
    	return false;
    });
    form.on('select(sel2)', function (data) {
    	if(data.value == "-1") {
        	selJgdm = $("#sel1").find("option:selected").val();
        	selJgmc = $("#sel1").find("option:selected").text();
        	$("#div3").hide();
    	} else {
        	selJgdm = $("#sel2").find("option:selected").val();
        	selJgmc = $("#sel2").find("option:selected").text();
        	$("#div3").show();
    		bindChild(data.value, '#sel3');
    	}
    	
    	$("#div4").hide();
    	return false;
    });
    form.on('select(sel3)', function (data) {
    	if(data.value == "-1") {
        	selJgdm = $("#sel2").find("option:selected").val();
        	selJgmc = $("#sel2").find("option:selected").text();
        	$("#div4").hide();
    	} else {
        	selJgdm = $("#sel3").find("option:selected").val();
        	selJgmc = $("#sel3").find("option:selected").text();
        	$("#div4").show();
    		bindChild(data.value, '#sel4');
    	}
    	
    	return false;
    });
    form.on('select(sel4)', function (data) {
    	if(data.value == "-1") {
        	selJgdm = $("#sel3").find("option:selected").val();
        	selJgmc = $("#sel3").find("option:selected").text();
    	} else {
        	selJgdm = $("#sel4").find("option:selected").val();
        	selJgmc = $("#sel4").find("option:selected").text();
    	}
    	
    	return false;
    });
    form.on('submit(btn_ok)', function (data) {
    	if(!selJgdm || selJgdm == "-1") {
    		top.winui.window.msg('请选择机构', {
                time: 2000
            });
    	} else {
    		var topWindow = top.winui.window.getWindow(menuid);
            $(topWindow).find("#jgmc").html(selJgmc);
            $(topWindow).find("#jgmc").val(selJgmc);
            $(topWindow).find("#jgdm").val(selJgdm);
            
        	top.winui.window.close(newMenuid);
    	}
    	return false;
    });
    form.on('submit(btn_reset)', function (data) {
    	var topWindow = top.winui.window.getWindow(menuid);
        $(topWindow).find("#jgmc").html("请选择机构");
        $(topWindow).find("#jgmc").val("请选择机构");
        $(topWindow).find("#jgdm").val(jgdm);
        
    	top.winui.window.close(newMenuid);
    	return false;
    });
    form.on('submit(btn_cancel)', function (data) {
    	top.winui.window.close(newMenuid);
    	return false;
    });
    if(closeReset) {
    	$("#btn_reset").hide();
    }

    exports('jgselect', {});
});

