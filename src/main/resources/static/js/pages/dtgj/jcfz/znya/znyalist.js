var api = "/api/dtgj/jcfz/znya/";
var mDataCon = 0;
var mCurr = 1;
var mSelData = { name: '', dj: '', fbsjBegin: '', fbsjEnd: '', count: 0, iscon: 1 }
var tjr = "";
layui.config({
    base: '/js/core/winui/' //指定 winui 路径
    , version: '1.0.0-beta'
}).define(['table', 'jquery', 'winui', 'laydate'], function (exports) {
    winui.renderColor();

    var table = layui.table,
        form = layui.form,
        $ = layui.$,
        tableId = 'tableid',
        laydate = layui.laydate,
        laypage = layui.laypage;

    laydate.render({
        elem: '#fbsjBegin'
    });

    laydate.render({
        elem: '#fbsjEnd'
    });

    form.on('submit', function (data) {
        return false;
    });
    
    form.on('checkbox', function (data) {

        var id = data.elem.id;
        var checked = data.elem.checked;
        if (id == 'topcheck') {
            $("input[rid='rowCheck']").prop('checked', checked);
            form.render('checkbox');
        }
    });

    function loadData() {
        table.render({
            id: tableId,
            elem: '#znya',
            url: api + "list",
            headers: { token: localStorage["token"] },
            where: mSelData,
            height: 'full-105', //自适应高度
            page: true,
            limits: [5, 10, 15, 20, 30, 40, 50, 100],
            limit: 5,
            done: function (res, curr, count) {
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
                { field: 'ID', title: '<input id="topcheck" type="checkbox" lay-skin="primary" >', toolbar: '#barSelRow', width: '5%' },
                { field: 'NAME', title: '名称', width: '20%' },
                { field: 'DJ', title: '等级', width: '10%' },
                { field: 'NR', title: '内容', width: '30%' },
                { field: 'FBSJ', title: '发布时间', width: '20%' },
                { title: '操作', align: 'center', toolbar: '#barZnya', width: '15%', templet: '#colNoNull' }
            ]]
        });
        //监听工具条
        table.on('tool(znya)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值
            var tr = obj.tr; //获得当前行 tr 的DOM对象
            var ids = '';   //选中的Id
            if (layEvent == 'del') { //删除
                deleteItem(data.ID, 'one');
            } else if (layEvent == 'edit') { //编辑
                showEdit('upd', data.ID, data.TJR);
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
                    tjr = data.LOGINNAME;
                    loadData();
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
        mSelData.name = $('#name').val(), mSelData.dj = $('#dj').val(), mSelData.fbsjBegin = $('#fbsjBegin').val(), mSelData.fbsjEnd = $('#fbsjEnd').val()
        reloadTableAll();
    }
    //表格刷新
    function reloadTable() {
        mSelData.iscon = 1;
        reloadTableAll();
    }

    //打开添加页面
    function showEdit(varType, varId, varTJR) {
        var tmpTitle = '添加智能预案';
        var tmpUrl = '/dtgj/jcfz/znya/edit?menuid=' + $.getUrlParam("id");
        if (varType == 'upd') {
            if (varTJR == tjr) {
                tmpTitle = '修改智能预案';
            } else {
                tmpTitle = '查看智能预案';
            }
            tmpUrl += ('&id=' + varId);
        }
        //从桌面打开
        top.winui.window.open({
            id: 'editZnya',
            type: 2,
            title: tmpTitle,
            content: tmpUrl,
            maxOpen: true
        });
    }
    //删除信息
    function deleteItem(ids, type) {
        var msg = type == 'one' ? '确认删除当前信息吗？' : '确认删除选中数据吗？'
        top.winui.window.confirm(msg, { icon: 3, title: '删除智能预案' }, function () {
            var index = layer.load(1);
            $.ajax({
                type: 'post',
                url: api + 'delete',
                headers: { token: localStorage["token"] },
                data: { ids: ids },
                dataType: 'json',
                success: function (data) {
                    layer.close(index);
                    if (data.code == "1") {
                        layer.close(index);
                        top.winui.window.msg('删除成功', {
                            icon: 1,
                            time: 2000
                        });
                        delfiles(ids);
                        reloadTable();
                    } else {
                        layer.close(index);
                        top.winui.window.msg(data.msg, {
                            icon: 2,
                            time: 2000
                        });
                    }
                },
                error: function (xml) {
                    layer.close(index);
                    top.winui.window.msg('操作失败', {
                        icon: 2,
                        time: 2000
                    });
                }
            });
        });
    }

    //删除选中信息
    function deleteItemAll() {
        var checkStatus = table.checkStatus(tableId);
        var checkCount = checkStatus.data.length;
        if (checkCount < 1) {
            top.winui.window.msg('请选择一条数据', {
                time: 2000
            });
            return false;
        }
        var ids = '';
        $(checkStatus.data).each(function (index, item) {
            ids += item.ID + ',';
        });
        deleteItem(ids, 'all');
    }

    //删除文件
    function delfiles(varIds) {
        var index = layer.load(1);
        layui.$.ajax({
            type: 'post',
            url: '/api/dtgj/fjxx/deletebypid',
            data: { ids: varIds, urltop: 'DTGJ/ZNYA/' },
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
                    time: 2000
                });
            }
        });
    }

    //绑定按钮事件
    $('#addZnya').on('click', showEdit);
    $('#deleteZnya').on('click', deleteItemAll);
    $('#reloadTable').on('click', reloadTable);
    $('#searchMenu').on('click', searchTable);

    exports('znyalist', {});
});