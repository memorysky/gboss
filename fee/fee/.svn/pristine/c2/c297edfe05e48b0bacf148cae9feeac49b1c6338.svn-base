package com.chinagps.fee.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.entity.po.Unit;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.page.Page;

/**
 * @Package:com.gboss.service
 * @ClassName:UnitService
 * @Description:从门店copy过来，以后可能会改成静态数据（搜索引擎）
 * @author:xiaoke
 * @date:2014-3-24 下午3:40:48
 */
public interface UnitService extends BaseService {
	
	public Page<Unit> search(PageSelect<Unit> pageSelect, Long subco_no);
	/**
	 * @Title:findUnitInNetsBypage
	 * @Description:分页查询入网车辆列表  服务开通报表
	 * @param pageSelect
	 * @return
	 * @throws SystemException
	 */
	public Page<HashMap<String, Object>> findUnitInNetsBypage(PageSelect<Map<String, Object>> pageSelect) throws SystemException;
	/**
	 * @Title:findAllUnitInNets
	 * @Description:查询所有入网车辆列表  服务开通报表
	 * @param conditionMap
	 * @return
	 * @throws SystemException
	 */
	public List<HashMap<String, Object>> findAllUnitInNets(Map<String, Object> conditionMap) throws SystemException;
}

