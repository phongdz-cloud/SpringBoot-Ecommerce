package com.shopme.admin.setting.state;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class StateRepositoryTests {
	@Autowired
	private StateRepository repo;

	@Autowired
	private EntityManager entityManager;

	@Test
	public void testCreateState() {
		Country countryVN = entityManager.find(Country.class, 4);
		State state = new State("West Bengal");
		state.setCountry(countryVN);

		State savedState = repo.save(state);

		assertThat(savedState).isNotNull();
	}

	@Test
	public void testListStatesByCountry() {
		Country countryVN = entityManager.find(Country.class, 1);
		List<State> states = repo.findByCountryOrderByNameAsc(countryVN);

		states.forEach(System.out::println);

		assertThat(states.size()).isGreaterThan(0);
	}

	@Test
	public void testUpdateState() {
		Integer id = 1;
		State stateById = repo.findById(id).get();

		stateById.setName("HN");
		repo.save(stateById);
		State updatedState = repo.findById(id).get();
		assertThat(updatedState.getName()).isEqualTo("HN");
	}

	@Test
	public void testGetState() {
		Integer id = 1;
		State stateById = repo.findById(id).get();

		assertThat(stateById).isNotNull();
	}

	@Test
	public void testDeleteState() {
		Integer id = 2;
		repo.deleteById(id);
		State deletedState = repo.findById(id).orElse(null);

		assertThat(deletedState).isNull();
	}
}
