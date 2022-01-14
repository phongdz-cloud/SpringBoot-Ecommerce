package com.shopme.admin.brand;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Brand;

@Service
@Transactional
public class BrandService {
	
	public static final int BRANDS_PER_PAGE = 10;

	@Autowired
	private BrandRepository repo;

	public List<Brand> listAll() {
		return (List<Brand>) repo.findAll(Sort.by("name").ascending());
	}
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, BRANDS_PER_PAGE, repo);
	}

	public Brand save(Brand brand) {
		return repo.save(brand);
	}

	public Brand get(Integer id) throws BrandNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new BrandNotFoundException("Could not find any brand with ID: " + id);
		}
	}

	public void delete(Integer id) throws BrandNotFoundException {
		Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			throw new BrandNotFoundException("Could not find any brand with Id " + id);
		}
		repo.deleteById(id);
	}

	public String checkUnique(Integer id, String name) {
		Brand brandByName = repo.findByName(name);
		if (brandByName == null)
			return "OK";

		boolean isCreatingNew = (id == null);

		if (isCreatingNew) {
			if (brandByName != null) {
				return "DuplicateName";
			}
		} else {
			if (brandByName.getId() != id) {
				return "DuplicateName";
			}
		}

		return "OK";
	}

}
