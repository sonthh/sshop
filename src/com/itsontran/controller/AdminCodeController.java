package com.itsontran.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itsontran.constant.Defines;
import com.itsontran.constant.MessageEnum;
import com.itsontran.model.bean.Code;
import com.itsontran.model.dao.CodeDAO;
import com.itsontran.util.PaginationUtil;

@Controller
@RequestMapping("admin/code")
public class AdminCodeController {
	
	@Autowired
	private CodeDAO codeDAO;
	
	@GetMapping(path = { "", "/page-{page}" })
	public String index(@PathVariable(name = "page", required = false) Integer page, ModelMap modelMap) {
		int numberOfItems = codeDAO.countItems();
		int numberOfPages = (int) Math.ceil((float) numberOfItems / Defines.ROW_COUNT);
		if (page == null) {
			page = 1;
		} else if (page < 1) {
			return "redirect:/admin/code/page-1";
		} else if (page > numberOfPages) {
			return "redirect:/admin/code/page-" + numberOfPages;
		}
		int offset = (page - 1) * Defines.ROW_COUNT;
		
		List<Integer> paginations = PaginationUtil.makePagination(numberOfPages, 7, page);
		
		modelMap.addAttribute("paginations", paginations);
		modelMap.addAttribute("page", page);
		modelMap.addAttribute("numberOfPages", numberOfPages);
		modelMap.addAttribute("numberOfItems", numberOfItems);
		
		List<Code> listCodes = codeDAO.getItemsPagination(offset);
		modelMap.addAttribute("listCodes", listCodes);
		return "admin.code.index";
	}
	
	@GetMapping({"search", "search-{name}-{page}"})
	public String search(
		@RequestParam(name="name", required=false) String searchParam,   //lấy từ doget của form tìm kiếm
		@PathVariable(name="name", required=false) String searchPathVar, //lấy từ đường dẫn
		@PathVariable(name="page", required=false) Integer page, ModelMap modelMap) {
		
		String searchString = null;
		if (searchParam == null) {
			searchString = searchPathVar;
		} else {
			searchString = searchParam;
		}
		
		int numberOfItems = codeDAO.countSearchItems(searchString);
		int numberOfPages = (int) Math.ceil((float) numberOfItems / Defines.ROW_COUNT);
		
		if (page == null || page < 1) {
			page = 1;
		} else if (page > numberOfPages) {
			page = numberOfPages;
		}
		
		int offset = (page - 1) * Defines.ROW_COUNT;
		
		List<Integer> paginations = PaginationUtil.makePagination(numberOfPages, 7, page);
		
		modelMap.addAttribute("paginations", paginations);
		modelMap.addAttribute("page", page);
		modelMap.addAttribute("numberOfPages", numberOfPages);
		modelMap.addAttribute("numberOfItems", numberOfItems);
		modelMap.addAttribute("searchString", searchString);
		
		List<Code> listCodes = codeDAO.getSearchItemsPagination(searchString, offset);
		modelMap.addAttribute("listCodes", listCodes);
		return "admin.code.search";
	}
	
	@GetMapping("delete/{id}")
	public String delete(@PathVariable("id") int id, RedirectAttributes ra) {
		if (codeDAO.deleteItem(id) > 0) {
			ra.addFlashAttribute("eMsg", MessageEnum.MSG_DELETE_SUCCESS);
		} else {
			ra.addFlashAttribute("eMsg", MessageEnum.MSG_ERROR);
		}
		return "redirect:/admin/code";
	}
	
	@ResponseBody
	@PostMapping("deleteSelected")
	public String delChecked(@RequestParam("checked[]") List<Integer> checkedCodeIds, RedirectAttributes ra) {
		for (Integer id : checkedCodeIds) {
			codeDAO.deleteItem(id);
		}
		return checkedCodeIds.size() + "";
	}
	
	@GetMapping("add")
	public String add(ModelMap modelMap) {
		return "admin.code.add";
	}
	
	@PostMapping("add")
	public String add(@Valid @ModelAttribute("objCode") Code objCode, BindingResult errors,
			ModelMap modelMap, RedirectAttributes ra) {
		
		/* START validate */
		objCode.setName(objCode.getName().trim());
		if (codeDAO.hasCodeName(objCode.getName())) {
			errors.rejectValue("name", "Existed.objCode.name");
		}
		if (objCode.getCountRemain() == null) {
			errors.rejectValue("countRemain", "NotNull.objCode.countRemain");
		}
		if (objCode.getValue() == null) {
			errors.rejectValue("value", "NotNull.objCode.value");
		}
		
		if (errors.hasErrors()) {
			return "admin.code.add";
		}
		/* END validate */
		
		if (codeDAO.addItem(objCode) > 0) {
			ra.addFlashAttribute("eMsg", MessageEnum.MSG_ADD_SUCCESS);
			ra.addFlashAttribute("objCode", objCode);//gởi objUser để tô màu cho dòng vừa sửa
			return "redirect:/admin/code";
		} else {
			modelMap.addAttribute("eMsg", MessageEnum.MSG_ERROR);
			return "admin.code.add";
		}
	}
	
	@GetMapping("edit/{id}-{page}")
	public String edit(@PathVariable("id") int id, @PathVariable("page") int page, ModelMap modelMap) {
		Code objCode = codeDAO.getItem(id);
		modelMap.addAttribute("objCode", objCode);
		modelMap.addAttribute("page", page);
		return "admin.code.edit";
	}
	
	@PostMapping("edit/{id}-{page}")
	public String edit(@Valid @ModelAttribute("objCode") Code objCode, BindingResult errors,
			@PathVariable("id") int id, @PathVariable("page") int page,
			RedirectAttributes ra, ModelMap modelMap) {
		/* START validate */
		if (objCode.getCountRemain() == null) {
			errors.rejectValue("countRemain", "NotNull.objCode.countRemain");
		}
		if (objCode.getValue() == null) {
			errors.rejectValue("value", "NotNull.objCode.value");
		}
		
		if (errors.hasErrors()) {
			return "admin.code.edit";
		}
		 /*END validate*/

		if (codeDAO.editItem(objCode) > 0) {
			ra.addFlashAttribute("eMsg", MessageEnum.MSG_UPDATE_SUCCESS);
			ra.addFlashAttribute("objCode", objCode);//gởi objUser để tô màu cho dòng vừa sửa
			return "redirect:/admin/code";
		} else {
			modelMap.addAttribute("eMsg", MessageEnum.MSG_ERROR);
			return "admin.code.edit";
		}
	}
}
