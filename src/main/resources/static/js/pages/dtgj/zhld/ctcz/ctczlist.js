var mDataCon = 0;
var mCurr = 1;
var mUrl = "/api/dtgj/ctcz";
var mSelData = { count: 0, iscon: 1 }

layui.config({
    base: '/js/core/winui/' //指定 winui 路径
    , version: '1.0.0-beta'
}).define(['table', 'form', 'laydate', 'jquery', 'winui'], function (exports) {
    winui.renderColor();
    var table = layui.table,
        form = layui.form,
        $ = layui.$,
        tableId = 'tableid',
        laydate = layui.laydate;
    //日期
    laydate.render({
        elem: '#txt_fbsjS'
    });
    laydate.render({
        elem: '#txt_fbsjE'
    });
    form.on('submit', function (data) {
        return false;
    });
    // form.on('select(ddl_qs)', function (data) {
    //     alert(data.value);
    //     if (data.value == "0") {
    //         $("#ddl_fk").show();
    //     } else {
    //         $("#ddl_fk").hide();
    //     }
    // });
    //表格渲染
    function loadData() {
        var index = layer.load(1);
        table.render({
            id: tableId,
            elem: '#list',
            url: mUrl + "/list",
            headers: { token: localStorage["token"] },
            where: mSelData,
            height: 'full-141', //自适应高度
            page: true,
            limits: [5, 10, 15, 20, 30, 40, 50, 100],
            limit: 5,
            done: function (res, curr, count) {
                $('#topcheck').prop('checked', false);
                form.render('checkbox');
                layer.close(index);
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                mDataCon = res.data.length;
                mCurr = curr;
                if (mSelData.iscon == 1) {
                    mSelData.count = count;
                    mSelData.iscon = 0;
                    this.where = mSelData;
                }
                if (mDataCon == 0 && count > 0 && mCurr > 1) {
                    mCurr--;
                    reloadTableAll();
                }
            },
            cols: [[
                // { field: 'ID', type: 'checkbox',width: '5%' },
                { field: 'ID', width: '6%', title: '<input id="topcheck" type="checkbox" lay-skin="primary" >', toolbar: '#barSelRow' },
                { field: 'NAME', title: '名称', width: '14%' },
                { field: 'TYPE', title: '类型', width: '10%', templet: '#tempLx' },
                { field: 'FBSJ', title: '发布时间', width: '15%' },
                { field: 'XFDWMC', title: '接收单位', width: '20%' },
                { field: 'ZT', title: '状态', width: '20%', templet: '#tempZt' },
                { title: '操作', fixed: 'right', align: 'left', toolbar: '#barctcz', width: '15%' }
            ]]
        });
        form.on('checkbox', function (data) {
            var id = data.elem.id;
            var checked = data.elem.checked;
            if (id == 'topcheck') {
                $("input[rid='rowCheck']").prop('checked', checked);
                form.render('checkbox');
            }
        });
        //监听工具条
        table.on('tool(list)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值
            var tr = obj.tr; //获得当前行 tr 的DOM对象
            var ids = '';   //选中的Id
            if (layEvent === 'del') { //删除
                deleteItem(data.ID, 'one');
            } else if (layEvent === 'edit') { //编辑
                // showEdit('upd', data.ID);
                show('修改信息','edit', data.ID);
            } else if (layEvent === 'qs') { //签收
                top.winui.window.confirm('确认签收当前信息吗？', { icon: 3, title: '签收信息' }, function () {
                    var index = layer.load(1);
                    $.ajax({
                        type: 'post',
                        url: mUrl + '/qs',
                        headers: { token: localStorage["token"] },
                        data: { id: data.ID, user: $("#hdfUser").val(), dwmc: $("#hdfDwbmMc").val(), dwdm: $("#hdfDwbm").val() },
                        dataType: 'json',
                        success: function (data) {
                            layer.close(index);
                            if (data.code == "1") {
                                top.winui.window.msg('签收成功', {
                                    icon: 1,
                                    time: 1000
                                });
                                reloadTable();
                            } else {
                                top.winui.window.msg(data.msg, {
                                    icon: 2,
                                    time: 1000
                                });
                            }
                        },
                        error: function (xml) {
                            layer.close(index);
                            top.winui.window.msg('操作失败', {
                                icon: 2,
                                time: 1000
                            });
                        }
                    });
                });
            } else if (layEvent === 'look') { //查看
                show('查看信息','look', data.ID);
            } else if (layEvent === 'xf') { //下发
                show('下发信息','xf', data.ID);
            } else if (layEvent === 'fk') { //反馈
                show('反馈信息','fk', data.ID);
            }
        });
    }
    //初始化用户信息
    function getUserByToken() {
        $.ajax({
            url: "/api/home/getUserByToken",
            method: 'GET',
            data: { 'token': localStorage["token"] },
            dataType: "JSON",
            success: function (data) {
                if (data && data.LOGINNAME) {
                    $("#hdfDwbmMc").val(data.DWMC);
                    $("#hdfDwbm").val(data.DWDM);
                    $("#hdfUser").val(data.LOGINNAME);
                    mSelData.dwdm = data.DWDM;
                    mSelData.user = data.LOGINNAME;
                    loadData();
                }
            },
            error: function () {
                location.href = "/error";
            }
        });
    }
    //初始化
    getUserByToken();
    //表格重载
    function reloadTableAll() {
        mSelData.iscon = 1;
        table.reload(tableId, {
            where: mSelData,
            page: {
                curr: mCurr
            }
        });
    }
    //表格查询
    function searchTable() {
        mCurr = 1;
        mSelData.mc = $('#txt_mc').val();
        mSelData.nr = $('#txt_nr').val();
        mSelData.lx = $('#ddl_lx').val();
        mSelData.qs = $('#ddl_qs').val();
        mSelData.fk = $('#ddl_fk').val();
        mSelData.fbsjS = $('#txt_fbsjS').val();
        mSelData.fbsjE = $('#txt_fbsjE').val();
        reloadTableAll();
    }
    //表格刷新
    function reloadTable() {
        mSelData.iscon = 1;
        reloadTableAll();
    }
    //打开编辑页面
    function showEdit(varType, varId) {
        var tmpTitle = '添加信息';
        var tmpUrl = '/dtgj/zhld/ctcz/ctczedit?menuid=' + $.getUrlParam("id");
        if (varType == 'upd') {
            tmpTitle = '修改信息';
            tmpUrl += ('&id=' + varId);
        }
        //从桌面打开
        top.winui.window.open({
            id: 'editCtcz',
            type: 2,
            title: tmpTitle,
            content: tmpUrl,
            maxOpen: true
        });
    }
    //打开页面
    function show(varName,varType, varId) {
        var tmpTitle = varName;
        var tmpUrl = '/dtgj/zhld/ctcz/ctcz'+varType+'?menuid=' + $.getUrlParam("id");
        tmpUrl += ('&id=' + varId);
        //从桌面打开
        top.winui.window.open({
            id: varType+'Ctcz',
            type: 2,
            title: tmpTitle,
            content: tmpUrl,
            maxOpen: true
        });
    }
    //删除信息
    function deleteItem(ids, type) {
        var msg = type == 'one' ? '确认删除当前信息吗？' : '确认删除选中数据吗？'
        top.winui.window.confirm(msg, { icon: 3, title: '删除信息' }, function () {
            var index = layer.load(1);
            $.ajax({
                type: 'post',
                url: mUrl+'/delete',
                headers: { token: localStorage["token"] },
                data: { ids: ids },
                dataType: 'json',
                success: function (data) {
                    layer.close(index);
                    if (data.code == "1") {
                        layer.close(index);
                        top.winui.window.msg('删除成功', {
                            icon: 1,
                            time: 1000
                        });
                        delfiles(ids);
                        reloadTable();
                    } else {
                        layer.close(index);
                        top.winui.window.msg(data.msg, {
                            icon: 2,
                            time: 1000
                        });
                    }
                },
                error: function (xml) {
                    layer.close(index);
                    top.winui.window.msg('操作失败', {
                        icon: 2,
                        time: 1000
                    });
                }
            });
        });
    }
    //删除选中信息
    function deleteItemAll() {
        var ids = '';
        $('input[rid=rowCheck]:checked').each(function () {
            ids += $(this).val() + ',';
        });
        if (ids.length <= 0) {
            top.winui.window.msg('请选择一条数据', {
                time: 1000
            });
        }else{
            deleteItem(ids, 'all');
        }
        // var checkStatus = table.checkStatus(tableId);
        // var checkCount = checkStatus.data.length;
        // if (checkCount < 1) {
        //     top.winui.window.msg('请选择一条数据', {
        //         time: 1000
        //     });
        //     return false;
        // }
        // var ids = '';
        // $(checkStatus.data).each(function (index, item) {
        //     ids += item.ID + ',';
        // });
        // deleteItem(ids, 'all');
    }
    //删除文件
    function delfiles(varIds) {
        var index = layer.load(1);
        layui.$.ajax({
            type: 'post',
            url: '/api/dtgj/fjxx/deletebypid',
            data: { ids: varIds, urltop: 'DTGJ/CTCZ/' },
            dataType: 'json',
            headers: { token: localStorage["token"] },
            success: function (json) {
                layer.close(index);
                if (json.code != "1") {
                    top.winui.window.msg(json.msg, {
                        icon: 2,
                        time: 1000
                    });
                }
            },
            error: function (xml) {
                layer.close(index);
                msg("删除文件操作失败", {
                    icon: 2,
                    time: 1000
                });
            }
        });
    }
    //绑定按钮事件
    $('#addctcz').on('click', showEdit);
    $('#deletectcz').on('click', deleteItemAll);
    $('#searchMenu').on('click', searchTable);
    $('#reloadTable').on('click', reloadTable);
    exports('ctczlist', {});
});