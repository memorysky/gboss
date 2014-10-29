package com.chinagps.fee.entity.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * @Package:com.chinagps.fee.entity.po.sel
 * @ClassName:PackItem
 * @Description:套餐服务项关系表的实体类
 * @author:zfy
 * @date:2014-5-20 上午11:52:38
 */
//@Entity
//@Table(name = "t_fee_pack_item")
public class PackItem extends BaseEntity{
	private static final long serialVersionUID = 1L;
	private Long id;//关联ID
	private Long packId;//套餐ID
	private Long itemId;//服务项ID
	private Integer weights;//权重
	private String remark;//服务项描述

	public PackItem() {
	}

	public PackItem(Long packId, Long itemId, Integer weights, String remark) {
		this.packId = packId;
		this.itemId = itemId;
		this.weights = weights;
		this.remark = remark;
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
	@Column(name = "pack_id")
	public Long getPackId() {
		return this.packId;
	}

	public void setPackId(Long packId) {
		this.packId = packId;
	}
	@Column(name = "item_id")
	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	@Column(name = "weights")
	public Integer getWeights() {
		return this.weights;
	}

	public void setWeights(Integer weights) {
		this.weights = weights;
	}
	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
