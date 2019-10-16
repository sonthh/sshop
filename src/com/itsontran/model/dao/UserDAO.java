package com.itsontran.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.itsontran.constant.Defines;
import com.itsontran.model.bean.Role;
import com.itsontran.model.bean.User;



@Repository
public class UserDAO {
	private final String ENABLE_USER = "UPDATE users SET enable = ? WHERE id = ?";
	private final String COUNT_ITEMS = "SELECT COUNT(*) FROM users";
	private final String COUNT_SEARCH_ITEMS = "SELECT COUNT(*) FROM users WHERE username LIKE ?";
	private final String FIND_ITEMS_PAGINATION = "SELECT u.*, r.name AS rname FROM users AS u"
			+ " INNER JOIN roles AS r ON u.roleId = r.id ORDER BY u.id DESC LIMIT ?, ?";
	private final String FIND_ITEM_BY_ID = "SELECT u.*, r.name AS rname FROM users AS u"
			+ " INNER JOIN roles AS r ON u.roleId = r.id WHERE u.id = ?";
	private final String FIND_ITEM_BY_USERNAME_ENABLE = "SELECT u.*, r.name AS rname FROM users AS u"
			+ " INNER JOIN roles AS r ON u.roleId = r.id WHERE u.username = ? AND u.enable = 1";
	private final String FIND_SEARCH_ITEMS_PAGINATION = "SELECT u.*, r.name AS rname FROM users AS u"
			+ " INNER JOIN roles AS r ON u.roleId = r.id WHERE username LIKE ? ORDER BY u.id DESC LIMIT ?, ?";
	private final String DELETE_ITEM = "DELETE FROM users WHERE id = ?";
	private final String HAS_USER_BY_USERNAME = "SELECT COUNT(*) FROM users WHERE username = ?";
	private final String INSERT_ITEM = "INSERT INTO users(roleId, username, fullname, password, email, phoneNumber, address, enable) VALUES"
			+ " (?, ?, ?, ?, ?, ?, ?, ?)";
	private final String UPDATE_ITEM = "UPDATE users SET roleId = ?, username = ?, fullname = ?, password = ?, email = ?,"
			+ " phoneNumber = ?, address = ? WHERE id = ?";
	private final String COUNT_EMAIL = "SELECT COUNT(*) FROM users WHERE email = ?";
	private final String FIND_USER_BY_USERNAME_PASSWORD = "SELECT u.*, r.name AS rname FROM users AS u "
			+ "INNER JOIN roles AS r ON u.roleId = r.id WHERE username = ? AND password = ?";
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private RowMapper<User> getRowMapper() {
		return new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new User(rs.getInt("id"), new Role(rs.getInt("roleId"), rs.getString("rname")),
						rs.getString("username"), rs.getString("fullname"), rs.getString("password"),
						rs.getString("email"), rs.getString("phoneNumber"), rs.getString("address"),
						rs.getTimestamp("dateCreated"), rs.getInt("enable"));
			}
		};
	}
	
	public List<User> getItemsPagination(int offset) {
		return jdbcTemplate.query(FIND_ITEMS_PAGINATION, new Object[] { offset, Defines.ROW_COUNT }, getRowMapper());
	}
	
	public int countItems() {
		return jdbcTemplate.queryForObject(COUNT_ITEMS, Integer.class);
	}

	public int countSearchItems(String searchString) {
		return jdbcTemplate.queryForObject(COUNT_SEARCH_ITEMS, new Object[] { "%" + searchString + "%" }, Integer.class);
	}

	public List<User> getSearchItemsPagination(String searchString, int offset) {
		return jdbcTemplate.query(FIND_SEARCH_ITEMS_PAGINATION, new Object[] { "%" + searchString + "%" ,offset, Defines.ROW_COUNT }, getRowMapper());
	}

	public int deleteItem(int id) {
		return jdbcTemplate.update(DELETE_ITEM, new Object[] { id });
	}

	public int addItem(User objUser) {
		return jdbcTemplate.update(INSERT_ITEM,
				new Object[] { objUser.getRole().getId(), objUser.getUsername(), objUser.getFullname(),
						objUser.getPassword(), objUser.getEmail(), objUser.getPhoneNumber(), objUser.getAddress(), objUser.getEnable() });
	}
	
	public boolean hasUserByUserName(String username) {
		return jdbcTemplate.queryForObject(HAS_USER_BY_USERNAME, new Object[] { username }, Boolean.class);
	}

	public User getItem(int id) {
		return jdbcTemplate.queryForObject(FIND_ITEM_BY_ID, new Object[] { id }, getRowMapper());
	}

	public int editItem(User objUser) {
		return jdbcTemplate.update(UPDATE_ITEM,
				new Object[] { objUser.getRole().getId(), objUser.getUsername(), objUser.getFullname(),
						objUser.getPassword(), objUser.getEmail(), objUser.getPhoneNumber(), objUser.getAddress(),
						objUser.getId() });
	}

	public User getItemByUsernameEnable(String username) {
		return jdbcTemplate.queryForObject(FIND_ITEM_BY_USERNAME_ENABLE, new Object[] { username }, getRowMapper());
	}

	public int activeUser(int id, int active) {
		return jdbcTemplate.update(ENABLE_USER, new Object[] { active, id });
	}

	public int countEmail(String email) {
		return jdbcTemplate.queryForObject(COUNT_EMAIL, new Object[] { email }, Integer.class);
	}
	
	public User getItemByUsernamePassword(String username, String password) {
		try {
			return jdbcTemplate.queryForObject(FIND_USER_BY_USERNAME_PASSWORD, new Object[] { username, password }, getRowMapper());
		} catch (Exception e) {
			return null;
		}
	}
}
