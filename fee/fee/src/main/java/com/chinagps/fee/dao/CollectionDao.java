package com.chinagps.fee.dao;

import java.util.List;
import java.util.Map;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.entity.po.Collection;
import com.chinagps.fee.entity.po.Info;

/**
 * @Package:com.chinagps.fee.dao
 * @ClassName:CollectionDao
 * @Description:托收资料数据持久层接口
 * @author:zfy
 * @date:2014-6-4 下午2:36:06
 */
public interface CollectionDao extends BaseDao {
	/**
	 * @Title:updatePrintFreqByCustomerId
	 * @Description:修改用户发票打印频率
	 * @param collection
	 * @return
	 * @throws SystemException
	 */
	public int updatePrintFreqByCustomerId(Collection collection)throws SystemException;
	
	/**
	 * @Title:updateDeliveryByIds
	 * @Description:批量设置用户发票是否投递
	 * @param collectionIds
	 * @param isDelivery
	 * @return
	 * @throws SystemException
	 */
	public int updateDeliveryByIds(List<Long> collectionIds,Integer isDelivery) throws SystemException;
	
	/**
	 * @Title:findCollections
	 * @Description:查询特殊投递用户的记录
	 * @param conditionMap
	 * @param order
	 * @param isDesc
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws SystemException
	 */
	public List<Collection> findCollections(Map<String, Object> conditionMap, String order,boolean isDesc,int pageNo, int pageSize)throws SystemException;
	/**
	 * @Title:countCollections
	 * @Description:获得特殊投递用户的记录数
	 * @param conditionMap
	 * @return
	 * @throws SystemException
	 */
	public int countCollections(Map<String, Object> conditionMap) throws SystemException;
	
	/**
	 * @Title:findCollectionFees
	 * @Description:查询车台托收资料、收费信息
	 * @param conditionMap
	 * @return
	 * @throws SystemException
	 */
	public List<Map<String,Object>> findCollectionFees(Map<String, Object> conditionMap) throws SystemException;
	
	/**
	 * @Title:findFeeInfos
	 * @Description:查询车台每一个类型的收费金额
	 * @param feeIds
	 * @param isArrearage 是否需要查欠费数据
	 * @param feeSedate 服务截止日期
	 * @return
	 * @throws SystemException
	 */
	public List<Map<String,Object>> findFeeInfos(String feeIds,boolean isArrearage,String feeSedate) throws SystemException;
	
}

