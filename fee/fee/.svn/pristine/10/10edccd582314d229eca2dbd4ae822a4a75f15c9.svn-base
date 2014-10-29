package com.chinagps.fee.service;

import java.util.List;
import java.util.Map;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.entity.po.sys.Operatelog;
import com.chinagps.fee.entity.po.sys.SysValue;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.page.Page;

/**
 * @Package:com.chinagps.fee.service
 * @ClassName:SysService
 * @Description:系统参数类型、值 业务层接口
 * @author:zfy
 * @date:2014-6-11 上午9:36:52
 */
public interface SysService extends BaseService{
	/**
	 * @Title:findSysValue
	 * @Description:查询系统参数值
	 * @param sysValue
	 * @return
	 * @throws SystemException
	 */
	public List<SysValue> findSysValue(SysValue sysValue) throws SystemException;
	/**
	 * @Title:findSysValueToMap
	 * @Description:查询系统参数值，转成Map对象
	 * @param sysValue
	 * @return
	 * @throws SystemException
	 */
	public Map<String,String> findSysValueToMap(SysValue sysValue) throws SystemException;
	
	/**
	 * @Title:findOperatelogPage
	 * @Description:分页查询操作日志
	 * @param pageSelect
	 * @return
	 * @throws SystemException
	 */
	public Page<Map<String,Object>> findOperatelogPage(PageSelect<Operatelog> pageSelect) throws SystemException;
	
}

