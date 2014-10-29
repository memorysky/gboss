package com.chinagps.fee.entity.po;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import sun.nio.cs.ext.GBK;

import com.chinagps.fee.comm.SystemConst;
import com.chinagps.fee.controller.SysController;
import com.chinagps.fee.util.FormatUtil;
import com.chinagps.fee.util.StringUtils;
import com.chinagps.fee.util.Utils;
import com.chinagps.fee.util.Validation;

/**
 * @Package:com.chinagps.fee.entity.po
 * @ClassName:Paytxt
 * @Description:托收文件记录表的实体类
 * @author:zfy
 * @date:2014-5-21 下午4:04:08
 */
@Entity
@Table(name = "t_fee_paytxt")
@SelectBeforeUpdate(value=true)
@DynamicUpdate(value=true)
@DynamicInsert(value=true)
public class Paytxt extends BaseEntity{
	private static final Long serialVersionUID = 1L;
	private Long paytxtId;//托收记录ID
	private Long subcoNo;//分公司ID
	private Long customerId;//客户ID/用户ID
	private String txtName;//托收文件名
	private Integer agency;//托收机构ID, 1=银盛, 2=金融中心(默认值),3=中国银联
	private Integer accountType;//银行帐号类型, 0=银行卡号, 1=存折号,2=信用卡
	private String bankNo;//银行编号
	private String custName;//银行帐户名
	private String idcard;//银行帐号对应户主的身份证号码
	private String accountNo;//银行账号
	private String contractNo;//合同号
	private String payContractNo;//托收合同号
	private Integer recordNum;//记录数
	private Float feeSum;//费用总额
	private Date stamp;//时间戳
	private Long opId;//操作员
	//private Character payTag;//托收返回码
	private String payTag;//托收返回码
	private Date payTime;//托收返回时间

	
	private String feeSumStr;//费用总额，用于生成托收文件
	private long recordIndex;//流水号，在同一批扣文件中唯一，用于生成银联托收文件
	
	public Paytxt() {
	}

	public Paytxt(Long paytxtId, String txtName, Integer agency,
			Integer accountType, String bankNo, String accountNo,
			String contractNo, String payContractNo, Integer recordNum,
			Float feeSum, Date stamp, Date payTime) {
		this.paytxtId = paytxtId;
		this.txtName = txtName;
		this.agency = agency;
		this.accountType = accountType;
		this.bankNo = bankNo;
		this.accountNo = accountNo;
		this.contractNo = contractNo;
		this.payContractNo = payContractNo;
		this.recordNum = recordNum;
		this.feeSum = feeSum;
		this.stamp = stamp;
		this.payTime = payTime;
	}

