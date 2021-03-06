package com.shopme.admin.shipping;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.product.ProductRepository;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.product.Product;

@Service
@Transactional
public class ShippingRateService {
	private static final int DIM_DIVISOR = 139;
	public static final int SHIPPINGS_PER_PAGE = 10;

	@Autowired
	private ShippingRateRepository shipRepo;

	@Autowired
	private CountryRepository countryRepo;

	@Autowired
	private ProductRepository productRepo;

	public List<ShippingRate> listAll() {
		return (List<ShippingRate>) shipRepo.findAll(Sort.by("country.name").ascending());
	}

	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, SHIPPINGS_PER_PAGE, shipRepo);
	}

	public List<Country> listAllCountry() {
		return countryRepo.findAllByOrderByNameAsc();
	}

	public void save(ShippingRate rateInForm) throws ShippingRateAlreadyExistsException {
		ShippingRate rateInDB = shipRepo.findByCountryAndState(rateInForm.getCountry().getId(), rateInForm.getState());
		boolean foundExistingRateInNewMode = rateInForm.getId() == null && rateInDB != null;
		boolean foundDifferentExistingRateInEditMode = rateInForm.getId() != null && rateInDB != null
				&& !rateInDB.equals(rateInForm);

		if (foundExistingRateInNewMode || foundDifferentExistingRateInEditMode) {
			throw new ShippingRateAlreadyExistsException("There's already a rate for the destination "
					+ rateInForm.getCountry().getName() + ", " + rateInForm.getState());
		}
		shipRepo.save(rateInForm);
	}

	public ShippingRate get(Integer id) throws ShippingRateNotFoundException {
		try {
			return shipRepo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new ShippingRateNotFoundException("Could not find any shipping with ID: " + id);
		}
	}

	public void delete(Integer id) throws ShippingRateNotFoundException {
		Long countById = shipRepo.countById(id);
		if (countById == null || countById == 0) {
			throw new ShippingRateNotFoundException("Could not find any shipping with Id " + id);
		}
		shipRepo.deleteById(id);
	}

	public void updateCODSpport(Integer id, boolean status) {
		shipRepo.updateCODSupport(id, status);
	}

	public float calculateShippingCost(Integer productId, Integer countryId, String state)
			throws ShippingRateNotFoundException {
		ShippingRate shippingRate = shipRepo.findByCountryAndState(countryId, state);
		if (shippingRate == null) {
			throw new ShippingRateNotFoundException(
					"No shipping rate found for the given " + " destination. You have to enter shipping cost manually");
		}

		Product product = productRepo.findById(productId).get();

		float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
		float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;

		return finalWeight * shippingRate.getRate();
	}

}
