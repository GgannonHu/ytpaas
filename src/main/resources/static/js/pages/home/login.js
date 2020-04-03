/**
 * 登录页面
 */

function onLoginClick() {
	var username = $("#username").val();
	var password = $("#password").val();
	if (username.length == 0) {
		$(".log-msg").html("<span><font color='#fff'>请输入用户名！</font></span>");
		return;
	}
	if (password.length == 0) {
		$(".log-msg").html("<span><font color='#fff'>请输入密码！</font></span>");
		return;
	}
	$.ajax({
        type: "post",
        async: true,
        data: {"loginid": username, "password": password},
        url: "/api/home/login",
        dataType: "json",
        beforeSend: function () {
        	$(".log-msg").html("<span class='text-muted'>登录中...</span>");
        },
        success: function (data) {
        	
        	if(data && data.code == "1"){//登录成功
				window.localStorage.setItem("lockscreen", false);
				window.localStorage.setItem("token", data.data.token);
				settings();
        	}else{
        		$(".log-msg").html("<span class='text-danger'>"+data.msg+"</span>");
        	}
        	
        },
        error: function (e) {
        	$(".log-msg").html("<span class='text-danger'>登录失败，服务器连接失败，请联系管理员！</span>");
        }
    });		
}

document.onkeydown = function (e) { // 回车提交表单
	// 兼容FF和IE和Opera
    var theEvent = window.event || e;
    var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
    if (code == 13) {
    	onLoginClick();
    }
}

if(window != top)
	top.location.href = location.href;

function settings() {
	$.ajax({
		url: "/api/home/getUserSettingsByToken",
		method: 'GET',
		data:{'token': localStorage["token"]},
		dataType:"JSON",
		success: function () {
			location.href="/index";
		},
		error: function() {
        	$(".log-msg").html("<span class='text-danger'>登录失败，服务器连接失败，请联系管理员！</span>");
		}
	});
}