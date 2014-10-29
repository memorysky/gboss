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
 * @ClassName:Info
 * @Description:计费信息表 的实体类
 * @author:zfy
 * @date:2014-5-20 下午6:41:23
 */
@Entity
@Table(name = "t_fee_info")
public class Info extends BaseEntity{
	private static final Long serialVersionUID = 1L;
	private Long feeId;//
	private Long subcoNo;//分公司ID
	private Long customerId;//客户ID, 对应是需要付账的客户
	private Long vehicleId;//车辆ID
	private Long unitId;//车台ID
	private Integer feetypeId;//计费类型, 1=服务费, 2=SIM卡, 3=保险 , 4=网上查车 
	private Integer payModel;//缴费方式, 系统值3050, 0=托收, 1=现金, 2=转账, 3=刷卡
	private Long itemId;//套餐ID, pack_id, 预留
	private String itemsId;//服务项串, 界面显示用
	private Float monthFee;//每月费用(元)
	private Float acAmount;//费用总额
	private String payCtNo;//合同号
	private Float realAmount;//实收金额
	private Date feeDate;//收费开始时间
	private Date feeSedate;//服务截止日期
	private Integer feeCycle;//收费周期(月)
	private Long opId;//操作员id
	private Date stamp;//时间戳

	public Info() {
	}

	public Info(Long feeId, Long vehicleId, Integer feetypeId, Long itemId,
			Float acAmount, String payCtNo, Float realAmount, Date stamp) {
		this.feeId = feeId;
		this.vehicleId = vehicleId;
		this.feetypeId = feetypeId;
		this.itemId = itemId;
		this.acAmount = acAmount;
		this.payCtNo = payCtNo;
		this.realAmount = realAmount;
		this.stamp = stamp;
	}

	public Info(Long feeId, Long subcoNo, Long customerId, Long vehicleId,
			Integer feetypeId, Long itemId, Float acAmount, String payCtNo,
			Float realAmount, Date stamp, Long opId, Date feeDate, Integer feeCycle) {
		this.feeId = feeId;
		this.subcoNo = subcoNo;
		this.customerId = customerId;
		this.vehicleId = vehicleId;
		this.feetypeId = feetypeId;
		this.itemId = itemId;
		this.acAmount = acAmount;
		this.payCtNo = payCtNo;
		this.realAmount = realAmount;
		this.stamp = stamp;
		this.opId = opId;
		this.feeDate = feeDate;
		this.feeCycle = feeCycle;
	}
	@Id
	@Column(name = "fee_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)//可以用increment，或者seqence(oracle),identity(mysql,ms sql)
	public Long getFeeId() {
		return this.feeId;
	}

	public void setFeeId(Long feeId) {
		this.feeId = feeId;
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
	@Column(name = "vehicle_id")
	public Long getVehicleId() {
		return this.vehicleId;
	}

	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	@Column(name = "feetype_id")
	public Integer getFeetypeId() {
		return this.feetypeId;
	}

	public void setFeetypeId(Integer feetypeId) {
		this.feetypeId = feetypeId;
	}
	@Column(name = "item_id")
	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
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
	@Column(name = "fee_date")
	public Date getFeeDate() {
		return this.feeDate;
	}

	public void setFeeDate(Date feeDate) {
		this.feeDate = feeDate;
	}
	@Column(name = "fee_cycle")
	public Integer getFeeCycle() {
		return this.feeCycle;
	}

	public void setFeeCycle(Integer feeCycle) {
		this.feeCycle = feeCycle;
	}
	@Column(name = "ac_amount")
	public Float getAcAmount() {
		return acAmount;
	}

	public void setAcAmount(Float acAmount) {
		this.acAmount = acAmount;
	}
	@Column(name = "pay_ct_no")
	public String getPayCtNo() {
		return payCtNo;
	}

	public void setPayCtNo(String payCtNo) {
		this.payCtNo = payCtNo;
	}
	@Column(name = "real_amount")
	public Float getRealAmount() {
		return realAmount;
	}

	public void setRealAmount(Float realAmount) {
		this.realAmount = realAmount;
	}
	@Column(name = "unit_id")
	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	@Column(name = "items_id")
	public String getItemsId() {
		return itemsId;
	}

	public void setItemsId(String itemsId) {
		this.itemsId = itemsId;
	}
	@Column(name = "pay_model")
	public Integer getPayModel() {
		return payModel;
	}

	public void setPayModel(Integer payModel) {
		this.payModel = payModel;
	}
	@Column(name = "month_fee")
	public Float getMonthFee() {
		return monthFee;
	}

	public void setMonthFee(Float monthFee) {
		this.monthFee = monthFee;
	}
	@Column(name = "fee_sedate")
	public Date getFeeSedate() {
		return feeSedate;
	}

	public void setFeeSedate(Date feeSedate) {
		this.feeSedate = feeSedate;
	}
	
	
}
