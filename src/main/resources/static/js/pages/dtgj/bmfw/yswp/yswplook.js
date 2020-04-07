layui.config({
    base: '/js/core/winui/' //指定 winui 路径
}).extend({
    winui: 'winui',
    window: 'js/winui.window'
}).define(['jquery', 'winui','window'], function (exports) {
    var msg = winui.window.msg,
        $ = layui.$, 
        layer = layui.layer;
        var yswpid = $.getUrlParam("id");
    
    //多文件列表示例
    var fFileListView = $('#fileList');//文件列表
    //获取文件列表
    function getFileData() {
        $("#div_wpzp").hide();
        var index = layer.load(1);
        $.ajax({
            type: 'get',
            url: '/api/dtgj/fjxx/listbypid',
            headers: { token: localStorage["token"] },
            data: { pid: yswpid },
            dataType: 'json',
            success: function (data) {
                layer.close(index);
                if (data.code == "1") {
                    var items = data.data;
                    if(items.length>0) {
                        $("#div_wpzp").show();
                        fFileListView.find('tr.get-list').remove();
                        for (var i = 0; i < items.length; i++) {
                            var item = items[i];
                            trAddData(item);
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
    }
    function trAddData(item) {
        var tmpItemId = item.ID;
        var tr = $([
            '<tr id="upload-"' + tmpItemId + ' class="get-list">',
            '<td><img src="/dtgj/fjxx/filedownload?mc=' + item.NAME + '&dz=' + item.FJDZ + '"></td>',
            '<td>' + item.NAME + '</td>',
            '<td>' + (item.FJDX / 1024).toFixed(1) + 'kb</td>',
            '<td>',
            '<a target="_blank" class="layui-btn layui-btn-xs layui-btn-danger " href="/dtgj/fjxx/filedownload?mc=' + item.NAME + '&dz=' + item.FJDZ + '" >下载</a>',
            '</td>',
            '</tr>'].join(''));
        fFileListView.append(tr);
    }
    if(yswpid) {
        $.ajax({
            type: 'get',
            url: '/api/dtgj/yswp/getById',
            headers: {token: localStorage["token"]},
            data: {id: yswpid},
            dataType:'json',
            success: function (data) {
                if(data.code == "1"){
                    $("#yswpName").val(data.data.MC);
                    $("#yswpSqdd").val(data.data.SQDD);
                    $("#yswpSqsj").val(data.data.SQSJ);
                    $("#yswpWpms").val(data.data.MS);
                    if(data.data.ZT=="已认领") {
                        $("#div_rlr").show();
                        $("#div_rlzj").show();
                        $("#div_rlsj").show();
                        $("#yswpRlr").val(data.data.RLR);
                        $("#yswpRlrSfzh").val(data.data.RLRSFZH);
                        $("#yswpRlrsj").val(data.data.RLSJ);
                } else {
                        $("#div_rlr").hide();
                        $("#div_rlzj").hide();
                        $("#div_rlsj").hide();
                    }

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
        getFileData();
    }
    exports('yswplook', {});
});