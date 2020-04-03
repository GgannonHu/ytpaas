package com.winton.ytpaas.common.util;

import com.alibaba.fastjson.JSONObject;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class Result {
    private Object data = "";
    private String msg = "";
    private String code = "";

    public String toString() {
        JSONObject obj = new JSONObject();
        obj.put("code", code);
        obj.put("msg", msg);
        obj.put("data", data);

        return Tools.toJSONString(obj);
	}
}