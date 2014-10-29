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
 * @ClassName:Payment
 * @Description:缴费记录明细表 的实体类
 * @author:zfy
 * @date:2014-5-27 下午2:21:55
 */
@Entity
@Table(name = "t_fee_payment_dt")
public class PaymentDt extends BaseEntity implements Cloneable{
	private static final Long serialVersionUID = 1L;
	private Long paymentSubId;//缴费明细ID
	private Long paymentId;//缴费总表ID
	private Long subcoNo;//分公司ID
	private Long customerId;//客户ID
	private Long orgId;//营业处组织机构ID, 现金缴费, 托收可为0
	private Long vehicleId;//车辆ID
	private Long unitId;//车台ID
	private Integer feetypeId;//计费项目类型, 系统值3100, 1=服务费, 2=SIM卡, 3=保险, 4=网上查车 
	private Integer payModel;//缴费方式, 系统值3050, 0=托收, 1=现金, 2=转账, 3=刷卡
	private Long collectionId;//'托收资料ID',
	private Long itemId;//项目ID
	private String itemsId;//服务项串, 界面显示用
	private String itemName;//缴费名称, 电子转账可能有缴费名称
	private Date SDate;//开始日期, 如采用余额方式, 无效
	private Date EDate;//结束日期, 如采用余额方式, 无效
	private Integer SMonths;//服务时长(月), 如采用余额方式, 无效
	private Integer SDays;//服务时长(天), 为欠费停机拆机补费时使用
	private Float acAmount;//应收费用总额, 现金缴费, 促销活动
	private Float realAmount;//实收金额
	private String bwNo;//收据单号/交易流水号
	private Long agentId;//经办人, 现金
	private String agentName;//经办人姓名,  现金
	private Date payTime;//缴费时间=托收扣款时间
	private Date stamp;//时间戳
	private Long opId;//操作员id
	private String remark;//备注
	private Integer isInvoice;//是否已开发票, 1=是, 0=否
	private Integer printNum;//发票打印次数
	private Date printTime;//发票打印时间
	private Integer isAudited;//是否审核, 1=是, 0=否
	private String auditor;//审核人
	private Date auditedTime;//审核时间

	public PaymentDt() {
	}
	@Column(name = "payment_id")
	public Long getPaymentId() {
		return this.paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
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
	@Column(name = "org_id")
	public Long getOrgId() {
		return this.orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
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
	@Column(name = "pay_model")
	public Integer getPayModel() {
		return this.payModel;
	}

	public void setPayModel(Integer payModel) {
		this.payModel = payModel;
	}
	@Column(name = "item_id")
	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	@Column(name = "item_name")
	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
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
	@Column(name = "s_months")
	public Integer getSMonths() {
		return this.SMonths;
	}

	public void setSMonths(Integer SMonths) {
		this.SMonths = SMonths;
	}
	@Column(name = "agent_id")
	public Long getAgentId() {
		return this.agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}
	@Column(name = "agent_name")
	public String getAgentName() {
		return this.agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
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
	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name = "is_invoice")
	public Integer getIsInvoice() {
		return this.isInvoice;
	}

	public void setIsInvoice(Integer isInvoice) {
		this.isInvoice = isInvoice;
	}

	@Column(name = "auditor")
	public String getAuditor() {
		return this.auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	@Column(name = "unit_id")
	public Long getUnitId() {
		return unitId;
	}


	public void setUnitId(Long unitId) {
		this.unitId = unitId;
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

	@Column(name = "bw_no")
	public String getBwNo() {
		return bwNo;
	}


	public void setBwNo(String bwNo) {
		this.bwNo = bwNo;
	}

	@Column(name = "print_num")
	public Integer getPrintNum() {
		return printNum;
	}


	public void setPrintNum(Integer printNum) {
		this.printNum = printNum;
	}

	@Column(name = "print_time")
	public Date getPrintTime() {
		return printTime;
	}


	public void setPrintTime(Date printTime) {
		this.printTime = printTime;
	}

	@Column(name = "is_audited")
	public Integer getIsAudited() {
		return isAudited;
	}


	public void setIsAudited(Integer isAudited) {
		this.isAudited = isAudited;
	}

	@Column(name = "audited_time")
	public Date getAuditedTime() {
		return auditedTime;
	}


	public void setAuditedTime(Date auditedTime) {
		this.auditedTime = auditedTime;
	}
	@Column(name = "pay_time",nullable=false,updatable=false,insertable=true)
    @Temporal(TemporalType.TIMESTAMP)
	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	@Column(name = "items_id")
	public String getItemsId() {
		return itemsId;
	}

	public void setItemsId(String itemsId) {
		this.itemsId = itemsId;
	}
	@Column(name = "s_days")
	public Integer getSDays() {
		return SDays;
	}

	public void setSDays(Integer sDays) {
		SDays = sDays;
	}
	@Id
	@Column(name = "payment_sub_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)//可以用increment，或者seqence(oracle),identity(mysql,ms sql)
	public Long getPaymentSubId() {
		return paymentSubId;
	}

	public void setPaymentSubId(Long paymentSubId) {
		this.paymentSubId = paymentSubId;
	}
	@Column(name = "collection_id")
	public Long getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
	}
	public Object clone(){ 
		Payment o = null; 
		try{ 
		o = (Payment)super.clone(); 
		}catch(CloneNotSupportedException e){ 
		e.printStackTrace(); 
		} 
		return o; 
	} 
}
