package com.restaurant.management.repository;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.restaurant.management.model.Category;
import com.restaurant.management.model.Dish;
@Repository
public interface DishOrganitationRepository extends PagingAndSortingRepository<Dish, Long>{
	 Page<Dish> findByNameStartingWithIgnoreCase(String name, Pageable pageable);
	 Page<Dish> findByCategory(Category category, Pageable pageable);
	 Page<Dish> findByPrice(BigDecimal price, Pageable pageable);
}
