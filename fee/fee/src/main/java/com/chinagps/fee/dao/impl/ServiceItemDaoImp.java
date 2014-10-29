package com.chinagps.fee.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.dao.ServiceItemDao;
import com.chinagps.fee.entity.po.Serviceitem;
import com.chinagps.fee.util.StringUtils;

/**
 * @Package:com.chinagps.fee.dao.impl
 * @ClassName:ServiceItemDaoImp
 * @Description:服务项管理持久层实现类
 * @author:zfy
 * @date:2013-8-9 下午2:21:52
 */
@Repository("serviceItemDao")  
public class ServiceItemDaoImp extends BaseDaoImpl implements ServiceItemDao {
	protected static final Logger logger = LoggerFactory.getLogger(ServiceItemDaoImp.class);

	@Override
	public List<Serviceitem> findServiceitem(Serviceitem serviceitem) throws SystemException{
		Criteria criteria = mysql1SessionFactory.getCurrentSession().createCriteria(Serviceitem.class); 
		if(serviceitem!=null){
			if(StringUtils.isNotBlank(serviceitem.getItemCode())){
				criteria.add(Restrictions.like("itemCode", serviceitem.getItemCode(),MatchMode.ANYWHERE));
			}
			if(StringUtils.isNotBlank(serviceitem.getItemName())){
				criteria.add(Restrictions.like("itemName", serviceitem.getItemName(),MatchMode.ANYWHERE));
			}
		}
		return criteria.list();
	}

	@Override
	public boolean checkSeviceItemCode(Serviceitem serviceitem)
			throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" select count(s.id) from Serviceitem as s");
		sb.append(" where 1=1 ");
		if(serviceitem!=null){
			if(StringUtils.isNotBlank(serviceitem.getItemCode())){
				sb.append(" and s.itemCode='").append(serviceitem.getItemCode()).append("'");
			}
			if(serviceitem.getItemId()!=null){
				sb.append(" and s.id!=").append(serviceitem.getItemId());
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
	public boolean checkSeviceItemName(Serviceitem serviceitem)
			throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" select count(s.id) from Serviceitem as s");
		sb.append(" where 1=1 ");
		if(serviceitem!=null){
			if(StringUtils.isNotBlank(serviceitem.getItemName())){
				sb.append(" and s.itemName='").append(serviceitem.getItemName()).append("'");
			}
			if(serviceitem.getItemId()!=null){
				sb.append(" and s.id!=").append(serviceitem.getItemId());
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
	public boolean checkItemIsUsing(Long serviceItemId)
			throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append(" select count(1) from t_fee_info as p");
		if(serviceItemId!=null){
			sb.append(" where concat(',',p.items_id) like '%,").append(serviceItemId).append(",%'");
		}
		Query query = mysql1SessionFactory.getCurrentSession().createSQLQuery(sb.toString());  
		if(((BigInteger)query.uniqueResult()).intValue()>0){
			return true;
		}else{
			return false;
		}
	}

}

