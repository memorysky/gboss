package com.fee.service.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
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

import com.chinagps.fee.entity.po.Collection;
import com.chinagps.fee.entity.po.Paytxt;
import com.chinagps.fee.entity.po.Reprintinvoice;
import com.chinagps.fee.entity.po.Serviceitem;
import com.chinagps.fee.service.PaymentService;
import com.chinagps.fee.service.PaytxtService;
import com.chinagps.fee.service.ReprintinvoiceService;
import com.chinagps.fee.service.ServiceItemService;
import com.chinagps.fee.service.impl.BillServiceJob;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.page.Page;

/**
 * @Package:com.fee.service.impl
 * @ClassName:ReprintinvoiceTest
 * @Description:发票补打业务层测试类
 * @author:zfy
 * @date:2014-6-9 下午4:13:12
 */
@RunWith(SpringJUnit4ClassRunner.class)//指定测试用例的运行器 这里是指定了Junit4 
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional //对所有的测试方法都使用事务，并在测试完成后回滚事务 
@TransactionConfiguration(transactionManager = "mysql1TxManager", defaultRollback = true)
public class ReprintinvoiceTest  {
	@Autowired
	@Qualifier("reprintinvoiceService")
	private ReprintinvoiceService reprintinvoiceService;

	
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
    public void testFindLPayPrint4PrintSetPage() {
		try {
			PageSelect<Map<String,Object>> pageSelect=new PageSelect<Map<String,Object>>();
			Page<Map<String,Object>> result=reprintinvoiceService.findReprintPage(pageSelect);
			jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(
					System.out, JsonEncoding.UTF8);
			System.out.println("result print begin:");
			jsonGenerator.writeObject(result);
			System.out.println();
			System.out.println("result print end");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	@Test
    public void testUpdateReprint() {
		try {
			Reprintinvoice reprintinvoice=new Reprintinvoice();
			reprintinvoice.setReprintId(1l);
			reprintinvoice.setSubcoNo(5l);
			reprintinvoice.setIsDel(1);
			int result=reprintinvoiceService.updateReprint(reprintinvoice);
			jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(
					System.out, JsonEncoding.UTF8);
			System.out.println("result print begin:");
			jsonGenerator.writeObject(result);
			System.out.println();
			System.out.println("result print end");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}

