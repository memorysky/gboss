package com.chinagps.fee.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.dao.ServiceItemDao;
import com.chinagps.fee.entity.po.Serviceitem;
import com.chinagps.fee.service.ServiceItemService;
import com.chinagps.fee.util.DateUtil;

/**
 * @Package:com.chinagps.fee.service.impl
 * @ClassName:ServiceItemServiceImp
 * @Description:服务项管理业务层实现类
 * @author:zfy
 * @date:2013-8-9 下午2:30:53
 */
@Service("serviceItemService")
@Transactional(value="mysql1TxManager")
public class ServiceItemServiceImp extends BaseServiceImpl implements
		ServiceItemService {
	protected static final Logger LOGGER = LoggerFactory.getLogger(ServiceItemServiceImp.class);
	
	@Autowired  
	@Qualifier("serviceItemDao")
	private ServiceItemDao serviceItemDao;
	
	@Override
	public List<Serviceitem> findServiceitem(Serviceitem serviceitem) throws SystemException{
		return serviceItemDao.findServiceitem(serviceitem);
	}

	@Override
	public int addServiceitem(Serviceitem serviceitem) throws SystemException {
		int result=1;
		if(serviceItemDao.checkSeviceItemCode(serviceitem)){
			result=2;
		}else{
			if(serviceItemDao.checkSeviceItemName(serviceitem)){
				result=3;
			}else{
				serviceItemDao.save(serviceitem);
			}
		}
		return result;
	}

	@Override
	public int updateServiceitem(Serviceitem serviceitem)
			throws SystemException {
		int result=1;
		if(serviceitem == null || serviceitem.getItemId()==null){
			return -1;
		}
		//修改之前，判断存在不存在
		if(serviceItemDao.get(Serviceitem.class, serviceitem.getItemId())!=null){
	
			if(serviceItemDao.checkSeviceItemCode(serviceitem)){
				result=2;
			}else{
				if(serviceItemDao.checkSeviceItemName(serviceitem)){
					result=3;
				}else{
					serviceItemDao.merge(serviceitem);
				}
			}
		}else{
			result=0;
		}
		return result;
	}

	@Override
	public int deleteServiceitem(Serviceitem serviceitem)
			throws SystemException {
		int result=1;
		if(serviceitem == null || serviceitem.getItemId()==null){
			return -1;
		}
		//操作之前，判断存在不存在
		if(serviceItemDao.get(Serviceitem.class, serviceitem.getItemId())!=null){
			if(serviceItemDao.checkItemIsUsing(serviceitem.getItemId())){
				result=2;
			}else{
				serviceItemDao.delete(Serviceitem.class, serviceitem.getItemId());
			}
		}else{
			result=0;
		}
		return result;
	}

}

