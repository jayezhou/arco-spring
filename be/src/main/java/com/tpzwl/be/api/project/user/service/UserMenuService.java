package com.tpzwl.be.api.project.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tpzwl.be.api.project.user.payload.response.MenuResponse;
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

	public List<MenuResponse> getUserMenusByName(String name) {
		User user = userRepository.findByUsername(name).orElseThrow(() -> new RuntimeException("Error: User is not found."));
		List<Long> roleIds = user.getRoles().stream().map(role -> role.getId()).collect(Collectors.toList());	
		List<Permission> permissions = roleRepository.findMenuTreeByRoleIds(roleIds);
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

		for (short i = 1; i < permissionMapByDepth.size(); i++) {
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
