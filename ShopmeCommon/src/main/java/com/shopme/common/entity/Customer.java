package com.shopme.common.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer extends AbstractAddressWithCountry {

	@Column(nullable = false, length = 45)
	private String email;

	@Column(nullable = false, length = 64)
	private String password;

	@Column(name = "created_time", nullable = false)
	private Date createdTime;

	private boolean enabled;

	@Column(name = "verification_code", length = 64)
	private String verificationCode;

	@Enumerated(EnumType.STRING)
	@Column(name = "authentication_type", length = 10)
	private AuthenticationType authenticationType;

	@Column(name = "rest_password_token", length = 30)
	private String restPasswordToken;

	@OneToMany(mappedBy = "customer")
	private Set<Address> addresses;

	public Customer() {
	}

	public Customer(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getFullName() {
		return firstName + " " + lastName;
	}

	public AuthenticationType getAuthenticationType() {
		return authenticationType;
	}

	public void setAuthenticationType(AuthenticationType authenticationType) {
		this.authenticationType = authenticationType;
	}

	public String getRestPasswordToken() {
		return restPasswordToken;
	}

	public void setRestPasswordToken(String restPasswordToken) {
		this.restPasswordToken = restPasswordToken;
	}

	public Set<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

}
