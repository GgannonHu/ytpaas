package com.winton.ytpaas.dtgj.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.winton.ytpaas.common.configuration.jwt.TokenService;
import com.winton.ytpaas.common.util.Result;
import com.winton.ytpaas.dtgj.dao.Dtgj_ComDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dtgjComService")
public class Dtgj_ComService {
	@Autowired
	TokenService tokenService;
	@Autowired
	Dtgj_ComDao dao;

	public Result getJg(String did) {
		Result res = new Result();
		List<Map<String, Object>> list = dao.getJg(did);
		JSONArray jr = new JSONArray();
		JSONObject obj = new JSONObject();
		obj.put("JGDM", did);
		for (Map<String, Object> item : list) {
			if (item.get("JGDM").equals(obj.get("JGDM"))) {
				obj.put("JGDM", item.get("JGDM"));
				obj.put("XZQH", item.get("XZQH"));
				obj.put("JGMC", item.get("JGMC"));
				obj.put("SJDM", item.get("SJDM"));
				obj.put("JGJB", item.get("JGJB"));
				obj.put("JGLBDM", item.get("JGLBDM"));
				obj.put("JGLBMC", item.get("JGLBMC"));

				obj.put("name", item.get("JGMC"));
				obj.put("pageurl", "");
				obj.put("px", "");
				obj.put("bz", "");
				obj.put("spread", "true");
			}
		}
		JSONArray child = bindTree(obj, list);
		if (child.size() > 0) {
			obj.put("children", child);
		}
		jr.add(obj);

		res.setCode("1");
		res.setData(jr);
		return res;
	}

	public JSONObject getJg() {
		JSONObject tmpRet = new JSONObject();
		tmpRet.put("data", dao.getJg());
		tmpRet.put("code", "1");
		return tmpRet;
	}

	private JSONArray bindTree(JSONObject sj, List<Map<String, Object>> list) {
		JSONArray jr = new JSONArray();
		for (Map<String, Object> item : list) {
			if (item.get("SJDM").equals(sj.get("JGDM"))) {
				JSONObject obj = new JSONObject();
				obj.put("XZQH", item.get("XZQH"));
				obj.put("JGDM", item.get("JGDM"));
				obj.put("JGMC", item.get("JGMC"));
				obj.put("SJDM", item.get("SJDM"));
				obj.put("JGJB", item.get("JGJB"));
				obj.put("JGLBDM", item.get("JGLBDM"));
				obj.put("JGLBMC", item.get("JGLBMC"));

				obj.put("name", item.get("JGMC"));
				obj.put("pageurl", "");
				obj.put("px", "");
				obj.put("bz", "");
				JSONArray child = bindTree(obj, list);
				if (child.size() > 0) {
					obj.put("children", child);
				}
				jr.add(obj);
			}
		}
		return jr;
	}

	public JSONObject getJgByPid(String id) {
		JSONObject tmpRet = new JSONObject();
		tmpRet.put("data", dao.getJgByPid(id));
		tmpRet.put("code", "1");
		return tmpRet;
	}

	public JSONObject bindCon(String tid) {
		JSONObject tmpRet = new JSONObject();
		tmpRet.put("data", dao.bindCon(tid));
		tmpRet.put("code", "1");
		return tmpRet;
	}
}