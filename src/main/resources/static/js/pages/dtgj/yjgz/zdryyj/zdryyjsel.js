var mUrlTop = "/api/dtgj/yjgz/zdryyj";
layui.config({
    base: '/js/core/winui/' //指定 winui 路径
}).extend({
    winui: 'winui',
    window: 'js/winui.window'
}).define(['table', 'jquery', 'winui', 'form', 'window', 'laydate'], function (exports) {
    var msg = winui.window.msg;
    var form = layui.form;
    var $ = layui.$;
    var layer = layui.layer;
    var laydate = layui.laydate;

    var rlyjid = $.getUrlParam("id");



    function loadRlyjData(varId) {
        var index = layer.load(1);
        $.ajax({
            type: 'get',
            url: mUrlTop + '/getrlyjbyid',
            headers: { token: localStorage["token"] },
            data: { id: varId },
            dataType: 'json',
            success: function (data) {
                layer.close(index);
                if (data.code == "1") {
                    var item = data.data;
                    loadZdryData(item.ZDRY_GMSFHM);
                    getJgMcByJgdm('yj_gajgjgdm',item.GAJGJGDM);//$('#yj_gajgjgdm').val(item.GAJGJGDM);
                    $('#yj_dtxldm').val(item.DTXLDM);
                    $('#yj_dtzdmc').val(item.DTZDMC);
                    $('#yj_gjxldm').val(item.GJXLDM);
                    $('#yj_zdry_fldm').val(item.ZDRY_FLDM);
                    $('#yj_zdry_xm').val(item.ZDRY_XM);
                    $('#yj_zdry_gmsfhm').val(item.ZDRY_GMSFHM);
                    $('#yj_zdry_zprlzp').attr("src",item.ZDRY_ZPRLZP);//$('#yj_zdry_zprlzp').val(item.ZDRY_ZPRLZP);
                    $('#yj_zdry_bkrlzp').attr("src",item.ZDRY_BKRLZP);//$('#yj_zdry_bkrlzp').val(item.ZDRY_BKRLZP);
                    $('#yj_yjsj').val(item.YJSJ);
                } else {
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

    function loadZdryData(varSfzh) {
        var index = layer.load(1);
        $.ajax({
            type: 'get',
            url: mUrlTop + '/getzdrybysfzh',
            headers: { token: localStorage["token"] },
            data: { sfzh: varSfzh },
            dataType: 'json',
            success: function (data) {
                layer.close(index);
                if (data.code == "1") {
                    var item = data.data;
                    getCsmcByBm('zd_xzqhdm', 'XZQH', item.XZQHDM);//$('#zd_xzqhdm').val(item.XZQHDM);
                    $('#zd_zdry_gmsfhm').val(item.ZDRY_GMSFHM);
                    $('#zd_zdry_xm').val(item.ZDRY_XM);
                    getCsmcByBm('zd_zdry_zzmmdm', 'ZZMM', item.ZDRY_ZZMMDM);//$('#zd_zdry_zzmmdm').val(item.ZDRY_ZZMMDM);
                    $('#zd_zdry_zy').val(item.ZDRY_ZY);
                    getCsmcByBm('zd_zdry_xbdm', 'XB', item.ZDRY_XBDM);//$('#zd_zdry_xbdm').val(item.ZDRY_XBDM);
                    $('#zd_zdry_csrq').val(item.ZDRY_CSRQ);
                    getCsmcByBm('zd_zdry_xldm', 'WHCD', item.ZDRY_XLDM);//$('#zd_zdry_xldm').val(item.ZDRY_XLDM);
                    getCsmcByBm('zd_zdry_mzdm', 'MZ', item.ZDRY_MZDM);//$('#zd_zdry_mzdm').val(item.ZDRY_MZDM);
                    $('#zd_zdry_dzmc').val(item.ZDRY_DZMC);
                    $('#zd_zdry_zdrflbm').val(item.ZDRY_ZDRFLBM);
                    $('#zd_zdry_zp').attr("src",item.ZDRY_ZP);//$('#zd_zdry_zp').val(item.ZDRY_ZP);
                    $('#zd_ajlbdm').val(item.AJLBDM);
                    $('#zd_ajbh').val(item.AJBH);
                    $('#zd_jyaq').val(item.JYAQ);
                    $('#zd_lgxx_lgrlxdh').val(item.LGXX_LGRLXDH);
                    $('#zd_lgxx_lgdw').val(item.LGXX_LGDW);
                    $('#zd_lgxx_lgsj').val(item.LGXX_LGSJ);
                    $('#zd_tary_xm').val(item.TARY_XM);
                    $('#zd_tary_gmsfhm').val(item.TARY_GMSFHM);
                } else {
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


    function getCsmcByBm(varId, varTid, varBm) {
        var index = layer.load(1);
        $.ajax({
            type: 'get',
            url: '/api/dtgj/com/getcsmcbybm',
            headers: { token: localStorage["token"] },
            data: { tid: varTid, bm: varBm },
            dataType: 'json',
            success: function (data) {
                layer.close(index);
                $('#' + varId).val(data.data);
            }
        });
    }

    function getJgMcByJgdm(varId, varJgdm) {
        var index = layer.load(1);
        $.ajax({
            type: 'get',
            url: '/api/dtgj/com/getjgmcbyjgdm',
            headers: { token: localStorage["token"] },
            data: { jgdm: varJgdm },
            dataType: 'json',
            success: function (data) {
                layer.close(index);
                $('#' + varId).val(data.data);
            }
        });
    }

    loadRlyjData(rlyjid);



    exports('zdryyjsel', {});
});
