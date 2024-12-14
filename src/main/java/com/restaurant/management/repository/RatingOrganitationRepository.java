package com.restaurant.management.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.restaurant.management.model.Rating;
@Repository
public interface RatingOrganitationRepository extends PagingAndSortingRepository<Rating, Long>{
	Page<Rating> findByDish_NameContainingIgnoreCase(String dishName, Pageable pageable);
	Page<Rating> findByRating(Integer rating, Pageable pageable);
	Page<Rating> findByRatingDate(Date date, Pageable pageable);
}
