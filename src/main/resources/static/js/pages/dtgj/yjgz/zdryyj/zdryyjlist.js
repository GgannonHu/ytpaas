var mDataCon = 0;
var mCurr = 1;
var mUrlTop = "/api/dtgj/yjgz/zdryyj";
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
            url: mUrlTop + '/rlyjlist',
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
                { field: 'GAJGJGDM', title: '公安机关', width: 220 },
                { field: 'DTXLDM', title: '地铁线路', width: 220 },
                { field: 'DTZDMC', title: '地铁站点', width: 220 },
                { field: 'GJXLDM', title: '公交线路', width: 220 },
                { field: 'ZDRY_FLDM', title: '重点人分类', width: 220 },
                { field: 'ZDRY_XM', title: '重点人姓名', width: 220 },
                { field: 'ZDRY_GMSFHM', title: '重点人身份证号', width: 220 },
                { field: 'ZDRY_ZPRLZP', title: '抓拍人脸照片', width: 220 },
                { field: 'ZDRY_BKRLZP', title: '布控人脸照片', width: 220 },
                { field: 'YJSJ', title: '预警时间', width: 220 },
                { fixed: 'right', title: '操作', align: 'center', toolbar: '#barYjct', width: 220 }
            ]]
        });

        //监听工具条
        table.on('tool(list)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值
            var tr = obj.tr; //获得当前行 tr 的DOM对象
            var ids = '';   //选中的Id
            if (layEvent == 'sel') {
                var tmpTitle = '详情信息';
                var tmpUrl = '/dtgj/yjgz/zdryyj/sel?menuid=' + $.getUrlParam("id") + '&id=' + data.ID;
                //从桌面打开
                top.winui.window.open({
                    id: 'selZdryyj',
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
    }

    //绑定按钮事件
    $('#btnSel').on('click', searchTable);
    $('#btnReloadTable').on('click', reloadTable);

    exports('zdryyjlist', {});
});
