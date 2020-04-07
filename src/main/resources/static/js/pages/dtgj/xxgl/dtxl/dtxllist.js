var mDataCon = 0;
var mCurr = 1;
var mUrlTop = "/api/dtgj/xxgl/dtxl";
var mSelData = { name: '', count: 0, iscon: 1 };
var mLOGINNAME = '';
layui.config({
    base: '/js/core/winui/' //指定 winui 路径
    , version: '1.0.0-beta'
}).define(['table', 'jquery', 'winui', 'laypage', 'form'], function (exports) {

    winui.renderColor();

    var table = layui.table;
    var $ = layui.$;
    var form = layui.form;
    //var laypage = layui.laypage;
    var tableId = 'tableid';
    //表格渲染
    function loadDataData() {
        table.render({
            id: tableId,
            elem: '#list',
            url: mUrlTop + '/list',
            headers: { token: localStorage["token"] },
            where: mSelData,
            height: 'full-75', //自适应高度
            page: true,
            limits: [5, 10, 15, 20, 30, 40, 50, 100],
            limit: 5,
            done: function (res, curr, count) {
                $('#topcheck').prop('checked', false);
                form.render('checkbox');
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
                { field: 'ID', title: '<input id="topcheck" type="checkbox" lay-skin="primary" />', toolbar: '#barSelRow', width: 50 },
                {
                    field: 'XZQHDM', title: '行政区划', templet: function (d) {
                        getCsmcByBm('lbxzqh_' + d.ID,'XZQH', d.XZQHDM);
                        return '<label id="lbxzqh_' + d.ID + '"></label>';
                    }, width: '15%'
                },
                { field: 'DTXLBH', title: '线路编码', width: '12%' },
                { field: 'DTXLMC', title: '线路名称' },
                { field: 'DTXLQDZ', title: '起点站', width: '12%' },
                { field: 'DTXLZDZ', title: '终点站', width: '12%' },
                { fixed: 'right', title: '操作', align: 'center', toolbar: '#barYjct', width: 220 }
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
            if (layEvent == 'del') { //删除
                deleteItem(data.ID, 'one');
            } else if (layEvent == 'edit') { //编辑
                showEdit('upd', data.ID);
            } else if (layEvent == 'sel') { //编辑
                showEdit('sel', data.ID);
            } else if (layEvent == 'selzd') {
                var tmpTitle = '站点信息';
                var tmpUrl = '/dtgj/xxgl/dtzd/list?id=listDtzd&xlbm=' + data.DTXLBH + '&zdmc=' + mSelData.zdmc;;
                //从桌面打开
                top.winui.window.open({
                    id: 'listDtzd',
                    type: 2,
                    title: tmpTitle,
                    content: tmpUrl,
                    maxOpen: true
                });
            }
        });
    }

    function getUserByToken() {
        $.ajax({
            url: "/api/home/getUserByToken",
            method: 'GET',
            data: { 'token': localStorage["token"] },
            dataType: "JSON",
            success: function (data) {
                if (data && data.LOGINNAME) {
                    mLOGINNAME = data.LOGINNAME;
                    getSelTj();
                    loadDataData();
                }
            },
            error: function () {
                location.href = "/error";
            }
        });
    }

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
        getSelTj();
        reloadTableAll();
    }
    //表格刷新
    function reloadTable() {
        mSelData.iscon = 1;
        reloadTableAll();
    }
    function getSelTj() {
        mSelData.xlmc = $('#xlmc').val();
        mSelData.zdmc = $('#zdmc').val();
    }

    //打开添加页面
    function showEdit(varType, varId) {
        var tmpTitle = '详情信息';
        var tmpUrl = '/dtgj/xxgl/dtxl/edit?type=' + varType + '&menuid=' + $.getUrlParam("id") + '&id=' + varId;

        //从桌面打开
        top.winui.window.open({
            id: 'editDtxl',
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
                url: mUrlTop + '/delete',
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
        } else {
            deleteItem(ids, 'all');
        }
    }

    function getCsmcByBm(varId, varTid, varBm) {
        var index = layer.load(1);
        $.ajax({
            type: 'get',
            url: '/api/dtgj/com/getcsmcbybm',
            headers: { token: localStorage["token"] },
            data: { tid: varTid, bm: varBm },
            dataType: 'json',
            success: function (data) {
                layer.close(index);
                $('#' + varId).html(data.data);
            }
        });
    }

    /*
       var checkStatus = table.checkStatus(tableId);
       var checkCount = checkStatus.data.length;
       if (checkCount < 1) {
           top.winui.window.msg('请选择一条数据', {
               time: 1000
           });
           return false;
       }
       var ids = '';
       $(checkStatus.data).each(function (index, item) {
           ids += item.ID + ',';
       });
       deleteItem(ids, 'all');
       */
    //绑定按钮事件
    $('#btnAdd').on('click', function () {
        showEdit('add', '');
    });
    $('#btnDelete').on('click', deleteItemAll);
    $('#btnSel').on('click', searchTable);
    $('#btnReloadTable').on('click', reloadTable);


    exports('dtxllist', {});
});
