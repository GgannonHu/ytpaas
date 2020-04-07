package com.winton.ytpaas.dtgj.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.winton.ytpaas.common.configuration.jwt.PassToken;
import com.winton.ytpaas.common.configuration.jwt.TokenService;
import com.winton.ytpaas.common.configuration.log.LogType;
import com.winton.ytpaas.common.configuration.log.SystemLog;
import com.winton.ytpaas.common.util.Result;
import com.winton.ytpaas.common.util.Tools;
import com.winton.ytpaas.dtgj.entity.Dtgj_Jqxx;
import com.winton.ytpaas.dtgj.service.Dtgj_JqxxService;
import com.winton.ytpaas.system.entity.Sys_User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/dtgj/xxgl/jqxx")
@Api(value = "警情信息", description = "警情信息接口")
public class ApiJqxxController {
    @Autowired
    Dtgj_JqxxService dtgjJqxxService;
    @Autowired
    TokenService tokenService;

    @ApiOperation(value = "获取警情信息列表", notes = "获取警情信息列表", httpMethod = "GET")
    @RequestMapping(value = "/list", produces = "application/json")
    public String JqxxList(HttpServletRequest request, HttpServletResponse response) {
        int page = Integer.parseInt(request.getParameter("page"));// PageNo
        int limit = Integer.parseInt(request.getParameter("limit"));// PageSize
        int count = Integer.parseInt(request.getParameter("count"));// PageSize
        int iscon = Integer.parseInt(request.getParameter("iscon"));
 
        String GAJGJGDM = request.getParameter("GAJGJGDM");
        String DTZDMC = request.getParameter("DTZDMC");
        String BJR_XM = request.getParameter("BJR_XM");
        String BJSJS = request.getParameter("BJSJS");
        String BJSJE = request.getParameter("BJSJE");
        String BJDH = request.getParameter("BJDH"); 
         
        JSONObject res = dtgjJqxxService.getList(GAJGJGDM,DTZDMC,BJR_XM,BJSJS,BJSJE,BJDH, page, limit, iscon);
        if (iscon != 1) {
            res.put("count", count);
        }
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "获取警情信息列表总数", notes = "获取警情信息列表总数", httpMethod = "GET")
    @RequestMapping(value = "/listcon", produces = "application/json")
    public String Jqxxcon(HttpServletRequest request, HttpServletResponse response) {
        String GAJGJGDM = request.getParameter("GAJGJGDM");
        String DTZDMC = request.getParameter("DTZDMC");
        String BJR_XM = request.getParameter("BJR_XM");
        String BJSJS = request.getParameter("BJSJS");
        String BJSJE = request.getParameter("BJSJE");
        String BJDH = request.getParameter("BJDH"); 
        JSONObject res = dtgjJqxxService.getListcon(GAJGJGDM,DTZDMC,BJR_XM,BJSJS,BJSJE,BJDH);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }
    
    @ApiOperation(value = "根据id获取警情信息列表", notes = "根据id获取警情列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "警情id", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/getById", produces = "application/json")
    public String getJqxxById(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        Result res = dtgjJqxxService.getById(id);
        return res.toString();
    }

    @ApiOperation(value = "添加警情", notes = "添加警情", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginId", value = "警情名", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "yswpName", value = "昵称", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/add", produces = "application/json")
    @SystemLog(description = "添加警情", type = LogType.SYSTEM_OPERATION)
    public String Dtgj_JqxxAdd(HttpServletRequest request, HttpServletResponse response) throws ParseException
    {
        Dtgj_Jqxx Jqxx = new Dtgj_Jqxx();
        Date date=null;
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sj=request.getParameter("Jqxx_BJSJ");
        if (sj != null) {
            date = formatter.parse(sj);
        } 
        String token = request.getHeader("token");
        Sys_User user = tokenService.verifyToken(token);
        Jqxx.setGAJGJGDM(user.getDWDM());  //  公安机关机构代码 
        Jqxx.setDTZDDM(request.getParameter("Jqxx_DTZDDM"));//  地铁站点代码 
        Jqxx.setDTZDMC(request.getParameter("Jqxx_DTZDMC"));// 地铁站点名称 
        Jqxx.setGJXLDM(request.getParameter("Jqxx_GJXLDM"));// 公交线路代码 
        Jqxx.setJJBH(request.getParameter("Jqxx_JJBH"));// 接警编号 
        Jqxx.setBJFSDM(request.getParameter("Jqxx_BJFSDM"));// 报警方式代码 
        Jqxx.setJQLBDM(request.getParameter("Jqxx_JQLBDM"));// 警情类别代码 
        Jqxx.setBJR_XM(request.getParameter("Jqxx_BJR_XM"));// 姓名 
        Jqxx.setBJR_XBDM(request.getParameter("Jqxx_BJR_XBDM"));// 性别代码 
        Jqxx.setBJR_LXDH(request.getParameter("Jqxx_BJR_LXDH"));// 联系电话 
        Jqxx.setBJDH(request.getParameter("Jqxx_BJDH"));// 报警电话 
        Jqxx.setBJSJ(date);// 报警时间 
        Jqxx.setJYJQ(request.getParameter("Jqxx_JYJQ"));// 简要警情 
        Jqxx.setTJR(user.getLOGINNAME());//  ssxq        提交人   
        Jqxx.setTJDW(user.getDWDM());//  ssxq        提交人单位   
        Jqxx.setTJDWMC(user.getDWMC());//  ssxq        提交人单位名称    
        
        Result res = dtgjJqxxService.add(Jqxx);
        return res.toString();
    }

