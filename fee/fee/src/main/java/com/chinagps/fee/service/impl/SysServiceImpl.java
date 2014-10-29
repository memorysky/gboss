package com.chinagps.fee.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.dao.PaymentDao;
import com.chinagps.fee.dao.SysDao;
import com.chinagps.fee.entity.po.Servicepack;
import com.chinagps.fee.entity.po.sys.Operatelog;
import com.chinagps.fee.entity.po.sys.SysValue;
import com.chinagps.fee.service.SysService;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.Utils;
import com.chinagps.fee.util.page.Page;
import com.chinagps.fee.util.page.PageUtil;

/**
 * @Package:com.chinagps.fee.service.impl
 * @ClassName:SysServiceImpl
 * @Description:系统参数类型、值 业务层实现类
 * @author:zfy
 * @date:2014-6-11 上午9:37:47
 */
@Service("sysService")
@Transactional(value="mysql1TxManager")
public class SysServiceImpl extends BaseServiceImpl implements SysService {
	
	@Autowired  
	@Qualifier("sysDao")
	private SysDao sysDao;
	
	@Override
	public List<SysValue> findSysValue(SysValue sysValue)
			throws SystemException {
		return sysDao.findSysValue(sysValue);
	}

	@Override
	public Page<Map<String,Object>> findOperatelogPage(PageSelect<Operatelog> pageSelect)
			throws SystemException {
		int total=sysDao.countOplogs(pageSelect.getFilter());
		List<Map<String,Object>> operatelogs=sysDao.findOplogs(pageSelect.getFilter(), pageSelect.getOrder(), pageSelect.getIs_desc(),pageSelect.getPageNo(),pageSelect.getPageSize());
		return PageUtil.getPage(total, pageSelect.getPageNo(), operatelogs, pageSelect.getPageSize());
	}

	@Override
	public Map<String, String> findSysValueToMap(SysValue sysValue)
			throws SystemException {
		List<SysValue> list=sysDao.findSysValue(sysValue);
		Map<String, String> resultMap=null;
		if(Utils.isNotNullOrEmpty(list)){
			resultMap=new HashMap<String, String>();
			for (SysValue sysValue2 : list) {
				resultMap.put(sysValue2.getSvalue(), sysValue2.getSname());
			}
		}
		return resultMap;
	}

}

