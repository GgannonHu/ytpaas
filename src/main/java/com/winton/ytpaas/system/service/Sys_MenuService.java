package com.winton.ytpaas.system.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.winton.ytpaas.common.util.Result;
import com.winton.ytpaas.system.dao.Sys_MenuDao;
import com.winton.ytpaas.system.entity.Sys_Menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sysMenuService")
public class Sys_MenuService {

    @Autowired
    Sys_MenuDao dao;
	
    public Sys_Menu getById(String cdbm) {
        return dao.getById(cdbm);
    }

    public List<Sys_Menu> getAll() {
        return dao.getAll();
    }
    public List<Map<String, Object>> getAllByJSBM(String jsbms) {
        return dao.getAllByJSBM(jsbms);
    }

    public List<Sys_Menu> getAll(String sjbm) {
        return dao.getAll(sjbm);
    }

    public Result add(Sys_Menu item) {
        Result res = new Result();

        boolean b = dao.add(item);
        if(b) {
            res.setCode("1");
            res.setMsg(item.getCDMC() + "添加成功");
        } else {
            res.setCode("-1");
            res.setMsg(item.getCDMC() + "添加失败");
        }
       
        return res;
    }

    public Result update(Sys_Menu item) {
        Result res = new Result();
        boolean b = dao.update(item);
        if(b) {
            res.setCode("1");
            res.setMsg(item.getCDMC() + "修改成功");
        } else {
            res.setCode("-1");
            res.setMsg(item.getCDMC() + "修改失败");
        }
       
        return res;
    }
    
    public Result delete(String cdbm) {
        Result res = new Result();
        boolean b = dao.delete(cdbm);
        if(b) {
            deleteChl(cdbm);
            res.setCode("1");
            res.setMsg("删除成功");
        } else {
            res.setCode("-1");
            res.setMsg("删除失败");
        }
       
        return res;
    }
    private void deleteChl(String sjbm) {
        List<Sys_Menu> list = dao.getAll(sjbm);
        for (Sys_Menu item : list) {
            dao.delete(item.getCDBM());
            deleteChl(item.getCDBM());
        }
    }

    public Result updatesort(String str) {
        Result res = new Result();
        JSONArray arr = JSONArray.parseArray(str);

        boolean b = dao.updatesort(arr);
        if(b) {
            res.setCode("1");
            res.setMsg("保存成功");
        } else {
            res.setCode("-1");
            res.setMsg("保存失败");
        }
       
        return res;
    }

    public String getUserMenuAuth(String basePath, String jsbm) {
        List<Map<String, Object>> list = dao.getAllByJSBM(jsbm);
        
        JSONArray arr = bindMenu("0", list);
        
        Result res = new Result();
        res.setCode("1");
        res.setData(arr);
        return res.toString();
    }

    public String getUserMenuAuth(String basePath, String jsbm, String pid) {
        List<Map<String, Object>> list = dao.getAllByJSBM(jsbm);
        
        JSONArray arr = bindMenu(pid, list);
        Result res = new Result();
        res.setCode("1");
        res.setData(arr);
        return res.toString();
    }
    
    public String getUserMenuAuthTree(String basePath, String jsbm, String pid) {
        List<Map<String, Object>> list = dao.getAllByJSBM(jsbm);
        JSONArray arr = getTreeData(pid, list);
        Result res = new Result();
        res.setCode("1");
        res.setData(arr);
        return res.toString();
    }
    
    private JSONArray getTreeData(String pid,List<Map<String, Object>> list) {
    	JSONArray arr = new JSONArray();
        for(Map<String, Object> item : list) {
            if(item.get("SJBM").toString().equals(pid)) {
                JSONObject obj = new JSONObject();
//                obj.put("extend", false);
                obj.put("icon", item.get("BZ"));
//                obj.put("isDesktopIcon", 1);
//                obj.put("maxOpen", -1);
//                obj.put("openType", 2);
//                obj.put("orderNumber", item.get("PX"));
                obj.put("_href", item.get("URL"));
                obj.put("id", item.get("CDBM"));
                obj.put("spread",true);
                obj.put("title", item.get("CDMC"));
                obj.put("name", item.get("CDMC"));
                JSONArray children =  getTreeData(item.get("CDBM").toString(), list) ;
                if(children != null && children.size()>0) {
                	obj.put("children", children);
                }else {
                	obj.put("children",new JSONArray());
                }
                
                arr.add(obj);
            }
        }

        return arr;
    }

