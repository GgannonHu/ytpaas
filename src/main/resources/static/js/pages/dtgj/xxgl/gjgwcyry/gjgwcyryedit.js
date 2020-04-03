layui.config({
    base: '/js/core/winui/' //指定 winui 路径
}).extend({
    winui: 'winui',
    window: 'js/winui.window'
}).define(['table', 'laydate', 'jquery', 'winui','form','window'], function (exports) {
    var msg = winui.window.msg,
        form = layui.form,
        $ = layui.$, 
        layer = layui.layer,
        laydate = layui.laydate;
        var isUpd = false;
        var gjgwcyryid = $.getUrlParam("id");
    //时间
    laydate.render({
        elem: '#gjgwcyrySqsj'
        ,type: 'datetime'
    });    
    var fOkMsg = '';
    if(gjgwcyryid) {
        $.ajax({
            type: 'get',
            url: '/api/dtgj/gjgwcyry/getById',
            headers: {token: localStorage["token"]},
            data: {id: gjgwcyryid},
            dataType:'json',
            success: function (data) {
                if(data.code == "1"){
                    $("#gjgwcyryName").val(data.data.MC);
                    $("#gjgwcyrySqdd").val(data.data.SQDD);
                    $("#gjgwcyrySqsj").val(data.data.SQSJ);
                    $("#gjgwcyryWpms").val(data.data.MS);
                    isUpd = true;
                }else{
                    msg('数据加载失败，请重试', {
                        icon: 2,
                        time: 1000
                    });
                }
            },
            error: function (xml) {
                msg('数据加载失败，请重试', {
                    icon: 2,
                    time: 1000
                });
            }
        });
    }
    function getuser() {
        $.ajax({
            url: "/api/home/getUserByToken",
            method: 'GET',
            data: { 'token': localStorage["token"] },
            dataType: "JSON",
            success: function (d) {
                if (d && d.LOGINNAME) {
                    $("#hdfUser").val(d.LOGINNAME);
                }
            },
            error: function () {
                location.href = "/error";
            }
        });
    }
    form.on('submit(formEditMenu)', function (data) {
        if (winui.verifyForm(data.elem)) {
            var index = layer.load(1);
            var url = "/api/dtgj/gjgwcyry/add";
            if(gjgwcyryid) {
                url = "/api/dtgj/gjgwcyry/update";
                data.field.id = gjgwcyryid;
            }
            layui.$.ajax({
                type: 'post',
                url: url,
                data: data.field,
                dataType: 'json',
                headers: {token: localStorage["token"]},
                success: function (json) {
                    layer.close(index);
                    if (json.code == "1") {
                        fOkMsg = json.msg;
                        gjgwcyryid = json.data;
                        closeThis();
                    } else {
                        msg(json.msg);
                    }
                },
                error: function (xml) {
                    layer.close(index);
                    msg("操作失败", {
                        icon: 2,
                        time: 1000
                    });
                }
            });
        }
        return false;
    });
    function closeThis() {
        msg(fOkMsg, {
            icon: 1,
            time: 1000
        });
        var topWindow = top.winui.window.getWindow($.getUrlParam("menuid"));
        if (isUpd) {
            $(topWindow).find("#reloadTable").trigger("click");
        }
        else {
            $(topWindow).find("#searchMenu").trigger("click");
        }
        setTimeout(() => {
            top.winui.window.close('editGjgwcyry');
        }, 1000);
    }
    getuser();
    exports('gjgwcyryedit', {});
});