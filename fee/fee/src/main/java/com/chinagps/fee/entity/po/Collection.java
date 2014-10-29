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

/**
 * @Package:com.chinagps.fee.entity.po
 * @ClassName:Collection
 * @Description:托收资料表的实体类
 * @author:zfy
 * @date:2014-6-4 下午2:28:16
 */
@Entity
@Table(name = "t_ba_collection")
@DynamicUpdate(value=true)
@DynamicInsert(value=true)
public class Collection extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long collectionId;//'托收资料ID',
	private Long subcoNo;//分公司ID
	private Long customerId;//'客户ID',
	private String payCtNo;//'托收合同号, 集团客户与归档合同号可能不同',
	private String acNo;//'银行帐号',
	private Integer acType;//'账号类型, 系统值3120, 0=借记卡, 1=存折, 2=信用卡',
	private String acName;//'账户名/托收用户名, 集客为公司名, 一般与客户名一致',
	private String acIdNo;//'账户名对应的身份证号码',
	private String bank;//'银行名称',
	private String bankCode;//'银行编号(与托收机构相关)',
	private Integer agency;//'托收机构, 系统值3000, 1=银盛, 2=金融中心, 3=银联', 
	private Integer feeCycle;//'收费周期(月)',
	private Integer isDelivery;//'发票是否投递, 系统值3110, 0=不投递, 1=平邮, 2=重点投递, 3=电子账单',
	private String addressee;//'发票收件人',
	private Integer printFreq;//'发票打印频率(月)',
	private String province;//'投递地址省',
	private String city;//'投递地址市',
	private String area;//'投递地址县/区',
	private String address;//'具体地址(街道门牌号码)',
	private String postCode;//'邮政编码',
	private String tel;//联系电话
	private String transactor;//'客户经办人(客户委托人)',
	private Date createDate;//'录入时间, 第一次记录',
	private Long opId;//'操作员ID',
	private Date stamp;//'操作时间',
	
	@Id
	@Column(name = "collection_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)//可以用increment，或者seqence(oracle),identity(mysql,ms sql)
	public Long getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
	}
	@Column(name = "customer_id")
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	@Column(name = "pay_ct_no")
	public String getPayCtNo() {
		return payCtNo;
	}
	public void setPayCtNo(String payCtNo) {
		this.payCtNo = payCtNo;
	}
	@Column(name = "ac_no")
	public String getAcNo() {
		return acNo;
	}
	public void setAcNo(String acNo) {
		this.acNo = acNo;
	}
	@Column(name = "ac_type")
	public Integer getAcType() {
		return acType;
	}
	public void setAcType(Integer acType) {
		this.acType = acType;
	}
	@Column(name = "ac_name")
	public String getAcName() {
		return acName;
	}
	public void setAcName(String acName) {
		this.acName = acName;
	}
	@Column(name = "ac_id_no")
	public String getAcIdNo() {
		return acIdNo;
	}
	public void setAcIdNo(String acIdNo) {
		this.acIdNo = acIdNo;
	}
	@Column(name = "bank")
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	@Column(name = "bank_code")
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	@Column(name = "agency")
	public Integer getAgency() {
		return agency;
	}
	public void setAgency(Integer agency) {
		this.agency = agency;
	}
	@Column(name = "fee_cycle")
	public Integer getFeeCycle() {
		return feeCycle;
	}
	public void setFeeCycle(Integer feeCycle) {
		this.feeCycle = feeCycle;
	}
	@Column(name = "is_delivery")
	public Integer getIsDelivery() {
		return isDelivery;
	}
	public void setIsDelivery(Integer isDelivery) {
		this.isDelivery = isDelivery;
	}
	@Column(name = "addressee")
	public String getAddressee() {
		return addressee;
	}
	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}
	@Column(name = "print_freq")
	public Integer getPrintFreq() {
		return printFreq;
	}
	public void setPrintFreq(Integer printFreq) {
		this.printFreq = printFreq;
	}
	@Column(name = "province")
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	@Column(name = "city")
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	@Column(name = "area")
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
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
	@Column(name = "transactor")
	public String getTransactor() {
		return transactor;
	}
	public void setTransactor(String transactor) {
		this.transactor = transactor;
	}
	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name = "op_id")
	public Long getOpId() {
		return opId;
	}
	public void setOpId(Long opId) {
		this.opId = opId;
	}
	@Column(name = "stamp",nullable=false,updatable=true,insertable=true)
    @Temporal(TemporalType.TIMESTAMP)
	public Date getStamp() {
		return stamp;
	}
	public void setStamp(Date stamp) {
		this.stamp = stamp;
	}
	@Column(name = "tel")
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	@Column(name = "subco_no")
	public Long getSubcoNo() {
		return subcoNo;
	}
	public void setSubcoNo(Long subcoNo) {
		this.subcoNo = subcoNo;
	}
	
}
