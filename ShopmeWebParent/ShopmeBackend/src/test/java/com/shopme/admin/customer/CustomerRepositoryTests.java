package com.shopme.admin.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {

	@Autowired
	private CustomerRepository repo;

	@Autowired
	private EntityManager entityManager;

	@Test
	public void testCreateCustomer() {
		Country country = entityManager.find(Country.class, 242);
		Customer customer = new Customer();
		customer.setEmail("19110092@student.hcmute.edu.vn");
		customer.setPassword("123456");
		customer.setFirstName("Tran");
		customer.setLastName("Linh");
		customer.setPhoneNumber("19110262");
		customer.setAddressLine1("Binh Dinh");
		customer.setCity("Thanh Pho Ho Chi Minh");
		customer.setState("Thu Duc");
		customer.setCountry(country);
		customer.setCreatedTime(new Date());
		customer.setPostalCode("74000");

		Customer savedCustomer = repo.save(customer);
		assertThat(savedCustomer).isNotNull();
	}

	@Test
	public void testListsCustomer() {
		List<Customer> customers = (List<Customer>) repo.findAll();

		customers.forEach(System.out::println);
		assertThat(customers.size()).isGreaterThan(0);
	}

	@Test
	public void testUpdateCustomer() {
		Integer customerId = 1;
		Customer customer = entityManager.find(Customer.class, customerId);
		customer.setAddressLine1("BRVT");
		Customer updatedCustomer = repo.save(customer);

		assertThat(updatedCustomer.getAddressLine1()).isEqualTo("BRVT");
	}

	@Test
	public void testDeleteCustomer() {
		Integer customerId = 4;
		repo.deleteById(customerId);

		Optional<Customer> deletedCustomer = repo.findById(customerId);

		assertThat(!deletedCustomer.isPresent());

	}

	@Test
	public void testFindByEmail() {
		String email = "19110304@student.hcmute.edu.vn";
		Customer customer = repo.findByEmail(email);

		assertThat(customer).isNotNull();
	}

	@Test
	public void testFindByVerificationCode() {
		//Customer customer = repo.updateEnabledStatus(verifyCation);

		//assertThat(customer).isNotNull();
	}

	@Test
	public void testEnableCustomer() {


	}

}
