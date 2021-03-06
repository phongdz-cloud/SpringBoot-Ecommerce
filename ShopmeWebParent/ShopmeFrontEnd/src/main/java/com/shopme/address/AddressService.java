package com.shopme.address;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.setting.CountryRepository;

@Service
@Transactional
public class AddressService {
	@Autowired
	private AddressRepository addressRepo;

	@Autowired
	private CountryRepository countryRepo;

	public List<Address> listAddressBook(Customer customer) {
		return addressRepo.findByCustomer(customer);
	}

	public List<Country> listAllCountries() {
		return countryRepo.findAllByOrderByNameAsc();
	}

	public void save(Address address) {
		addressRepo.save(address);
	}

	public Address get(Integer addressId, Integer customerId) throws AddressNotFoundException {
		Address findByIdAndCustomer = addressRepo.findByIdAndCustomer(addressId, customerId);

		if (findByIdAndCustomer == null) {
			throw new AddressNotFoundException(
					"Could not find any address ID: " + addressId + " and Customer ID: " + customerId);
		}
		return findByIdAndCustomer;
	}

	public void delete(Integer addressId, Integer customerId) throws AddressNotFoundException {
		Address findByIdAndCustomer = get(addressId, customerId);
		addressRepo.deleteByIdAndCustomer(findByIdAndCustomer.getId(), findByIdAndCustomer.getCustomer().getId());
	}

	public void setDefaultAddress(Integer defaultAddressId, Integer customerId) {
		if (defaultAddressId > 0) {
			addressRepo.setDefaultAddress(defaultAddressId);

		}
		addressRepo.setNonDefaultForOthers(defaultAddressId, customerId);
	}

	public Address getDefaultAddress(Customer customer) {

		return addressRepo.findDefaultByCustomer(customer.getId());
	}

}
