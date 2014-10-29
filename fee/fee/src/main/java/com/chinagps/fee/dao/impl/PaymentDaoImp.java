package com.chinagps.fee.dao.impl;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.FloatType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.dao.PaymentDao;
import com.chinagps.fee.entity.vo.ItemVO;
import com.chinagps.fee.util.StringUtils;
import com.chinagps.fee.util.Utils;
import com.chinagps.fee.util.page.PageUtil;

/**
 * @Package:com.chinagps.fee.dao.impl
 * @ClassName:PaymentDaoImp
 * @Description:缴费记录持久层实现类
 * @author:zfy
 * @date:2014-6-4 下午2:37:17
 */
@Repository("paymentDao")  
public class PaymentDaoImp extends BaseDaoImpl implements PaymentDao {
	protected static final Logger logger = LoggerFactory.getLogger(PaymentDaoImp.class);

	@Override
	public List<Map<String, Object>> findLPayPrint4PrintSet(
			Map<String, Object> conditionMap, String order, boolean isDesc,
			int pageNo, int pageSize) throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" select c.customer_id as customerId,cu.customer_name as customerName");
		sb.append(",replace(concat(c.province,'省',c.city,'市 ',c.area,'区(县)',c.address),'省市区(县)','') address,c.post_code as postCode,max(p.pay_time) as payTime");
		sb.append(",max(p.print_time) as printTime,c.print_freq as printFreq,max(p.print_num) as printNum");
		sb.append(" from v_collection c");
		sb.append(" inner join t_fee_payment_dt_dt p on c.collection_id=p.collection_id");
		sb.append(" left join t_ba_customer cu on c.customer_id=cu.customer_id");
		sb = getCon4LPayPrint4PrintSet(sb, conditionMap);
		sb.append(" group by c.customer_id");
		if (StringUtils.isNotBlank(order)) {
			sb.append(" order by ").append(order);
			if (isDesc) {
				sb.append(" desc");
			} else {
				sb.append(" asc");
			}
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
	public int countLPayPrint4PrintSet(Map<String, Object> conditionMap)
			throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" select count(*) from (");
		sb.append(" SELECT c.customer_id from v_collection c");
		sb.append(" inner join t_fee_payment_dt p on c.collection_id=p.collection_id");
		sb.append(" left join t_ba_customer cu on c.customer_id=cu.customer_id");
		sb = getCon4LPayPrint4PrintSet(sb, conditionMap);
		sb.append(" group by c.customer_id ) as t");
		SQLQuery query=mysql1SessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		return ((BigInteger)query.uniqueResult()).intValue();
	
	}


	@Override
	public List<Map<String, Object>> findPrints(
			Map<String, Object> conditionMap, String order, boolean isDesc,
			int pageNo, int pageSize) throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" select bc.customer_id as customerId,replace(concat(bc.province,'省',bc.city,'市',bc.area,'区(县)',bc.address),'省市区(县)','') address,bc.post_code as postCode,bc.ac_name as acName,bc.addressee,bc.ac_no acNo");
		sb.append(" ,bc.is_delivery isDelivery,if(bc.is_delivery=1,'平邮','重点投递') as isDeliveryVal,");
		sb.append("  sum(fp.real_amount) as realAmount,max(fp.pay_time) as payTime");
		sb.append(" ,max(fp.print_time) as printTime,fp.print_num as printNum,fp.collection_id,group_concat(distinct cast(fp.payment_id as char)) as paymentIds");
		sb.append(" ,c.customer_name as customerName,c.email from t_fee_payment_dt fp ");
		sb.append(" inner join t_ba_unit u on fp.unit_id=u.unit_id");
		sb.append(" inner join t_ba_vehicle v on fp.vehicle_id=v.vehicle_id");
		sb.append(" inner join t_ba_customer c on fp.customer_id=c.customer_id");
		sb.append(" inner join t_ba_collection bc on fp.collection_id=bc.collection_id");
		sb = getCon4Prints(sb,conditionMap);
		sb.append(" group by fp.collection_id ");
		int limitStart=PageUtil.getPageStart(pageNo, pageSize);
		if (StringUtils.isNotBlank(order)) {
			sb.append(" order by ").append(order);
			if (isDesc) {
				sb.append(" desc");
			} else {
				sb.append(" asc");
			}
		}else{
			sb.append(" order by fp.pay_time desc");
		}
		if (pageNo>0 && pageSize>0) {
			sb.append(" limit ");
			sb.append(limitStart);
			sb.append(",");
			sb.append(pageSize);
		}
		SQLQuery query=mysql1SessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public int countPrints(Map<String, Object> conditionMap)
			throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" select count(*) from (");
		sb.append(" select fp.collection_id from t_fee_payment_dt fp ");
		sb.append(" inner join t_ba_unit u on fp.unit_id=u.unit_id");
		sb.append(" inner join t_ba_vehicle v on fp.vehicle_id=v.vehicle_id");
		sb.append(" inner join t_ba_customer c on fp.customer_id=c.customer_id");
		sb.append(" inner join t_ba_collection bc on fp.collection_id=bc.collection_id");
		sb = getCon4Prints(sb,conditionMap);
		sb.append(" group by fp.collection_id ");
		sb.append(") t");
		SQLQuery query=mysql1SessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		return ((BigInteger)query.uniqueResult()).intValue();
	} 

	 private StringBuffer getCon4Prints(StringBuffer sb,Map conditionMap){
		 sb.append(" where fp.pay_model=0 and fp.real_amount>0 and bc.address is not null and bc.address<>'' and bc.addressee is not null and bc.addressee<>'' and (bc.is_delivery = 1 or bc.is_delivery=2)");
		 if(conditionMap!=null){
				if (Utils.isNotNullOrEmpty(conditionMap.get("subcoNo"))) {//分公司ID
					sb.append(" and fp.subco_no=").append(conditionMap.get("subcoNo"));
				}
				if(Utils.isNotNullOrEmpty(conditionMap.get("startDate")) && Utils.isNotNullOrEmpty(conditionMap.get("endDate"))){//付费开始、结束时间
					sb.append(" and fp.pay_time between '").append(conditionMap.get("startDate"));
					sb.append(" 00:00:00' and '").append(conditionMap.get("endDate")).append(" 23:59:59'");
				}
				if(Utils.isNotNullOrEmpty(conditionMap.get("printNum"))){//打印次数
					//第一次打印
					int printNum=Integer.valueOf(conditionMap.get("printNum").toString());
					if(printNum==0){
						sb.append(" and fp.print_num=0");
					}else{//补打
						sb.append(" and fp.print_num>0");
					}
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("unitIds"))) {//车台集合
					sb.append(" and fp.unit_id in(:unitIds)");
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("unitId"))) {//车台id
					sb.append(" and fp.unit_id=").append(conditionMap.get("unitId"));
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("vehicleId"))) {//车辆id
					sb.append(" and fp.vehicle_id=").append(conditionMap.get("vehicleId"));
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("customerId"))) {//客户ID
					sb.append(" and fp.customer_id=").append(conditionMap.get("customerId"));
				}
				
				if (Utils.isNotNullOrEmpty(conditionMap.get("collectionId"))) {//托收资料ID
					sb.append(" and fp.collection_id=").append(conditionMap.get("collectionId"));
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("unitNum"))) {//车台呼号
					//sb.append(" and u.call_letter like '%").append(StringUtils.replaceSqlKey(conditionMap.get("unitNum"))).append("%'");
					sb.append(" and u.call_letter='").append(StringUtils.replaceSqlKey(conditionMap.get("unitNum"))).append("'");
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("vehicleNum"))) {//车辆
					//sb.append(" and v.plate_no like '%").append(StringUtils.replaceSqlKey(conditionMap.get("vehicleNum"))).append("%'");
					sb.append(" and v.plate_no='").append(StringUtils.replaceSqlKey(conditionMap.get("vehicleNum"))).append("'");
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("customerName"))) {//客户
					//sb.append(" and c.customer_name like '%").append(StringUtils.replaceSqlKey(conditionMap.get("customerName"))).append("%'");
					sb.append(" and c.customer_name='").append(StringUtils.replaceSqlKey(conditionMap.get("customerName"))).append("'");
				}
				if(conditionMap != null && Utils.isNotNullOrEmpty(conditionMap.get("printDate"))){//打印日期
					sb.append(" and fp.print_time between '").append(conditionMap.get("printDate")).append("' and '").append(conditionMap.get("printDate")).append(" 23:59:59'") ;
				}
				if(Utils.isNotNullOrEmpty(conditionMap.get("isDelivery"))){//发票是否投递, 系统值3110, 0=不投递, 1=平邮, 2=重点投递, 3=电子账单
					sb.append(" and bc.is_delivery=").append(conditionMap.get("isDelivery"));
					if(Integer.parseInt(conditionMap.get("isDelivery").toString())==3){//电子对账单
						String str=sb.toString().replace("bc.address is not null and bc.address<>''","c.email is not null and c.email<>''");
						sb.delete(0, sb.length());
						sb.append(str);
					}
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("payCtNo"))) {//合同号
					//sb.append(" and bc.pay_ct_no like '%").append(StringUtils.replaceSqlKey(conditionMap.get("payCtNo"))).append("%'");
					sb.append(" and bc.pay_ct_no ='").append(StringUtils.replaceSqlKey(conditionMap.get("payCtNo"))).append("'");
				}
			}
			return sb;
	}
	 private StringBuffer getCon4Prints2(StringBuffer sb,Map conditionMap){
			sb.append(" where vp.realAmount>0 ");//投递地址为空、费用为0的不能打印发票
			if(conditionMap!=null){
				if (Utils.isNotNullOrEmpty(conditionMap.get("subcoNo"))) {//分公司ID
					sb.append(" and vp.subcoNo=").append(conditionMap.get("subcoNo"));
				}
				if(Utils.isNotNullOrEmpty(conditionMap.get("startDate")) && Utils.isNotNullOrEmpty(conditionMap.get("endDate"))){//付费开始、结束时间
					sb.append(" and vp.payTime between '").append(conditionMap.get("startDate"));
					sb.append(" 00:00:00' and '").append(conditionMap.get("endDate")).append(" 23:59:59'");
				}
				if(Utils.isNotNullOrEmpty(conditionMap.get("isDelivery"))){//发票是否投递, 系统值3110, 0=不投递, 1=平邮, 2=重点投递, 3=电子账单
					sb.append(" and vp.isDelivery=").append(conditionMap.get("isDelivery"));
				}
				if(Utils.isNotNullOrEmpty(conditionMap.get("printDate"))){//打印日期
					sb.append(" and date_format(vp.printTime,'%Y-%m-%d')='").append(conditionMap.get("printDate")).append("'");
				}
				if(Utils.isNotNullOrEmpty(conditionMap.get("printNum"))){//打印次数
					//第一次打印
					int printNum=Integer.valueOf(conditionMap.get("printNum").toString());
					if(printNum==0){
						sb.append(" and vp.printNum=0");
						//如果是平邮、重点投递需要续打印频率关联
						if(Utils.isNotNullOrEmpty(conditionMap.get("isDelivery")) && Integer.parseInt(conditionMap.get("isDelivery").toString())!=3){//发票是否投递, 系统值3110, 0=不投递, 1=平邮, 2=重点投递, 3=电子账单
							sb.append(" and f_months_between(ifnull(case when vp.printTime ='0000-00-00 00:00:00' then null else vp.printTime end,date_sub(sysdate(),interval 2 year)),sysdate())>=vp.bc.print_freq");
						}
					}else{//补打
						sb.append(" and vp.printNum>0");
					}
				}
				/*if (Utils.isNotNullOrEmpty(conditionMap.get("paymentIds"))) {//缴费记录集合
					sb.append(" and vp.paymentIds in(:paymentIds)");
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("unitIds"))) {//车台集合
					sb.append(" and vp.unitId in(:unitIds)");
				}*/
				if (Utils.isNotNullOrEmpty(conditionMap.get("paymentIds"))) {//缴费记录集合
					sb.append(" and vp.paymentIds in(").append(conditionMap.get("paymentIds")).append(")");
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("unitIds"))) {//车台集合
					sb.append(" and vp.unitId in(").append(conditionMap.get("unitIds")).append(")");
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("customerId"))) {//客户ID
					sb.append(" and vp.customerId=").append(conditionMap.get("customerId"));
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("collectionId"))) {//托收资料ID
					sb.append(" and vp.collectionId=").append(conditionMap.get("collectionId"));
				}
			}
			return sb;
	}
	 private StringBuffer getCon4LPayPrint4PrintSet(StringBuffer sb,Map conditionMap){
			sb.append(" where p.real_amount>0 and c.address is not null");//投递地址为空、费用为0的不能打印发票
			if(conditionMap!=null){
				if(Utils.isNotNullOrEmpty(conditionMap.get("subcoNo"))){//分公司ID
					sb.append(" and p.subco_no=").append(conditionMap.get("subcoNo"));
				}
				if(Utils.isNotNullOrEmpty(conditionMap.get("orgId"))){//营业处ID
					sb.append(" and p.org_id=").append(conditionMap.get("orgId"));
				}
				if(Utils.isNotNullOrEmpty(conditionMap.get("unitId"))){//车台ID
					sb.append(" and p.unit_id=").append(conditionMap.get("unitId"));
				}
				if(Utils.isNotNullOrEmpty(conditionMap.get("vehicleId"))){//车辆ID
					sb.append(" and p.vehicle_id=").append(conditionMap.get("vehicleId"));
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("customerId"))) {//客户ID
					sb.append(" and c.customer_id=").append(conditionMap.get("customerId"));
				}
			}
			return sb;
	}
	 
	 private StringBuffer getCon4Prints2Pmd(StringBuffer sb,Map conditionMap,int custType){
		 sb.append("select group_concat(distinct p.payment_id separator ',') as paymentIds,p.unit_id unitId,c.cust_type as custType,");
		 sb.append("p.vehicle_id vehicleId,p.customer_id customerId,p.collection_id collectionId,sum(p.real_amount) as realAmount, date_format(p.pay_time,'%Y-%m-%d') as payTime,max(p.print_time) as printTime,");
		 sb.append("p.bw_no as bwNo,date_format(min(p.s_date),'%Y-%m-%d') as sdate,date_format(max(p.e_date),'%Y-%m-%d') as edate,p.print_num printNum,");
		 sb.append("p.remark,c.customer_name as customerName,bc.addressee as addressee,");
		 sb.append("replace(concat(bc.province,'省',bc.city,'市',bc.area,'区(县)',bc.address),'省市区(县)','') as address,");
		 sb.append("bc.post_code as postCode,");
		 sb.append("if((c.cust_type = 0),concat(u.call_letter,'(',v.plate_no,')'),'统一托收') as callLetter1,");
		 sb.append("if((c.cust_type = 0),u.call_letter,'统一托收') as callLetter,");
		 sb.append("v.plate_no as plateNo,bc.bank as bank,bc.ac_no as acNo,bc.ac_name as acName,curdate() as printDate");
		// sb.append(",group_concat(p.feetype_id) as feetypeIds, group_concat(p.real_amount) as amounts,group_concat(date_format(p.pay_time,'%Y-%m-%d')) as payDates");
		 sb.append(" from t_fee_payment_dt p");
		 sb.append(" inner join t_ba_unit u on p.unit_id=u.unit_id ");
		 sb.append(" inner join t_ba_vehicle v on p.vehicle_id=v.vehicle_id ");
		 sb.append(" inner join t_ba_customer c on p.customer_id=c.customer_id ");
		 sb.append(" inner join t_ba_collection bc on p.collection_id=bc.collection_id ");
		//表示是补打，关联补打发票记录表
			if (conditionMap!=null && Utils.isNotNullOrEmpty(conditionMap.get("isRePrint"))) {
				sb.append(" inner join t_fee_reprintinvoice r on p.unit_id=r.unit_id");
				sb.append(" and date_format(p.pay_time,'%y-%m')=date_format(r.print_time,'%y-%m')");
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
			}
		 sb.append(" where p.pay_model = 0");
		 sb.append(" and p.real_amount > 0");
		 sb.append(" and bc.address is not null and bc.address <> ''and bc.addressee is not null and bc.addressee <> '' and (bc.is_delivery = 1 or bc.is_delivery=2) ");
		 if(conditionMap!=null){
	
				if (Utils.isNotNullOrEmpty(conditionMap.get("subcoNo"))) {//分公司ID
					sb.append(" and p.subco_no=").append(conditionMap.get("subcoNo"));
				}
				if(Utils.isNotNullOrEmpty(conditionMap.get("startDate")) && Utils.isNotNullOrEmpty(conditionMap.get("endDate"))){//付费开始、结束时间
					sb.append(" and p.pay_time between '").append(conditionMap.get("startDate"));
					sb.append(" 00:00:00' and '").append(conditionMap.get("endDate")).append(" 23:59:59'");
				}
				if(Utils.isNotNullOrEmpty(conditionMap.get("isDelivery"))){//发票是否投递, 系统值3110, 0=不投递, 1=平邮, 2=重点投递, 3=电子账单
					sb.append(" and bc.is_delivery=").append(conditionMap.get("isDelivery"));
				}
				if(Utils.isNotNullOrEmpty(conditionMap.get("printDate"))){//打印日期
					sb.append(" and date_format(p.print_time,'%Y-%m-%d')='").append(conditionMap.get("printDate")).append("'");
				}
				if(Utils.isNotNullOrEmpty(conditionMap.get("printNum"))){//打印次数
					//第一次打印
					int printNum=Integer.valueOf(conditionMap.get("printNum").toString());
					if(printNum==0){
						sb.append(" and p.print_num=0");
						//如果是平邮、重点投递需要续打印频率关联
						if(Utils.isNotNullOrEmpty(conditionMap.get("isDelivery")) && Integer.parseInt(conditionMap.get("isDelivery").toString())!=3){//发票是否投递, 系统值3110, 0=不投递, 1=平邮, 2=重点投递, 3=电子账单
							sb.append(" and f_months_between(ifnull(case when p.print_time ='0000-00-00 00:00:00' then null else p.print_time end,date_sub(sysdate(),interval 2 year)),sysdate())>=bc.print_freq");
						}
					}else{//补打
						sb.append(" and p.print_time>0");
					}
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("paymentIds"))) {//缴费记录集合
					sb.append(" and p.payment_id in(").append(conditionMap.get("paymentIds")).append(")");
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("unitIds"))) {//车台集合
					sb.append(" and p.unit_id in(").append(conditionMap.get("unitIds")).append(")");
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("customerId"))) {//客户ID
					sb.append(" and p.customer_id=").append(conditionMap.get("customerId"));
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("collectionId"))) {//托收资料ID
					sb.append(" and p.collection_id=").append(conditionMap.get("collectionId"));
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("unitNum"))) {//车台呼号
					//sb.append(" and u.call_letter like '%").append(StringUtils.replaceSqlKey(conditionMap.get("unitNum"))).append("%'");
					sb.append(" and u.call_letter='").append(StringUtils.replaceSqlKey(conditionMap.get("unitNum"))).append("'");
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("vehicleNum"))) {//车辆
					//sb.append(" and v.plate_no like '%").append(StringUtils.replaceSqlKey(conditionMap.get("vehicleNum"))).append("%'");
					sb.append(" and v.plate_no='").append(StringUtils.replaceSqlKey(conditionMap.get("vehicleNum"))).append("'");
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("customerName"))) {//客户
					//sb.append(" and c.customer_name like '%").append(StringUtils.replaceSqlKey(conditionMap.get("customerName"))).append("%'");
					sb.append(" and c.customer_name='").append(StringUtils.replaceSqlKey(conditionMap.get("customerName"))).append("'");
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("payCtNo"))) {//合同号
					//sb.append(" and bc.pay_ct_no like '%").append(StringUtils.replaceSqlKey(conditionMap.get("payCtNo"))).append("%'");
					sb.append(" and bc.pay_ct_no ='").append(StringUtils.replaceSqlKey(conditionMap.get("payCtNo"))).append("'");
				}
			}
		    if(custType==0){//私家车
		    	sb.append(" and c.cust_type=0");
		    	sb.append(" group by p.unit_id");//,p.remark
		    }else{
		    	sb.append(" and c.cust_type<>0");
		    	sb.append(" group by p.collection_id,p.remark");
		    }
			return sb;
	}
	 @Override
		public List<Map<String, Object>> findPrints2(
				Map<String, Object> conditionMap) throws SystemException {
			StringBuffer sb=new StringBuffer();
			sb=getCon4Prints2Pmd(sb, conditionMap, 0);
			sb.append(" union all ");
			sb=getCon4Prints2Pmd(sb, conditionMap, 1);
			SQLQuery query=mysql1SessionFactory.getCurrentSession().createSQLQuery(sb.toString());
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			return query.list();
		}
	/*@Override
	public List<Map<String, Object>> findPrints2(
			Map<String, Object> conditionMap) throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" select bc.customer_id as customerId,bcu.customer_name as customerName,bc.address,bc.post_code as postCode");
		sb.append(" ,bu.call_letter callLetter,bc.bank,bc.ac_no acNo,bc.ac_name acName, p.bw_no bwNo,p.unit_id unitId,current_date() as printDate");
		sb.append(" ,p.s_date as sdate,p.e_date edate,p.print_num printNum");
		sb.append(" ,p.itemFeetypeIds,p.itemAmounts,p.itemPaytimes,p.paymentIds,p.real_amount realAmount,p.pay_time payTime");
		sb.append(" from v_collection bc inner join");
		sb.append(" (select group_concat(fp.feetype_id) as itemFeetypeIds,");
		sb.append(" group_concat(fp.real_amount) as itemAmounts");
		sb.append(" ,group_concat(date_format(fp.pay_time,'%Y-%m-%d')) as itemPaytimes");
		sb.append(" ,group_concat(fp.payment_id) as paymentIds,fp.unit_id,fp.customer_id,sum(fp.real_amount) as real_amount");
		sb.append(" ,fp.pay_time,fp.bw_no,date_format(min(fp.s_date),'%Y-%m-%d') as s_date,date_format(max(fp.e_date),'%Y-%m-%d') as e_date");
		sb.append(",fp.print_num");
		sb.append(" from t_fee_payment_dt fp where fp.pay_model=0 group by fp.unit_id) p");
		sb.append(" on bc.customer_id=p.customer_id");
		sb.append(" left join t_ba_customer bcu on bc.customer_id=bcu.customer_id");
		sb.append(" left join t_ba_unit bu on bu.unit_id= p.unit_id");
		sb.append(" select vp.subcoNo,vp.customerId,vp.customerName,vp.custType,vp.addressee,vp.address,vp.postCode,");
		sb.append(" vp.callLetter1,vp.callLetter,vp.plateNo,vp.bank,vp.acNo,vp.acName,vp.bwNo,vp.unitId,vp.printDate,min(vp.sdate) sdate,");
		sb.append("max(vp.edate) edate,vp.printNum,");
		sb.append("group_concat(vp.paymentIds) paymentIds,sum(vp.realAmount) realAmount,");
		sb.append("max(vp.payTime) payTime,vp.remark,vp.printFreq,vp.isDelivery from v_print_data vp");

		//sb.append(" select vp.* from v_print_data vp");
		//表示是补打，关联补打发票记录表
		if (conditionMap!=null && Utils.isNotNullOrEmpty(conditionMap.get("isRePrint"))) {
			sb.append(" inner join t_fee_reprintinvoice r on vp.unitid=r.unit_id");
			sb.append(" and date_format(vp.paytime,'%y-%m')=date_format(r.print_time,'%y-%m')");
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
		}
		sb = getCon4Prints2(sb,conditionMap);
		sb.append(" and vp.custType=0");//私家车
		sb.append(" group by vp.unitId");
		
		sb.append(" union all ");
		
		sb.append(" select vp.subcoNo,vp.customerId,vp.customerName,vp.custType,vp.addressee,vp.address,vp.postCode,");
		sb.append(" vp.callLetter1,vp.callLetter,vp.plateNo,vp.bank,vp.acNo,vp.acName,vp.bwNo,vp.unitId,vp.printDate,min(vp.sdate) sdate,");
		sb.append("max(vp.edate) edate,vp.printNum,");
		sb.append("group_concat(vp.paymentIds) paymentIds,sum(vp.realAmount) realAmount,");
		sb.append("max(vp.payTime) payTime,vp.remark,vp.printFreq,vp.isDelivery from v_print_data vp");

		//sb.append(" select vp.* from v_print_data vp");
		//表示是补打，关联补打发票记录表
		if (conditionMap!=null && Utils.isNotNullOrEmpty(conditionMap.get("isRePrint"))) {
			sb.append(" inner join t_fee_reprintinvoice r on vp.unitid=r.unit_id");
			sb.append(" and date_format(vp.paytime,'%y-%m')=date_format(r.print_time,'%y-%m')");
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
		}
		sb = getCon4Prints2(sb,conditionMap);
		sb.append(" and vp.custType<>0");//集客
		sb.append(" group by vp.collectionId,vp.remark");
		SQLQuery query=mysql1SessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		if (conditionMap!=null && Utils.isNotNullOrEmpty(conditionMap.get("paymentIds"))) {
			query.setParameterList("paymentIds", (List<Long>)conditionMap.get("paymentIds"));
		}
		if (conditionMap!=null && Utils.isNotNullOrEmpty(conditionMap.get("unitIds"))) {
			query.setParameterList("unitIds", (List<Long>)conditionMap.get("unitIds"));
		}
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}
*/
	@Override
	public int updatePrintNums(Session session,Map<String, Object> conditionMap,int printNum) throws SystemException {
		if(conditionMap!=null){
			StringBuffer sb=new StringBuffer();
			/*sb.append(" update t_fee_payment p");
			if(printNum>0){
				sb.append(" set is_invoice=1,p.print_num=ifnull(p.print_num,0)+1,p.print_time=sysdate()");
			}else{
				sb.append(" set is_invoice=0,p.print_num=0,p.print_time=null");
			}
			
			sb.append(" where payment_id in (").append(paymentIds).append(")");
			Query query = mysql1SessionFactory.getCurrentSession().createSQLQuery(sb.toString());  
		    query.executeUpdate();
			
		    sb.delete(0, sb.length());*/
		    
			sb.append(" update t_fee_payment_dt fp");
			sb.append(" inner join t_ba_unit u inner join t_ba_vehicle v ");
			sb.append(" inner join t_ba_customer c inner join t_ba_collection bc ");
			if(printNum>0){
				sb.append(" set is_invoice=1,fp.print_num=ifnull(fp.print_num,0)+1,fp.print_time=sysdate()");
			}else{
				sb.append(" set is_invoice=0,fp.print_num=0,fp.print_time=null");
			}
			
			sb=getCon4PaymentDts4Print(sb, conditionMap);
			if(session==null){
				session=mysql1SessionFactory.getCurrentSession();
			}
			Query query = session.createSQLQuery(sb.toString());  
			return query.executeUpdate();
		}else{
			return 0;
		}
	}

	@Override
	public List<Map<String, Object>> findPayments(
			Map<String, Object> conditionMap, String order, boolean isDesc,
			int pageNo, int pageSize) throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" select p.payment_id paymentId,p.customer_id customerId,c.customer_name customerName,");
		sb.append(" p.vehicle_id vehicled,v.plate_no plateNo,p.unit_id unitId, u.call_letter callLetter, date_format(u.service_date,'%Y-%m-%d') serviceDate,");
		sb.append("group_concat(sv.sname,':',p.ac_amount) feetypes1,");
		sb.append(" group_concat(sv.sname,':',p.ac_amount,'【','服务时长:',p.s_months,'个月',ifnull(p.s_days,0),'天,',date_format(p.s_date,'%Y-%m-%d'),'~',date_format(p.e_date,'%Y-%m-%d'),'】' separator '<br/>') feetypes2,");
		sb.append(" sv2.sname payModel,p.s_months sMonths,sum(p.ac_amount) acAmount,sum(p.real_amount) realAmount,");
		sb.append(" p.pay_time payTime,p.stamp,p.op_id opId,p.remark,p.is_invoice isInvoice,");
		sb.append(" p.print_num printNum,p.print_time printTime,");
		sb.append(" p.bw_no bwNo,co.bank,co.ac_no acNo,co.pay_ct_no payCtNo");
		sb.append(" from t_fee_payment_dt p ");
		sb.append(" inner join t_ba_unit u on p.unit_id=u.unit_id");
		sb.append(" inner join t_ba_vehicle v on p.vehicle_id=v.vehicle_id");
		sb.append(" inner join t_ba_customer c on p.customer_id=c.customer_id");
		sb.append(" inner join t_ba_collection co on p.collection_id=co.collection_id");
		sb.append(" left join t_sys_value sv on sv.stype=3100 and p.feetype_id=sv.svalue");
		sb.append(" left join t_sys_value sv2 on sv2.stype=3050 and p.pay_model=sv2.svalue");
		sb = getCon4Payments(sb,conditionMap);
		 sb.append(" group by p.unit_id,p.payment_id");
		 //sb.append(" group by payment_id");
		if (StringUtils.isNotBlank(order)) {
			sb.append(" order by ").append(order);
			if (isDesc) {
				sb.append(" desc");
			} else {
				sb.append(" asc");
			}
		}else{
			sb.append(" order by p.pay_time desc");
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
	public int countPayments(Map<String, Object> conditionMap)
			throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" select count(*) from (");
		sb.append(" select p.payment_id");
		sb.append(" from t_fee_payment_dt p ");
		sb.append(" inner join t_ba_unit u on p.unit_id=u.unit_id");
		sb.append(" inner join t_ba_vehicle v on p.vehicle_id=v.vehicle_id");
		sb.append(" inner join t_ba_customer c on p.customer_id=c.customer_id");
		sb.append(" inner join t_ba_collection co on p.collection_id=co.collection_id");
		sb.append(" left join t_sys_value sv on sv.stype=3100 and p.feetype_id=sv.svalue");
		sb.append(" left join t_sys_value sv2 on sv2.stype=3050 and p.pay_model=sv2.svalue");
		sb = getCon4Payments(sb,conditionMap);
		sb.append(" group by p.unit_id,p.pay_time");
		//sb.append(" group by payment_id");
		sb.append(") t");
		SQLQuery query=mysql1SessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		return ((BigInteger)query.uniqueResult()).intValue();
	}
	 private StringBuffer getCon4Payments(StringBuffer sb,Map conditionMap){
			sb.append(" where 1=1 ");
			if(conditionMap!=null){
				if(Utils.isNotNullOrEmpty(conditionMap.get("startDate")) && Utils.isNotNullOrEmpty(conditionMap.get("endDate"))){//付费开始、结束时间
					sb.append(" and p.pay_time between '").append(conditionMap.get("startDate"));
					sb.append(" 00:00:00' and '").append(conditionMap.get("endDate")).append(" 23:59:59'");
				}
				if(Utils.isNotNullOrEmpty(conditionMap.get("payModel"))){
					sb.append(" and p.pay_model=").append(conditionMap.get("payModel"));
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("unitId"))) {//车台集合
					sb.append(" and p.unit_id=").append(conditionMap.get("unitId"));;
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("customerId"))) {//客户ID
					sb.append(" and p.customer_id=").append(conditionMap.get("customerId"));
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("vehicleId"))) {//车辆ID
					sb.append(" and p.vehicle_id=").append(conditionMap.get("vehicleId"));
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("subcoNo"))) {//分公司ID
					sb.append(" and p.subco_no=").append(conditionMap.get("subcoNo"));
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("bwNo"))) {//收据单号
					sb.append(" and p.bw_no like '%").append(conditionMap.get("bwNo")).append("%'");
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
				if (Utils.isNotNullOrEmpty(conditionMap.get("acNo"))) {//银行账号
					sb.append(" and co.ac_no like '%").append(StringUtils.replaceSqlKey(conditionMap.get("acNo"))).append("%'");
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("payCtNo"))) {//合同号
					sb.append(" and co.pay_ct_no like '%").append(StringUtils.replaceSqlKey(conditionMap.get("payCtNo"))).append("%'");
				}
			}
			return sb;
	}

	@Override
	public List<Map<String, Object>> findArrearageInfos(
			Map<String, Object> conditionMap, String order, boolean isDesc,
			int pageNo, int pageSize) throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append("select u.vehicle_id vehicleId,v.plate_no plateNo,u.customer_id customerId,c.customer_name customerName,");
		sb.append(" l.phones,u.unit_id unitId,u.call_letter callLetter,u.sales,date_format(u.service_date,'%Y-%m-%d') serviceDate,sv.sname payModel,i.feeIds");
		sb.append(" from t_ba_unit u");
		sb.append(" inner join t_ba_vehicle v on u.vehicle_id=v.vehicle_id inner join t_ba_customer c on u.customer_id=c.customer_id");
		sb.append(" left join (select l1.customer_id,group_concat(l1.linkman,l1.phone) as phones from t_ba_linkman l1 group by l1.customer_id) l on u.customer_id=l.customer_id");
	    sb.append(" inner join (select i1.unit_id,i1.pay_model,group_concat(i1.fee_id) feeIds");
	    sb.append(" from t_fee_info i1 where i1.real_amount>0");
	    if(conditionMap!=null){
    	  if (Utils.isNotNullOrEmpty(conditionMap.get("subcoNo"))) {//分公司ID
				sb.append(" and i1.subco_no=").append(conditionMap.get("subcoNo"));
			}
	       if(Utils.isNotNullOrEmpty(conditionMap.get("payModel"))){
	    		sb.append(" and i1.pay_model=").append(conditionMap.get("payModel"));
		   }
	       if(Utils.isNotNullOrEmpty(conditionMap.get("arrType"))){
	    	   sb.append(" and i1.feetype_id=").append(conditionMap.get("arrType"));
	       }
	       if(Utils.isNotNullOrEmpty(conditionMap.get("serviceEdate"))){
	    	   sb.append(" and i1.fee_sedate<'").append((String)conditionMap.get("serviceEdate")).append("'");
	    	   sb.append(" and i1.fee_date<'").append((String)conditionMap.get("serviceEdate")).append(" 23:59:59'");
	       }
	       if(Utils.isNotNullOrEmpty(conditionMap.get("serviceSdate"))){
	    	   sb.append(" and i1.fee_sedate>='").append((String)conditionMap.get("serviceSdate")).append("'");
	       }
	       if (Utils.isNotNullOrEmpty(conditionMap.get("unitId"))) {//车台集合
				sb.append(" and i1.unit_id=").append(conditionMap.get("unitId"));;
			}
			if (Utils.isNotNullOrEmpty(conditionMap.get("customerId"))) {//客户ID
				sb.append(" and i1.customer_id=").append(conditionMap.get("customerId"));
			}
			if (Utils.isNotNullOrEmpty(conditionMap.get("vehicleId"))) {//车辆ID
				sb.append(" and i1.vehicle_id=").append(conditionMap.get("vehicleId"));
			}
			
	    }
	    sb.append(" group by i1.unit_id ");
	    int limitStart=PageUtil.getPageStart(pageNo, pageSize);
		/*if (pageNo>0 && pageSize>0) {
			sb.append(" limit ");
			sb.append("0,");
			sb.append(limitStart+pageSize);
		}*/
	    sb.append(" ) i on u.unit_id=i.unit_id");
		
		/* sb.append(" inner join (select f1.unit_id,f1.pay_model,concat('合计:',sum(sarrearageamount),'元【',group_concat(f1.arrearageamount),'】') ");
		 sb.append("feeItems from ( select i1.unit_id, i1.pay_model,concat((select s.sname from t_sys_value s where s.stype=3100 and s.is_del=0 " );
		 sb.append("and s.svalue=i1.feetype_id),':', max(ceil(if(f_months_between(i1.fee_sedate,'2014-09-30')<0,0,");
		 sb.append("f_months_between(i1.fee_sedate,'2014-09-30'))/ifnull(i1.fee_cycle,1))*ifnull(i1.real_amount,0)),'元(',");
		 sb.append("date_format(i1.fee_sedate,'%Y-%m-%d'),')') arrearageamount, max(ceil(if(f_months_between(i1.fee_sedate, '2014-09-30')<0,0,");
		 sb.append("f_months_between(i1.fee_sedate,'2014-09-30'))/ifnull(i1.fee_cycle,1))*ifnull(i1.real_amount,0)) sarrearageamount");
	     sb.append(" from t_fee_info i1 where i1.fee_date<=sysdate() and i1.fee_sedate<sysdate()");
	     if(conditionMap!=null){
	       if(Utils.isNotNullOrEmpty(conditionMap.get("payModel"))){
	    		sb.append(" and i1.pay_model=").append(conditionMap.get("payModel"));
		   }
	       if(Utils.isNotNullOrEmpty(conditionMap.get("arrType"))){
	    	   sb.append(" and i1.feetype_id=").append(conditionMap.get("arrType"));
	       }
	       if(Utils.isNotNullOrEmpty(conditionMap.get("serviceEdate"))){
	    	   sb.append(" and i1.fee_sedate<'").append((String)conditionMap.get("serviceEdate")).append("'");
	       }
	     }
		 sb.append("group by i1.unit_id,i1.feetype_id) f1 group by f1.unit_id)  i on u.unit_id=i.unit_id");
	    */
	    sb.append(" left join t_sys_value sv on sv.stype=3050 and i.pay_model=sv.svalue");
		sb = getCon4ArrearageInfos(sb,conditionMap);
		sb.append(" group by u.unit_id");
		if (StringUtils.isNotBlank(order)) {
			sb.append(" order by ").append(order);
			if (isDesc) {
				sb.append(" desc");
			} else {
				sb.append(" asc");
			}
		}
		if (pageNo>0 && pageSize>0) {
			sb.append(" limit ");
			sb.append(limitStart);
			sb.append(",");
			sb.append(pageSize);
		}
		SQLQuery query=mysql1SessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public int countArrearageInfos(Map<String, Object> conditionMap)
			throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" select count(u.unit_id) ");
		sb.append(" from t_ba_unit u");
		sb.append(" inner join t_ba_vehicle v on u.vehicle_id=v.vehicle_id inner join t_ba_customer c on u.customer_id=c.customer_id");
		sb.append(" inner join (select i1.unit_id,i1.pay_model ");
	    sb.append(" from t_fee_info i1 where i1.real_amount>0");
	    if(conditionMap!=null){
	    	 if (Utils.isNotNullOrEmpty(conditionMap.get("subcoNo"))) {//分公司ID
					sb.append(" and i1.subco_no=").append(conditionMap.get("subcoNo"));
				}
		       if(Utils.isNotNullOrEmpty(conditionMap.get("payModel"))){
		    		sb.append(" and i1.pay_model=").append(conditionMap.get("payModel"));
			   }
		       if(Utils.isNotNullOrEmpty(conditionMap.get("arrType"))){
		    	   sb.append(" and i1.feetype_id=").append(conditionMap.get("arrType"));
		       }
		       if(Utils.isNotNullOrEmpty(conditionMap.get("serviceEdate"))){
		    	   sb.append(" and i1.fee_sedate<'").append((String)conditionMap.get("serviceEdate")).append("'");
		    	   sb.append(" and i1.fee_date<'").append((String)conditionMap.get("serviceEdate")).append(" 23:59:59'");
		       }
		       if(Utils.isNotNullOrEmpty(conditionMap.get("serviceSdate"))){
		    	   sb.append(" and i1.fee_sedate>='").append((String)conditionMap.get("serviceSdate")).append("'");
		       }
		       if (Utils.isNotNullOrEmpty(conditionMap.get("unitId"))) {//车台集合
					sb.append(" and i1.unit_id=").append(conditionMap.get("unitId"));;
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("customerId"))) {//客户ID
					sb.append(" and i1.customer_id=").append(conditionMap.get("customerId"));
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("vehicleId"))) {//车辆ID
					sb.append(" and i1.vehicle_id=").append(conditionMap.get("vehicleId"));
				}
				
		    }
	    sb.append(" group by i1.unit_id ) i on u.unit_id=i.unit_id");
		sb = getCon4ArrearageInfos(sb,conditionMap);
		//sb.append(" group by u.unit_id ) t");
		SQLQuery query=mysql1SessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		return ((BigInteger)query.uniqueResult()).intValue();
	}
	
	private StringBuffer getCon4ArrearageInfos(StringBuffer sb,Map conditionMap){
			sb.append(" where u.flag=0");
			if(conditionMap!=null){
				if (Utils.isNotNullOrEmpty(conditionMap.get("subcoNo"))) {//分公司ID
					sb.append(" and u.subco_no=").append(conditionMap.get("subcoNo"));
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("unitId"))) {//车台集合
					sb.append(" and u.unit_id=").append(conditionMap.get("unitId"));;
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("customerId"))) {//客户ID
					sb.append(" and u.customer_id=").append(conditionMap.get("customerId"));
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("vehicleId"))) {//车辆ID
					sb.append(" and u.vehicle_id=").append(conditionMap.get("vehicleId"));
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
			}
			return sb;
	}

	@Override
	public int addPrint(Session session,Map<String, Object> conditionMap) throws SystemException {
		if(conditionMap!=null){
			StringBuffer sb=new StringBuffer();
			sb.append(" insert into t_fee_print(payment_id,subco_no,");
			sb.append(" print_time,addressee,address,post_code,");
			sb.append(" op_id,remark)");
			sb.append(" select fp.payment_sub_id,fp.subco_no,");
			sb.append(" sysdate(),co.addressee,co.address,co.post_code,");
			sb.append(" fp.op_id,concat(co.ac_name,' ',co.ac_no)");
			sb.append(" from t_fee_payment_dt fp ");
			sb.append(" inner join t_ba_unit u on fp.unit_id=u.unit_id");
			sb.append(" inner join t_ba_vehicle v on fp.vehicle_id=v.vehicle_id");
			sb.append(" inner join t_ba_customer c on fp.customer_id=c.customer_id");
			sb.append(" inner join t_ba_collection co on fp.collection_id=co.collection_id");
			//sb.append(" where p.payment_sub_id in (").append(paymentIds).append(")");
			sb=getCon4PaymentDts4Print(sb, conditionMap);
			if(session==null){
				session=mysql1SessionFactory.getCurrentSession();
			}
			Query query = session.createSQLQuery(sb.toString());  
			return query.executeUpdate();
		}else{
			return 0;
		}
	}

	@Override
	public List<Map<String, Object>> findPrintHistorys(
			Map<String, Object> conditionMap, String order, boolean isDesc,
			int pageNo, int pageSize) throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" select p.customer_id customerId,c.customer_name customerName,p.vehicle_id vehicleId,v.plate_no plateNo,");
		sb.append(" p.unit_id unitId,u.call_letter callLetter,sum(p.ac_amount) acAmount,sum(p.real_amount) realAmount,");
		sb.append(" pr.print_time printTime,pr.addressee,");
		sb.append(" pr.address,pr.post_code postCode,pr.stamp,");
		sb.append(" pr.op_id opId,pr.remark");
		sb.append(" from t_fee_print pr inner join t_fee_payment_dt p on pr.payment_id=p.payment_sub_id");
		sb.append(" inner join t_ba_unit u on p.unit_id=u.unit_id ");
		sb.append(" inner join t_ba_vehicle v on p.vehicle_id=v.vehicle_id ");
		sb.append(" inner join t_ba_customer c on p.customer_id=c.customer_id ");
		sb.append(" inner join t_ba_collection bc on p.collection_id=bc.collection_id ");
		 sb = getCon4PrintHistorys(sb,conditionMap);
		 sb.append(" group by p.unit_id,p.pay_time,pr.print_time");
		if (StringUtils.isNotBlank(order)) {
			sb.append(" order by ").append(order);
			if (isDesc) {
				sb.append(" desc");
			} else {
				sb.append(" asc");
			}
		}else{
			sb.append(" order by pr.print_time desc");
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
	public int countPrintHistorys(Map<String, Object> conditionMap)
			throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" select count(*) from (");
		sb.append(" select p.customer_id");
		sb.append(" from t_fee_print pr inner join t_fee_payment_dt p on pr.payment_id=p.payment_sub_id");
		sb.append(" inner join t_ba_unit u on p.unit_id=u.unit_id ");
		sb.append(" inner join t_ba_vehicle v on p.vehicle_id=v.vehicle_id ");
		sb.append(" inner join t_ba_customer c on p.customer_id=c.customer_id ");
		sb.append(" inner join t_ba_collection bc on p.collection_id=bc.collection_id ");
		 sb = getCon4PrintHistorys(sb,conditionMap);
		 sb.append(" group by p.unit_id,p.pay_time,pr.print_time");
		//sb.append(" group by payment_id");
		sb.append(") t");
		SQLQuery query=mysql1SessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		return ((BigInteger)query.uniqueResult()).intValue();
	}
	 private StringBuffer getCon4PrintHistorys(StringBuffer sb,Map conditionMap){
			sb.append(" where 1=1 ");
			if(conditionMap!=null){
				if (Utils.isNotNullOrEmpty(conditionMap.get("subcoNo"))) {//分公司ID
					sb.append(" and pr.subco_no=").append(conditionMap.get("subcoNo"));
				}
				if(Utils.isNotNullOrEmpty(conditionMap.get("startDate")) && Utils.isNotNullOrEmpty(conditionMap.get("endDate"))){//付费开始、结束时间
					sb.append(" and pr.print_time between '").append(conditionMap.get("startDate"));
					sb.append(" 00:00:00' and '").append(conditionMap.get("endDate")).append(" 23:59:59'");
				}
				if(Utils.isNotNullOrEmpty(conditionMap.get("payModel"))){
					sb.append(" and p.pay_model=").append(conditionMap.get("payModel"));
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("unitId"))) {//车台集合
					sb.append(" and p.unit_id=").append(conditionMap.get("unitId"));;
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("customerId"))) {//客户ID
					sb.append(" and p.customer_id=").append(conditionMap.get("customerId"));
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("vehicleId"))) {//车辆ID
					sb.append(" and p.vehicle_id=").append(conditionMap.get("vehicleId"));
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("collectionId"))) {//托收资料ID
					sb.append(" and p.collection_id=").append(conditionMap.get("collectionId"));
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("unitNum"))) {//车台呼号
					//sb.append(" and u.call_letter like '%").append(StringUtils.replaceSqlKey(conditionMap.get("unitNum"))).append("%'");
					sb.append(" and u.call_letter='").append(StringUtils.replaceSqlKey(conditionMap.get("unitNum"))).append("'");
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("vehicleNum"))) {//车辆
					//sb.append(" and v.plate_no like '%").append(StringUtils.replaceSqlKey(conditionMap.get("vehicleNum"))).append("%'");
					sb.append(" and v.plate_no='").append(StringUtils.replaceSqlKey(conditionMap.get("vehicleNum"))).append("'");
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("customerName"))) {//客户
					//sb.append(" and c.customer_name like '%").append(StringUtils.replaceSqlKey(conditionMap.get("customerName"))).append("%'");
					sb.append(" and c.customer_name='").append(StringUtils.replaceSqlKey(conditionMap.get("customerName"))).append("'");
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("payCtNo"))) {//合同号
					//sb.append(" and bc.pay_ct_no like '%").append(StringUtils.replaceSqlKey(conditionMap.get("payCtNo"))).append("%'");
					sb.append(" and bc.pay_ct_no ='").append(StringUtils.replaceSqlKey(conditionMap.get("payCtNo"))).append("'");
				}
				
			}
			return sb;
	}

	@Override
	public List<ItemVO> findPaymentDts4Print(
			Map<String, Object> conditionMap) throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" select fp.feetype_id as itemId,sv.sname as itemName,sum(fp.real_amount) as itemMoney,");
		sb.append(" date_format(fp.pay_time,'%Y-%m-%d') as payDate from t_fee_payment_dt fp");
		sb.append(" left join t_sys_value sv on sv.stype=3100 and fp.feetype_id=sv.svalue");
		sb.append(" inner join t_ba_unit u on fp.unit_id=u.unit_id ");
		sb.append(" inner join t_ba_vehicle v on fp.vehicle_id=v.vehicle_id ");
		sb.append(" inner join t_ba_customer c on fp.customer_id=c.customer_id ");
		sb.append(" inner join t_ba_collection bc on fp.collection_id=bc.collection_id ");
		sb = getCon4PaymentDts4Print(sb,conditionMap);
		sb.append(" group by fp.feetype_id");
		SQLQuery query=mysql1SessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		
		query.addScalar("itemId",StringType.INSTANCE).addScalar("itemName",StringType.INSTANCE);
		query.addScalar("itemMoney",FloatType.INSTANCE).addScalar("payDate",StringType.INSTANCE);   
		query.setResultTransformer(Transformers.aliasToBean(ItemVO.class));
		return query.list();
	}
	 private StringBuffer getCon4PaymentDts4Print(StringBuffer sb,Map conditionMap){
			sb.append(" where 1=1 ");
			if(conditionMap!=null){
				if (Utils.isNotNullOrEmpty(conditionMap.get("isUpdatePrintNums"))) {//如果是修改打印次数
					sb.append(" and fp.unit_id=u.unit_id and fp.vehicle_id=v.vehicle_id and fp.customer_id=c.customer_id and fp.collection_id=bc.collection_id ");
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("subcoNo"))) {//分公司ID
					sb.append(" and fp.subco_no=").append(conditionMap.get("subcoNo"));
				}
				if(Utils.isNotNullOrEmpty(conditionMap.get("startDate")) && Utils.isNotNullOrEmpty(conditionMap.get("endDate"))){//付费开始、结束时间
					sb.append(" and fp.pay_time between '").append(conditionMap.get("startDate"));
					sb.append(" 00:00:00' and '").append(conditionMap.get("endDate")).append(" 23:59:59'");
				}
				if(Utils.isNotNullOrEmpty(conditionMap.get("printNum"))){//打印次数
					//第一次打印
					int printNum=Integer.valueOf(conditionMap.get("printNum").toString());
					if(printNum==0){
						sb.append(" and fp.print_num=0");
					}else{//补打
						sb.append(" and fp.print_num>0");
					}
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("paymentIds"))) {//缴费记录集合
					sb.append(" and fp.payment_id in(").append(conditionMap.get("paymentIds")).append(")");
				}
				/*if (Utils.isNotNullOrEmpty(conditionMap.get("unitIds"))) {//车台集合
					sb.append(" and fp.unit_id in(:unitIds)");
				}*/
				
				if (Utils.isNotNullOrEmpty(conditionMap.get("unitId"))) {//车台id
					sb.append(" and fp.unit_id=").append(conditionMap.get("unitId"));
				}else if (Utils.isNotNullOrEmpty(conditionMap.get("unitIds"))) {//车台集合
					sb.append(" and fp.unit_id in(").append(conditionMap.get("unitIds")).append(")");
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("vehicleId"))) {//车辆id
					sb.append(" and fp.vehicle_id=").append(conditionMap.get("vehicleId"));
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("customerId"))) {//客户ID
					sb.append(" and fp.customer_id=").append(conditionMap.get("customerId"));
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("unitNum"))) {//车台呼号
					//sb.append(" and u.call_letter like '%").append(StringUtils.replaceSqlKey(conditionMap.get("unitNum"))).append("%'");
					sb.append(" and u.call_letter='").append(StringUtils.replaceSqlKey(conditionMap.get("unitNum"))).append("'");
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("vehicleNum"))) {//车辆
					//sb.append(" and v.plate_no like '%").append(StringUtils.replaceSqlKey(conditionMap.get("vehicleNum"))).append("%'");
					sb.append(" and v.plate_no='").append(StringUtils.replaceSqlKey(conditionMap.get("vehicleNum"))).append("'");
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("customerName"))) {//客户
					//sb.append(" and c.customer_name like '%").append(StringUtils.replaceSqlKey(conditionMap.get("customerName"))).append("%'");
					sb.append(" and c.customer_name='").append(StringUtils.replaceSqlKey(conditionMap.get("customerName"))).append("'");
				}
				if (Utils.isNotNullOrEmpty(conditionMap.get("payCtNo"))) {//合同号
					//sb.append(" and bc.pay_ct_no like '%").append(StringUtils.replaceSqlKey(conditionMap.get("payCtNo"))).append("%'");
					sb.append(" and bc.pay_ct_no ='").append(StringUtils.replaceSqlKey(conditionMap.get("payCtNo"))).append("'");
				}
				if(Utils.isNotNullOrEmpty(conditionMap.get("printDate"))){//打印日期
					//sb.append(" and date_format(fp.print_time,'%Y-%m-%d')='").append(conditionMap.get("printDate")).append("'");
					sb.append(" and fp.print_time between '").append(conditionMap.get("printDate")).append("' and '").append(conditionMap.get("printDate")).append(" 23:59:59'") ;
				}
				
				
			}
			return sb;
	}

	 @Override
		public int updatePrintNums(Session session,String paymentIds,int printNum) throws SystemException {
			if(StringUtils.isNotBlank(paymentIds)){
				StringBuffer sb=new StringBuffer();
				/*sb.append(" update t_fee_payment p");
				if(printNum>0){
					sb.append(" set is_invoice=1,p.print_num=ifnull(p.print_num,0)+1,p.print_time=sysdate()");
				}else{
					sb.append(" set is_invoice=0,p.print_num=0,p.print_time=null");
				}
				
				sb.append(" where payment_id in (").append(paymentIds).append(")");
				Query query = mysql1SessionFactory.getCurrentSession().createSQLQuery(sb.toString());  
			    query.executeUpdate();
				
			    sb.delete(0, sb.length());*/
			    
				sb.append(" update t_fee_payment_dt p");
				if(printNum>0){
					sb.append(" set is_invoice=1,p.print_num=ifnull(p.print_num,0)+1,p.print_time=sysdate()");
				}else{
					sb.append(" set is_invoice=0,p.print_num=0,p.print_time=null");
				}
				
				sb.append(" where payment_id in (").append(paymentIds).append(")");
				if(session==null){
					session=mysql1SessionFactory.getCurrentSession();
				}
				Query query = session.createSQLQuery(sb.toString());  
				return query.executeUpdate();
			}else{
				return 0;
			}
		}

	@Override
	public int addPaymentDt(Session session,Long paytxtId, Long paymentId,String payCtNo)
			throws SystemException {
		if(Utils.isNotNullOrEmpty(paytxtId) && Utils.isNotNullOrEmpty(paymentId)){
			StringBuffer sb=new StringBuffer();
		    
			sb.append("insert into t_fee_payment_dt (payment_id,");
			sb.append("subco_no,customer_id,org_id,vehicle_id,unit_id,feetype_id,");
			sb.append("pay_model,collection_id,item_id,items_id,item_name,s_date,e_date,s_months,s_days,ac_amount,");
			sb.append("real_amount,bw_no,agent_id,agent_name,pay_time,stamp,op_id,remark,is_invoice,print_num");
			sb.append(") ");
			sb.append("select ").append(paymentId).append(",b.subco_no,b.customer_id ,0,b.vehicle_id,b.unit_id,b.feetype_id,0,b.collection_id,");
			sb.append("b.item_id,b.items_id,'',b.s_date,b.e_date,ceil(f_months_between(b.s_date,b.e_date)),0,");
			sb.append("b.amount,b.amount,concat(b.bill_id,''),b.op_id,'',sysdate(),sysdate(),b.op_id,'").append(payCtNo).append("',0,0");
			sb.append(" from t_fee_bill b where b.paytxt_id=").append(paytxtId);
			if(session==null){
				session=mysql1SessionFactory.getCurrentSession();
			}
			Query query = session.createSQLQuery(sb.toString());  
			return query.executeUpdate();
		}else{
			return 0;
		}
	}

}

