package com.shopme.admin.setting.country;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Country;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CountryRepositoryTests {

	@Autowired
	private CountryRepository repo;

	@Test
	public void testCreateCountry() {
//		Country country = new Country("Republic of India", "In");
//		Country country = new Country("United States", "US");
		Country country = new Country("United Kingdom", "UK");
		Country savedCountry = repo.save(country);

		assertThat(savedCountry).isNotNull();
	}

	@Test
	public void testListCountry() {
		List<Country> countries = (List<Country>) repo.findAll();
		countries.forEach(System.out::println);

		assertThat(countries.size()).isGreaterThan(0);
	}

	@Test
	public void testGetCountry() {
		Integer countryId = 3;
		Country countryById = repo.findById(countryId).get();
		System.out.println(countryById);
		assertThat(countryById).isNotNull();
	}
	
	@Test
	public void testUpdateCountry() {
		Integer countryId = 1;
		Country countryById = repo.findById(countryId).get();
		countryById.setCode("600");
		repo.save(countryById);
		assertThat(countryById.getCode()).isEqualTo("600");
	}
	
	@Test
	public void testDeleteCountry() {
		Integer countryId = 3;
		repo.deleteById(countryId);
		Country deletedCountry= repo.findById(countryId).orElse(null);
		assertThat(deletedCountry).isNull();
	}
}
