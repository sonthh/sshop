package com.itsontran.model.dao;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class StatisticsDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public int countOrderBeetwenTwoDay(Timestamp t1, Timestamp t2) {
		final String sql = "SELECT COUNT(*) FROM orders WHERE status != 4 AND dateCreated >= ? AND dateCreated <= ?";
		int count = 0;
		try {
			count = jdbcTemplate.queryForObject(sql, new Object[] { t1, t2 }, Integer.class);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return count;
	}

	public int countAllAmountBeetwenTwoDay(Timestamp t1, Timestamp t2) {
		final String sql = "SELECT SUM(amount) FROM orders WHERE status != 4 AND dateCreated >= ? AND dateCreated <= ?";
		int amount = 0;
		try {
			amount = jdbcTemplate.queryForObject(sql, new Object[] { t1, t2 }, Integer.class);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return amount;
	}
	
	public int countProductTypeBeetwenTwoDay(Timestamp t1, Timestamp t2) {
		final String sql = "SELECT COUNT(DISTINCT productId) FROM orderitems AS oi INNER JOIN orders"
				+ " AS o ON oi.orderId = o.id WHERE o.status != 4 AND o.dateCreated >= ? AND o.dateCreated <= ?";
		int count = 0;
		try {
			count = jdbcTemplate.queryForObject(sql, new Object[] { t1, t2 }, Integer.class);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return count;
	}

	public int countReceivedAmountBeetwenTwoDay(Timestamp t1, Timestamp t2) {
		final String sql = "SELECT SUM(amount) FROM orders WHERE status = 3 AND dateCreated >= ? AND dateCreated <= ?";
		int count = 0;
		try {
			count = jdbcTemplate.queryForObject(sql, new Object[] { t1, t2 }, Integer.class);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return count;
	}

	public int countReceivedPaymentAmountBeetwenTwoDay(Timestamp t1, Timestamp t2, int isPayment) {
		final String sql = "SELECT SUM(amount) FROM orders WHERE status = 3 AND payment = ? AND dateCreated >= ? AND dateCreated <= ?";
		int count = 0;
		try {
			count = jdbcTemplate.queryForObject(sql, new Object[] { isPayment, t1, t2 }, Integer.class);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return count;
	}

	public int countOrderBeetwenTwoDayAsStatus(Timestamp t1, Timestamp t2, int status) {
		final String sql = "SELECT COUNT(*) FROM orders WHERE status = ? AND dateCreated >= ? AND dateCreated <= ?";
		int count = 0;
		try {
			count = jdbcTemplate.queryForObject(sql, new Object[] { status, t1, t2 }, Integer.class);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return count;
	}
	
}
