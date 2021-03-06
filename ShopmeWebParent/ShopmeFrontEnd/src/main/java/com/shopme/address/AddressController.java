package com.shopme.address;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.Utility;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;

@Controller
public class AddressController {
	@Autowired
	private AddressService addressService;
	@Autowired
	private CustomerService customerService;

	@GetMapping("/address_book")
	public String showAddressBook(Model model, HttpServletRequest request) throws CustomerNotFoundException {
		Customer customer = getAuthenticatedCustomer(request);
		List<Address> listAddresses = addressService.listAddressBook(customer);
		boolean usePrimaryAddessAsDefault = true;
		for (Address address : listAddresses) {
			if (address.isDefaultForShipping()) {
				usePrimaryAddessAsDefault = false;
				break;
			}
		}

		model.addAttribute("listAddresses", listAddresses);
		model.addAttribute("customer", customer);
		model.addAttribute("usePrimaryAddessAsDefault", usePrimaryAddessAsDefault);
		return "address_book/addresses";
	}

	@GetMapping("/address_book/new")
	public String showFormAddressBook(Model model) {
		List<Country> listCountries = addressService.listAllCountries();
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("address", new Address());
		return "address_book/address_form";
	}

	@GetMapping("/address_book/edit/{id}")
	public String editAddress(@PathVariable Integer id, Model model, RedirectAttributes ra,
			HttpServletRequest request) {

		try {
			Customer customer = getAuthenticatedCustomer(request);
			Address address = addressService.get(id, customer.getId());
			List<Country> listCountries = addressService.listAllCountries();

			model.addAttribute("address", address);
			model.addAttribute("listCountries", listCountries);
		} catch (CustomerNotFoundException | AddressNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/address_book";
		}
		return "address_book/address_form";
	}

	@PostMapping("/address_book/save")
	public String saveAddress(Address address, RedirectAttributes ra, HttpServletRequest request)
			throws CustomerNotFoundException {
		Customer customer = getAuthenticatedCustomer(request);
		address.setCustomer(customer);
		addressService.save(address);

		String redirectOption = request.getParameter("redirect");
		String redirectURL = "redirect:/address_book";

		if ("checkout".equals(redirectOption)) {
			redirectURL += "?redirect=checkout";
		}

		ra.addFlashAttribute("message", "The address has been saved successfully");
		return redirectURL;
	}

	@GetMapping("/address_book/delete/{id}")
	public String deleteAddress(@PathVariable Integer id, RedirectAttributes ra, HttpServletRequest request) {
		try {
			Customer customer = getAuthenticatedCustomer(request);
			addressService.delete(id, customer.getId());
			ra.addFlashAttribute("message", "Delete Address ID: " + id + " successfully.");
		} catch (CustomerNotFoundException | AddressNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/address_book";
	}

	@GetMapping("/address_book/default/{id}")
	public String setDefaultAddress(@PathVariable Integer id, RedirectAttributes ra, HttpServletRequest request)
			throws CustomerNotFoundException {
		Customer customer = getAuthenticatedCustomer(request);
		addressService.setDefaultAddress(id, customer.getId());

		String redirectOption = request.getParameter("redirect");
		String redirectURL = "redirect:/address_book";

		if ("cart".equals(redirectOption)) {
			redirectURL = "redirect:/cart";
		} else if ("checkout".equals(redirectOption)) {
			redirectURL = "redirect:/checkout";
		}

		return redirectURL;
	}

	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		return customerService.getCustomerByEmail(email);
	}
}
