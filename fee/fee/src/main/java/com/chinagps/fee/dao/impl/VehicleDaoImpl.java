package com.chinagps.fee.dao.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.chinagps.fee.dao.VehicleDao;
import com.chinagps.fee.entity.po.Vehicle;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.StringUtils;
import com.chinagps.fee.util.Utils;
import com.chinagps.fee.util.page.Page;
import com.chinagps.fee.util.page.PageUtil;

/**
 * @Package:com.gboss.dao.impl
 * @ClassName:VehicleDaoImpl
 * @Description:TODO
 * @author:xiaoke
 * @date:2014-3-24 下午3:01:56
 */

@Repository("VehicleDao")
public class VehicleDaoImpl extends BaseDaoImpl implements VehicleDao {


	@Override
	public Page<Vehicle> search(PageSelect<Vehicle> pageSelect, Long subco_no) {
		StringBuffer sb =new StringBuffer( "from Vehicle where 1=1 ");
		Map filter = pageSelect.getFilter();
		Set<String> keys = filter.keySet();
		if (Utils.isNotNullOrEmpty(subco_no)) {
			sb.append(" and subco_no=").append(subco_no);
		}
		for(Iterator it=keys.iterator();it.hasNext();){
			String key = (String)it.next();
			if("customer_id".equals(key)){
				 sb.append(" and vehicle_id in (select vehicle_id from CustVehicle where customer_id=");
				 sb.append( filter.get(key)).append(")");
			}else{
				if(filter.get(key) instanceof Integer && Utils.isNotNullOrEmpty(filter.get(key))) {
					 sb.append(" and ").append(key).append(" = ").append((Integer) filter.get(key) );
				}else if (filter.get(key) instanceof String && Utils.isNotNullOrEmpty(filter.get(key))) {
					sb.append(" and ").append(key).append(" like '%").append(StringUtils.replaceSqlKey((String)filter.get(key))).append("%'");
				}
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
			sb.append(" order by stamp,vehicle_id desc");
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
