package com.chinagps.fee.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinagps.fee.comm.SystemConst;
import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.dao.PaymentDao;
import com.chinagps.fee.dao.ReprintinvoiceDao;
import com.chinagps.fee.entity.po.Collection;
import com.chinagps.fee.entity.po.Reprintinvoice;
import com.chinagps.fee.entity.po.Serviceitem;
import com.chinagps.fee.entity.vo.ItemVO;
import com.chinagps.fee.service.PaymentService;
import com.chinagps.fee.service.ReprintinvoiceService;
import com.chinagps.fee.util.MoneyUtil;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.StringUtils;
import com.chinagps.fee.util.Utils;
import com.chinagps.fee.util.page.Page;
import com.chinagps.fee.util.page.PageUtil;

/**
 * @Package:com.chinagps.fee.service.impl
 * @ClassName:ReprintinvoiceServiceImp
 * @Description:发票补打业务层实现类
 * @author:zfy
 * @date:2014-6-9 下午3:31:36
 */
@Service("reprintinvoiceService")
@Transactional(value="mysql1TxManager")
public class ReprintinvoiceServiceImp extends BaseServiceImpl implements
		ReprintinvoiceService {
	protected static final Logger LOGGER = LoggerFactory.getLogger(ReprintinvoiceServiceImp.class);
	
	@Autowired  
	@Qualifier("reprintinvoiceDao")
	private ReprintinvoiceDao reprintinvoiceDao;

	@Override
	public Page<Map<String, Object>> findReprintPage(
			PageSelect<Map<String, Object>> pageSelect) throws SystemException {
		List<Map<String, Object>> dataList=reprintinvoiceDao.findRePrints(pageSelect.getFilter(), pageSelect.getOrder(), pageSelect.getIs_desc(), pageSelect.getPageNo(), pageSelect.getPageSize());
		int total=reprintinvoiceDao.countRePrints(pageSelect.getFilter());
		return PageUtil.getPage(total, pageSelect.getPageNo(), dataList, pageSelect.getPageSize());
	}

	@Override
	public int updateReprint(Reprintinvoice reprintinvoice)
			throws SystemException {
		int result=1;
		if(reprintinvoice == null || reprintinvoice.getReprintId()==null){
			return -1;
		}
		//修改之前，判断存在不存在
		Reprintinvoice oldReprintinvoice=reprintinvoiceDao.get(Reprintinvoice.class, reprintinvoice.getReprintId());
		if(oldReprintinvoice!=null){
			reprintinvoice.setIsPrinted(oldReprintinvoice.getIsPrinted());
			if(reprintinvoice.getIsDel()==1){
				reprintinvoice.setDelTime(new Date());
			}
			//reprintinvoice.setPrintTime(oldReprintinvoice.getPrintTime());
			reprintinvoiceDao.merge(reprintinvoice);
		}else{
			result=0;
		}
		return result;
	}

	@Override
	public int updateReprint2(List<Long> reprintIds)
			throws SystemException {
		int result=1;
		if( Utils.isNullOrEmpty(reprintIds)){
			return -1;
		}
		reprintinvoiceDao.updateRePrints(reprintIds, 1,null);
		return result;
	}

	@Override
	public List<Map<String, Object>> findReprints(Map<String, Object> map)
			throws SystemException {
		return reprintinvoiceDao.findRePrints(map,null,false ,0,0);
	}

	@Override
	public int updateReprint2(String reprintIds) throws SystemException {
		String[] reprintIdArray= reprintIds.split(",");
		List<Long> reprintIdList=null;
		if(Utils.isNotNullOrEmpty(reprintIdArray)){
			reprintIdList=new ArrayList<Long>();
			for (String v : reprintIdArray) {
				reprintIdList.add(Long.valueOf(v));
			}
		}
		return reprintinvoiceDao.updateRePrints(reprintIdList, 1,null);
	}
	
}

