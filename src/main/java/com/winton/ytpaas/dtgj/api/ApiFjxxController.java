package com.winton.ytpaas.dtgj.api;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.winton.ytpaas.common.configuration.jwt.PassToken;
import com.winton.ytpaas.common.configuration.jwt.TokenService;
import com.winton.ytpaas.common.configuration.log.LogType;
import com.winton.ytpaas.common.configuration.log.SystemLog;
import com.winton.ytpaas.common.util.Result;
import com.winton.ytpaas.common.util.Tools;
import com.winton.ytpaas.dtgj.service.Dtgj_FjxxService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/dtgj/fjxx")
@Api(value = "地铁公交_附件信息接口", description = "地铁公交_附件信息接口")
public class ApiFjxxController {
    @Autowired
    Dtgj_FjxxService dtgjFjxxService;
    @Autowired
    TokenService tokenService;

    @ApiOperation(value = "附件上传", notes = "附件上传", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "files", value = "上传的文件", dataType = "Files", paramType = "query", required = true),
            @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "header", required = true) })
    @RequestMapping(value = "/upload", produces = "application/json")
    @SystemLog(description = "地铁公交_附件上传", type = LogType.API)
    @PassToken
    public String upload(HttpServletRequest request, HttpServletResponse response,
            @RequestParam("file") MultipartFile file) {
        Result res = new Result();
        try {
            String urltop = request.getParameter("urltop");
            String pid = request.getParameter("pid");
            File f = new File("/Users/Gg@/Java/YTPAASUPLOADS/" + urltop + pid);
            if (!f.exists()) {
                f.mkdirs();
            }
            String path = f.getAbsolutePath();
            String fileName = file.getOriginalFilename(); // 将文件名重新命名（因为可能会存在相同名字的文件）
            File tmpFile = new File(path, fileName); // 将二进制文件写到服务器磁盘上
            file.transferTo(tmpFile);

            // File tmpFile = Tools.multipartFile2File(file);
            Map<String, Object> tmpItem = new HashMap<String, Object>();
            String tmpFileName = tmpFile.getName();
            tmpItem.put("pid", pid);
            tmpItem.put("name", tmpFileName);
            tmpItem.put("type", tmpFileName.substring(tmpFileName.lastIndexOf(".") + 1));
            tmpItem.put("fjdz", tmpFile.getPath().replaceAll("\\\\", "/"));
            tmpItem.put("fjdx", tmpFile.length());
            res = dtgjFjxxService.add(tmpItem);

        } catch (IllegalStateException e) {
            res.setCode("-1");
            res.setMsg("上传失败");
            e.printStackTrace();
        } catch (IOException e) {
            res.setCode("-1");
            res.setMsg("上传失败");
            e.printStackTrace();
        }
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "获取附件信息", notes = "获取附件信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "附件父级id", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "header", required = true) })
    @RequestMapping(value = "/listbypid", produces = "application/json")
    public String listByPid(HttpServletRequest request, HttpServletResponse response) {

        String pid = request.getParameter("pid");

        JSONObject res = dtgjFjxxService.getListByPid(pid);
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "附件删除", notes = "附件删除", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "需要删除的主键ID", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "header", required = true) })
    @RequestMapping(value = "/deletebyid", produces = "application/json")
    @SystemLog(description = "删除附件", type = LogType.API)
    public String deleteByid(HttpServletRequest request, HttpServletResponse response) {
        Result res = new Result();

        String tmpIds = request.getParameter("ids");

        JSONObject tmpDataJson = dtgjFjxxService.getListByIds(tmpIds);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> tmpData = (List<Map<String, Object>>) tmpDataJson.get("data");
        for (Map<String, Object> item : tmpData) {
            String tmpFileUrl = item.get("FJDZ").toString();
            File tmpFile = new File(tmpFileUrl);
            delFile(tmpFile);
        }
        res = dtgjFjxxService.delete(tmpIds);

        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    @ApiOperation(value = "附件删除", notes = "附件删除", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "需要删除的父级ID", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "token", value = "用户的token令牌", dataType = "String", paramType = "header", required = true) })
    @RequestMapping(value = "/deletebypid", produces = "application/json")
    @SystemLog(description = "删除附件", type = LogType.API)
    public String deleteByPid(HttpServletRequest request, HttpServletResponse response) {
        Result res = new Result();
        String urltop = request.getParameter("urltop");
        String tmpPids = request.getParameter("ids");

        JSONObject tmpDataJson = dtgjFjxxService.getListByPids(tmpPids);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> tmpData = (List<Map<String, Object>>) tmpDataJson.get("data");
        if (tmpData.size() > 0) {
            String[] tmpIds = tmpPids.split(",");
            for (String pid : tmpIds) {
                if (pid.length() > 0) {
                    File tmpFlieUrl = new File("/YTPAASUPLOADS/" + urltop + pid);
                    delFile(tmpFlieUrl);
                }
            }
            res = dtgjFjxxService.deleteByPid(tmpPids);
        } else {
            res.setCode("1");
        }
        String retStr = Tools.toJSONString(res);
        return retStr;
    }

    private static boolean delFile(File file) {
        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delFile(f);
            }
        }
        return file.delete();
    }
}