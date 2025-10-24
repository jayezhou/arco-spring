package com.tpzwl.be.api.project.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tpzwl.be.api.project.user.payload.response.MenuResponse;
import com.tpzwl.be.api.security.mapper.RoleMapper;
import com.tpzwl.be.api.security.model.Permission;
import com.tpzwl.be.api.security.model.User;
import com.tpzwl.be.api.security.repository.RoleRepository;
import com.tpzwl.be.api.security.repository.UserRepository;

@Service
public class UserMenuService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private RoleMapper roleMapper;

	public List<MenuResponse> getUserMenusByName(String name) {
		User user = userRepository.findByUsername(name).orElseThrow(() -> new RuntimeException("Error: User is not found."));
		List<Long> roleIds = user.getRoles().stream().map(role -> role.getId()).collect(Collectors.toList());	
		
		/**
		 * 分别用JPA命名查询和MyBatis实现获取菜单树的功能。对于复杂查询，MyBatis通常更灵活和高效一些。
		 * 而且列和对象属性的映射更方便，可查看Permission类的的depth属性，它只是一个临时属性，没有在数据库中创建对应的列，这时JPA就需要创建这个列，否则会映射失败，而MyBatis就不需要。
		 * 所以应该只使用JPA进行数据库的迁移（自动建表修改表、自动增减字段）和实体的简单CRUD，而复杂查询使用MyBatis来实现。
         */
//		List<Permission> permissions = roleRepository.findMenuTreeByRoleIds(roleIds);
		List<Permission> permissions = roleMapper.findMenuTreeByRoleIds(roleIds);
		
		// Create a HashMap to hold the menus grouped by their depth
		HashMap<Short, HashMap<Long, Permission>> permissionMapByDepth = new HashMap<>(); 	
		for (Permission permission : permissions) {
		    // Get the depth of the current permission
		    Short depth = permission.getDepth().shortValue();	
		    // Get or create the sub-HashMap for the current depth
		    HashMap<Long, Permission> subMap = permissionMapByDepth.computeIfAbsent(depth, k -> new HashMap<Long, Permission>());	
		    // Add the Permission to the sub-HashMap with the permission ID as the key
		    subMap.put(permission.getId(), permission);
		}
		
		// Build the menu tree starting from depth 1
		List<MenuResponse> menuTree = new ArrayList<>();
		HashMap<Short, List<MenuResponse>> menuMapByDepth = new HashMap<>();

		for (short i = 1; i <= permissionMapByDepth.size(); i++) {
			HashMap<Long, Permission> currentLevel = permissionMapByDepth.get(i);
			List<MenuResponse> menuResponseList = new ArrayList<>();
			for (Permission permission : currentLevel.values()) {
				MenuResponse menuResponse = new MenuResponse();
				menuResponse.setId(permission.getId());
				menuResponse.setName(permission.getRouterName());
				menuResponse.setPermission(permission.getPermission());
				menuResponse.setPath(permission.getPath());
				MenuResponse.Meta meta = menuResponse.new Meta();
				meta.setLocale(permission.getLocale());
				meta.setIcon(permission.getIcon());
				meta.setRequiresAuth(permission.getRequiresAuth());
				meta.setOrder(permission.getSortOrder());
				menuResponse.setMeta(meta);
				menuResponse.setChildren(new ArrayList<MenuResponse>());
				menuResponseList.add(menuResponse);
				
				if (i == 1) {
					menuTree.add(menuResponse);
				} else {
					List<MenuResponse> previousLevelMenuResponseList = menuMapByDepth.get((short)(i - 1));
					for (MenuResponse parentMenuResponse : previousLevelMenuResponseList) {
						if (permission.getParent() != null
								&& permission.getParent().equals(parentMenuResponse.getId())) {
							parentMenuResponse.getChildren().add(menuResponse);
							break;
						}
					}   
				}
			}		
			menuMapByDepth.put(i, menuResponseList);
		}

		return menuTree;	
    }

}
