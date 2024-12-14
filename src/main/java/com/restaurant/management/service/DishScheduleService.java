package com.restaurant.management.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.restaurant.management.model.Dish;
import com.restaurant.management.model.DishSchedule;
import com.restaurant.management.repository.DishScheduleOrganitationRepository;
import com.restaurant.management.repository.DishScheduleRepository;

@Service
public class DishScheduleService {
	@Autowired
	DishScheduleRepository dishScheduleRepository;
	@Autowired
	DishScheduleOrganitationRepository dishScheduleOrganitationRepository;

	public Page<DishSchedule> finAllDishSchedule(Pageable pageable) {
		return dishScheduleOrganitationRepository.findAll(pageable);
	}

	public Page<DishSchedule> finByDateDishSchedule(Date date, Pageable pageable) {
		return dishScheduleOrganitationRepository.findByScheduledDate(date, pageable);
	}
	public Page<DishSchedule> findByDateRange(Date startDate, Date endDate, Pageable pageable) {
	    return dishScheduleOrganitationRepository.findByScheduledDateBetween(startDate, endDate, pageable);
	}

	public List<DishSchedule> getAllDishSchedule() {
		return dishScheduleRepository.findAll();
	}

	public DishSchedule getDishSchedule(Long id) {
		Optional<DishSchedule> dishSchedule = dishScheduleRepository.findById(id);
		return dishSchedule.orElse(null);
	}

	public void saveOrUpdateDishSchedule(DishSchedule dishSchedule) {
		if (dishSchedule.getId() == null) {
			if (existsByDishAndDate(dishSchedule.getDish(), dishSchedule.getScheduledDate())) {
				throw new IllegalArgumentException("El plato ya está programado para esa fecha.");
			}
		} else {
			if (existsDuplicateExcludingCurrent(dishSchedule)) {
				throw new IllegalArgumentException("El plato ya está programado para esa fecha.");
			}
		}
		dishScheduleRepository.save(dishSchedule);
	}

	public void deleteDishSchedule(Long id) {
		dishScheduleRepository.deleteById(id);
	}

	public List<DishSchedule> getDishScheduleByDate(Date date) {
		return dishScheduleRepository.findByScheduledDate(date);
	}

	public List<DishSchedule> findByScheduledDateBetween(Date startDate, Date endDate) {
		return dishScheduleRepository.findByScheduledDateBetween(startDate, endDate);
	}

	public boolean existsByDishAndDate(Dish dish, Date scheduledDate) {
		return dishScheduleRepository.existsByDishAndScheduledDate(dish, scheduledDate);
	}

	public boolean existsDuplicateExcludingCurrent(DishSchedule dishSchedule) {
		return dishScheduleRepository.existsByDishAndScheduledDateAndIdNot(dishSchedule.getDish(),
				dishSchedule.getScheduledDate(), dishSchedule.getId());
	}

}
