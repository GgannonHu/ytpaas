package com.winton.ytpaas.system.api;

import java.util.Date;

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
import com.winton.ytpaas.system.entity.Sys_Menu;
import com.winton.ytpaas.system.entity.Sys_Role;
import com.winton.ytpaas.system.entity.Sys_User;
import com.winton.ytpaas.system.service.Sys_JGService;
import com.winton.ytpaas.system.service.Sys_MenuService;
import com.winton.ytpaas.system.service.Sys_RoleService;
import com.winton.ytpaas.system.service.Sys_UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/system")
@Api(value = "系统管理接口", description = "系统管理接口")
public class ApiSystemController {
    @Autowired    Sys_UserService sysUserService;
    @Autowired    Sys_RoleService sysRoleService;
    @Autowired    Sys_JGService sysJGService;
    @Autowired    Sys_MenuService sysMenuService;
    @Autowired    TokenService tokenService;


    @ApiOperation(value = "获取用户当前页的数据权限", notes = "获取用户当前页的数据权限", httpMethod = "GET")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "url", value = "页面链接", dataType = "String", paramType = "query", required = true)
    })
    @RequestMapping(value = "/sjqx",produces="application/json")
    public String sjqx(HttpServletRequest request,HttpServletResponse response){
    	String token = request.getHeader("token");
    	String url = request.getParameter("url");
    	Sys_User user = tokenService.verifyToken(token);
    	
        Result res = sysRoleService.getSJQX(user, url);
        
        return res.toString();
    }
    @ApiOperation(value = "获取用户单位数据权限", notes = "获取用户单位数据权限", httpMethod = "GET")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "yhbm", value = "用户编码", dataType = "String", paramType = "query", required = true)
    })
    @RequestMapping(value = "/user/qxdw",produces="application/json")
    public String yhDwqx(HttpServletRequest request,HttpServletResponse response){
    	String yhbm = request.getParameter("yhbm");
    	
        Result res = sysRoleService.getQXDW(yhbm);
        
        return res.toString();
    }

    @ApiOperation(value = "获取用户列表", notes = "获取用户列表", httpMethod = "GET")
    @RequestMapping(value = "/user/list",produces="application/json")
    public String userList(HttpServletRequest request,HttpServletResponse response){
    	int page = Integer.parseInt(request.getParameter("PageNo"));
        int limit = Integer.parseInt(request.getParameter("PageSize"));
        String jgdm = request.getParameter("jgdm");
        
        JSONObject where = new JSONObject();
        where.put("jgdm", jgdm);
        
        Result res = sysUserService.getAll(where, page, limit);
        return res.toString();
    }
    @PassToken
    @ApiOperation(value = "获取用户列表总数", notes = "获取用户列表总数", httpMethod = "GET")
    @RequestMapping(value = "/user/list/count",produces="application/json")
    public String userListCount(HttpServletRequest request,HttpServletResponse response){
    	String jgdm = request.getParameter("jgdm");
        
        JSONObject where = new JSONObject();
        where.put("jgdm", jgdm);
        
        Result res = sysUserService.getAllCount(where);
        return res.toString();
    }
    @ApiOperation(value = "根据id获取用户列表", notes = "根据id获取用户列表", httpMethod = "GET")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "用户名", dataType = "String", paramType = "query", required = true)
    })
    @RequestMapping(value = "/user/getById",produces="application/json")
    public String getUserById(HttpServletRequest request,HttpServletResponse response){
        String yhbm = request.getParameter("id");
        Result res = sysUserService.getById(yhbm);
        return res.toString();
    }


    @ApiOperation(value = "添加用户", notes = "添加用户", httpMethod = "POST")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "LOGINNAME", value = "用户名", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "PASSWORD", value = "密码", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "YHXM", value = "姓名", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "YHSFZH", value = "身份证号", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "YHLXDH", value = "联系电话", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "YHJH", value = "警号", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "DWDM", value = "单位编码", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "DWMC", value = "单位名称", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "JSBM", value = "角色编码", dataType = "String", paramType = "query", required = true)
    })
    @RequestMapping(value="/user/add",produces = "application/json")
    @SystemLog(description = "添加用户", type = LogType.SYSTEM_OPERATION)
    public String sys_UserAdd(HttpServletRequest request,HttpServletResponse response)
    {
    	String token = request.getHeader("token");
    	String yhbm = tokenService.verifyToken(token).getYHBM();
    	
        Date date = new Date();
        Sys_User user = new Sys_User();
        
        user.setLOGINNAME(request.getParameter("LOGINNAME"));
        user.setPASSWORD(PassWord.md5(request.getParameter("PASSWORD")));
        user.setYHXM(request.getParameter("YHXM"));
        user.setYHSFZH(request.getParameter("YHSFZH"));
        user.setYHLXDH(request.getParameter("YHLXDH"));
        user.setYHJH(request.getParameter("YHJH"));
        user.setDWDM(request.getParameter("DWDM"));
        user.setDWMC(request.getParameter("DWMC"));
        user.setJSBM(request.getParameter("JSBM"));
        
        user.setCJYH(yhbm);
        user.setCJSJ(date);
        
        Result res = sysUserService.add(user);
        return res.toString();
    }


    @ApiOperation(value = "修改用户", notes = "修改用户", httpMethod = "POST")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "LOGINNAME", value = "用户名", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "YHXM", value = "姓名", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "YHSFZH", value = "身份证号", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "YHLXDH", value = "联系电话", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "YHJH", value = "警号", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "DWDM", value = "单位编码", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "DWMC", value = "单位名称", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "JSBM", value = "角色编码", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "YHBM", value = "用户编码", dataType = "String", paramType = "query", required = true)
    })
    @RequestMapping(value="/user/update",produces = "application/json")
    @SystemLog(description = "修改用户", type = LogType.SYSTEM_OPERATION)
    public String sys_UserUpdate(HttpServletRequest request,HttpServletResponse response)
    {
    	String token = request.getHeader("token");
    	String yhbm = tokenService.verifyToken(token).getYHBM();
    	
        Date date = new Date();
        Sys_User user = new Sys_User();
        
        user.setLOGINNAME(request.getParameter("LOGINNAME"));
        user.setYHXM(request.getParameter("YHXM"));
        user.setYHSFZH(request.getParameter("YHSFZH"));
        user.setYHLXDH(request.getParameter("YHLXDH"));
        user.setYHJH(request.getParameter("YHJH"));
        user.setDWDM(request.getParameter("DWDM"));
        user.setDWMC(request.getParameter("DWMC"));
        user.setJSBM(request.getParameter("JSBM"));
        user.setYHBM(request.getParameter("YHBM"));
        
        user.setXGYH(yhbm);
        user.setXGSJ(date);
        
        Result res = sysUserService.update(user);
        return res.toString();
    }

    @ApiOperation(value = "删除用户", notes = "删除用户", httpMethod = "GET")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "用户id", dataType = "String", paramType = "query", required = true)
    })
    @RequestMapping(value="/user/delete",produces = "application/json")
    @SystemLog(description = "删除用户", type = LogType.SYSTEM_OPERATION)
    public String sys_UserDelete(HttpServletRequest request,HttpServletResponse response)
    {
        String id = request.getParameter("ids");

        Result res = sysUserService.delete(id);
        return res.toString();
    }
    @ApiOperation(value = "设置用户的权限单位", notes = "设置用户的权限单位", httpMethod = "GET")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "yhbm", value = "用户编码", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "qxdw", value = "权限单位", dataType = "String", paramType = "query", required = true)
    })
    @RequestMapping(value="/user/datadw/update",produces = "application/json")
    @SystemLog(description = "设置用户的权限单位", type = LogType.SYSTEM_OPERATION)
    public String sys_UserQXDWUpdate(HttpServletRequest request,HttpServletResponse response)
    {
        String yhbm = request.getParameter("yhbm");
        String qxdw = request.getParameter("qxdw");

        Result res = sysUserService.updateQXDW(yhbm, qxdw);
        return res.toString();
    }
    
    

    @ApiOperation(value = "机构管理", notes = "机构管理", httpMethod = "GET")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "sjqx", value = "数据权限", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "jgdm", value = "机构代码", dataType = "String", paramType = "query", required = true)
    })
	@RequestMapping(value="/jg/jggl",produces = "application/json")
    @SystemLog(description = "机构管理", type = LogType.SYSTEM_OPERATION)
    public String sys_JGGL(HttpServletRequest request,HttpServletResponse response)
    {
        String sjqx = request.getParameter("sjqx");
        String jgdm = request.getParameter("jgdm");
        
    	JSONObject res = sysJGService.getAll(sjqx, jgdm);
    	
        return Tools.toJSONString(res);
    }
    @ApiOperation(value = "获取用户的权限机构", notes = "获取用户的权限机构", httpMethod = "GET")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "sjqx", value = "数据权限", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "jgdm", value = "机构代码", dataType = "String", paramType = "query", required = true)
    })
	@RequestMapping(value="/jg/qxjg",produces = "application/json")
    @SystemLog(description = "获取用户的权限机构", type = LogType.SYSTEM_OPERATION)
    public String sys_JGGL_QXJG(HttpServletRequest request,HttpServletResponse response)
    {
        String sjqx = request.getParameter("sjqx");
        String jgdm = request.getParameter("jgdm");
        
    	JSONObject res = sysJGService.getYJQX(sjqx, jgdm);
    	
        return Tools.toJSONString(res);
    }
    @ApiOperation(value = "获取子机构", notes = "获取子机构", httpMethod = "GET")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "jgdm", value = "机构代码", dataType = "String", paramType = "query", required = true)
    })
	@RequestMapping(value="/jg/zjg",produces = "application/json")
    @SystemLog(description = "获取子机构", type = LogType.SYSTEM_OPERATION)
    public String sys_JGGL_ZJG(HttpServletRequest request,HttpServletResponse response)
    {
        String jgdm = request.getParameter("jgdm");
        
    	JSONObject res = sysJGService.getBySjdm(jgdm);
    	
        return Tools.toJSONString(res);
    }
    @ApiOperation(value = "根据机构编码获取机构名称", notes = "根据机构编码获取机构名称", httpMethod = "GET")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "jgdm", value = "机构代码", dataType = "String", paramType = "query", required = true)
    })
	@RequestMapping(value="/jg/jgmc/get",produces = "application/json")
    @SystemLog(description = "根据机构编码获取机构名称", type = LogType.SYSTEM_OPERATION)
    public String sys_JG_GetJGMC(HttpServletRequest request,HttpServletResponse response)
    {
    	String jgdm = request.getParameter("jgdm");
        String jgmc = sysJGService.getByJgmc(jgdm);
    	
        return jgmc;
    }

    @ApiOperation(value = "获取下级角色列表", notes = "获取下级角色列表", httpMethod = "GET")
	@RequestMapping(value="/role/list",produces = "application/json")
    @SystemLog(description = "获取下级角色列表", type = LogType.SYSTEM_OPERATION)
    public String sys_RoleList(HttpServletRequest request,HttpServletResponse response)
    {
    	String token = request.getHeader("token");
    	Sys_User user = tokenService.verifyToken(token);
    	
    	Result res = sysRoleService.getXJJS(user.getJSBM());
    	
        return Tools.toJSONString(res);
    }
    @ApiOperation(value = "添加角色", notes = "添加角色", httpMethod = "GET")
	@RequestMapping(value="/role/add",produces = "application/json")
    @SystemLog(description = "添加角色", type = LogType.SYSTEM_OPERATION)
    public String sys_RoleAdd(HttpServletRequest request,HttpServletResponse response)
    {
    	String token = request.getHeader("token");
    	Sys_User user = tokenService.verifyToken(token);
    	
    	Sys_Role item = new Sys_Role();
    	item.setJSMC(request.getParameter("JSMC"));
    	item.setJSMS(request.getParameter("JSMS"));
    	item.setJSLX(request.getParameter("JSLX"));
    	item.setCJYH(user.getYHBM());
    	item.setCJSJ(new Date());
    	item.setBZ("");
    	
    	Result res = sysRoleService.add(item);
    	
        return Tools.toJSONString(res);
    }
    @ApiOperation(value = "修改角色", notes = "修改角色", httpMethod = "GET")
	@RequestMapping(value="/role/update",produces = "application/json")
    @SystemLog(description = "修改角色", type = LogType.SYSTEM_OPERATION)
    public String sys_RoleUpdate(HttpServletRequest request,HttpServletResponse response)
    {
    	String token = request.getHeader("token");
    	Sys_User user = tokenService.verifyToken(token);
    	
    	Sys_Role item = new Sys_Role();
    	item.setJSBM(request.getParameter("JSBM"));
    	item.setJSMC(request.getParameter("JSMC"));
    	item.setJSMS(request.getParameter("JSMS"));
    	item.setXGYH(user.getYHBM());
    	item.setXGSJ(new Date());

    	Result res = sysRoleService.update(item);
    	
        return Tools.toJSONString(res);
    }
    @ApiOperation(value = "删除角色", notes = "删除角色", httpMethod = "GET")
	@RequestMapping(value="/role/delete",produces = "application/json")
    @SystemLog(description = "删除角色", type = LogType.SYSTEM_OPERATION)
    public String sys_RoleDelete(HttpServletRequest request,HttpServletResponse response)
    {
    	String jsbm = request.getParameter("JSBM");

    	Result res = sysRoleService.delete(jsbm);
    	
        return Tools.toJSONString(res);
    }
    @ApiOperation(value = "根据角色编码获取角色", notes = "根据角色编码获取角色", httpMethod = "GET")
	@RequestMapping(value="/role/getById",produces = "application/json")
    @SystemLog(description = "根据角色编码获取角色", type = LogType.SYSTEM_OPERATION)
    public String sys_RoleGetById(HttpServletRequest request,HttpServletResponse response)
    {
    	String jsbm = request.getParameter("id");

    	Sys_Role res = sysRoleService.getById(jsbm);
    	
        return Tools.toJSONString(res);
    }
    @ApiOperation(value = "设置功能权限", notes = "设置功能权限", httpMethod = "GET")
	@RequestMapping(value="/role/save/gnqx",produces = "application/json")
    @SystemLog(description = "设置功能权限", type = LogType.SYSTEM_OPERATION)
    public String sys_RoleSaveGNQX(HttpServletRequest request,HttpServletResponse response)
    {
    	String jsbm = request.getParameter("jsbm");
    	String cdbm = request.getParameter("cdbm");

    	Result res = sysRoleService.saveGNQX(jsbm, cdbm);
    	
        return Tools.toJSONString(res);
    }
    @ApiOperation(value = "获取功能权限菜单", notes = "获取功能权限菜单", httpMethod = "GET")
	@RequestMapping(value="/role/qxcd",produces = "application/json")
    @SystemLog(description = "获取功能权限菜单", type = LogType.SYSTEM_OPERATION)
    public String sys_RoleQxcd(HttpServletRequest request,HttpServletResponse response)
    {
    	String jsbm = request.getParameter("jsbm");

    	Result res = sysMenuService.getMyGNQX(jsbm);
    	
        return Tools.toJSONString(res);
    }
    @ApiOperation(value = "设置数据权限", notes = "设置数据权限", httpMethod = "GET")
	@RequestMapping(value="/role/save/sjqx",produces = "application/json")
    @SystemLog(description = "设置数据权限", type = LogType.SYSTEM_OPERATION)
    public String sys_RoleSaveSJQX(HttpServletRequest request,HttpServletResponse response)
    {
    	String jsbm = request.getParameter("jsbm");
    	String type = request.getParameter("type"); // 1 设置全部，2 设置具体
    	String sjqx = request.getParameter("sjqx");
    	String cdurl = request.getParameter("cdurl");
    	
    	Result res = sysRoleService.saveSJQX(jsbm, cdurl, type, sjqx);
    	
        return Tools.toJSONString(res);
    }
    
    

    @ApiOperation(value = "菜单管理（树形json）", notes = "菜单管理（树形json）", httpMethod = "GET")
	@RequestMapping(value="/menu/treelist",produces = "application/json")
    @SystemLog(description = "菜单管理（树形json）", type = LogType.SYSTEM_OPERATION)
    public String sys_MenuTreeList(HttpServletRequest request,HttpServletResponse response)
    {
    	Result res = sysMenuService.getAllTree();
        return Tools.toJSONString(res);
    }
    @ApiOperation(value = "功能权限", notes = "功能权限", httpMethod = "GET")
	@RequestMapping(value="/menu/gnqx",produces = "application/json")
    @SystemLog(description = "功能权限", type = LogType.SYSTEM_OPERATION)
    public String sys_MenuGNQX(HttpServletRequest request,HttpServletResponse response)
    {
    	String jsbm = request.getParameter("jsbm");
    	
    	Result res = sysMenuService.getAllGNQX(jsbm);
        return Tools.toJSONString(res);
    }
    @ApiOperation(value = "添加菜单", notes = "添加菜单", httpMethod = "GET")
	@RequestMapping(value="/menu/add",produces = "application/json")
    @SystemLog(description = "添加菜单", type = LogType.SYSTEM_OPERATION)
    public String sys_MenuAdd(HttpServletRequest request,HttpServletResponse response)
    {
    	String token = request.getHeader("token");
    	Sys_User user = tokenService.verifyToken(token);
    	
    	Sys_Menu item = new Sys_Menu();
    	item.setCDMC(request.getParameter("CDMC"));
    	item.setSJBM(request.getParameter("SJBM"));
    	item.setURL(request.getParameter("URL"));
    	item.setPX(request.getParameter("PX"));
    	item.setBZ(request.getParameter("BZ"));
    	item.setCJYH(user.getYHBM());
    	item.setCJSJ(new Date());
    	
    	Result res = sysMenuService.add(item);
        return Tools.toJSONString(res);
    }
    @ApiOperation(value = "修改菜单", notes = "修改菜单", httpMethod = "GET")
	@RequestMapping(value="/menu/update",produces = "application/json")
    @SystemLog(description = "修改菜单", type = LogType.SYSTEM_OPERATION)
    public String sys_MenuUpdate(HttpServletRequest request,HttpServletResponse response)
    {
    	String token = request.getHeader("token");
    	Sys_User user = tokenService.verifyToken(token);
    	
    	Sys_Menu item = new Sys_Menu();
    	item.setCDBM(request.getParameter("CDBM"));
    	item.setCDMC(request.getParameter("CDMC"));
    	item.setURL(request.getParameter("URL"));
    	item.setPX(request.getParameter("PX"));
    	item.setBZ(request.getParameter("BZ"));
    	item.setXGYH(user.getYHBM());
    	item.setXGSJ(new Date());
    	
    	Result res = sysMenuService.update(item);
        return Tools.toJSONString(res);
    }
    @ApiOperation(value = "删除菜单", notes = "删除菜单", httpMethod = "GET")
	@RequestMapping(value="/menu/delete",produces = "application/json")
    @SystemLog(description = "删除菜单", type = LogType.SYSTEM_OPERATION)
    public String sys_MenuDelete(HttpServletRequest request,HttpServletResponse response)
    {
    	String cdbm = request.getParameter("CDBM");
    	
    	Result res = sysMenuService.delete(cdbm);
        return Tools.toJSONString(res);
    }
    
}