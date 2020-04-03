package com.winton.ytpaas.system.entity;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class Sys_User {

	@JSONField(name = "YHBM")
    private String YHBM;
	@JSONField(name = "LOGINNAME")
    private String LOGINNAME;
	@JSONField(name = "PASSWORD")
    private String PASSWORD;
	@JSONField(name = "YHXM")
    private String YHXM;
	@JSONField(name = "YHSFZH")
    private String YHSFZH;
	@JSONField(name = "YHLXDH")
    private String YHLXDH;
	@JSONField(name = "YHJH")
    private String YHJH;
	@JSONField(name = "DWDM")
    private String DWDM;
	@JSONField(name = "DWMC")
    private String DWMC;
	@JSONField(name = "SF")
    private String SF;
	@JSONField(name = "JSBM")
    private String JSBM;
	@JSONField(name = "ZT")
    private String ZT;
	@JSONField(name = "CJYH")
    private String CJYH;
	@JSONField(name = "CJSJ")
    private Date CJSJ;
	@JSONField(name = "XGYH")
    private String XGYH;
	@JSONField(name = "XGSJ")
    private Date XGSJ;
	@JSONField(name = "BZ")
    private String BZ;

}