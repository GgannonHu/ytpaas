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
import com.winton.ytpaas.dtgj.entity.Dtgj_Asjxx;
import com.winton.ytpaas.dtgj.service.Dtgj_AsjxxService;
import com.winton.ytpaas.system.entity.Sys_User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/dtgj/xxgl/asjxx")
@Api(value = "案事件信息", description = "案事件信息接口")
public class ApiAsjxxController {
    @Autowired
    Dtgj_AsjxxService dtgjAsjxxService;
    @Autowired
    TokenService tokenService;

    @ApiOperation(value = "获取案事件信息列表", notes = "获取案事件信息列表", httpMethod = "GET")
    @RequestMapping(value = "/list", produces = "application/json")
    public String AsjxxList(HttpServletRequest request, HttpServletResponse response) {
        int page = Integer.parseInt(request.getParameter("page"));// PageNo
        int limit = Integer.parseInt(request.getParameter("limit"));// PageSize
        int count = Integer.parseInt(request.getParameter("count"));// PageSize
        int iscon = Integer.parseInt(request.getParameter("iscon"));
 
        String GAJGJGDM = request.getParameter("GAJGJGDM");
        String DTZDMC = request.getParameter("DTZDMC");
        String AJMC = request.getParameter("AJMC");
        String ASJFSKSSJS = request.getParameter("ASJFSKSSJS");
        String ASJFSKSSJE = request.getParameter("ASJFSKSSJE");
        String AFDD = request.getParameter("AFDD");
        String SSXQ = request.getParameter("SSXQ");
         
        JSONObject res = dtgjAsjxxService.getList(GAJGJGDM,DTZDMC,AJMC,ASJFSKSSJS,ASJFSKSSJE,AFDD,SSXQ, page, limit, iscon);
        if (iscon != 1) {
            res.put("count", count);
        }
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "获取遗失物品列表总数", notes = "获取遗失物品列表总数", httpMethod = "GET")
    @RequestMapping(value = "/listcon", produces = "application/json")
    public String Asjxxcon(HttpServletRequest request, HttpServletResponse response) {
        String GAJGJGDM = request.getParameter("GAJGJGDM");
        String DTZDMC = request.getParameter("DTZDMC");
        String AJMC = request.getParameter("AJMC");
        String ASJFSKSSJS = request.getParameter("ASJFSKSSJS");
        String ASJFSKSSJE = request.getParameter("ASJFSKSSJE");
        String AFDD = request.getParameter("AFDD");
        String SSXQ = request.getParameter("SSXQ");
        JSONObject res = dtgjAsjxxService.getListcon(GAJGJGDM,DTZDMC,AJMC,ASJFSKSSJS,ASJFSKSSJE,AFDD,SSXQ);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }
    
    @ApiOperation(value = "根据id获取案事件信息列表", notes = "根据id获取案事件列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "案事件id", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/getById", produces = "application/json")
    public String getAsjxxById(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        Result res = dtgjAsjxxService.getById(id);
        return res.toString();
    }

    @ApiOperation(value = "添加案事件", notes = "添加案事件", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginId", value = "案事件名", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "yswpName", value = "昵称", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/add", produces = "application/json")
    @SystemLog(description = "添加案事件", type = LogType.SYSTEM_OPERATION)
    public String Dtgj_AsjxxAdd(HttpServletRequest request, HttpServletResponse response) throws ParseException
    {
        Dtgj_Asjxx asjxx = new Dtgj_Asjxx();
        Date date=null;
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sj=request.getParameter("asjxx_ASJFSKSSJ");
        if (sj != null) {
            date = formatter.parse(sj);
        } 
        String token = request.getHeader("token");
        Sys_User user = tokenService.verifyToken(token);
        asjxx.setGAJGJGDM(user.getDWDM());  //gajgjgdm    公安机关机构代码
        asjxx.setDTZDBM(request.getParameter("asjxx_DTZDBM"));//  dtzdbm      地铁站点编码
        asjxx.setDTZDMC(request.getParameter("asjxx_DTZDMC"));//  dtzdmc      地铁站点名称
        asjxx.setGJXLBM(request.getParameter("asjxx_GJXLBM"));//  gjxlbm      公交线路编码
        asjxx.setGJGSMC(request.getParameter("asjxx_GJGSMC"));//  gjgsmc      公交公司名称
        asjxx.setAJBH(request.getParameter("asjxx_AJBH"));//  ajbh        案件编号
        asjxx.setAJLB(request.getParameter("asjxx_AJLB"));//  ajlb        案件类别
        asjxx.setAJMC(request.getParameter("asjxx_AJMC"));//  ajmc        案件名称
        asjxx.setJYAQ(request.getParameter("asjxx_JYAQ"));//  jyaq        简要案情
        asjxx.setASJFSKSSJ(date);//  asjfskssj   案事件发生开始时间
        asjxx.setAFDD(request.getParameter("asjxx_AFDD"));//  afdd        案发地点
        asjxx.setSFCS(request.getParameter("asjxx_SFCS"));//  sfcs        事发场所
        asjxx.setSSXQ(request.getParameter("asjxx_SSXQ"));//  ssxq        所属辖区        
        asjxx.setTJR(user.getLOGINNAME());//  ssxq        提交人   
        asjxx.setTJDW(user.getDWDM());//  ssxq        提交人单位   
        asjxx.setTJDWMC(user.getDWMC());//  ssxq        提交人单位名称    
        
        Result res = dtgjAsjxxService.add(asjxx);
        return res.toString();
    }

