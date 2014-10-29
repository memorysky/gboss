package com.fee.service.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import com.chinagps.fee.entity.po.Payment;
import com.chinagps.fee.entity.po.Paytxt;
import com.chinagps.fee.entity.po.Serviceitem;
import com.chinagps.fee.service.PaymentService;
import com.chinagps.fee.service.PaytxtService;
import com.chinagps.fee.service.ServiceItemService;
import com.chinagps.fee.service.impl.BillServiceJob;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.page.Page;

/**
 * @Package:com.fee.service.impl
 * @ClassName:PaytmentServiceTest
 * @Description:缴费业务层测试类
 * @author:zfy
 * @date:2014-6-5 下午3:24:39
 */
@RunWith(SpringJUnit4ClassRunner.class)//指定测试用例的运行器 这里是指定了Junit4 
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional //对所有的测试方法都使用事务，并在测试完成后回滚事务 
@TransactionConfiguration(transactionManager = "mysql1TxManager", defaultRollback = false)
public class PaytmentServiceTest  {
	@Autowired
	@Qualifier("paymentService")
	private PaymentService paymentService;
	
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
	
	/*@Test
    public void testFindLPayPrint4PrintSetPage() {
		try {
			PageSelect<Map<String,Object>> pageSelect=new PageSelect<Map<String,Object>>();
			Page<Map<String,Object>> result=paymentService.findLPayPrint4PrintSetPage(pageSelect);
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
    public void testUpdatePrintFreqByCustomerId() {
		try {
			Collection collection=new Collection();
			collection.setCustomerId(11l);
			collection.setPrintFreq(1);
			int result=paymentService.updatePrintFreqByCustomerId(collection);
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
    public void testFindPrintsPage() {
		try {
			PageSelect<Map<String,Object>> pageSelect=new PageSelect<Map<String,Object>>();
			Page<Map<String,Object>> result=paymentService.findPrintsPage(pageSelect);
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
    public void testFindPrints2() {
		try {
			Map<String,Object> map=new HashMap<String,Object>();
			//map.put("customerId", 13);
			List<Long> paymentIds=new ArrayList<Long>();
			paymentIds.add(5l);
			paymentIds.add(6l);
			paymentIds.add(7l);
			paymentIds.add(8l);
			paymentIds.add(9l);
			map.put("paymentIds", paymentIds);
			List<Map<String,Object>> result=paymentService.findPrints2(map);
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
    public void testUpdatePrintNums() {
		try {
			Map<String,Object> map=new HashMap<String,Object>();
			//map.put("customerId", 13);
			List<Long> paymentIds=new ArrayList<Long>();
			paymentIds.add(5l);
			paymentIds.add(6l);
			paymentIds.add(7l);
			paymentIds.add(8l);
			paymentIds.add(9l);
			map.put("paymentIds", paymentIds);
			int result=paymentService.updatePrintNums(paymentIds);
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
	*/
	 @Test
	 public void testBatchAddPayments() {
		 List<Payment> payments=new ArrayList<Payment>();
		 Payment payment =null;
		 payment=new Payment();
		 java.util.Date date=new java.util.Date();
		 for(int i=0;i<10;i++){
			 //payment=new Payment();
			 payment.setSubcoNo(5l);
			 payment.setOrgId(Long.valueOf(i+1));
			 payment.setCustomerId(1323l);
			 payment.setVehicleId(1742l);
			 payment.setUnitId(1736l);
			 payment.setFeetypeId(1);
			 payment.setAcAmount(22f);
			 payment.setPayModel(0);
			 payment.setRealAmount(22f);
			 payment.setItemId(1l);
			 payment.setOpId(1l);
			 date.setSeconds(date.getSeconds()+i);
			 payment.setPrintTime(date);
			 payment.setPayTime(date);
			 payment.setStamp(payment.getPayTime());
			 payment.setRemark("压力测试");
			 //payments.add(payment);
			 paymentService.save(payment);
			 payment=(Payment)payment.clone();
			/* try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		 }
		// ThreadPayment threadPayment=new ThreadPayment(payments, paymentService);
		 //threadPayment.start();
		
		 
	 }
	 class ThreadPayment extends Thread{
		 private List<Payment> payments;
		 private PaymentService paymentService;
		 public ThreadPayment(List<Payment> payments,PaymentService paymentService){
			 this.payments=payments;
			 this.paymentService=paymentService;
		 }
		 public void run(){
			 System.out.println(payments.size());
			for (Payment payment :payments) {
				System.out.println("add ");
				paymentService.save(payment);
				/*try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
			}
		 }
	 }
}

