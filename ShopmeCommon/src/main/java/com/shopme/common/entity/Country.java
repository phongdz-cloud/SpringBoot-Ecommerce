package com.shopme.common.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "countries")
public class Country extends IdBasedEntity{


	@Column(nullable = false, length = 45)
	private String name;

	@Column(nullable = false, length = 5)
	private String code;

	@OneToMany(mappedBy = "country")
	private Set<State> states;

	@OneToMany(mappedBy = "country")
	private Set<Customer> customers;

	@OneToMany(mappedBy = "country")
	private Set<ShippingRate> shippingRate;

	@OneToMany(mappedBy = "country")
	private Set<Address> addresses;

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


	@Override
	public String toString() {
		return "Country [" + (name != null ? "name=" + name + ", " : "") + (code != null ? "code=" + code : "") + "]";
	}

}
