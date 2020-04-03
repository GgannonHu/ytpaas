var jsdw="";
var jsdwMc="";
layui.config({
    base: '/js/core/winui/' //指定 winui 路径
}).extend({
    winui: 'winui',
    window: 'js/winui.window'
}).define(['jquery', 'winui','form','window', 'tree'], function (exports) {
    var msg = winui.window.msg,
        form = layui.form,
        $ = layui.$, 
        layer = layui.layer;
    var ctczid = $.getUrlParam("id");
    //多文件列表示例
    var fFileListView = $('#fileList');//文件列表
    var fOkMsg = '';
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
    //绑定树
    var treeData = [];
    function initTree() {
        var index = layer.load(1);
    	$.ajax({
    		url: '/api/dtgj/com/jg',
            type: 'get',
            dataType: 'json',
            headers: { token: localStorage["token"] },
            data: { pid: $("#hdfDwbm").val() },
            success: function (data) {
                layer.close(index);
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
                    $("#hdfDwbm").val(d.DWDM);
                    $("#hdfDwbmMc").val(d.DWMC);
                    $("#hdfUser").val(d.LOGINNAME);
                    initTree();
                }
            },
            error: function () {
                location.href = "/error";
            }
        });
    }
    form.on('submit(formXFMenu)', function (data) {
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
            else {
                msg("请选择接收单位", {
                    icon: 2,
                    time: 1000
                });
            }
        }
        return false;
    });
    function closeThis() {
        msg(fOkMsg, {
            icon: 1,
            time: 1000
        });
        var topWindow = top.winui.window.getWindow($.getUrlParam("menuid"));
        $(topWindow).find("#reloadTable").trigger("click");
        setTimeout(() => {
            top.winui.window.close('xfCtcz');
        }, 1000);
    }
    getuser();
    exports('ctczxf', {});
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