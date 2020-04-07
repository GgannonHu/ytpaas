var mLOGINNAME = '';
var TJR= '';
layui.config({
    base: '/js/core/winui/' //指定 winui 路径
}).extend({
    winui: 'winui',
    window: 'js/winui.window'
}).define(['table', 'laydate', 'jquery', 'winui','form','window', 'upload'], function (exports) {
    var msg = winui.window.msg,
        form = layui.form,
        $ = layui.$, 
        layer = layui.layer,
        laydate = layui.laydate;
        var upload = layui.upload;
        var isUpd = false;
        var Asjxxid = $.getUrlParam("id");
    //时间
    laydate.render({
        elem: '#asjxx_ASJFSKSSJ'
        ,type: 'datetime'
    });
    
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
        , accept: 'images'//file
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
                        '<td><img src="' + result + '"></td>',
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
                this.data = { pid: Asjxxid, urltop: 'DTGJ/YSWP/' }
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
    function bindzdmc(bm, selected) {
        $.ajax({
            url: '/api/dtgj/xxgl/asjxx/zdxx',
            headers: { token: localStorage["token"] },
            data: {
                bm: bm
            },
            dataType: "JSON",
            success: function (data) {
                var str = "<option value=''>请选择</option>";
                $.each(data.data, function () {
                    str += "<option value='" + this.BM + "'>" + this.MC + "</option>";
                });
                $("#asjxx_DTZDMC").html(str);
                $("#asjxx_DTZDMC").val(selected);
                form.render('select');

                form.on('select(asjxx_DTZDMC)', function (data) {
                    // alert(data.value); //得到被选中的值
                });
            }
        });
    }
    function bindxlxx(xzqh) {
        $.ajax({
            url: '/api/dtgj/xxgl/asjxx/xlxx',
            headers: { token: localStorage["token"] },
            data: {
                xzqh: xzqh
            },
            dataType: "JSON",
            success: function (data) {
                 
                var str = "<option value=''>请选择</option>";
                $.each(data.data, function () {
                    str += "<option value='" + this.BM + "'>" + this.MC + "</option>";
                });
                $("#asjxx_GJXLBM").html(str);
                loadData();

                form.render('select');
                form.on('select(asjxx_GJXLBM)', function (data) {
                    // alert(data.value); //得到被选中的值
                    bindzdmc(data.value, "");
                });
            }
        });
    }
    //获取文件列表
    function getFileData() {
        var index = layer.load(1);
        $.ajax({
            type: 'get',
            url: '/api/dtgj/fjxx/listbypid',
            headers: { token: localStorage["token"] },
            data: { pid: Asjxxid },
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
    function loadData() {

        if(Asjxxid) {
            $.ajax({
                type: 'get',
                url: '/api/dtgj/xxgl/asjxx/getById',
                headers: {token: localStorage["token"]},
                data: {id: Asjxxid},
                dataType:'json',
                success: function (data) { 
                    if(data.code == "1"){  
                        bindzdmc(data.data.GJXLBM, data.data.DTZDBM);
                        $("#asjxx_GAJGJGDM").val(data.data.GAJGJGDM);//  gajgjgdm    公安机关机构代码
                        $("#asjxx_DTZDBM").val(data.data.DTZDBM);//  dtzdbm      地铁站点编码 
                        $("#asjxx_DTZDMC").val(data.data.DTZDMC);//  dtzdmc      地铁站点名称
                        $("#asjxx_GJXLBM").val(data.data.GJXLBM);//  gjxlbm      公交线路编码
                        $("#asjxx_GJGSMC").val(data.data.GJGSMC);//  gjgsmc      公交公司名称
                        $("#asjxx_AJBH").val(data.data.AJBH);//  ajbh        案件编号
                        $("#asjxx_AJLB").val(data.data.AJLB);//  ajlb        案件类别
                        $("#asjxx_AJMC").val(data.data.AJMC);//  ajmc        案件名称
                        $("#asjxx_JYAQ").val(data.data.JYAQ);//  jyaq        简要案情
                        $("#asjxx_ASJFSKSSJ").val(data.data.ASJFSKSSJ);//  asjfskssj   案事件发生开始时间
                        $("#asjxx_AFDD").val(data.data.AFDD);//  afdd        案发地点
                        $("#asjxx_SFCS").val(data.data.SFCS);//  sfcs        事发场所11
                        $("#asjxx_SSXQ").val(data.data.SSXQ);//  ssxq        所属辖区 
                        TJR = data.data.TJR;
                        if(TJR!=mLOGINNAME)
                        {
                            $("#btn_queding").attr("style","display:none;"); 
                            $("#btn_queding").attr("style","display:none;"); 
                            $(".layui-input").attr("disabled", "disabled");
                            $(".layui-input").attr("placeholder", "");
                            // $("#form select").attr('disabled','disabled');
                            $("select").attr("disabled", "disabled");
                            form.render('select'); 
                        }
                        form.render('select'); 
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
             
        }
    }
    function trAddData(item) {
        var tmpItemId = item.ID;
        var tr = $([
            '<tr id="upload-"' + tmpItemId + ' class="get-list">',
            '<td><img src="/dtgj/fjxx/filedownload?mc=' + item.NAME + '&dz=' + item.FJDZ + '"></td>',
            '<td>' + item.NAME + '</td>',
            '<td>' + (item.FJDX / 1024).toFixed(1) + 'kb</td>',
            '<td style="color: #5FB878;">已上传</td>',
            '<td>',
            '<a class="layui-btn layui-btn-xs layui-btn-danger file-delete" >删除</a>',
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
    
    function getuser() {
         
        $.ajax({
            url: "/api/home/getUserByToken",
            method: 'GET',
            data: { 'token': localStorage["token"] },
            dataType: "JSON",
            success: function (d) { 
                if (d && d.LOGINNAME) {
                    mLOGINNAME = d.LOGINNAME;
                    dwdmsj = d.DWDM.substring(0, 4);
                    dwdmfj = d.DWDM.substring(0, 6);
                    jsbm = d.JSBM;
                    // bindcon("ZZMM", "zzmm");
                    // bindcon("MZ", "mz"); 
                    if (dwdmfj == "230000") {
                        bindxlxx("23");
                    } else if (dwdmfj.substring(4, 6) == "00") {
                        bindxlxx(dwdmsj);
                    } else {
                        bindxlxx(dwdmfj);
                    }
                }
                // if (d && d.LOGINNAME) {
                //     $("#hdfUser").val(d.LOGINNAME);
                // }
            },
            error: function () {
                location.href = "/error";
            }
        });
    }
    form.on('submit(formEditMenu)', function (data) {
        if (winui.verifyForm(data.elem)) {
            var index = layer.load(1);
            var url = "/api/dtgj/xxgl/asjxx/add";
            if(Asjxxid) {
                url = "/api/dtgj/xxgl/asjxx/update";
                data.field.id = Asjxxid;
            }
            layui.$.ajax({
                type: 'post',
                url: url,
                data: //da.field,
                
                {
                    asjxx_DTZDBM: $("#asjxx_DTZDMC").val(),
                    asjxx_DTZDMC: $("#asjxx_DTZDMC").find("option:selected").text(),
                    asjxx_GJXLBM: $("#asjxx_GJXLBM").val(),                        
                    asjxx_GJGSMC: $("#asjxx_GJGSMC").val(),
                    asjxx_AJBH: $("#asjxx_AJBH").val(),
                    asjxx_AJLB: $("#asjxx_AJLB").val(),
                    asjxx_AJMC: $("#asjxx_AJMC").val(),
                    asjxx_JYAQ: $("#asjxx_JYAQ").val(),
                    asjxx_ASJFSKSSJ: $("#asjxx_ASJFSKSSJ").val(),
                    asjxx_AFDD: $("#asjxx_AFDD").val(),
                    asjxx_SFCS: $("#asjxx_SFCS").val(),
                    asjxx_SSXQ: $("#asjxx_SSXQ").val(),
                    id: Asjxxid
                },
                dataType: 'json',
                headers: {token: localStorage["token"]},
                success: function (json) {
                    layer.close(index);
                    if (json.code == "1") {
                        fOkMsg = json.msg;
                        yjctid = json.data;
                        fFunCon = 0;
                        // if (fUploadCon > 0) {
                        //     $("#uploadListAction").trigger("click");
                        // } else {
                        //     fFunCon++;
                        // }
                        // alert(fFunCon);
                        // if (fDelFileId.length > 0) {
                        //     delfiles();
                        // } else {
                        //     fFunCon++;
                        // }
                        // if (fFunCon >= 2) {
                            closeThis();
                        // }
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
            top.winui.window.close('editYswp');
        }, 1000);
    }
    getuser();
    exports('yswpedit', {});
});