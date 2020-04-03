package com.winton.ytpaas.system.entity;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class Sys_Role_Menu {

	@JSONField(name = "QXBM")
    private String QXBM;
	@JSONField(name = "JSBM")
    private String JSBM;
	@JSONField(name = "CDBM")
    private String CDBM;
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