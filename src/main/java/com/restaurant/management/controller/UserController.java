package com.restaurant.management.controller;

import java.io.InputStream;
import java.time.LocalDate;
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

import com.restaurant.management.model.Dish;
import com.restaurant.management.model.DishSchedule;
import com.restaurant.management.model.Rating;
import com.restaurant.management.model.User;
import com.restaurant.management.service.DishScheduleService;
import com.restaurant.management.service.DishService;
import com.restaurant.management.service.RatingService;
import com.restaurant.management.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
@RequestMapping("/principal")
public class UserController {
	@Autowired
	UserService userService;
	@Autowired
	DishScheduleService dishScheduleService;
	@Autowired
	DishService dishService;
	@Autowired
	RatingService ratingService;

	@GetMapping("/users")
	public String listUsers(Model model, @PageableDefault(size = 10, page = 0) Pageable pageable) {
		Page<User> page = userService.finAllUsers(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
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
		return "/user/users_list";
	}

	@GetMapping("/menu/{id}")
	public String showMenu(@PathVariable("id") Long id, Model model,
			@PageableDefault(size = 10, page = 0) Pageable pageable) {
		Date today = java.sql.Date.valueOf(LocalDate.now());
		List<Dish> dishes = dishService.getAllDishes();
		User user = userService.getUser(id);
		Page<DishSchedule> page = dishScheduleService.finByDateDishSchedule(today,
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));

		model.addAttribute("page", page);
		int totalPages = page.getTotalPages();
		int currentPage = page.getNumber();
		int start = Math.max(1, currentPage);
		int end = Math.min(currentPage + 20, totalPages);
		if (totalPages > 0) {
			List<Integer> pageNumbers = new ArrayList<>();
			for (int i = start; i <= end; i++) {
				pageNumbers.add(i);
			}
			model.addAttribute("pageNumbers", pageNumbers);
		}
		model.addAttribute("currentPage", "menuToday");
		model.addAttribute("id", id);
		model.addAttribute("title", "Platos del Día");
		model.addAttribute("mensaje_null", "No hay platos programados para hoy");
		List<Long> dishes_id = new ArrayList<>();
		for (Dish dish : dishes) {
			boolean hasRated = ratingService.existsByUserAndDish(user, dish);
			if (hasRated) {
				dishes_id.add(dish.getId());
			}
		}
		model.addAttribute("list_ids_rated", dishes_id);
		return "customers/menu";
	}

	@GetMapping("/menuTomorrow/{id}")
	public String showMenuTomorrow(@PathVariable("id") Long id, Model model,
			@PageableDefault(size = 10, page = 0) Pageable pageable) {
		Date today = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
		List<Dish> dishes = dishService.getAllDishes();
		User user = userService.getUser(id);
		Page<DishSchedule> page = dishScheduleService.finByDateDishSchedule(today,
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));

