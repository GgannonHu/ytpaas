layui.config({
    base: '/js/core/winui/' //指定 winui 路径
    , version: '1.0.0-beta'
}).define(['table', 'jquery', 'winui'], function (exports) {

    winui.renderColor();

    var table = layui.table,
        $ = layui.$,
        tableId = 'tableid';
    //表格渲染
  
    table.render({
        id: tableId,
        elem: '#role',
        url: '/api/system/role/list',
        headers: {token: localStorage["token"]},
        height: 'full-100', //自适应高度
        page: false,
        cols: [[
        	{ field: 'JSBM', type: 'checkbox' },
            { field: 'JSMC', title: '角色名称' },
            { field: 'JSMS', title: '角色描述' },
            { title: '设置权限', fixed: 'right', align: 'center', toolbar: '#barQX' },
            { title: '操作', fixed: 'right', align: 'center', toolbar: '#barRole' }
        ]]
    });
    //监听工具条
    table.on('tool(role)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值
        var tr = obj.tr; //获得当前行 tr 的DOM对象
        var ids = '';   //选中的Id
        if (layEvent === 'del') { //删除
        	 $(data).each(function (index, item) {
                 ids += item.JSBM + ',';
             });
            deleteRole(ids, obj);
        } else if (layEvent === 'edit') { //编辑
            var index = layer.load(1);
            layer.close(index);
            //从桌面打开
            top.winui.window.open({
                id: 'editRole',
                type: 2,
                title: '编辑角色信息',
                content: '/system/role/edit?id=' + data.JSBM + '&jsmc=' + data.JSMC + '&menuid=' + $.getUrlParam("id"),
                maxOpen: true
            });
        } else if (layEvent === 'gnqx') { // 功能权限
            var index = layer.load(1);
            layer.close(index);
            //从桌面打开
            top.winui.window.open({
                id: 'editGNQX',
                type: 2,
                title: '设置角色功能权限',
                content: '/system/role/gnqx?id=' + data.JSBM + '&jsmc=' + data.JSMC + '&menuid=' + $.getUrlParam("id"),
                maxOpen: true
            });
        } else if (layEvent === 'sjqx') { // 数据权限
            var index = layer.load(1);
            layer.close(index);
            //从桌面打开
            top.winui.window.open({
                id: 'editSJQX',
                type: 2,
                title: '设置角色数据权限',
                content: '/system/role/sjqx?id=' + data.JSBM + '&jsmc=' + data.JSMC + '&menuid=' + $.getUrlParam("id"),
                maxOpen: true
            });
        }
    });
    //表格重载
    function reloadTable() {
        table.reload(tableId, {});
    }

    //打开添加页面
    function addRole() {
        var index = layer.load(1);
        layer.close(index);
        //从桌面打开
        top.winui.window.open({
            id: 'editRole',
            type: 2,
            title: '添加角色',
            content: '/system/role/edit?menuid=' + $.getUrlParam("id"),
            maxOpen: true
        });
    }
    //删除角色
    function deleteRole(ids, obj) {
        var msg = obj ? '确认删除用户【' + obj.data.JSMC + '】吗？' : '确认删除选中数据吗？'
        top.winui.window.confirm(msg, { icon: 3, title: '删除系统角色' }, function (index) {
        	$.ajax({
                type: 'post',
                url: '/api/system/role/delete',
                headers: {token: localStorage["token"]},
                data: {JSBM: ids},
                dataType:'json',
                success: function (data) {
                	if(data.code == "1"){
                		layer.close(index);
                        top.winui.window.msg('删除成功', {
                            icon: 1,
                            time: 2000
                        });
                        reloadTable();
                	}else{
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
    //绑定按钮事件
    $('#addRole').on('click', addRole);
    $('#deleteRole').on('click', function () {
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
            ids += item.JSBM + ',';
        });
        deleteRole(ids);
    });
    $('#reloadTable').on('click', reloadTable);

    exports('rolelist', {});
});
