package com.chinagps.fee.service;

import java.util.List;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.entity.po.Serviceitem;

/**
 * @Package:com.chinagps.fee.service
 * @ClassName:ServiceItemService
 * @Description:服务项管理业务层接口
 * @author:zfy
 * @date:2013-8-9 下午2:22:38
 */
public interface ServiceItemService extends BaseService{
	
	/**
	 * @Title:findServiceitem
	 * @Description:查询服务项
	 * @param serviceitem
	 * @return
	 * @throws SystemException
	 * @throws
	 */
	public List<Serviceitem> findServiceitem(Serviceitem serviceitem) throws SystemException;
	
	/**
	 * @Title:addServiceitem
	 * @Description:添加服务项
	 * @param serviceitem
	 * @return 1:成功，2：编号已存在，3：名称已存在
	 * @throws SystemException
	 * @throws
	 */
	public int addServiceitem(Serviceitem serviceitem) throws SystemException;
	
	/**
	 * @Title:updateServiceitem
	 * @Description:添加服务项
	 * @param serviceitem
	 * @return 1:成功，2：编号已存在，3：名称已存在
	 * @throws SystemException
	 * @throws
	 */
	public int updateServiceitem(Serviceitem serviceitem) throws SystemException;
	
	/**
	 * @Title:deleteServiceitem
	 * @Description:删除服务项
	 * @param serviceitem
	 * @return 1:成功，2：服务项在使用中,不能删除
	 * @throws SystemException
	 * @throws
	 */
	public int deleteServiceitem(Serviceitem serviceitem) throws SystemException;
}

