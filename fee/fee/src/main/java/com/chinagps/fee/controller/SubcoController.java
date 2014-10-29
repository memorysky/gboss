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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinagps.fee.comm.SystemConst;
import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.entity.po.Subco;
import com.chinagps.fee.service.SubcoService;
import com.chinagps.fee.util.LogOperation;

/**
 * @Package:com.chinagps.fee.controller
 * @ClassName:SubcoController
 * @Description:公司托收账号信息管理控制层
 * @author:zfy
 * @date:2014-5-27 下午3:41:45
 */
@Controller
@RequestMapping(value="/service")
public class SubcoController extends BaseController {
	protected static final Logger LOGGER = LoggerFactory
			.getLogger(SubcoController.class);

	@Autowired
	@Qualifier("subcoService")
	private SubcoService subcoService; 

	@RequestMapping(value = "findSubco")
	public @ResponseBody
	 List<Subco> findSubco(
			@RequestBody Map<String,Object> map,
			 HttpServletRequest request) throws SystemException {
		List<Subco> result = null;
		try {
			String companyId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_COMPANYID);
			if(map==null){
				map=new HashMap<String,Object>();
			}
			map.put("subcoNo", companyId);
			result = subcoService.findSubco(map);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			// 打印
			e.printStackTrace();
			// 同时把异常抛到前台
			throw new SystemException();
		}
		return result;
	}

	@RequestMapping(value = "addSubco")
	@LogOperation(description = "添加分公司托收账号", op_type = SystemConst.OPERATELOG_ADD, model_id = 30030)
	public @ResponseBody
	 HashMap<String, Object> addSubco(
			@RequestBody  Subco subco,
			 HttpServletRequest request) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean flag = true;
		String msg = SystemConst.OP_SUCCESS;
		try {
		
			String userId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_ID);
			String companyId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_COMPANYID);
			subco.setOpId(Long.valueOf(userId));
			subco.setSubcoNo((Long.valueOf(companyId)));
			int result = subcoService.addSubco(subco);
			if (result == -1) {
				flag = false;
				msg = "参数不合法";
			} 
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			flag = false;
			msg = SystemConst.OP_FAILURE;
			e.printStackTrace();
		}
		resultMap.put(SystemConst.SUCCESS, flag);
		resultMap.put(SystemConst.MSG, msg);
		return resultMap;
	}

	@RequestMapping(value = "getSubco")
	public @ResponseBody
	Subco getSubco(
			@RequestParam Long id,
			 HttpServletRequest request) throws SystemException {
		Subco result = null;
		try {
			if(id!=null){
				result = subcoService.get(Subco.class, id);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			// 打印
			e.printStackTrace();
			// 同时把异常抛到前台
			throw new SystemException();
		}
		return result;
	}

	@RequestMapping(value = "updateSubco")
	@LogOperation(description = "修改分公司托收账号", op_type = SystemConst.OPERATELOG_UPDATE, model_id = 30030)
	public @ResponseBody
	 HashMap<String, Object> updateSubco(
			@RequestBody Subco subco,
			 BindingResult bindingResult,  HttpServletRequest request) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean flag = true;
		String msg = SystemConst.OP_SUCCESS;
		try {
			String userId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_ID);
			String companyId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_COMPANYID);
			subco.setOpId(Long.valueOf(userId));
			subco.setSubcoNo((Long.valueOf(companyId)));
			int result = subcoService.updateSubco(subco);
			if (result == -1) {
				flag = false;
				msg = "参数不合法";
			} else if (result == 0) {
				flag = false;
				msg = "要操作的对象不存在";
			} 
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			flag = false;
			msg = SystemConst.OP_FAILURE;
			e.printStackTrace();
		}
		resultMap.put(SystemConst.SUCCESS, flag);
		resultMap.put(SystemConst.MSG, msg);
		return resultMap;
	}

	@RequestMapping(value = "deleteSubco", method = RequestMethod.POST)
	@LogOperation(description = "删除分公司托收账号", op_type = SystemConst.OPERATELOG_DEL, model_id = 30030)
	public @ResponseBody
	 HashMap<String, Object> deleteSubco(
			@RequestBody  Subco cubco,
			 BindingResult bindingResult, HttpServletRequest request) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean flag = true;
		String msg = SystemConst.OP_SUCCESS;
		
			try {
					int result = subcoService
							.deleteSubco(cubco);
					if (result == -1) {
						flag = false;
						msg = "参数不合法";
					}else if (result == 0) {
						flag = false;
						msg = "要操作的对象不存在";
					} 
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				flag = false;
				msg = SystemConst.OP_FAILURE;
				e.printStackTrace();
			}
		resultMap.put(SystemConst.SUCCESS, flag);
		resultMap.put(SystemConst.MSG, msg);
		return resultMap;
	}
	
}
