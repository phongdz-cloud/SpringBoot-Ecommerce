package com.shopme.admin.order;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.setting.SettingService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.setting.Setting;

@Controller
public class OrderController {
	private String defaultRedirectURL = "redirect:/orders/page/1?sortField=orderTime&sortDir=desc";

	@Autowired
	private OrderService orderService;

	@Autowired
	private SettingService settingService;

	@GetMapping("/orders")
	public String listFirstPage(Model model) {
		return defaultRedirectURL;
	}

	@GetMapping("/orders/page/{pageNumber}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listOrders", moduleURL = "/orders") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNumber") int pageNum, Model model) {
		orderService.listByPage(pageNum, helper);
		loadCurrencySetting(model);
		return "orders/orders";
	}

	@GetMapping("/orders/detail/{id}")
	public String viewOrderDetail(@PathVariable Integer id, Model model, RedirectAttributes ra) {
		try {
			Order order = orderService.get(id);
			loadCurrencySetting(model);
			model.addAttribute("order", order);
			return "orders/order_details_modal";
		} catch (OrderNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return defaultRedirectURL;
		}
	}

	@GetMapping("/orders/delete/{id}")
	public String deleteOrder(@PathVariable Integer id, RedirectAttributes ra) {
		try {
			orderService.deleteOrder(id);
			ra.addFlashAttribute("message", "The order ID " + id + " has been deleted.");
		} catch (OrderNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}

		return defaultRedirectURL;
	}

	@GetMapping("/orders/edit/{id}")
	public String editOrder(@PathVariable Integer id, Model model, RedirectAttributes ra, HttpServletRequest request) {
		try {
			Order order = orderService.get(id);

			List<Country> listCountries = orderService.listAllCountries();
			
			model.addAttribute("pageTitle", "Edit Order (ID: " + id + ")");
			model.addAttribute("order", order);
			model.addAttribute("listCountries", listCountries);

			return "orders/order_form";
		} catch (OrderNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return defaultRedirectURL;
		}
	}

	private void loadCurrencySetting(Model model) {
		List<Setting> currencySetting = settingService.getCurrencySetting();
		currencySetting.forEach(setting -> {
			model.addAttribute(setting.getKey(), setting.getValue());
		});
	}
}
