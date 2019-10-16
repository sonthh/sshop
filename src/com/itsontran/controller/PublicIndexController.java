package com.itsontran.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.google.gson.Gson;
import com.itsontran.constant.Defines;
import com.itsontran.model.bean.Cart;
import com.itsontran.model.bean.CartItem;
import com.itsontran.model.bean.Category;
import com.itsontran.model.bean.Code;
import com.itsontran.model.bean.Contact;
import com.itsontran.model.bean.Order;
import com.itsontran.model.bean.OrderItem;
import com.itsontran.model.bean.Product;
import com.itsontran.model.bean.Slide;
import com.itsontran.model.dao.CategoryDAO;
import com.itsontran.model.dao.CodeDAO;
import com.itsontran.model.dao.ContactDAO;
import com.itsontran.model.dao.OrderDAO;
import com.itsontran.model.dao.OrderItemDAO;
import com.itsontran.model.dao.ProductDAO;
import com.itsontran.model.dao.SlideDAO;
import com.itsontran.service.CategoryService;
import com.itsontran.service.SendMailService;
import com.itsontran.util.PaginationUtil;


@Controller
public class PublicIndexController {
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ContactDAO contactDAO;
	
	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private CategoryDAO categoryDAO;
	
	@Autowired
	private OrderDAO orderDAO;
	
	@Autowired
	private OrderItemDAO orderItemDAO;
	
	@Autowired
	private SlideDAO slideDAO;
	
	@Autowired
	private CodeDAO codeDAO;
	
	@Autowired
	private SendMailService sendMail;
	
	@ModelAttribute
	public void commonObject(ModelMap modelMap) {
		//sử dụng LinkedHashMap để gởi menu
		Map<Category, List<Category>> mapCategories = categoryService.getMapCategory();
		modelMap.addAttribute("mapCategories", mapCategories);
		
		Map<Category, List<Product>> mapProductByParentCateogy = categoryService.getMapProductByParentCateogy();
		modelMap.addAttribute("mapProductByParentCateogy", mapProductByParentCateogy);
	}
	
	@GetMapping("")
	public String index(ModelMap modelMap) {
		List<Product> listTopProducts = productDAO.getItemsTop(8);
		modelMap.addAttribute("listTopProducts", listTopProducts);
		
		List<Product> listRandProducts = productDAO.getItemsRand(9);
		modelMap.addAttribute("listRandProducts", listRandProducts);
		
		List<Slide> listTopSlides = slideDAO.getTopItems();
		modelMap.addAttribute("listTopSlides", listTopSlides);
		return "public.index";
	}
	
	/*s:cat*/
	@GetMapping({ "category/{categoryName}-{categoryId}.html", "category/{categoryName}-{categoryId}/page-{page}.html" })
	public String cat(@PathVariable("categoryName") String catName, @PathVariable("categoryId") int categoryId,
			@PathVariable(name = "page", required = false) Integer page, ModelMap modelMap) {
		
		int numberOfItems = productDAO.countItemsByCategoryId(categoryId);
		int numberOfPages = (int) Math.ceil((float) numberOfItems / Defines.ROW_COUNT_PUBLIC);
		if (page == null) {
			page = 1;
		} else if (page < 1) {
			return "redirect:/" + catName + "-" + categoryId + "/page-" + page;
		} else if (page > numberOfPages) {
			return "redirect:/" + catName + "-" + categoryId + "/page-" + numberOfPages;
		}
		int offset = (page - 1) * Defines.ROW_COUNT_PUBLIC;
		
		List<Integer> paginations = PaginationUtil.makePagination(numberOfPages, 7, page);
		
		modelMap.addAttribute("paginations", paginations);
		modelMap.addAttribute("page", page);
		modelMap.addAttribute("numberOfPages", numberOfPages);
		modelMap.addAttribute("numberOfItems", numberOfItems);
		
		Category objCategory = categoryDAO.getItem(categoryId);
		modelMap.addAttribute("objCategory", objCategory);
		List<Product> listProducts = productDAO.getItemsPaginationsByCategoryId(offset, categoryId, Defines.ROW_COUNT_PUBLIC);
		modelMap.addAttribute("listProducts", listProducts);
		return "public.category";
	}
	/* e:cat */
	
