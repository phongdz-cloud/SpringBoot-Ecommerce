package com.shopme.admin.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.Customer;

public interface CustomerRepository extends SearchRepository<Customer, Integer> {
	public Customer findByEmail(String email);

	@Query("SELECT c FROM Customer c WHERE CONCAT (c.id, ' ', c.firstName, ' ', c.lastName, ' '"
			+ ", c.country.name,'',c.email,'',c.city,'',c.state,'',c.addressLine1,'',c.addressLine2,'',c.postalCode) LIKE %?1%")
	public Page<Customer> findAll(String keyword, Pageable pageable);

	public Long countById(Integer id);

	@Query("UPDATE Customer c SET c.enabled = ?2 WHERE c.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);

}
