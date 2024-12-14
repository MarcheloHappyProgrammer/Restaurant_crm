package com.restaurant.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ReportController {
	@GetMapping("/reports")
	public String showReportsPage() {
		return "/reports/reports_page";
	}
	
}
