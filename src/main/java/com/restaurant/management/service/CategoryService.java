package com.restaurant.management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restaurant.management.model.Category;
import com.restaurant.management.repository.CategoryRepository;

@Service
public class CategoryService {
	@Autowired
	CategoryRepository categoryRepository;
	
	public List<Category> getAllCategories(){
		return categoryRepository.findAll();
	}
	public Category getCategory(Long id) {
		Optional<Category> category = categoryRepository.findById(id);
		return category.orElse(null);
	}
	
	public void saveOrUpdateCategory(Category category) {
		categoryRepository.save(category);
	}
	public void deleteCategory(Long id) {
		categoryRepository.deleteById(id);
	}
	public List<Category> searchCategoriesByName(String name){
		List<Category> categories = categoryRepository.findByNameStartingWithIgnoreCase(name);
		return categories;
	}
}
