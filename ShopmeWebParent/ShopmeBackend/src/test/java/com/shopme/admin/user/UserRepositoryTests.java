package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userNamHM = new User("nam@codejava,net", "nam2020", "Nam", "Ha Minh");
		userNamHM.addRole(roleAdmin);

		User savedUser = userRepository.save(userNamHM);

		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateNewUserWithTwoRole() {
		User userHP = new User("hoaiphong@gmail.com", "ravi2020", "Phong", "Hoai");
		Role roleEditor = new Role(3);
		Role roleAssistance = new Role(5);

		userHP.addRole(roleEditor);
		userHP.addRole(roleAssistance);

		User savedUser = userRepository.save(userHP);

		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = userRepository.findAll();
		listUsers.forEach(user -> System.out.println(user));

	}

	@Test
	public void testGetUserById() {
		User userNam = userRepository.findById(1).get();
		System.out.println(userNam);
		assertThat(userNam).isNotNull();
	}

	@Test
	public void testUpdateUserDetails() {
		User userNam = userRepository.findById(1).get();
		userNam.setEnabled(true);
		userNam.setEmail("namjavaprogrammer@gmail.com");

		userRepository.save(userNam);
	}

	@Test
	public void testUpdateUserRoles() {
		User userHP = userRepository.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalespersion = new Role(2);

		userHP.getRoles().remove(roleEditor);
		userHP.addRole(roleSalespersion);

		userRepository.save(userHP);
	}

	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		userRepository.deleteById(userId);
	}

	@Test
	public void testGetUserByEmail() {
		String email = "hoaiphong@gmail.com";
		User user = userRepository.getUserByEmail(email);

		assertThat(user).isNotNull();
	}

	@Test
	public void testCountById() {
		Integer id = 1;
		Long countById = userRepository.countById(id);
		assertThat(countById).isNotNull().isGreaterThan(0);
	}

	@Test
	public void testDisableUser() {
		Integer id = 1;
		userRepository.updateEnabledStatus(id, false);

	}

	@Test
	public void testEnableUser() {
		Integer id = 3;
		userRepository.updateEnabledStatus(id, true);

	}

	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize = 4;

		PageRequest pageable = PageRequest.of(pageNumber, pageSize);

		Page<User> page = userRepository.findAll(pageable);

		List<User> listUsers = page.getContent();

		listUsers.forEach(user -> System.out.println(user));

		assertThat(listUsers.size()).isEqualTo(pageSize);
	}

	@Test
	public void testSearchUsers() {
		String keyword= "bruce";
		int pageNumber = 0;
		int pageSize = 4;

		PageRequest pageable = PageRequest.of(pageNumber, pageSize);

		Page<User> page = userRepository.findAll(keyword, pageable);
		
		List<User> listUsers = page.getContent();
		
		listUsers.forEach(user -> System.out.println(user));
		
		assertThat(listUsers.size()).isGreaterThan(0);
	}

}
