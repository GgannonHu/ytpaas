var mSelData = {};	// 查询条件
var mSelId = "";	// 随机数变量 如果一致不重新绑定分页
var rowN = 0;		// 当前页实际条数，用于删除后刷新哪一页
var row = 1;		// 当前页
var limit = 10;		// 一页多少条

layui.config({
    base: '/js/core/winui/' //指定 winui 路径
    , version: '1.0.0-beta'
}).define(['table', 'laypage', 'jquery', 'winui'], function (exports) {

    winui.renderColor();

    var table = layui.table,
        $ = layui.$,
        laydate = layui.laydate,
        tableId = 'tableid',
        laypage = layui.laypage;
    var form = layui.form;
    var thisForm = $.getUrlParam("id");
    
    // 查询权限、权限机构代码、添加权限、修改权限、删除权限
    var cxqx = "", jgdm = "", tjqx = "", xgqx = "", scqx = "";

    // 初始化权限
    function init() {
    	$.ajax({
    		url: '/api/system/sjqx',
    		data: {url: '/system/user'},
    		type: 'get',
    		dataType: 'json',
    		headers: {token: localStorage["token"]},
    		success: function(data) {
				jgdm = data.msg;
    			$.each(data.data, function() {
    				// 初始化查询权限、添加权限、修改权限、删除权限
    				if(this.QXLB == "CXQX01" || this.QXLB == "CXQX02" || this.QXLB == "CXQX03" || 
    						this.QXLB == "CXQX04" || this.QXLB == "CXQX05") {
    					cxqx = this.QXLB;
    				} else if (this.QXLB == "TJQX") {
    					tjqx = this.QXLB;
    				} else if (this.QXLB == "XGQX") {
    					xgqx = this.QXLB;
    				} else if (this.QXLB == "SCQX") {
    					scqx = this.QXLB;
    				}
    			});
    			// 添加和删除权限
    			if(!tjqx) $("#addUser").hide();
    			if(!scqx) $("#deleteUser").hide();
    			
    			// 初始化，加载第一页
    			$('#jgdm').val(jgdm);
    			loadDataSel(1);
    		}
    	});
    }
    // 初始化查询条件+分页条件
    function loadDataSel(curr) {
        row = curr;
        mSelData = {
            PageSize: limit, 
            jgdm: $('#jgdm').val()
        };
        loadData(curr, 1);
    }
    // 加载数据，cuur当前页，isCon是否加载总页数（1 加载，0 不加载）
    function loadData(curr, isCon) {
    	// 表格内修改和删除权限
    	var bar = "";
    	if(xgqx && scqx) bar = "#barUser1";
    	else if(xgqx && !scqx) bar = "#barUser2";
    	else if(!xgqx && scqx) bar = "#barUser3";
    	
    	// 设置参数
    	mSelId = Math.random();
        var tmpSelData = mSelData;
        tmpSelData["PageNo"] = curr;
    	
        // 渲染表格
    	table.render({
            id: tableId,
            elem: '#user',
            url: '/api/system/user/list',
            headers: {token: localStorage["token"]},
            height: 'full-165', //自适应高度
            where: tmpSelData,
            cols: [[
            	{ field: 'YHBM', type: 'checkbox' },
                { field: 'LOGINNAME', title: '登录名' },
                { field: 'YHXM', title: '姓名' },
                { field: 'YHSFZH', title: '身份证' },
                { field: 'YHLXDH', title: '电话' },
                { field: 'YHJH', title: '警号' },
            	{ field: 'DWMC', title: '单位' },
                { title: '操作', fixed: 'right', align: 'center', toolbar: bar, width: '16%' }
            ]],
            done: function(res, curr, count){
                // 表格渲染完成之后，如果当前没有数据且不是第一页，重新渲染上一页的数据
                if (res.data.length == 0 && row > 1) {
                    row = row - 1;
                    loadDataSel(row);
                }
                else {
                	// 如果正确渲染，设置当前页实际条数
                    rowN = res.data.length;
                }
            }
        });
    	
    	// 如果需要加载总页数，加载总页数；否则只刷新页码
    	if (isCon != 0) {
            loadDataCon(tmpSelData, curr, mSelId);
        } else {
            SetPageData(curr);
        }
    }
    // 加载总页数及分页信息
    function loadDataCon(tmpSelData, curr, varSelId) {
        $.get("/api/system/user/list/count", tmpSelData,
            function (d) {
                if (d.code == "1" && mSelId == varSelId) {
                    mCon = d.data;
                    SetPageData(curr);
                }
            }, "json");
    }
    // 设置分页信息
    function SetPageData(curr) {
        laypage.render({
            elem: 'page',
            count: mCon,
            limit: limit,
            curr: curr || 1,
            layout: ['count', 'prev', 'page', 'next', 'refresh', 'skip'],
            jump: function (obj, first) {
                if (!first) {
                    row = obj.curr;
                    loadData(obj.curr, 0);
                }
            }
        });
        $(".layui-laypage-count").html("共&nbsp;" + Math.ceil(mCon / limit).toString() + "&nbsp;页&nbsp;" + mCon + "&nbsp;条&nbsp;");
    }
    
    //监听工具条
    table.on('tool(user)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值
        var tr = obj.tr; //获得当前行 tr 的DOM对象
        var ids = '';   //选中的Id
        if (layEvent === 'del') { //删除
        	 $(data).each(function (index, item) {
                 ids += item.YHBM + ',';
             });
            deleteUser(ids, obj);
        } else if (layEvent === 'edit') { //编辑
            var index = layer.load(1);
            layer.close(index);
            //从桌面打开
            top.winui.window.open({
                id: 'editUser',
                type: 2,
                title: '编辑用户信息',
                content: '/system/user/edit?id=' + data.YHBM + '&menuid=' + thisForm,
                maxOpen: true
            });
        }
    });
    
    // 表格重载，回到第一页
    function searchTable() {
        loadDataSel('1');
    }
    // 表格重载，当前页
    function reloadTable() {
        loadDataSel(row);
    }

    //打开添加页面
    function addUser() {
        var index = layer.load(1);
        layer.close(index);
        //从桌面打开
        top.winui.window.open({
            id: 'editUser',
            type: 2,
            title: '添加用户',
            content: '/system/user/edit?menuid=' + thisForm,
            maxOpen: true
        });
    }
    //删除角色
    function deleteUser(ids, obj) {
        var msg = obj ? '确认删除用户【' + obj.data.YHXM + '】吗？' : '确认删除选中数据吗？'
        top.winui.window.confirm(msg, { icon: 3, title: '删除系统用户' }, function (index) {
        	$.ajax({
                type: 'post',
                url: '/api/system/user/delete',
                headers: {token: localStorage["token"]},
                data: {ids: ids},
                dataType:'json',
                success: function (data) {
                	if(data.code == "1"){
                		layer.close(index);
                        top.winui.window.msg('删除成功', {
                            icon: 1,
                            time: 2000
                        });
                        
                        // 如果删除的数据大于当前页的实际数据，则刷新上一页
                        if (rowN <= (ids.split(',').length - 1)) {
                            row = row - 1;
                        }
                        loadDataSel(row);
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
    
    // 初始化
    init();
    // 添加按钮
    $('#addUser').on('click', addUser);
    // 删除按钮
    $('#deleteUser').on('click', function () {
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
            ids += item.YHBM + ',';
        });
        deleteUser(ids);
    });
    // 刷新数据，刷新当前页
    $('#reloadTable').on('click', reloadTable);
    // 搜索按钮，回到第一页
    $('#searchMenu').on('click', searchTable);
    // 选择机构
    $('#jgmc').on('click', function() {
    	var selectJGForm = "selectJG";
    	top.winui.window.open({
            id: selectJGForm,
            title: '选择单位',
            type: 2,
            area: ['400px', '300px'],
            content: '/system/jg/select?sjqx=' + cxqx + '&jgdm=' + jgdm + '&menuid=' + thisForm + '&newMenuid=' + selectJGForm,
            maxOpen: true
        });
    });
    // 屏蔽form提交
    form.on('submit', function (data) {
	        return false;
	});
    exports('userlist', {});
});