	/*s:search theo cat*/
	@GetMapping({ "search", "search/{categoryName}-{categoryId}/{name}/page-{page}" })
	public String search(
		@RequestParam(name="categoryId", required=false) Integer categoryIdParam,
		@PathVariable(name="categoryId", required=false) Integer categoryIdVar,
		@RequestParam(name="name", required=false) String searchParam,   //lấy từ doget của form tìm kiếm
		@PathVariable(name="name", required=false) String searchPathVar, //lấy từ đường dẫn
		@PathVariable(name="page", required=false) Integer page, ModelMap modelMap) {
		
		String searchString = null;
		if (searchParam == null) {
			searchString = searchPathVar;
		} else {
			searchString = searchParam;
		}
		
		Integer categoryId = null;
		if (categoryIdParam == null) {
			categoryId = categoryIdVar;
		} else {
			categoryId = categoryIdParam;
		}
		if (categoryId == null) {
			categoryId = 0;
		}
		
		int numberOfItems = productDAO.countSearchItemsByCategory(searchString, categoryId);
		int numberOfPages = (int) Math.ceil((float) numberOfItems / Defines.ROW_COUNT_PUBLIC);
		
		if (page == null || page < 1) {
			page = 1;
		} else if (page > numberOfPages) {
			page = numberOfPages;
		}
		
		int offset = (page - 1) * Defines.ROW_COUNT_PUBLIC;
		
		List<Integer> paginations = PaginationUtil.makePagination(numberOfPages, 7, page);
		
		modelMap.addAttribute("paginations", paginations);
		modelMap.addAttribute("page", page);
		modelMap.addAttribute("numberOfPages", numberOfPages);
		modelMap.addAttribute("numberOfItems", numberOfItems);
		modelMap.addAttribute("searchString", searchString);
		if (categoryId != 0) {
			modelMap.addAttribute("objCategory", categoryDAO.getItem(categoryId));
		}
		
		List<Product> listProducts = productDAO.getSearchItemsByCategoryPagination(searchString, categoryId, offset, Defines.ROW_COUNT_PUBLIC);
		modelMap.addAttribute("listProducts", listProducts);
		
		return "public.search";
	}
	/*e:search theo cat*/
	
	@GetMapping("{name}-{id}.html")
	public String detail(@PathVariable("id") int id, ModelMap modelMap, 
			HttpServletRequest request, HttpServletResponse response) {
		Product objProduct = productDAO.getItem(id);
		modelMap.addAttribute("objProduct", objProduct);
		
		List<Product> listRandProducts = productDAO.getItemsRand(9);
		modelMap.addAttribute("listRandProducts", listRandProducts);
		
		Cookie cookie =	WebUtils.getCookie(request, "increaseView-" + id);
		if (cookie == null) {
			productDAO.increaseView(id);
			cookie = new Cookie("increaseView-" + id, "increaseView-" + id);
			cookie.setMaxAge(60 * 60);
			response.addCookie(cookie);
		}
		
		return "public.detail";
	}
	
	
	
	@GetMapping("contact")
	public String contact() {
		return "public.contact";
	}
	
	@ResponseBody
	@PostMapping("contact")
	public String contact(@Valid @ModelAttribute("objContact") Contact objContact, BindingResult errors) {
		
		if (errors.hasErrors()) {
			System.out.println("Nhập form không hợp lê");
			return "0";
		}
		
		if (contactDAO.insertItem(objContact) > 0) {
			return "1";
		} else {
			return "0";
		}
	}
	
	@GetMapping("about")
	public String about() {
		return "public.about";
	}
	
	@GetMapping("cart")
	public String cart() {
		return "public.cart";
	}
	
	/**
	 * 
	 * @param productId	id của sản phẩm trong giỏ hàng, nếu đã tồn tại thì tăng số lượng, nếu ko thì thêm mới số lượng là 0
	 * @param session
	 * @return
	 */
	@ResponseBody
	@PostMapping("cart/add")
	public String addCartItem(@RequestParam("productId") Integer productId, HttpSession session) {
		Cart cart = (Cart) session.getAttribute("cart");
		if (cart == null) return "";
		
		if (cart.getListCartItems().containsKey(productId)) {
			CartItem cartItem = cart.getListCartItems().get(productId);
			cartItem.setQuantity(cartItem.getQuantity() + 1);
			cart.updateCart(productId, cartItem);
		} else {
			Product product = productDAO.getItem(productId);
			cart.updateCart(productId, new CartItem(product, 1));
		}
		
		return cart.countItems() + "";
	}
	
	@ResponseBody
	@PostMapping("cart/addToCartInDetailPage")
	public String addToCartInDetailPage(@RequestParam("productId") Integer productId, @RequestParam("quantity") Integer quantity,
			HttpSession session) {
		Cart cart = (Cart) session.getAttribute("cart");
		
		if (cart.getListCartItems().containsKey(productId)) {
			CartItem cartItem = cart.getListCartItems().get(productId);
			cartItem.setQuantity(quantity);
			cart.updateCart(productId, cartItem);
		} else {
			Product product = productDAO.getItem(productId);
			cart.updateCart(productId, new CartItem(product, quantity));
		}
		
		return cart.countItems() + "";
	}
	
