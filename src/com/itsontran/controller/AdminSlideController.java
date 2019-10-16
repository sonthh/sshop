package com.itsontran.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
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
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itsontran.constant.Defines;
import com.itsontran.constant.MessageEnum;
import com.itsontran.model.bean.Slide;
import com.itsontran.model.dao.SlideDAO;
import com.itsontran.util.FilenameUtil;
import com.itsontran.util.PaginationUtil;

@Controller
@RequestMapping("admin/slide")
public class AdminSlideController {

	@Autowired
	private SlideDAO slideDAO;

	@Autowired
	private ServletContext servletContext;

	@GetMapping(path = { "", "/page-{page}" })
	public String index(@PathVariable(name = "page", required = false) Integer page, ModelMap modelMap) {
		int numberOfItems = slideDAO.countItems();
		int numberOfPages = (int) Math.ceil((float) numberOfItems / Defines.ROW_COUNT);
		if (page == null) {
			page = 1;
		} else if (page < 1) {
			return "redirect:/admin/slide/page-1";
		} else if (page > numberOfPages) {
			return "redirect:/admin/slide/page-" + numberOfPages;
		}
		int offset = (page - 1) * Defines.ROW_COUNT;

		List<Integer> paginations = PaginationUtil.makePagination(numberOfPages, 7, page);

		modelMap.addAttribute("paginations", paginations);
		modelMap.addAttribute("page", page);
		modelMap.addAttribute("numberOfPages", numberOfPages);
		modelMap.addAttribute("numberOfItems", numberOfItems);

		List<Slide> listSlides = slideDAO.getItemsPagination(offset);
		modelMap.addAttribute("listSlides", listSlides);
		return "admin.slide.index";
	}

	@GetMapping({ "search", "search-{name}-{page}" })
	public String search(@RequestParam(name = "name", required = false) String searchParam, // lấy từ doget của form tìm
																							// kiếm
			@PathVariable(name = "name", required = false) String searchPathVar, // lấy từ đường dẫn
			@PathVariable(name = "page", required = false) Integer page, ModelMap modelMap) {

		String searchString = null;
		if (searchParam == null) {
			searchString = searchPathVar;
		} else {
			searchString = searchParam;
		}

		int numberOfItems = slideDAO.countSearchItems(searchString);
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

		List<Slide> listSlides = slideDAO.getSearchItemsPagination(searchString, offset);
		modelMap.addAttribute("listSlides", listSlides);
		return "admin.slide.search";
	}

	@GetMapping("delete/{id}")
	public String delete(@PathVariable("id") int id, RedirectAttributes ra) {
		if (slideDAO.deleteItem(id) > 0) {
			ra.addFlashAttribute("eMsg", MessageEnum.MSG_DELETE_SUCCESS);
		} else {
			ra.addFlashAttribute("eMsg", MessageEnum.MSG_ERROR);
		}
		return "redirect:/admin/slide";
	}

	@ResponseBody
	@PostMapping("deleteSelected")
	public String delChecked(@RequestParam("checked[]") List<Integer> checkedSlideIds, RedirectAttributes ra) {
		for (Integer id : checkedSlideIds) {
			slideDAO.deleteItem(id);
		}
		return checkedSlideIds.size() + "";
	}

	@ResponseBody
	@PostMapping("/activeSlide")
	public String activeSlide(@RequestParam("id") int id, @RequestParam("active") int active) {
		active = active == 1 ? 0 : 1;
		if (slideDAO.activeSlide(id, active) > 0) {
			return "1";
		} else {
			return "0";
		}
	}

	@GetMapping("add")
	public String add(ModelMap modelMap) {
		return "admin.slide.add";
	}

	@PostMapping("add")
	public String add(@Valid @ModelAttribute("objSlide") Slide objSlide, BindingResult errors,
			@RequestParam("picture") CommonsMultipartFile cmf, ModelMap modelMap, RedirectAttributes ra) {
		//System.out.println(objSlide);

		if (cmf.getOriginalFilename().equals("")) {
			errors.rejectValue("image", "NotChoose.objSlide.image");
		}
		if (objSlide.getSort() == null) {
			errors.rejectValue("sort", "NotNull.objSlide.sort");
		}

		if (errors.hasErrors()) {
			return "admin.slide.add";
		}

		objSlide.setImage(FilenameUtil.rename(cmf.getOriginalFilename()));
		if (slideDAO.addItem(objSlide) > 0) {

			String dirPath = servletContext.getRealPath("") + "WEB-INF" + File.separator + Defines.DIR_UPLOAD;
			File dirUpload = new File(dirPath);
			if (!dirUpload.exists()) {
				dirUpload.mkdirs();
			}
			try {
				cmf.transferTo(new File(dirUpload + File.separator + objSlide.getImage()));
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}

			ra.addFlashAttribute("eMsg", MessageEnum.MSG_ADD_SUCCESS);
			ra.addFlashAttribute("objSlide", objSlide);// gởi objUser để tô màu cho dòng vừa sửa
			return "redirect:/admin/slide";
		} else {
			modelMap.addAttribute("eMsg", MessageEnum.MSG_ERROR);
			return "admin.slide.add";
		}
	}

	@GetMapping("edit/{id}-{page}")
	public String edit(@PathVariable("id") int id, @PathVariable("page") int page, ModelMap modelMap) {
		Slide objSlide = slideDAO.getItem(id);
		modelMap.addAttribute("objSlide", objSlide);
		modelMap.addAttribute("page", page);
		return "admin.slide.edit";
	}

	@PostMapping("edit/{id}-{page}")
	public String edit(@Valid @ModelAttribute("objSlide") Slide objSlide, BindingResult errors,
			@PathVariable("id") int id, @PathVariable("page") int page,
			@RequestParam("picture") CommonsMultipartFile cmf, RedirectAttributes ra, ModelMap modelMap) {

		Slide oldSlide = slideDAO.getItem(id);

		//System.out.println(objSlide);
		if (objSlide.getSort() == null) {
			errors.rejectValue("sort", "NotNull.objSlide.sort");
		}

		if (errors.hasErrors()) {
			return "admin.slide.edit";
		}

		boolean check = true; //có chọn ảnh mới hay không
		if (!cmf.getOriginalFilename().equals("")) {
			objSlide.setImage(FilenameUtil.rename(cmf.getOriginalFilename()));
		} else {
			check = false;
			objSlide.setImage(oldSlide.getImage());
		}

		if (slideDAO.editItem(objSlide) > 0) {
			if (check) {
				String dirPath = servletContext.getRealPath("") + "WEB-INF" + File.separator + Defines.DIR_UPLOAD;
				File dirUpload = new File(dirPath);
				if (!dirUpload.exists()) {
					dirUpload.mkdirs();
				}
				try {
					File file = new File(dirPath + File.separator + oldSlide.getImage());
					file.delete();
					cmf.transferTo(new File(dirUpload + File.separator + objSlide.getImage()));
				} catch (IllegalStateException | IOException e) {
					e.printStackTrace();
				}
			}
			ra.addFlashAttribute("eMsg", MessageEnum.MSG_UPDATE_SUCCESS);
			ra.addFlashAttribute("objSlide", objSlide);// gởi objUser để tô màu cho dòng vừa sửa
			return "redirect:/admin/slide/page-" + page;
		} else {
			modelMap.addAttribute("eMsg", MessageEnum.MSG_ERROR);
			return "admin.slide.edit";
		}
	}
}
