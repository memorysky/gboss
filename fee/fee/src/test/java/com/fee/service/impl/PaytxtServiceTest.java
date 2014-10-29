package com.fee.service.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.chinagps.fee.entity.po.Paytxt;
import com.chinagps.fee.entity.po.Serviceitem;
import com.chinagps.fee.service.PaytxtService;
import com.chinagps.fee.service.ServiceItemService;
import com.chinagps.fee.service.impl.BillServiceJob;

/**
 * @Package:com.fee.service.impl
 * @ClassName:BillServiceJobTest
 * @Description:账单生成业务层测试类
 * @author:zfy
 * @date:2014-5-21 下午12:00:03
 */
@RunWith(SpringJUnit4ClassRunner.class)//指定测试用例的运行器 这里是指定了Junit4 
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional //对所有的测试方法都使用事务，并在测试完成后回滚事务 
@TransactionConfiguration(transactionManager = "mysql1TxManager", defaultRollback = true)
public class PaytxtServiceTest  {
	@Autowired
	@Qualifier("paytxtService")
	//@Resource(name="paytxtService")
	private PaytxtService paytxtService; 
	
	ObjectMapper  objectMapper = new ObjectMapper();
	JsonGenerator jsonGenerator = null;
	
	@Before //在每个测试用例方法之前都会执行  
    public void init(){  
		//System.out.println("测试begin！");
    }
	
	@After //在每个测试用例执行完之后执行  
    public void destory(){  
		//System.out.println("测试end！");
    } 
	
	@Test
    public void testAddPaytxts() {
		try {
			Date date=new Date(new java.util.Date().getTime());
			long result=paytxtService.addPaytxts(1l, 1, "E0700021", 1l, date );
			System.out.println("result print end;"+result);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	@Test
    public void testFindZjPaytxts() {
		try {
			Map<String, Object> map=new HashMap<String, Object>();
			 Map<String,Object> paytxt=paytxtService.getZjPaytxts(map);
			System.out.println("result print end;"+paytxt);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	@Test
    public void testGetMaxFileTimesOfToday() {
		try {
			Map<String, Object> map=new HashMap<String, Object>();
			 String result=paytxtService.getMaxFileTimesOfToday(map);
			System.out.println("result print end;"+result);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}

