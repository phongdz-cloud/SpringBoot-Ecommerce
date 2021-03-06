package com.shopme.admin.shipping;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ShippingRateRepositoryTests {
	@Autowired
	private ShippingRateRepository repo;
	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateNew() {
		ShippingRate shippingRate = new ShippingRate();
		Country country = entityManager.find(Country.class, 14);
		shippingRate.setCountry(country);
		shippingRate.setCodSupported(true);
		shippingRate.setRate(12.5F);
		shippingRate.setDays(7);
		shippingRate.setState("Queensland");
		ShippingRate savedShippingRate = repo.save(shippingRate);
		assertThat(shippingRate).isNotNull();
	}

	@Test
	public void testCreate2New() {
		ShippingRate shippingRate1 = new ShippingRate();
		ShippingRate shippingRate2 = new ShippingRate();
		Country country = entityManager.find(Country.class, 106);
		shippingRate1.setCountry(country);
		shippingRate1.setCodSupported(false);
		shippingRate1.setRate(9.11F);
		shippingRate1.setDays(3);
		shippingRate1.setState("Maharashtra");

		shippingRate2.setCountry(country);
		shippingRate2.setCodSupported(true);
		shippingRate2.setRate(9.5F);
		shippingRate2.setDays(3);
		shippingRate2.setState("Delhi");

		Iterable<ShippingRate> iterable = repo.saveAll(List.of(shippingRate1, shippingRate2));
		assertThat(iterable).size().isEqualTo(2);
	}
	
	@Test
	public void testUpdate() {
		ShippingRate shippingRate = repo.findById(1).get();
		shippingRate.setDays(6);
		ShippingRate updatedShippingRate = repo.save(shippingRate);
		assertThat(updatedShippingRate.getDays()).isEqualTo(6);
	}
	
	@Test
	public void testDelete() {
		repo.deleteById(3);
		ShippingRate deletedShippingRate = repo.findById(3).orElse(null);
		assertThat(deletedShippingRate).isNull();
	}
	@Test
	public void testFindAll() {
		Iterable<ShippingRate> iterable = repo.findAll();
		assertThat(iterable).size().isGreaterThan(0);
		iterable.forEach(System.out::println);
	}
	
	@Test
	public void testFindByCountryAndState() {
		Integer countryId = 106;
		String state = "Maharashtraa";
		ShippingRate shippingState = repo.findByCountryAndState(countryId, state);
		assertThat(shippingState).isNotNull();
	}
	
	@Test
	public void testUpdateCODSupport() {
		Integer shippingRateId = 4;
		repo.updateCODSupport(shippingRateId, true);
		ShippingRate updatedShippingRate = repo.findById(shippingRateId).get();
		assertThat(updatedShippingRate.isCodSupported());
	}
	
}
