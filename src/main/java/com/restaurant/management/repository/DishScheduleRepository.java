package com.restaurant.management.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restaurant.management.model.Dish;
import com.restaurant.management.model.DishSchedule;
@Repository
public interface DishScheduleRepository extends JpaRepository<DishSchedule, Long> {
	List<DishSchedule> findByScheduledDate(Date scheduledDate);

	List<DishSchedule> findByScheduledDateBetween(Date startDate, Date endDate);
	boolean existsByDishAndScheduledDateAndIdNot(Dish dish, Date scheduledDate, Long id);
	boolean existsByDishAndScheduledDate(Dish dish, Date scheduledDate);
	
}
