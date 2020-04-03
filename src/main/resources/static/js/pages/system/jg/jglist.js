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

    var cxqx = "", jgdm = "", tjqx = "", xgqx = "", scqx = "";
    
    
    function init() {
    	$.ajax({
    		url: '/api/system/sjqx',
    		data: {url: '/system/jg'},
    		type: 'get',
    		dataType: 'json',
    		headers: {token: localStorage["token"]},
    		success: function(data) {
    			jgdm = data.msg;
    			$.each(data.data, function() {
    				if(this.QXLB == "CXQX01" || this.QXLB == "CXQX02" || this.QXLB == "CXQX03" || 
    						this.QXLB == "CXQX04" || this.QXLB == "CXQX05") {
    					cxqx = this.QXLB;
    				} else if (this.QXLB == "TJQX") {
    					tjqx = this.QXLB;
    				} else if (this.QXLB == "XGQX") {
    					xgqx = this.QXLB;
    				} else if (this.QXLB == "SCQX") {
    					scqx = this.QXLB;
    				}
    			});
    			initJG();
    		}
    	});
    }
    function initJG() {
    	$.ajax({
    		url: '/api/system/jg/qxjg',
    		data: {sjqx: cxqx, jgdm: jgdm},
    		type: 'get',
    		dataType: 'json',
    		headers: {token: localStorage["token"]},
    		success: function(data) {
    			var items = "";
    			$.each(data.data, function() {
    				items += "<option value='" + this.JGDM + "' data-sjdm='" + this.SJDM + "'>" + this.JGMC + "</option>";
    			});
    			$("#ddl_jg").empty();
    			$("#ddl_jg").append(items);
    			
    			form.render();
    			
    			initTable();
    		}
    	});
    }
    
    
    //表格渲染
    var initTable = function(){
    	var sjdm = $("#ddl_jg").find("option:selected").data("sjdm");
    	var jgdm = $("#ddl_jg").find("option:selected").val();
    	
	    layer.load(1);
	    
	    treetable.render({
	        id: tableId,
	        treeColIndex :0,
	        treeSpid: jgdm,
	        treeIdName: 'JGDM',
	        treePidName: 'SJDM',
	        treeDefaultClose: true,	//是否默认折叠
            treeLinkage: false,		//父级展开时是否自动展开所有子级
	        elem: '#jgTable',
	        url: '/api/system/jg/jggl',
	        where: {sjqx: cxqx, jgdm: jgdm},
	        headers: {token: localStorage["token"]},
	        height: 'full-80', //自适应高度
	        even: false,  //隔行变色
	        page: false,
	        cols: [[
	            { field: 'JGMC', title: '机构名称' },
	            { field: 'JGDM', title: '机构代码' },
	            { field: 'WZPX', title: '排序', width: 80 },
	            { field: 'CJSJ', title: '采集时间' }
	        ]],
	        done:function(){
	        	//$("table").css("height","100%")
	        	layer.closeAll("loading");
	        }
	    });
    };
    
    init();
    $("#searchMenu").on("click", initTable);
    form.on('submit', function (data) {
	        return false;
   	 });

    exports('jglist', {});
});

