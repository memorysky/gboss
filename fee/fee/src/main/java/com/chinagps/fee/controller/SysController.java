package com.chinagps.fee.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinagps.fee.comm.SystemConst;
import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.entity.po.sys.Operatelog;
import com.chinagps.fee.entity.po.sys.SysValue;
import com.chinagps.fee.service.SysService;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.page.Page;

/**
 * @Package:com.chinagps.fee.controller
 * @ClassName:SysController
 * @Description:系统参数类型、值 控制层
 * @author:zfy
 * @date:2014-6-11 上午9:39:21
 */
@Controller
@RequestMapping(value="/sys")
public class SysController extends BaseController {
	protected static final Logger LOGGER = LoggerFactory
			.getLogger(SysController.class);

	@Autowired
	@Qualifier("sysService")
	private SysService sysService; 

	/**
	 * @Title:findSysValue
	 * @Description:查询系统参数值
	 * @param sysValue
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "findSysValue")
	public @ResponseBody
	 List<SysValue> findSysValue(
			@RequestBody SysValue sysValue,
			 HttpServletRequest request) throws SystemException {
		List<SysValue> result = null;
		try {
			if(sysValue!=null){
				sysValue.setIsDel(0);
			}
			result = sysService.findSysValue(sysValue);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			// 打印
			e.printStackTrace();
			// 同时把异常抛到前台
			throw new SystemException();
		}
		return result;
	}
	
	/**
	 * @Title:findOperatelogPage
	 * @Description:分页查询操作日志
	 */
	@RequestMapping(value = "findOperatelogPage")
	public @ResponseBody
	Page<Map<String,Object>> findOperatelogPage(
			@RequestBody PageSelect<Operatelog> pageSelect,
			BindingResult bindingResult, HttpServletRequest request)
			throws SystemException {
		Page<Map<String,Object>> results = null;
		try {
			String companyId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_COMPANYID);
			if (pageSelect != null) {
				Map conditionMap = pageSelect.getFilter();
				if (conditionMap == null) {
					conditionMap = new HashMap();
				}
				conditionMap.put("subcoNo", companyId);
			}
			results = sysService.findOperatelogPage(pageSelect);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			// 打印日志
			e.printStackTrace();
			// 同时把异常抛到前台
			throw new SystemException();
		}
		return results;
	}
}
