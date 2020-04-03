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
        var isUpd = false;
        var gjgwcyryid = $.getUrlParam("id");
    var fOkMsg = '';
    
    /**
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
                form.render('select');

                form.on('select(' + id + ')', function (data) {
                    // alert(data.value); //得到被选中的值
                });
            }
        });
    }
    if(gjgwcyryid) {
        $.ajax({
            type: 'get',
            url: '/api/dtgj/gjgwcyry/getById',
            headers: {token: localStorage["token"]},
            data: {id: gjgwcyryid},
            dataType:'json',
            success: function (data) {
                if(data.code == "1"){
                    $("#gjgwcyryName").val(data.data.GJGWCYRY_XM);
                    $("#gjgwcyryIdcard").val(data.data.GJGWCYRY_GMSFZH);
                    $("#gjgwcyryDwmc").val(data.data.GJGWCYRY_DWMC);
                    $("#gjgwcyryQybm").val(data.data.GJGWCYRY_QYBM);
                    $("#gjgwcyryGwmc").val(data.data.GJGWCYRY_GWMC);
                    $("#gjgwcyryLxdh").val(data.data.GJGWCYRY_LXDH);
                    $("#gjgwcyryDzmc").val(data.data.GJGWCYRY_DZMC);
                    $("#gjgwcyryWffz").val(data.data.GJGWCYRY_WFFZJLMS);

                    bindcon("MZ", "gjgwcyryMz", data.data.GJGWCYRY_MZDM);
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
    } else {
        bindcon("MZ", "gjgwcyryMz", "");
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
                    $("#hdfDwdm").val(d.DWDM);
                    $("#hdfDwmc").val(d.DWMC);
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