package com.shopme.admin.order;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.setting.SettingService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.order.OrderStatus;
import com.shopme.common.entity.order.OrderTrack;
import com.shopme.common.entity.product.Product;
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

	@PostMapping("/order/save")
	public String saveOrder(Order order, HttpServletRequest request, RedirectAttributes ra) {
		System.out.println("Payment Method: " + order.getPaymentMethod());
		String countryName = request.getParameter("countryName");
		order.setCountry(countryName);

		updateProductDetails(order, request);
		updateOrderTracks(order, request);

		orderService.save(order);

		ra.addFlashAttribute("message", "The order ID " + order.getId() + " has been updated successfully!");
		return defaultRedirectURL;
	}

	private void updateOrderTracks(Order order, HttpServletRequest request) {
		String[] trackIds = request.getParameterValues("trackId");
		String[] trackDates = request.getParameterValues("trackDate");
		String[] trackStatuses = request.getParameterValues("trackStatus");
		String[] trackNotes = request.getParameterValues("trackNotes");
		List<OrderTrack> orderTracks = order.getOrderTracks();
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		
		for (int i = 0; i < trackIds.length; i++) {
			OrderTrack trackRecord = new OrderTrack();
			Integer trackId = Integer.parseInt(trackIds[i]);
			if (trackId > 0) {
				trackRecord.setId(trackId);
			}
			
			trackRecord.setOrder(order);
			trackRecord.setStatus(OrderStatus.valueOf(trackStatuses[i]));
			trackRecord.setNotes(trackNotes[i]);
			try {
				trackRecord.setUpdatedTime(dateFormatter.parse(trackDates[i]));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			orderTracks.add(trackRecord);
		}
	}

	private void updateProductDetails(Order order, HttpServletRequest request) {
		String[] detailIds = request.getParameterValues("detailId");
		String[] productIds = request.getParameterValues("productId");
		String[] productDetailCosts = request.getParameterValues("productDetailCost");
		String[] quantites = request.getParameterValues("quantity");
		String[] productPrices = request.getParameterValues("productPrice");
		String[] productSubtotals = request.getParameterValues("productSubtotal");
		String[] productShipCosts = request.getParameterValues("productShipCost");

		Set<OrderDetail> orderDetails = order.getOrderDetails();
		for (int i = 0; i < detailIds.length; i++) {
			System.out.println("Detail ID: " + detailIds[i]);
			System.out.println("Product ID: " + productIds[i]);
			System.out.println("Product Cost: " + productDetailCosts[i]);
			System.out.println("Quantity: " + quantites[i]);
			System.out.println("Product Price: " + productPrices[i]);

			OrderDetail orderDetail = new OrderDetail();
			Integer detailId = Integer.parseInt(detailIds[i]);
			if (detailId > 0) {
				orderDetail.setId(detailId);
			}

			orderDetail.setOrder(order);
			orderDetail.setProduct(new Product(Integer.parseInt(productIds[i])));
			orderDetail.setProductCost(Float.parseFloat(productDetailCosts[i]));
			orderDetail.setSubtotal(Float.parseFloat(productSubtotals[i]));
			orderDetail.setShippingCost(Float.parseFloat(productShipCosts[i]));
			orderDetail.setQuantity(Integer.parseInt(quantites[i]));
			orderDetail.setUnitPrice(Float.parseFloat(productPrices[i]));

			orderDetails.add(orderDetail);
		}

	}

	private void loadCurrencySetting(Model model) {
		List<Setting> currencySetting = settingService.getCurrencySetting();
		currencySetting.forEach(setting -> {
			model.addAttribute(setting.getKey(), setting.getValue());
		});
	}
}
