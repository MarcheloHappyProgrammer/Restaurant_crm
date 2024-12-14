package com.restaurant.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.restaurant.management.model.Category;
import com.restaurant.management.service.CategoryService;

@Controller
@RequestMapping("/category")
public class CategoryController {
	@Autowired
	CategoryService categoryService;
	
	@GetMapping("/")
	public RedirectView redirectCategoryWithTrailingSlash() {
		return new RedirectView("/category");
	}
	@GetMapping
	public String listCategories(Model model) {
		List<Category> categories = categoryService.getAllCategories();
		model.addAttribute("categories", categories);
		return "/category/category_list";
	}
	@GetMapping("/search")
	public String searchCategories(@RequestParam("name") String name, Model model) {
		List<Category> categories = categoryService.searchCategoriesByName(name);
		model.addAttribute("categories", categories);
		return "/category/category_list";
	}
	@GetMapping("/add")
	public String showAddForm(Model model) {
		model.addAttribute("category", new Category());
		return "/category/category_form";
	}
	@PostMapping("/add")
	public String addCategory(@ModelAttribute("category") Category category) {
		categoryService.saveOrUpdateCategory(category);
		return "redirect:/category";
	}
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable("id") Long id, Model model) {
		Category category = categoryService.getCategory(id);
		model.addAttribute("category", category);
		return "/category/category_edit";
	}
	@PostMapping("/update")
	public String updateCategory(@ModelAttribute("category") Category category) {
		categoryService.saveOrUpdateCategory(category);
		return "redirect:/category";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteCategory(@PathVariable("id") Long id) {
		categoryService.deleteCategory(id);
		return "redirect:/category";
	}
}