	/**
	 * 
	 * @param productId id của sản phẩm cần remove khỏi giỏ hàng
	 * @param session
	 * @return Sau khi remove khỏi map thì trả về json countCartItem: số lượng giỏ hàng còn lại, total tổng tiền giỏ hàng
	 */
	@ResponseBody
	@PostMapping("cart/remove")
	public String removeCartItem(@RequestParam("productId") Integer productId, HttpSession session) {
		
		Cart cart = (Cart) session.getAttribute("cart");
		cart.removeToCart(productId);
		
		Gson gson = new Gson();
		Map<String, String> inputMap = new HashMap<String, String>();
        inputMap.put("countCartItem", cart.countItems() + "");
        inputMap.put("total", cart.getTotalCart() + "");
        String json = gson.toJson(inputMap);
        
		return json;
	}
	/**
	 * Xóa toàn bộ giỏ hàng
	 * @param session
	 * @return
	 */
	@ResponseBody
	@PostMapping("cart/removeAll")
	public String removeAllCartItem(HttpSession session) {
		session.setAttribute("cart", new Cart());
		return "";
	}
	
	/**
	 * thay đổi số lượng của một giỏ hàng item, trả về json totalPriceCartItem là số tiền của giỏ hàng item đó, total tổng số tiền của giỏ hàng
	 * @param productId
	 * @param quantity
	 * @param session
	 * @return
	 */
	@ResponseBody
	@PostMapping("cart/updateCartItem")
	public String updateCartItem(@RequestParam("productId") Integer productId,
			@RequestParam("quantity") Integer quantity, HttpSession session) {

		Cart cart = (Cart) session.getAttribute("cart");
		CartItem cartItem = null;
		if (cart.getListCartItems().containsKey(productId)) {
			cartItem = cart.getListCartItems().get(productId);
			cartItem.setQuantity(quantity);
			cart.updateCart(productId, cartItem);
		}
		
		Gson gson = new Gson();
		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.put("totalPriceCartItem", "" + (cartItem.getQuantity() * cartItem.getProduct().getPrice())
				* ((100 - cartItem.getProduct().getDiscount()) / 100.0));
        inputMap.put("total", cart.getTotalCart() + "");
        String json = gson.toJson(inputMap);
		
		return json;
	}
	
	@ResponseBody
	@PostMapping("/cart/order")
	public String order(@Valid @ModelAttribute("objOrder") Order objOrder, @RequestParam("code") String codeName,
			BindingResult errors, HttpSession session) {
		
		if (errors.hasErrors()) {
			return "0";
		}
		
		Cart cart = (Cart) session.getAttribute("cart");
		if (cart == null) {
			return "0";
		}
		
		objOrder.setAmount(cart.getTotalCart());
		System.out.println(objOrder);
		
		//kiểm tra giảm giá mã code
		codeName = codeName.trim();
		if (codeDAO.hasCodeName(codeName)) {
			Code objCode = null;
			try {
				objCode = codeDAO.getItemByName(codeName);
				if (objCode != null && objCode.getCountRemain() > 0) {
					objOrder.setAmount((int) (((100 - objCode.getValue()) / 100.0) * objOrder.getAmount()));
					objCode.setCountRemain(objCode.getCountRemain() - 1);
					codeDAO.editItem(objCode);
				}
			} catch (Exception e) {
			}
		}
		
		if (objOrder.getPayment().equals("2")) { //thanh toán khi nhận hàng
			objOrder.setAmount(objOrder.getAmount() + 40000);
			objOrder.setStatus(1);
			objOrder.setPayment("0");
		} else {
			objOrder.setStatus(1);
			objOrder.setPayment("1");
		}
		System.out.println(objOrder.getAmount());
		//thêm vào bảng order và trả về id vừa thêm vào bảng
		objOrder = orderDAO.addItem(objOrder);
		int orderId = objOrder.getId();
		
		//lưu danh sách cartitem vào bảng orderdetail
		for (Map.Entry<Integer, CartItem> item : cart.getListCartItems().entrySet()) {
			OrderItem objOrderItem = new OrderItem(0,
					new Order(orderId, null, null, null, null, null, null, null, null, null),
					new Product(item.getValue().getProduct().getId(), null, null, null, null, null, null, null, null,
							null, null, null),
					item.getValue().getQuantity());
            orderItemDAO.addItem(objOrderItem);
        }
		sendMail.sendMailOrder("tranhuuhongson@gmail.com", cart);
		cart = new Cart();
		session.setAttribute("cart", cart);
		return "1";
	}
	
	/*@ResponseBody
	@GetMapping("sendMail")
	public String sendMail() {
		sendMail.sendMailOrder("tranhuuhongson@gmail.com");
		return "";
	}*/
	
}
