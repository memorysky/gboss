package com.chinagps.fee.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.dao.ServicePackDao;
import com.chinagps.fee.entity.po.PackItem;
import com.chinagps.fee.entity.po.Packprice;
import com.chinagps.fee.entity.po.Servicepack;
import com.chinagps.fee.service.ServicePackService;
import com.chinagps.fee.util.DateUtil;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.Utils;
import com.chinagps.fee.util.page.Page;
import com.chinagps.fee.util.page.PageUtil;

/**
 * @Package:com.chinagps.fee.service.impl
 * @ClassName:ServicePackServiceImp
 * @Description:服务套餐管理业务层实现类
 * @author:zfy
 * @date:2013-8-9 下午2:30:53
 */
@Service("servicePackService")
@Transactional(value="mysql1TxManager")
public class ServicePackServiceImp extends BaseServiceImpl implements
		ServicePackService {
	protected static final Logger LOGGER = LoggerFactory.getLogger(ServicePackServiceImp.class);
	
	@Autowired  
	@Qualifier("servicePackDao")
	private ServicePackDao servicePackDao;

	@Override
	public int addServicePackItemPrice(Servicepack servicepack)
			throws SystemException {
		if(servicepack==null){
			return -1;
		}
		int result=1;
		//判断套餐编号是否存在
		if(servicePackDao.checkServicePackCode(servicepack)){
			result=2;
		}else{
			if(servicePackDao.checkServicePackName(servicepack)){
				result=3;
			}else{
				List<PackItem> packItems=servicepack.getPackItems();
				List<Packprice> packprices=servicepack.getPackprices();
				
				servicepack.setIsChecked(0);//默认未审核
				//添加套餐信息
				servicePackDao.save(servicepack);
				Long packId=servicepack.getPackId();
				//添加套餐服务项关联信息
				if(packItems!=null){
					for (PackItem packiItem : packItems) {
						packiItem.setPackId(packId);
						servicePackDao.save(packiItem);
					}
				}
				Long subcoNo=servicepack.getSubcoNo();
				//添加套餐价格信息
				if(packprices!=null){
					for (Packprice packprice : packprices) {
						packprice.setPackId(packId);
						packprice.setSubcoNo(subcoNo);
						servicePackDao.save(packprice);
					}
				}
			}
		}
		return result;
	}

	@Override
	public int updateServicePackItemPrice(Servicepack servicepack)
			throws SystemException {
		if(servicepack==null || servicepack.getPackId()==null){
			return -1;
		}
		int result=1;
		Long packId=servicepack.getPackId();
		//修改之前，判断存在不存在
		if(servicePackDao.get(Servicepack.class, packId)!=null){
			//为了避免出现错误a different object with the same identifier value was already associated
			//所以用merge
			//判断套餐编号是否存在
			if(servicePackDao.checkServicePackCode(servicepack)){
				result=2;
			}else{
				if(servicePackDao.checkServicePackName(servicepack)){
					result=3;
				}else{
					List<PackItem> packItems=servicepack.getPackItems();
					List<Packprice> packprices=servicepack.getPackprices();
				
					//修改套餐信息
					servicePackDao.merge(servicepack);
					//添加套餐服务项关联信息,先删除原有的关系
					servicePackDao.deletePackItemByPackId(packId);
					if(packItems!=null){
						for (PackItem packiItem : packItems) {
							packiItem.setPackId(packId);
							servicePackDao.save(packiItem);
						}
					}
					//添加套餐价格信息,先删除原有的价格信息
					servicePackDao.deletePackPriceByPackId(packId);
					if(packprices!=null){
						for (Packprice packprice : packprices) {
							packprice.setPackId(packId);
							servicePackDao.save(packprice);
						}
					}
				}
					
			}
		}else{
			result=0;
		}
		return result;
	}

	@Override
	public int deleteServicePackItemPrice(Long packId) throws SystemException {
		if(packId==null){
			return -1;
		}
		//删之前，判断存在不存在
		if(servicePackDao.get(Servicepack.class, packId)!=null){
			//先删除与服务项的关系表
			servicePackDao.deletePackItemByPackId(packId);
		    //再删除价格表
			servicePackDao.deletePackPriceByPackId(packId);
			//最后删除套餐表
			servicePackDao.delete(Servicepack.class, packId);
			return 1;
		}else{
			return 0;
		}
	}

	@Override
	public Page<Map<Long, Object>> findServicePackItemPrice(
			PageSelect<Servicepack> pageSelect) throws SystemException {
		//先查套餐
		int total=servicePackDao.countServicePacks(pageSelect.getFilter());
		List<Servicepack> servicePacks=servicePackDao.findServicePacks(pageSelect.getFilter(), pageSelect.getOrder(), pageSelect.getIs_desc(),pageSelect.getPageNo(),pageSelect.getPageSize());
		//得到packId集合,用于查询相关的用户类型价格、服务项
		List<Long> packIds=new ArrayList<Long>();
		List<Packprice> packprices=null;
		Map<String, Object> mapResult=null;
		List<Map<Long, Object>> mapResultList=new ArrayList<Map<Long,Object>>();
		Map<Long, Object> map4Price=new HashMap<Long, Object>();
		if(servicePacks!=null){
			for (Servicepack servicepack : servicePacks) {
				packIds.add(servicepack.getPackId());
				
				mapResult=new HashMap<String, Object>();
				mapResult=Utils.transBean2Map(servicepack);
				map4Price.put(servicepack.getPackId(), mapResult);
			}
			
			//再查询套餐与用户类型价格的关系
			if(packIds!=null&&!packIds.isEmpty()){
				packprices=servicePackDao.findPackPrices(packIds);
				if(packprices!=null){
					for (Packprice packprice : packprices) {
						mapResult=(Map<String, Object>) map4Price.get(packprice.getPackId());
						if(mapResult!=null){
							if(packprice.getCustType().intValue()==1){//普通卡
								mapResult.put("type0MonthPrice", packprice.getMonthPrice());
								mapResult.put("type0YearPrice", packprice.getYearPrice());
							
							}else if(packprice.getCustType().intValue()==2){//银卡
								mapResult.put("type1MonthPrice", packprice.getMonthPrice());
								mapResult.put("type1YearPrice", packprice.getYearPrice());
							
							}else if(packprice.getCustType().intValue()==3){//金卡
								mapResult.put("type2MonthPrice", packprice.getMonthPrice());
								mapResult.put("type2YearPrice", packprice.getYearPrice());
							
							}else if(packprice.getCustType().intValue()==4){//白金卡
								mapResult.put("type3MonthPrice", packprice.getMonthPrice());
								mapResult.put("type3YearPrice", packprice.getYearPrice());
							
							}
						}
						
					}
				}
			}
		    Set<Entry<Long, Object>> set=map4Price.entrySet();
		    for(Entry<Long, Object> entry:set){
		    	mapResultList.add((Map<Long, Object>)entry.getValue());
		    }
		}
		return PageUtil.getPage(total, pageSelect.getPageNo(), mapResultList, pageSelect.getPageSize());
	}

	@Override
	public int updateIsValidByIds(List<Long> ids, Integer isvalid,
			Long userId) throws SystemException {
		return servicePackDao.updateIsValidByIds(ids, isvalid, userId, DateUtil.formatNowTime());
	}

	@Override
	public int updateIsVerifiedByIds(List<Long> ids, Long verifyUserId)
			throws SystemException {
		return servicePackDao.updateIsVerifiedByIds(ids, verifyUserId, DateUtil.formatNowTime());
	}

	@Override
	public HashMap<String, Object> getServicePackItemsPrices(Long id)
			throws SystemException {
		Servicepack servicepack=servicePackDao.get(Servicepack.class, id);
		List<Map<Long, Object>> packiItems=null;
		List<Packprice> packprices=null;
		if(servicepack!=null){
			List<Long> packIds=new ArrayList<Long>();
			packIds.add(id);
			packprices=servicePackDao.findPackPrices(packIds);
			packiItems=servicePackDao.findPackItems(packIds);
		}
		HashMap<String, Object> result=new HashMap<String, Object>();
		result.put("pack", servicepack);//套餐
		result.put("packItems", packiItems);//套餐与服务项的关系
		result.put("packPrices", packprices);//套餐与不同用户类型的价格关系
		return result;
	}

	@Override
	public List<Map<Long, Object>> findServicePackItemPrice(Map conditionMap)
			throws SystemException {
		List<Servicepack> servicePacks = servicePackDao.findServicePacks(conditionMap);
		//得到packId集合,用于查询相关的用户类型价格、服务项
		List<Long> packIds=new ArrayList<Long>();
		List<Packprice> packprices=null;
		Map<String, Object> mapResult=null;
		List<Map<Long, Object>> mapResultList=new ArrayList<Map<Long,Object>>();
		Map<Long, Object> map4Price=new HashMap<Long, Object>();
		if(servicePacks!=null){
			for (Servicepack servicepack : servicePacks) {
				packIds.add(servicepack.getPackId());
				
				mapResult=new HashMap<String, Object>();
				mapResult=Utils.transBean2Map(servicepack);
				map4Price.put(servicepack.getPackId(), mapResult);
			}
			
			//再查询套餐与用户类型价格的关系
			if(packIds!=null&&!packIds.isEmpty()){
				packprices=servicePackDao.findPackPrices(packIds);
				if(packprices!=null){
					for (Packprice packprice : packprices) {
						mapResult=(Map<String, Object>) map4Price.get(packprice.getPackId());
						if(mapResult!=null){
							if(packprice.getCustType().intValue()==1){//普通卡
								mapResult.put("type0MonthPrice", packprice.getMonthPrice());
								mapResult.put("type0YearPrice", packprice.getYearPrice());
							
							}else if(packprice.getCustType().intValue()==2){//银卡
								mapResult.put("type1MonthPrice", packprice.getMonthPrice());
								mapResult.put("type1YearPrice", packprice.getYearPrice());
							
							}else if(packprice.getCustType().intValue()==3){//金卡
								mapResult.put("type2MonthPrice", packprice.getMonthPrice());
								mapResult.put("type2YearPrice", packprice.getYearPrice());
							
							}else if(packprice.getCustType().intValue()==4){//白金卡
								mapResult.put("type3MonthPrice", packprice.getMonthPrice());
								mapResult.put("type3YearPrice", packprice.getYearPrice());
							
							}
						}
						
					}
				}
			}
		    Set<Entry<Long, Object>> set=map4Price.entrySet();
		    for(Entry<Long, Object> entry:set){
		    	mapResultList.add((Map<Long, Object>)entry.getValue());
		    }
		}
	    return mapResultList;
	}

}

