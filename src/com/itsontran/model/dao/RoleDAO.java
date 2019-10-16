package com.itsontran.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.itsontran.model.bean.Role;

@Repository
public class RoleDAO {
	
	private final String FIND_ITEMS = "SELECT * FROM roles";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<Role> getItems() {
		return jdbcTemplate.query(FIND_ITEMS, new BeanPropertyRowMapper<Role>(Role.class));
	}
}
