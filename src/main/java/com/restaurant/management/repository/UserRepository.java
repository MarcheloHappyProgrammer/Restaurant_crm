package com.restaurant.management.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.restaurant.management.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	User findByDni(String dni);
	List<User> findByLastLoginBetween(Date startDate, Date endDate);
}
