package com.chinagps.fee.entity.po;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 * @Package:com.chinagps.fee.entity.po.sel
 * @ClassName:Servicepack
 * @Description:套餐表 的实体类
 * @author:zfy
 * @date:2014-5-20 下午2:35:27
 */
@Entity
@Table(name = "t_fee_service_pack")
public class Servicepack  extends BaseEntity{
	private static final long serialVersionUID = 1L;
	private Long packId;//套餐ID
	private Long subcoNo;//分公司, 0=总部定义, 大于0=分公司自定义
	private String packCode;//套餐编号
	private String name;//套餐名称
	private Integer ismain;//是否主套餐
	private Float price;//价格
	private Integer flag;///是否新用户有效, 1=是, 0=否
	private Long orgId;//所属机构ID
	private Integer isChecked;//是否审核, 1=已审核, 0=未审核
	private Long chkOpId;//审核人ID
	private Date chkTime;//审核时间
	private String remark;//备注, 服务项描述
	private Long opId;//操作员ID
	private Date stamp;//操作时间
	
	private List<PackItem> packItems;//套餐服务项
	
	private List<Packprice> packprices;//套餐价格
	public Servicepack() {
	}

	@Id
	@Column(name = "pack_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)//可以用increment，或者seqence(oracle),identity(mysql,ms sql)
	public Long getPackId() {
		return this.packId;
	}

	public void setPackId(Long packId) {
		this.packId = packId;
	}
	@Column(name = "subco_no")
	public Long getSubcoNo() {
		return this.subcoNo;
	}

	public void setSubcoNo(Long subcoNo) {
		this.subcoNo = subcoNo;
	}
	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "ismain")
	public Integer getIsmain() {
		return this.ismain;
	}

	public void setIsmain(Integer ismain) {
		this.ismain = ismain;
	}
	@Column(name = "price")
	public Float getPrice() {
		return this.price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}
	@Column(name = "org_id")
	public Long getOrgId() {
		return this.orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name = "stamp",nullable=false,updatable=true,insertable=true)
    @Temporal(TemporalType.TIMESTAMP) 
	public Date getStamp() {
		return this.stamp;
	}

	public void setStamp(Date stamp) {
		this.stamp = stamp;
	}

	@Transient
	public List<PackItem> getPackItems() {
		return packItems;
	}

	public void setPackItems(List<PackItem> packItems) {
		this.packItems = packItems;
	}
	@Transient
	public List<Packprice> getPackprices() {
		return packprices;
	}

	public void setPackprices(List<Packprice> packprices) {
		this.packprices = packprices;
	}
	@Column(name = "pack_code")
	public String getPackCode() {
		return packCode;
	}

	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}
	@Column(name = "flag")
	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	@Column(name = "is_checked")
	public Integer getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(Integer isChecked) {
		this.isChecked = isChecked;
	}
	@Column(name = "chk_op_id")
	public Long getChkOpId() {
		return chkOpId;
	}

	public void setChkOpId(Long chkOpId) {
		this.chkOpId = chkOpId;
	}
	@Column(name = "chk_time")
	public Date getChkTime() {
		return chkTime;
	}

	public void setChkTime(Date chkTime) {
		this.chkTime = chkTime;
	}
	@Column(name = "op_id")
	public Long getOpId() {
		return opId;
	}

	public void setOpId(Long opId) {
		this.opId = opId;
	}
	
}
