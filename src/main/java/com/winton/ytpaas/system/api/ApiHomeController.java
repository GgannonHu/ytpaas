package com.winton.ytpaas.system.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.winton.ytpaas.common.configuration.jwt.PassToken;
import com.winton.ytpaas.common.configuration.jwt.TokenService;
import com.winton.ytpaas.common.configuration.log.LogType;
import com.winton.ytpaas.common.configuration.log.SystemLog;
import com.winton.ytpaas.common.util.PassWord;
import com.winton.ytpaas.common.util.Result;
import com.winton.ytpaas.common.util.Tools;
import com.winton.ytpaas.system.entity.Sys_User;
import com.winton.ytpaas.system.service.Sys_MenuService;
import com.winton.ytpaas.system.service.Sys_UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/home")
@Api(value = "首页常用接口", description = "首页常用接口")
public class ApiHomeController {
    @Autowired
    TokenService tokenService;
    @Autowired
    Sys_UserService sysUserService;
    @Autowired
    Sys_MenuService sysMenuService;

    @ApiOperation(value = "登录", notes = "登录", httpMethod = "GET")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "loginid", value = "用户名", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "password", value = "密码", dataType = "String", paramType = "query", required = true)
    })
    @PassToken
    @RequestMapping(value = "/login", produces="application/json")
    @SystemLog(description = "登录", type = LogType.LOGIN)
    public String login(HttpServletRequest request, HttpServletResponse response) {
        String loginid = request.getParameter("loginid");
        String password = request.getParameter("password");

        Result res = sysUserService.login(loginid, password);

        return res.toString();
    }

    @ApiOperation(value = "根据token获取用户信息", notes = "根据token获取用户信息", httpMethod = "GET")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "query", required = true)
    })
    @PassToken
    @RequestMapping(value = "/getUserByToken", produces="application/json")
    @SystemLog(description = "根据token获取用户信息", type = LogType.API)
    public String getUserByToken(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getParameter("token");
        Sys_User user = tokenService.verifyToken(token);
        user.setPASSWORD("");
        return Tools.toJSONString(user);
    }

    @ApiOperation(value = "根据token获取用户配置信息", notes = "根据token获取用户配置信息", httpMethod = "GET")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "query", required = true)
    })
    @PassToken
    @RequestMapping(value = "/getUserSettingsByToken", produces="application/json")
    @SystemLog(description = "根据token获取用户配置信息", type = LogType.API)
    public String getUserSettingsByToken(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getParameter("token");
        Sys_User user = tokenService.verifyToken(token);
        user.setPASSWORD("");
        JSONObject json = new JSONObject();
        json.put("color", 31);
        json.put("bgSrc", "/images/bg_01.jpg");
        json.put("lockBgSrc", "/images/bg_01.jpg");
        
        Tools.addCookie(response, "settings", json.toJSONString(), 24 * 60);
        return Tools.toJSONString(user);
    }

    
    @ApiOperation(value = "解锁屏幕", notes = "解锁屏幕", httpMethod = "GET")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "password", value = "解锁密码", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "query", required = true)
    })
    @RequestMapping(value = "/openLock", produces="application/json")
    @SystemLog(description = "解锁屏幕", type = LogType.SYSTEM_OPERATION)
    public Map<String, Object> openLock(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("token");
        String password = request.getParameter("password");
        Sys_User user = tokenService.verifyToken(token);
        
		Map<String, Object> map = new HashMap<>();
        if(PassWord.md5(password).equals(user.getPASSWORD())) {
            map.put("success", true);
		}else {
			map.put("success", false);
		}
        return map;
    }


    @ApiOperation(value = "修改密码", notes = "修改密码", httpMethod = "POST")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "pwd1", value = "原密码", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "pwd2", value = "新密码", dataType = "String", paramType = "query", required = true)
    })
    @RequestMapping(value = "/userinfo/pwd", produces="application/json")
    @SystemLog(description = "修改密码", type = LogType.SYSTEM_OPERATION)
    public String updatePwd(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("token");
        Sys_User user = tokenService.verifyToken(token);
        
        String yhbm = user.getYHBM();
        String pwd1 = request.getParameter("pwd1");
        String pwd2 = request.getParameter("pwd2");

        Result res = sysUserService.updatePassword(yhbm, pwd1, pwd2);
        return res.toString();
    }


    @ApiOperation(value = "获取用户菜单权限", notes = "获取用户菜单权限", httpMethod = "GET")
    @RequestMapping(value = "/menu/desktop")
    public String getMenu(HttpServletRequest request, HttpServletResponse response){
        String token = request.getHeader("token");
        Sys_User user = tokenService.verifyToken(token);

        String basePath = request.getContextPath();
        
        String res = sysMenuService.getUserMenuAuth(basePath, user.getJSBM());

        return res;
    }


    @ApiOperation(value = "获取子系统用户菜单", notes = "\"获取子系统用户菜单", httpMethod = "GET")
    @RequestMapping(value = "/menu/subsystem")
    public String getSubsystemMenu(HttpServletRequest request, HttpServletResponse response){
        String token = request.getHeader("token");
        Sys_User user = tokenService.verifyToken(token);
        String pid = request.getParameter("pid");
        String basePath = request.getContextPath();
        
        String res = sysMenuService.getUserMenuAuth(basePath, user.getJSBM(), pid);

        return res;
    }
    
    @ApiOperation(value = "获取子系统用户菜单树", notes = "\"获取子系统用户菜单树", httpMethod = "GET")
    @RequestMapping(value = "/menu/subsystem/tree")
    public String getSubsystemMenuTreeData(HttpServletRequest request, HttpServletResponse response){
        String token = request.getHeader("token");
        Sys_User user = tokenService.verifyToken(token);
        String pid = request.getParameter("pid");
        String basePath = request.getContextPath();
        
        String res = sysMenuService.getUserMenuAuthTree(basePath, user.getJSBM(), pid);

        return res;
    }
    

}