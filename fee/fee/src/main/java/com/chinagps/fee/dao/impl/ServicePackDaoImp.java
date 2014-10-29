package com.chinagps.fee.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.dao.ServicePackDao;
import com.chinagps.fee.entity.po.Packprice;
import com.chinagps.fee.entity.po.Servicepack;
import com.chinagps.fee.util.StringUtils;
import com.chinagps.fee.util.Utils;
import com.chinagps.fee.util.page.PageUtil;

/**
 * @Package:com.chinagps.fee.dao.impl
 * @ClassName:ServicePackDaoImp
 * @Description:服务套餐管理持久层实现类
 * @author:zfy
 * @date:2013-8-9 下午2:21:52
 */
@Repository("servicePackDao")  
public class ServicePackDaoImp extends BaseDaoImpl implements ServicePackDao {
	protected static final Logger logger = LoggerFactory.getLogger(ServiceItemDaoImp.class);

	@Override
	public boolean checkServicePackCode(Servicepack servicepack)
			throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" select count(s.id) from Servicepack as s");
		sb.append(" where 1=1 ");
		if(servicepack!=null){
			if(StringUtils.isNotBlank(servicepack.getName())){
				sb.append(" and s.name='").append(servicepack.getName()).append("'");
			}
			if(servicepack.getPackId()!=null){
				sb.append(" and s.id!=").append(servicepack.getPackId());
			}
		}
		Query query = mysql1SessionFactory.getCurrentSession().createQuery(sb.toString());  
		if((Long)query.uniqueResult()>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean checkServicePackName(Servicepack servicepack)
			throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" select count(s.id) from Servicepack as s");
		sb.append(" where 1=1 ");
		if(servicepack!=null){
			if(StringUtils.isNotBlank(servicepack.getPackCode())){
				sb.append(" and s.packCode='").append(servicepack.getPackCode()).append("'");
			}
			if(servicepack.getPackId()!=null){
				sb.append(" and s.id!=").append(servicepack.getPackId());
			}
		}
		Query query = mysql1SessionFactory.getCurrentSession().createQuery(sb.toString());  
		if((Long)query.uniqueResult()>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public int deletePackItemByPackId(Long packId) throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" delete PackItem ");
		sb.append(" where packId=").append(packId);
		Query query = mysql1SessionFactory.getCurrentSession().createQuery(sb.toString());  
		return query.executeUpdate();
	}

	@Override
	public int deletePackPriceByPackId(Long packId) throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" delete Packprice");
		sb.append(" where packId=").append(packId);
		Query query = mysql1SessionFactory.getCurrentSession().createQuery(sb.toString());  
		return query.executeUpdate();
	}

	@Override
	public List<Packprice> findPackPrices(List<Long> packIds)
			throws SystemException {
		Criteria criteria=mysql1SessionFactory.getCurrentSession().createCriteria(Packprice.class);
		if(packIds!=null){
			criteria.add(Restrictions.in("packId", packIds));
		}
		return criteria.list();
	}

	@Override
	public List<Servicepack> findServicePacks(Map conditionMap,String order,boolean isDesc,
			int pageNo, int pageSize) throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" from Servicepack as sp");
		sb=getConditionHql(sb,conditionMap);
		if (StringUtils.isNotBlank(order)) {
			sb.append(" order by ").append(order);
			if (isDesc) {
				sb.append(" desc");
			} else {
				sb.append(" asc");
			}
		}
		Query query = mysql1SessionFactory.getCurrentSession().createQuery(sb.toString());  
		query.setFirstResult(PageUtil.getPageStart(pageNo, pageSize));
		query.setMaxResults(pageSize);
		return query.list();
	}

	@Override
	public int countServicePacks(Map conditionMap)
			throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" select count(*) from Servicepack as sp");
		sb=getConditionHql(sb,conditionMap);
		Query query = mysql1SessionFactory.getCurrentSession().createQuery(sb.toString());  
		return ((Long)query.uniqueResult()).intValue();
	}

    private StringBuffer getConditionHql(StringBuffer packHqlSb,Map conditionMap){
		packHqlSb.append(" where 1=1");
		if(conditionMap!=null){
			if(conditionMap.get("code")!=null){
				packHqlSb.append(" and sp.packCode like '%").append(StringUtils.replaceSqlKey(conditionMap.get("code"))).append("%'");
			}
			if(conditionMap.get("name")!=null){
				packHqlSb.append(" and sp.name like '%").append(StringUtils.replaceSqlKey(conditionMap.get("name"))).append("%'");
			}
			/*if(StringUtils.isNotNullOrEmpty(conditionMap.get("orgId"))){
				packHqlSb.append(" and sp.subcoNo=").append(conditionMap.get("orgId"));
			}*/
			if(Utils.isNotNullOrEmpty(conditionMap.get("companyId"))){
				packHqlSb.append(" and sp.subcoNo=").append(conditionMap.get("companyId"));
			}
			if(conditionMap.get("isVerified")!=null){
				packHqlSb.append(" and sp.isChecked=").append(conditionMap.get("isVerified"));
			}
		}
		return packHqlSb;
    }

	@Override
	public int updateIsVerifiedByIds(List<Long> ids, Long verifyUserId,
			String verifiedTime) throws SystemException {
		StringBuffer packHqlSb=new StringBuffer();
		packHqlSb.append(" update Servicepack ");
		packHqlSb.append(" set isChecked=1 ");
		if(verifyUserId!=null){
			packHqlSb.append(",chkOpId=").append(verifyUserId);
		}
		if(StringUtils.isNotBlank(verifiedTime)){
			packHqlSb.append(",chkTime='").append(verifiedTime).append("'");
		}
		if(ids!=null&&!ids.isEmpty()){
			packHqlSb.append(" where id in (:ids)");
		}
		Query query = mysql1SessionFactory.getCurrentSession().createQuery(packHqlSb.toString());  
		if(ids!=null&&!ids.isEmpty()){
			query.setParameterList("ids", ids); 
		}
		return query.executeUpdate();
	}

	@Override
	public int updateIsValidByIds(List<Long> ids,Integer isvalid, Long userId, String stamp)
			throws SystemException {
		StringBuffer packHqlSb=new StringBuffer();
		packHqlSb.append(" update Servicepack ");
		packHqlSb.append(" set isChecked=").append(isvalid);
		if(userId!=null){
			packHqlSb.append(",opId=").append(userId);
		}
		if(StringUtils.isNotBlank(stamp)){
			packHqlSb.append(",stamp='").append(stamp).append("'");
		}
		if(ids!=null&&!ids.isEmpty()){
			packHqlSb.append(" where id in (:ids)");
		}
		Query query = mysql1SessionFactory.getCurrentSession().createQuery(packHqlSb.toString());  
		if(ids!=null&&!ids.isEmpty()){
			query.setParameterList("ids", ids); 
		}
		return query.executeUpdate();
	}

	@Override
	public List<Map<Long, Object>> findPackItems(List<Long> packIds)
			throws SystemException {
		StringBuffer hqlSb=new StringBuffer();
		hqlSb.append("select new map(pi.itemId as itemId,si.itemName as itemName,pi.remark as remark,pi.weights as weights) ");
		hqlSb.append(" from PackItem as pi,Serviceitem as si");
		hqlSb.append(" where pi.itemId=si.id");
		if(packIds!=null&&!packIds.isEmpty()){
			hqlSb.append(" and pi.packId in (:ids)");
		}
		Query query = mysql1SessionFactory.getCurrentSession().createQuery(hqlSb.toString());  
		if(packIds!=null&&!packIds.isEmpty()){
			query.setParameterList("ids", packIds); 
		}
		return query.list();
	}

	@Override
	public List<Servicepack> findServicePacks(Map conditionMap) throws SystemException {
		StringBuffer packHqlSb=new StringBuffer();
		packHqlSb.append(" from Servicepack as sp");
		packHqlSb=getConditionHql(packHqlSb,conditionMap);
		Query query = mysql1SessionFactory.getCurrentSession().createQuery(packHqlSb.toString());  
		return query.list();
	}

}

