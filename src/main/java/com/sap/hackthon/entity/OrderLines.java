package com.sap.hackthon.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "OrderLines")
@SequenceGenerator(name = "OrderLinesSeq", allocationSize = 1, sequenceName = "orderlines_seq")
public class OrderLines {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OrderLinesSeq")
	@Column
	private Long id;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Orders order;

	@Column
	private String productName;

	@Column
	private Integer price;

	@Column
	private Integer count;

	@Column
	private Integer lineTotal;

	@Column
	private String memo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Orders getOrder() {
		return order;
	}

	public void setOrder(Orders order) {
		this.order = order;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getLineTotal() {
		return lineTotal;
	}

	public void setLineTotal(Integer lineTotal) {
		this.lineTotal = lineTotal;
	}

}
