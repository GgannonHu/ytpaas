package com.winton.ytpaas.dtgj.entity;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class Dtgj_Znya {

    @JSONField(name = "ID")
    private String ID;
    @JSONField(name = "NAME")
    private String NAME;
    @JSONField(name = "DJ")
    private String DJ;
    @JSONField(name = "NR")
    private String NR;
    @JSONField(name = "TJR")
    private String TJR;
    @JSONField(name = "TJDW")
    private String TJDW;
    @JSONField(name = "TJSJ")
    private Date TJSJ;
    @JSONField(name = "TJDWMC")
    private String TJDWMC;
}