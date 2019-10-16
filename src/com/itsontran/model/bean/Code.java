package com.itsontran.model.bean;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.sun.istack.internal.NotNull;

public class Code {
	private Integer id;
	@NotBlank
	@Size(min=6, max=20)
	private String name;
	@NotNull
	@Min(1)
	private Integer countRemain;
	@NotNull
	@Min(1)
	@Max(100)
	private Integer value;

	public Code(Integer id, @NotBlank String name, Integer countRemain, Integer value) {
		super();
		this.id = id;
		this.name = name;
		this.countRemain = countRemain;
		this.value = value;
	}

	public Code() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCountRemain() {
		return countRemain;
	}

	public void setCountRemain(Integer countRemain) {
		this.countRemain = countRemain;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Code [id=" + id + ", name=" + name + ", countRemain=" + countRemain + ", value=" + value + "]";
	}

}
