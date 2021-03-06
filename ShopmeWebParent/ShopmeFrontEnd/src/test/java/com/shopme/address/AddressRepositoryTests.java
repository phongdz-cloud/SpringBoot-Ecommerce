package com.shopme.address;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class AddressRepositoryTests {
	@Autowired
	private AddressRepository repo;
	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testAddNew() {
		Integer countryId = 242;
		Integer customerId = 3;
		Country country = entityManager.find(Country.class, countryId);
		Customer customer = entityManager.find(Customer.class, customerId);
		Address address = new Address();
		address.setCountry(country);
		address.setCustomer(customer);
		address.setFirstName("Ho");
		address.setLastName("Phong");
		address.setPhoneNumber("0375489103");
		address.setCity("BRVT");
		address.setState("Phan Thiet");
		address.setAddressLine1("Phan Thiet");
		address.setPostalCode("83");
		Address savedAdress = repo.save(address);
		assertThat(savedAdress).isNotNull();
	}

	@Test
	public void testFindByCustomer() {
		Integer customerId = 1;
		Customer customer = entityManager.find(Customer.class, customerId);
		List<Address> findByCustomer = repo.findByCustomer(customer);

		assertThat(findByCustomer.size()).isEqualTo(2);
		findByCustomer.forEach(System.out::println);
	}

	@Test
	public void testFindByIdAndCustomer() {
		Integer customerId = 1;
		Integer addressId = 1;
		Customer customer = entityManager.find(Customer.class, customerId);
		Address findByIdAndCustomer = repo.findByIdAndCustomer(addressId, customer.getId());
		assertThat(findByIdAndCustomer).isNotNull();
		System.out.println(findByIdAndCustomer);
	}

	@Test
	public void testUpdate() {
		Integer addressId = 4;
		Address address = entityManager.find(Address.class, addressId);
		//address.setPostalCode("80");
		address.setDefaultForShipping(true); 
		Address savedAddress = repo.save(address);
		assertThat(savedAddress.getPostalCode()).isEqualTo("80");
	}

	@Test
	public void testDeleteByIdAndCustomer() {
		Integer customerId = 1;
		Integer addressId = 1;
		Customer customer = entityManager.find(Customer.class, customerId);
		repo.deleteByIdAndCustomer(addressId, customer.getId());
		Address address = repo.findById(customerId).orElse(null);
		assertThat(address).isNull();
	}
	
	@Test
	public void testSetDefault() {
		Integer addressId = 3;
		repo.setDefaultAddress(addressId);
		
		Address address = repo.findById(addressId).get();
		assertThat(address.isDefaultForShipping()).isTrue();
	}
	
	@Test
	public void testSetNonDefaultAddresses() {
		Integer addressId = 2;
		Integer customerId = 2;
		repo.setNonDefaultForOthers(addressId, customerId);
	}
	
	@Test
	public void testGetDefault() {
		Integer customerId = 5;
		Address address = repo.findDefaultByCustomer(customerId);
		assertThat(address).isNotNull();
		System.out.println(address);
	}
}
