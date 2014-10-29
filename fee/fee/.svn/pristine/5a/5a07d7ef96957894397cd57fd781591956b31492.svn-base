package com.chinagps.fee.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.entity.vo.ItemVO;

/**
 * @Package:com.chinagps.fee.dao
 * @ClassName:PaymentDao
 * @Description:缴费记录数据持久层接口
 * @author:zfy
 * @date:2014-6-4 下午2:36:06
 */
public interface PaymentDao extends BaseDao {
	/**
	 * @Title:findLPayPrint4PrintSet
	 * @Description:查询客户最后一次打印、缴费时间、打印次数，用于发票打印频率设定的查询
	 * @param conditionMap
	 * @param order
	 * @param isDesc
	 * @param pn
	 * @param pageSize
	 * @return
	 * @throws SystemException
	 */
	public List<Map<String, Object>> findLPayPrint4PrintSet(Map<String, Object> conditionMap, String order,boolean isDesc,int pageNo, int pageSize)throws SystemException;
	/**
	 * @Title:countLPayPrint4PrintSet
	 * @Description:获得客户最后一次打印、缴费时间、打印次数，用于发票打印频率设定的查询 的记录数
	 * @param conditionMap
	 * @return
	 * @throws SystemException
	 */
	public int countLPayPrint4PrintSet(Map<String, Object> conditionMap) throws SystemException;
	
	/**
	 * @Title:findPrints
	 * @Description:查询需要打印发票的记录
	 * @param conditionMap
	 * @param order
	 * @param isDesc
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws SystemException
	 */
	public List<Map<String, Object>> findPrints(Map<String, Object> conditionMap, String order,boolean isDesc,int pageNo, int pageSize)throws SystemException;
	/**
	 * @Title:countPrints
	 * @Description:获得需要打印发票的记录数
	 * @param conditionMap
	 * @return
	 * @throws SystemException
	 */
	public int countPrints(Map<String, Object> conditionMap) throws SystemException;
	
	/**
	 * @Title:findPrints2
	 * @Description:查询需要打印发票的记录  查询“打印本页”、“打印本页选中记录”等数据
	 * @param conditionMap
	 * @return
	 * @throws SystemException
	 */
	public List<Map<String, Object>> findPrints2(Map<String, Object> conditionMap)throws SystemException;
	
	/**
	 * @Title:updatePrintNums
	 * @Description:修改打印次数
	 * @param paymentIds
	 * @return
	 * @throws SystemException
	 */
	public int updatePrintNums(Session session,Map<String, Object> conditionMap,int printNum)throws SystemException;
	
	/**
	 * @Title:updatePrintNums
	 * @Description:修改打印次数
	 * @param paymentIds
	 * @return
	 * @throws SystemException
	 */
	public int updatePrintNums(Session session,String paymentIds,int printNum)throws SystemException;
	
	/**
	 * @Title:addPrint
	 * @Description:添加打印日志
	 * @param paymentIds
	 * @return
	 * @throws SystemException
	 */
	public int addPrint(Session session,Map<String, Object> conditionMap)throws SystemException;
	
	/**
	 * @Title:findPayments
	 * @Description:查询缴费记录
	 * @param conditionMap
	 * @param order
	 * @param isDesc
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws SystemException
	 */
	public List<Map<String, Object>> findPayments(Map<String, Object> conditionMap, String order,boolean isDesc,int pageNo, int pageSize)throws SystemException;
	/**
	 * @Title:findPaymentDts4Print
	 * @Description:查询缴费明细
	 * @param conditionMap
	 * @return
	 * @throws SystemException
	 */
	public List<ItemVO> findPaymentDts4Print(Map<String, Object> conditionMap)throws SystemException;
	
	/**
	 * @Title:countPayments
	 * @Description:获得缴费记录数
	 * @param conditionMap
	 * @return
	 * @throws SystemException
	 */
	public int countPayments(Map<String, Object> conditionMap) throws SystemException;
	
	/**
	 * 
	 * @Title:findArrearageInfos
	 * @Description:获得欠费用户信息
	 * @param conditionMap
	 * @param order
	 * @param isDesc
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws SystemException
	 */
	public List<Map<String, Object>> findArrearageInfos(Map<String, Object> conditionMap, String order,boolean isDesc,int pageNo, int pageSize)throws SystemException;
	/**
	 * @Title:countArrearageInfos
	 * @Description:获得欠费用户信息记录数
	 * @param conditionMap
	 * @return
	 * @throws SystemException
	 */
	public int countArrearageInfos(Map<String, Object> conditionMap) throws SystemException;
	/**
	 * @Title:findPrintHistorys
	 * @Description:查询打印历史记录
	 * @param conditionMap
	 * @param order
	 * @param isDesc
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws SystemException
	 */
	public List<Map<String, Object>> findPrintHistorys(Map<String, Object> conditionMap, String order,boolean isDesc,int pageNo, int pageSize)throws SystemException;
	/**
	 * @Title:countPrintHistorys
	 * @Description:获得打印历史记录数
	 * @param conditionMap
	 * @return
	 * @throws SystemException
	 */
	public int countPrintHistorys(Map<String, Object> conditionMap) throws SystemException;
	/**
	 * @Title:addPaymentDt
	 * @Description:添加缴费明细
	 * @param paytxtId
	 * @param paymentId
	 * @return
	 * @throws SystemException
	 */
	public int addPaymentDt(Session session,Long paytxtId,Long paymentId,String payCtNo) throws SystemException;
	
}

