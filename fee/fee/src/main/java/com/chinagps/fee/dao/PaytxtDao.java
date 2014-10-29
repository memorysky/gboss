package com.chinagps.fee.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.entity.po.Paytxt;


/**
 * @Package:com.chinagps.fee.dao
 * @ClassName:PaytxtDao
 * @Description:托收文件记录数据持久层接口
 * @author:zfy
 * @date:2014-5-21 下午4:25:49
 */
public interface PaytxtDao extends BaseDao {
	/**
	 * @Title:findPaytxts
	 * @Description:查询托收文件记录
	 * @param conditionMap
	 * @return
	 * @throws SystemException
	 */
	//public List<Paytxt> findPaytxts(Map<String, Object> conditionMap,int pageNo, int pageSize)throws SystemException;
	public List<Paytxt> findPaytxts(Session session,Map<String, Object> conditionMap,int pageNo, int pageSize)throws SystemException;
	
	/**
	 * @Title:findZjPaytxts
	 * @Description:查询托收文件记录总金额、总户数，用于生成总结文件
	 * @param conditionMap
	 * @return
	 * @throws SystemException
	 */
	public Map<String,Object> getZjPaytxts(Map<String, Object> conditionMap)throws SystemException;
	
	/**
	 * @Title:getMaxFileTimesOfToday
	 * @Description:获得某个托收机构当天文件最大批次号
	 * @param conditionMap
	 * @return
	 * @throws SystemException
	 */
	public String getMaxFileTimesOfToday(Map<String, Object> conditionMap)throws SystemException;
	
	/**
	 * @Title:updateServiceTime
	 * @Description:修改服务截止日期
	 * @param unitId
	 * @return
	 * @throws SystemException
	 */
	public int updateServiceTime(Long unitId,int feeTypeId,String endDate) throws SystemException;
	
	/**
	 * @Title:findPaytxts
	 * @Description:查询托收记录
	 * @param conditionMap
	 * @param order
	 * @param isDesc
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws SystemException
	 */
	public List<Map<String, Object>> findPaytxts(Map<String, Object> conditionMap, String order,boolean isDesc,int pageNo, int pageSize)throws SystemException;
	/**
	 * @Title:countPaytxts
	 * @Description:获得托收记录数
	 * @param conditionMap
	 * @return
	 * @throws SystemException
	 */
	public int countPaytxts(Map<String, Object> conditionMap) throws SystemException;
	
	/**
	 * @Title:getMaxTxtName
	 * @Description:获得最新的下载文件的文件名
	 * @param conditionMap
	 * @return
	 * @throws SystemException
	 */
	public String getMaxTxtName(Map<String, Object> conditionMap)throws SystemException;
	/**
	 * @Title:updateServiceTime
	 * @Description:根据托收文件修改服务截止日期
	 * @param paytxtId
	 * @return
	 * @throws SystemException
	 */
	public int updateServiceTime(Session session,Long paytxtId) throws SystemException;
	
	public int addTmpTable(Session session,List<Paytxt> paytxts,long subcoNo,String txtName,int agency)throws SystemException;
	
	public int deleteTmpTable(Session session,long subcoNo,String txtName,int agency) throws SystemException; 
	
	/**
	 * @Title:findPaytxtsDt
	 * @Description:查询托收明细
	 * @param conditionMap
	 * @param order
	 * @param isDesc
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws SystemException
	 */
	public List<Map<String, Object>> findPaytxtsDt(Map<String, Object> conditionMap, String order,boolean isDesc,int pageNo, int pageSize)throws SystemException;
	public int countPaytxtsDt(Map<String, Object> conditionMap) throws SystemException;
	
}

