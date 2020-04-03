layui.config({
    base: '/js/core/winui/' //指定 winui 路径
}).extend({
    winui: 'winui',
    window: 'js/winui.window'
}).define(['jquery', 'winui','form','window'], function (exports) {
    var msg = winui.window.msg,
        form = layui.form,
        $ = layui.$, 
        layer = layui.layer;
    var ctczid = $.getUrlParam("id");
    //多文件列表示例
    var fFileListView = $('#fileList');//文件列表
    var fqsListView = $('#qsList');//签收列表
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
            '<a target="_blank" class="layui-btn layui-btn-xs layui-btn-danger " href="/dtgj/fjxx/filedownload?mc=' + item.NAME + '&dz=' + item.FJDZ + '" >下载</a>',
            '</td>',
            '</tr>'].join(''));
        fFileListView.append(tr);
    }
    function trQsAddData(item) {
        var tr = $([
            '<tr class="get-list">',
            '<td>' + item.QSDWMC + '</td>',
            '<td>' + item.QSSJ + '</td>',
            '</td>',
            '</tr>'].join(''));
        fqsListView.append(tr);
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
                    if (data.data.XFDWMC) {
                        var jsdws = data.data.XFDWMC.split(',');
                        for (i = 0; i < jsdws.length; i++) {
                            html = '';
                            html += '<div style="border:1px solid #F0F0F0;float:left; margin:5px">';
                            html += '<label  style="padding:0px 5px">' + jsdws[i] + '</label>';
                            html += '</div>';
                            $("#div_jsdw").append(html);
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
        $.ajax({
            type: 'get',
            url: '/api/dtgj/ctcz/getQsById',
            headers: {token: localStorage["token"]},
            data: {id: ctczid},
            dataType:'json',
            success: function (data) {
                if (data.code == "1") {
                    if (data.data.length > 0) {
                        $("#div_qs").show();
                        for (var i = 0; i < data.data.length; i++) {
                            trQsAddData(data.data[i]);
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
    exports('ctczlook', {});
});