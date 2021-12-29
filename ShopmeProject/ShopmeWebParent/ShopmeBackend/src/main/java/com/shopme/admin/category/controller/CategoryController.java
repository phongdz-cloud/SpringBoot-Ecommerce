package com.shopme.admin.category.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.category.CategoryNotFoundException;
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.user.UserNotFoundException;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Controller
public class CategoryController {
	@Autowired
	private CategoryService service;

	@GetMapping("/categories")
	public String listCategories(Model model) {
		List<Category> listCategories = service.listAll();
		model.addAttribute("listCategories", listCategories);
		return "categories/categories";
	}

	@GetMapping("/categories/new")
	public String newCategory(Model model) {
		List<Category> listCategories = service.listCategoriesUserdInForm();

		model.addAttribute("category", new Category());
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle", "Create new Category");
		return "categories/category_form";
	}

	@PostMapping("/categories/save")
	public String saveCategory(Category category, @RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes ra) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			category.setImage(fileName);
			Category savedCategory = service.save(category);
			String uploadDir = "../category-images/" + savedCategory.getId();
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			service.save(category);
		}

		ra.addFlashAttribute("message", "The category has been saved successfully.");
		return "redirect:/categories";
	}

	@GetMapping("/categories/edit/{id}")
	public String editCategory(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			List<Category> listCategories = service.listCategoriesUserdInForm();
			Category category = service.get(id);

			model.addAttribute("category", category);
			model.addAttribute("pageTitle", "Edit Category (ID: " + id + ")");
			model.addAttribute("listCategories", listCategories);
			return "categories/category_form";
		} catch (CategoryNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/categories";
		}

	}
}
