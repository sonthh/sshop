package com.itsontran.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itsontran.model.dao.CategoryDAO;
import com.itsontran.model.dao.ContactDAO;
import com.itsontran.model.dao.OrderDAO;
import com.itsontran.model.dao.ProductDAO;
import com.itsontran.model.dao.SlideDAO;
import com.itsontran.model.dao.UserDAO;

@Controller
@RequestMapping("admin")
public class AdminIndexController {
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private ContactDAO contactDAO;
	
	@Autowired
	private OrderDAO orderDAO;
	
	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private SlideDAO slideDAO;
	
	@Autowired
	private CategoryDAO categoryDAO;
	
	@GetMapping("")
	public String index(ModelMap modelMap) {
		int countUser = userDAO.countItems();
		modelMap.addAttribute("countUser", countUser);
		
		int countContact = contactDAO.countItems();
		modelMap.addAttribute("countContact", countContact);
		
		int countOrder = orderDAO.countItems();
		modelMap.addAttribute("countOrder", countOrder);
		
		int countProduct = productDAO.countItems();
		modelMap.addAttribute("countProduct", countProduct);
		
		int countSlide = slideDAO.countItems();
		modelMap.addAttribute("countSlide", countSlide);
		
		int countCategory = categoryDAO.countItems();
		modelMap.addAttribute("countCategory", countCategory);
		
		return "admin.index.index";
	}
	
}
