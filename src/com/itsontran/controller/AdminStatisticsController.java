package com.itsontran.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itsontran.model.dao.StatisticsDAO;
import com.itsontran.util.DateUtil;

@Controller
@RequestMapping("admin/statistics")
public class AdminStatisticsController {
	
	@Autowired
	private StatisticsDAO dao;
	
	@GetMapping("")
	public String index(ModelMap modelMap, String startDay, String endDay) {
		Timestamp t1 = null;
		Timestamp t2 = null;
		
		if (startDay == null || endDay == null) {
			Calendar cal = Calendar.getInstance();
			
			t2 = new Timestamp(cal.getTimeInMillis());
			
			cal.set(Calendar.DAY_OF_MONTH, 1);
			t1 = new Timestamp(cal.getTimeInMillis());
			
			//gởi ngày đầu tiên của tháng và ngày hiện tại sang view
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			startDay = sdf.format(t1);
			endDay = sdf.format(t2);
			
		} else {
			//2 ngày convert từ views
			t1 = DateUtil.convertStringToTimestamp(startDay);
			t2 = DateUtil.convertStringToTimestamp(endDay);
		}
		
		modelMap.addAttribute("startDay", startDay);
		modelMap.addAttribute("endDay", endDay);
		
		int countOrder = dao.countOrderBeetwenTwoDay(t1, t2);
		modelMap.addAttribute("countOrder", countOrder);
		
		int countAllAmount = dao.countAllAmountBeetwenTwoDay(t1, t2);
		modelMap.addAttribute("countAllAmount", countAllAmount);
		
		int countReceivedAmount = dao.countReceivedAmountBeetwenTwoDay(t1, t2);
		modelMap.addAttribute("countReceivedAmount", countReceivedAmount);
		
		int countProductType = dao.countProductTypeBeetwenTwoDay(t1, t2);
		modelMap.addAttribute("countProductType", countProductType);
		
		int countReceivedPaymentAmount = dao.countReceivedPaymentAmountBeetwenTwoDay(t1, t2, 1);
		modelMap.addAttribute("countReceivedPaymentAmount", countReceivedPaymentAmount);
		
		int countCompleteOrder = dao.countOrderBeetwenTwoDayAsStatus(t1, t2, 3);
		modelMap.addAttribute("countCompleteOrder", countCompleteOrder);
		
		int countWaitOrder = dao.countOrderBeetwenTwoDayAsStatus(t1, t2, 1);
		modelMap.addAttribute("countWaitOrder", countWaitOrder);
		
		int countSendOrder = dao.countOrderBeetwenTwoDayAsStatus(t1, t2, 2);
		modelMap.addAttribute("countSendOrder", countSendOrder);
		
		int countRefuseOrder = dao.countOrderBeetwenTwoDayAsStatus(t1, t2, 4);
		modelMap.addAttribute("countRefuseOrder", countRefuseOrder);
		
		return "admin.statistics.index";
	}
	@PostMapping("")
	public String indexPost(ModelMap modelMap,
			@RequestParam(name="startDay", required=false) String startDay, 
			@RequestParam(name="endDay", required=false) String endDay) {
		return index(modelMap, startDay, endDay);
	}
	
	public static void main(String[] args) {
		Timestamp t1 = new Timestamp(System.currentTimeMillis());
		System.out.println(t1.toString());
	}
	
}
