package com.chinagps.fee.dao.impl;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.dao.ReprintinvoiceDao;
import com.chinagps.fee.util.StringUtils;
import com.chinagps.fee.util.Utils;
import com.chinagps.fee.util.page.PageUtil;

/**
 * @Package:com.chinagps.fee.dao.impl
 * @ClassName:ReprintinvoiceDaoImpl
 * @Description:发票补打数据持久层实现类
 * @author:zfy
 * @date:2014-6-9 下午3:34:18
 */
@Repository("reprintinvoiceDao") 
public class ReprintinvoiceDaoImpl extends BaseDaoImpl implements ReprintinvoiceDao {

	@Override
	public List<Map<String, Object>> findRePrints(
			Map<String, Object> conditionMap, String order, boolean isDesc,
			int pageNo, int pageSize) throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append("select r.reprint_id reprintId, r.subco_no subcoNo, r.customer_id customerId, c.customer_name customerName, r.vehicle_id vehicleId,");
		sb.append(" r.unit_id unitId,v.plate_no plateNo, r.call_letter callLetter, r.stamp stamp,");
		sb.append(" date_format(r.print_time,'%Y-%m') printTime,r.is_del isDel,r.del_time delTime,r.is_printed isPrinted");
		sb.append(" from t_fee_reprintinvoice r");
		sb.append(" inner join (select collection_id,unit_id from t_fee_info fi group by fi.unit_id) i on r.unit_id=i.unit_id");
		sb.append(" inner join t_ba_unit u on r.unit_id=u.unit_id");
		sb.append(" inner join t_ba_vehicle v on r.vehicle_id=v.vehicle_id");
		sb.append(" inner join t_ba_customer c on r.customer_id=c.customer_id");
		sb.append(" inner join t_ba_collection bc on i.collection_id=bc.collection_id ");
		sb = getCon4Prints(sb,conditionMap);
		if (StringUtils.isNotBlank(order)) {
			sb.append(" order by ").append(order);
			if (isDesc) {
				sb.append(" desc");
			} else {
				sb.append(" asc");
			}
		}else {
			sb.append(" order by r.stamp desc");
		}
		if (pageNo>0 && pageSize>0) {
			sb.append(" limit ");
			sb.append(PageUtil.getPageStart(pageNo, pageSize));
			sb.append(",");
			sb.append(pageSize);
		}
		SQLQuery query=mysql1SessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public int countRePrints(Map<String, Object> conditionMap)
			throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" select count(r.reprint_id)");
		sb.append(" from t_fee_reprintinvoice r");
		sb.append(" inner join (select collection_id,unit_id from t_fee_info fi group by fi.unit_id) i on r.unit_id=i.unit_id");
		sb.append(" inner join t_ba_unit u on r.unit_id=u.unit_id");
		sb.append(" inner join t_ba_vehicle v on r.vehicle_id=v.vehicle_id");
		sb.append(" inner join t_ba_customer c on r.customer_id=c.customer_id");
		sb.append(" inner join t_ba_collection bc on i.collection_id=bc.collection_id ");
		sb = getCon4Prints(sb, conditionMap);
		SQLQuery query=mysql1SessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		return ((BigInteger)query.uniqueResult()).intValue();
	}
	
	private StringBuffer getCon4Prints(StringBuffer sb,Map conditionMap){
		sb.append(" where 1=1 ");
		if(conditionMap!=null){
			if (Utils.isNotNullOrEmpty(conditionMap.get("subcoNo"))) {//分公司ID
				sb.append(" and r.subco_no=").append(conditionMap.get("subcoNo"));
			}
			int invoiceType=conditionMap.get("invoiceType")==null?1:Integer.parseInt(conditionMap.get("invoiceType").toString());
			if(invoiceType ==1){//已补打
	            sb.append("  and r.is_printed=1");
	        }else if(invoiceType ==2){//已删除
	            sb.append("  and r.is_del = 1 ");
	        }else{//最近请求
	            sb.append(" and r.is_printed = 0 ");
	            sb.append("  and r.is_del = 0 ");
	        }
			if(invoiceType!=0){
				if(Utils.isNotNullOrEmpty(conditionMap.get("startDate")) && Utils.isNotNullOrEmpty(conditionMap.get("endDate"))){//付费开始、结束时间
					sb.append(" and r.stamp between '").append(conditionMap.get("startDate"));
					sb.append(" 00:00:00' and '").append(conditionMap.get("endDate")).append(" 23:59:59'");
				}
			}
			/*if (Utils.isNotNullOrEmpty(conditionMap.get("reprintIds"))) {//补打发票登记id记录集合
				sb.append(" and r.reprint_id in(:reprintIds)");
			}*/
			if (Utils.isNotNullOrEmpty(conditionMap.get("reprintIds"))) {//补打发票登记id记录集合
				sb.append(" and r.reprint_id in(").append(conditionMap.get("reprintIds")).append(")");
			}
			if (Utils.isNotNullOrEmpty(conditionMap.get("customerId"))) {//客户ID
				sb.append(" and r.customer_id=").append(conditionMap.get("customerId"));
			}
			if (Utils.isNotNullOrEmpty(conditionMap.get("unitId"))) {//车台id
				sb.append(" and r.unit_id=").append(conditionMap.get("unitId"));
			}
			if (Utils.isNotNullOrEmpty(conditionMap.get("vehicleId"))) {//车辆id
				sb.append(" and r.vehicle_id=").append(conditionMap.get("vehicleId"));
			}
			if (Utils.isNotNullOrEmpty(conditionMap.get("collectionId"))) {//托收资料ID
				sb.append(" and i.collection_id=").append(conditionMap.get("collectionId"));
			}
			if (Utils.isNotNullOrEmpty(conditionMap.get("unitNum"))) {//车台呼号
				sb.append(" and u.call_letter like '%").append(StringUtils.replaceSqlKey(conditionMap.get("unitNum"))).append("%'");
			}
			if (Utils.isNotNullOrEmpty(conditionMap.get("vehicleNum"))) {//车辆
				sb.append(" and v.plate_no like '%").append(StringUtils.replaceSqlKey(conditionMap.get("vehicleNum"))).append("%'");
			}
			if (Utils.isNotNullOrEmpty(conditionMap.get("customerName"))) {//客户
				sb.append(" and c.customer_name like '%").append(StringUtils.replaceSqlKey(conditionMap.get("customerName"))).append("%'");
			}
			if (Utils.isNotNullOrEmpty(conditionMap.get("payCtNo"))) {//合同号
				sb.append(" and bc.pay_ct_no like '%").append(StringUtils.replaceSqlKey(conditionMap.get("payCtNo"))).append("%'");
			}
		}
		return sb;
}

	@Override
	public int updateRePrints(Map<String, Object> conditionMap, Integer isDel, Integer isPrinted)
			throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" update t_fee_reprintinvoice r");
		sb.append(" inner join (select collection_id,unit_id from t_fee_info fi group by fi.unit_id) i");
		if(isDel!=null){
			sb.append(" set is_del=").append(isDel).append(",del_time=sysdate()");
		}else{
			sb.append(" set is_printed=").append(isPrinted);
		}
		if(conditionMap!=null){
			sb = getCon4Prints(sb,conditionMap);
			sb.append(" and r.unit_id=i.unit_id");
			Query query = mysql1SessionFactory.getCurrentSession().createSQLQuery(sb.toString()); 
			/*if(Utils.isNotNullOrEmpty(conditionMap.get("rePrintIds"))){
				query.setParameterList("rePrintIds", (List<Long>)conditionMap.get("rePrintIds"));
			}*/
			return query.executeUpdate();
		}else{
			return 0;
		}
	}
	@Override
	public int updateRePrints(List<Long> rePrintIds, Integer isDel, Integer isPrinted)
			throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" update t_fee_reprintinvoice r");
		if(isDel!=null){
			sb.append(" set is_del=").append(isDel).append(",del_time=sysdate()");
		}else{
			sb.append(" set is_printed=").append(isPrinted);
		}
		
		if(Utils.isNotNullOrEmpty(rePrintIds)){
			sb.append(" where reprint_id in (:rePrintIds)");
			Query query = mysql1SessionFactory.getCurrentSession().createSQLQuery(sb.toString()); 
			query.setParameterList("rePrintIds", rePrintIds);
			return query.executeUpdate();
		}else{
			return 0;
		}
	}

}

