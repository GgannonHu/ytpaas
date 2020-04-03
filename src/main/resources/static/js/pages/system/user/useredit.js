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
    var thisForm = "editUser";
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
    			$.each(data.data, function() {
    				// 初始化查询权限、添加权限、修改权限、删除权限
    				if(this.QXLB == "CXQX01" || this.QXLB == "CXQX02" || this.QXLB == "CXQX03" || 
    						this.QXLB == "CXQX04" || this.QXLB == "CXQX05") {
    					cxqx = this.QXLB;
    					jgdm = this.QXNR;
    				} else if (this.QXLB == "TJQX") {
    					tjqx = this.QXLB;
    				} else if (this.QXLB == "XGQX") {
    					xgqx = this.QXLB;
    				} else if (this.QXLB == "SCQX") {
    					scqx = this.QXLB;
    				}
    			});
    			initRole();
    		}
    	});
    }
    function initRole() {
    	$.ajax({
            type: 'get',
            url: '/api/system/role/list',
            headers: {token: localStorage["token"]},
            data: {id: userid},
            dataType:'json',
            success: function (data) {
                if(data.code == "1"){
            		$("#JSBM").empty();
                	$.each(data.data, function() {
                		$("#JSBM").append('<input type="checkbox" name="JSBM" value="' + this.JSBM + '" lay-skin="primary" title="' + this.JSMC + '">');
                		form.render();
                	});
                	initEdit();
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
    function initEdit() {
    	if(userid) {
            $("#loginId").addClass("layui-disabled");
            $("#loginId").prop("disabled", true);
            
            $.ajax({
                type: 'get',
                url: '/api/system/user/getById',
                headers: {token: localStorage["token"]},
                data: {id: userid},
                dataType:'json',
                success: function (data) {
                    if(data.code == "1"){
                        $("#LOGINNAME").val(data.data.LOGINNAME);
                        $("#LOGINNAME").attr("disabled", true).addClass("layui-disabled");
                        $("#div_password").hide();
                        $("#YHXM").val(data.data.YHXM);
                        $("#YHSFZH").val(data.data.YHSFZH);
                        $("#YHLXDH").val(data.data.YHLXDH);
                        $("#YHJH").val(data.data.YHJH);
                        $("#jgdm").val(data.data.DWDM);
                        $("#jgmc").val(data.data.DWMC);
                        var JSBM = data.data.JSBM.split(",");
                    	$("input[name=JSBM]").each(function() {
                    		var that = this;
                    		$.each(JSBM, function() {
                    			if($(that).val() == this) {
                            		$(that).prop("checked", true);
                            	}
                    		});
                        });
                    	form.render();
                        
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
    }
    
    
    init();
    form.on('submit(formEditMenu)', function (data) {
        
        if (winui.verifyForm(data.elem)) {
            
            var url = "/api/system/user/add";
            if(userid) {
                url = "/api/system/user/update";
                data.field.YHBM = userid;
            }
            
            if(!data.field.DWDM) {
            	msg("请选择单位", {
                    icon: 2,
                    time: 2000
                });
            	return false;
            }
            var JSBM = [];
            $("input[name=JSBM]").each(function() {
            	if($(this).prop("checked")) {
            		JSBM.push($(this).val());
            	}
            });
            if(JSBM.length == 0) {
            	msg("请选择角色", {
                    icon: 2,
                    time: 2000
                });
            	return false;
            }
            data.field.PASSWORD = "123";
            data.field.JSBM = JSBM.join(",");
            
            var loadIndex = layer.load(1);
            
            layui.$.ajax({
                type: 'post',
                url: url,
                data: data.field,
                dataType: 'json',
                headers: {token: localStorage["token"]},
                success: function (json) {
                    try{
                        if (json.code == "1") {
                            msg(json.msg, {
                                icon: 1,
                                time: 2000
                            });
                            var topWindow = top.winui.window.getWindow(parentForm);
                            if(userid)
                                $(topWindow).find("#reloadTable").trigger("click");
                            else
                                $(topWindow).find("#searchMenu").trigger("click");
                            setTimeout(() => {
                                top.winui.window.close('editUser');
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
        }
        
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
    
    exports('useredit', {});
});