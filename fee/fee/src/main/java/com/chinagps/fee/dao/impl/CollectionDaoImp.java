package com.chinagps.fee.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.dao.CollectionDao;
import com.chinagps.fee.dao.PaymentDao;
import com.chinagps.fee.entity.po.Collection;
import com.chinagps.fee.entity.po.Info;
import com.chinagps.fee.util.StringUtils;
import com.chinagps.fee.util.Utils;
import com.chinagps.fee.util.page.PageUtil;

/**
 * @Package:com.chinagps.fee.dao.impl
 * @ClassName:CollectionDaoImp
 * @Description:托收资料持久层实现类
 * @author:zfy
 * @date:2014-6-4 下午2:37:17
 */
@Repository("collectionDao")  
public class CollectionDaoImp extends BaseDaoImpl implements CollectionDao {
	protected static final Logger logger = LoggerFactory.getLogger(CollectionDaoImp.class);
	
	@Override
	public int updatePrintFreqByCustomerId(Collection collection)
			throws SystemException {
		if(collection!=null && Utils.isNotNullOrEmpty(collection.getCustomerId())&& Utils.isNotNullOrEmpty(collection.getPrintFreq())){
			StringBuffer sb=new StringBuffer();
			sb.append(" update Collection");
			sb.append(" set printFreq=").append(collection.getPrintFreq());
			sb.append(" where customerId=").append(collection.getCustomerId());
			Query query = mysql1SessionFactory.getCurrentSession().createQuery(sb.toString());  
			return query.executeUpdate();
		}else{
			return 0;
		}
	}

	@Override
	public int updateDeliveryByIds(List<Long> collectionIds, Integer isDelivery)
			throws SystemException {
		if(Utils.isNotNullOrEmpty(collectionIds)){
			StringBuffer sb=new StringBuffer();
			sb.append(" update Collection");
			sb.append(" set isDelivery=").append(isDelivery);
			sb.append(" where collectionId in (:collectionIds)");
			Query query = mysql1SessionFactory.getCurrentSession().createQuery(sb.toString());  
			query.setParameterList("collectionIds", collectionIds);
			return query.executeUpdate();
		}else{
			return 0;
		}
	}

