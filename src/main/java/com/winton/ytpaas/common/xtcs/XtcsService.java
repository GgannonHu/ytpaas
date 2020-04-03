package com.winton.ytpaas.common.xtcs;

import com.winton.ytpaas.common.util.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("xtcsService")
public class XtcsService {
	@Autowired
	XtcsDao dao;

	public Result bindCon(String tid) {
        Result res = new Result();
        res.setCode("1");
        res.setData(dao.bindCon(tid));
        
		return res;
	}
}