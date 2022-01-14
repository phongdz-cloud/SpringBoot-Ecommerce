package com.shopme.common.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "countries")
public class Country {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 45)
	private String name;

	@Column(nullable = false, length = 5)
	private String code;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
	private Set<State> states;

	@OneToMany(mappedBy = "country")
	private Set<Customer> customers;

	@OneToMany(mappedBy = "country")
	private Set<ShippingRate> shippingRate;

	public Country() {
	}

	public Country(String name) {
		this.name = name;
	}

	public Country(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public Country(Integer id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Set<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(Set<Customer> customers) {
		this.customers = customers;
	}

	public Set<ShippingRate> getShippingRate() {
		return shippingRate;
	}

	public void setShippingRate(Set<ShippingRate> shippingRate) {
		this.shippingRate = shippingRate;
	}

	@Override
	public String toString() {
		return "Country [" + (name != null ? "name=" + name + ", " : "") + (code != null ? "code=" + code : "") + "]";
	}

}
