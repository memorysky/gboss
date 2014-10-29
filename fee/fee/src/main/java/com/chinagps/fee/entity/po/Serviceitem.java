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
 * @Package:com.chinagps.fee.entity.po.sel
 * @ClassName:Serviceitem
 * @Description:服务项表 的实体类
 * @author:zfy
 * @date:2014-5-20 下午2:32:02
 */
@Entity
@Table(name = "t_fee_service_item")
public class Serviceitem extends BaseEntity{
	private static final long serialVersionUID = 1L;
	private Long itemId;//服务项ID
	private String itemCode;//服务项编号
	private String itemName;//服务项名称
	private Integer feetypeId;//计费类型, 1=服务费, 2=SIM卡, 3=保险 , 4=网上查车 
	private Integer flag;//是否对新用户有效, 1=是, 0=否
	private Float price;//价格
	private Long opId;//操作员ID
	private Date stamp;//操作时间
	private String remark;//服务项备注

	public Serviceitem() {
	}

	public Serviceitem(Date stamp) {
		this.stamp = stamp;
	}

	@Id
	@Column(name = "item_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)//可以用increment，或者seqence(oracle),identity(mysql,ms sql)
	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	@Column(name = "price")
	public Float getPrice() {
		return this.price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}
	@Column(name = "stamp",nullable=false,updatable=true,insertable=true)
    @Temporal(TemporalType.TIMESTAMP)
	public Date getStamp() {
		return this.stamp;
	}

	public void setStamp(Date stamp) {
		this.stamp = stamp;
	}
	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name = "item_code")
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	@Column(name = "item_name")
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	@Column(name = "flag")
	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	@Column(name = "op_id")
	public Long getOpId() {
		return opId;
	}

	public void setOpId(Long opId) {
		this.opId = opId;
	}
	@Column(name = "feetype_id")
	public Integer getFeetypeId() {
		return this.feetypeId;
	}

	public void setFeetypeId(Integer feetypeId) {
		this.feetypeId = feetypeId;
	}
}
