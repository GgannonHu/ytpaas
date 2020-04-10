var api = "/api/dtgj/zhld/qwgl/dwgl/abry/";
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
        abryid = $.getUrlParam("id"),
        type = $.getUrlParam("type");

    if (type == 'view') {
        $(".layui-input").attr("disabled", "disabled");
        $(".layui-input").attr("placeholder", "");
        $("select").attr("disabled", "disabled");
        form.render('select');

        $("#submit").hide();
        $("#cancle").hide();
        $("#select_gjxlbm_div").hide();
        $("#select_dtzdmc_div").hide();
        $("#select_zzmm_div").hide();
        $("#select_mz_div").hide();

        $("#txt_gjxlbm_div").show();
        $("#txt_dtzdmc_div").show();
        $("#txt_zzmm_div").show();
        $("#txt_mz_div").show();
    } else {
        $("#txt_gjxlbm_div").hide();
        $("#txt_dtzdmc_div").hide();
        $("#txt_zzmm_div").hide();
        $("#txt_mz_div").hide();
    }

    getUserByToken();

    /**
     * 
     * @param {TEMP_SYS_XTCS中的CSLX} tid 
     * @param {要渲染的控件ID} id 
     */
    function bindcon(tid, id, selected) {
        // alert(selected);
        $.ajax({
            url: "/api/dtgj/com/bindcon",
            headers: { token: localStorage["token"] },
            data: {
                tid: tid
            },
            dataType: "JSON",
            success: function (data) {
                var str = "<option value=''>请选择</option>";
                $.each(data.data, function () {
                    str += "<option value='" + this.BM + "'>" + this.MC + "</option>";
                });
                $("#" + id).html(str);
                $("#" + id).val(selected);
                var zzmm = $("#zzmm").find("option:selected").text();
                if (zzmm != '请选择') {
                    $("#txt_zzmm").val(zzmm);
                }

                var mz = $("#mz").find("option:selected").text();
                if (mz != '请选择') {
                    $("#txt_mz").val(mz);
                }


                form.render('select');

                form.on('select(' + id + ')', function (data) {
                    // alert(data.value); //得到被选中的值
                });
            }
        });
    }

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
                $("#txt_dtzdmc").val($("#dtzdmc").find("option:selected").text());
                form.render('select');

                form.on('select(dtzdmc)', function (data) {
                    // alert(data.value); //得到被选中的值
                });
            }
        });
    }

    function loadData() {

        if (abryid) {
            // $("#xm").addClass("layui-disabled");
            // $("#xm").prop("disabled", true);

            $.ajax({
                type: 'get',
                url: api + 'getById',
                headers: { token: localStorage["token"] },
                data: { id: abryid },
                dataType: 'json',
                success: function (data) {
                    if (data.code == "1") {
                        $("#id").val(data.data.ID);
                        $("#gjxlbm").val(data.data.GJXLBM);
                        bindzdmc(data.data.GJXLBM, data.data.DTZDBM);
                        $("#gmsfhm").val(data.data.ABRY_GMSFHM);
                        $("#xm").val(data.data.ABRY_XM);
                        $("#yddh").val(data.data.ABRY_YDDH);
                        $("#zzmm").val(data.data.ABRY_ZZMMDM);
                        bindcon("ZZMM", "zzmm", data.data.ABRY_ZZMMDM);
                        bindcon("MZ", "mz", data.data.ABRY_MZDM);
                        $("#dz").val(data.data.ABRY_DZMC);
                        $("#dw").val(data.data.ABRY_DWMC);
                        if (data.data.TJR != tjr) {
                            $(".layui-input").attr("disabled", "disabled");
                            $(".layui-input").attr("placeholder", "");
                            $("select").attr("disabled", "disabled");
                            form.render('select');

                            $("#submit").hide();
                            $("#cancle").hide();
                        }

                        $("#txt_gjxlbm").val($("#gjxlbm").find("option:selected").text());

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
        } else {
            bindcon("ZZMM", "zzmm", "");
            bindcon("MZ", "mz", "");
        }
    }

    form.on('submit(formEditMenu)', function (data) {

        if (winui.verifyForm(data.elem)) {
            var index = layer.load(1);

            var url = api + "add";
            if (abryid) {
                url = api + "update";
            }
            layui.$.ajax({
                type: 'post',
                url: url,
                data: {
                    dtzdbm: $("#dtzdmc").val(),
                    dtzdmc: $("#dtzdmc").find("option:selected").text(),
                    gjxlbm: $("#gjxlbm").val(),
                    gmsfhm: $("#gmsfhm").val(),
                    xm: $("#xm").val(),
                    yddh: $("#yddh").val(),
                    zzmm: $("#zzmm").val(),
                    mz: $("#mz").val(),
                    dz: $("#dz").val(),
                    dw: $("#dw").val(),
                    id: abryid,
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
                            if (abryid)
                                $(topWindow).find("#reloadTable").trigger("click");
                            else
                                $(topWindow).find("#searchMenu").trigger("click");

                            setTimeout(() => {
                                top.winui.window.close('editAbry');
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

    exports('abryedit', {});
});