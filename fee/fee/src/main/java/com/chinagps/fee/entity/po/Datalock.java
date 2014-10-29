package com.chinagps.fee.entity.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

/**
 * @Package:com.chinagps.fee.entity.po
 * @ClassName:Datalock
 * @Description:计费托收加锁表的实体类
 * @author:zfy
 * @date:2014-5-21 下午4:04:08
 */
@Entity
@Table(name = "t_fee_datalock")
public class Datalock  extends BaseEntity{
	private static final Long serialVersionUID = 1L;
	private Long subcoNo;//分公司ID
	private Integer locktag;//锁标志, 0=解锁,可修改关键数据, 1=加锁,不可修改
	private Date stamp;//时间戳

	private String times;//下载批次
	public Datalock() {
	}

	public Datalock(Long subcoNo) {
		this.subcoNo = subcoNo;
	}

	public Datalock(Long subcoNo, Integer locktag) {
		this.subcoNo = subcoNo;
		this.locktag = locktag;
	}
	@Id
	@Column(name = "subco_no")
	public Long getSubcoNo() {
		return this.subcoNo;
	}

	public void setSubcoNo(Long subcoNo) {
		this.subcoNo = subcoNo;
	}
	@Column(name = "locktag")
	public Integer getLocktag() {
		return this.locktag;
	}

	public void setLocktag(Integer locktag) {
		this.locktag = locktag;
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
	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

}
