package com.shopme.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTests {
	@Autowired
	private BrandRepository brandRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateNewBrandWithOneCategory() {
		Category catLaptops = entityManager.find(Category.class, 6);
		Brand brand = new Brand("Acer");
		brand.getCategories().add(catLaptops);
		Brand savedBrand = brandRepo.save(brand);

		assertThat(savedBrand.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateNewBrandWithTwoCategory() {
		Category catMemory = entityManager.find(Category.class, 29);
		Category catInternalHardDrivers = entityManager.find(Category.class, 24);
		Brand brand = new Brand("Samsung");
		brand.getCategories().add(catMemory);
		brand.getCategories().add(catInternalHardDrivers);
		Brand savedBrand = brandRepo.save(brand);

		assertThat(savedBrand.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAllBrands() {
		Iterable<Brand> listBrands = brandRepo.findAll();
		listBrands.forEach(brand -> System.out.println(brand));
		assertThat(listBrands).isNotEmpty();
	}

	@Test
	public void testGetBrandById() {
		Brand brandApple = brandRepo.findById(4).get();
		System.out.println(brandApple);
		assertThat(brandApple).isNotNull();
	}

	@Test
	public void testUpdateBrandDetails() {
		Brand brandSamsung = brandRepo.findById(3).get();
		brandSamsung.setName("Samsung Electronics");

	}

	@Test
	public void testDeleteBrand() {
		Integer brandId = 2;
		brandRepo.deleteById(brandId);
	}

}
