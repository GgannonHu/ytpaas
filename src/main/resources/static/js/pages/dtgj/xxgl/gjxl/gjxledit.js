var mUrlTop = "/api/dtgj/xxgl/gjxl";
layui.config({
    base: '/js/core/winui/' //指定 winui 路径
}).extend({
    winui: 'winui',
    window: 'js/winui.window'
}).define(['table', 'jquery', 'winui', 'form', 'window', 'laydate'], function (exports) {
    var msg = winui.window.msg;
    var form = layui.form;
    var $ = layui.$;
    var layer = layui.layer;
    var laydate = layui.laydate;

    var selId = $.getUrlParam("id");
    var pageType = $.getUrlParam("type");;

    //修改页面是否只读样式
    if (pageType == 'sel') {
        $('#pageSubmit').hide();
        $('#divXzqh').show();
        $(".layui-input").attr("disabled", "disabled");
        $(".layui-input").attr("placeholder", "");
    } else {
        $('#pageSubmit').show();
        laydate.render({
            elem: '#qdzsmc_kssj', type: 'time'
        });
        laydate.render({
            elem: '#qdzsmc_jssj', type: 'time'
        });
        laydate.render({
            elem: '#zdzsmc_kssj', type: 'time'
        });
        laydate.render({
            elem: '#zdzsmc_jssj', type: 'time'
        });
    }

    //修改初始化
    if (selId) {
        $("#gjxlbm").attr("disabled", "disabled");
        $("#gjxlmc").attr("disabled", "disabled");
        var index = layer.load(1);
        $.ajax({
            type: 'get',
            url: mUrlTop + '/getitembyid',
            headers: { token: localStorage["token"] },
            data: { id: selId },
            dataType: 'json',
            success: function (data) {
                layer.close(index);
                if (data.code == "1") {
                    var item = data.data;
                    getCsmcByBm('showXzqh', 'XZQH', item.XZQHDM);//$('#showXzqh').val(item.XZQHMC);

                    $("#gjxlbm").val(item.GJXLBM);
                    $("#gjxlmc").val(item.GJXLMC);
                    $("#gjxlqdz").val(item.GJXLQDZ);
                    $("#gjxlzdz").val(item.GJXLZDZ);

                    var qdzsmc_kssj = item.QDZSMC_KSSJ;
                    if (qdzsmc_kssj != null) {
                        $("#qdzsmc_kssj").val(qdzsmc_kssj.substring(qdzsmc_kssj.indexOf(' '), qdzsmc_kssj.length));
                    }
                    var qdzsmc_jssj = item.QDZSMC_JSSJ;
                    if (qdzsmc_jssj != null) {
                        $("#qdzsmc_jssj").val(qdzsmc_jssj.substring(qdzsmc_jssj.indexOf(' '), qdzsmc_jssj.length));
                    }
                    var zdzsmc_kssj = item.ZDZSMC_KSSJ;
                    if (zdzsmc_kssj != null) {
                        $("#zdzsmc_kssj").val(zdzsmc_kssj.substring(zdzsmc_kssj.indexOf(' '), zdzsmc_kssj.length));
                    }
                    var zdzsmc_jssj = item.ZDZSMC_JSSJ;
                    if (zdzsmc_jssj != null) {
                        $("#zdzsmc_jssj").val(zdzsmc_jssj.substring(zdzsmc_jssj.indexOf(' '), zdzsmc_jssj.length));
                    }

                } else {
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

    //提交
    form.on('submit(formEditMenu)', function (data) {
        if (winui.verifyForm(data.elem)) {
            var index = layer.load(1);

            var url = mUrlTop + "/add";
            if (selId) {
                url = mUrlTop + "/update";
                data.field.id = selId;
            }
            layui.$.ajax({
                type: 'post',
                url: url,
                data: data.field,
                dataType: 'json',
                headers: { token: localStorage["token"] },
                success: function (json) {
                    layer.close(index);
                    if (json.code == "1") {
                        fOkMsg = json.msg;
                        selId = json.data;

                        closeThis();
                    } else {
                        msg(json.msg, {
                            icon: 2,
                            time: 1000
                        });
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

    //关闭窗体
    function closeThis() {
        msg(fOkMsg, {
            icon: 1,
            time: 1000
        });

        var topWindow = top.winui.window.getWindow($.getUrlParam("menuid"));

        if (pageType == 'upd') {
            $(topWindow).find("#btnReloadTable").trigger("click");
        }
        else {
            $(topWindow).find("#btnSel").trigger("click");
        }
        setTimeout(() => {
            top.winui.window.close('editGjxl');
        }, 1000);
    }

    function getCsmcByBm(varId, varTid, varBm) {
        var index = layer.load(1);
        $.ajax({
            type: 'get',
            url: '/api/dtgj/com/getcsmcbybm',
            headers: { token: localStorage["token"] },
            data: { tid: varTid, bm: varBm },
            dataType: 'json',
            success: function (data) {
                layer.close(index);
                $('#' + varId).val(data.data);
            }
        });
    }

    exports('gjxledit', {});
});
