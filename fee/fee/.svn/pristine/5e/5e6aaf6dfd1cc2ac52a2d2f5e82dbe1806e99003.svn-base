package com.chinagps.fee.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.dao.UnitDao;
import com.chinagps.fee.entity.po.Unit;
import com.chinagps.fee.service.UnitService;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.page.Page;
import com.chinagps.fee.util.page.PageUtil;

/**
 * @Package:com.gboss.service.impl
 * @ClassName:UnitServiceImpl
 * @Description:TODO
 * @author:xiaoke
 * @date:2014-3-24 下午3:45:05
 */

@Service("UnitService")
@Transactional(value="mysql1TxManager")
public class UnitServiceImpl extends BaseServiceImpl implements UnitService {
	
	@Autowired
	@Qualifier("UnitDao")
	private UnitDao unitDao;


	@Override
	public Page<Unit> search(PageSelect<Unit> pageSelect, Long subco_no) {
		return unitDao.search(pageSelect, subco_no);
	}


	@Override
	public Page<HashMap<String, Object>> findUnitInNetsBypage(
			PageSelect<Map<String, Object>> pageSelect) throws SystemException {
		List<HashMap<String, Object>> dataList=unitDao.findUnitInNets(pageSelect.getFilter(), pageSelect.getOrder(), pageSelect.getIs_desc(), pageSelect.getPageNo(), pageSelect.getPageSize());
		int total = unitDao.countUnitInNets(pageSelect.getFilter());
		return PageUtil.getPage(total, pageSelect.getPageNo(), dataList, pageSelect.getPageSize());
	}

	@Override
	public List<HashMap<String, Object>> findAllUnitInNets(
			Map<String, Object> conditionMap) throws SystemException {
		return unitDao.findUnitInNets(conditionMap,null,false,0,0);
	}
}

