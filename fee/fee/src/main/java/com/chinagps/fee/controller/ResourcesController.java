package com.chinagps.fee.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import ldap.objectClasses.CommonModule;
import ldap.objectClasses.CommonOperator;
import ldap.objectClasses.CommonRole;
import ldap.oper.OpenLdap;
import ldap.oper.OpenLdapManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinagps.fee.comm.SystemConst;
import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.entity.po.Subco;
import com.chinagps.fee.service.SubcoService;
import com.chinagps.fee.util.StringUtils;
import com.chinagps.fee.util.Utils;

@Controller
public class ResourcesController extends BaseController{
	@Autowired
	@Qualifier("subcoService")
	private SubcoService subcoService; 
	
	@RequestMapping(value = "/moduleList", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> moduleList(@Validated CommonModule commonModule, BindingResult bindingResult,HttpServletRequest request) throws SystemException{
		List<Map<String, Object>> list=null;
		try {
			//查询出该分公司有哪些托收机构
			String companyId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_COMPANYID);
			String orgId=(String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_ORGID);
			Map<String,Object>	map=new HashMap<String,Object>();
			map.put("subcoNo", companyId);
			map.put("flag", 1);
			List<Subco> subcos = subcoService.findSubco(map);
			List<Integer> agencys=new ArrayList<Integer>();
			if(Utils.isNotNullOrEmpty(subcos)){
				for (Subco subco : subcos) {
					agencys.add(subco.getAgency());
				}
			}
			
			CommonOperator operator = (CommonOperator) request.getSession().getAttribute(SystemConst.ACCOUNT_USER);
			if(operator==null){
				return new ArrayList<Map<String, Object>>();
			}
			OpenLdap ldap = OpenLdapManager.getInstance();
			//判断是否是分公司
			int level=ldap.getUserCompanyLevel(orgId);
			
			String filt = "";
			if(commonModule.getModuleid()==null||commonModule.getModuleid().equals("")){
				filt = "(&(objectclass=commonModule)(systemid="+SystemConst.SYSTEMID+"))";
			}else{
				filt = "(&(objectclass=commonModule)(systemid="+SystemConst.SYSTEMID+")(parentmoduleid="+commonModule.getModuleid()+"))";
			}
			List<CommonModule> moduleList = ldap.searchModule("", filt);
			List<String> modules = new ArrayList<String>();
			List<String> roleids = null;
			if(request.getSession().getAttribute(SystemConst.ACCOUNT_ROLEIDS)!=null){
				roleids = (List<String>)request.getSession().getAttribute(SystemConst.ACCOUNT_ROLEIDS);
			}
			if(roleids==null ){
				return new ArrayList<Map<String, Object>>();
			}
			for (String roleid : roleids) {
				String filter = "(&(objectclass=commonRole)(roleid="+roleid+"))";
				List<CommonRole> roleList = ldap.searchRole("", filter);
				for(CommonRole role:roleList){
					if(!modules.contains(role.getModuleids())){
						modules.addAll(role.getModuleids());
					}
				}
			}
			Map<String, Map<String, Object>> temp = new HashMap<String, Map<String, Object>>();
			list = new ArrayList<Map<String, Object>>();
			for(int i=0;i<moduleList.size();i++){
				CommonModule module = moduleList.get(i);
				
				//查看有哪些托收机构,没有的就隐藏那个托收机构
				if(level!=0){//如果是分公司
					if(module.getModulename().indexOf("金融中心")>-1){
						if(!agencys.contains(2)){
							continue;
						}
					}else if(module.getModulename().indexOf("银盛")>-1){
						if(!agencys.contains(1)){
							continue;
						}
					}else if(module.getModulename().indexOf("银联")>-1){
						if(!agencys.contains(3)){
							continue;
						}
					}
				}
				
				String menuId = module.getModuleid();
				if(modules!=null&&!modules.contains(menuId)){
					continue;
				}
				String parentModuleId = module.getParentmoduleid();
				Map<String, Object> item = new HashMap<String, Object>();			
				temp.put(menuId, item);
				if(parentModuleId == null){
					list.add(item);
				}
				
				item.put("name", module.getModulename());
				item.put("url", module.getControl1());
				item.put("type", module.getControl2());
				item.put("id", menuId);
				item.put("parentId", module.getParentmoduleid());
				initControl2(item, module.getControl2());
				
				Map<String, Object> parent = temp.get(parentModuleId);
				if(parent != null){
					//List<MenuItem> childList = parent.getMenu();
					Object childList = parent.get("items");
					if(childList == null){
						parent.put("items", new ArrayList<Map<String, Object>>());
					}
					List<Map<String, Object>> childListAgin = (List<Map<String, Object>>) parent.get("items");
					childListAgin.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	private void initControl2(Map<String, Object> item, String control2){
		if(StringUtils.isBlank(control2)){
			return;
		}
		String []configs = control2.split(",");
		for(String config: configs){
			int sp = config.indexOf(":");
			if(sp > 0){
				item.put(config.substring(0, sp), config.substring(sp + 1));
			}
		}
	}
	
}
