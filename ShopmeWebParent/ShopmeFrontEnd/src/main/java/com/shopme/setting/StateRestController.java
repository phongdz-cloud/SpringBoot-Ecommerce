package com.shopme.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.State;
import com.shopme.common.entity.StateDTO;

@RestController
public class StateRestController {
	@Autowired
	private StateRepository stateRepo;

	@Autowired
	private CountryRepository countryRepo;

	@GetMapping("/settings/list_states_by_country/{id}")
	public List<StateDTO> listByCountry(@PathVariable("id") Integer countryId) {
		List<State> states = stateRepo.findByCountryOrderByNameAsc(countryRepo.findById(countryId).get());
		List<StateDTO> listStatesDTO = new ArrayList<>();
		for (State state : states) {
			listStatesDTO.add(new StateDTO(state.getId(), state.getName()));
		}
		return listStatesDTO;
	}

	
}
