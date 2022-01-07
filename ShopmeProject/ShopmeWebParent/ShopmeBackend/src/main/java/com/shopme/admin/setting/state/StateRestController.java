package com.shopme.admin.setting.state;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.State;

@RestController
public class StateRestController {
	@Autowired
	private StateRepository stateRepo;

	@Autowired
	private CountryRepository countryRepo;

	@GetMapping("/states/list_by_country/{id}")
	public List<StateDTO> listByCountry(@PathVariable("id") Integer countryId) {
		List<State> states = stateRepo.findByCountryOrderByNameAsc(countryRepo.findById(countryId).get());
		List<StateDTO> listStatesDTO = new ArrayList<>();
		for (State state : states) {
			listStatesDTO.add(new StateDTO(state.getId(), state.getName()));
		}
		return listStatesDTO;
	}

	@PostMapping("/states/save")
	public String save(@RequestBody State state) {
		State savedState = stateRepo.save(state);

		return String.valueOf(savedState.getId());
	}

	@DeleteMapping("/states/delete/{id}")
	public void delete(@PathVariable("id") Integer stateId) {
		stateRepo.deleteById(stateId);
	}
}