package com.chinagps.fee.controller;

import java.util.HashMap;
import java.util.List;

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
import com.chinagps.fee.entity.po.Serviceitem;
import com.chinagps.fee.service.ServiceItemService;
import com.chinagps.fee.util.LogOperation;
import com.chinagps.fee.util.StringUtils;

/**
 * @Package:com.chinagps.fee.controller.sel
 * @ClassName:ServiceItemController
 * @Description:服务项管理控制层
 * @author:zfy
 * @date:2013-8-9 下午3:24:14
 */
@Controller
@RequestMapping(value="/service")
public class ServiceItemController extends BaseController {
	protected static final Logger LOGGER = LoggerFactory
			.getLogger(ServiceItemController.class);

	@Autowired
	@Qualifier("serviceItemService")
	private ServiceItemService serviceItemService; 

	@RequestMapping(value = "findServiceItem")
	public @ResponseBody
	 List<Serviceitem> findServiceItem(
			@RequestBody Serviceitem serviceitem,
			 HttpServletRequest request) throws SystemException {
		List<Serviceitem> result = null;
		try {
			String userId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_ID);
			result = serviceItemService.findServiceitem(serviceitem);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			// 打印
			e.printStackTrace();
			// 同时把异常抛到前台
			throw new SystemException();
		}
		return result;
	}

	@RequestMapping(value = "addServiceitem")
	@LogOperation(description = "添加服务项", op_type = SystemConst.OPERATELOG_ADD, model_id = 30031)
	public @ResponseBody
	 HashMap<String, Object> addServiceitem(
			@RequestBody  Serviceitem serviceitem,
			 HttpServletRequest request) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean flag = true;
		String msg = SystemConst.OP_SUCCESS;
		try {
		
			String userId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_ID);
			if (StringUtils.isNotBlank(userId)) {
				serviceitem.setOpId(Long.valueOf(userId));
			}
			int result = serviceItemService.addServiceitem(serviceitem);
			if (result == -1) {
				flag = false;
				msg = "参数不合法";
			} else if (result == 2) {
				flag = false;
				msg = "服务项编号为[" + serviceitem.getItemCode() + "]的已存在";
			} else if (result == 3) {
				flag = false;
				msg = "服务项名称为[" + serviceitem.getItemName() + "]的已存在";
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

	/**
	 * @Title:getServiceItem
	 * @Description:根据id查询服务项
	 * @param id
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "getServiceItem")
	public @ResponseBody
	 Serviceitem getServiceItem(
			@RequestParam(value = "id", required = false)  Long id,
			 HttpServletRequest request) throws SystemException {
		Serviceitem result = null;
		try {
			result = serviceItemService.get(Serviceitem.class, id);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			// 打印
			e.printStackTrace();
			// 同时把异常抛到前台
			throw new SystemException();
		}
		return result;
	}

	@RequestMapping(value = "updateServiceitem")
	@LogOperation(description = "修改服务项", op_type = SystemConst.OPERATELOG_UPDATE, model_id = 30031)
	public @ResponseBody
	 HashMap<String, Object> updateServiceitem(
			@RequestBody  Serviceitem serviceitem,
			 BindingResult bindingResult,  HttpServletRequest request) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean flag = true;
		String msg = SystemConst.OP_SUCCESS;
		try {
			String userId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_ID);
			if (StringUtils.isNotBlank(userId)) {
				serviceitem.setOpId(Long.valueOf(userId));
			}
			int result = serviceItemService.updateServiceitem(serviceitem);
			if (result == -1) {
				flag = false;
				msg = "参数不合法";
			} else if (result == 0) {
				flag = false;
				msg = "要操作的服务项不存在";
			} else if (result == 2) {
				flag = false;
				msg = "服务项编号为[" + serviceitem.getItemCode() + "]的已存在";
			} else if (result == 3) {
				flag = false;
				msg = "服务项名称为[" + serviceitem.getItemName() + "]的已存在";
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

	@RequestMapping(value = "deleteServiceitem", method = RequestMethod.POST)
	@LogOperation(description = "删除服务项", op_type = SystemConst.OPERATELOG_DEL, model_id = 30031)
	public @ResponseBody
	 HashMap<String, Object> deleteServiceitem(
			@RequestBody  Serviceitem serviceitem,
			 BindingResult bindingResult, HttpServletRequest request) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean flag = true;
		String msg = SystemConst.OP_SUCCESS;
		
			try {
				if (serviceitem == null || serviceitem.getItemId() == null) {
					flag = false;
					msg = "参数不合法";
				} else {
					int result = serviceItemService
							.deleteServiceitem(serviceitem);
					if (result == 0) {
						flag = false;
						msg = "要操作的服务项不存在";
					} else if (result == 2) {
						flag = false;
						msg = "该服务项在使用中，不能删除!";
					}
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
