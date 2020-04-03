package com.winton.ytpaas.common.configuration.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 系统日志操作类
 */
public class SystemLogService {

    private Logger logger;
    /**
     * 实例化系统日志
     * @param logType 日志类型
     */
    public SystemLogService(LogType logType) {
        logger = LoggerFactory.getLogger(logType.toString() + "_LOG");
    }

    /**
     * 生成日志
     * @param msg 输出日志内容，JSON格式：{sqlStr: "select * from dual", createTime: "2019-11-21 15:39:00"}
     */
    public void log(String msg) {
        logger.info(msg);
    }
}