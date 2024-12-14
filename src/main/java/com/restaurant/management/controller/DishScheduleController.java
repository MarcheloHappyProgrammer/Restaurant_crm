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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.restaurant.management.model.DishSchedule;
import com.restaurant.management.service.CategoryService;
import com.restaurant.management.service.DishScheduleService;
import com.restaurant.management.service.DishService;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
@RequestMapping("/dishSchedule")
public class DishScheduleController {
	@Autowired
	private DishScheduleService dishScheduleService;
	@Autowired
	private DishService dishService;
	@Autowired
	private CategoryService categoryService;

	@GetMapping
	public String listDishSchedule(Model model, @PageableDefault(size = 10, page = 0) Pageable pageable) {
		Page<DishSchedule> page = dishScheduleService
				.finAllDishSchedule(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
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
		return "dish_schedule/dishSchedule_list";
	}

	@GetMapping("/search")
	public String searchByDate(
			@RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
			Model model, @PageableDefault(size = 10, page = 0) Pageable pageable) {
		Page<DishSchedule> page;
		if (date == null) {
			page = dishScheduleService
					.finAllDishSchedule(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
		} else {
			page = dishScheduleService.finByDateDishSchedule(date,
					PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
		}

		model.addAttribute("page", page);
		var totalPages = page.getTotalPages();
		var currentPage = page.getNumber();
		var start = Math.max(1, currentPage);
		var end = Math.min(currentPage + 20, totalPages);
		model.addAttribute("dishSchedule", dishScheduleService.getAllDishSchedule());
		if (totalPages > 0) {
			List<Integer> pageNumbers = new ArrayList<>();
			for (int i = start; i <= end; i++) {
				pageNumbers.add(i);
			}
			model.addAttribute("pageNumbers", pageNumbers);
		}
		return "dish_schedule/dishSchedule_list";
	}

	@GetMapping("/add")
	public String addDishScheduleForm(Model model) {
		model.addAttribute("dishSchedule", new DishSchedule());
		model.addAttribute("dishes", dishService.getAllDishes());
		model.addAttribute("categories", categoryService.getAllCategories());
		return "dish_schedule/dishSchedule_form";
	}

	@PostMapping("/save")
	public String addDishSchedule(@ModelAttribute("dishSchedule") DishSchedule dishSchedule, Model model) {
		try {
			dishScheduleService.saveOrUpdateDishSchedule(dishSchedule);
			return "redirect:/dishSchedule";
		} catch (IllegalArgumentException e) {
			model.addAttribute("errorMessage", e.getMessage());
			model.addAttribute("dishSchedule", dishSchedule);
			model.addAttribute("dishes", dishService.getAllDishes());
			model.addAttribute("categories", categoryService.getAllCategories());
			return "dish_schedule/dishSchedule_form";
		}
	}

	@GetMapping("/edit/{id}")
	public String editDishScheduleForm(@PathVariable("id") Long id, Model model) {
		DishSchedule dishSchedule = dishScheduleService.getDishSchedule(id);
		model.addAttribute("dishSchedule", dishSchedule);
		model.addAttribute("dishes", dishService.getAllDishes());
		model.addAttribute("categories", categoryService.getAllCategories());
		return "dish_schedule/dishSchedule_edit";
	}

	@PostMapping("/update")
	public String editDishSchedule(DishSchedule dishSchedule, Model model) {
		try {
			dishScheduleService.saveOrUpdateDishSchedule(dishSchedule);
			return "redirect:/dishSchedule";
		} catch (IllegalArgumentException e) {
			model.addAttribute("errorMessage", e.getMessage());
			model.addAttribute("dishSchedule", dishSchedule);
			model.addAttribute("dishes", dishService.getAllDishes());
			model.addAttribute("categories", categoryService.getAllCategories());
			return "dish_schedule/dishSchedule_edit";
		}
	}

	@GetMapping("/delete/{id}")
	public String deleteDishSchedule(@PathVariable("id") Long id) {
		dishScheduleService.deleteDishSchedule(id);
		return "redirect:/dishSchedule";
	}

	@GetMapping("/searchByRange")
	public String searchByDateRange(
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			Model model, @PageableDefault(size = 10, page = 0) Pageable pageable) {

		Page<DishSchedule> page;
		if (startDate == null && endDate == null) {
			return "redirect:/dishSchedule";
		} else {
			page = dishScheduleService.findByDateRange(startDate, endDate,
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

		return "dish_schedule/dishSchedule_list";
	}

	@GetMapping("/reports")
	public void generateReport(HttpServletResponse response) throws Exception {
		List<DishSchedule> dishSchedule = dishScheduleService.getAllDishSchedule();
		InputStream reportStream = getClass().getResourceAsStream("/reports/dish_schedule_report.jrxml");
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dishSchedule);
		Map<String, Object> parameters = new HashMap<String, Object>();
		JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=reporte_programaciones.pdf");
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
	}

	@GetMapping("/reports/excel")
	public void generateExcelReport(HttpServletResponse response) throws Exception {
		List<DishSchedule> dishSchedule = dishScheduleService.getAllDishSchedule();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("reporte_programacion_platos");
		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("Scheduled Date");
		headerRow.createCell(1).setCellValue("Dish Name");
		headerRow.createCell(2).setCellValue("Category");

		int rowNum = 1;
		for (DishSchedule schedule : dishSchedule) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(schedule.getScheduledDate().toString());
			row.createCell(1).setCellValue(schedule.getDish().getName());
			row.createCell(2).setCellValue(schedule.getDish().getCategory().getName());

		}
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=reporte_programacion_platos.xlsx");
		workbook.write(response.getOutputStream());
		workbook.close();
	}

	@GetMapping("/reports/range")
	public void generateReportByRange(HttpServletResponse response,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate)
			throws Exception {
		List<DishSchedule> dishSchedule = dishScheduleService.findByScheduledDateBetween(startDate, endDate);
		InputStream reportStream = getClass().getResourceAsStream("/reports/dish_schedule_report.jrxml");
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dishSchedule);
		Map<String, Object> parameters = new HashMap<String, Object>();
		JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=reporte_programaciones.pdf");
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

	}

	@GetMapping("/reports/range/excel")
	public void generateExcelReportByRange(HttpServletResponse response,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate)
			throws Exception {
		List<DishSchedule> dishSchedule = dishScheduleService.findByScheduledDateBetween(startDate, endDate);
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("reporte_programacion_platos");
		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("Scheduled Date");
		headerRow.createCell(1).setCellValue("Dish Name");
		headerRow.createCell(2).setCellValue("Category");

		int rowNum = 1;
		for (DishSchedule schedule : dishSchedule) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(schedule.getScheduledDate().toString());
			row.createCell(1).setCellValue(schedule.getDish().getName());
			row.createCell(2).setCellValue(schedule.getDish().getCategory().getName());

		}
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=reporte_programacion_platos.xlsx");
		workbook.write(response.getOutputStream());
		workbook.close();

	}
}
