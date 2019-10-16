package com.itsontran.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.itsontran.constant.Defines;
import com.itsontran.model.bean.Slide;

@Repository
public class SlideDAO {
	private final String FIND_ITEMS_PAGINATION = "SELECT * FROM slides ORDER BY id DESC LIMIT ?, ?";
	private final String FIND_TOP_ITEMS = "SELECT * FROM slides WHERE active = 1 ORDER BY sort DESC LIMIT 3";
	private final String FIND_ITEM_BY_ID = "SELECT * FROM slides WHERE id = ?";
	private final String FIND_SEARCH_ITEMS_PAGINATION = "SELECT * FROM slides WHERE name LIKE ? ORDER BY id DESC LIMIT ?, ?";
	private final String COUNT_ITEMS = "SELECT COUNT(*) FROM slides";
	private final String COUNT_SEARCH_ITEMS = "SELECT COUNT(*) FROM slides WHERE name LIKE ?";
	private final String DELETE_ITEM = "DELETE FROM slides WHERE id = ?";
	private final String ACTIVE_SLIDE = "UPDATE slides SET active = ? WHERE id = ?";
	private final String ADD_ITEM = "INSERT INTO slides(link, image, sort, name, description) VALUES(?, ?, ?, ?, ?)";
	private final String EDIT_ITEM = "UPDATE slides SET link = ?, image = ?, sort = ?, name = ?, description = ? WHERE id = ?";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public BeanPropertyRowMapper<Slide> getBeanPropertyRowMapper() {
		return new BeanPropertyRowMapper<Slide>(Slide.class);
	}

	public List<Slide> getItemsPagination(int offset) {
		return jdbcTemplate.query(FIND_ITEMS_PAGINATION, new Object[] { offset, Defines.ROW_COUNT },
				getBeanPropertyRowMapper());
	}
	
	public List<Slide> getTopItems() {
		return jdbcTemplate.query(FIND_TOP_ITEMS, getBeanPropertyRowMapper());
	}
	
	public List<Slide> getSearchItemsPagination(String searchString, int offset) {
		return jdbcTemplate.query(FIND_SEARCH_ITEMS_PAGINATION, new Object[] { "%" + searchString + "%", offset, Defines.ROW_COUNT },
				getBeanPropertyRowMapper());
	}

	public int countItems() {
		return jdbcTemplate.queryForObject(COUNT_ITEMS, Integer.class);
	}

	public int countSearchItems(String searchString) {
		return jdbcTemplate.queryForObject(COUNT_SEARCH_ITEMS, new Object[] { "%" + searchString + "%" }, Integer.class);
	}
	
	public int deleteItem(int id) {
		return jdbcTemplate.update(DELETE_ITEM, new Object[] { id });
	}

	public int activeSlide(int id, int active) {
		return jdbcTemplate.update(ACTIVE_SLIDE, new Object[] { active, id });
	}

	public int addItem(Slide objSlide) {
		return jdbcTemplate.update(ADD_ITEM, new Object[] { objSlide.getLink(), objSlide.getImage(), objSlide.getSort(),
				objSlide.getName(), objSlide.getDescription() });
	}

	public Slide getItem(int id) {
		return jdbcTemplate.queryForObject(FIND_ITEM_BY_ID, new Object[] { id }, getBeanPropertyRowMapper());
	}

	public int editItem(Slide objSlide) {
		return jdbcTemplate.update(EDIT_ITEM, new Object[] { objSlide.getLink(), objSlide.getImage(), objSlide.getSort(),
				objSlide.getName(), objSlide.getDescription(), objSlide.getId() });
	}

}
