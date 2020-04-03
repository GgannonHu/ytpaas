var mUrlTop = "/api/dtgj/xxgl/dtxl";
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

    var mSelXzqhdm = '';
    //修改页面是否只读样式
    if (pageType == 'sel') {
        $('#pageSubmit').hide();
        $("#xzqhsj").attr("disabled", "disabled");
        $("#xzqhfj").attr("disabled", "disabled");
        $("#dtxlbh").attr("disabled", "disabled");
        $("#dtxlmc").attr("disabled", "disabled");
        $("#qdzsmc_kssj").attr("disabled", "disabled");
        $("#qdzsmc_jssj").attr("disabled", "disabled");
        $("#zdzsmc_kssj").attr("disabled", "disabled");
        $("#zdzsmc_jssj").attr("disabled", "disabled");
        $("#dtxlqdz").attr("disabled", "disabled");
        $("#dtxlzdz").attr("disabled", "disabled");
        $("#xllc").attr("disabled", "disabled");
        $('#divXzqh').show();
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
        $("#dtxlbh").attr("disabled", "disabled");
        $("#dtxlmc").attr("disabled", "disabled");
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
                    mSelXzqhdm = item.XZQHDM;
                    $('#showXzqh').val(item.XZQHMC);

                    $("#dtxlbh").val(item.DTXLBH);
                    $("#dtxlmc").val(item.DTXLMC);
                    $("#dtxlqdz").val(item.DTXLQDZ);
                    $("#dtxlzdz").val(item.DTXLZDZ);
                    $("#xllc").val(item.XLLC);

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

                    SetSelectData('xzqhsj', '230000000000', 4);
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
    } else {
        SetSelectData('xzqhsj', '230000000000');
    }

    //提交
    form.on('submit(formEditMenu)', function (data) {
        if (winui.verifyForm(data.elem)) {
            /*
            var tmpSj = $("#xzqhsj").val();
            var tmpFj = $("#xzqhfj").val();

            if (tmpSj.length > 0) {
                if (tmpFj.length > 0) {
                    data.field.xzqhdm = tmpFj.substr(0, 6);
                } else {
                    data.field.xzqhdm = tmpSj.substr(0, 6);
                }
            } else {
                msg("请选择行政区划", {
                    icon: 2,
                    time: 1000
                });
                return false;
            }
            */
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
            top.winui.window.close('editDtxl');
        }, 1000);
    }


    function SetSelectData(varSelId, varPid, varJb) {
        return;
        if (varPid.length > 0) {
            var index = layer.load(1);
            layui.$.ajax({
                type: 'post',
                url: '/api/dtgj/com/jglist',
                data: { pid: varPid },
                dataType: 'json',
                headers: { token: localStorage["token"] },
                success: function (json) {
                    layer.close(index);
                    if (json.code == "1") {
                        var items = json.data;
                        $('#' + varSelId + ' option').remove();
                        if (items.length > 0) {
                            var tmpSelVal = '';
                            $('#' + varSelId).append('<option value="" >请选择</option>');
                            for (var i = 0; i < items.length; i++) {
                                item = items[i];
                                var selected = '';
                                if (mSelXzqhdm.length > 0) {
                                    if (mSelXzqhdm.substr(0, varJb) == item.JGDM.substr(0, varJb)) {
                                        tmpSelVal = item.JGDM;
                                        selected = 'selected = "selected"';
                                    }
                                }
                                $('#' + varSelId).append('<option value="' + item.JGDM + '" ' + selected + ' >' + item.JGMC + '</option>');
                            }
                            selRetFunAll(varSelId, tmpSelVal);
                        }
                    }
                },
                error: function (xml) {
                    layer.close(index);
                    $('#' + varSelId + ' option').remove();
                    $('#' + varSelId).append('<option value="" >请选择</option>');
                }
            });
        } else {
            $('#' + varSelId + ' option').remove();
            $('#' + varSelId).append('<option value="" >请选择</option>');
            selRetFunAll(varSelId, '');
        }
    }

    function selRetFunAll(varSelId, varSelVal) {
        if (varSelId == 'xzqhsj') {
            if (varSelVal != '') {
                SetSelectData('xzqhfj', varSelVal, 6);
            } else {
                $('#xzqhfj option').remove();
                $('#xzqhfj').append('<option value="" >请选择</option>');
                form.render('select');
            }
        } else {
            form.render('select');
        }
    }

    form.verify({
        xllc: function (value, item) { //value：表单的值、item：表单的DOM对象
            if (isNaN(value)) {
                return '线路里程应为数字';
            }
        }
    });

    form.on('select(xzqhsj)', function (data) {
        SetSelectData('xzqhfj', data.value);
    });
    form.on('select(xzqhfj)', function (data) {
    });

    exports('dtxledit', {});
});