    private JSONArray bindMenu(String pid, List<Map<String, Object>> list) {
        JSONArray arr = new JSONArray();
        for(Map<String, Object> item : list) {
            if(item.get("SJBM").toString().equals(pid)) {
                JSONObject obj = new JSONObject();
                obj.put("extend", false);
                obj.put("icon", item.get("BZ"));
                obj.put("isDesktopIcon", 1);
                obj.put("maxOpen", -1);
                obj.put("openType", 2);
                obj.put("orderNumber", item.get("PX"));
                obj.put("pageURL", item.get("URL"));
                obj.put("href", item.get("URL"));
                obj.put("id", item.get("CDBM"));
                obj.put("parentId", item.get("SJBM"));
                obj.put("title", item.get("CDMC"));
                obj.put("name", item.get("CDMC"));
                
                if(item.get("SFZCD").toString().equals("1")) {
                    obj.put("childs", bindMenu(item.get("CDBM").toString(), list));
                }
                
                arr.add(obj);
            }
        }

        return arr;
    }
    
    
    
    public Result getAllTree() {
    	Result res = new Result();
    	List<Sys_Menu> list = dao.getAll();
    	
    	Sys_Menu item = new Sys_Menu();
    	item.setCDBM("0");
    	item.setCDMC("菜单管理");
    	item.setSJBM("-1");
    	list.add(item);
    	
    	JSONArray jr = new JSONArray();
    	JSONObject obj =new JSONObject();
    	obj.put("cdbm", "0");
    	obj.put("name", "子系统");
    	obj.put("pageurl", "");
    	obj.put("px", "");
    	obj.put("bz", "");
    	obj.put("spread", "true");
    	JSONArray child = bindTree(obj, list);
    	if(child.size() > 0) {
    		obj.put("children", child);
		}
    	jr.add(obj);
    	
    	res.setCode("1");
    	res.setData(jr);
        return res;
    }
    private JSONArray bindTree(JSONObject sj, List<Sys_Menu> list) {
    	JSONArray jr = new JSONArray();
    	for(Sys_Menu item : list) {
    		if(item.getSJBM().equals(sj.get("cdbm"))) {
    			JSONObject obj =new JSONObject();
    	    	obj.put("cdbm", item.getCDBM());
    	    	obj.put("name", item.getCDMC());
    	    	obj.put("pageurl", item.getURL());
    	    	obj.put("px", item.getPX());
    	    	obj.put("bz", item.getBZ());
    	    	JSONArray child = bindTree(obj, list);
    	    	if(child.size() > 0) {
    	    		obj.put("children", child);
        		}
    	    	jr.add(obj);
    		}
    	}
    	return jr;
    }
    

    public Result getAllGNQX(String jsbm) {
    	Result res = new Result();
    	List<Map<String, Object>> list = dao.getGNQX(jsbm);
    	
    	res.setCode("1");
    	res.setData(list);
        return res;
    }
    public Result getMyGNQX(String jsbm) {
    	Result res = new Result();
    	List<Sys_Menu> list = dao.getByJSBM(jsbm);
    	
    	Sys_Menu item = new Sys_Menu();
    	item.setCDBM("0");
    	item.setCDMC("菜单管理");
    	item.setSJBM("-1");
    	list.add(item);
    	
    	JSONArray jr = new JSONArray();
    	JSONObject obj =new JSONObject();
    	obj.put("cdbm", "0");
    	obj.put("name", "子系统");
    	obj.put("pageurl", "");
    	obj.put("px", "");
    	obj.put("bz", "");
    	obj.put("spread", "true");
    	JSONArray child = bindTree(obj, list);
    	if(child.size() > 0) {
    		obj.put("children", child);
		}
    	jr.add(obj);
    	
    	res.setCode("1");
    	res.setData(jr);
        return res;
    }
}