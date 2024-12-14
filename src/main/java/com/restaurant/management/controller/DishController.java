package com.restaurant.management.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.restaurant.management.model.Category;
import com.restaurant.management.model.Dish;
import com.restaurant.management.service.CategoryService;
import com.restaurant.management.service.DishService;

@Controller
@RequestMapping("/dish")
public class DishController {
	@Autowired
	private DishService dishService;
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/")
	public RedirectView redirectDishWithTrailingSlash() {
		return new RedirectView("/dish");
	}

	@GetMapping
	public String listDishes(Model model, @PageableDefault(size = 10, page = 0) Pageable pageable) {
		Page<Dish> page = dishService.finAllDish(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));

		model.addAttribute("page", page);
		var totalPages = page.getTotalPages();
		var currentPage = page.getNumber();
		var start = Math.max(1, currentPage);
		var end = Math.min(currentPage + 20, totalPages);
		if (totalPages > 0) {
			List<Integer> pageNumbers = new ArrayList<>();
			for (int i = start; i <= end; i++) {
				pageNumbers.add(i);
			}
			model.addAttribute("pageNumbers", pageNumbers);
		}
		model.addAttribute("categories", categoryService.getAllCategories());
		return "dish/dishes_list";
	}

	@GetMapping("/add")
	public String showAddForm(Model model) {
		model.addAttribute("dish", new Dish());
		model.addAttribute("categories", categoryService.getAllCategories());
		return "dish/dish_form";
	}

	@PostMapping("/add")
	public String addDish(@ModelAttribute("dish") Dish dish, @RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {
			Path directory = Paths.get("src//main//resources//static/img");
			String rute = directory.toFile().getAbsolutePath();
			try {
				byte[] bytesImg = file.getBytes();
				Path ruteAbsolute = Paths.get(rute + "//" + file.getOriginalFilename());
				Files.write(ruteAbsolute, bytesImg);
				dish.setImage(file.getOriginalFilename());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		dishService.saveOrUpdateDish(dish);
		return "redirect:/dish";
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable("id") Long id, Model model) {
		Dish dish = dishService.getDish(id);

		model.addAttribute("dish", dish);
		model.addAttribute("categories", categoryService.getAllCategories());
		return "dish/dish_edit";
	}

	@PostMapping("/update")
	public String updateDish(@ModelAttribute("dish") Dish dish) {
		dishService.saveOrUpdateDish(dish);
		return "redirect:/dish";
	}

	@GetMapping("delete/{id}")
	public String deleteDish(@PathVariable("id") Long id) {
		dishService.deleteDish(id);
		return "redirect:/dish";
	}

	@GetMapping("/search")
	public String searchByName(@RequestParam("name") String name, Model model,
			@PageableDefault(size = 10, page = 0) Pageable pageable) {
		Page<Dish> page;
		if (name != null && name != "") {
			page = dishService.searchDishByName(name, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
		} else {
			return "redirect:/dish";
		}
		model.addAttribute("page", page);
		var totalPages = page.getTotalPages();
		var currentPage = page.getNumber();
		var start = Math.max(1, currentPage);
		var end = Math.min(currentPage + 20, totalPages);
		if (totalPages > 0) {
			List<Integer> pageNumbers = new ArrayList<>();
			for (int i = start; i <= end; i++) {
				pageNumbers.add(i);
			}
			model.addAttribute("pageNumbers", pageNumbers);
		}
		model.addAttribute("categories", categoryService.getAllCategories());
		return "dish/dishes_list";
	}

	@GetMapping("/searchByCategory")
	public String searchByCategory(@RequestParam("categoryId") Long id, Model model,
			@PageableDefault(size = 10, page = 0) Pageable pageable) {
		Page<Dish> page;
		Category categoria = categoryService.getCategory(id);
		page = dishService.searchDishByCategory(categoria,
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));

		model.addAttribute("page", page);
		var totalPages = page.getTotalPages();
		var currentPage = page.getNumber();
		var start = Math.max(1, currentPage);
		var end = Math.min(currentPage + 20, totalPages);
		if (totalPages > 0) {
			List<Integer> pageNumbers = new ArrayList<>();
			for (int i = start; i <= end; i++) {
				pageNumbers.add(i);
			}
			model.addAttribute("pageNumbers", pageNumbers);
		}
		model.addAttribute("categories", categoryService.getAllCategories());
		model.addAttribute("selectedCategoryId", id);
		return "dish/dishes_list";
	}

	@GetMapping("/searchByPrice")
	public String searchByCategory(@RequestParam("price") BigDecimal price, Model model,
			@PageableDefault(size = 10, page = 0) Pageable pageable) {
		Page<Dish> page;
		page = dishService.searchDishByPrice(price, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
		model.addAttribute("page", page);
		var totalPages = page.getTotalPages();
		var currentPage = page.getNumber();
		var start = Math.max(1, currentPage);
		var end = Math.min(currentPage + 20, totalPages);
		if (totalPages > 0) {
			List<Integer> pageNumbers = new ArrayList<>();
			for (int i = start; i <= end; i++) {
				pageNumbers.add(i);
			}
			model.addAttribute("pageNumbers", pageNumbers);
		}
		model.addAttribute("categories", categoryService.getAllCategories());
		return "dish/dishes_list";
	}

	public DishService getDishService() {
		return dishService;
	}

	public void setDishService(DishService dishService) {
		this.dishService = dishService;
	}

	public CategoryService getCategoryService() {
		return categoryService;
	}

	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

}
