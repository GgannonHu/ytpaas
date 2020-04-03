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

    var jsbm = $.getUrlParam("id");
    
    if(jsbm) {
        $("#div_jslx").hide();
        
        $.ajax({
            type: 'get',
            url: '/api/system/role/getById',
            headers: {token: localStorage["token"]},
            data: {id: jsbm},
            dataType:'json',
            success: function (data) {
                $("#JSMC").val(data.JSMC);
                $("#JSMS").val(data.JSMS);
            },
            error: function (xml) {
                msg('数据加载失败，请重试', {
                    icon: 2,
                    time: 2000
                });
            }
        });
    }
    
    form.on('submit(formEditMenu)', function (data) {
        
        if (winui.verifyForm(data.elem)) {
            var index = layer.load(1);

            var url = "/api/system/role/add";
            if(jsbm) {
                url = "/api/system/role/update";
                data.field.JSBM = jsbm;
            }

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
                            
                            var topWindow = top.winui.window.getWindow($.getUrlParam("menuid"));
                            $(topWindow).find("#reloadTable").trigger("click");
                            
                            setTimeout(() => {
                                top.winui.window.close('editRole');
                            }, 2000);
                        } else {
                            msg(json.msg);
                        }
                    
                    }catch(e){
                        msg("操作失败", {
                            icon: 2,
                            time: 2000
                        });
                    }
                    layer.close(index);
                },
                error: function (xml) {
                    msg("操作失败", {
                        icon: 2,
                        time: 2000
                    });
                    layer.close(index);
                }
            });
        }
        
        return false;
    });

    exports('roleedit', {});
});