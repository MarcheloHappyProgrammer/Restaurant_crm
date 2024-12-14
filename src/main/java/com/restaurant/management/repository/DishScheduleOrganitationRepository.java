package com.restaurant.management.repository;


import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.restaurant.management.model.DishSchedule;
@Repository
public interface DishScheduleOrganitationRepository extends PagingAndSortingRepository<DishSchedule, Long>{
	Page<DishSchedule> findByScheduledDate(Date date, Pageable pageable);
	Page<DishSchedule> findByScheduledDateBetween(Date startDate, Date endDate, Pageable pageable);
}
