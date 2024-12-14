package com.restaurant.management.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.restaurant.management.model.User;
import com.restaurant.management.repository.RatingRepository;
import com.restaurant.management.repository.UserOrganitationRepository;
import com.restaurant.management.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserOrganitationRepository userOrganitationRepository;
	@Autowired
	RatingRepository ratingRepository;

	public Page<User> finAllUsers(Pageable pageable) {
		return userOrganitationRepository.findAll(pageable);
	}

	public Page<User> searchUserByDate(Date date, Pageable pageable) {
		return userOrganitationRepository.findByLastLogin(date, pageable);
	}

	public Page<User> searchUserByName(String name, Pageable pageable) {
		return userOrganitationRepository.findByFullNameStartingWithIgnoreCase(name, pageable);
	}

	public Page<User> searchUserByDni(String dni, Pageable pageable) {
		return userOrganitationRepository.findByDniStartingWithIgnoreCase(dni, pageable);
	}

	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	public User getUser(Long id) {
		Optional<User> user = userRepository.findById(id);
		return user.orElse(null);
	}

	public void saveOrUpdateUser(User user) {
		userRepository.save(user);
	}

	public User findByDni(String dni) {
		return userRepository.findByDni(dni);
	}

	public List<User> searchLastLoginBetween(Date startDate, Date endDate) {
		return userRepository.findByLastLoginBetween(startDate, endDate);
	}

	
}
