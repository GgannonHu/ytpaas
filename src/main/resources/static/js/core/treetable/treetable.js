layui.define(['layer', 'table'], function (exports) {
    var _$ = layui.jquery;
    var layer = layui.layer;
    var table = layui.table;

    var treetable = {
        // 渲染树形表格
        render: function (param) {
            // 检查参数
            if (!treetable.checkParam(param)) {
                return;
            }
            // 获取数据
            if (param.data) {
                treetable.init(param, param.data);
            } else {
                _$.ajax({
                	url: param.url, 
                	headers: param.headers, 
                	data: param.where, 
                	type: "get",
                	dataType: 'json',
                	success: function (res) {
                		treetable.init(param, res.data);
                	},
                	error: function(e) {
                		layer.msg('数据加载失败', {
                            offset: '40px',
                            zIndex: layer.zIndex
                        });
                	}
                });
            }
        },
        // 渲染表格
        init: function (param, data) {
            var mData = [];
            var doneCallback = param.done;
            var tNodes = data;
            
            // 补上id和pid字段
            for (var i = 0; i < tNodes.length; i++) {
                var tt = tNodes[i];
                if (!tt.id) {
                    if (!param.treeIdName) {
                        layer.msg('参数treeIdName不能为空', {icon: 5});
                        return;
                    }
                    tt.id = tt[param.treeIdName];
                }
                if (!tt.pid) {
                    if (!param.treePidName) {
                        layer.msg('参数treePidName不能为空', {icon: 5});
                        return;
                    }
                    tt.pid = tt[param.treePidName];
                }
            }

            // 对数据进行排序
            var sort = function (s_pid, data) {
                for (var i = 0; i < data.length; i++) {
                    if (data[i].pid == s_pid) {
                        var len = mData.length;
                        if (len > 0 && mData[len - 1].id == s_pid) {
                            mData[len - 1].isParent = true;
                        }
                        mData.push(data[i]);
                        sort(data[i].id, data);
                    }
                }
            };
            sort(param.treeSpid, tNodes);

            // 重写参数
            param.url = undefined;
            param.data = mData;
            
            param.page = {
                count: param.data.length,
                limit: param.data.length
            };
            param.cols[0][param.treeColIndex].templet = function (d) {
                var mId = d.id;
                var mPid = d.pid;
                var isDir = d.isParent;
                var emptyNum = treetable.getEmptyNum(mPid, mData);
                var iconHtml = '';
                for (var i = 0; i < emptyNum; i++) {
                    iconHtml += '<span class="treeTable-empty"></span>';
                }
                if (isDir) {
                    iconHtml += '<i class="layui-icon layui-icon-triangle-d"></i> <i class="layui-icon layui-icon-layer"></i>';
                } else {
                    iconHtml += '<i class="layui-icon layui-icon-file"></i>';
                }
                iconHtml += '&nbsp;&nbsp;';
                var ttype = isDir ? 'dir' : 'file';
                var vg = '<span class="treeTable-icon open" lay-tid="' + mId + '" lay-tpid="' + mPid + '" lay-ttype="' + ttype + '">';
                
                return vg + iconHtml + d[param.cols[0][param.treeColIndex].field] + '</span>';
            };

            param.done = function (res, curr, count) {
                _$(param.elem).next().addClass('treeTable');
                //不显示工具条及分页时，让表格内容自适应高度
                _$(param.elem).next().find(".layui-table-box").css("height","100%");
                _$(param.elem).next().find(".layui-table-box .layui-table-main").css("height",_$(param.elem).next().find(".layui-table-box .layui-table-main").height()+40);
                
                _$('.treeTable .layui-table-page').css('display', 'none');
                _$(param.elem).next().attr('treeLinkage', param.treeLinkage);
                // 绑定事件换成对body绑定
                /*_$('.treeTable .treeTable-icon').click(function () {
                    treetable.toggleRows(_$(this), param.treeLinkage);
                });*/
                if (param.treeDefaultClose) {
                    treetable.foldAll(param.elem);
                }
                if (doneCallback) {
                    doneCallback(res, curr, count);
                }
            };
            // 渲染表格
            table.render(param);
        },
        // 计算缩进的数量
        getEmptyNum: function (pid, data) {
            var num = 0;
            if (!pid) {
                return num;
            }
            var tPid;
            for (var i = 0; i < data.length; i++) {
                if (pid == data[i].id) {
                    num += 1;
                    tPid = data[i].pid;
                    break;
                }
            }
            return num + treetable.getEmptyNum(tPid, data);
        },
        //父子节点联动
        checkRows:function (_$dom, linkage){
        	var type = _$dom.attr('lay-ttype');
            if ('file' == type) {
                return;
            }
            var icon = _$dom.find(".treeTable-icon");
            var mId = $(icon).attr('lay-tid');
            _$dom.closest('tbody').find('tr').each(function () {
                var _$ti = _$(this).find('.treeTable-icon');
                var pid = _$ti.attr('lay-tpid');
                if (mId == pid) {
                	var input = _$(this).find('.layui-form-checkbox').prev();
                	if(linkage){
                		if(!input.prop("checked")){
                			_$(this).find('.layui-form-checkbox').trigger("click");
                		}
                	}else{
                		if(input.prop("checked")){
                			_$(this).find('.layui-form-checkbox').trigger("click");
                		}
                	}
                }
            });
        },
        
        // 展开/折叠行
        toggleRows: function (_$dom, linkage) {
            var type = _$dom.attr('lay-ttype');
            if ('file' == type) {
                return;
            }
            var mId = _$dom.attr('lay-tid');
            var isOpen = _$dom.hasClass('open');
            if (isOpen) {
                _$dom.removeClass('open');
            } else {
                _$dom.addClass('open');
            }
            _$dom.closest('tbody').find('tr').each(function () {
                var _$ti = _$(this).find('.treeTable-icon');
                var pid = _$ti.attr('lay-tpid');
                var ttype = _$ti.attr('lay-ttype');
                var tOpen = _$ti.hasClass('open');
                if (mId == pid) {
                    if (isOpen) {
                        _$(this).hide();
                        if ('dir' == ttype && tOpen == isOpen) {
                            _$ti.trigger('click');
                        }
                    } else {
                        _$(this).show();
                        if (linkage && 'dir' == ttype && tOpen == isOpen) {
                            _$ti.trigger('click');
                        }
                    }
                }
            });
        },
        // 检查参数
        checkParam: function (param) {
            if (!param.treeSpid && param.treeSpid != 0) {
                layer.msg('参数treeSpid不能为空', {icon: 5});
                return false;
            }

            if (!param.treeColIndex && param.treeColIndex != 0) {
                layer.msg('参数treeColIndex不能为空', {icon: 5});
                return false;
            }
            return true;
        },
        // 展开所有
        expandAll: function (dom) {
            _$(dom).next('.treeTable').find('.layui-table-body tbody tr').each(function () {
                var _$ti = _$(this).find('.treeTable-icon');
                var ttype = _$ti.attr('lay-ttype');
                var tOpen = _$ti.hasClass('open');
                if ('dir' == ttype && !tOpen) {
                    _$ti.trigger('click');
                }
            });
        },
        // 折叠所有
        foldAll: function (dom) {
            _$(dom).next('.treeTable').find('.layui-table-body tbody tr').each(function () {
                var _$ti = _$(this).find('.treeTable-icon');
                var ttype = _$ti.attr('lay-ttype');
                var tOpen = _$ti.hasClass('open');
                if ('dir' == ttype && tOpen) {
                    _$ti.trigger('click');
                }
            });
        }
    };

    layui.link(layui.cache.base + 'treetable/treetable.css');

    // 给图标列绑定事件
    _$('body').on('click', '.treeTable .treeTable-icon', function () {
        var treeLinkage = _$(this).parents('.treeTable').attr('treeLinkage');
        if ('true' == treeLinkage) {
            treetable.toggleRows(_$(this), true);
        } else {
            treetable.toggleRows(_$(this), false);
        }
    });
    
    // 选择行事件
    _$('body').on('click', '.treeTable .layui-form-checkbox', function () {
    	var input = _$(this).prev();
        var treeLinkage = $(input).prop("checked");
        if (treeLinkage) {
            treetable.checkRows(_$(this).parents("tr"), true);
        } else {
            treetable.checkRows(_$(this).parents("tr"), false);
        }
    });

    exports('treetable', treetable);
});
