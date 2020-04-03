package com.winton.ytpaas.common.configuration.log;

import lombok.Data;

import java.util.Date;


@Data
public class Log {

    /**
     * 描述：自定义的描述内容
     */
    private String description;
    /**
     * 日志类型：自定义的日志类型 0登陆日志 1操作日志 2api
     */
    private String logType;
    /**
     * 请求路径（封装）
     */
    private String requestUrl;
    /**
     * 请求类型（封装）
     */
    private String requestType;
    /**
     * 请求参数（封装）
     */
    private String requestParam;
    /**
     * 浏览器信息
     */
    private String browser;
    /**
     * 操作系统
     */
    private String operatSystem;
    /**
     * 登录名（封装）
     */
    private String loginName;
    /**
     * 用户名（封装）
     */
    private String yhxm;
    /**
     * 机构编码（封装）
     */
    private String dwbm;
    /**
     * 机构名称（封装）
     */
    private String dwmc;
    /**
     * ip（封装）
     */
    private String ip;
    /**
     * 花费时间 毫秒（封装）
     */
    private Integer costTime;
    /**
     * 创建时间
     */
    private Date createTime;

}