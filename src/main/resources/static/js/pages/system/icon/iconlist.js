layui.config({
    base: '/js/core/' //指定 winui 路径
    , version: '1.0.0-beta'
}).extend({
    winui: 'winui/winui',
    treetable:'treetable/treetable',
    window: 'winui/js/winui.window'
}).define(['table', 'jquery', 'winui', 'window', 'layer'], function (exports) {
    winui.renderColor();

    $ = layui.$, tableId = 'menuTable';
    
    var parentForm = $.getUrlParam("menuid");
    var txtid = $.getUrlParam("txtid");
    var showid = $.getUrlParam("showid");
    var thisForm = "selectIcon";
    
    $("#icons a").click(function() {
    	var icon = $(this).find("i").attr("class");
    	
    	var topWindow = top.winui.window.getWindow(parentForm);
    	$(topWindow).find("#" + txtid).val(icon.replace("fa ", ""));
    	$(topWindow).find("#" + showid).attr("class", icon);
        setTimeout(() => {
            top.winui.window.close(thisForm);
        }, 100);
    });
    
    exports('iconlist', {});
});

