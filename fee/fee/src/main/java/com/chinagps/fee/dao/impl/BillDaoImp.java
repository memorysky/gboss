package com.chinagps.fee.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.dao.BillDao;
import com.chinagps.fee.entity.po.Bill;
import com.chinagps.fee.util.Utils;

/**
 * @Package:com.chinagps.fee.dao.impl
 * @ClassName:BillDaoImp
 * @Description:账单持久层实现类
 * @author:zfy
 * @date:2014-5-20 下午7:05:29
 */
@Repository("billDaoImp")  
public class BillDaoImp extends BaseDaoImpl implements BillDao {
	protected static final Logger logger = LoggerFactory.getLogger(BillDaoImp.class);

	@Override
	public List<Bill> findBills(Map<String, Object> conditionMap)
			throws SystemException {
		StringBuffer sb=new StringBuffer();
		sb.append("from Bill as p");
		sb.append(" where 1=1");
		if(Utils.isNotNullOrEmpty(conditionMap.get("paytxtId"))){
			sb.append(" and p.paytxtId=").append(conditionMap.get("paytxtId"));
		}
		Query query = mysql1SessionFactory.getCurrentSession().createQuery(sb.toString()); 
		return query.list();
	}
   
}

