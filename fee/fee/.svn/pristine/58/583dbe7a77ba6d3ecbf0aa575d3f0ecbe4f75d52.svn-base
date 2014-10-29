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

/**
 * @Package:com.chinagps.fee.entity.po
 * @ClassName:PaymentItem
 * @Description:缴费项目分表 的实体类
 * @author:zfy
 * @date:2014-5-27 下午2:21:55
 */
@Entity
@Table(name = "t_fee_payment_item")
public class PaymentItem extends BaseEntity{
	private static final Long serialVersionUID = 1L;
	private Long paymentIemId;//缴费项目分表ID
	private Long paymentId;//缴费总表ID
	private Long subcoNo;//分公司ID
	private Long customerId;//客户ID
	private Integer feetypeId;//计费项目类型, 系统值3100, 1=服务费, 2=SIM卡, 3=保险, 4=网上查车 
	private Float acAmount;//应收费用总额, 现金缴费, 促销活动
	private Float realAmount;//实收金额
	private Date stamp;//时间戳
	private Long opId;//操作员id
	@Id
	@Column(name = "payment_item_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)//可以用increment，或者seqence(oracle),identity(mysql,ms sql)
	public Long getPaymentIemId() {
		return paymentIemId;
	}
	public void setPaymentIemId(Long paymentIemId) {
		this.paymentIemId = paymentIemId;
	}
	@Column(name = "payment_id")
	public Long getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}
	@Column(name = "subco_no")
	public Long getSubcoNo() {
		return subcoNo;
	}
	public void setSubcoNo(Long subcoNo) {
		this.subcoNo = subcoNo;
	}
	@Column(name = "customer_id")
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	@Column(name = "feetype_id")
	public Integer getFeetypeId() {
		return feetypeId;
	}
	public void setFeetypeId(Integer feetypeId) {
		this.feetypeId = feetypeId;
	}
	@Column(name = "ac_amount")
	public Float getAcAmount() {
		return acAmount;
	}
	public void setAcAmount(Float acAmount) {
		this.acAmount = acAmount;
	}
	@Column(name = "real_amount")
	public Float getRealAmount() {
		return realAmount;
	}
	public void setRealAmount(Float realAmount) {
		this.realAmount = realAmount;
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
	
}
