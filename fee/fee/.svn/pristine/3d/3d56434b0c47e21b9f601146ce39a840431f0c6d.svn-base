package com.chinagps.fee.entity.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Package:com.chinagps.fee.entity.po
 * @ClassName:Print
 * @Description:打印发票记录表 的实体类
 * @author:zfy
 * @date:2014-7-31 下午2:39:30
 */
@Entity
@Table(name = "t_fee_subco")
public class Print  extends BaseEntity{
	private Long printId; //打印记录ID
	private Long paymentId; //缴费记录ID
	  private Long subcoNo; //分公司ID
	  private String printTime; //发票打印时间
	  private String addressee; //发票收件人
	  private String address; //地址
	  private String postCode; //邮政编码
	  private Date stamp; //时间戳
	  private Long opId; //操作员id
	  private String remark; //备注
	  
	@Id
	@Column(name = "print_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)//可以用increment，或者seqence(oracle),identity(mysql,ms sql)
	public Long getPrintId() {
		return printId;
	}
	public void setPrintId(Long printId) {
		this.printId = printId;
	}
	@Column(name = "subco_no")
	public Long getSubcoNo() {
		return subcoNo;
	}
	public void setSubcoNo(Long subcoNo) {
		this.subcoNo = subcoNo;
	}
	@Column(name = "payment_id")
	public Long getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}
	@Column(name = "print_time")
	public String getPrintTime() {
		return printTime;
	}
	public void setPrintTime(String printTime) {
		this.printTime = printTime;
	}
	@Column(name = "addressee")
	public String getAddressee() {
		return addressee;
	}
	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}
	@Column(name = "address")
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Column(name = "post_code")
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	@Column(name = "stamp")
	public Date getStamp() {
		return stamp;
	}
	public void setStamp(Date stamp) {
		this.stamp = stamp;
	}
	@Column(name = "op_id")
	public Long getOpId() {
		return opId;
	}
	public void setOpId(Long opId) {
		this.opId = opId;
	}
	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	  
}

