package com.sap.hackthon.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

//@Entity
//@Table(name = "T_ORDER")
//@SequenceGenerator(name = "OrdersSeq", allocationSize = 1, sequenceName = "T_ORDER_SEQ")
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OrdersSeq")
	@Column
	private Long id;

	@Column
	private String customerName;

	@Column
	private String address;

	@Column
	private Integer total;

	@Column
	private Integer tax;

	@Column
	private String memo;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderLines> lines = new ArrayList<OrderLines>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getTax() {
		return tax;
	}

	public void setTax(Integer tax) {
		this.tax = tax;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<OrderLines> getLines() {
		return lines;
	}

	public void setLines(List<OrderLines> lines) {
		this.lines = lines;
	}

}