    @ApiOperation(value = "修改警情", notes = "修改警情", httpMethod = "POST")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "yswpName", value = "昵称", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "id", value = "遗失物品id", dataType = "Integer", paramType = "query", required = true)
    })
    @RequestMapping(value="/update",produces = "application/json")
    @SystemLog(description = "修改警情信息", type = LogType.SYSTEM_OPERATION)
    public String Dtgj_JqxxUpdate(HttpServletRequest request,HttpServletResponse response) throws ParseException
    {
        Dtgj_Jqxx Jqxx = new Dtgj_Jqxx();
        Date date=null;
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sj=request.getParameter("Jqxx_BJSJ");
        if (sj != null) {
            date = formatter.parse(sj);//1111

        } 
        String token = request.getHeader("token");
        Sys_User user = tokenService.verifyToken(token);
        Jqxx.setGAJGJGDM(user.getDWDM());  //  公安机关机构代码 
        Jqxx.setDTZDDM(request.getParameter("Jqxx_DTZDDM"));//  地铁站点代码 
        Jqxx.setDTZDMC(request.getParameter("Jqxx_DTZDMC"));// 地铁站点名称 
        Jqxx.setGJXLDM(request.getParameter("Jqxx_GJXLDM"));// 公交线路代码 
        Jqxx.setJJBH(request.getParameter("Jqxx_JJBH"));// 接警编号 
        Jqxx.setBJFSDM(request.getParameter("Jqxx_BJFSDM"));// 报警方式代码 
        Jqxx.setJQLBDM(request.getParameter("Jqxx_JQLBDM"));// 警情类别代码 
        Jqxx.setBJR_XM(request.getParameter("Jqxx_BJR_XM"));// 姓名 
        Jqxx.setBJR_XBDM(request.getParameter("Jqxx_BJR_XBDM"));// 性别代码 
        Jqxx.setBJR_LXDH(request.getParameter("Jqxx_BJR_LXDH"));// 联系电话 
        Jqxx.setBJDH(request.getParameter("Jqxx_BJDH"));// 报警电话 
        Jqxx.setBJSJ(date);// 报警时间 
        Jqxx.setJYJQ(request.getParameter("Jqxx_JYJQ"));// 简要警情 
        Jqxx.setTJR(user.getLOGINNAME());//  ssxq        提交人   
        Jqxx.setTJDW(user.getDWDM());//  ssxq        提交人单位   
        Jqxx.setTJDWMC(user.getDWMC());//  ssxq        提交人单位名称      
        
        Jqxx.setID(request.getParameter("id"));
        
        Result res = dtgjJqxxService.update(Jqxx);
        return res.toString();
    }

    @ApiOperation(value = "删除警情", notes = "删除警情", httpMethod = "GET")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "警情id", dataType = "Integer", paramType = "query", required = true)
    })

    @RequestMapping(value="/delete",produces = "application/json")
    @SystemLog(description = "删除警情", type = LogType.SYSTEM_OPERATION)
    public String Dtgj_JqxxDelete(HttpServletRequest request,HttpServletResponse response)
    {
        String id = request.getParameter("ids");
        Result res = dtgjJqxxService.delete(id);
        return res.toString();
    } 

    @PassToken
    @ApiOperation(value = "获取线路信息", notes = "获取线路信息", httpMethod = "GET")
    @RequestMapping(value = "/xlxx", produces = "application/json")
    public String getXlxx(HttpServletRequest request, HttpServletResponse response) {
        String xzqh = request.getParameter("xzqh");
        JSONObject res = dtgjJqxxService.getXlxx(xzqh);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @PassToken
    @ApiOperation(value = "获取站点信息", notes = "获取站点信息", httpMethod = "GET")
    @RequestMapping(value = "/zdxx", produces = "application/json")
    public String getZdxx(HttpServletRequest request, HttpServletResponse response) {
        String bm = request.getParameter("bm");

        JSONObject res = dtgjJqxxService.getZdxx(bm);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

}