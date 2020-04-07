var api = "/api/dtgj/zhld/qwgl/dwgl/qwll/";
var dwdmsj = "";
var dwdmfj = "";
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
                    dwdmsj = data.DWDM.substring(0, 4);
                    dwdmfj = data.DWDM.substring(0, 6);
                    if (dwdmfj == "230000") {
                        bindxlxx("23");
                    } else if (dwdmfj.substring(4, 6) == "00") {
                        bindxlxx(dwdmsj);
                    } else {
                        bindxlxx(dwdmfj);
                    }
                }
            },
            error: function () {
                location.href = "/error";
            }
        });
    }

    var msg = winui.window.msg,
        $ = layui.$,
        form = layui.form,
        layer = layui.layer,
        laydate = layui.laydate,
        qwllid = $.getUrlParam("id");

    getUserByToken();

    function bindxlxx(xzqh) {
        $.ajax({
            url: api + "xlxx",
            headers: { token: localStorage["token"] },
            data: {
                xzqh: xzqh
            },
            dataType: "JSON",
            success: function (data) {
                var str = "<option value=''>请选择</option>";
                $.each(data.data, function () {
                    str += "<option value='" + this.BM + "'>" + this.MC + "</option>";
                });
                $("#gjxlbm").html(str);
                loadData();

                form.render('select');
                form.on('select(gjxlbm)', function (data) {
                    // alert(data.value); //得到被选中的值
                    bindzdmc(data.value, "");
                });
            }
        });
    }

    function bindzdmc(bm, selected) {
        $.ajax({
            url: api + "zdxx",
            headers: { token: localStorage["token"] },
            data: {
                bm: bm
            },
            dataType: "JSON",
            success: function (data) {
                var str = "<option value=''>请选择</option>";
                $.each(data.data, function () {
                    str += "<option value='" + this.BM + "'>" + this.MC + "</option>";
                });
                $("#dtzdmc").html(str);
                $("#dtzdmc").val(selected);
                form.render('select');

                form.on('select(dtzdmc)', function (data) {
                    // alert(data.value); //得到被选中的值
                });
            }
        });
    }

    function loadData() {

        if (qwllid) {
            // $("#xm").addClass("layui-disabled");
            // $("#xm").prop("disabled", true);

            $.ajax({
                type: 'get',
                url: api + 'getById',
                headers: { token: localStorage["token"] },
                data: { id: qwllid },
                dataType: 'json',
                success: function (data) {
                    if (data.code == "1") {
                        $("#id").val(data.data.ID);
                        $("#gjxlbm").val(data.data.GJXLBM);
                        bindzdmc(data.data.GJXLBM, data.data.DTZDBM);
                        $("#gjgsmc").val(data.data.GJGSMC);
                        $("#xm").val(data.data.QWLL_XM);
                        $("#lxdh").val(data.data.QWLL_LXDH);
                        $("#jybh").val(data.data.QWLL_JYBH);
                        $("#qwlbdm").val(data.data.QWLB_QWLBDM);
                        if (data.data.TJR != tjr) {
                            $("#submit").hide();
                            $("#cancle").text('确定');
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
    }

    form.on('submit(formEditMenu)', function (data) {

        if (winui.verifyForm(data.elem)) {
            var index = layer.load(1);

            var url = api + "add";
            if (qwllid) {
                url = api + "update";
            }
            layui.$.ajax({
                type: 'post',
                url: url,
                data: {
                    dtzdbm: $("#dtzdmc").val(),
                    dtzdmc: $("#dtzdmc").find("option:selected").text(),
                    gjxlbm: $("#gjxlbm").val(),
                    gjgsmc: $("#gjgsmc").val(),
                    xm: $("#xm").val(),
                    lxdh: $("#lxdh").val(),
                    jybh: $("#jybh").val(),
                    qwlbdm: $("#qwlbdm").val(),
                    id: qwllid,
                    tjr: tjr,
                    tjdw: tjdw,
                    tjdwmc, tjdwmc
                },
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
                            if (qwllid)
                                $(topWindow).find("#reloadTable").trigger("click");
                            else
                                $(topWindow).find("#searchMenu").trigger("click");

                            setTimeout(() => {
                                top.winui.window.close('editQwll');
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

    exports('qwlledit', {});
});