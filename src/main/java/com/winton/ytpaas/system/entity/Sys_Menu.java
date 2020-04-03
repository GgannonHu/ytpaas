package com.winton.ytpaas.system.entity;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class Sys_Menu {

	@JSONField(name = "CDBM")
    private String CDBM;
	@JSONField(name = "CDMC")
    private String CDMC;
	@JSONField(name = "SJBM")
    private String SJBM;
	@JSONField(name = "URL")
    private String URL;
	@JSONField(name = "PX")
    private String PX;
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