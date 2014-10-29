package com.fee.service.impl;

import java.util.List;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.chinagps.fee.entity.po.Serviceitem;
import com.chinagps.fee.entity.po.sys.Operatelog;
import com.chinagps.fee.service.OperatelogService;
import com.chinagps.fee.service.ServiceItemService;
import com.chinagps.fee.util.SpringContext;

/**
 * @Package:com.fee.service.impl
 * @ClassName:ServiceItemServiceTest
 * @Description:服务项业务层测试类
 * @author:zfy
 * @date:2013-8-9 下午3:37:48
 */
@RunWith(SpringJUnit4ClassRunner.class)//指定测试用例的运行器 这里是指定了Junit4 
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional //对所有的测试方法都使用事务，并在测试完成后回滚事务 
@TransactionConfiguration(transactionManager = "mysql1TxManager", defaultRollback = true)
public class ServiceItemServiceTest  {
	@Autowired
	@Qualifier("serviceItemService")
	private ServiceItemService serviceItemService; 
	
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
    public void testFindServiceitem() {
		try {
			Serviceitem serviceitem=new Serviceitem();
			//serviceitem.setName("车圣宝典");
			List<Serviceitem> result=serviceItemService.findServiceitem(serviceitem);
			jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(System.out, JsonEncoding.UTF8);
			System.out.println("result print begin:");
			jsonGenerator.writeObject(result);
			System.out.println("result print end");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @Test
    public void testAddServiceitem() {
		try {
			Serviceitem serviceitem=new Serviceitem();
			serviceitem.setItemCode("wsch");
			serviceitem.setFeetypeId(1);
			serviceitem.setItemName("网上查车");
			serviceitem.setFlag(1);
			serviceitem.setOpId(1l);
			serviceitem.setPrice(13f);
			serviceitem.setRemark("1");
			int result=serviceItemService.addServiceitem(serviceitem);
			jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(System.out, JsonEncoding.UTF8);
			System.out.println("result print begin:");
			jsonGenerator.writeObject(result);
			System.out.println("result print end");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @Test
    public void testUpdateServiceitem() {
		try {
			Serviceitem serviceitem=new Serviceitem();
			serviceitem.setItemCode("wsch");
			serviceitem.setItemName("网上查车");
			int result=serviceItemService.updateServiceitem(serviceitem);
			jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(System.out, JsonEncoding.UTF8);
			System.out.println("result print begin:");
			jsonGenerator.writeObject(result);
			System.out.println("result print end");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @Test
    public void testDeleteServiceitem() {
		try {
			Serviceitem serviceitem=new Serviceitem();
			int result=serviceItemService.deleteServiceitem(serviceitem);
			jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(System.out, JsonEncoding.UTF8);
			System.out.println("result print begin:");
			jsonGenerator.writeObject(result);
			System.out.println("result print end");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
}