	@Override
	public List<Collection> findCollections(
			Map<String, Object> conditionMap, String order, boolean isDesc,
			int pageNo, int pageSize) throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" from Collection as c");
		sb=getConditionHql(sb,conditionMap);
		if (StringUtils.isNotBlank(order)) {
			sb.append(" order by ").append(order);
			if (isDesc) {
				sb.append(" desc");
			} else {
				sb.append(" asc");
			}
		}else{
			sb.append(" order by c.customerId");
		}
		Query query = mysql1SessionFactory.getCurrentSession().createQuery(sb.toString());  
		query.setFirstResult(PageUtil.getPageStart(pageNo, pageSize));
		query.setMaxResults(pageSize);
		return query.list();
	}

	@Override
	public int countCollections(Map<String, Object> conditionMap)
			throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" select count(*) from Collection as c");
		sb=getConditionHql(sb,conditionMap);
		Query query = mysql1SessionFactory.getCurrentSession().createQuery(sb.toString());  
		return ((Long)query.uniqueResult()).intValue();
	}
	
	 private StringBuffer getConditionHql(StringBuffer packHqlSb,Map conditionMap){
			packHqlSb.append(" where 1=1");
			if(conditionMap!=null){
				if(Utils.isNotNullOrEmpty(conditionMap.get("customerId"))){
					packHqlSb.append(" and c.customerId=").append(conditionMap.get("customerId"));
				}
				if(Utils.isNotNullOrEmpty(conditionMap.get("subcoNo"))){
					packHqlSb.append(" and c.subcoNo=").append(conditionMap.get("subcoNo"));
				}
				if(Utils.isNotNullOrEmpty(conditionMap.get("payCtNo"))){
					packHqlSb.append(" and c.payCtNo like '%").append(StringUtils.replaceSqlKey(conditionMap.get("payCtNo"))).append("%'");
				}
				if(Utils.isNotNullOrEmpty(conditionMap.get("acNo"))){
					packHqlSb.append(" and c.acNo like '%").append(StringUtils.replaceSqlKey(conditionMap.get("acNo"))).append("%'");
				}
				if(conditionMap.get("isDelivery")!=null){
					packHqlSb.append(" and c.isDelivery=").append(conditionMap.get("isDelivery"));
				}
			}
			return packHqlSb;
	    }

	@Override
	public List<Map<String, Object>> findCollectionFees(
			Map<String, Object> conditionMap) throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" select bv.vehicle_id vehicleId,bv.plate_no plateNo, bu.customer_id customerId,bu.unit_id unitId,bu.call_letter callLetter,date_format(bu.fix_time,'%Y-%m-%d') fixTime,");
		sb.append(" bu.reg_status regStatus,ins.ic_no icNo,ins.sname,ins.is_buy_tp isBuyTp,");
		sb.append(" f.feeIds,f.collection_id collectionId");
		sb.append(" from t_ba_unit bu left join t_ba_vehicle bv on bu.vehicle_id=bv.vehicle_id left join ");
		sb.append(" (select fi.unit_id,fi.collection_id, group_concat(cast(fi.fee_id as char)) as feeIds");
		sb.append(" from t_fee_info fi where fi.pay_model=0 group by fi.unit_id");
		sb.append(" ) f on bu.unit_id=f.unit_id");
		sb.append(" left join (select bi.ic_no, bi.is_buy_tp,v.sname,bi.unit_id from ");
		sb.append(" ( select * from t_ba_insurance bi1 where bi1.status=2 and bi1.is_bdate<sysdate() and bi1.is_edate>sysdate() group by bi1.unit_id) bi");             
		sb.append(" left join t_sys_value v on bi.ic_no=v.svalue and v.stype=3030) as ins on bu.unit_id=ins.unit_id");
		sb.append(" where 1=1");
		if(conditionMap!=null){
			if(Utils.isNotNullOrEmpty(conditionMap.get("unitId"))){
				sb.append(" and bu.unit_id=").append(conditionMap.get("unitId"));
			}
			if(Utils.isNotNullOrEmpty(conditionMap.get("vehicleId"))){
				sb.append(" and bu.vehicle_id=").append(conditionMap.get("vehicleId"));
			}
			if(Utils.isNotNullOrEmpty(conditionMap.get("customerId"))){
				sb.append(" and bu.customer_id=").append(conditionMap.get("customerId"));
			}
		}
		SQLQuery query=mysql1SessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public List<Map<String,Object>> findFeeInfos(
			String feeIds,boolean isArrearage,String feeSedate) throws SystemException {
		if(Utils.isNotNullOrEmpty(feeIds)){
				StringBuffer sb=new StringBuffer();
				sb.append(" select fi.feetype_id feetypeId,sum(fi.month_fee) monthFee,sum(fi.real_amount) realAmount");
				sb.append(",max(fi.fee_cycle) feeCycle,ifnull(date_format(max(fi.fee_date),'%Y-%m-%d'),'') feeDate,ifnull(date_format(max(fi.fee_sedate),'%Y-%m-%d'),'') feeSedate");
				if(isArrearage){//需要查欠费金额
					if(StringUtils.isBlank(feeSedate)){
						feeSedate="sysdate()";
					}else{
						feeSedate="'"+feeSedate+"'";
					}
					sb.append(",max(ceil(if(f_months_between(fi.fee_sedate,").append(feeSedate).append(")<0,0,f_months_between(fi.fee_sedate,").append(feeSedate).append("))/ifnull(fi.fee_cycle,1))*ifnull(fi.real_amount,0)) arrearageAmount");
				}
				sb.append(" from t_fee_info fi");
				sb.append(" where fi.fee_id in(").append(feeIds).append(")");
				sb.append(" group by fi.feetype_id");
				Query query = mysql1SessionFactory.getCurrentSession().createSQLQuery(sb.toString());  
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
		}else{
			return null;
		}
	}
}

