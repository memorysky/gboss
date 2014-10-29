package com.chinagps.fee.dao;

import java.util.List;
import java.util.Map;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.entity.po.Bill;
import com.chinagps.fee.entity.po.Paytxt;


/**
 * @Package:com.chinagps.fee.dao
 * @ClassName:BillDao
 * @Description:账单数据持久层接口
 * @author:zfy
 * @date:2014-5-20 下午7:04:34
 */
public interface BillDao extends BaseDao {
	/**
	 * @Title:findBills
	 * @Description:查询账单
	 * @param conditionMap
	 * @return
	 * @throws SystemException
	 */
	public List<Bill> findBills(Map<String, Object> conditionMap)throws SystemException;
}

