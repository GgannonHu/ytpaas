package com.winton.ytpaas.dtgj.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DtgjController {
    @RequestMapping("/dtgj/index")
    public String dtgj() {
        return "/dtgj/index";
    }

    @RequestMapping("/dtgj/zhld/qwgl/dwgl/abry")
    public String abry() {
        return "/dtgj/zhld/qwgl/dwgl/abry/abrylist";
    }

    @RequestMapping("/dtgj/zhld/qwgl/dwgl/abry/edit")
    public String abryEdit() {
        return "/dtgj/zhld/qwgl/dwgl/abry/abryedit";
    }

    @RequestMapping("/dtgj/zhld/qwgl/dwgl/qwll")
    public String qwll() {
        return "/dtgj/zhld/qwgl/dwgl/qwll/qwlllist";
    }

    @RequestMapping("/dtgj/zhld/qwgl/dwgl/qwll/edit")
    public String qwllEdit() {
        return "/dtgj/zhld/qwgl/dwgl/qwll/qwlledit";
    }

    @RequestMapping("/dtgj/zhld/qwgl/qwbb")
    public String qwbb() {
        return "/dtgj/zhld/qwgl/qwbb/qwbblist";
    }

    @RequestMapping("/dtgj/zhld/qwgl/qwbb/edit")
    public String qwbbEdit() {
        return "/dtgj/zhld/qwgl/qwbb/qwbbedit";
    }

    @RequestMapping("/dtgj/xxgl/jqxx/jqxxedit")
    public String jqxxedit() {
        return "/dtgj/xxgl/jqxx/jqxxedit";
    }

    @RequestMapping("/dtgj/xxgl/jqxx/list")
    public String jqxxlist() {
        return "/dtgj/xxgl/jqxx/jqxxlist";
    }

    @RequestMapping("/dtgj/jcfz/znya")
    public String znya() {
        return "/dtgj/jcfz/znya/znyalist";
    }

    @RequestMapping("/dtgj/jcfz/znya/edit")
    public String znyaEdit() {
        return "/dtgj/jcfz/znya/znyaedit";
    }

    @RequestMapping("/dtgj/bmfw/yswp")
    public String yswp() {
        return "/dtgj/bmfw/yswp/yswplist";
    }

    @RequestMapping("/dtgj/bmfw/yswp/yswpedit")
    public String yswpedit() {
        return "/dtgj/bmfw/yswp/yswpedit";
    }

    @RequestMapping("/dtgj/bmfw/yswp/yswplook")
    public String yswplook() {
        return "/dtgj/bmfw/yswp/yswplook";
    }

    @RequestMapping("/dtgj/bmfw/yswp/yswprl")
    public String yswprl() {
        return "/dtgj/bmfw/yswp/yswprl";
    }

    @RequestMapping("/dtgj/zhld/ctcz")
    public String ctcz() {
        return "/dtgj/zhld/ctcz/ctczlist";
    }

    @RequestMapping("/dtgj/zhld/ctcz/ctczedit")
    public String ctczedit() {
        return "/dtgj/zhld/ctcz/ctczedit";
    }

    @RequestMapping("/dtgj/zhld/ctcz/ctczxf")
    public String ctczxf() {
        return "/dtgj/zhld/ctcz/ctczxf";
    }

    @RequestMapping("/dtgj/zhld/ctcz/ctczfk")
    public String ctczfk() {
        return "/dtgj/zhld/ctcz/ctczfk";
    }

    @RequestMapping("/dtgj/zhld/ctcz/ctczlook")
    public String ctczlook() {
        return "/dtgj/zhld/ctcz/ctczlook";
    }

    @RequestMapping("/dtgj/zhld/yjct")
    public String yjct() {
        return "/dtgj/zhld/yjct/yjctlist";
    }

    @RequestMapping("/dtgj/zhld/yjct/edit")
    public String yjctedit() {
        return "/dtgj/zhld/yjct/yjctedit";
    }

    @RequestMapping("/dtgj/zhld/yjct/xf")
    public String yjctxf() {
        return "/dtgj/zhld/yjct/yjctxf";
    }

    @RequestMapping("/dtgj/xxgl/dtxl/list")
    public String dtxllist() {
        return "/dtgj/xxgl/dtxl/dtxllist";
    }

    @RequestMapping("/dtgj/xxgl/dtxl/edit")
    public String dtxledit() {
        return "/dtgj/xxgl/dtxl/dtxledit";
    }

    @RequestMapping("/dtgj/xxgl/dtzd/list")
    public String dtzdlist() {
        return "/dtgj/xxgl/dtzd/dtzdlist";
    }

    @RequestMapping("/dtgj/xxgl/dtzd/edit")
    public String dtzdedit() {
        return "/dtgj/xxgl/dtzd/dtzdedit";
    }

    @RequestMapping("/dtgj/xxgl/gjxl/list")
    public String gjxllist() {
        return "/dtgj/xxgl/gjxl/gjxllist";
    }

    @RequestMapping("/dtgj/xxgl/gjxl/edit")
    public String gjxledit() {
        return "/dtgj/xxgl/gjxl/gjxledit";
    }

    @RequestMapping("/dtgj/xxgl/gjzd/list")
    public String gjzdlist() {
        return "/dtgj/xxgl/gjzd/gjzdlist";
    }

    @RequestMapping("/dtgj/xxgl/gjzd/edit")
    public String gjzdedit() {
        return "/dtgj/xxgl/gjzd/gjzdedit";
    }

    @RequestMapping("/dtgj/xxgl/asjxx/asjxxedit")
    public String asjxxedit() {
        return "/dtgj/xxgl/asjxx/asjxxedit";
    }

    @RequestMapping("/dtgj/xxgl/asjxx/list")
    public String asjxxlist() {
        return "/dtgj/xxgl/asjxx/asjxxlist";
    }

    @RequestMapping("/dtgj/xxgl/gjgwcyry/list")
    public String gjgwcyrylist() {
        return "/dtgj/xxgl/gjgwcyry/gjgwcyrylist";
    }

    @RequestMapping("/dtgj/xxgl/gjgwcyryedit")
    public String gjgwcyryedit() {
        return "/dtgj/xxgl/gjgwcyry/gjgwcyryedit";
    }

    @RequestMapping("/dtgj/xxgl/gjgwcyrylook")
    public String gjgwcyrylook() {
        return "/dtgj/xxgl/gjgwcyry/gjgwcyrylook";
    }

    // 文件下载
    @RequestMapping("/dtgj/fjxx/filedownload")
    public ResponseEntity<byte[]> fileDownLoad(HttpServletRequest request) throws Exception {
        String mc = request.getParameter("mc");
        String dz = request.getParameter("dz");
        String fileName = mc;
        String realPath = dz;
        InputStream in = new FileInputStream(new File(realPath));// 将该文件加入到输入流之中
        byte[] body = null;
        body = new byte[in.available()];// 返回下一次对此输入流调用的方法可以不受阻塞地从此输入流读取（或跳过）的估计剩余字节数
        in.read(body);// 读入到输入流里面
        fileName = new String(fileName.getBytes("gbk"), "iso8859-1");// 防止中文乱码
        HttpHeaders headers = new HttpHeaders();// 设置响应头
        headers.add("Content-Disposition", "attachment;filename=" + fileName);
        HttpStatus statusCode = HttpStatus.OK;// 设置响应吗
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(body, headers, statusCode);
        return response;
    }

    // 重点人员预警
    @RequestMapping("/dtgj/yjgz/zdryyj/list")
    public String zdryyjlist() {
        return "/dtgj/yjgz/zdryyj/zdryyjlist";
    }

    @RequestMapping("/dtgj/yjgz/zdryyj/sel")
    public String zdryyjsel() {
        return "/dtgj/yjgz/zdryyj/zdryyjsel";
    }

    @RequestMapping("/dtgj/xxgl/bkry/list")
    public String bkrylist() {
        return "/dtgj/xxgl/bkry/bkrylist";
    }

    @RequestMapping("/dtgj/xxgl/bkry/edit")
    public String bkryedit() {
        return "/dtgj/xxgl/bkry/bkryedit";
    }
}