		model.addAttribute("page", page);
		int totalPages = page.getTotalPages();
		int currentPage = page.getNumber();
		int start = Math.max(1, currentPage);
		int end = Math.min(currentPage + 20, totalPages);
		if (totalPages > 0) {
			List<Integer> pageNumbers = new ArrayList<>();
			for (int i = start; i <= end; i++) {
				pageNumbers.add(i);
			}
			model.addAttribute("pageNumbers", pageNumbers);
		}
		model.addAttribute("currentPage", "menuTomorrow");
		model.addAttribute("id", id);
		model.addAttribute("title", "Platos para mañana");
		model.addAttribute("mensaje_null", "No hay platos programados para mañana");
		List<Long> dishes_id = new ArrayList<>();
		for (Dish dish : dishes) {
			boolean hasRated = ratingService.existsByUserAndDish(user, dish);
			if (hasRated) {
				dishes_id.add(dish.getId());
			}
		}
		model.addAttribute("list_ids_rated", dishes_id);
		return "customers/menu";
	}

	@GetMapping("/menuAll/{id}")
	public String listAllDishes(@PathVariable("id") Long id, Model model,
			@PageableDefault(size = 10, page = 0) Pageable pageable) {
		Page<Dish> page = dishService.finAllDish(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
		model.addAttribute("page", page);
		int totalPages = page.getTotalPages();
		int currentPage = page.getNumber();
		int start = Math.max(1, currentPage);
		int end = Math.min(currentPage + 20, totalPages);
		if (totalPages > 0) {
			List<Integer> pageNumbers = new ArrayList<>();
			for (int i = start; i <= end; i++) {
				pageNumbers.add(i);
			}
			model.addAttribute("pageNumbers", pageNumbers);
		}
		model.addAttribute("currentPage", "menuAll");
		model.addAttribute("id", id);
		model.addAttribute("title", "Todos Nuestros Platos");
		model.addAttribute("mensaje_null", "No hay platos registrados.");
		return "customers/menuAll";
	}

	@GetMapping("/login")
	public String showLogin(Model model) {
		model.addAttribute("user", new User());
		return "customers/login";
	}
	@GetMapping("/login2")
	public String showLogin2(Model model) {
		model.addAttribute("user", new User());
		return "customers/login2";
	}
	@PostMapping("/getInto")
	public String registerUser(@ModelAttribute("user") User user) {
		User existingUser = userService.findByDni(user.getDni());

		if (existingUser != null) {
			return "redirect:/principal/menu/" + existingUser.getId() + "?loginSuccess=true&name="
					+ existingUser.getFullName();
		} else {
			userService.saveOrUpdateUser(user);
			return "redirect:/principal/menu/" + user.getId() + "?loginSuccess=true&name="
					+ user.getFullName();
		}
	}

	@GetMapping("/loginAdmin")
	public String showLoginAdmin(Model model) {
		return "customers/loginAdmin";
	}

	@PostMapping("getIntoAdmin")
	public String loginAdmin(@RequestParam("nombre") String nombre, @RequestParam("password") String password,
			Model model) {
		if ("admin".equals(nombre) && "admin".equals(password)) {
			return "redirect:/dishSchedule?loginSuccess=true";
		} else {
			model.addAttribute("errorMessage", "Usuario o contraseña incorrectas.");
			return "customers/loginAdmin";
		}
	}

	@GetMapping("/rate/{dishId}/{userId}")
	public String rateDish(@PathVariable("dishId") Long dishId, @PathVariable("userId") Long idUser, Model model) {
		Dish dish = dishService.getDish(dishId);
		model.addAttribute("dish", dish);
		model.addAttribute("title", "Calificar");
		model.addAttribute("id", idUser);
		return "customers/rate_dish";
	}

	@PostMapping("/rate/{dishId}/{userId}")
	public String rateDish(@PathVariable("dishId") Long dishId, @PathVariable("userId") Long idUser,
			@RequestParam("rating") int rating, @RequestParam("comment") String comment) {
		User user = userService.getUser(idUser);
		if(user.getRatingCount()!=null) {
			int n = user.getRatingCount();
			user.setRatingCount(n + 1);
		}else {
			user.setRatingCount(1);
		}
		
		Dish dish = dishService.getDish(dishId);
		Rating ratingDish = new Rating();
		ratingDish.setDish(dish);
		ratingDish.setRating(rating);
		ratingDish.setUser(user);
		ratingDish.setComment(comment);
		ratingService.saveOrUpdateDish(ratingDish);
		return "redirect:/principal/menu/" + idUser + "?calification=true";
	}

	@GetMapping("/users/searchByName")
	public String searchByName(@RequestParam("name") String name, Model model,
			@PageableDefault(size = 10, page = 0) Pageable pageable) {
		Page<User> page;
		if (name != null && name != "") {
			page = userService.searchUserByName(name, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
		} else {
			return "redirect:/principal/users";
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
		return "/user/users_list";
	}

	@GetMapping("/users/searchByDni")
	public String searchByDni(@RequestParam("dni") String dni, Model model,
			@PageableDefault(size = 10, page = 0) Pageable pageable) {
		Page<User> page;
		if (dni != null && dni != "") {
			page = userService.searchUserByDni(dni, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
		} else {
			return "redirect:/principal/users";
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
		return "/user/users_list";
	}

	@GetMapping("/users/searchByDate")
	public String searchByDate(
			@RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
			Model model, @PageableDefault(size = 10, page = 0) Pageable pageable) {
		Page<User> page;
		if (date == null) {
			return "redirect:/principal/users";

		} else {
			page = userService.searchUserByDate(date, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
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
		return "/user/users_list";
	}

	@GetMapping("/user/reports")
	public void generateReport(HttpServletResponse response) throws Exception {
		List<User> empleados = userService.findAllUsers();
		InputStream reportStream = getClass().getResourceAsStream("/reports/customers_report.jrxml");
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(empleados);
		Map<String, Object> parameters = new HashMap<String, Object>();
		JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=reporte_clientes.pdf");
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
	}

	@GetMapping("/user/reports/excel")
	public void generateExcelReport(HttpServletResponse response) throws Exception {
		List<User> users = userService.findAllUsers();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("reporte_clientes");
		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("User ID");
		headerRow.createCell(1).setCellValue("Full Name");
		headerRow.createCell(2).setCellValue("DNI");
		headerRow.createCell(3).setCellValue("Last Login");
		headerRow.createCell(4).setCellValue("Rating Count");
		int rowNum = 1;
		for (User user : users) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(user.getId());
			row.createCell(1).setCellValue(user.getFullName());
			row.createCell(2).setCellValue(user.getDni());
			row.createCell(3).setCellValue(user.getLastLogin() != null ? user.getLastLogin().toString() : "");
			row.createCell(4).setCellValue(user.getRatingCount());
		}
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=reporte_clientes.xlsx");
		workbook.write(response.getOutputStream());
		workbook.close();
	}

	@GetMapping("/user/reports/range")
	public void generateReportByRange(HttpServletResponse response,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate)
			throws Exception {
		List<User> usuarios = userService.searchLastLoginBetween(startDate, endDate);
		InputStream reportStream = getClass().getResourceAsStream("/reports/customers_report.jrxml");
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(usuarios);
		Map<String, Object> parameters = new HashMap<String, Object>();
		JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=reporte_clientes.pdf");
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
	}

	@GetMapping("/user/reports/range/excel")
	public void generateExcelReportByRange(HttpServletResponse response,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate)
			throws Exception {
		List<User> users = userService.searchLastLoginBetween(startDate, endDate);
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("reporte_clientes");
		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("User ID");
		headerRow.createCell(1).setCellValue("Full Name");
		headerRow.createCell(2).setCellValue("DNI");
		headerRow.createCell(3).setCellValue("Last Login");
		headerRow.createCell(4).setCellValue("Rating Count");
		int rowNum = 1;
		for (User user : users) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(user.getId());
			row.createCell(1).setCellValue(user.getFullName());
			row.createCell(2).setCellValue(user.getDni());
			row.createCell(3).setCellValue(user.getLastLogin() != null ? user.getLastLogin().toString() : "");
			row.createCell(4).setCellValue(user.getRatingCount());
		}
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=reporte_clientes.xlsx");
		workbook.write(response.getOutputStream());
		workbook.close();
	}

}
