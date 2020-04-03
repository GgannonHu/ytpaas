var mUrlTop = "/api/dtgj/zhld/yjct";
var mFjxxUrlTop = "/api/dtgj/fjxx";
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

    var yjctid = $.getUrlParam("id");
    var pageType = $.getUrlParam("type");;

    if (pageType == 'sel') {
        $('#uploadList').hide();
        $('#pageSubmit').hide();
        $('#fThZt').hide();
        $("#name").attr("readonly", "readonly");
        $("#nr").attr("readonly", "readonly");
    } else {
        $('#uploadList').show();
        $('#pageSubmit').show();
        $('#fThZt').show();
    }

    //多文件列表示例
    var fFileListView = $('#fileList');//文件列表
    var fFileNames = [];//判断重复文件名
    var fUploadCon = 0;//上传数量
    var fUploadLoadIndex;//加载提示Id
    var fDelFileId = '';
    var fOkMsg = '';
    var fFunCon = 0;
    var fUploadListIns = upload.render({
        elem: '#uploadList'
        , url: mFjxxUrlTop + '/upload' //改成您自己的上传接口
        , accept: 'file'
        , multiple: true
        , size: 9216
        , auto: false
        , bindAction: '#uploadListAction'
        , choose: function (obj) {
            var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列

            //读取本地文件
            obj.preview(function (index, file, result) {
                if (fFileNames.indexOf(file.name) < 0) {
                    fUploadCon++;

                    var tr = $([
                        '<tr id="upload-' + index + '">',
                        '<td>' + file.name + '</td>',
                        '<td>' + (file.size / 1024).toFixed(1) + 'kb</td>',
                        '<td>等待上传</td>',
                        '<td>',
                        '<a class="layui-btn layui-btn-xs file-reload layui-hide">重传</a>',
                        '<a class="layui-btn layui-btn-xs layui-btn-danger file-delete">删除</a>',
                        '</td>',
                        '</tr>'].join(''));

                    //重传
                    tr.find('.file-reload').on('click', function () {
                        obj.upload(index, file);
                    });

                    //删除
                    fFileNames.push(file.name);
                    var tmpFmIndex = (fFileNames.length - 1);
                    tr.find('.file-delete').on('click', function () {
                        delete files[index];
                        delete fFileNames[tmpFmIndex];
                        fUploadCon--;
                        tr.remove();
                        fUploadListIns.config.elem.next()[0].value = '';
                    });
                    fFileListView.append(tr);
                } else {
                    delete files[index];
                    fUploadListIns.config.elem.next()[0].value = '';
                    msg("添加失败,已存在相同文件!", {
                        icon: 2,
                        time: 1000
                    });
                }
            });
        }
        , before: function (obj) {
            if (fUploadCon > 0) {
                this.data = { pid: yjctid, urltop: 'DTGJ/YJCT/' }
                fUploadLoadIndex = layer.load(1);
            } else {
                closeThis();
            }
        }
        , done: function (res, index, upload) {
            if (res.code == '1') { //上传成功
                var tr = fFileListView.find('tr#upload-' + index)
                var tds = tr.children();
                tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
                tds.eq(3).html(''); //清空操作
                tr.remove();
                return delete this.files[index]; //删除文件队列已经上传成功的文件
            } else {
                this.error(index, upload);
            }
        }
        , allDone: function (obj) {
            //得到总文件数 obj.total 请求成功的文件数 obj.successful 请求失败的文件数 obj.aborted
            fUploadCon = obj.aborted;
            layer.close(fUploadLoadIndex);
            if (fUploadCon == 0) {
                fFunCon++;
                if (fFunCon >= 2) {
                    closeThis();
                }
            } else {
                getFileData();
                msg('存在上传错误的附件,请点击重传!', {
                    icon: 2,
                    time: 1000
                });
            }
        }
        , error: function (index, upload) {
            var tr = fFileListView.find('tr#upload-' + index)
            var tds = tr.children();
            tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
            tds.eq(3).find('.file-reload').removeClass('layui-hide'); //显示重传
            fFunCon--;
        }
    });

    //获取文件列表
    function getFileData() {
        var index = layer.load(1);
        $.ajax({
            type: 'get',
            url: mFjxxUrlTop + '/listbypid',
            headers: { token: localStorage["token"] },
            data: { pid: yjctid },
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
            pageType != 'sel' ? '<td style="color: #5FB878;">已上传</td>' : '',
            '<td>',
            pageType != 'sel' ? '<a class="layui-btn layui-btn-xs layui-btn-danger file-delete" >删除</a>' : '',
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
    if (yjctid) {
        var index = layer.load(1);
        $.ajax({
            type: 'get',
            url: mUrlTop + '/getitembyid',
            headers: { token: localStorage["token"] },
            data: { id: yjctid },
            dataType: 'json',
            success: function (data) {
                layer.close(index);
                if (data.code == "1") {
                    $("#name").val(data.data.NAME);
                    $("#nr").val(data.data.NR);
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

    form.on('submit(formEditMenu)', function (data) {
        if (winui.verifyForm(data.elem)) {
            var index = layer.load(1);

            var url = mUrlTop + "/add";
            if (yjctid) {
                url = mUrlTop + "/update";
                data.field.id = yjctid;
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
                        yjctid = json.data;
                        fFunCon = 0;
                        if (fUploadCon > 0) {
                            $("#uploadListAction").trigger("click");
                        } else {
                            fFunCon++;
                        }
                        if (fDelFileId.length > 0) {
                            delfiles();
                        } else {
                            fFunCon++;
                        }
                        if (fFunCon >= 2) {
                            closeThis();
                        }
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

    //删除文件
    function delfiles() {
        var index = layer.load(1);
        layui.$.ajax({
            type: 'post',
            url: mFjxxUrlTop + '/deletebyid',
            data: { ids: fDelFileId },
            dataType: 'json',
            headers: { token: localStorage["token"] },
            success: function (json) {
                layer.close(index);
                if (json.code == "1") {
                    fFunCon++;
                    if (fFunCon >= 2) {
                        closeThis();
                    }
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

    function closeThis() {

        msg(fOkMsg, {
            icon: 1,
            time: 1000
        });

        var topWindow = top.winui.window.getWindow($.getUrlParam("menuid"));

        if (pageType == 'upd') {
            $(topWindow).find("#reloadTable").trigger("click");
        }
        else {
            $(topWindow).find("#selyjct").trigger("click");
        }
        setTimeout(() => {
            top.winui.window.close('editYjct');
        }, 1000);
    }

    exports('yjctedit', {});
});
