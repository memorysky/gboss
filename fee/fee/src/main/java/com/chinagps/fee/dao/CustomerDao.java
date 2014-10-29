package com.chinagps.fee.dao;

import com.chinagps.fee.entity.po.Customer;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.page.Page;
/**
 * @Package:com.chinagps.fee.dao
 * @ClassName:CustomerDao
 * @Description:从门店copy过来，以后可能会改成静态数据（搜索引擎）
 * @author:zfy
 * @date:2014-6-11 上午10:45:37
 */
public interface CustomerDao extends BaseDao {
	
	public Page<Customer> search(PageSelect<Customer> pageSelect, Long subco_no);
}
