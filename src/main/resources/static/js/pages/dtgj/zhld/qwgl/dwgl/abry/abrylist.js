var api = "/api/dtgj/zhld/qwgl/dwgl/abry/";
var dwdmsj = "";
var dwdmfj = "";
var mDataCon = 0;
var mCurr = 1;
var gajgjgdm = "";
var tjr = "";
var mSelData = { dtzdmc: '', gjxlbm: '', gmsfhm: '', xm: '', dw: '', gajgjgdm: gajgjgdm, count: 0, iscon: 1 }
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
            elem: '#abry',
            url: api + "list",
            headers: { token: localStorage["token"] },
            where: mSelData,
            height: 'full-110', //自适应高度
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
                { field: 'GJXLBM', title: '公交线路编码', width: '15%' },
                { field: 'DTZDMC', title: '站点名称', width: '10%' },
                { field: 'ABRY_GMSFHM', title: '公民身份号码', width: '20%' },
                { field: 'ABRY_XM', title: '姓名', width: '10%' },
                { field: 'ABRY_DWMC', title: '单位', width: '25%' },
                { title: '操作', align: 'center', toolbar: '#barAbry', width: '15%', templet: '#colNoNull' }
            ]]
        });
        //监听工具条
        table.on('tool(abry)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
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
                    dwdmsj = data.DWDM.substring(0, 4);
                    dwdmfj = data.DWDM.substring(0, 6);
                    if (dwdmfj == "230000") {
                        bindxlxx("23");
                    } else if (dwdmfj.substring(4, 6) == "00") {
                        bindxlxx(dwdmsj);
                    } else {
                        bindxlxx(dwdmfj);
                    }
                }
            },
            error: function () {
                location.href = "/error";
            }
        });
    }

    getUserByToken();

    function bindxlxx(xzqh) {
        gajgjgdm = xzqh;
        mSelData.gajgjgdm = gajgjgdm;
        $.ajax({
            url: api + "xlxx",
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
                $("#gjxlbm").html(str);
                loadData();

                form.render('select');
                form.on('select(gjxlbm)', function (data) {
                    // alert(data.value); //得到被选中的值
                    bindzdmc(data.value);
                });
            }
        });
    }

    function bindzdmc(bm) {
        $.ajax({
            url: api + "zdxx",
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
                $("#dtzdmc").html(str);
                form.render('select');

                form.on('select(dtzdmc)', function (data) {
                    // alert(data.value); //得到被选中的值
                });
            }
        });
    }

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
        mSelData.dtzdmc = $('#dtzdmc').val(), mSelData.gjxlbm = $('#gjxlbm').val(), mSelData.gmsfhm = $('#gmsfhm').val(), mSelData.xm = $('#xm').val(), mSelData.dw = $('#dw').val(), mSelData.gajgjgdm = gajgjgdm
        reloadTableAll();
    }
    //表格刷新
    function reloadTable() {
        mSelData.iscon = 1;
        reloadTableAll();
    }

    //打开添加页面
    function showEdit(varType, varId, varTJR) {
        var tmpTitle = '添加安保人员';
        var tmpUrl = '/dtgj/zhld/qwgl/dwgl/abry/edit?menuid=' + $.getUrlParam("id");
        if (varType == 'upd') {
            if (varTJR == tjr) {
                tmpTitle = '修改安保人员';
            } else {
                tmpTitle = '查看安保人员';
            }
            tmpUrl += ('&id=' + varId);
        }
        //从桌面打开
        top.winui.window.open({
            id: 'editAbry',
            type: 2,
            title: tmpTitle,
            content: tmpUrl,
            maxOpen: true
        });
    }
    //删除信息
    function deleteItem(ids, type) {
        var msg = type == 'one' ? '确认删除当前信息吗？' : '确认删除选中数据吗？'
        top.winui.window.confirm(msg, { icon: 3, title: '删除勤务报备' }, function () {
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

    //绑定按钮事件
    $('#addAbry').on('click', showEdit);
    $('#deleteAbry').on('click', deleteItemAll);
    $('#reloadTable').on('click', reloadTable);
    $('#searchMenu').on('click', searchTable);

    exports('abrylist', {});
});