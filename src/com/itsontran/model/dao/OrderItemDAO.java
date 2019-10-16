package com.itsontran.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.itsontran.model.bean.Category;
import com.itsontran.model.bean.Order;
import com.itsontran.model.bean.OrderItem;
import com.itsontran.model.bean.Product;

@Repository
public class OrderItemDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String INSERT_ITEM = "INSERT INTO orderitems(orderId, productId, quantity) VALUES(?, ?, ?)";
	private final String DELETE_ITEMS_BY_ORDER_ID = "DELETE FROM orderitems WHERE orderId = ?";
	
	private final String FIND_ITEMS_BY_ORDER_ID = "SELECT oi.*, o.*, p.* FROM orderitems AS oi INNER JOIN orders AS o ON oi.orderId = o.id"
			+ " INNER JOIN products AS p ON oi.productId = p.id WHERE oi.orderId = ?";
	
	private RowMapper<OrderItem> getRowMapper() {
		return new RowMapper<OrderItem>() {

			@Override
			public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
				OrderItem orderItem = new OrderItem(rs.getInt("oi.id"), 
						
						new Order(rs.getInt("o.id"), rs.getInt("o.status"), rs.getString("o.fullname"), rs.getString("o.address"),
						rs.getString("o.email"), rs.getString("o.phone"), rs.getString("o.message"),
						rs.getTimestamp("o.dateCreated"), rs.getInt("o.amount"), rs.getString("o.payment")), 
						
						new Product(rs.getInt("p.id"), new Category(rs.getInt("p.categoryId"), null, null, null),
						rs.getString("p.name"), rs.getString("p.description"), rs.getString("p.content"), rs.getInt("p.price"),
						rs.getInt("p.discount"), rs.getString("p.image"), rs.getString("p.imageList"), rs.getTimestamp("p.dateCreated"),
						rs.getInt("p.view"), rs.getInt("p.countRemain")), 
						
						rs.getInt("oi.quantity"));
				return orderItem;
			}
		};
	}
	
	public List<OrderItem> getItemsByOrderId(int orderId) {
		return jdbcTemplate.query(FIND_ITEMS_BY_ORDER_ID, new Object[] { orderId }, getRowMapper());
	}
	
	public int addItem(OrderItem objOrderItem) {
		return jdbcTemplate.update(INSERT_ITEM, new Object[] { objOrderItem.getOrder().getId(),
				objOrderItem.getProduct().getId(), objOrderItem.getQuantity() });
	}
	
	public int deleteItemsByOrderId(int orderId) {
		return jdbcTemplate.update(DELETE_ITEMS_BY_ORDER_ID, new Object[] { orderId });
	}
	
}
