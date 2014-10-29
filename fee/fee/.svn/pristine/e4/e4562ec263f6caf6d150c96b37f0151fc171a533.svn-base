package com.chinagps.fee.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.entity.po.Unit;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.page.Page;

/**
 * @Package:com.gboss.dao
 * @ClassName:UnitDao
 * @Description:TODO 从门店copy过来，以后可能会改成静态数据（搜索引擎）
 * @author:xiaoke
 * @date:2014-3-24 下午3:07:06
 */
public interface UnitDao extends BaseDao {
	
	public Page<Unit> search(PageSelect<Unit> pageSelect, Long subco_no);
	/**
	 * @Title:findUnitInNets
	 * @Description:查询入网车辆列表  入网明细表
	 * @param conditionMap
	 * @param order
	 * @param isDesc
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws SystemException
	 */
	public List<HashMap<String, Object>> findUnitInNets(Map<String, Object> conditionMap, String order,boolean isDesc, int pageNo, int pageSize) throws SystemException;
	
	/**
	 * @Title:countUnitInNets
	 * @Description:查询入网车辆记录数  入网明细表
	 * @param conditionMap
	 * @return
	 * @throws SystemException
	 */
	public int countUnitInNets(Map<String,Object> conditionMap) throws SystemException;
}

