package com.tpzwl.be.api.security.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@NamedNativeQuery(
		name = "Role.countByRole", 
		query = "SELECT r.id, r.name, COUNT(r.*) count FROM roles r GROUP BY r.id ORDER by r.id", 
		resultSetMapping = "Mapping.RoleCount"
)
@SqlResultSetMapping(
		name = "Mapping.RoleCount", 
		classes = @ConstructorResult(targetClass = RoleCount.class, columns = {
				@ColumnResult(name = "id"), @ColumnResult(name = "name", type = String.class), @ColumnResult(name = "count") 
		})
)


@NamedNativeQuery(
    name = "Role.findMenuTreeByRoleIds",
    query = "WITH RECURSIVE permission_tree AS ( " +
            "    SELECT p.id, p.permission, p.is_menu, p.parent, p.path, p.router_name, p.locale, p.requires_auth, p.icon, p.sort_order, " +
            "       1 AS depth " +
            "    FROM role_permissions rp " +
            "    JOIN permissions p ON rp.permission_id = p.id " +
            "    WHERE rp.role_id IN (:roleIds) AND p.parent is null AND p.is_menu = true " +
            "  UNION ALL " +
            "    SELECT p.id, p.permission, p.is_menu, p.parent, p.path, p.router_name, p.locale, p.requires_auth, p.icon, p.sort_order, " +
            "       pt.depth + 1 AS depth" +
            "    FROM permissions p " +
            "    JOIN permission_tree pt ON p.parent = pt.id " +
            "    WHERE p.is_menu = true " +
            ") " +
            "SELECT id, permission, is_menu, parent, path, router_name, locale, requires_auth, icon, sort_order, depth FROM permission_tree",
    resultClass = Permission.class
)

@Entity
@Table(name = "roles")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(length = 40)
	private EnumRole name;

	@ManyToMany
	@JoinTable(
	    name = "role_authorities",
	    joinColumns = @JoinColumn(name = "role_id"),
	    inverseJoinColumns = @JoinColumn(name = "authority_id")
	)
	private Set<Authority> authorities = new HashSet<>();
	
	@ManyToMany
	@JoinTable(
	    name = "role_permissions",
	    joinColumns = @JoinColumn(name = "role_id"),
	    inverseJoinColumns = @JoinColumn(name = "permission_id")
	)
	private Set<Permission> permissions = new HashSet<>();

	public Role() {
	}

	public Role(EnumRole name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EnumRole getName() {
		return name;
	}

	public void setName(EnumRole name) {
		this.name = name;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

}