package com.restaurant.management.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import com.restaurant.management.model.Dish;
import com.restaurant.management.model.Rating;
import com.restaurant.management.service.DishService;
import com.restaurant.management.service.RatingService;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
@RequestMapping("/rating")
public class RatingController {
	@Autowired
	RatingService ratingService;
	@Autowired
	DishService dishService;

	@GetMapping("/")
	public RedirectView redirectDishWithTrailingSlash() {
		return new RedirectView("/rating");
	}

	@GetMapping
	public String listRatings(Model model, @PageableDefault(size = 10, page = 0) Pageable pageable) {
		Page<Rating> page = ratingService
				.finAllRatings(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
		model.addAttribute("page", page);
		var totalPages = page.getTotalPages();
		var currentPage = page.getNumber();
		var start = Math.max(1, currentPage);
		var end = Math.min(currentPage + 20, totalPages);
		if (totalPages > 0) {
			List<Integer> pageNumbers = new ArrayList<>();
			for (int i = start; i <= end; i++) {
				pageNumbers.add(i);
			}
			model.addAttribute("pageNumbers", pageNumbers);
		}
		
		model.addAttribute("dishes", dishService.getAllDishes());
		return "rating/ratings_list";
	}

	@GetMapping("/searchByDishName")
	public String searchByCategory(@RequestParam("dishId") Long id, Model model,
			@PageableDefault(size = 10, page = 0) Pageable pageable) {
		Page<Rating> page;
		Dish dish = dishService.getDish(id);
		page = ratingService.searchRatingByDishName(dish.getName(),
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));

		model.addAttribute("page", page);
		var totalPages = page.getTotalPages();
		var currentPage = page.getNumber();
		var start = Math.max(1, currentPage);
		var end = Math.min(currentPage + 20, totalPages);
		if (totalPages > 0) {
			List<Integer> pageNumbers = new ArrayList<>();
			for (int i = start; i <= end; i++) {
				pageNumbers.add(i);
			}
			model.addAttribute("pageNumbers", pageNumbers);
		}
		model.addAttribute("dishes", dishService.getAllDishes());
		model.addAttribute("selectedDishId", id);
		return "rating/ratings_list";
	}

	@GetMapping("/searchByStars")
	public String searchByStars(@RequestParam("stars") Integer stars, Model model,
			@PageableDefault(size = 10, page = 0) Pageable pageable) {
		Page<Rating> page;
		page = ratingService.searchRatingStars(stars, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));

		model.addAttribute("page", page);
		var totalPages = page.getTotalPages();
		var currentPage = page.getNumber();
		var start = Math.max(1, currentPage);
		var end = Math.min(currentPage + 20, totalPages);
		if (totalPages > 0) {
			List<Integer> pageNumbers = new ArrayList<>();
			for (int i = start; i <= end; i++) {
				pageNumbers.add(i);
			}
			model.addAttribute("pageNumbers", pageNumbers);
		}
		model.addAttribute("dishes", dishService.getAllDishes());
		model.addAttribute("selectedStars", stars);
		return "rating/ratings_list";
	}

	@GetMapping("/searchByDate")
	public String searchByDate(
			@RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
			Model model, @PageableDefault(size = 10, page = 0) Pageable pageable) {
		Page<Rating> page;
		if (date == null) {
			page = ratingService.finAllRatings(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
		} else {
			page = ratingService.searchRatingByDate(date,
					PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
		}

		model.addAttribute("page", page);
		var totalPages = page.getTotalPages();
		var currentPage = page.getNumber();
		var start = Math.max(1, currentPage);
		var end = Math.min(currentPage + 20, totalPages);
		if (totalPages > 0) {
			List<Integer> pageNumbers = new ArrayList<>();
			for (int i = start; i <= end; i++) {
				pageNumbers.add(i);
			}
			model.addAttribute("pageNumbers", pageNumbers);
		}
		return "rating/ratings_list";
	}


	@GetMapping("/reports")
	public void generateReport(HttpServletResponse response) throws Exception {
		List<Rating> ratings = ratingService.getAllRatings();
		InputStream reportStream = getClass().getResourceAsStream("/reports/ratings_report.jrxml");
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(ratings);
		Map<String, Object> parameters = new HashMap<String, Object>();
		JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=reporte_calificaciones.pdf");
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
	}
	@GetMapping("/reports/excel")
	public void generateExcelReport(HttpServletResponse response) throws Exception {
		List<Rating> ratings = ratingService.getAllRatings();

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("reporte_calificaciones");

		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("Dish Name");
		headerRow.createCell(1).setCellValue("Category");
		headerRow.createCell(2).setCellValue("Rating (Stars)");
		headerRow.createCell(3).setCellValue("Comment");
		headerRow.createCell(4).setCellValue("Rating Date");

		int rowNum = 1;
		for (Rating rating : ratings) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(rating.getDish().getName());
			row.createCell(1).setCellValue(rating.getDish().getCategory().getName());
			row.createCell(2).setCellValue(rating.getRating());
			row.createCell(3).setCellValue(rating.getComment());
			row.createCell(4).setCellValue(rating.getRatingDate().toString());
		}

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=reporte_calificaciones.xlsx");
		workbook.write(response.getOutputStream());
		workbook.close();
	}

	@GetMapping("delete/{id}")
	public String deleteRating(@PathVariable("id") Long id) {
		ratingService.deleteRating(id);
		return "redirect:/rating";
	}

	@GetMapping("/reports/range")
	public void generateReportyRange(HttpServletResponse response,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate)
			throws Exception {
		List<Rating> ratings = ratingService.searchRatingDateBetween(startDate, endDate);
		InputStream reportStream = getClass().getResourceAsStream("/reports/ratings_report.jrxml");
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(ratings);
		Map<String, Object> parameters = new HashMap<String, Object>();
		JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=reporte_calificaciones.pdf");
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
	}

	@GetMapping("/reports/range/excel")
	public void generateExcelReportyRange(HttpServletResponse response,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate)
			throws Exception {
		List<Rating> ratings = ratingService.searchRatingDateBetween(startDate, endDate);
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("reporte_calificaciones");

		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("Dish Name");
		headerRow.createCell(1).setCellValue("Category");
		headerRow.createCell(2).setCellValue("Rating (Stars)");
		headerRow.createCell(3).setCellValue("Comment");
		headerRow.createCell(4).setCellValue("Rating Date");

		int rowNum = 1;
		for (Rating rating : ratings) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(rating.getDish().getName());
			row.createCell(1).setCellValue(rating.getDish().getCategory().getName());
			row.createCell(2).setCellValue(rating.getRating());
			row.createCell(3).setCellValue(rating.getComment());
			row.createCell(4).setCellValue(rating.getRatingDate().toString());
		}

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=reporte_calificaciones.xlsx");
		workbook.write(response.getOutputStream());
		workbook.close();
	}
	

}
