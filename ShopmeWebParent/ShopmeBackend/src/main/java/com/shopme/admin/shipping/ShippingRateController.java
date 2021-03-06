package com.shopme.admin.shipping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;

@Controller
public class ShippingRateController {
	@Autowired
	private ShippingRateService service;
	
	private String defaultRedirectURL = "redirect:/shipping/page/1?sortField=country.name&sortDir=asc";

	@GetMapping("/shipping")
	public String listFirstPage(Model model) {
		return defaultRedirectURL;
	}

	@GetMapping("/shipping/page/{pageNumber}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listShippings", moduleURL = "/shipping") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNumber") int pageNum) {
		service.listByPage(pageNum, helper);

		return "shipping/shipping_rates";
	}

	@GetMapping("/shipping/new")
	public String newShipping(Model model) {
		List<Country> listCountries = service.listAllCountry();

		model.addAttribute("shipping", new ShippingRate());
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("pageTitle", "Create new Shipping Rates");
		return "shipping/shipping_rate_form";
	}

	@GetMapping("/shipping/edit/{id}")
	public String editShipping(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			ShippingRate shipping = service.get(id);
			List<Country> listCountries = service.listAllCountry();
			model.addAttribute("shipping", shipping);
			model.addAttribute("listCountries", listCountries);
			model.addAttribute("pageTitle", "Edit Shipping (ID: " + id + ")");

			return "shipping/shipping_rate_form";
		} catch (ShippingRateNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/shipping";
		}
	}

	@PostMapping("/shipping/save")
	public String saveShipping(ShippingRate shipping, RedirectAttributes ra) {
		try {
			service.save(shipping);
			ra.addFlashAttribute("message","The shipping rate has been saved successfully");
		} catch (ShippingRateAlreadyExistsException e) {
			ra.addFlashAttribute("message",e.getMessage());
		}
		return defaultRedirectURL;
	}

	@GetMapping("/shipping/delete/{id}")
	public String deleteShipping(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);
			redirectAttributes.addFlashAttribute("message", "The shipping ID " + id + " has been deleted successfully");
		} catch (ShippingRateNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return defaultRedirectURL;
	}

	@GetMapping("/shipping/{id}/enabled/{status}")
	public String updateShippingEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes) {
		service.updateCODSpport(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "COD support for shipping rate ID " + id + " has been updated";
		redirectAttributes.addFlashAttribute("message", message);
		return defaultRedirectURL;
	}

}
