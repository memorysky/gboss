package com.chinagps.fee.service.impl;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;

import ldap.objectClasses.CommonCompany;
import ldap.oper.OpenLdap;
import ldap.oper.OpenLdapManager;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.chinagps.fee.comm.SystemConst;
import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.controller.SysController;
import com.chinagps.fee.dao.BillDao;
import com.chinagps.fee.dao.PaymentDao;
import com.chinagps.fee.dao.SysDao;
import com.chinagps.fee.dao.impl.BillDaoImp;
import com.chinagps.fee.entity.po.sys.SysValue;
import com.chinagps.fee.entity.vo.ItemVO;
import com.chinagps.fee.util.CreateEmailHtml;
import com.chinagps.fee.util.MoneyUtil;
import com.chinagps.fee.util.SendMail;
import com.chinagps.fee.util.StringUtils;
import com.chinagps.fee.util.Utils;

/**
 * @Package:com.chinagps.fee.service.impl
 * @ClassName:BillServiceJob
 * @Description:账单生成的定时器业务层实现类
 * @author:zfy
 * @date:2014-5-20 下午7:03:08
 */
@Service("billServiceJob")
public class BillServiceJob extends BaseServiceImpl  {
	protected static final Logger LOGGER = LoggerFactory.getLogger(BillServiceJob.class);
	
	@Autowired  
	@Qualifier("billDaoImp")
	private BillDao billDao;
	
	@Autowired  
	@Qualifier("paymentDao")
	private PaymentDao paymentDao;
	
	@Autowired  
	@Qualifier("sysDao")
	private SysDao sysDao;
	
