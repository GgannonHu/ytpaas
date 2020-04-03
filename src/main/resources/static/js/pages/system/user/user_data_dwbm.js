layui.config({
    base: '/js/core/winui/' //指定 winui 路径
}).extend({
    winui: 'winui',
    window: 'js/winui.window'
}).define(['table', 'jquery', 'winui','form','window'], function (exports) {
    var msg = winui.window.msg,
        form = layui.form,
        $ = layui.$, 
        layer = layui.layer;

    var userid = $.getUrlParam("id");
    var xm = $.getUrlParam("xm");
    var thisForm = "dataUser";
    var parentForm = $.getUrlParam("menuid");
    
    // 查询权限、权限机构代码、添加权限、修改权限、删除权限
    var cxqx = "", jgdm = "", tjqx = "", xgqx = "", scqx = "";

    // 初始化权限
    function init() {
    	$.ajax({
    		url: '/api/system/sjqx',
    		data: {url: '/system/user'},
    		type: 'get',
    		dataType: 'json',
    		headers: {token: localStorage["token"]},
    		success: function(data) {
                jgdm = data.data.dwbm;
    			$.each(data.data.sjqx, function() {
    				// 初始化查询权限、添加权限、修改权限、删除权限
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
    			initEdit();
    		}
    	});
    }
    function initEdit() {
        $("#xm").html(xm);
    	$.ajax({
            type: 'get',
            url: '/api/system/user/qxdw',
            headers: {token: localStorage["token"]},
            data: {yhbm: userid},
            dataType:'json',
            success: function (data) {
                if(data.code == "1"){
                    $("#qxdw").html(data.data.dwmc);
                    
                }else{
                    msg('数据加载失败，请重试', {
                        icon: 2,
                        time: 2000
                    });
                }
                
            },
            error: function (xml) {
                msg('数据加载失败，请重试', {
                    icon: 2,
                    time: 2000
                });
            }
        });
    }
    
    
    init();
    form.on('submit(formEditMenu)', function (data) {
        var a1 = $("#qxdwbm").val();

        if(!a1) {
            msg("请选择权限单位", {
                icon: 2,
                time: 2000
            });
            return false;
        }
        var loadIndex = layer.load(1);
        
        $.ajax({
            type: 'post',
            url: '/api/system/user/datadw/update',
            data: {yhbm: userid, qxdw: a1},
            dataType: 'json',
            headers: {token: localStorage["token"]},
            success: function (json) {
                try{
                    if (json.code == "1") {
                        msg(json.msg, {
                            icon: 1,
                            time: 2000
                        });
                        
                        setTimeout(() => {
                            top.winui.window.close(thisForm);
                        }, 2000);
                    } else {
                        msg(json.msg);
                        layer.close(loadIndex);
                    }
                
                }catch(e){
                    msg("操作失败", {
                        icon: 2,
                        time: 2000
                    });
                    layer.close(loadIndex);
                }
            },
            error: function (xml) {
                msg("操作失败", {
                    icon: 2,
                    time: 2000
                });
                layer.close(loadIndex);
            }
        });

        return false;
    });

    // 选择机构
    form.on('submit(btnJG)', function (data) {
    	var selectJGForm = "selectJG";
    	top.winui.window.open({
            id: selectJGForm,
            title: '选择单位',
            type: 2,
            area: ['400px', '300px'],
            content: '/system/jg/select?closeReset=1&sjqx=' + cxqx + '&jgdm=' + jgdm + '&menuid=' + thisForm + '&newMenuid=' + selectJGForm,
            maxOpen: true
        });
        return false;
    });
    // 选择机构
    form.on('submit(btnOK)', function (data) {
        var a1 = $("#jgdm").val();
        var a2 = $("#jgmc").val();

        if(a1 && a2) {
            a1 = $("#qxdwbm").val() ? $("#qxdwbm").val() + ',' + a1 : a1;
            a2 = $("#qxdwmc").html() ? $("#qxdwmc").html() + ',' + a2 : a2;
    
            $("#qxdwbm").val(a1);
            $("#qxdwmc").html(a2);
            $("#jgdm").val('');
            $("#jgmc").val('');
        }
        
        return false;
    });
    
    exports('user_data_dwbm', {});
});