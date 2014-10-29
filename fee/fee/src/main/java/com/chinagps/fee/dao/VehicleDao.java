package com.chinagps.fee.dao;

import com.chinagps.fee.entity.po.Vehicle;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.page.Page;

/**
 * @Package:com.gboss.dao
 * @ClassName:VehicleDao
 * @Description:TODO 从门店copy过来，以后可能会改成静态数据（搜索引擎）
 * @author:xiaoke
 * @date:2014-3-24 下午2:56:45
 */
public interface VehicleDao extends BaseDao {
	
	
	public Page<Vehicle> search(PageSelect<Vehicle> pageSelect, Long subco_no);
	
}

