package com.restaurant.management.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restaurant.management.model.Dish;

@Repository
public interface DishRepository extends JpaRepository<Dish,Long>{
	
}
