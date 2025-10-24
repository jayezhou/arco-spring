package com.tpzwl.be.api.security.model;

import jakarta.persistence.*;

@Entity
@Table(name = "permissions")
public class Permission {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50)
	private String permission;

	@Column
	private Boolean isMenu;

	@Column
	private Long parent;
	
	@Column(length = 30)
	private String path;
	
	@Column(length = 30)
	private String routerName;	

	@Column(length = 30)
	private String locale;	
	
	@Column
	private Boolean requiresAuth;	
	
	@Column(length = 20)
	private String icon;
	
	@Column
	private Short sortOrder;

	// 使用MyBatis查询时，这个字段不会自动映射到数据库列，因此使用@Transient注解
	@Transient
	// 使用JPA时，必须注解为@Column(insertable = false, updatable = false)，在数据库创建这个字段，否则会因为没有这个字段映射失败
//	@Column(insertable = false, updatable = false)
    private Short depth;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Boolean getIsMenu() {
		return isMenu;
	}

	public void setIsMenu(Boolean isMenu) {
		this.isMenu = isMenu;
	}

	public Long getParent() {
		return parent;
	}

	public void setParent(Long parent) {
		this.parent = parent;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getRouterName() {
		return routerName;
	}

	public void setRouterName(String routerName) {
		this.routerName = routerName;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Boolean getRequiresAuth() {
		return requiresAuth;
	}

	public void setRequiresAuth(Boolean requiresAuth) {
		this.requiresAuth = requiresAuth;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

    public Short getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Short sortOrder) {
		this.sortOrder = sortOrder;
	}

    public Short getDepth() {
        return depth;
    }

    public void setDepth(Short depth) {
        this.depth = depth;
    }
    
}