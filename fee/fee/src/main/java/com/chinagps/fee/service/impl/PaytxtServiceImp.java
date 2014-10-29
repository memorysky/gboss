package com.chinagps.fee.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.chinagps.fee.comm.SystemConst;
import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.dao.BillDao;
import com.chinagps.fee.dao.PaymentDao;
import com.chinagps.fee.dao.PaytxtDao;
import com.chinagps.fee.entity.po.Datalock;
import com.chinagps.fee.entity.po.Payment;
import com.chinagps.fee.entity.po.Paytxt;
import com.chinagps.fee.service.PaytxtService;
import com.chinagps.fee.util.FormatUtil;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.StringUtils;
import com.chinagps.fee.util.Utils;
import com.chinagps.fee.util.page.Page;
import com.chinagps.fee.util.page.PageUtil;

/**
 * @Package:com.chinagps.fee.service.impl
 * @ClassName:PaytxtServiceImp
 * @Description:托收文件记录业务层实现类
 * @author:zfy
 * @date:2014-5-21 下午4:25:05
 */
@Service("paytxtService")
public class PaytxtServiceImp extends BaseServiceImpl implements
		PaytxtService {
	protected static final Logger LOGGER = LoggerFactory.getLogger(PaytxtServiceImp.class);
	
	private long result=0;
			 
	@Autowired  
	@Qualifier("paytxtDao")
	private PaytxtDao paytxtDao;
	
	@Autowired  
	@Qualifier("billDaoImp")
	private BillDao billDao;
	
	@Autowired  
	@Qualifier("paymentDao")
	private PaymentDao paymentDao;

	@Override
	public List<Paytxt> findPaytxts(Map<String, Object> conditionMap)
			throws SystemException {
		return paytxtDao.findPaytxts(null,conditionMap,0,0);
	}

	@Override
	public long addPaytxts(final long subcoNo,final int agency ,final String fileName,final long opId,final Date endDate)
			throws SystemException {
		 Session session=paytxtDao.getMysql1CurrentSession();
		 //如果用openSession，则需要手动开启事务，且需要手动关闭事务
		 //Transaction tx= session.beginTransaction(); //开启事务
		 session.doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				CallableStatement cs=connection.prepareCall("{call p_create_paytxt_company(?,?,?,?,?,?)}");
			/*	cs.registerOutParameter(1, Types.BIGINT);
				cs.registerOutParameter(2, Types.INTEGER);
				cs.registerOutParameter(3, Types.VARCHAR);
				cs.registerOutParameter(4, Types.BIGINT);
				cs.registerOutParameter(5, Types.DATE);
				cs.registerOutParameter(6, Types.BIGINT);*/
				cs.setLong(1, subcoNo);
				cs.setInt(2, agency);
				cs.setString(3, fileName);
				cs.setLong(4, opId);
				cs.setDate(5, endDate);
				cs.execute();
				PaytxtServiceImp.this.result=cs.getLong(6);
			}
			
		}); 
		 return result;
	}

	@Override
	public  Map<String,Object> getZjPaytxts(Map<String, Object> conditionMap)
			throws SystemException {
		return paytxtDao.getZjPaytxts(conditionMap);
	}
	
	@Override
	public String getMaxTxtName(Map<String, Object> conditionMap)throws SystemException {
		return paytxtDao.getMaxTxtName(conditionMap);
		
	}

	@Override
	public String getMaxFileTimesOfToday(Map<String, Object> conditionMap)
			throws SystemException {
		return paytxtDao.getMaxFileTimesOfToday(conditionMap);
	}


	@Override
	public void addUpdateDataLock(Datalock datalock) throws SystemException {
		 paytxtDao.merge(datalock);
	}

	@Override
	public Page<Map<String, Object>> findPaytxtPage(
			PageSelect<Map<String, Object>> pageSelect) throws SystemException {
		List<Map<String, Object>> dataList=paytxtDao.findPaytxts(pageSelect.getFilter(), pageSelect.getOrder(), pageSelect.getIs_desc(), pageSelect.getPageNo(), pageSelect.getPageSize());
		int total=paytxtDao.countPaytxts(pageSelect.getFilter());
		return PageUtil.getPage(total, pageSelect.getPageNo(), dataList, pageSelect.getPageSize());

	}

	@Override
	public List<Map<String, Object>> findAllPaytxt(Map<String, Object> map)
			throws SystemException {
		return paytxtDao.findPaytxts(map, null, false, 0, 0);
	}

	@Override
	public Datalock getDatalockByCompanyId(Datalock datalock) throws Exception {
		if(Utils.isNotNullOrEmpty(datalock) && Utils.isNotNullOrEmpty(datalock.getSubcoNo())){
			return paytxtDao.get(Datalock.class, datalock.getSubcoNo());
		}
		return null;
	}

	@Override
	@Transactional(rollbackFor = java.lang.Exception.class,propagation=Propagation.REQUIRES_NEW)
	public void uploadPaytxt1(List<MultipartFile> paytxtFiles, int agency,HttpServletRequest request,HttpServletResponse response)
			throws Exception {
		boolean flag = true;
		String msg = SystemConst.OP_SUCCESS;
		StringBuffer info = new StringBuffer(); //记录返回的信息
		OutputStream out = null;
		InputStreamReader isr=null;
		Session session=null;
		Transaction tx=null;
		try {
			session=paytxtDao.openSession();
			tx=session.beginTransaction();
			session.setCacheMode(CacheMode.IGNORE);//忽略第二级缓存
			session.setFlushMode(FlushMode.AUTO);//如果FlushMode是MANUAL或NEVEL,在操作过程中hibernate会将事务设置为readonly
			
			
			String userId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_ID);
			//分公司ID
			String companyId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_COMPANYID);
			
			if(paytxtFiles!=null && paytxtFiles.size()%2==0){
				Long opId=Long.valueOf(userId);
				 String fileName=null;
				 String fileNamePrefix=null;
				 String fileNameSuffix=null;
				 BufferedReader br = null;
				 long recordNumY=0;//已划款户数
				 double feeSumY=0f;//已划款金额
				 //存放上传文件的结果
				 //交易文件
				Map<String,Object> paytxt10Map=new HashMap<String,Object>();
				//总结文件
				Map<String,Object> paytxt20Map=new HashMap<String,Object>();
				
				for (MultipartFile paytxtFile : paytxtFiles) {
					 if(paytxtFile.getSize()>10000000){
						    flag = false;
							msg = "上传失败：文件大小不能超过10M!";
							break;
			                //throw new Exception("上传失败：文件大小不能超过10M");            
			          }else{
			        	  fileName=paytxtFile.getOriginalFilename();
			        	  fileNamePrefix=fileName.substring(0,fileName.lastIndexOf("."));
			        	  fileNameSuffix=fileName.substring(fileName.lastIndexOf("."));
			        	  //提回交易文件
			        	  if(SystemConst.PAY_TXT_TYPE.get(6).equalsIgnoreCase(fileNameSuffix)){
			        	     paytxt10Map.put(fileNamePrefix,paytxtFile);
			        	  } else if(SystemConst.PAY_TXT_TYPE.get(7).equalsIgnoreCase(fileNameSuffix)){ //提回总结文件
			        		  paytxt20Map.put(fileNamePrefix,paytxtFile);
			        	  }
			        	  
			          }
					 
				}
				
				Set<Map.Entry<String, Object>> keySet=paytxt10Map.entrySet();
				Iterator<Entry<String, Object>> iterator=keySet.iterator();
				String keyStr=null;
				String trueFileName=null;
				//String trueFileNameLike=null;//去掉批次的文件名
				String newFileName=null;//最新的文件名
				MultipartFile multipartFile=null;
				Map<String,Object> map=new HashMap<String,Object>();
			     int totalnum = 0; //记录总共上传的记录数
			     List tradeUnSuccessfulData; //收失败的记录
			     String traderecord = null;
			     Paytxt paytxt=null;
			     String kkDate=null;//扣款日期
			     int lineIndex=0;//每个文件的第n行
			     Object[] objArray=null;//总结文件返回的数据
			    long subcoNo=Long.valueOf(companyId);
				map.put("subcoNo", subcoNo);
				map.put("agency", agency);
				//托收返回码
				map.put("notPayTag", SystemConst.PAY_Y1);
				
				/*“Y”-成功扣款
				“A”-已销户或无此帐户
				“B”-帐户冻结
				“E”-金额不足
				“F”-直接借记支付中的金额超过事先规定限额
				“G”-直接借记无授权记录
				“O”-其他原因*/
				
				
				while (iterator.hasNext()) {
					lineIndex=0;
					totalnum=0;
					feeSumY=0d;
					recordNumY=0;
					Map.Entry<java.lang.String, java.lang.Object> entry = (Map.Entry<java.lang.String, java.lang.Object>) iterator
							.next();
					keyStr=entry.getKey();
					trueFileName=keyStr;
					if(agency==2){//金融中心
						trueFileName= keyStr.substring(0,3)+SystemConst.ZERO2+keyStr.substring(5);
					}
					//判断是否有需要提回的托收文件记录
					map.put("txtName", trueFileName);
					
					//删除中间表相关数据
					paytxtDao.deleteTmpTable(session, subcoNo, trueFileName,agency);
					session.flush();
					session.clear();
		            tx.commit();
		            tx=session.beginTransaction();
					//判断是否是最新的文件
					newFileName=getMaxTxtName(map);
					if(newFileName!=null && newFileName.equals(trueFileName)){
						Map<String,Object> mapZj=getZjPaytxts(map);
						if(Utils.isNotNullOrEmpty(mapZj.get("feeSum"))){//需要
							
							if(StringUtils.isNotBlank(keyStr)){
								//总结文件
								if(!paytxt20Map.containsKey(keyStr)){
									flag = false;
									msg = "上传文件失败，请检查是否选择了交易文件名为【"+keyStr+"】相匹配的总结文件!";
									break;
								}else{
									//总结文件
									multipartFile=(MultipartFile) paytxt20Map.get(keyStr);
									isr=new InputStreamReader(multipartFile.getInputStream());
									br = new BufferedReader(isr);
									objArray=setTotalFileLine(br.readLine(),agency);
									kkDate=Utils.clearNull(objArray[0]);
									if(agency==2){//金融中心
										if(Utils.isNotNullOrEmpty(objArray[3])){
											if(!objArray[3].equals(keyStr)){
												info.append("上传的总结文件：  " + keyStr + " 中，交易文件名与实际上传的文件名不匹配，上传失败!");
												continue;
											}
										}
									}
									
									//判断交易文件和总结文件的已划款户数、已划款金额是否一致
									if(StringUtils.isNotBlank(kkDate)){
										//交易文件
										multipartFile=(MultipartFile) entry.getValue();
										isr=new InputStreamReader(multipartFile.getInputStream());
										br = new BufferedReader(isr);
										List<Paytxt> paytxts=new ArrayList<Paytxt>();
										tradeUnSuccessfulData = new ArrayList(); //托收失败的记录
						                while ((traderecord = br.readLine()) != null) {
						                	lineIndex++;//行号
						                    totalnum++; //应上传的总数
						                    paytxt=new Paytxt();
						                    paytxt.setAgency(agency);
						                    paytxt.dealFileLine1(traderecord);
						                    paytxts.add(paytxt);
						                	 //已划款户数、已划款金额
						                	 if(SystemConst.PAY_Y1.equals(paytxt.getPayTag())){
						                		 feeSumY+=paytxt.getFeeSum();
												 recordNumY+=1;
											 }else{
												 tradeUnSuccessfulData.add("第"+lineIndex+"行："+paytxt.getPayContractNo());
											 }
						                	}
						                	DecimalFormat df = new DecimalFormat("0");
						                	String zjFeeSum=df.format(Utils.isNullOrEmpty(objArray[2])?0f:Double.valueOf(FormatUtil.tructZeroBefore((String)objArray[2])));
							                String jyFeeSum=df.format(feeSumY);
						                	if(recordNumY!=(Utils.isNullOrEmpty(objArray[1])?0:Long.valueOf(FormatUtil.tructZeroBefore((String)objArray[1])))){
												throw new RuntimeException("上传的交易文件：  " + keyStr + "与对应的总结文件已划款户数对不上，上传失败！");
												
											}//else if(new BigDecimal(feeSumY).toString()!=new BigDecimal((Utils.isNullOrEmpty(objArray[2])?0f:Double.valueOf(FormatUtil.tructZeroBefore((String)objArray[2])))).toString()){
							                else if(!jyFeeSum.equals(zjFeeSum)){	
							                	throw new RuntimeException("上传的交易文件：  " + keyStr + "与对应的总结文件已划款金额对不上，上传失败！");
											}
						                	//将paytxt记录插入到中间表
						                	paytxtDao.addTmpTable(session, paytxts, subcoNo, trueFileName, agency);
						                	
						                	session.flush();
											session.clear();
								            tx.commit();
								            tx=session.beginTransaction();
								            
								            //调用存储过程
								            updatePaytxts(session, subcoNo, agency, trueFileName, opId);
								            
								            
								            info.append("上传的交易文件：  " + keyStr + " 中，");
								              
							                info.append("写入数据库的记录共 " + totalnum + " 条");
							                int tradeUnSuccessfulSize=tradeUnSuccessfulData.size();
							                //如果有扣款失败的记录
							                if(tradeUnSuccessfulSize>0){
							                	 info.append(",扣款失败的记录共 " + tradeUnSuccessfulSize + " 条");
						                		 info.append("，扣款失败的合同号分别如下：</br>");
						                		 for (int j = 0; j < tradeUnSuccessfulSize; j++) {
						                			 info.append(tradeUnSuccessfulData.get(j).toString());
						                			 info.append("</br>");
						                		 }
							                 }
				                        	 info.append("</br>");
						             }else{
						            	 info.append("上传的总结文件：").append(keyStr).append(" 中，划款日期为空或者小于送银行日期，上传失败！");
										 continue;
						             }
									
								}
								
							}
						}else{
							 info.append("上传的交易文件： ").append(keyStr).append("中（上传失败），没有找到匹配的总结文件，或者没有找到需要更新的记录！</br>");
						}
					}else{
						info.append("上传的交易文件： ").append(keyStr).append("上传失败，不是最新的托收文件，最新的托收文件名为【").append(newFileName).append("】！</br>");
					}
				}
				
				if(info.length()<1){
					info.append("上传文件失败，请检查是否选择了匹配的交易文件和总结文件，或者没有找到需要更新的记录！");
				}
				msg=info.toString();
			}else{
				flag = false;
				msg = "上传文件失败，请检查是否选择了匹配的交易文件和总结文件!";
			}
			
			 session.flush();
			 session.clear();
             tx.commit();
             session.close();
			
			out = response.getOutputStream();
            String str = "<script>parent.callback('"+msg+"',"+flag+");</script>";
            out.write(str.getBytes("UTF-8"));
            out.flush();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			flag = false;
			msg = SystemConst.OP_FAILURE; //回滚事务
			tx.rollback();
			throw new Exception(e.getMessage());
		} finally {
			  if (session != null) {
				    if (session.isOpen()) {
				     //关闭session
				     session.close();
				    }
				   }
			 if(out != null){
	                try{out.close();}catch(Exception e1){
	                	e1.printStackTrace();
	                }
	             } if(isr != null){
		                try{isr.close();}catch(Exception e1){
		                	e1.printStackTrace();
		                }
		             }
		}
	}
	//总结文件处理
		public Object[] setTotalFileLine(String sumuprecord,int agency){
				 String dateString = null;
				 Object[] objArray=new Object[4];
				 	if(StringUtils.isNotBlank(sumuprecord)){
				        if (sumuprecord.length() < 91) {
				        } else {
				        	if (agency==1) {//银盛
				                dateString = sumuprecord.substring(87, 95);
				                objArray[1]=sumuprecord.substring(49, 57).trim();//已划款户数
				                objArray[2]=sumuprecord.substring(57, 71).trim();//已划款金额
				                //实际扣款日期，大于等于“送银行日期”
				                if(Long.valueOf(dateString)>=Long.valueOf(sumuprecord.substring(71, 79))){
				                	objArray[0]=dateString;//划款时间
				                }
				        	}else if (agency==2) {//金融中心
				                dateString = sumuprecord.substring(83, 91);
				                objArray[1]=sumuprecord.substring(45, 53).trim();//已划款户数
				                objArray[2]=sumuprecord.substring(53, 67).trim();//已划款金额
				                //实际扣款日期，大于等于“送银行日期”
				                if(Long.valueOf(dateString)>=Long.valueOf(sumuprecord.substring(67, 75))){
				                	objArray[0]=dateString;//划款时间
				                }
				                //文件名
				                objArray[3]=sumuprecord.substring(15, 23).trim();
				            }
				            //dateString = dateString.substring(0, 4) + "-" + dateString.substring(4, 6) + "-" +dateString.substring(6, 8);
				        }
				 	}
				 	
				 	
				 	
			       return objArray;
			}

		@Override
		//@Transactional(rollbackFor = java.lang.Exception.class,propagation=Propagation.REQUIRED)
		public void uploadPaytxt2(List<MultipartFile> paytxtFiles,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
			boolean flag = true;
			String msg = SystemConst.OP_SUCCESS;
			StringBuffer info = new StringBuffer(); //记录返回的信息
			OutputStream out = null;
			InputStreamReader isr=null;
			Session session=null;
			Transaction tx=null;
			try {
				session=paytxtDao.openSession();
				tx=session.beginTransaction();
				session.setCacheMode(CacheMode.IGNORE);//忽略第二级缓存
				session.setFlushMode(FlushMode.AUTO);//如果FlushMode是MANUAL或NEVEL,在操作过程中hibernate会将事务设置为readonly
				
				String userId = (String) request.getSession().getAttribute(
						SystemConst.ACCOUNT_ID);
				//分公司ID
				String companyId = (String) request.getSession().getAttribute(
						SystemConst.ACCOUNT_COMPANYID);
				int agency=3;
				if(paytxtFiles!=null){
					 String fileName=null;
					 String fileNamePrefix=null;
					 String fileNameSuffix=null;
					 //String trueFileNameLike=null;//去掉批次的文件名
					 String newFileName=null;//最新的文件名
					 BufferedReader br = null;
						Map<String,Object> map=new HashMap<String,Object>();
					     int totalnum = 0; //记录总共上传的记录数
					     List tradeUnSuccessfulData; //收失败的记录
					     String traderecord = null;
					     Paytxt paytxt=null;
					     long recordNumY=0;//已划款户数
						 double feeSumY=0d;//已划款金额
						 String recordNumYStr=null;//已划款户数
						 String feeSumYStr=null;//已划款金额
					 //存放上传文件的结果
					 //交易文件
					     int lineIndex=0;//每个文件的第n行
					for (MultipartFile paytxtFile : paytxtFiles) {
						lineIndex=0;
						 if(paytxtFile.getSize()>10000000){
							    flag = false;
								msg = "上传失败：文件大小不能超过10M!";
								break;
				                //throw new Exception("上传失败：文件大小不能超过10M");            
				          }else{
				        	  fileName=paytxtFile.getOriginalFilename();
				        	  fileNamePrefix=fileName.substring(0,fileName.lastIndexOf("."));
				        	  fileNameSuffix=fileName.substring(fileName.lastIndexOf("."));
				        	  //提回文件
				        	  if(SystemConst.PAY_TXT_TYPE.get(10).equalsIgnoreCase(fileNameSuffix)){
				        		long subcoNo=Long.valueOf(companyId);
				  				map.put("subcoNo", subcoNo);
				  				map.put("agency", agency);
				  				//托收返回码
				  				map.put("notPayTag", SystemConst.ZERO2);
				  				
				  				/*　　　00	交易成功
				  					　　　01	查询发卡方
				  					　　　03	无效商户
				  					　　　04	问题卡
				  					　　　05	交易拒绝
				  					　　　12	无效交易
				  					　　　14	无效卡号
				  					15	读卡错误 ...*/
				  				if(fileNamePrefix.charAt(15)=='p' || fileNamePrefix.charAt(15)=='P'){
				  					fileNamePrefix=fileNamePrefix.replace("P", "Q").replace("p", "Q");
				  					
				  				//判断是否有需要提回的托收文件记录
									map.put("txtName", fileNamePrefix);
									
									//判断是否是最新的文件
									newFileName=getMaxTxtName(map);
									if(newFileName!=null && newFileName.equals(fileNamePrefix)){
										Map<String,Object> mapZj=getZjPaytxts(map);
										if(Utils.isNotNullOrEmpty(mapZj.get("feeSum"))){//需要
											lineIndex=0;
											totalnum=0;
											recordNumY=0;//已划款户数
											feeSumY=0d;//已划款金额
											recordNumYStr=null;//已划款户数
											feeSumYStr=null;//已划款金额
											
											//判断已划款户数、已划款金额是否一致
											isr=new InputStreamReader(paytxtFile.getInputStream());
											br = new BufferedReader(isr);
											Long opId=Long.valueOf(userId);
											//删除中间表相关数据
											paytxtDao.deleteTmpTable(session, subcoNo, fileNamePrefix,agency);
											session.flush();
											session.clear();
								            tx.commit();
								            tx=session.beginTransaction();
								            
								            List<Paytxt> paytxts=new ArrayList<Paytxt>();
											tradeUnSuccessfulData = new ArrayList(); //托收失败的记录
											 while ((traderecord = br.readLine()) != null) {
								                	if (traderecord.getBytes(FormatUtil.GBK).length == 188) {
								                		lineIndex++;
								                		totalnum++; //计算上传的总数
								                		paytxt=new Paytxt();
								                		paytxt.setAgency(agency);
								                		paytxt.setOpId(opId);
								                		paytxt.dealFileLine2(traderecord);
								                		
								                		paytxts.add(paytxt);
								                		 //已划款户数、已划款金额
									                    if(SystemConst.ZERO2.equals(paytxt.getPayTag())){
															 feeSumY+=paytxt.getFeeSum();
															 recordNumY+=1;
														 }else{
															 tradeUnSuccessfulData.add("第"+lineIndex+"行："+paytxt.getAccountNo());
														 }
								                	 }else if(traderecord.getBytes(FormatUtil.GBK).length == 48){//尾记录
							                        	 //判断总结文件 成功扣款金额、成功扣款笔数
								                    	if(StringUtils.isNotBlank(traderecord)) {
								                    		if(traderecord.length()>47){
								                    			recordNumYStr=StringUtils.getSubStrByByte(traderecord, 36, 48,FormatUtil.GBK).trim();
								                    			feeSumYStr=StringUtils.getSubStrByByte(traderecord, 24, 36,FormatUtil.GBK).trim();
								                    			DecimalFormat df = new DecimalFormat("0");
											                	String zjFeeSum=df.format((Utils.isNullOrEmpty(feeSumYStr)?0f:Double.valueOf(FormatUtil.tructZeroBefore(feeSumYStr))/100));
												                String jyFeeSum=df.format(feeSumY);
								                    			if(recordNumY!=(Utils.isNullOrEmpty(recordNumYStr)?0:Long.valueOf(((recordNumYStr))))){
								        							throw new RuntimeException("上传的提回文件：  " + fileNamePrefix.replace("Q", "P") + "中成功扣款笔数对不上，上传失败！");
								        							
								        						} else if(!jyFeeSum.equals(zjFeeSum)){
								        							throw new RuntimeException("上传的提回文件：  " + fileNamePrefix.replace("Q", "P") + "中成功扣款金额对不上，上传失败！");
								        						}
									                    	}else{
									                    		throw new RuntimeException("上传的提回文件：  " + fileNamePrefix.replace("Q", "P") + "中成功扣款笔数、成功扣款金额对不上，上传失败！");
									                    	}
								                    	}
	
								                    }
								                	
								                	
											 }
											 
											//将paytxt记录插入到中间表
							                	paytxtDao.addTmpTable(session, paytxts, subcoNo,fileNamePrefix , agency);
							                	
							                	session.flush();
												session.clear();
									            tx.commit();
									            tx=session.beginTransaction();
									            
									            //调用存储过程
									            updatePaytxts(session, subcoNo, agency, fileNamePrefix, opId);
									            
									            
									            info.append("上传的提回文件：  " + fileNamePrefix.replace("Q", "P") + " 中，");
									              
								                info.append("写入数据库的记录共 " + totalnum + " 条");
								                int tradeUnSuccessfulSize=tradeUnSuccessfulData.size();
								                //如果有扣款失败的记录
								                if(tradeUnSuccessfulSize>0){
								                	 info.append(",扣款失败的记录共 " + tradeUnSuccessfulSize + " 条");
							                		 info.append("，扣款失败的银行账号分别如下：</br>");
							                		 for (int j = 0; j < tradeUnSuccessfulSize; j++) {
							                			 info.append(tradeUnSuccessfulData.get(j).toString());
							                			 info.append("</br>");
							                		 }
								                 }
					                        	 info.append("</br>");
						  				}else{
											 info.append("上传的提回文件：  ").append( fileNamePrefix).append("中（上传失败），没有找到需要更新的记录！</br>");
										}
										}else{
										info.append("上传的提回文件：  ").append( fileNamePrefix).append("上传失败，不是最新的托收文件，最新的托收文件名为【").append(newFileName).append("】！</br>");
									}
				  				}else{
									 info.append("上传的提回文件：  ").append( fileNamePrefix).append("，文件名不正确！</br>");
								}
				  			
				                 
				        	  }
				        	  
				          }
						 
					}
					
					
					if(info.length()<1){
						info.append("上传文件失败，请检查是否选择了格式正确的提回文件！");
					}
					 session.flush();
					 session.clear();
		             tx.commit();
					session.close();
					
					msg=info.toString();
					out = response.getOutputStream();
		            String str = "<script>parent.callback('"+msg+"',"+flag+");</script>";
		            out.write(str.getBytes("UTF-8"));
		            out.flush();
				}else{
					flag = false;
					msg = "上传文件失败，请检查是否选择了匹配的交易文件和总结文件！";
				}
				
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				flag = false;
				msg = SystemConst.OP_FAILURE;
				 //回滚事务
				tx.rollback();
				throw new Exception(e.getMessage());
			} finally {
				  if (session != null) {
					    if (session.isOpen()) {
					     //关闭session
					     session.close();
					    }
					   }
				 if(out != null){
		                try{out.close();}catch(Exception e1){
		                	e1.printStackTrace();
		                }
		             }
				 if(isr != null){
		                try{isr.close();}catch(Exception e1){
		                	e1.printStackTrace();
		                }
		             }
			}
		}

		@Override
		public List<Paytxt> findPaytxts(Map<String, Object> conditionMap,
				int pageNo, int pageSize) throws SystemException {
			return paytxtDao.findPaytxts(null,conditionMap, pageNo, pageSize);
		}

		@Override
		public String updatePaytxts(Session session,final long subcoNo,final int agency,final String fileName,final long opId) throws SystemException {
			 session.doWork(new Work() {
					@Override
					public void execute(Connection connection) throws SQLException {
						CallableStatement cs=connection.prepareCall("{call p_upload_paytxt(?,?,?,?,?)}");
						cs.setLong(1, subcoNo);
						cs.setInt(2, agency);
						cs.setString(3, fileName);
						cs.setLong(4, opId);
						cs.setString(5, "");
						cs.execute();
					}
					
				}); 
			return "";
		}

		@Override
		public Page<Map<String, Object>> findPaytxtDtPage(
				PageSelect<Map<String, Object>> pageSelect)
				throws SystemException {
			List<Map<String, Object>> dataList=paytxtDao.findPaytxtsDt(pageSelect.getFilter(), pageSelect.getOrder(), pageSelect.getIs_desc(), pageSelect.getPageNo(), pageSelect.getPageSize());
			int total=paytxtDao.countPaytxtsDt(pageSelect.getFilter());
			return PageUtil.getPage(total, pageSelect.getPageNo(), dataList, pageSelect.getPageSize());
		}

		@Override
		public List<Map<String, Object>> findAllPaytxtDt(Map<String, Object> map)
				throws SystemException {
			return paytxtDao.findPaytxtsDt(map, null, false, 0, 0);
		}
}

