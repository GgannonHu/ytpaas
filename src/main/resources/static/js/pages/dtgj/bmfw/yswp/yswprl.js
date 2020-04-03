layui.config({
    base: '/js/core/winui/' //指定 winui 路径
}).extend({
    winui: 'winui',
    window: 'js/winui.window'
}).define(['table', 'laydate', 'jquery', 'winui','form','window'], function (exports) {
    var msg = winui.window.msg,
        form = layui.form,
        $ = layui.$, 
        layer = layui.layer;
    var yswpid = $.getUrlParam("id");
    
    //多文件列表示例
    var fFileListView = $('#fileList');//文件列表
    var fFileNames = [];//判断重复文件名

    //获取文件列表
    function getFileData() {
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
            '<td><img src="/dtgj/fjxx/filedownload?mc=' + item.NAME + '&dz=' + item.FJDZ + '"></td>',
            '<td>' + item.NAME + '</td>',
            '<td>' + (item.FJDX / 1024).toFixed(1) + 'kb</td>',
            '<td>',
            '<a target="_blank" class="layui-btn layui-btn-xs layui-btn-danger " href="/dtgj/fjxx/filedownload?mc=' + item.NAME + '&dz=' + item.FJDZ + '" >下载</a>',
            '</td>',
            '</tr>'].join(''));
        fFileNames.push(item.NAME);
        var tmpFmIndex = (fFileNames.length - 1);
        tr.find('.file-delete').on('click', function () {
            fDelFileId += (tmpItemId + ',');
            delete fFileNames[tmpFmIndex];
            tr.remove();
        });
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
                    $("#yswpWpms").val(data.data.MS);
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
    form.on('submit(formEditMenu)', function (data) {
        if (winui.verifyForm(data.elem)) {
            var index = layer.load(1);
            var url = "/api/dtgj/yswp/rl";
            if(yswpid) {
                data.field.id = yswpid;
            }
            layui.$.ajax({
                type: 'post',
                url: url,
                data: data.field,
                dataType: 'json',
                headers: {token: localStorage["token"]},
                success: function (json) {
                    layer.close(index);
                    try{
                        if (json.code == "1") {
                            msg(json.msg, {
                                icon: 1,
                                time: 1000
                            });
                            var topWindow = top.winui.window.getWindow($.getUrlParam("menuid"));
                            $(topWindow).find("#reloadTable").trigger("click");
                            setTimeout(() => {
                                top.winui.window.close('rlYswp');
                            }, 1000);
                        } else {
                            msg(json.msg);
                        }
                    }catch(e){
                        msg("操作失败", {
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
    exports('yswprl', {});
});