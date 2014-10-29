package com.chinagps.fee.dao.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.dao.CustomerDao;
import com.chinagps.fee.entity.po.Customer;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.StringUtils;
import com.chinagps.fee.util.Utils;
import com.chinagps.fee.util.page.Page;
import com.chinagps.fee.util.page.PageUtil;

@Repository("CustomerDao")  
public class CustomerDaoImpl extends BaseDaoImpl implements CustomerDao {

	@Override
	public Page<Customer> search(PageSelect<Customer> pageSelect, Long subco_no) {
		StringBuffer sb =new StringBuffer( "from Customer where 1=1 ");
		Map filter = pageSelect.getFilter();
		Set<String> keys = filter.keySet();
		if (Utils.isNotNullOrEmpty(subco_no)) {
			sb.append(" and subco_no=").append(subco_no);
		}
		for(Iterator it=keys.iterator();it.hasNext();){
			String key = (String)it.next();
			if(filter.get(key) instanceof Integer && Utils.isNotNullOrEmpty(filter.get(key))) {
				 sb.append(" and ").append(key).append(" = ").append((Integer) filter.get(key) );
			}else if (filter.get(key) instanceof String && Utils.isNotNullOrEmpty(filter.get(key))) {
				sb.append(" and ").append(key).append(" like '%").append(StringUtils.replaceSqlKey((String)filter.get(key))).append("%'");
			}
			
		}
		if (StringUtils.isNotBlank(pageSelect.getOrder())) {
			sb.append(" order by ").append(pageSelect.getOrder());
			if (pageSelect.getIs_desc()) {
				sb.append(" desc");
			} else {
				sb.append(" asc");
			}
		}else{
			sb.append(" order by stamp,customer_id desc");
		}
		//List list = listAll(hql, pageSelect.getPageNo(), pageSelect.getPageSize());
		Query query = mysql1SessionFactory.getCurrentSession().createQuery(sb.toString());  
		query.setFirstResult(PageUtil.getPageStart(pageSelect.getPageNo(), pageSelect.getPageSize()));
		query.setMaxResults(pageSelect.getPageSize());
		List list=query.list();
		
		String oldStr=sb.toString();
		sb.delete(0, sb.length());
		sb.append(" select count(*) ").append(oldStr);
		int count =((Long)mysql1SessionFactory.getCurrentSession().createQuery(sb.toString()).uniqueResult()).intValue();//countAll(sb);
		return PageUtil.getPage(count, pageSelect.getPageNo(), list, pageSelect.getPageSize());
	}
	
}
