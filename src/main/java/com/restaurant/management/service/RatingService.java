package com.restaurant.management.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.restaurant.management.model.Dish;
import com.restaurant.management.model.Rating;
import com.restaurant.management.model.User;
import com.restaurant.management.repository.RatingOrganitationRepository;
import com.restaurant.management.repository.RatingRepository;

@Service
public class RatingService {
	@Autowired
	RatingRepository ratingRepository;
	@Autowired
	RatingOrganitationRepository ratingOrganitationRepository;

	public Page<Rating> finAllRatings(Pageable pageable) {
		return ratingOrganitationRepository.findAll(pageable);
	}

	public Rating getRating(Long id) {
		Optional<Rating> rating = ratingRepository.findById(id);
		return rating.orElse(null);
	}

	public List<Rating> getAllRatings() {
		return ratingRepository.findAll();
	}

	public void saveOrUpdateDish(Rating rating) {
		ratingRepository.save(rating);
	}

	public boolean existsByUserAndDish(User user, Dish dish) {
		return ratingRepository.existsByUserAndDish(user, dish);
	}

	public Page<Rating> searchRatingByDishName(String name, Pageable pageable) {
		return ratingOrganitationRepository.findByDish_NameContainingIgnoreCase(name, pageable);
	}

	public Page<Rating> searchRatingStars(Integer stars, Pageable pageable) {
		return ratingOrganitationRepository.findByRating(stars, pageable);
	}

	public Page<Rating> searchRatingByDate(Date date, Pageable pageable) {
		return ratingOrganitationRepository.findByRatingDate(date, pageable);
	}

	public void deleteRating(Long id) {
		ratingRepository.deleteById(id);
	}

	public List<Rating> searchRatingDateBetween(Date startDate, Date endDate) {
		return ratingRepository.findByRatingDateBetween(startDate, endDate);
	}

}
