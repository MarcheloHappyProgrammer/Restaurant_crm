package com.restaurant.management.model;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Dish {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;
	private BigDecimal price;
	private String image;
	@OneToMany(mappedBy = "dish", cascade = CascadeType.ALL)
	private List<Rating> ratings;
	@OneToMany(mappedBy = "dish", cascade = CascadeType.ALL)
	private List<DishSchedule> dishSchedule;
	public List<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}

	public List<DishSchedule> getDishSchedule() {
		return dishSchedule;
	}

	public void setDishSchedule(List<DishSchedule> dishSchedule) {
		this.dishSchedule = dishSchedule;
	}

	

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public double getAverageRating() {
		if (ratings == null || ratings.isEmpty()) {
			return 0.0;
		}
		return ratings.stream().mapToDouble(Rating::getRating) 
				.average().orElse(0.0);
	}

}
