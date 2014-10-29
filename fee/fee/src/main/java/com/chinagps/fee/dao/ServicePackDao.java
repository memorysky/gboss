package com.chinagps.fee.dao;

import java.util.List;
import java.util.Map;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.entity.po.Packprice;
import com.chinagps.fee.entity.po.Servicepack;


/**
 * @Package:com.chinagps.fee.dao
 * @ClassName:ServicePackDao
 * @Description:服务套餐管理数据持久层接口
 * @author:zfy
 * @date:2013-8-9 下午2:20:47
 */
public interface ServicePackDao extends BaseDao {
	/**
	 * @Title:checkServicePackCode
	 * @Description:判断套餐编号是否存在
	 * @param servicepack
	 * @return true:已存在；false:不存在
	 * @throws SystemException
	 * @throws
	 */
	public boolean checkServicePackCode(Servicepack servicepack) throws SystemException;
	/**
	 * @Title:checkServicePackName
	 * @Description:判断套餐名称是否存在
	 * @param servicepack
	 * @return true:已存在；false:不存在
	 * @throws SystemException
	 * @throws
	 */
	public boolean checkServicePackName(Servicepack servicepack) throws SystemException;
	
	/**
	 * @Title:deletePackItemByPackId
	 * @Description:根据套餐ID删除套餐服务项关系
	 * @return
	 * @throws SystemException
	 * @throws
	 */
	public int deletePackItemByPackId(Long packId) throws SystemException;
	
	/**
	 * @Title:deletePackPriceByPackId
	 * @Description:根据套餐ID删除套餐价格关系
	 * @return
	 * @throws SystemException
	 * @throws
	 */
	public int deletePackPriceByPackId(Long packId) throws SystemException;
	
	/**
	 * @Title:findPackPrices
	 * @Description:根据套餐ID集合查询相关的用户类型价格信息
	 * @param packIds
	 * @return
	 * @throws SystemException
	 * @throws
	 */
	public List<Packprice> findPackPrices(List<Long> packIds) throws SystemException;
	/**
	 * @Title:findPackItems
	 * @Description:根据套餐ID集合查询相关的服务项信息
	 * @param packIds
	 * @return
	 * @throws SystemException
	 * @throws
	 */
	public List<Map<Long, Object>> findPackItems(List<Long> packIds) throws SystemException;
	
	/**
	 * @Title:findServicePacks
	 * @Description:分页查询服务套餐
	 * @param servicepack
	 * @param order
	 * @param isDesc
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws SystemException
	 */
	public List<Servicepack> findServicePacks(Map conditionMap, String order,boolean isDesc,int pageNo,int pageSize) throws SystemException;
	
	/**
	 * @Title:findServicePacks
	 * @Description:查询套餐
	 * @param conditionMap
	 * @return
	 * @throws SystemException
	 */
	public List<Servicepack> findServicePacks(Map conditionMap) throws SystemException;
	
	/**
	 * @Title:countServicePacks
	 * @Description:查询套餐总数
	 * @param servicepack
	 * @return
	 * @throws SystemException
	 * @throws
	 */
	public int countServicePacks(Map conditionMap) throws SystemException;
	
	/**
	 * @Title:updateIsVerifiedByIds
	 * @Description:批量修改审核状态
	 * @param ids
	 * @return
	 * @throws SystemException
	 * @throws
	 */
	public int updateIsVerifiedByIds(List<Long> ids,Long verifyUserId,String verifiedTime) throws SystemException;
	
	/**
	 * @Title:updateIsValidByIds
	 * @Description:批量设置套餐对新客户是否生效
	 * @param ids
	 * @param isvalid
	 * @param userId
	 * @param stamp
	 * @return
	 * @throws SystemException
	 */
	public int updateIsValidByIds(List<Long> ids,Integer isvalid,Long userId,String stamp) throws SystemException;
}

