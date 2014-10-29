package com.chinagps.fee.entity.po;


import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Package:com.chinagps.fee.entity.po
 * @ClassName:Bill
 * @Description:计费账单表 的实体类
 * @author:zfy
 * @date:2014-5-20 下午6:54:33
 */
@Entity
@Table(name = "t_fee_bill")
public class Bill extends BaseEntity{
	private static final Long serialVersionUID = 1L;
	private Long billId;//账单ID
	private Long subcoNo;//分公司ID
	private Long customerId;//客户ID
	private Long vehicleId;//车辆ID
	private Long unitId;//车台ID
	private Integer feetypeId;//计费类型类型, 1=服务费, 2=SIM卡, 3=保险 , 4=网上查车 
	private Long collectionId;//'托收资料ID',
	private Long itemId;//项目ID
	private String itemsId;//服务项串, 界面显示用
	private Date SDate;//开始日期
	private Date EDate;//结束日期
	private Date CDate;//记账日期
	private Float amount;//费用总额
	private String payCtNo;//合同号*
	private Date stamp;//时间戳
	private Long opId;//操作员id
	private Integer flag;//计费标志, 0=未付费, 1=付费成功
	private Date feeDate;//扣款时间, 系统扣款成功后更新
	private Integer paytxtId;//托收文件ID, 为了销账

	public Bill() {
	}

	public Bill(Long billId, Long vehicleId, Integer feetypeId, Long itemId,
			Float amount, String payCtNo, Date stamp, Date feeDate) {
		this.billId = billId;
		this.vehicleId = vehicleId;
		this.feetypeId = feetypeId;
		this.itemId = itemId;
		this.amount = amount;
		this.payCtNo = payCtNo;
		this.stamp = stamp;
		this.feeDate = feeDate;
	}

	public Bill(Long billId, Long subcoNo, Long customerId, Long vehicleId,
			Integer feetypeId, Long itemId, Date SDate, Date EDate, Date CDate,
			Float amount, String payCtNo, Date stamp, Long opId,
			Integer flag, Date feeDate, Integer paytxtId) {
		this.billId = billId;
		this.subcoNo = subcoNo;
		this.customerId = customerId;
		this.vehicleId = vehicleId;
		this.feetypeId = feetypeId;
		this.itemId = itemId;
		this.SDate = SDate;
		this.EDate = EDate;
		this.CDate = CDate;
		this.amount = amount;
		this.payCtNo = payCtNo;
		this.stamp = stamp;
		this.opId = opId;
		this.flag = flag;
		this.feeDate = feeDate;
		this.paytxtId = paytxtId;
	}
	@Id
	@Column(name = "bill_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)//可以用increment，或者seqence(oracle),identity(mysql,ms sql)
	public Long getBillId() {
		return this.billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
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
	@Column(name = "s_date")
	public Date getSDate() {
		return this.SDate;
	}

	public void setSDate(Date SDate) {
		this.SDate = SDate;
	}
	@Column(name = "e_date")
	public Date getEDate() {
		return this.EDate;
	}

	public void setEDate(Date EDate) {
		this.EDate = EDate;
	}
	@Column(name = "c_date")
	public Date getCDate() {
		return this.CDate;
	}

	public void setCDate(Date CDate) {
		this.CDate = CDate;
	}
	@Column(name = "amount")
	public Float getAmount() {
		return this.amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}
	@Column(name = "stamp")
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
	@Column(name = "flag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	@Column(name = "fee_date")
	public Date getFeeDate() {
		return this.feeDate;
	}

	public void setFeeDate(Date feeDate) {
		this.feeDate = feeDate;
	}
	@Column(name = "paytxt_id")
	public Integer getPaytxtId() {
		return this.paytxtId;
	}

	public void setPaytxtId(Integer paytxtId) {
		this.paytxtId = paytxtId;
	}
	@Column(name = "unit_id")
	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	@Column(name = "pay_ct_no")
	public String getPayCtNo() {
		return payCtNo;
	}

	public void setPayCtNo(String payCtNo) {
		this.payCtNo = payCtNo;
	}
	@Column(name = "items_id")
	public String getItemsId() {
		return itemsId;
	}

	public void setItemsId(String itemsId) {
		this.itemsId = itemsId;
	}
	@Column(name = "collection_id")
	public Long getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
	}
	
}
