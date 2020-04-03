package com.winton.ytpaas.dtgj.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSONObject;
import com.winton.ytpaas.common.configuration.jwt.PassToken;
import com.winton.ytpaas.common.configuration.jwt.TokenService;
import com.winton.ytpaas.common.configuration.log.LogType;
import com.winton.ytpaas.common.configuration.log.SystemLog;
import com.winton.ytpaas.common.util.Result;
import com.winton.ytpaas.common.util.Tools;
import com.winton.ytpaas.dtgj.entity.Dtgj_Abry;
import com.winton.ytpaas.dtgj.service.Dtgj_AbryService;
import com.winton.ytpaas.system.entity.Sys_User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/dtgj/zhld/qwgl/dwgl/abry")
@Api(value = "安保人员管理接口", description = "安保人员管理接口")
public class ApiAbryController {
    @Autowired
    Dtgj_AbryService dtgjAbryService;
    @Autowired
    TokenService tokenService;

    @PassToken
    @ApiOperation(value = "获取安保人员列表", notes = "获取安保人员列表", httpMethod = "GET")
    @RequestMapping(value = "/list", produces = "application/json")
    public String abryList(HttpServletRequest request, HttpServletResponse response) {
        String gajgjgdm = request.getParameter("gajgjgdm");

        int page = Integer.parseInt(request.getParameter("page"));// PageNo
        int limit = Integer.parseInt(request.getParameter("limit"));// PageSize
        int count = Integer.parseInt(request.getParameter("count"));// PageSize
        int iscon = Integer.parseInt(request.getParameter("iscon"));

        String dtzdmc = request.getParameter("dtzdmc");
        String gjxlbm = request.getParameter("gjxlbm");
        String gmsfhm = request.getParameter("gmsfhm");
        String xm = request.getParameter("xm");
        String dw = request.getParameter("dw");
        JSONObject res = dtgjAbryService.getList(dtzdmc, gjxlbm, gmsfhm, xm, dw, page, limit, iscon, gajgjgdm);
        if (iscon != 1) {
            res.put("count", count);
        }
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @PassToken
    @ApiOperation(value = "获取线路信息", notes = "获取线路信息", httpMethod = "GET")
    @RequestMapping(value = "/xlxx", produces = "application/json")
    public String getXlxx(HttpServletRequest request, HttpServletResponse response) {
        String xzqh = request.getParameter("xzqh");
        JSONObject res = dtgjAbryService.getXlxx(xzqh);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @PassToken
    @ApiOperation(value = "获取站点信息", notes = "获取站点信息", httpMethod = "GET")
    @RequestMapping(value = "/zdxx", produces = "application/json")
    public String getZdxx(HttpServletRequest request, HttpServletResponse response) {
        String bm = request.getParameter("bm");

        JSONObject res = dtgjAbryService.getZdxx(bm);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "根据id获取安保人员信息", notes = "根据id获取安保人员信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/getById", produces = "application/json")
    public String getAbryById(HttpServletRequest request, HttpServletResponse response) {
        Result res = dtgjAbryService.getById(request.getParameter("id"));
        System.out.print(Tools.toJSONString(res));
        return Tools.toJSONString(res);
    }

    @ApiOperation(value = "添加安保人员", notes = "添加安保人员", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dtzdbm", value = "地铁线路编码", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "dtzdmc", value = "地铁线路名称", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "gjxlbm", value = "公交线路编码", dataType = "Date", paramType = "query", required = true),
            @ApiImplicitParam(name = "gmsfhm", value = "公民身份号码", dataType = "Date", paramType = "query", required = true),
            @ApiImplicitParam(name = "xm", value = "姓名", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "yddh", value = "移动电话", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "zzmm", value = "政治面貌", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "mz", value = "民族", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "dz", value = "地址", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "dw", value = "单位", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/add", produces = "application/json")
    @SystemLog(description = "添加安保人员", type = LogType.SYSTEM_OPERATION)
    public String dtgj_AbryAdd(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("token");
        Sys_User user = tokenService.verifyToken(token);

        Dtgj_Abry abry = new Dtgj_Abry();

        abry.setGAJGJGDM(user.getDWDM());
        abry.setDTZDBM(request.getParameter("dtzdbm"));
        abry.setDTZDMC(request.getParameter("dtzdmc"));
        abry.setGJXLBM(request.getParameter("gjxlbm"));
        abry.setABRY_GMSFHM(request.getParameter("gmsfhm"));
        abry.setABRY_XM(request.getParameter("xm"));
        abry.setABRY_YDDH(request.getParameter("yddh"));
        abry.setABRY_ZZMMDM(request.getParameter("zzmm"));
        abry.setABRY_MZDM(request.getParameter("mz"));
        abry.setABRY_DZMC(request.getParameter("dz"));
        abry.setABRY_DWMC(request.getParameter("dw"));
        abry.setTJR(request.getParameter("tjr"));
        abry.setTJDW(request.getParameter("tjdw"));
        abry.setTJDWMC(request.getParameter("tjdwmc"));

        Result res = dtgjAbryService.add(abry);
        return res.toString();
    }

    @ApiOperation(value = "修改安保人员", notes = "修改安保人员", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dtzdbm", value = "地铁线路编码", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "dtzdmc", value = "地铁线路名称", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "gjxlbm", value = "公交线路编码", dataType = "Date", paramType = "query", required = true),
            @ApiImplicitParam(name = "gmsfhm", value = "公民身份号码", dataType = "Date", paramType = "query", required = true),
            @ApiImplicitParam(name = "xm", value = "姓名", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "yddh", value = "移动电话", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "zzmm", value = "政治面貌", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "mz", value = "民族", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "dz", value = "地址", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "dw", value = "单位", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/update", produces = "application/json")
    @SystemLog(description = "修改安保人员", type = LogType.SYSTEM_OPERATION)
    public String dtgj_QwbbUpdate(HttpServletRequest request, HttpServletResponse response) {

        Dtgj_Abry abry = new Dtgj_Abry();

        abry.setID(request.getParameter("id"));
        abry.setDTZDBM(request.getParameter("dtzdbm"));
        abry.setDTZDMC(request.getParameter("dtzdmc"));
        abry.setGJXLBM(request.getParameter("gjxlbm"));
        abry.setABRY_GMSFHM(request.getParameter("gmsfhm"));
        abry.setABRY_XM(request.getParameter("xm"));
        abry.setABRY_YDDH(request.getParameter("yddh"));
        abry.setABRY_ZZMMDM(request.getParameter("zzmm"));
        abry.setABRY_MZDM(request.getParameter("mz"));
        abry.setABRY_DZMC(request.getParameter("dz"));
        abry.setABRY_DWMC(request.getParameter("dw"));

        Result res = dtgjAbryService.update(abry);
        return res.toString();
    }

    @ApiOperation(value = "删除安保人员", notes = "删除安保人员", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "Integer", paramType = "query", required = true) })
    @RequestMapping(value = "/delete", produces = "application/json")
    @SystemLog(description = "删除安保人员", type = LogType.SYSTEM_OPERATION)
    public String dtgj_QwbbDelete(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("ids");

        Result res = dtgjAbryService.delete(id);
        return res.toString();
    }
}