package com.chinagps.fee.dao;

import java.util.List;
import java.util.Map;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.entity.po.Collection;
import com.chinagps.fee.util.page.Page;

/**
 * @Package:com.chinagps.fee.dao
 * @ClassName:ReprintinvoiceDao
 * @Description:发票补打数据持久层接口
 * @author:zfy
 * @date:2014-6-9 下午3:32:41
 */
public interface ReprintinvoiceDao extends BaseDao {
	/**
	 * @Title:findRePrints
	 * @Description:查询补打发票的记录
	 * @param conditionMap
	 * @param order
	 * @param isDesc
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws SystemException
	 */
	public List<Map<String, Object>> findRePrints(Map<String, Object> conditionMap, String order,boolean isDesc,int pageNo, int pageSize)throws SystemException;
	/**
	 * @Title:countRePrints
	 * @Description:获得需要补打发票的记录数
	 * @param conditionMap
	 * @return
	 * @throws SystemException
	 */
	public int countRePrints(Map<String, Object> conditionMap) throws SystemException;
	
	/**
	 * @Title:updateRePrints
	 * @Description:删除补打发票的设置、修改发票是否打印标记
	 * @param rePrintIds
	 * @param isDel
	 * @return
	 * @throws SystemException
	 */
	public int updateRePrints(Map<String, Object> conditionMap,Integer isDel,Integer isPrinted)throws SystemException;
	
	/**
	 * @Title:updateRePrints
	 * @Description:删除补打发票的设置、修改发票是否打印标记
	 * @param rePrintIds
	 * @param isDel
	 * @return
	 * @throws SystemException
	 */
	public int updateRePrints(List<Long> rePrintIds,Integer isDel,Integer isPrinted)throws SystemException;

}

