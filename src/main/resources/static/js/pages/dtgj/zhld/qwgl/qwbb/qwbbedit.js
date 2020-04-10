var api = "/api/dtgj/zhld/qwgl/qwbb/";
var tjr = "";
var tjdw = "";
var tjdwmc = "";
layui.config({
    base: '/js/core/winui/' //指定 winui 路径
}).extend({
    winui: 'winui',
    window: 'js/winui.window'
}).define(['table', 'jquery', 'winui', 'form', 'window', 'laydate'], function (exports) {

    function getUserByToken() {
        $.ajax({
            url: "/api/home/getUserByToken",
            method: 'GET',
            data: { 'token': localStorage["token"] },
            dataType: "JSON",
            success: function (data) {
                if (data && data.LOGINNAME) {
                    tjr = data.LOGINNAME;
                    tjdw = data.DWDM;
                    tjdwmc = data.DWMC;
                }
            },
            error: function () {
                location.href = "/error";
            }
        });
    }

    var msg = winui.window.msg,
        form = layui.form,
        $ = layui.$,
        layer = layui.layer,
        laydate = layui.laydate;

    laydate.render({
        elem: '#fbsj',
        value: new Date(),
        type: 'datetime'
    });

    laydate.render({
        elem: '#wcsj',
        value: new Date(),
        type: 'datetime'
    });

    getUserByToken();

    var qwbbid = $.getUrlParam("id");
    var type = $.getUrlParam("type");

    if (type == 'view') {
        $(".layui-input").attr("disabled", "disabled");
        $(".layui-input").attr("placeholder", "");
        $("select").attr("disabled", "disabled");
        form.render('select');
        $("#qwnr").attr("disabled", "disabled");

        $("#submit").hide();
        $("#cancle").hide();
        $("#txt_wczt_div").show();
        $("#select_wczt_div").hide();
    } else {
        $("#txt_wczt_div").hide();
    }

    if (qwbbid) {
        // $("#xm").addClass("layui-disabled");
        // $("#xm").prop("disabled", true);

        $.ajax({
            type: 'get',
            url: api + 'getById',
            headers: { token: localStorage["token"] },
            data: { id: qwbbid },
            dataType: 'json',
            success: function (data) {
                if (data.code == "1") {
                    $("#id").val(data.data.ID);
                    $("#xm").val(data.data.XM);
                    $("#sfzh").val(data.data.SFZH);
                    $("#qwnr").val(data.data.QWNR);
                    $("#fbsj").val(data.data.FBSJ);
                    $("#wcsj").val(data.data.WCSJ);
                    $("#wczt").val(data.data.WCZT);
                    $("#txt_wczt").val(data.data.WCZT);
                    $("#tjr").val(data.data.TJR);
                    $("#tjdw").val(data.data.TJDW);
                    $("#tjsj").val(data.data.TJSJ);

                    if (data.data.TJR != tjr) {
                        $(".layui-input").attr("disabled", "disabled");
                        $(".layui-input").attr("placeholder", "");
                        $("select").attr("disabled", "disabled");
                        form.render('select');
                        $("#qwnr").attr("disabled", "disabled");

                        $("#submit").hide();
                        $("#cancle").hide();
                    }

                    form.render('select')
                } else {
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

    form.on('submit(formEditMenu)', function (data) {

        if (winui.verifyForm(data.elem)) {
            var index = layer.load(1);

            var url = api + "add";
            if (qwbbid) {
                url = api + "update";
                data.field.id = qwbbid;
            }

            layui.$.ajax({
                type: 'post',
                url: url,
                data: data.field,
                dataType: 'json',
                headers: { token: localStorage["token"] },
                success: function (json) {
                    try {
                        if (json.code == "1") {
                            msg(json.msg, {
                                icon: 1,
                                time: 2000
                            });

                            var topWindow = top.winui.window.getWindow($.getUrlParam("menuid"));
                            if (qwbbid)
                                $(topWindow).find("#reloadTable").trigger("click");
                            else
                                $(topWindow).find("#searchMenu").trigger("click");

                            setTimeout(() => {
                                top.winui.window.close('editQwbb');
                            }, 2000);
                        } else {
                            msg(json.msg);
                        }

                    } catch (e) {
                        msg("操作失败", {
                            icon: 2,
                            time: 2000
                        });
                    }
                },
                error: function (xml) {
                    msg("操作失败", {
                        icon: 2,
                        time: 2000
                    });
                }
            });
            layer.close(index);
        }

        return false;
    });

    exports('qwbbedit', {});
});