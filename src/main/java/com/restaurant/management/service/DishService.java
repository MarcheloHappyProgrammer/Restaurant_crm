package com.restaurant.management.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.restaurant.management.model.Category;
import com.restaurant.management.model.Dish;
import com.restaurant.management.repository.DishOrganitationRepository;
import com.restaurant.management.repository.DishRepository;

@Service
public class DishService {
	@Autowired
	DishRepository dishRepository;
	@Autowired
	DishOrganitationRepository dishOrganitationRepository;

	public Page<Dish> finAllDish(Pageable pageable) {
		return dishOrganitationRepository.findAll(pageable);
	}

	public Page<Dish> searchDishByName(String name, Pageable pageable) {
		return dishOrganitationRepository.findByNameStartingWithIgnoreCase(name, pageable);
	}

	public Page<Dish> searchDishByCategory(Category category, Pageable pageable) {
		return dishOrganitationRepository.findByCategory(category, pageable);
	}

	public Page<Dish> searchDishByPrice(BigDecimal price, Pageable pageable) {
		return dishOrganitationRepository.findByPrice(price, pageable);
	}

	public List<Dish> getAllDishes() {
		return dishRepository.findAll();
	}

	public Dish getDish(Long id) {
		Optional<Dish> dish = dishRepository.findById(id);
		return dish.orElse(null);
	}

	public void saveOrUpdateDish(Dish dish) {
		dishRepository.save(dish);
	}

	public void deleteDish(Long id) {
		dishRepository.deleteById(id);
	}

}
