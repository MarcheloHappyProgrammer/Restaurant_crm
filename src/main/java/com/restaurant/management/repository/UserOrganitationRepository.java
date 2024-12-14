package com.restaurant.management.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.restaurant.management.model.User;

@Repository
public interface UserOrganitationRepository extends PagingAndSortingRepository<User, Long>{
	Page<User> findByLastLogin(Date date, Pageable pageable);
	Page<User> findByFullNameStartingWithIgnoreCase(String fullName, Pageable pageable);
	 Page<User> findByDniStartingWithIgnoreCase(String dni, Pageable pageable);
}
