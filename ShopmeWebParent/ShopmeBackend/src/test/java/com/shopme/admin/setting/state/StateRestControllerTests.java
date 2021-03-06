package com.shopme.admin.setting.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import com.shopme.common.entity.StateDTO;

@SpringBootTest
@AutoConfigureMockMvc
public class StateRestControllerTests {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@WithMockUser(username = "nam@codejava.net", password = "namhm2020", roles = "ADMIN")
	public void testListByCountries() throws Exception {
		Country countryById = entityManager.find(Country.class, 4);
		String url = "/states/list_by_country/" + countryById.getId();

		MvcResult result = mockMvc.perform(get(url)).andExpect(status().isOk()).andDo(print()).andReturn();
		String jsonResponse = result.getResponse().getContentAsString();

		StateDTO[] states = objectMapper.readValue(jsonResponse, StateDTO[].class);

		for (StateDTO state : states) {
			System.out.println(state);
		}

		assertThat(states).hasSizeGreaterThan(0);
	}

	@Test
	public void testCreateState() throws JsonProcessingException, Exception {
		String url = "/states/save";
		Country countryVN = entityManager.find(Country.class, 1);
		State state = new State("TPHCM");
		state.setCountry(countryVN);

		MvcResult result = mockMvc.perform(
				post(url).contentType("application/json").content(objectMapper.writeValueAsString(state)).with(csrf()))
				.andDo(print()).andExpect(status().isOk()).andReturn();
		String response = result.getResponse().getContentAsString();
		Integer stateId = Integer.parseInt(response);

		State findByID = entityManager.find(State.class, stateId);
		assertThat(findByID).isNotNull();
	}

	@Test
	public void testUpdateState() throws JsonProcessingException, Exception {
		String url = "/states/save";
		State state = entityManager.find(State.class, 7);
		state.setName("BRVT");

		MvcResult result = mockMvc.perform(
				post(url).contentType("application/json").content(objectMapper.writeValueAsString(state)).with(csrf()))
				.andDo(print()).andExpect(status().isOk()).andReturn();
		String response = result.getResponse().getContentAsString();
		Integer stateId = Integer.parseInt(response);

		State findByID = entityManager.find(State.class, stateId);
		assertThat(findByID.getName()).isEqualTo("BRVT");
	}

	@Test
	public void testDeleteState() throws Exception {
		int stateId = 7;
		String url = "/states/delete/" + stateId;
		mockMvc.perform(get(url)).andExpect(status().isOk());

		State state = entityManager.find(State.class, stateId);
		assertThat(state).isNull();
	}
}
