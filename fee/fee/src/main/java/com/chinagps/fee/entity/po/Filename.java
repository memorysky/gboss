package com.chinagps.fee.entity.po;

// Generated 2014-5-20 11:28:49 by Hibernate Tools 4.0.0

/**
 * TFeeFilename generated by hbm2java
 */
public class Filename implements java.io.Serializable {

	private Long id;
	private Long subcoNo;
	private String filename;

	public Filename() {
	}

	public Filename(Long subcoNo, String filename) {
		this.subcoNo = subcoNo;
		this.filename = filename;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSubcoNo() {
		return this.subcoNo;
	}

	public void setSubcoNo(Long subcoNo) {
		this.subcoNo = subcoNo;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
