package com.winton.ytpaas.system.entity;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class Sys_Role_Data {

	@JSONField(name = "QXBM")
    private String QXBM;
	@JSONField(name = "SJCDBM")
    private String SJCDBM;
	@JSONField(name = "JSBM")
    private String JSBM;
	@JSONField(name = "QXMC")
    private String QXMC;
	@JSONField(name = "QXURL")
    private String QXURL;
	@JSONField(name = "QXLB")
    private String QXLB;
	@JSONField(name = "QXNR")
    private String QXNR;

}