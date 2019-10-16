package com.itsontran.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
	
	@GetMapping("403.html")
	public String error403() {
		return "error/403";
	}
	
	@GetMapping("404.html")
	public String error404() {
		return "error/404";
	}
}
