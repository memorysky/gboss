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

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

/**
 * @Package:com.chinagps.fee.entity.po
 * @ClassName:Reprintinvoice
 * @Description:补打发票登记表 的实体类
 * @author:zfy
 * @date:2014-6-5 上午10:33:12
 */
@Entity
@Table(name = "t_fee_reprintinvoice")
@SelectBeforeUpdate(value=true)
@DynamicUpdate(value=true)
@DynamicInsert(value=true)
public class Reprintinvoice extends BaseEntity{
	private static final Long serialVersionUID = 1L;
	private Long reprintId;//打印发票登记id
	private Long subcoNo;//分公司ID
	private Long customerId;//客户ID
	private Long vehicleId;//车辆ID
	private Long unitId;//车台ID
	private String callLetter;//呼号
	private Date stamp;//更新时间戳
	private Date printTime;//补打发票日期
	private Integer isDel;//是否删除, 0=未注销, 1=注销
	private Date delTime;//记录删除时间
	private Integer isPrinted;//是否打印, 0=未打印, 1=打印

	public Reprintinvoice() {
	}

	public Reprintinvoice(Long customerId, Long vehicleId, Date stamp,
			Date printTime, int isDel, Date delTime, int isPrinted) {
		this.customerId = customerId;
		this.vehicleId = vehicleId;
		this.stamp = stamp;
		this.printTime = printTime;
		this.isDel = isDel;
		this.delTime = delTime;
		this.isPrinted = isPrinted;
	}

	public Reprintinvoice(Long subcoNo, Long customerId, Long vehicleId,
			String callLetter, Date stamp, Date printTime, int isDel,
			Date delTime, int isPrinted) {
		this.subcoNo = subcoNo;
		this.customerId = customerId;
		this.vehicleId = vehicleId;
		this.callLetter = callLetter;
		this.stamp = stamp;
		this.printTime = printTime;
		this.isDel = isDel;
		this.delTime = delTime;
		this.isPrinted = isPrinted;
	}
	@Id
	@Column(name = "reprint_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)//可以用increment，或者seqence(oracle),identity(mysql,ms sql)
	public Long getReprintId() {
		return reprintId;
	}

	public void setReprintId(Long reprintId) {
		this.reprintId = reprintId;
	}
	@Column(name = "unit_id")
	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
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
	@Column(name = "call_letter")
	public String getCallLetter() {
		return this.callLetter;
	}

	public void setCallLetter(String callLetter) {
		this.callLetter = callLetter;
	}
	@Column(name = "stamp",nullable=false,updatable=false,insertable=true)
    @Temporal(TemporalType.TIMESTAMP)
	public Date getStamp() {
		return this.stamp;
	}

	public void setStamp(Date stamp) {
		this.stamp = stamp;
	}
	@Column(name = "print_time")
	public Date getPrintTime() {
		return this.printTime;
	}

	public void setPrintTime(Date printTime) {
		this.printTime = printTime;
	}
	@Column(name = "is_del")
	public Integer getIsDel() {
		return this.isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}
	@Column(name = "del_time")
	public Date getDelTime() {
		return this.delTime;
	}

	public void setDelTime(Date delTime) {
		this.delTime = delTime;
	}
	@Column(name = "is_printed")
	public Integer getIsPrinted() {
		return isPrinted;
	}

	public void setIsPrinted(Integer isPrinted) {
		this.isPrinted = isPrinted;
	}

}
