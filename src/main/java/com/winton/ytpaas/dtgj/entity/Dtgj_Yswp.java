package com.winton.ytpaas.dtgj.entity;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class Dtgj_Yswp {

	@JSONField(name = "ID")
    private String ID;
	@JSONField(name = "MC")
    private String MC;
	@JSONField(name = "MS")
    private String MS;
	@JSONField(name = "SQDD")
    private String SQDD;
	@JSONField(name = "SQSJ")
    private Date SQSJ;
	@JSONField(name = "ZT")
    private String ZT;
	@JSONField(name = "TJR")
    private String TJR;
	@JSONField(name = "TJDW")
    private String TJDW;
	@JSONField(name = "TJSJ")
    private Date TJSJ;
	@JSONField(name = "RLR")
    private String RLR;
	@JSONField(name = "RLRSFZH")
    private String RLRSFZH;
	@JSONField(name = "RLSJ")
    private Date RLSJ;
}