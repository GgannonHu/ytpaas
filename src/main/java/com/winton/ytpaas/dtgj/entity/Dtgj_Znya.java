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
    @JSONField(name = "FBR")
    private String FBR;
    @JSONField(name = "FBDW")
    private String FBDW;
    @JSONField(name = "FBSJ")
    private Date FBSJ;
}