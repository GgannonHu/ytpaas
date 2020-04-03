layui.config({
    base: '/js/core/winui/' //指定 winui 路径
}).extend({
    winui: 'winui',
    window: 'js/winui.window'
}).define(['table', 'jquery', 'winui','form','window'], function (exports) {
    var msg = winui.window.msg,
        $ = layui.$;
        var gjgwcyryid = $.getUrlParam("id");
    if(gjgwcyryid) {
        $.ajax({
            type: 'get',
            url: '/api/dtgj/gjgwcyry/getById',
            headers: {token: localStorage["token"]},
            data: {id: gjgwcyryid},
            dataType:'json',
            success: function (data) {
                if(data.code == "1"){
                    $("#gjgwcyryName").val(data.data.GJGWCYRY_XM);
                    $("#gjgwcyryIdcard").val(data.data.GJGWCYRY_GMSFZH);
                    $("#gjgwcyryDwmc").val(data.data.GJGWCYRY_DWMC);
                    $("#gjgwcyryQybm").val(data.data.GJGWCYRY_QYBM);
                    $("#gjgwcyryGwmc").val(data.data.GJGWCYRY_GWMC);
                    $("#gjgwcyryLxdh").val(data.data.GJGWCYRY_LXDH);
                    $("#gjgwcyryDzmc").val(data.data.GJGWCYRY_DZMC);
                    $("#gjgwcyryWffz").val(data.data.GJGWCYRY_WFFZJLMS);

                    $("#gjgwcyryMz").val(data.data.GJGWCYRY_MZDM);
                }else{
                    msg('数据加载失败，请重试', {
                        icon: 2,
                        time: 1000
                    });
                }
            },
            error: function (xml) {
                msg('数据加载失败，请重试', {
                    icon: 2,
                    time: 1000
                });
            }
        });
    }
    exports('gjgwcyryedit', {});
});