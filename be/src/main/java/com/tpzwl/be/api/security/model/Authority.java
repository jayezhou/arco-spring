package com.tpzwl.be.api.security.model;

import jakarta.persistence.*;

@Entity
@Table(name = "authorities")
public class Authority {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50)
	private String name;

	public Authority() {
	}

	public Authority(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}