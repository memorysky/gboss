package com.chinagps.fee.service.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.dao.CustomerDao;
import com.chinagps.fee.entity.po.Customer;
import com.chinagps.fee.service.CustomerService;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.page.Page;

@Service("CustomerService")
@Transactional(value="mysql1TxManager")
public class CustomerServiceImpl extends BaseServiceImpl implements
		CustomerService {

	@Autowired
	@Qualifier("CustomerDao")
	private CustomerDao customerDao;


	@Override
	public Page<Customer> search(PageSelect<Customer> pageSelect, Long subco_no) {
		return customerDao.search(pageSelect, subco_no);
	}


}
