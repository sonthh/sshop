package com.itsontran.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itsontran.model.bean.Role;
import com.itsontran.model.bean.User;
import com.itsontran.model.dao.UserDAO;
import com.itsontran.service.SendMailService;


@Controller
@RequestMapping("auth")
public class AuthController {
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private SendMailService sendMailService;
	
	@Autowired
	private BCryptPasswordEncoder bCrypt;

	@GetMapping("login")
	public String login() {
		return "auth/login";
	}
	
	@GetMapping("sign-up")
	public String signUp() {
		return "auth/sign-up";
	}
	
	@GetMapping("forgot-password")
	public String forgotPassword() {
		return "auth/forgot-password";
	}
	
	@GetMapping("notification")
	public String notification() {
		return "auth/notification";
	}
	
	@PostMapping("sign-up")
	public String register(@Valid @ModelAttribute("objUser") User objUser, BindingResult errors,
			@RequestParam("repassword") String repassword, RedirectAttributes redirectAttributes,
			ModelMap modelMap, HttpServletRequest request) {
		System.out.println(objUser);
		
		//email ko được rỗng
		objUser.setEmail(objUser.getEmail().trim());
		if (objUser.getEmail().equals("")) {
			errors.rejectValue("email", "objUser", "Vui lòng nhập email.");
		} else if (userDAO.countEmail(objUser.getEmail()) > 0) {
			errors.rejectValue("email", "objUser", "Email đã tồn tại.");
		}
		
		//tồn tại username
		if (userDAO.hasUserByUserName(objUser.getUsername())) {
			errors.rejectValue("username", "objUser", "Username đã tồn tại.");
		}
		
		//password ko rỗng
		if (objUser.getPassword().trim().equals("")) {
			errors.rejectValue("password", "objUser", "Vui lòng nhập password.");
		} else if (objUser.getPassword().length() < 5 || objUser.getPassword().length() > 32) { //password từ 5-32
			errors.rejectValue("password", "objUser", "Vui lòng nhập password từ 5-32 kí tự.");
		}
		//confirm password
		if (!objUser.getPassword().trim().equals(repassword)) {
			errors.rejectValue("password", "objUser", "Vui lòng confirm password.");
		}
		
		
		objUser.setRole(new Role(3, null));
		objUser.setEnable(0);
		objUser.setPassword(bCrypt.encode(objUser.getPassword()));
		
		if (errors.hasErrors()) {
			return "auth/sign-up";
		}
		
		try {
			//gởi url để người dùng vào email xác nhận
			String urlConfirm = request.getRequestURL() + "/confirm?u=" + objUser.getUsername() + "&p=" + objUser.getPassword();
			sendMailService.sendMailSignUp(objUser.getEmail(), urlConfirm);
		} catch (Exception e) {
			//nếu gởi ko thành công tức là email không tồn tại
			//e.printStackTrace();
			System.out.println("Lỗi gởi mail");
			modelMap.addAttribute("objUser", objUser);
			modelMap.addAttribute("msg", "Email bạn không hợp lệ hoặc bạn chưa kết nối internet");
			return "auth/sign-up";
		}
		
		userDAO.addItem(objUser);
		redirectAttributes.addFlashAttribute("msg", "Đăng kí thành công, vui lòng check email để xác nhận");
		return "redirect:/auth/sign-up";
	}
	
	//enable khi user confirm trong email
	@GetMapping("sign-up/confirm")
	public String confirmAccount(@RequestParam("u") String username, @RequestParam("p") String password) {
		User objUser = userDAO.getItemByUsernamePassword(username, password);
		if (objUser != null) {
			userDAO.activeUser(objUser.getId(), 1);
			return "redirect:/auth/register/confirm/notification?msg=success";
		} else {
			return "redirect:/auth/register/confirm/notification?msg=error";
		}
	}
	
	/*@GetMapping("public-login")
	public String publicLogin() {
		return "public.login";
	}
	
	@GetMapping("forgot")
	public String publicForgot() {
		return "public.forgot";
	}*/
	
}
