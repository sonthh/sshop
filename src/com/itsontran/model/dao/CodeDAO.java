package com.itsontran.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.itsontran.constant.Defines;
import com.itsontran.model.bean.Code;

@Repository
public class CodeDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	private final String COUNT_ITEMS = "SELECT COUNT(*) FROM codes";
	private final String FIND_ITEMS_PAGINATION = "SELECT * FROM codes ORDER BY id DESC LIMIT ?, ?";
	private final String FIND_ITEM_BY_ID = "SELECT * FROM codes WHERE id = ?";
	private final String FIND_ITEM_BY_NAME = "SELECT * FROM codes WHERE name = ?";
	private final String DELETE_ITEM = "DELETE FROM codes WHERE id = ?";
	private final String HAS_CODE_NAME = "SELECT COUNT(*) FROM codes WHERE name = ?";
	private final String INSERT_ITEM = "INSERT INTO codes(name, countRemain, value) VALUES(?, ?, ?)";
	private final String UPDATE_ITEM = "UPDATE codes SET name = ?, countRemain = ?, value = ? WHERE id = ?";
	private final String COUNT_SEARCH_ITEMS = "SELECT COUNT(*) FROM codes WHERE name LIKE ?";
	private final String FIND_SEARCH_ITEMS_PAGINATION = "SELECT * FROM codes"
			+ " WHERE name LIKE ? ORDER BY id DESC LIMIT ?, ?";

	private BeanPropertyRowMapper<Code> getBeanPropertyRowMapper() {
		return new BeanPropertyRowMapper<Code>(Code.class);
	}

	public int countItems() {
		return jdbcTemplate.queryForObject(COUNT_ITEMS, Integer.class);
	}

	public List<Code> getItemsPagination(int offset) {
		return jdbcTemplate.query(FIND_ITEMS_PAGINATION, new Object[] { offset, Defines.ROW_COUNT },
				getBeanPropertyRowMapper());
	}

	public int deleteItem(int id) {
		return jdbcTemplate.update(DELETE_ITEM, new Object[] { id });
	}

	public boolean hasCodeName(String name) {
		return jdbcTemplate.queryForObject(HAS_CODE_NAME, new Object[] { name }, Boolean.class);
	}

	public int addItem(Code objCode) {
		return jdbcTemplate.update(INSERT_ITEM,
				new Object[] { objCode.getName(), objCode.getCountRemain(), objCode.getValue() });
	}

	public Code getItem(int id) {
		return jdbcTemplate.queryForObject(FIND_ITEM_BY_ID, new Object[] { id },
				getBeanPropertyRowMapper());
	}
	
	public Code getItemByName(String name) {
		return jdbcTemplate.queryForObject(FIND_ITEM_BY_NAME, new Object[] { name },
				getBeanPropertyRowMapper());
	}

	public int editItem(Code objCode) {
		return jdbcTemplate.update(UPDATE_ITEM,
				new Object[] { objCode.getName(), objCode.getCountRemain(), objCode.getValue(), objCode.getId() });
	}

	public int countSearchItems(String searchString) {
		return jdbcTemplate.queryForObject(COUNT_SEARCH_ITEMS, new Object[] { "%" + searchString + "%" }, Integer.class);
	}

	public List<Code> getSearchItemsPagination(String searchString, int offset) {
		return jdbcTemplate.query(FIND_SEARCH_ITEMS_PAGINATION, new Object[] { "%" + searchString + "%" ,offset, Defines.ROW_COUNT },
				getBeanPropertyRowMapper());
	}

}