	public Paytxt(Long paytxtId, Long subcoNo, Long customerId,
			String txtName, Integer agency, Integer accountType, String bankNo,
			String custName, String idcard, String accountNo,
			String contractNo, String payContractNo, Integer recordNum,
			Float feeSum, Date stamp, Long opId, String payTag,
			Date payTime) {
		this.paytxtId = paytxtId;
		this.subcoNo = subcoNo;
		this.customerId = customerId;
		this.txtName = txtName;
		this.agency = agency;
		this.accountType = accountType;
		this.bankNo = bankNo;
		this.custName = custName;
		this.idcard = idcard;
		this.accountNo = accountNo;
		this.contractNo = contractNo;
		this.payContractNo = payContractNo;
		this.recordNum = recordNum;
		this.feeSum = feeSum;
		this.stamp = stamp;
		this.opId = opId;
		this.payTag = payTag;
		this.payTime = payTime;
	}
	@Id
	@Column(name = "paytxt_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)//可以用increment，或者seqence(oracle),identity(mysql,ms sql)
	public Long getPaytxtId() {
		return this.paytxtId;
	}

	public void setPaytxtId(Long paytxtId) {
		this.paytxtId = paytxtId;
	}
	@Column(name = "subco_no")
	public Long getSubcoNo() {
		return this.subcoNo;
	}

	public void setSubcoNo(Long subcoNo) {
		this.subcoNo = subcoNo;
	}
	@Column(name = "customer_id")
	public Long getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	@Column(name = "txt_name")
	public String getTxtName() {
		return this.txtName;
	}

	public void setTxtName(String txtName) {
		this.txtName = txtName;
	}
	@Column(name = "account_type")
	public Integer getAccountType() {
		return this.accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}
	@Column(name = "bank_no")
	public String getBankNo() {
		return this.bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
	@Column(name = "cust_name")
	public String getCustName() {
		return this.custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}
	@Column(name = "idcard")
	public String getIdcard() {
		return this.idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	@Column(name = "account_no")
	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	@Column(name = "contract_no")
	public String getContractNo() {
		return this.contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	@Column(name = "pay_contract_no")
	public String getPayContractNo() {
		return this.payContractNo;
	}

	public void setPayContractNo(String payContractNo) {
		this.payContractNo = payContractNo;
	}
	@Column(name = "record_num")
	public Integer getRecordNum() {
		return this.recordNum;
	}

	public void setRecordNum(Integer recordNum) {
		this.recordNum = recordNum;
	}
	@Column(name = "fee_sum")
	public Float getFeeSum() {
		return this.feeSum;
	}

	public void setFeeSum(Float feeSum) {
		this.feeSum = feeSum;
	}
	@Column(name = "stamp",nullable=false,updatable=true,insertable=true)
    @Temporal(TemporalType.TIMESTAMP)
	public Date getStamp() {
		return this.stamp;
	}

	public void setStamp(Date stamp) {
		this.stamp = stamp;
	}
	@Column(name = "op_id")
	public Long getOpId() {
		return this.opId;
	}

	public void setOpId(Long opId) {
		this.opId = opId;
	}
	@Column(name = "pay_tag")
	public String getPayTag() {
		return this.payTag;
	}

	public void setPayTag(String payTag) {
		this.payTag = payTag;
	}
	@Column(name = "pay_time")
	public Date getPayTime() {
		return this.payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	
	/**
	 * @Title:getFileLine
	 * @Description:用于金融中心和银盛
	 * @return
	 * @throws
	 */
	@Transient
	public String gainFileLine(boolean isLastLine){
		StringBuffer sb=new StringBuffer();
		 if (agency==1) {//银盛
			 if(StringUtils.isNotBlank(payContractNo)){
				 if ((payContractNo.length() >= 0) && (payContractNo.length() <= 18)) {
		              this.payContractNo =FormatUtil.rightAppendBlank(payContractNo, 18);
		          } else {
		              this.payContractNo = payContractNo.substring(0, 18);
		          } 
			 }else{
				 this.payContractNo =FormatUtil.rightAppendBlank("", 18); 
			 }
	         
	      }else if(agency==2){//金融中心
	    	  if(StringUtils.isNotBlank(payContractNo)){
		          if ((payContractNo.length() >= 0) && (payContractNo.length() <= 10)) {
		              this.payContractNo = FormatUtil.rightAppendBlank(payContractNo, 10);
		          } else {
		              this.payContractNo = payContractNo.substring(0, 10);
		          }
	    	  }else{
	    		  this.payContractNo =FormatUtil.rightAppendBlank("", 10); 
	    	  }
	      }

	      this.bankNo = FormatUtil.appendZero(bankNo);
	      if(StringUtils.isNotBlank(accountNo)){
		      if(accountNo.length()>0&&(accountNo.length()<=21)){
		    	  this.accountNo=FormatUtil.rightAppendBlank(accountNo,21);
		      }else {
		          this.accountNo = FormatUtil.rightAppendBlank(SystemConst.BLANK, 21);
		      }
	      }else{
	    	  this.accountNo = FormatUtil.rightAppendBlank(SystemConst.BLANK, 21);
	      }

	      this.feeSumStr=getFeeSumStr();
	      if(StringUtils.isNotBlank(feeSumStr)){
		      if((feeSumStr.length()>0)&&(String.valueOf(feeSumStr).length()<=12)) {
		          this.feeSumStr =FormatUtil.leftAppendZero(feeSumStr, 12);
		      }else {
		          this.feeSumStr = "000000000.00";
		      }
	      }

	      sb.append(payContractNo);
	      sb.append(bankNo);
	      sb.append(accountNo);
	      sb.append(feeSumStr);
	      sb.append("X");//托收标记
	      if(Utils.isLinux() && !isLastLine){
	    	  sb.append((char)13);//0D 0A 换行
	      }
		return sb.toString();
	}

	/**
	 * @Title:getFileLine2
	 * @Description:用于银联
	 * @return
	 * @throws
	 */
	@Transient
	public String gainFileLine2(){
		//如果不是银联
		if (agency!=3){
			 return null;
		}
		StringBuffer sb=new StringBuffer();
		//流水号 在同一批扣文件中唯一 6字节
		sb.append(FormatUtil.leftAppendZero(String.valueOf(recordIndex), 6));
		//银行代码 8字节
		sb.append(FormatUtil.rightAppendBlank(bankNo.trim(),8));
		//帐号 30 不足位左对齐右补空格
		sb.append(FormatUtil.rightAppendBlank(accountNo.trim(),30));
		//帐号类型
		sb.append(FormatUtil.rightAppendBlank(String.valueOf(accountType),1));
		//用户姓名
		sb.append(FormatUtil.rightAppendBlank(custName.trim(),10,FormatUtil.GBK));
		//身份证号
		sb.append(FormatUtil.rightAppendBlank(Utils.clearNull(idcard),19));
		this.feeSumStr=getFeeSumStr();
		if(Validation.isNumericalString(feeSumStr)){
			  feeSumStr = FormatUtil.getAppointedCurrencyFormat(feeSumStr,10,2).replaceAll("\\.","");
	      }else{
	    	  feeSumStr = FormatUtil.getAppointedCurrencyFormat("0",10,2).replaceAll("\\.","");
	      }
		//金额
		sb.append(feeSumStr);
		//响应码
		sb.append(FormatUtil.rightAppendBlank(SystemConst.BLANK,2));
		//保留域 ,目前保留域用来存储合同号
		sb.append(FormatUtil.rightAppendBlank(payContractNo.trim(),100));
		return sb.toString();
	}
	@Transient
	public String getFeeSumStr() {
		if(this.feeSum!=null){
			//小数点后2位
			this.feeSumStr=FormatUtil.floatToStr2Deci(this.feeSum.floatValue());
		}
		return feeSumStr;
	}

	public void setFeeSumStr(String feeSumStr) {
		this.feeSumStr = feeSumStr;
	}

	@Column(name="agency")
	public Integer getAgency() {
		return agency;
	}

	public void setAgency(Integer agency) {
		this.agency = agency;
	}

	@Transient
	public long getRecordIndex() {
		return recordIndex;
	}

	public void setRecordIndex(long recordIndex) {
		this.recordIndex = recordIndex;
	}
	
	//金融中心、银盛交易文件处理
	public void dealFileLine1(String line){
		 try {
			 String payTagStr=null;
			 char[] charArray=null;
			 if (agency==1) {//银盛
				 payContractNo = line.substring(0, 18).trim();
				 bankNo = line.substring(18, 20).trim();
				 accountNo = line.substring(20, 41).trim();
				 feeSumStr = line.substring(41, 53).trim();
				 if(StringUtils.isNotBlank(feeSumStr)){
					 feeSum=Float.valueOf(feeSumStr);
				 }
				 payTagStr = line.substring(53).trim();
				 charArray= payTagStr.toCharArray();
				 if(Utils.isNotNullOrEmpty(charArray)){
					 payTag=payTagStr;//charArray[0];
				 }
			 }else if(agency==2){//金融中心
				 payContractNo = line.substring(0, 10).trim();
				 bankNo = line.substring(10, 12).trim();
				 accountNo = line.substring(12, 33).trim();
				 feeSumStr = line.substring(33, 45).trim();
				 if(StringUtils.isNotBlank(feeSumStr)){
					 feeSum=Float.valueOf(feeSumStr);
				 }
				 payTagStr = line.substring(45).trim();
				 charArray= payTagStr.toCharArray();
				 if(Utils.isNotNullOrEmpty(charArray)){
					 payTag=payTagStr;//charArray[0];
				 }
	         }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	}
	
	//银联交易文件处理
		public void dealFileLine2(String traderecord){
			 try {
				 //银行代码
				 this.bankNo=StringUtils.getSubStrByByte(traderecord, 6, 14,FormatUtil.GBK).trim();
				 //帐号 30 不足位左对齐右补空格
				 this.accountNo=StringUtils.getSubStrByByte(traderecord, 14, 44,FormatUtil.GBK).trim();
				 this.accountType=Integer.parseInt(StringUtils.getSubStrByByte(traderecord, 44, 45,FormatUtil.GBK));
				//用户姓名
				 this.custName=StringUtils.getSubStrByByte(traderecord, 45, 55,FormatUtil.GBK).trim();
				 //身份证号
				 this.idcard=StringUtils.getSubStrByByte(traderecord, 55, 74,FormatUtil.GBK).trim();
				//扣款金额
				 this.feeSumStr= StringUtils.getSubStrByByte(traderecord, 74, 86,FormatUtil.GBK).trim();
				 if(StringUtils.isNotBlank(feeSumStr)){
					 this.feeSum=Float.valueOf(feeSumStr)/100;
				 }
				//响应码
				 this.payTag=StringUtils.getSubStrByByte(traderecord, 86, 88,FormatUtil.GBK).trim();
		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }
		}
}
