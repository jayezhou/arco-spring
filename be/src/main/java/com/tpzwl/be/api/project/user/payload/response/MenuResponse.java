package com.tpzwl.be.api.project.user.payload.response;

import java.util.ArrayList;
import java.util.List;

public class MenuResponse {
	
	public class Meta {
		private String locale;
		private Boolean requiresAuth;
		private String icon;
		private Short order;
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
		public Short getOrder() {
			return order;
		}
		public void setOrder(Short order) {
			this.order = order;
		}
	}


	private Long id;
	private String path;
	private String name;
	private String permission;
	private Meta meta;
	private List<MenuResponse> children = new ArrayList<>();

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Meta getMeta() {
		return meta;
	}
	public void setMeta(Meta meta) {
		this.meta = meta;
	}
	public List<MenuResponse> getChildren() {
		return children;
	}
	public void setChildren(List<MenuResponse> children) {
		this.children = children;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}


}
