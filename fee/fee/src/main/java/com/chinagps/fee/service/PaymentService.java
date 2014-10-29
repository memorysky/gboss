package com.chinagps.fee.service;

import java.util.List;
import java.util.Map;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.entity.po.Collection;
import com.chinagps.fee.entity.po.Serviceitem;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.page.Page;


/**
 * @Package:com.chinagps.fee.service
 * @ClassName:PaymentService
 * @Description:缴费业务层接口
 * @author:zfy
 * @date:2014-6-5 下午3:12:27
 */
public interface PaymentService extends BaseService{
	/**
	 * @Title:findLPayPrint4PrintSetPage
	 * @Description:分页查询发票打印频率设定列表
	 * @param pageSelect
	 * @return
	 * @throws SystemException
	 */
	public Page<Map<String, Object>> findLPayPrint4PrintSetPage(PageSelect<Map<String, Object>> pageSelect) throws SystemException;
	
	/**
	 * @Title:updatePrintFreqByCustomerId
	 * @Description:修改用户发票打印频率
	 * @param collection
	 * @return
	 * @throws SystemException
	 */
	public int updatePrintFreqByCustomerId(Collection collection)throws SystemException;
	
	/**
	 * @Title:findPrintsPage
	 * @Description:分页查询需打印发票的列表
	 * @param pageSelect
	 * @return
	 * @throws SystemException
	 */
	public Page<Map<String, Object>> findPrintsPage(PageSelect<Map<String, Object>> pageSelect) throws SystemException;
	/**
	 * @Title:findPrintsAll
	 * @Description:查询所有打印发票的列表
	 * @param conditionMap
	 * @return
	 * @throws SystemException
	 */
	public List<Map<String, Object>> findPrintsAll(Map<String, Object> conditionMap) throws SystemException;

	/**
	 * @Title:findPrints2
	 * @Description:查询需要打印发票的记录  查询“打印本页”、“打印本页选中记录”等数据
	 * @param conditionMap
	 * @return
	 * @throws SystemException
	 */
	public List<Map<String, Object>> findPrints2(Map<String, Object> conditionMap,boolean isShowItemList)throws SystemException;

	/**
	 * @Title:findRePrints
	 * @Description:查询需要补打发票的记录  查询“打印本页”、“打印本页选中记录”等数据
	 * @param conditionMap
	 * @return
	 * @throws SystemException
	 */
	public List<Map<String, Object>> findRePrints(Map<String, Object> conditionMap,boolean isShowItemList)throws SystemException;

	/**
	 * @Title:updatePrintNums
	 * @Description:清楚打印标记
	 * @param paymentIds
	 * @return
	 * @throws SystemException
	 */
	public int updatePrintNums(List<Long> paymentIds)throws SystemException;
	
	/**
	 * @Title:updatePrintNums
	 * @Description:修改打印次数
	 * @param paymentIds
	 * @return
	 * @throws SystemException
	 */
	public int updatePrintNums(Map<String, Object> conditionMap,int printNum)throws SystemException;
	
	/**
	 * @Title:updateDeliveryByIds
	 * @Description:批量设置用户发票是否特殊投递
	 * @param collectionIds
	 * @param isDelivery
	 * @return
	 * @throws SystemException
	 */
	public int updateDeliveryByIds(List<Long> collectionIds,Integer isDelivery) throws SystemException;
	
	/**
	 * @Title:findCollectionPage
	 * @Description:分页查询托收信息
	 * @param pageSelect
	 * @return
	 * @throws SystemException
	 */
	public Page<Collection> findCollectionPage(PageSelect<Collection> pageSelect) throws SystemException;
	/**
	 * @Title:findCollectionFees
	 * @Description:查询车台托收资料、收费信息
	 * @param conditionMap 外层Map：客户托收资料信息-》里层的vehiUnitList：每个车的车台的收费信息（一个客户有多个车，一个车有多个车台）
	 * @return
	 * @throws SystemException
	 */
	public Map<String,Object> findCollectionFees(Map<String, Object> conditionMap) throws SystemException;
	/**
	 * @Title:findPaymentPage
	 * @Description:分页查询缴费记录
	 * @param pageSelect
	 * @return
	 * @throws SystemException
	 */
	public Page<Map<String, Object>> findPaymentPage(PageSelect<Map<String, Object>> pageSelect) throws SystemException;
	/**
	 * @Title:findAllPayment
	 * @Description:查询缴费记录
	 * @param map
	 * @return
	 * @throws SystemException
	 */
	public List<Map<String, Object>> findAllPayment(Map<String, Object> map) throws SystemException;

	/**
	 * @Title:findArrearageInfosPage
	 * @Description:分页查询欠费用户信息
	 * @param pageSelect
	 * @return
	 * @throws SystemException
	 */
	public Page<Map<String, Object>> findArrearageInfosPage(PageSelect<Map<String, Object>> pageSelect) throws SystemException;
	/**
	 * @Title:findAllArrearageInfos
	 * @Description:查询欠费用户信息
	 * @param pageSelect
	 * @return
	 * @throws SystemException
	 */
	public List<Map<String, Object>> findAllArrearageInfos(Map<String, Object> map) throws SystemException;
	/**
	 * @Title:findPrintHistorysPage
	 * @Description:分页查询发票打印历史记录
	 * @param pageSelect
	 * @return
	 * @throws SystemException
	 */
	public Page<Map<String, Object>> findPrintHistorysPage(PageSelect<Map<String, Object>> pageSelect) throws SystemException;
	
}

