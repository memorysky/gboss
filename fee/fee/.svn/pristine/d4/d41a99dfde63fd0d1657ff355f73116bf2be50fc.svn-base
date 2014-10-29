package com.chinagps.fee.entity.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * @Package:com.chinagps.fee.entity.po.sel
 * @ClassName:Packprice
 * @Description:套餐价格表的实体类
 * @author:zfy
 * @date:2014-5-20 上午11:57:51
 */
@Entity
@Table(name = "t_fee_packprice")
public class Packprice extends BaseEntity{
	private static final long serialVersionUID = 1L;
	private Long id;//套餐价格ID
	private Long subcoNo;//分公司ID, 0=总部定义, 大于0=分公司自定义
	private Long packId;//套餐ID
	private Integer custType;//客户类型, VIP类型, 0=不按VIP定价 ;VIP等级, 系统值2020, 1=普通卡, 2=银卡, 3=金卡, 4=白金卡, 99=免费
	private Integer years;//入网年限, 0=不按年限定价
	private Float monthPrice;//月价格
	private Float yearPrice;//年价格
	private Integer giftMonths;//年用户赠送月数, 0=不赠送, 暂不启用, 最好转为折扣或赠送费用
	public Packprice() {
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)//可以用increment，或者seqence(oracle),identity(mysql,ms sql)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "subco_no")
	public Long getSubcoNo() {
		return this.subcoNo;
	}

	public void setSubcoNo(Long subcoNo) {
		this.subcoNo = subcoNo;
	}
	@Column(name = "pack_id")
	public Long getPackId() {
		return this.packId;
	}

	public void setPackId(Long packId) {
		this.packId = packId;
	}
	@Column(name = "month_price")
	public Float getMonthPrice() {
		return this.monthPrice;
	}

	public void setMonthPrice(Float monthPrice) {
		this.monthPrice = monthPrice;
	}
	@Column(name = "year_price")
	public Float getYearPrice() {
		return this.yearPrice;
	}

	public void setYearPrice(Float yearPrice) {
		this.yearPrice = yearPrice;
	}
	@Column(name = "cust_type")
	public Integer getCustType() {
		return custType;
	}

	public void setCustType(Integer custType) {
		this.custType = custType;
	}
	@Column(name = "years")
	public Integer getYears() {
		return years;
	}

	public void setYears(Integer years) {
		this.years = years;
	}
	@Column(name = "gift_months")
	public Integer getGiftMonths() {
		return giftMonths;
	}

	public void setGiftMonths(Integer giftMonths) {
		this.giftMonths = giftMonths;
	}

}
