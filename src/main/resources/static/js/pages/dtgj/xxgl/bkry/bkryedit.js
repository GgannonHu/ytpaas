var mUrlTop = "/api/dtgj/xxgl/bkry";
layui.config({
    base: '/js/core/winui/' //指定 winui 路径
}).extend({
    winui: 'winui',
    window: 'js/winui.window'
}).define(['table', 'jquery', 'winui', 'form', 'window', 'upload'], function (exports) {
    var msg = winui.window.msg;
    var form = layui.form;
    var $ = layui.$;
    var layer = layui.layer;
    var upload = layui.upload;

    var selId = $.getUrlParam("id");
    var pageType = $.getUrlParam("type");

    //修改页面是否只读样式
    if (pageType == 'sel') {
        $('#pageSubmit').hide();
        $(".layui-input").attr("disabled", "disabled");
        $(".layui-input").attr("placeholder", "");
    } else {
        $('#pageSubmit').show();
        //laydate.render({ elem: '#' });
    }

    //修改初始化
    if (selId) {
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
                    $("#name").val(item.NAME);
                    $("#idcard").val(item.IDCARD);
                    $("#bknr").val(item.BKNR);
                    $("#picture").val(item.PICTURE);

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

    //普通图片上传
    var uploadInst = upload.render({
        elem: '#test1'
        , auto: false
        , choose: function (obj) {
            //预读本地文件示例，不支持ie8
            obj.preview(function (index, file, result) {
                $('#demo1').attr('src', result); //图片链接（base64）
            });
        }
    });


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
            top.winui.window.close('editBkry');
        }, 1000);
    }
    /*
    form.verify({
        xllc: function (value, item) { //value：表单的值、item：表单的DOM对象
            if (isNaN(value)) {
                return '线路里程应为数字';
            }
            if (value.length > 6) {
                return '线路里程不能超过六位数字';
            }
        } 
    });
    */

    exports('bkryedit', {});
});
