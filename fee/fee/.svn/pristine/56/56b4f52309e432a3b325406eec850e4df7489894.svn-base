package com.chinagps.fee.controller;

import javax.servlet.http.HttpServletRequest;

import ldap.oper.OpenLdap;
import ldap.oper.OpenLdapManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinagps.fee.comm.SystemConst;
import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.entity.po.Customer;
import com.chinagps.fee.service.CustomerService;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.StringUtils;
import com.chinagps.fee.util.page.Page;

/**
 * @Package:com.chinagps.fee.controller
 * @ClassName:CustomerController
 * @Description:从门店copy过来，以后可能会改成静态数据（搜索引擎）
 * @author:zfy
 * @date:2014-6-11 上午10:50:38
 */
@Controller
@RequestMapping(value="/ba")
public class CustomerController extends BaseController{
	protected static final Logger LOGGER = LoggerFactory
			.getLogger(CustomerController.class);
	@Autowired
	private CustomerService customerService; 
	
	
	@RequestMapping(value = "getCustomers", method = RequestMethod.POST)
	public @ResponseBody Page<Customer> list(@RequestBody PageSelect<Customer> pageSelect, BindingResult bindingResult,HttpServletRequest request) throws SystemException{
		String companyid = (String) request.getSession().getAttribute(SystemConst.ACCOUNT_COMPANYID);
		Page<Customer> list=null;
		try {
			Long subco=null;
			String orgId=(String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_ORGID);
			if (StringUtils.isNotBlank(orgId)) {
				OpenLdap openLdap = OpenLdapManager.getInstance();
				//判断是否是分公司
				int level=openLdap.getUserCompanyLevel(orgId);
				if(level==0){//总部
					
				}else{
					subco=Long.valueOf(companyid);
				}
			}
			list = customerService.search(pageSelect, subco);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return list;
	}
	
}