	/**
	 * @Title:createFeeBill
	 * @Description:每个月15号 0点过5分 生成账单
	 * @throws
	 */
	// @Scheduled(cron = "0 5 0 15 * ?")  
	public void createFeeBill(){
		 Session session=billDao.getMysql1CurrentSession();
		 //如果用openSession，则需要手动开启事务，且需要手动关闭事务
		 //Transaction tx= session.beginTransaction(); //开启事务
		 session.doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				CallableStatement cs=connection.prepareCall("{call p_create_bill(?)}");
				cs.registerOutParameter(1, Types.INTEGER);
				cs.execute();
				int result=cs.getInt(1);
				//System.out.println(result+"ok");
			}
		}); 
		// tx.commit();  
	   // session.close(); 
	 }
	
	/**
	 * @Title:sendEmail
	 * @Description:每个月1号8点过5分 发送邮件(托收成功，发送给客户)
	 * @throws
	 */
	//@Scheduled(cron = "0 5 8 1 * ?")
	//@Scheduled(cron = "0 2 17 7 * ?")
	public void sendEmail(){
		try {
			Map<String,Object> conditionMap=new HashMap<String, Object>();
			conditionMap.put("printNum", 0);//第一次打印
			conditionMap.put("isDelivery", 3);//投递方式：电子对账单
			List<Map<String, Object>> dataList=paymentDao.findPrints(conditionMap, null, false, 0, 0);
			List<Map<String, Object>> detailDataList=null;
			
			 Map<Integer, String> ITEM_TYPE = new HashMap<Integer, String>();
	         ITEM_TYPE.put(1, "服务费");
	 		ITEM_TYPE.put(2, "SIM卡");
	 		ITEM_TYPE.put(3, "保险");
	 		ITEM_TYPE.put(4, "终端产品");
	 		ITEM_TYPE.put(5, "回单");
			
			if(Utils.isNotNullOrEmpty(dataList)){
				StringBuffer sb=new StringBuffer();
				String email=null;
				String paymentIdsStr=null;
				//解析项目明细，并修改打印次数，数字金额转人民币，拼接计费时段
				String paymentIds=null;
				 String itemFeetypeIds=null;//项目类型id
				 
				 String itemFeetypeIds2[]=null;
				 String itemAmounts=null;//每个不同项目类型的金额
				 String itemAmounts2[]=null;
				 String itemPaytimes=null;//每个不同项目类型的扣款时间
				 String itemPaytimes2[]=null;
				 HashMap<String, Object> itemMap=null;
				 List<ItemVO> itemList=null;
				 ItemVO itemVO=null;
				 String realAmount=null;//金额
				 String sdate=null;//开始时间
				 String edate=null;//结束时间
				 SendMail sendmail = SendMail.getSendMail();
			       
				for (Map<String, Object> dataMap : dataList) {
					sb.delete(0, sb.length());
					email=Utils.clearNull(dataMap.get("email"));
					  sb.append("<span style='font-weight:bold;'> 尊敬的 ").append(dataMap.get("customerName")).append(" 用户  您好!</span>");
					  //sb.append(" 感谢您使用赛格导航终端！特为您呈上2014年05月09日至2014年06月08日计费托收情况。");
					  sb.append("</br> 感谢您使用赛格导航终端！");
					  if(StringUtils.isNotBlank(email)){
						paymentIdsStr=Utils.clearNull(dataMap.get("paymentIds"));
						if(StringUtils.isNotBlank(paymentIdsStr)){
							conditionMap.put("paymentIds", Arrays.asList(StringUtils.stringToLongArray(paymentIdsStr)));
							detailDataList=paymentDao.findPrints2(conditionMap);
							if(Utils.isNotNullOrEmpty(detailDataList)){
								for (Map<String, Object> map : detailDataList) {
									//拼接计费时段
									sdate=map.get("sdate")==null?null:String.valueOf(map.get("sdate"));
									edate=map.get("edate")==null?null:String.valueOf(map.get("edate"));
								
									sb.append("</br>特为您呈上").append(sdate).append("至").append(edate).append("计费托收情况。");
									sb.append("车载终端电话:"+map.get("callLetter"));
									
									//数字金额转人民币
									realAmount=map.get("realAmount")==null?null:String.valueOf(map.get("realAmount"));
									if(StringUtils.isNotBlank(realAmount)){
										sb.append("</br>缴费总额(大写):"+MoneyUtil.CNValueOf(realAmount));
										sb.append("&nbsp;&nbsp;(小写):"+realAmount);
									}
									
									//修改打印次数
									paymentIds=map.get("paymentIds")==null?null:String.valueOf(map.get("paymentIds"));
									if(StringUtils.isNotBlank(paymentIds)){
										paymentDao.updatePrintNums(null,paymentIds,1);
									}
									//解析项目明细
									itemFeetypeIds =map.get("itemFeetypeIds")==null?null:String.valueOf(map.get("itemFeetypeIds"));
									itemAmounts =map.get("itemAmounts")==null?null:String.valueOf(map.get("itemAmounts"));
									itemPaytimes =map.get("itemPaytimes")==null?null:String.valueOf(map.get("itemPaytimes"));
									if(StringUtils.isNotBlank(itemFeetypeIds) && StringUtils.isNotBlank(itemAmounts) && StringUtils.isNotBlank(itemPaytimes)){
										itemFeetypeIds2=itemFeetypeIds.split(",");
										itemAmounts2=itemAmounts.split(",");
										itemPaytimes2=itemPaytimes.split(",");
										if(Utils.isNotNullOrEmpty(itemFeetypeIds2) && Utils.isNotNullOrEmpty(itemAmounts2) && Utils.isNotNullOrEmpty(itemPaytimes2)){
											itemMap=new HashMap<String, Object>();
											itemList=new ArrayList<ItemVO>();
											for (int i = 0; i < itemFeetypeIds2.length; i++) {
												//如果项目类型ID存在就累加金额
												if(itemMap.containsKey(itemFeetypeIds2[i])){
													for (ItemVO itemVO2 : itemList) {
														if(itemVO2.getItemId().equals(itemFeetypeIds2[i])){
															itemVO2.setItemMoney(itemVO2.getItemMoney()+(itemAmounts2[i]==null?0:Float.valueOf(itemAmounts2[i])));
															break;
														}
													}
												}else{//如果项目类型ID不存在添加一条数据	
													itemMap.put(itemFeetypeIds2[i], ITEM_TYPE.get(Integer.valueOf(itemFeetypeIds2[i])));
													itemVO=new ItemVO();
													itemVO.setItemId(itemFeetypeIds2[i]);
													itemVO.setItemName(itemMap.get(itemFeetypeIds2[i])==null?null:itemMap.get(itemFeetypeIds2[i]).toString());
													itemVO.setItemMoney(itemAmounts2[i]==null?null:Float.valueOf(itemAmounts2[i]));
													itemVO.setPayDate(itemPaytimes2[i]==null?null:itemPaytimes2[i]);
													itemList.add(itemVO);
												}
											}
											sb.append("</br>项目明细：");
											sb.append("<table style='border-collapse: collapse;border-spacing: 0;border: 1px solid #ddd !important;'>");
											sb.append("<tr>");
											sb.append("<td style='border: 1px solid #ddd !important;font-weight:bold;'>项目</td>");
											sb.append("<td style='border: 1px solid #ddd !important;font-weight:bold;'>金额</td>");
											sb.append("<td style='border: 1px solid #ddd !important;font-weight:bold;'>扣款日期</td>");
											sb.append("</tr>");
										
											for (ItemVO itemVO2 : itemList) {
												sb.append("<tr>");
												sb.append("<td style='border: 1px solid #ddd !important;'>").append(itemVO2.getItemName()).append("</td>");
												sb.append("<td style='border: 1px solid #ddd !important;'>").append(itemVO2.getItemMoney()).append("</td>");
												sb.append("<td style='border: 1px solid #ddd !important;'>").append(itemVO2.getPayDate()).append("</td>");
												sb.append("</tr>");
											
											}
											sb.append("</tr>");
											sb.append("</table>");
										}
									}
								}	
							}
						}
						
						sendmail.setTo(email);
				        sendmail.setSubject(SystemConst.FEE_EMAIL_TITLE);
				        sendmail.setContent(sb.toString());
				        sendmail.sendMail();  
					}
				}
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
	 }
	/**
	 * @Title:sendEmail
	 * @Description:每个月1号8点过5分 发送邮件(托收成功，发送给客户)
	 * @throws
	 */
	//@Scheduled(cron = "1 * * * * ?")
	//@Scheduled(cron = "0 5 8 1 * ?")
	public void sendEmail2(){
		try {
			LOGGER.info("begin enter sendEmail()");
			System.out.println("begin enter sendEmail()");
			
			//计费项目类型
			SysValue sysValue=new SysValue();
			sysValue.setIsDel(0);
			sysValue.setStype(SystemConst.FEE_TYPE_ID);
			List<SysValue> list=sysDao.findSysValue(sysValue);
			Map<String, String> feeTypeMap=null;
			if(Utils.isNotNullOrEmpty(list)){
				feeTypeMap=new HashMap<String, String>();
				for (SysValue sysValue2 : list) {
					feeTypeMap.put(sysValue2.getSvalue(), sysValue2.getSname());
				}
			}
			Map<String,Object> conditionMap=new HashMap<String, Object>();
			conditionMap.put("printNum", 0);//第一次打印
			conditionMap.put("isDelivery", 3);//投递方式：电子对账单
			List<Map<String, Object>> dataList=paymentDao.findPrints(conditionMap, null, false, 0, 0);
			List<Map<String, Object>> detailDataList=null;
			 CreateEmailHtml createEmailHtml = new CreateEmailHtml();
			 String targetFileDirectory=System.getProperty("user.dir");//"D:/summary/javaToWord";
	         String sFileName = "email.htm";
	         String mailTemplete = targetFileDirectory+"/"+sFileName;
	         String readHTML=null;
	         OpenLdap openLdap = OpenLdapManager.getInstance();
			if(Utils.isNotNullOrEmpty(dataList)){
				String email=null;
				Integer subCo=null;
				String paymentIdsStr=null;
				//解析项目明细，并修改打印次数，数字金额转人民币，拼接计费时段
				String paymentIds=null;
				 String itemFeetypeIds=null;//项目类型id
				 
				 String itemFeetypeIds2[]=null;
				 String itemAmounts=null;//每个不同项目类型的金额
				 String itemAmounts2[]=null;
				 String itemPaytimes=null;//每个不同项目类型的扣款时间
				 String itemPaytimes2[]=null;
				 HashMap<String, Object> itemMap=null;
				 List<ItemVO> itemList=null;
				 ItemVO itemVO=null;
				 SendMail sendmail = SendMail.getSendMail();
			     Map<Integer,String> companyNameMap=new HashMap<Integer,String>();  
			     CommonCompany commonCompany=null;
			     for (Map<String, Object> dataMap : dataList) {
					email=Utils.clearNull(dataMap.get("email"));
					//设置分公司名称
					subCo=5;
					subCo=dataMap.get("subco_no")==null?5:((BigInteger)dataMap.get("subco_no")).intValue();
					if(subCo!=null){
						if(companyNameMap.containsKey(subCo)){
							dataMap.put("companyName",companyNameMap.get(subCo));
						}else{
							commonCompany =  openLdap.getCompanyById(subCo.toString());
							if(commonCompany!=null){
								companyNameMap.put(subCo,commonCompany.getCnfullname());
								dataMap.put("companyName",commonCompany.getCnfullname());
							}
							
						}
					}
					  if(StringUtils.isNotBlank(email)){
						paymentIdsStr=Utils.clearNull(dataMap.get("paymentIds"));
						if(StringUtils.isNotBlank(paymentIdsStr)){
							conditionMap.put("paymentIds", Arrays.asList(StringUtils.stringToLongArray(paymentIdsStr)));
							detailDataList=paymentDao.findPrints2(conditionMap);
							if(Utils.isNotNullOrEmpty(detailDataList)){
								for (Map<String, Object> map : detailDataList) {
									//修改打印次数
									paymentIds=map.get("paymentIds")==null?null:String.valueOf(map.get("paymentIds"));
									if(StringUtils.isNotBlank(paymentIds)){
										paymentDao.updatePrintNums(null,paymentIds,1);
									}
									//解析项目明细
									itemFeetypeIds =map.get("itemFeetypeIds")==null?null:String.valueOf(map.get("itemFeetypeIds"));
									itemAmounts =map.get("itemAmounts")==null?null:String.valueOf(map.get("itemAmounts"));
									itemPaytimes =map.get("itemPaytimes")==null?null:String.valueOf(map.get("itemPaytimes"));
									if(StringUtils.isNotBlank(itemFeetypeIds) && StringUtils.isNotBlank(itemAmounts) && StringUtils.isNotBlank(itemPaytimes)){
										itemFeetypeIds2=itemFeetypeIds.split(",");
										itemAmounts2=itemAmounts.split(",");
										itemPaytimes2=itemPaytimes.split(",");
										if(Utils.isNotNullOrEmpty(itemFeetypeIds2) && Utils.isNotNullOrEmpty(itemAmounts2) && Utils.isNotNullOrEmpty(itemPaytimes2)){
											itemMap=new HashMap<String, Object>();
											itemList=new ArrayList<ItemVO>();
											for (int i = 0; i < itemFeetypeIds2.length; i++) {
												//如果项目类型ID存在就累加金额
												if(itemMap.containsKey(itemFeetypeIds2[i])){
													for (ItemVO itemVO2 : itemList) {
														if(itemVO2.getItemId().equals(itemFeetypeIds2[i])){
															itemVO2.setItemMoney(itemVO2.getItemMoney()+(itemAmounts2[i]==null?0:Float.valueOf(itemAmounts2[i])));
															break;
														}
													}
												}else{//如果项目类型ID不存在添加一条数据	
													itemMap.put(itemFeetypeIds2[i], feeTypeMap.get(itemFeetypeIds2[i]));
													itemVO=new ItemVO();
													itemVO.setItemId(itemFeetypeIds2[i]);
													itemVO.setItemName(itemMap.get(itemFeetypeIds2[i])==null?null:itemMap.get(itemFeetypeIds2[i]).toString());
													itemVO.setItemMoney(itemAmounts2[i]==null?null:Float.valueOf(itemAmounts2[i]));
													itemVO.setPayDate(itemPaytimes2[i]==null?null:itemPaytimes2[i]);
													itemList.add(itemVO);
												}
											}
											map.put("itemList",itemList);
										
										}
									}
									
								}	
								dataMap.put("unitList", detailDataList);
							}
						}
						
						//生成html文件
				         boolean bOK = createEmailHtml.geneHtmlFile("email_invoice.ftl",dataMap, targetFileDirectory,sFileName);
				         //System.out.println("already create email.html");
				         if(bOK){
				        	 sendmail.setTo(email);
				        	 sendmail.setSubject(SystemConst.FEE_EMAIL_TITLE);
				        	 readHTML =createEmailHtml.readHTML(mailTemplete);
				        	  //发送html文件的内容
				        	 //System.out.println("发送的html:"+readHTML);
				        	 sendmail.setContent(readHTML); 
				        	 sendmail.sendMail(); 
				        	 //System.out.println("send ok");
				         }
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			e.printStackTrace();
		}
		LOGGER.info("leave sendEmail()");
		System.out.println("leave sendEmail()");
	 }
	
	/**
	 * @Title:sendEmailTest
	 * @Description:模拟数据测试邮件发送
	 * @param emailAddress
	 * @throws
	 */
	public void sendEmailTest(String emailAddress){
		try {
			LOGGER.info("begin enter sendEmailTest()");
			System.out.println("begin enter sendEmailTest()");
			Map<String,Object> dataMap=new HashMap<String, Object>();
			dataMap.put("customerId",1);
			dataMap.put("customerName","周峰炎");
			dataMap.put("address","湖南 长沙 岳麓 麓景路2号生产力促进中心");
			dataMap.put("postCode","430013");
			dataMap.put("acName","周峰炎");
			dataMap.put("addressee","周峰炎");
			dataMap.put("acNo","6223687321898");
			if(StringUtils.isBlank(emailAddress)){
				emailAddress="2697396219@qq.com";
			}
			dataMap.put("email",emailAddress);
			dataMap.put("isDelivery",3);
			dataMap.put("realAmount",3000);
			dataMap.put("payTime","2014-07-08");
			dataMap.put("printNum",1);
			dataMap.put("subco_no",5);
			dataMap.put("companyName","深圳赛格导航有限公司");
			
			List<Map<String, Object>> detailDataList=new ArrayList<Map<String,Object>>();
			Map<String,Object> detailDataMap=new HashMap<String, Object>();
			detailDataMap.put("callLetter","15116351819");
			detailDataMap.put("sdate","2014-06-01");
			detailDataMap.put("edate","2014-08-01");
			detailDataMap.put("realAmount",3000);
			detailDataMap.put("payTime","2014-07-01");
			detailDataMap.put("bwNo","0001");
			detailDataMap.put("itemFeetypeIds","1,2,3");
			detailDataMap.put("itemAmounts","1200,1300,500");
			detailDataMap.put("itemPaytimes","2014-05-15,2014-06-15,2014-07-15");
			detailDataList.add(detailDataMap);
			
			 CreateEmailHtml createEmailHtml = new CreateEmailHtml();
			 String targetFileDirectory=System.getProperty("user.dir");//"D:/summary/javaToWord";
	         String sFileName = "email.htm";
	         String mailTemplete = targetFileDirectory+"/"+sFileName;
	         String readHTML=null;
	         
	         Map<Integer, String> ITEM_TYPE = new HashMap<Integer, String>();
	         ITEM_TYPE.put(1, "服务费");
	 		ITEM_TYPE.put(2, "SIM卡");
	 		ITEM_TYPE.put(3, "保险");
	 		ITEM_TYPE.put(4, "终端产品");
	 		ITEM_TYPE.put(5, "回单");
			if(Utils.isNotNullOrEmpty(dataMap)){
				String email=null;
				//解析项目明细，数字金额转人民币，拼接计费时段
				 String itemFeetypeIds=null;//项目类型id
				 
				 String itemFeetypeIds2[]=null;
				 String itemAmounts=null;//每个不同项目类型的金额
				 String itemAmounts2[]=null;
				 String itemPaytimes=null;//每个不同项目类型的扣款时间
				 String itemPaytimes2[]=null;
				 HashMap<String, Object> itemMap=null;
				 List<ItemVO> itemList=null;
				 ItemVO itemVO=null;
				 SendMail sendmail = SendMail.getSendMail();
					email=Utils.clearNull(dataMap.get("email"));
					  if(StringUtils.isNotBlank(email)){
							//detailDataList=paymentDao.findPrints2(dataMap);
							if(Utils.isNotNullOrEmpty(detailDataList)){
								for (Map<String, Object> map : detailDataList) {
									//解析项目明细
									itemFeetypeIds =map.get("itemFeetypeIds")==null?null:String.valueOf(map.get("itemFeetypeIds"));
									itemAmounts =map.get("itemAmounts")==null?null:String.valueOf(map.get("itemAmounts"));
									itemPaytimes =map.get("itemPaytimes")==null?null:String.valueOf(map.get("itemPaytimes"));
									if(StringUtils.isNotBlank(itemFeetypeIds) && StringUtils.isNotBlank(itemAmounts) && StringUtils.isNotBlank(itemPaytimes)){
										itemFeetypeIds2=itemFeetypeIds.split(",");
										itemAmounts2=itemAmounts.split(",");
										itemPaytimes2=itemPaytimes.split(",");
										if(Utils.isNotNullOrEmpty(itemFeetypeIds2) && Utils.isNotNullOrEmpty(itemAmounts2) && Utils.isNotNullOrEmpty(itemPaytimes2)){
											itemMap=new HashMap<String, Object>();
											itemList=new ArrayList<ItemVO>();
											for (int i = 0; i < itemFeetypeIds2.length; i++) {
												//如果项目类型ID存在就累加金额
												if(itemMap.containsKey(itemFeetypeIds2[i])){
													for (ItemVO itemVO2 : itemList) {
														if(itemVO2.getItemId().equals(itemFeetypeIds2[i])){
															itemVO2.setItemMoney(itemVO2.getItemMoney()+(itemAmounts2[i]==null?0:Float.valueOf(itemAmounts2[i])));
															break;
														}
													}
												}else{//如果项目类型ID不存在添加一条数据	
													itemMap.put(itemFeetypeIds2[i], ITEM_TYPE.get(Integer.valueOf(itemFeetypeIds2[i])));
													itemVO=new ItemVO();
													itemVO.setItemId(itemFeetypeIds2[i]);
													itemVO.setItemName(itemMap.get(itemFeetypeIds2[i])==null?null:itemMap.get(itemFeetypeIds2[i]).toString());
													itemVO.setItemMoney(itemAmounts2[i]==null?null:Float.valueOf(itemAmounts2[i]));
													itemVO.setPayDate(itemPaytimes2[i]==null?null:itemPaytimes2[i]);
													itemList.add(itemVO);
												}
											}
											map.put("itemList",itemList);
										
										}
									}
									
								}	
								dataMap.put("unitList", detailDataList);
							}
						
						//生成html文件
				         boolean bOK = createEmailHtml.geneHtmlFile("email_invoice.ftl",dataMap, targetFileDirectory,sFileName);
				         //System.out.println("already create email.html");
				         if(bOK){
				        	 sendmail.setTo(email);
				        	 sendmail.setSubject(SystemConst.FEE_EMAIL_TITLE);
				        	 readHTML =createEmailHtml.readHTML(mailTemplete);
				        	  //发送html文件的内容
				        	 //System.out.println("发送的html:"+readHTML);
				        	 sendmail.setContent(readHTML); 
				        	 sendmail.sendMail(); 
				        	 //System.out.println("send ok");
				         }
					}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			e.printStackTrace();
		}
		LOGGER.info("leave sendEmailTest()");
		System.out.println("leave sendEmailTest()");
	 }
}

