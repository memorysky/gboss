package com.chinagps.fee.service;

import com.chinagps.fee.entity.po.Customer;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.page.Page;
/**
 * @Package:com.chinagps.fee.service
 * @ClassName:CustomerService
 * @Description:从门店copy过来，以后可能会改成静态数据（搜索引擎）
 * @author:zfy
 * @date:2014-6-11 上午10:51:09
 */
public interface CustomerService extends BaseService {
	
	public Page<Customer> search(PageSelect<Customer> pageSelect, Long subco_no);

}
