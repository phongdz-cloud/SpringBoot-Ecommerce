package com.shopme.admin.shipping;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.ShippingRate;

public interface ShippingRateRepository extends SearchRepository<ShippingRate, Integer> {
	
	@Query("SELECT s FROM ShippingRate s WHERE CONCAT (s.country.name, ' ', s.state) LIKE %?1%")
	public Page<ShippingRate> findAll(String keyword, Pageable pageable);

	@Query("SELECT sr FROM ShippingRate sr WHERE sr.country.id = ?1 AND sr.state = ?2")
	public ShippingRate findByCountryAndState(Integer countryId, String state);
	
	public Long countById(Integer id);
	
	@Query("UPDATE ShippingRate s SET s.codSupported = ?2 WHERE s.id = ?1")
	@Modifying
	public void updateCODSupport(Integer id, boolean enabled);
}
