package com.chinagps.fee.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.entity.po.Datalock;
import com.chinagps.fee.entity.po.Paytxt;
import com.chinagps.fee.entity.po.Reprintinvoice;
import com.chinagps.fee.entity.po.Serviceitem;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.page.Page;


/**
 * @Package:com.chinagps.fee.service
 * @ClassName:ReprintinvoiceService
 * @Description:发票补打业务层接口
 * @author:zfy
 * @date:2014-6-9 下午3:30:31
 */
public interface ReprintinvoiceService extends BaseService{
	
	/**
	 * @Title:findReprintPage
	 * @Description:分页查询补打设置信息
	 * @param pageSelect
	 * @return
	 * @throws SystemException
	 */
	public Page<Map<String, Object>> findReprintPage(PageSelect<Map<String, Object>> pageSelect) throws SystemException;
	/**
	 * @Title:findReprints
	 * @Description:查询所有补打设置信息
	 * @param map
	 * @return
	 * @throws SystemException
	 * @throws
	 */
	public List<Map<String, Object>> findReprints(Map<String, Object> map) throws SystemException;

	/**
	 * @Title:updateReprint
	 * @Description:修改补打发票的信息
	 * @param reprintinvoice
	 * @return
	 * @throws SystemException
	 */
	public int updateReprint(Reprintinvoice reprintinvoice) throws SystemException;
	/**
	 * @Title:updateReprint2
	 * @Description:删除补打发票的信息
	 * @param reprintIds
	 * @return
	 * @throws SystemException
	 */
	public int updateReprint2(List<Long> reprintIds) throws SystemException;
	
	 /** @Title:updateReprint2
	 * @Description:删除补打发票的信息
	 * @param reprintIds
	 * @return
	 * @throws SystemException
	 */
	public int updateReprint2(String reprintIds) throws SystemException;

}

