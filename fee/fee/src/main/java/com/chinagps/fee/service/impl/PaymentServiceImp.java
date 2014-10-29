package com.chinagps.fee.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.FloatType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinagps.fee.comm.SystemConst;
import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.dao.CollectionDao;
import com.chinagps.fee.dao.PaymentDao;
import com.chinagps.fee.dao.ReprintinvoiceDao;
import com.chinagps.fee.dao.SysDao;
import com.chinagps.fee.entity.po.Collection;
import com.chinagps.fee.entity.po.Customer;
import com.chinagps.fee.entity.po.Print;
import com.chinagps.fee.entity.po.sys.SysValue;
import com.chinagps.fee.entity.vo.ItemVO;
import com.chinagps.fee.service.PaymentService;
import com.chinagps.fee.util.MoneyUtil;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.StringUtils;
import com.chinagps.fee.util.Utils;
import com.chinagps.fee.util.page.Page;
import com.chinagps.fee.util.page.PageUtil;

/**
 * @Package:com.chinagps.fee.service.impl
 * @ClassName:PaymentServiceImp
 * @Description:缴费业务层实现类
 * @author:zfy
 * @date:2014-6-9 下午3:31:20
 */
@Service("paymentService")
@Transactional(value="mysql1TxManager")
public class PaymentServiceImp extends BaseServiceImpl implements
		PaymentService {
	protected static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImp.class);
	
	@Autowired  
	@Qualifier("paymentDao")
	private PaymentDao paymentDao;
	
	@Autowired  
	@Qualifier("collectionDao")
	private CollectionDao collectionDao;
	
	@Autowired  
	@Qualifier("reprintinvoiceDao")
	private ReprintinvoiceDao reprintinvoiceDao;
	
	@Autowired  
	@Qualifier("sysDao")
	private SysDao sysDao;
	
	@Override
	public Page<Map<String, Object>> findLPayPrint4PrintSetPage(
			PageSelect<Map<String, Object>> pageSelect) throws SystemException {
		List<Map<String, Object>> dataList=paymentDao.findLPayPrint4PrintSet(pageSelect.getFilter(), pageSelect.getOrder(), pageSelect.getIs_desc(), pageSelect.getPageNo(), pageSelect.getPageSize());
		int total=paymentDao.countLPayPrint4PrintSet(pageSelect.getFilter());
		return PageUtil.getPage(total, pageSelect.getPageNo(), dataList, pageSelect.getPageSize());

	}

	@Override
	public int updatePrintFreqByCustomerId(Collection collection)
			throws SystemException {
		return collectionDao.updatePrintFreqByCustomerId(collection);
	}

	@Override
	public Page<Map<String, Object>> findPrintsPage(
			PageSelect<Map<String, Object>> pageSelect) throws SystemException {
		List<Map<String, Object>> dataList=paymentDao.findPrints(pageSelect.getFilter(), pageSelect.getOrder(), pageSelect.getIs_desc(), pageSelect.getPageNo(), pageSelect.getPageSize());
		int total=paymentDao.countPrints(pageSelect.getFilter());
		return PageUtil.getPage(total, pageSelect.getPageNo(), dataList, pageSelect.getPageSize());

	}


	@Override
	public int updatePrintNums(List<Long> paymentIds) throws SystemException {
		if(Utils.isNotNullOrEmpty(paymentIds)){
			//由于SQL语句长度默认不能大于1M,因此建议paymentIdsi 没1000条进行一次查询
			//List<Map<String, Object>> dataList=new ArrayList<Map<String, Object>>();
				int size=paymentIds.size();
				int maxSize=SystemConst.MAX_SIZE;
				if(size>maxSize){
					int freq=size/maxSize;
					int remainder=size%maxSize;//余数
					int i=0;
					for (i = 0; i < freq; i++) {
						paymentDao.updatePrintNums(null,Utils.longlistToString(paymentIds.subList(i*maxSize,((i*maxSize)+(maxSize)))), 0);
					}
					if(remainder>0){
						paymentDao.updatePrintNums(null,Utils.longlistToString(paymentIds.subList(i*maxSize,maxSize*i+remainder)), 0);
					}
				}else{
					paymentDao.updatePrintNums(null,Utils.longlistToString(paymentIds),0);
				}
			return 1;
		}else{
			return 0;
		}
	}

	@Override
	public List<Map<String, Object>> findRePrints(
			Map<String, Object> conditionMap,boolean isShowItemList) throws SystemException {
		//查询需要打印的记录
		List<Map<String, Object>> dataList=this.findPrints2(conditionMap,isShowItemList);
		//修改补打发票记录表的是否打印字段
		conditionMap.remove("unitId");
		conditionMap.remove("customerId");
		conditionMap.remove("unitNum");
		conditionMap.remove("vehicleNum");
		conditionMap.remove("customerName");
		conditionMap.remove("payCtNo");
		reprintinvoiceDao.updateRePrints(conditionMap, null, 1);
		return dataList;
	}

	@Override
	public int updateDeliveryByIds(List<Long> collectionIds, Integer isDelivery)
			throws SystemException {
		return collectionDao.updateDeliveryByIds(collectionIds, isDelivery);
	}

	@Override
	public Page<Collection> findCollectionPage(PageSelect<Collection> pageSelect)
			throws SystemException {
		List<Collection> dataList=collectionDao.findCollections(pageSelect.getFilter(), pageSelect.getOrder(), pageSelect.getIs_desc(), pageSelect.getPageNo(), pageSelect.getPageSize());
		int total=collectionDao.countCollections(pageSelect.getFilter());
		return PageUtil.getPage(total, pageSelect.getPageNo(), dataList, pageSelect.getPageSize());

	}

	@Override
	public Map<String, Object> findCollectionFees(
			Map<String, Object> conditionMap) throws SystemException {
		//客户ID
		String customerId=Utils.clearNull(conditionMap.get("customerId"));
		Map<String,Object> resultMap=new HashMap<String,Object>();
		//计费项目类型
		SysValue sysValue=new SysValue();
		sysValue.setIsDel(0);
		sysValue.setStype(SystemConst.FEE_TYPE_ID);
		List<SysValue> list=sysDao.findSysValue(sysValue);
		Map<String, String> feeTypeMap=null;
		if(Utils.isNotNullOrEmpty(list)){
			feeTypeMap=new HashMap<String, String>();
			for (SysValue sysValue2 : list) {
				feeTypeMap.put(sysValue2.getSvalue(), sysValue2.getSname());
			}
		}
		List<Map<String,Object>> dataList=collectionDao.findCollectionFees(conditionMap);
		if(Utils.isNotNullOrEmpty(dataList)){
			String feeIds=null;
			String feeTypeIdStr=null;
			StringBuffer feeInfos=null;
			int index=0;
			List<Map<String,Object>> feeList=null;
			for (Map<String, Object> map : dataList) {
				customerId=Utils.clearNull(map.get("customerId"));
				//查询车台每一个类型的实收金额,每月费用
				feeIds=Utils.clearNull(map.get("feeIds"));
				if(StringUtils.isNotBlank(feeIds)){
					feeList=collectionDao.findFeeInfos(feeIds,false,null);
					if(Utils.isNotNullOrEmpty(feeList)){
						feeInfos=new StringBuffer();
						index=0;
						for (Map<String, Object> map2 : feeList) {
							feeTypeIdStr=Utils.clearNull(map2.get("feetypeId"));
							if(StringUtils.isNotBlank(feeTypeIdStr)){
								index++;
								feeInfos.append(index).append("、");
								feeInfos.append(feeTypeMap.get(feeTypeIdStr)).append(" 【");
								feeInfos.append("服务截止日期：").append(map2.get("feeSedate")).append("，");
								feeInfos.append("开始收费日期：").append(map2.get("feeDate")).append("，");
								feeInfos.append("收费频率：").append(map2.get("feeCycle")).append("个月，");
								feeInfos.append("月服务费：").append(map2.get("monthFee")).append("元】<br/>");
							}
						}
						if(feeInfos.indexOf("<br/>")>0){
							map.put("feeItems",feeInfos.delete(feeInfos.length()-5, feeInfos.length()));
						}
					}
				}
			}
			//查询客户的托收信息
			if(StringUtils.isNotBlank(customerId)){
				conditionMap.clear();
				conditionMap.put("customerId", customerId);
				//发票投递方式
				sysValue.setStype(SystemConst.DELIVERY_ID);
				list=sysDao.findSysValue(sysValue);
				feeTypeMap.clear();
				if(Utils.isNotNullOrEmpty(list)){
					for (SysValue sysValue2 : list) {
						feeTypeMap.put(sysValue2.getSvalue(), sysValue2.getSname());
					}
				}
				List<Collection> collections=collectionDao.findCollections(conditionMap, null,false ,0,0);
				if(Utils.isNotNullOrEmpty(collections)){
					Collection collection=collections.get(0);
					resultMap=Utils.transBean2Map(collection);
					if(collection.getIsDelivery()!=null){
						resultMap.put("isDeliveryVal", feeTypeMap.get(String.valueOf(collection.getIsDelivery())));
					}
				}
				//根据客户id，查询出客户名称
				Customer customer=collectionDao.get(Customer.class, Long.valueOf(customerId));
				if(customer!=null){
					resultMap.put("customerName",customer.getCustomer_name());
				}
			}
			resultMap.put("vehiUnitList", dataList);
			return resultMap;
		}
		return new HashMap<String, Object>();
	}

	@Override
	public Page<Map<String, Object>> findPaymentPage(
			PageSelect<Map<String, Object>> pageSelect) throws SystemException {
		List<Map<String, Object>> dataList=paymentDao.findPayments(pageSelect.getFilter(), pageSelect.getOrder(), pageSelect.getIs_desc(), pageSelect.getPageNo(), pageSelect.getPageSize());
		int total=paymentDao.countPayments(pageSelect.getFilter());
		return PageUtil.getPage(total, pageSelect.getPageNo(), dataList, pageSelect.getPageSize());
	}

	@Override
	public Page<Map<String, Object>> findArrearageInfosPage(
			PageSelect<Map<String, Object>> pageSelect) throws SystemException {
		String feeSedate=null;//服务截止日期
		if(pageSelect!=null){
			if(pageSelect.getFilter()!=null){
				feeSedate=Utils.clearNull(pageSelect.getFilter().get("serviceEdate"));
			}
		}
		List<Map<String, Object>> dataList=paymentDao.findArrearageInfos(pageSelect.getFilter(), pageSelect.getOrder(), pageSelect.getIs_desc(), pageSelect.getPageNo(), pageSelect.getPageSize());
		if(Utils.isNotNullOrEmpty(dataList)){
			//计费项目类型
			SysValue sysValue=new SysValue();
			sysValue.setIsDel(0);
			sysValue.setStype(SystemConst.FEE_TYPE_ID);
			List<SysValue> list=sysDao.findSysValue(sysValue);
			Map<String, String> feeTypeMap=null;
			if(Utils.isNotNullOrEmpty(list)){
				feeTypeMap=new HashMap<String, String>();
				for (SysValue sysValue2 : list) {
					feeTypeMap.put(sysValue2.getSvalue(), sysValue2.getSname());
				}
			}
			String feeIds=null;
			String feeTypeIdStr=null;
			StringBuffer feeInfos=null;
			Double arrearageAmount=0d;//欠费总金额
			int index=0;
			List<Map<String,Object>> feeList=null;
			for (Map<String, Object> map : dataList) {
				//查询车台每一个类型的实收金额,每月费用
				feeIds=Utils.clearNull(map.get("feeIds"));
				if(StringUtils.isNotBlank(feeIds)){
					feeList=collectionDao.findFeeInfos(feeIds,true,feeSedate);
					if(Utils.isNotNullOrEmpty(feeList)){
						feeInfos=new StringBuffer("合计: xx元 【");
						index=0;
						arrearageAmount=0d;
						//欠费金额
						for (Map<String, Object> map2 : feeList) {
							feeTypeIdStr=Utils.clearNull(map2.get("feetypeId"));
							if(StringUtils.isNotBlank(feeTypeIdStr)){
								index++;
								feeInfos.append(index).append("、");
								feeInfos.append(feeTypeMap.get(feeTypeIdStr)).append(":");
								feeInfos.append(map2.get("arrearageAmount")).append("元(");
								feeInfos.append(map2.get("feeSedate")).append(")  ");
								arrearageAmount+=Double.valueOf(Utils.objToDouble(map2.get("arrearageAmount")));
							}
						}
						feeInfos.append("】");
						map.put("feeItems",feeInfos.toString().replace("xx", String.valueOf(arrearageAmount)));
					}
				}
			}
		}
		int total=paymentDao.countArrearageInfos(pageSelect.getFilter());
		return PageUtil.getPage(total, pageSelect.getPageNo(), dataList, pageSelect.getPageSize());

	}

	@Override
	public List<Map<String, Object>> findAllPayment(Map<String, Object> map)
			throws SystemException {
		return paymentDao.findPayments(map, null, false, 0, 0);
	}

	@Override
	public List<Map<String, Object>> findAllArrearageInfos(
			Map<String, Object> conditionMap) throws SystemException {
		String feeSedate=null;//服务截止日期
		if(conditionMap!=null){
			feeSedate=Utils.clearNull(conditionMap.get("serviceEdate"));
		}
		List<Map<String, Object>> dataList=paymentDao.findArrearageInfos(conditionMap, null, false, 0, 0);
		if(Utils.isNotNullOrEmpty(dataList)){
			//计费项目类型
			SysValue sysValue=new SysValue();
			sysValue.setIsDel(0);
			sysValue.setStype(SystemConst.FEE_TYPE_ID);
			List<SysValue> list=sysDao.findSysValue(sysValue);
			Map<String, String> feeTypeMap=null;
			if(Utils.isNotNullOrEmpty(list)){
				feeTypeMap=new HashMap<String, String>();
				for (SysValue sysValue2 : list) {
					feeTypeMap.put(sysValue2.getSvalue(), sysValue2.getSname());
				}
			}
			String feeIds=null;
			String feeTypeIdStr=null;
			StringBuffer feeInfos=null;
			Double arrearageAmount=0d;//欠费总金额
			int index=0;
			List<Map<String,Object>> feeList=null;
			for (Map<String, Object> map : dataList) {
				//查询车台每一个类型的实收金额,每月费用
				feeIds=Utils.clearNull(map.get("feeIds"));
				if(StringUtils.isNotBlank(feeIds)){
					feeList=collectionDao.findFeeInfos(feeIds,true,feeSedate);
					if(Utils.isNotNullOrEmpty(feeList)){
						feeInfos=new StringBuffer("合计: xx元 【");
						index=0;
						arrearageAmount=0d;
						//欠费金额
						for (Map<String, Object> map2 : feeList) {
							feeTypeIdStr=Utils.clearNull(map2.get("feetypeId"));
							if(StringUtils.isNotBlank(feeTypeIdStr)){
								index++;
								feeInfos.append(index).append("、");
								feeInfos.append(feeTypeMap.get(feeTypeIdStr)).append(":");
								feeInfos.append(map2.get("arrearageAmount")).append("元(");
								feeInfos.append(map2.get("feeSedate")).append(")  ");
								arrearageAmount+=Double.valueOf(Utils.objToDouble(map2.get("arrearageAmount")));
							}
						}
						feeInfos.append("】");
						map.put("feeItems",feeInfos.toString().replace("xx", String.valueOf(arrearageAmount)));
					}
				}
			}
		}
		return dataList;
	}

	@Override
	public Page<Map<String, Object>> findPrintHistorysPage(
			PageSelect<Map<String, Object>> pageSelect) throws SystemException {
		List<Map<String, Object>> dataList=paymentDao.findPrintHistorys(pageSelect.getFilter(), pageSelect.getOrder(), pageSelect.getIs_desc(), pageSelect.getPageNo(), pageSelect.getPageSize());
		int total=paymentDao.countPrintHistorys(pageSelect.getFilter());
		return PageUtil.getPage(total, pageSelect.getPageNo(), dataList, pageSelect.getPageSize());

	}
	@Override
	public List<Map<String, Object>> findPrints2(
			Map<String, Object> conditionMap,boolean isShowItemList) throws SystemException {
		List<Map<String, Object>> dataList= paymentDao.findPrints2(conditionMap);
		String pagePaymentIds=conditionMap.get("paymentIds")==null?null:String.valueOf(conditionMap.get("paymentIds"));
		
		Session session=null;
		Transaction tx=null;
			try {
				session=paymentDao.openSession();
				tx=session.beginTransaction();
				session.setCacheMode(CacheMode.IGNORE);//忽略第二级缓存
				session.setFlushMode(FlushMode.AUTO);//如果FlushMode是MANUAL或NEVEL,在操作过程中hibernate会将事务设置为readonly
				
				if(dataList!=null){
					//解析项目明细，并修改打印次数，数字金额转人民币，拼接计费时段
					String paymentIds=null;
					 List<ItemVO> itemList=null;
					 //ItemVO itemVO=new ItemVO();
					 String realAmount=null;//金额
					 String sdate=null;//开始时间
					 String edate=null;//结束时间
					// int index=0;//行数
					for (Map<String, Object> map : dataList) {
						//index++;
						//数字金额转人民币
						realAmount=map.get("realAmount")==null?null:String.valueOf(map.get("realAmount"));
						if(StringUtils.isNotBlank(realAmount)){
							map.put("realAmountRMB",MoneyUtil.CNValueOf(realAmount));
						}
						
						/*if(StringUtils.isNotBlank(paymentIds)){
							//修改打印次数
							paymentDao.updatePrintNums(session,conditionMap,1);
							//添加打印日志
							paymentDao.addPrint(session,conditionMap);
						}*/
						 if(isShowItemList){//如果不是导出Excel
							//拼接计费时段
							sdate=map.get("sdate")==null?null:String.valueOf(map.get("sdate"));
							edate=map.get("edate")==null?null:String.valueOf(map.get("edate"));
							if(StringUtils.isNotBlank(sdate) && StringUtils.isNotBlank(edate)){
								map.put("paySEdate",sdate+" 至 "+edate);
							}
							
							//修改打印次数、添加打印日志
							paymentIds=map.get("paymentIds")==null?null:String.valueOf(map.get("paymentIds"));
							conditionMap.remove("unitId");
							conditionMap.remove("customerId");
							if("0".equals(map.get("custType").toString())){//私家车
								conditionMap.put("unitId", map.get("unitId"));
							}else{//集客
								conditionMap.put("customerId", map.get("customerId"));
							}
							conditionMap.put("paymentIds", paymentIds);
							itemList=paymentDao.findPaymentDts4Print(conditionMap);
							if(Utils.isNotNullOrEmpty(itemList)){
								map.put("itemList",itemList);
							}
						}
					}
				}
				conditionMap.remove("unitId");
				conditionMap.remove("customerId");
				conditionMap.put("paymentIds", pagePaymentIds);
				conditionMap.put("isUpdatePrintNums", true);
				//修改打印次数
				paymentDao.updatePrintNums(session,conditionMap,1);
				//添加打印日志
				conditionMap.remove("isUpdatePrintNums");
				paymentDao.addPrint(session,conditionMap);
				
				 session.flush();
				 session.clear();
	             tx.commit();
	             session.close();
			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
				LOGGER.error(e.getMessage(), e);
			}finally {
				  if (session != null) {
					    if (session.isOpen()) {
					     //关闭session
					     session.close();
					    }
					}
			}
		
		return dataList;
	}

	@Override
	public int updatePrintNums(Map<String, Object> conditionMap, int printNum)
			throws SystemException {
		return paymentDao.updatePrintNums(null,conditionMap,0);
	}

	@Override
	public List<Map<String, Object>> findPrintsAll(
			Map<String, Object> conditionMap) throws SystemException {
		return paymentDao.findPrints(conditionMap,null,false,0,0);
	}
}

