package com.itsontran.model.bean;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.URL;

import com.sun.istack.internal.NotNull;

public class Slide {
	private Integer id;
	@NotBlank
	@URL
	private String link;
	private String image;
	@NotNull
	private Integer sort;
	@NotNull
	private Integer active;
	@NotBlank
	private String name;
	@NotBlank
	private String description;

	public Slide(Integer id, String link, String image, Integer sort, Integer active, String name, String description) {
		super();
		this.id = id;
		this.link = link;
		this.image = image;
		this.sort = sort;
		this.active = active;
		this.name = name;
		this.description = description;
	}

	public Slide() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Slide [id=" + id + ", link=" + link + ", image=" + image + ", sort=" + sort + ", active=" + active
				+ ", name=" + name + ", description=" + description + "]";
	}

}
