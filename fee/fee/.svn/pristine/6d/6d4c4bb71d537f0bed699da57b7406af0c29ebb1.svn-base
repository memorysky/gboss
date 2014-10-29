package com.chinagps.fee.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.entity.po.Servicepack;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.page.Page;


/**
 * @Package:com.chinagps.fee.service
 * @ClassName:ServicePackService
 * @Description:服务套餐管理业务层接口
 * @author:zfy
 * @date:2013-8-9 下午2:22:38
 */
public interface ServicePackService extends BaseService{
	/**
	 * @Title:addServicePackItemPrice
	 * @Description:添加套餐、套餐服务项、套餐价格
	 * @param servicepack 套餐描述
	 * @return 1:成功，2：套餐编号已存在，3：套餐名称已存在
	 * @throws SystemException
	 * @throws
	 */
	public int addServicePackItemPrice(Servicepack servicepack) throws SystemException;
	/**
	 * @Title:updateServicePackItemPrice
	 * @Description:修改套餐、套餐服务项、套餐价格
	 * @param servicepack 套餐描述
	 * @return 1:成功，2：套餐编号已存在，3：套餐名称已存在
	 * @throws SystemException
	 * @throws
	 */
	public int updateServicePackItemPrice(Servicepack servicepack) throws SystemException;
	
	/**
	 * @Title:deleteServicePackItemPrice
	 * @Description:根据套餐ID删除套餐、套餐服务项、套餐价格
	 * @param packId
	 * @return
	 * @throws SystemException
	 * @throws
	 */
	public int deleteServicePackItemPrice(Long packId) throws SystemException;
	
	/**
	 * @Title:findServicePackItemPrice
	 * @Description:分页查询套餐（包括不同用户类型的价格）
	 * @param pageSelect
	 * @return
	 * @throws SystemException
	 */
	public Page<Map<Long,Object>> findServicePackItemPrice(PageSelect<Servicepack> pageSelect) throws SystemException;
	
	/**
	 * @Title:findServicePackItemPrice
	 * @Description:查询套餐
	 * @param conditionMap
	 * @return
	 * @throws SystemException
	 */
	public List<Map<Long, Object>> findServicePackItemPrice(Map conditionMap) throws SystemException;
	
	/**
	 * @Title:updateIsValidByIds
	 * @Description:批量设置套餐对新客户是否生效
	 * @param ids
	 * @param isvalid
	 * @param userId
	 * @return
	 * @throws SystemException
	 * @throws
	 */
	public int updateIsValidByIds(List<Long> ids,Integer isvalid,Long userId) throws SystemException;
	
	/**
	 * @Title:updateIsVerifiedByIds
	 * @Description:批量修改审核状态
	 * @param ids
	 * @param verifyUserId
	 * @return
	 * @throws SystemException
	 * @throws
	 */
	public int updateIsVerifiedByIds(List<Long> ids,Long verifyUserId) throws SystemException;
	
	/**
	 * @Title:getServicePackItemsPrices
	 * @Description:根据ID查询套餐，以及套餐与服务项的关系、套餐与不同用户类型的价格关系
	 * @param id
	 * @return
	 * @throws SystemException
	 * @throws
	 */
	public HashMap<String, Object> getServicePackItemsPrices(Long id) throws SystemException;
}

