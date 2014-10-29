package com.chinagps.fee.service.impl;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.dao.UserRemarkDao;
import com.chinagps.fee.entity.po.UserRemark;
import com.chinagps.fee.service.UserRemarkService;
import com.chinagps.fee.util.DateUtil;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.page.Page;
import com.chinagps.fee.util.page.PageUtil;

/**
 * @Package:com.gboss.service.impl
 * @ClassName:UserRemarkServiceImpl
 * @Description:备忘录业务层实现类
 * @author:zfy
 * @date:2013-11-18 上午10:21:48
 */
@Service("userRemarkService")
@Transactional(value="mysql1TxManager")
public class UserRemarkServiceImpl extends BaseServiceImpl implements
		UserRemarkService {
	protected static final Logger LOGGER = LoggerFactory.getLogger(UserRemarkServiceImpl.class);
	
	@Autowired  
	@Qualifier("userRemarkDao")
	private UserRemarkDao userRemarkDao;
	
	@Override
	public Page<HashMap<String, Object>> findUserRemarks(
			PageSelect<HashMap<String, Object>> pageSelect)
			throws SystemException {
		if(pageSelect==null){
			return null;
		}else{
	        int total=userRemarkDao.countUserRemarks(pageSelect.getFilter());
	        List<HashMap<String, Object>> list=userRemarkDao.findUserRemark(pageSelect.getFilter(), pageSelect.getOrder(), pageSelect.getIs_desc(), pageSelect.getPageNo(),pageSelect.getPageSize());
	        return PageUtil.getPage(total, pageSelect.getPageNo(), list, pageSelect.getPageSize());
		}
	}

	@Override
	public int addUserRemark(UserRemark userRemark) throws SystemException {
		int result=1;
		if(userRemark==null){
			result=-1;
		}else{
			userRemark.setStamp(DateUtil.formatToday());
			userRemarkDao.save(userRemark);
			//如果是设为登陆后提示，则把其他的设为不提示
			if(userRemark.getIsAlert()==1){
				userRemarkDao.updateRemarkIsAlert(userRemark.getId(), userRemark.getUserId());
			}
		}
		return result;
	}

	@Override
	public int updateUserRemark(UserRemark userRemark) throws SystemException {
		int result=1;
		if(userRemark==null || userRemark.getId()==null){
			result=-1;
		}else{
			//判断存不存在
			if(userRemarkDao.get(UserRemark.class, userRemark.getId())!=null){
				userRemark.setStamp(DateUtil.formatToday());
				userRemarkDao.merge(userRemark);
				//如果是设为登陆后提示，则把其他的设为不提示
				if(userRemark.getIsAlert()==1){
					userRemarkDao.updateRemarkIsAlert(userRemark.getId(), userRemark.getUserId());
				}
			}else{
				result=0;
			}
			
		}
		return result;
	}

	@Override
	public int deleteUserRemarks(List<Long> idList) throws SystemException {
		int result=1;
		if(idList==null || idList.isEmpty()){
			result=-1;
		}else{
			userRemarkDao.deleteUserRemark(idList);
		}
		return result;
	}

	@Override
	public UserRemark getUserRemarkById(Long id) throws SystemException {
		if(id==null){
			return null;
		}else{
			return get(UserRemark.class, id);
		}
	}
}

