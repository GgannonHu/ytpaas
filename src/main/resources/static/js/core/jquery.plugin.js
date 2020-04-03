
(function($){
  
    /**
     * 获取url参数
     */
    $.getUrlParam = function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null)
            return decodeURI(r[2]);
  
        return "";
    }
  
    /**
     * 退出登录，清空token
     */
    $.logout = function() {
        localStorage.removeItem("token");
        location.href = "/";
    }
  })(jQuery)
  
  