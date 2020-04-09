var jsdw="";
var jsdwMc="";
layui.config({
    base: '/js/core/winui/' //指定 winui 路径
}).extend({
    winui: 'winui',
    window: 'js/winui.window'
}).define(['table', 'jquery', 'winui','form','window', 'tree', 'upload'], function (exports) {
    var msg = winui.window.msg,
        form = layui.form,
        $ = layui.$, 
        layer = layui.layer;
    var upload = layui.upload;
    var isUpd = false;
    var ctczid = $.getUrlParam("id");
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
        , url: '/api/dtgj/fjxx/upload' //改成自己的上传接口
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
                        // '<a class="layui-btn layui-btn-xs file-reload layui-hide">重传</a>',
                        '<a class="layui-btn layui-btn-xs file-reload layui-hide" title="重传" ><i class="fa fa-upload"></i></a>',
                        // '<a class="layui-btn layui-btn-xs layui-btn-danger file-delete">删除</a>',
                        '<a class="layui-btn layui-btn-danger layui-btn-xs file-delete" title="删除"><i class="fa fa-trash-o"></i></a>',
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
                this.data = { pid: ctczid, urltop: 'DTGJ/CTCZ/' }
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
            '<td style="color: #5FB878;">已上传</td>',
            '<td>',
            // '<a class="layui-btn layui-btn-xs layui-btn-danger file-delete" >删除</a>',
            '<a class="layui-btn layui-btn-danger layui-btn-xs file-delete" title="删除"><i class="fa fa-trash-o"></i></a>',
            // '<a target="_blank" class="layui-btn layui-btn-xs layui-btn-danger " href="/dtgj/fjxx/filedownload?mc=' + item.NAME + '&dz=' + item.FJDZ + '" >下载</a>',
            '<a target="_blank" class="layui-btn layui-btn-normal layui-btn-xs" title="下载" href="/dtgj/fjxx/filedownload?mc=' + item.NAME + '&dz=' + item.FJDZ + '" ><i class="fa fa-download"></i></a>',
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
    //绑定树
    var treeData = [];
    function initTree() {
    	$.ajax({
    		url: '/api/dtgj/com/jg',
            type: 'get',
            dataType: 'json',
            headers: { token: localStorage["token"] },
            data: { pid: $("#hdfDwbm").val() },
            success: function (data) {
                treeData = data.data;
    			bindTree();
    		}
    	});
    }
    function bindTree() {
    	layui.tree({
    		elem: '#menu' //传入元素选择器
    		,onlyIconControl: true  //是否仅允许节点左侧图标控制展开收缩
    		,click: function(obj){
    			treeClick(obj);
    		}
      	 	,nodes: treeData
      	});
    }
    function treeClick(obj) {
        //.put("JGDM", item.get("JGDM"));
        //.put("XZQH", item.get("XZQH"));
        //.put("JGMC", item.get("JGMC"));
        //.put("SJDM", item.get("SJDM"));
        //.put("JGJB", item.get("JGJB"));
        //.put("JGLBDM", item.get("JGLBDM"));
        //.put("JGLBMC", item.get("JGLBMC"));
        //.put("name", item.get("JGMC"));
        if ($("#hdfDwbm").val() != obj.JGDM) {
            var html = '';
            html += '<div id="bqId' + obj.JGDM + '" style="border:1px solid #F0F0F0;float:left; margin:5px">';
            html += '<label  style="padding:0px 5px">' + obj.JGMC + '</label>';
            html += '<a class="layui-btn layui-btn-primary layui-btn-xs" style="background-color: #F0F0F0" href="javascript:removeYx(\'' + obj.JGDM + '\',\'' + obj.JGMC + '\');">X</a>';
            html += '</div>';
            var patt1 = new RegExp(obj.JGDM);
            var result = patt1.test(jsdw);
            if (!result) {
                if (jsdw != "") {
                    jsdw += ",";
                    jsdwMc += ",";
                }
                jsdw += obj.JGDM;
                jsdwMc += obj.JGMC;
                $("#hdfJsdw").val(jsdw);
                $("#hdfJsdwMc").val(jsdwMc);
                $("#div_jsdw").append(html);
            }
        }
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
                    $("#ctczNr").val(data.data.NR);
                    jsdw = data.data.XFDW;
                    jsdwMc = data.data.XFDWMC;
                    if (jsdw != null) {
                        $("#hdfJsdw").val(data.data.XFDW);
                        $("#hdfJsdwMc").val(data.data.XFDWMC);
                        var strJsdw = new Array(); //定义一数组
                        strJsdw = jsdw.split(","); //字符分割
                        var strJsdwMc = new Array(); //定义一数组
                        strJsdwMc = jsdwMc.split(","); //字符分割
                        for (i = 0; i < strJsdw.length; i++) {
                            var html = '';
                            html += '<div id="bqId' + strJsdw[i] + '" style="border:1px solid #F0F0F0;float:left; margin:5px">';
                            html += '<label  style="padding:0px 5px">' + strJsdwMc[i] + '</label>';
                            html += '<a class="layui-btn layui-btn-primary layui-btn-xs" style="background-color: #F0F0F0" href="javascript:removeYx(\'' + strJsdw[i] + '\',\'' + strJsdwMc[i] + '\');">X</a>';
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
                    initTree();
                }
            },
            error: function () {
                location.href = "/error";
            }
        });
    }
    form.on('submit(formEditMenu)', function (data) {
        if (winui.verifyForm(data.elem)) {
            if($("#hdfJsdwMc").val()!="") {
                var index = layer.load(1);
                var url = "/api/dtgj/ctcz/add";
                if(ctczid) {
                    url = "/api/dtgj/ctcz/update";
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
                        if (json.code == "1") {
                            fOkMsg = json.msg;
                            ctczid = json.data;
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
            else {
                msg("请选择接收单位", {
                    icon: 2,
                    time: 1000
                });
            }
        }
        return false;
    });
    //删除文件
    function delfiles() {
        var index = layer.load(1);
        layui.$.ajax({
            type: 'post',
            url: '/api/dtgj/fjxx/deletebyid',
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
        if (isUpd) {
            $(topWindow).find("#reloadTable").trigger("click");
        }
        else {
            $(topWindow).find("#searchMenu").trigger("click");
        }
        setTimeout(() => {
            top.winui.window.close('editCtcz');
        }, 1000);
    }
    getuser();
    exports('ctczedit', {});
});
function removeYx(id,mc) {
    var box = document.getElementById('bqId' + id);
    box.parentNode.removeChild(box);
    var patt1 = new RegExp(id);
    var result = patt1.test(jsdw);
    if (result) {
        if(jsdw.split(',').length>1) {
            var reg = new RegExp(","+id,"g");//g,表示全部替换。
            var reg1 = new RegExp(id+",","g");
            jsdw=jsdw.replace(reg,"").replace(reg1,"");
            reg = new RegExp(","+mc,"g");
            reg1 = new RegExp(mc+",","g");
            jsdwMc=jsdwMc.replace(reg,"").replace(reg1,"");
        }
        else {
            jsdw=jsdw.replace(id,"");
            jsdwMc=jsdwMc.replace(mc,"");
        }
    }
    $("#hdfJsdw").val(jsdw);
    $("#hdfJsdwMc").val(jsdwMc);
}