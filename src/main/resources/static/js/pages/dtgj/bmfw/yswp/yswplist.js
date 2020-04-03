var mDataCon = 0;
var mCurr = 1;
var mUrl = "/api/dtgj/yswp";
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
        elem: '#txt_sqsjS'
    });
    laydate.render({
        elem: '#txt_sqsjE'
    });
    form.on('submit', function (data) {
        return false;
    });
    //表格渲染
    function loadData() {
        var index = layer.load(1);
        table.render({
            id: tableId,
            elem: '#list',
            url: mUrl + "/list",
            headers: { token: localStorage["token"] },
            where: mSelData,
            height: 'full-107', //自适应高度
            page: true,
            limits: [5, 10, 15, 20, 30, 40, 50, 100],
            limit: 5,
            done: function(res, curr, count){
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
                { field: 'ID', width: '5%', title: '<input id="topcheck" type="checkbox" lay-skin="primary" >', toolbar: '#barSelRow' },
                { field: 'MC', title: '物品名称', width: '10%' },
                { field: 'MS', title: '物品描述', width: '13%' },
                { field: 'SQDD', title: '拾取地点', width: '10%' },
                { field: 'SQSJ', title: '拾取时间', width: '12%' },
                { field: 'ZT', title: '状态', width: '10%', templet: '#tempZt' },
                { field: 'RLR', title: '认领人', width: '10%', templet: '#tempRl' },
                { field: 'RLSJ', title: '认领时间', width: '12%', templet: '#tempRlSj' },
                { title: '操作', fixed: 'right', align: 'center', toolbar: '#baryswp', width: '18%' }
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
                showEdit('upd', data.ID);
            } else if (layEvent === 'rl') { //认领
                var index = layer.load(1);
                layer.close(index);
                //从桌面打开
                top.winui.window.open({
                    id: 'rlYswp',
                    type: 2,
                    title: '认领物品信息',
                    content: '/dtgj/bmfw/yswp/yswprl?id=' + data.ID + '&menuid=' + $.getUrlParam("id"),
                    maxOpen: true
                });
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
        mSelData.zt = $('#ddl_zt').val();
        mSelData.ms = $('#txt_ms').val();
        mSelData.sqdd = $('#txt_sqdd').val();
        mSelData.sqsjS = $('#txt_sqsjS').val();
        mSelData.sqsjE = $('#txt_sqsjE').val();
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
        var tmpUrl = '/dtgj/bmfw/yswp/yswpedit?menuid=' + $.getUrlParam("id");
        if (varType == 'upd') {
            tmpTitle = '修改信息';
            tmpUrl += ('&id=' + varId);
        }
        //从桌面打开
        top.winui.window.open({
            id: 'editYswp',
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
            data: { ids: varIds, urltop: 'DTGJ/YSWP/' },
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
    $('#addyswp').on('click', showEdit);
    $('#deleteyswp').on('click', deleteItemAll);
    $('#searchMenu').on('click', searchTable);
    $('#reloadTable').on('click', reloadTable);
    exports('yswplist', {});
});