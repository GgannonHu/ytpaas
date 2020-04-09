layui.config({
    base: '/js/core/winui/' //指定 winui 路径
}).extend({
    winui: 'winui',
    window: 'js/winui.window'
}).define(['jquery','form', 'winui','window'], function (exports) {
    var msg = winui.window.msg,
        $ = layui.$, 
        form = layui.form,
        layer = layui.layer;
    var ctczid = $.getUrlParam("id");
    //多文件列表示例
    var fFileListView = $('#fileList');//文件列表
    var ffkListView = $('#fkList');//反馈列表
    //获取文件列表
    function getFileData() {
        var index = layer.load(1);
        $.ajax({
            type: 'get',
            url: '/api/dtgj/fjxx/listbypid',
            headers: { token: localStorage["token"] },
            data: { pid: ctczid },
            dataType: 'json',
            success: function (data) {
                layer.close(index);
                if (data.code == "1") {
                    var items = data.data;
                    fFileListView.find('tr.get-list').remove();
                    for (var i = 0; i < items.length; i++) {
                        var item = items[i];
                        trAddData(item);
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
    function trAddData(item) {
        var tmpItemId = item.ID;
        var tr = $([
            '<tr id="upload-"' + tmpItemId + ' class="get-list">',
            '<td>' + item.NAME + '</td>',
            '<td>' + (item.FJDX / 1024).toFixed(1) + 'kb</td>',
            '<td>',
            // '<a target="_blank" class="layui-btn layui-btn-xs layui-btn-danger " href="/dtgj/fjxx/filedownload?mc=' + item.NAME + '&dz=' + item.FJDZ + '" >下载</a>',
            '<a target="_blank" class="layui-btn layui-btn-normal layui-btn-xs" title="下载" href="/dtgj/fjxx/filedownload?mc=' + item.NAME + '&dz=' + item.FJDZ + '" ><i class="fa fa-download"></i></a>',
            '</td>',
            '</tr>'].join(''));
        fFileListView.append(tr);
    }
    function trFkAddData(item) {
        var tr = $([
            '<tr class="get-list">',
            '<td>' + item.FKDWMC + '</td>',
            '<td>' + item.FKSJ + '</td>',
            '<td>' + item.FKNR + '</td>',
            '</tr>'].join(''));
        ffkListView.append(tr);
    }
    if(ctczid) {
        $.ajax({
            type: 'get',
            url: '/api/dtgj/ctcz/getById',
            headers: {token: localStorage["token"]},
            data: {id: ctczid},
            dataType:'json',
            success: function (data) {
                if(data.code == "1"){
                    $("#ctczName").val(data.data.NAME);
                    var html = '';
                    html += '<div style="border:1px solid #F0F0F0;float:left; margin:5px">';
                    html += '<label  style="padding:0px 5px">' + data.data.NR + '</label>';
                    html += '</div>';
                    $("#div_nr").append(html);
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
        $.ajax({
            type: 'get',
            url: '/api/dtgj/ctcz/getFkById',
            headers: {token: localStorage["token"]},
            data: {id: ctczid},
            dataType:'json',
            success: function (data) {
                if(data.code == "1"){
                    if (data.data.length > 0) {
                        $("#div_fk").show();
                        for (var i = 0; i < data.data.length; i++) {
                            trFkAddData(data.data[i]);
                        }
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
        getFileData();
    }
    
    function getuser() {
        $.ajax({
            url: "/api/home/getUserByToken",
            method: 'GET',
            data: { 'token': localStorage["token"] },
            dataType: "JSON",
            success: function (d) {
                if (d && d.LOGINNAME) {
                    $("#hdfDwbmMc").val(d.DWMC);
                    $("#hdfDwbm").val(d.DWDM);
                    $("#hdfUser").val(d.LOGINNAME);
                }
            },
            error: function () {
                location.href = "/error";
            }
        });
    }
    form.on('submit(formFkMenu)', function (data) {
        if (winui.verifyForm(data.elem)) {
            if($("#ctczFk").val()!="") {
                var index = layer.load(1);
                var url = "/api/dtgj/ctcz/fk";
                if(ctczid) {
                    data.field.id = ctczid;
                }
                layui.$.ajax({
                    type: 'post',
                    url: url,
                    data: data.field,
                    dataType: 'json',
                    headers: {token: localStorage["token"]},
                    success: function (json) {
                        layer.close(index);
                        fOkMsg = json.msg;
                        if (json.code == "1") {
                            msg(fOkMsg, {
                                icon: 1,
                                time: 1000
                            });
                            var topWindow = top.winui.window.getWindow($.getUrlParam("menuid"));
                            $(topWindow).find("#reloadTable").trigger("click");
                            setTimeout(() => {
                                top.winui.window.close('fkCtcz');
                            }, 1000);
                        } else {
                            msg(fOkMsg);
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
            else {
                msg("请填写反馈内容", {
                    icon: 2,
                    time: 1000
                });
            }
        }
        return false;
    });
    getuser();
    exports('ctczfk', {});
});