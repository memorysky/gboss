package com.chinagps.fee.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.dao.VehicleDao;
import com.chinagps.fee.entity.po.Vehicle;
import com.chinagps.fee.service.VehicleService;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.page.Page;
import com.chinagps.fee.util.page.PageUtil;

/**
 * @Package:com.gboss.service.impl
 * @ClassName:VehicleServiceImpl
 * @Description:TODO
 * @author:xiaoke
 * @date:2014-3-24 下午3:22:09
 */

@Service("vehicleService")
@Transactional(value="mysql1TxManager")
public class VehicleServiceImpl extends BaseServiceImpl implements VehicleService {

	@Autowired
	@Qualifier("VehicleDao")
	private VehicleDao vehicleDao;
	

	@Override
	public Page<Vehicle> search(PageSelect<Vehicle> pageSelect, Long subco_no) {
		return vehicleDao.search(pageSelect, subco_no);
	}
}