    @ApiOperation(value = "修改案事件", notes = "修改案事件", httpMethod = "POST")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "yswpName", value = "昵称", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "id", value = "遗失物品id", dataType = "Integer", paramType = "query", required = true)
    })
    @RequestMapping(value="/update",produces = "application/json")
    @SystemLog(description = "修改遗失物品", type = LogType.SYSTEM_OPERATION)
    public String Dtgj_AsjxxUpdate(HttpServletRequest request,HttpServletResponse response) throws ParseException
    {
        Dtgj_Asjxx asjxx = new Dtgj_Asjxx();
        Date date=null;
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sj=request.getParameter("asjxx_ASJFSKSSJ");
        if (sj != null) {
            date = formatter.parse(sj);
        } 
        String token = request.getHeader("token");
        Sys_User user = tokenService.verifyToken(token);
        asjxx.setGAJGJGDM(user.getDWDM());  //gajgjgdm    公安机关机构代码
        asjxx.setGAJGJGDM(request.getParameter("asjxx_GAJGJGDM"));  //gajgjgdm    公安机关机构代码
        asjxx.setDTZDBM(request.getParameter("asjxx_DTZDBM"));//  dtzdbm      地铁站点编码
        asjxx.setDTZDMC(request.getParameter("asjxx_DTZDMC"));//  dtzdmc      地铁站点名称
        asjxx.setGJXLBM(request.getParameter("asjxx_GJXLBM"));//  gjxlbm      公交线路编码
        asjxx.setGJGSMC(request.getParameter("asjxx_GJGSMC"));//  gjgsmc      公交公司名称
        asjxx.setAJBH(request.getParameter("asjxx_AJBH"));//  ajbh        案件编号
        asjxx.setAJLB(request.getParameter("asjxx_AJLB"));//  ajlb        案件类别
        asjxx.setAJMC(request.getParameter("asjxx_AJMC"));//  ajmc        案件名称
        asjxx.setJYAQ(request.getParameter("asjxx_JYAQ"));//  jyaq        简要案情
        asjxx.setASJFSKSSJ(date);//  asjfskssj   案事件发生开始时间
        asjxx.setAFDD(request.getParameter("asjxx_AFDD"));//  afdd        案发地点
        asjxx.setSFCS(request.getParameter("asjxx_SFCS"));//  sfcs        事发场所
        asjxx.setSSXQ(request.getParameter("asjxx_SSXQ"));//  ssxq        所属辖区  
        asjxx.setTJR(user.getLOGINNAME());//  ssxq        提交人   
        asjxx.setTJDW(user.getDWDM());//  ssxq        提交人单位   
        asjxx.setTJDWMC(user.getDWMC());//  ssxq        提交人单位名称   
        asjxx.setID(request.getParameter("id"));
        
        Result res = dtgjAsjxxService.update(asjxx);
        return res.toString();
    }

    @ApiOperation(value = "删除案事件", notes = "删除案事件", httpMethod = "GET")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "案事件id", dataType = "Integer", paramType = "query", required = true)
    })

    @RequestMapping(value="/delete",produces = "application/json")
    @SystemLog(description = "删除案事件", type = LogType.SYSTEM_OPERATION)
    public String Dtgj_AsjxxDelete(HttpServletRequest request,HttpServletResponse response)
    {
        String id = request.getParameter("ids");
        Result res = dtgjAsjxxService.delete(id);
        return res.toString();
    } 

    @PassToken
    @ApiOperation(value = "获取线路信息", notes = "获取线路信息", httpMethod = "GET")
    @RequestMapping(value = "/xlxx", produces = "application/json")
    public String getXlxx(HttpServletRequest request, HttpServletResponse response) {
        String xzqh = request.getParameter("xzqh");
        JSONObject res = dtgjAsjxxService.getXlxx(xzqh);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @PassToken
    @ApiOperation(value = "获取站点信息", notes = "获取站点信息", httpMethod = "GET")
    @RequestMapping(value = "/zdxx", produces = "application/json")
    public String getZdxx(HttpServletRequest request, HttpServletResponse response) {
        String bm = request.getParameter("bm");

        JSONObject res = dtgjAsjxxService.getZdxx(bm);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

}