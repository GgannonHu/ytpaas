layui.config({
    base: './js/core/winui/' //指定 winui 路径
    , version: '1.0.0-beta'
}).extend({  //指定js别名
    window: 'js/winui.window',
    desktop: 'js/winui.desktop',//获取桌面菜单
    start: 'js/winui.start', //获取开始菜单
    helper: 'js/winui.helper'
}).define(['window', 'desktop', 'start', 'helper'], function (exports) {
    var $ = layui.jquery;
    var timer;
    var isFullScreen = 0;
    
    $(function () {
        $.ajax({
            url: "/api/home/getUserByToken",
            method: 'GET',
            data:{'token': localStorage["token"]},
            dataType:"JSON",
            success: function (data) {
                if(data && data.bgSrc) {
                    $("body").css("background", "url(" + data.bgSrc + ")");
                }
            },
            error: function() {
                location.href="/error";
            }
        });
        
        winui.window.msg('欢迎登录YTPaaS平台', {
            time: 4500,
            offset: '60px'
        });

    //    winui.window.open({
    //        id: '公告',
    //        type: 1,
    //        title: '演示公告',
    //        content: '<p style="padding:20px;">简洁、直观、高效、灵活、低廉的管理软件开发平台，让软件开发更迅速、简单。</p>',
    //        area: ['400px', '400px']
    //    });

        winui.config({
            settings: layui.data('winui').settings || {
                color: 32,
                taskbarMode: 'bottom',
                startSize: 'sm',
                bgSrc: '/images/bg_04.jpg',
                lockBgSrc: '/images/bg_04.jpg'
            },  //如果本地配置为空则给默认值
            desktop: {
                options: {
                	url: '/api/home/menu/desktop',
                    method: 'post',
                    data: {  },
                    headers:{'token': localStorage["token"]}
                },    //可以为{}  默认 请求 json/desktopmenu.json
                done: function (desktopApp) {
                    desktopApp.ondblclick(function (id, elem) {
                        OpenWindow(elem);
                    });
                    desktopApp.contextmenu({
                        item: ["打开"],
                        item1: function (id, elem) {
                            OpenWindow(elem);
                        },
                        item2: function (id, elem, events) {
                            winui.window.msg('删除回调');
                            $(elem).remove();
                            //从新排列桌面app
                            events.reLocaApp();
                        },
                        item3: function (id, elem, events) {
                            winui.window.msg('自定义回调');
                        }
                    });
                }
            },
            menu: {
                options: {
                    url: '/api/home/menu/desktop',
                    method: 'get',
                    data: { },
                    headers:{'token': localStorage["token"]}
                },
                done: function (menuItem) {
                    //监听开始菜单点击
                    menuItem.onclick(function (elem) {
                        OpenWindow(elem);
                    });
                    menuItem.contextmenu({
                        item: [{
                            icon: 'fa-cog'
                            , text: '设置'
                        }, {
                            icon: 'fa-close'
                            , text: '关闭'
                        }, {
                            icon: 'fa-qq'
                            , text: '右键菜单可自定义'
                        }],
                        item1: function (id, elem) {
                            //设置回调
                            console.log(id);
                            console.log(elem);
                        },
                        item2: function (id, elem) {
                            //关闭回调
                        },
                        item3: function (id, elem) {
                            winui.window.msg('自定义回调');
                        }
                    });
                }
            }
        }).init({
            audioPlay: false, //是否播放音乐（开机音乐只会播放一次，第二次播放需要关闭当前页面从新打开，刷新无用）
            renderBg: false //是否渲染背景图 （由于js是写在页面底部，所以不太推荐使用这个来渲染，背景图应写在css或者页面头部的时候就开始加载）
        }, function () {
            //初始化完毕回调
        	
        });
    });

    //开始菜单磁贴点击
    $('.winui-tile').on('click', function () {
        OpenWindow(this);
    });

    //开始菜单左侧主题按钮点击
    $('.winui-start-item.winui-start-individuation').on('click', function () {
        winui.window.openTheme();
    });

    //打开窗口的方法（可自己根据需求来写）
    function OpenWindow(menuItem) {
        var $this = $(menuItem);
        var url = $this.attr('win-url');
        var title = $this.find(".winui-icon").html()+' '+$this.attr('win-title');
        var id = $this.attr('win-id');
        var type = parseInt($this.attr('win-opentype'));
        var maxOpen = parseInt($this.attr('win-maxopen')) || -1;
        if (url == 'theme') {
            winui.window.openTheme();
            return;
        }
        if (!url || !title || !id) {
            winui.window.msg('菜单配置错误（菜单链接、标题、id缺一不可）');
            return;
        }
        url += "?id="+id;
        var content;
        if (type === 1) {
            $.ajax({
                type: 'get',
                url: url,
                async: false,
                success: function (data) {
                    content = data;
                },
                error: function (e) {
                    $.ajax({
                        type: 'get',
                        url: '/error',
                        async: false,
                        success: function (data) {
                            content = data;
                        },
                        error: function () {
                            layer.close(load);
                        }
                    });
                }
            });
        } else {
            content = url;
        }
        //核心方法（参数请看文档，config是全局配置 open是本次窗口配置 open优先级大于config）
        winui.window.config({
            anim: 0,
            miniAnim: 0,
            maxOpen: -1
        }).open({
            id: id,
            type: type,
            title: title,
            content: content
            //,area: ['70vw','80vh']
            //,offset: ['10vh', '15vw']
            , maxOpen: maxOpen
            //, max: false
            //, min: false
            //, refresh:true
        });
    }

    //注销登录
    $('.logout').on('click', function () {
        winui.hideStartMenu();
        winui.window.confirm('确认注销吗?', { icon: 3, title: '提示' }, function (index) {
            winui.window.msg('执行注销操作，返回登录界面');
            location.href="/"
        });
    });
    
    $(".person-password").on("click", function(){
    	 top.winui.window.open({
             id: 'editPassword',
             type: 2,
             title: '修改密码',
             content: "/home/password/edit",
             area: ['35vw', '60vh'],
             offset: ['15vh', '20vw'],
         });
    });


    // 判断是否显示锁屏（这个要放在最后执行）
    if (window.localStorage.getItem("lockscreen") == "true") {
        winui.lockScreen(true);
    }else{
    	initTimer();
    }
    
    function checkPassword(password){
    	alert($.cookie("pwd"))
    	if (password === $.cookie("pwd")) {
        	initTimer();
            return true;
        } else {
            winui.window.msg('密码错误', { shift: 6 });
            return false;
        }
    }
    
    function initTimer(){
    	timer = setInterval(() => {
			var len = $.cookie('no-action-time');
			if(len === undefined){
				len = 0;
				$.cookie('no-action-time',len);
			}
			if(len >= 300){
				clearInterval(timer);
				winui.lockScreen(true);
			}else{
				len = Number(len)+1;
				$.removeCookie('no-action-time')
				$.cookie('no-action-time',len);
			}
			
		}, 1000);
    }
    //扩展桌面助手工具
    winui.helper.addTool([
        {
            tips: '修改密码',
            icon: 'fa-key',
        	click: function(e) {
        		top.winui.window.open({
                    id: 'editPassword',
                    type: 2,
                    title: '修改密码',
                    content: "/Main/editPassword",
                    area: ['35vw', '60vh'],
                    offset: ['15vh', '20vw'],
                });
        	}
        }, {
            tips: '主题设置',
            icon: 'fa-paw',
            click: function (e) {
                winui.window.openTheme();
            }
        }, {
            tips: '锁屏',
            icon: 'fa-tv',
            click: function (e) {
                winui.lockScreen(true);
            }
        }, {
            tips: '进入全屏',
            icon: 'fa-expand',
            click: function (e) {
            	if(isFullScreen == 0) {
                	winui.fullScreen(document.documentElement);
                	isFullScreen = 1;
                	$(this).find("i").attr("class", "fa fa-fw fa-compress");
                	$(this).data("tips", "退出全屏");
            	} else {
            		winui.exitFullScreen();
                	isFullScreen = 0;
                	$(this).find("i").attr("class", "fa fa-fw fa-expand");
                	$(this).data("tips", "进入全屏");
            	}
            }
        }, {
            tips: '注销登录',
            icon: 'fa fa-power-off',
        	click: function(e) {
        		winui.window.confirm('确认注销吗?', { icon: 3, title: '提示' }, function (index) {
                    winui.window.msg('执行注销操作，返回登录界面');
                    location.href="/"
                });
        	}
        }
        ]);

    exports('index', {});
});