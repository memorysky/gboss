package com.chinagps.fee.controller;

import javax.servlet.http.HttpServletRequest;

import ldap.oper.OpenLdap;
import ldap.oper.OpenLdapManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinagps.fee.comm.SystemConst;
import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.entity.po.Vehicle;
import com.chinagps.fee.service.VehicleService;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.StringUtils;
import com.chinagps.fee.util.page.Page;

/**
 * @Package:com.gboss.controller
 * @ClassName:VehicleController
 * @Description:从门店copy过来，以后可能会改成静态数据（搜索引擎）
 * @author:bzhang
 * @date:2014-6-5 下午3:39:01
 */
@Controller
@RequestMapping(value="/ba")
public class VehicleController extends BaseController {
	protected static final Logger LOGGER = LoggerFactory
			.getLogger(VehicleController.class);
	
	@Autowired
	@Qualifier("vehicleService")
	private VehicleService vehicleService;

	@RequestMapping(value = "/getVehicles", method = RequestMethod.POST)
	public @ResponseBody Page<Vehicle> getVehicles(@RequestBody PageSelect<Vehicle> pageSelect, BindingResult bindingResult,HttpServletRequest request) throws SystemException {
		String companyid = (String) request.getSession().getAttribute(SystemConst.ACCOUNT_COMPANYID);
		Long subco=null;
		Page<Vehicle> result=null;
		try {
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
			result = vehicleService.search(pageSelect, subco);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			// 打印日志
			e.printStackTrace();
		}
		return result;
	}

}

