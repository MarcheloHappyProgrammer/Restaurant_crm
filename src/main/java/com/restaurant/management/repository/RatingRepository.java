package com.restaurant.management.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.restaurant.management.model.Dish;
import com.restaurant.management.model.Rating;
import com.restaurant.management.model.User;
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long>{
	boolean existsByUserAndDish(User user, Dish dish);
	List<Rating> findByRatingDateBetween(Date startDate, Date endDate);
	@Query("SELECT COUNT(r) FROM Rating r WHERE r.user.id = :userId")
    long countRatingsByUserId(@Param("userId") Long userId);
}
