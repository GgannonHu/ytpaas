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
import com.winton.ytpaas.dtgj.entity.Dtgj_Qwll;
import com.winton.ytpaas.dtgj.service.Dtgj_QwllService;
import com.winton.ytpaas.system.entity.Sys_User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/dtgj/zhld/qwgl/dwgl/qwll")
@Api(value = "勤务力量管理接口", description = "勤务力量管理接口")
public class ApiQwllController {
    @Autowired
    Dtgj_QwllService dtgjQwllService;
    @Autowired
    TokenService tokenService;

    @PassToken
    @ApiOperation(value = "获取勤务力量列表", notes = "获取勤务力量列表", httpMethod = "GET")
    @RequestMapping(value = "/list", produces = "application/json")
    public String qwllList(HttpServletRequest request, HttpServletResponse response) {
        String gajgjgdm = request.getParameter("gajgjgdm");

        int page = Integer.parseInt(request.getParameter("page"));// PageNo
        int limit = Integer.parseInt(request.getParameter("limit"));// PageSize
        int count = Integer.parseInt(request.getParameter("count"));// PageSize
        int iscon = Integer.parseInt(request.getParameter("iscon"));

        String gjgsmc = request.getParameter("gjgsmc");
        String xm = request.getParameter("xm");
        String lxdh = request.getParameter("lxdh");
        JSONObject res = dtgjQwllService.getList(gjgsmc, xm, lxdh, page, limit, iscon, gajgjgdm);
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
        JSONObject res = dtgjQwllService.getXlxx(xzqh);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @PassToken
    @ApiOperation(value = "获取站点信息", notes = "获取站点信息", httpMethod = "GET")
    @RequestMapping(value = "/zdxx", produces = "application/json")
    public String getZdxx(HttpServletRequest request, HttpServletResponse response) {
        String bm = request.getParameter("bm");

        JSONObject res = dtgjQwllService.getZdxx(bm);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "根据id获取勤务力量信息", notes = "根据id获取勤务力量信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/getById", produces = "application/json")
    public String getQwllById(HttpServletRequest request, HttpServletResponse response) {
        Result res = dtgjQwllService.getById(request.getParameter("id"));
        return Tools.toJSONString(res);
    }

    @ApiOperation(value = "添加勤务力量", notes = "添加勤务力量", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dtzdbm", value = "地铁线路编码", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "dtzdmc", value = "地铁线路名称", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "gjxlbm", value = "公交线路编码", dataType = "Date", paramType = "query", required = true),
            @ApiImplicitParam(name = "gjgsmc", value = "公交公司名称", dataType = "Date", paramType = "query", required = true),
            @ApiImplicitParam(name = "xm", value = "姓名", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "lxdh", value = "联系电话", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "jybh", value = "警员编号", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "qwlbdm", value = "勤务类别代码", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/add", produces = "application/json")
    @SystemLog(description = "添加勤务力量", type = LogType.SYSTEM_OPERATION)
    public String dtgj_QwllAdd(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("token");
        Sys_User user = tokenService.verifyToken(token);

        Dtgj_Qwll qwll = new Dtgj_Qwll();

        qwll.setGAJGJGDM(user.getDWDM());
        qwll.setDTZDBM(request.getParameter("dtzdbm"));
        qwll.setDTZDMC(request.getParameter("dtzdmc"));
        qwll.setGJXLBM(request.getParameter("gjxlbm"));
        qwll.setGJGSMC(request.getParameter("gjgsmc"));
        qwll.setQWLL_XM(request.getParameter("xm"));
        qwll.setQWLL_LXDH(request.getParameter("lxdh"));
        qwll.setQWLL_JYBH(request.getParameter("jybh"));
        qwll.setQWLB_QWLBDM(request.getParameter("qwlbdm"));
        qwll.setTJR(request.getParameter("tjr"));
        qwll.setTJDW(request.getParameter("tjdw"));
        qwll.setTJDWMC(request.getParameter("tjdwmc"));

        Result res = dtgjQwllService.add(qwll);
        return res.toString();
    }

    @ApiOperation(value = "修改勤务力量", notes = "修改勤务力量", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dtzdbm", value = "地铁线路编码", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "dtzdmc", value = "地铁线路名称", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "gjxlbm", value = "公交线路编码", dataType = "Date", paramType = "query", required = true),
            @ApiImplicitParam(name = "gjgsmc", value = "公交公司名称", dataType = "Date", paramType = "query", required = true),
            @ApiImplicitParam(name = "xm", value = "姓名", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "lxdh", value = "联系电话", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "jybh", value = "警员编号", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "qwlbdm", value = "勤务类别代码", dataType = "String", paramType = "query", required = true) })
    @RequestMapping(value = "/update", produces = "application/json")
    @SystemLog(description = "修改勤务力量", type = LogType.SYSTEM_OPERATION)
    public String dtgj_QwllUpdate(HttpServletRequest request, HttpServletResponse response) {

        Dtgj_Qwll qwll = new Dtgj_Qwll();

        qwll.setID(request.getParameter("id"));
        qwll.setDTZDBM(request.getParameter("dtzdbm"));
        qwll.setDTZDMC(request.getParameter("dtzdmc"));
        qwll.setGJXLBM(request.getParameter("gjxlbm"));
        qwll.setGJGSMC(request.getParameter("gjgsmc"));
        qwll.setQWLL_XM(request.getParameter("xm"));
        qwll.setQWLL_LXDH(request.getParameter("lxdh"));
        qwll.setQWLL_JYBH(request.getParameter("jybh"));
        qwll.setQWLB_QWLBDM(request.getParameter("qwlbdm"));

        Result res = dtgjQwllService.update(qwll);
        return res.toString();
    }

    @ApiOperation(value = "删除勤务力量", notes = "删除勤务力量", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "Integer", paramType = "query", required = true) })
    @RequestMapping(value = "/delete", produces = "application/json")
    @SystemLog(description = "删除勤务力量", type = LogType.SYSTEM_OPERATION)
    public String dtgj_QwllDelete(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("ids");

        Result res = dtgjQwllService.delete(id);
        return res.toString();
    }
}