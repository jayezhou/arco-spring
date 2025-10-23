package com.tpzwl.be.api.project.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tpzwl.be.api.project.user.payload.response.UserInfoResponse;
import com.tpzwl.be.api.security.model.User;
import com.tpzwl.be.api.security.repository.UserRepository;

@Service
public class UserInfoService {

	@Autowired
	private UserRepository userRepository;

	public UserInfoResponse getUserInfoByName(String name) {
		User user = userRepository.findByUsername(name).orElseThrow(() -> new RuntimeException("Error: User is not found."));

		UserInfoResponse userInfoResponse = new UserInfoResponse();
		userInfoResponse.setId(user.getId());
		userInfoResponse.setName(user.getUsername());
		userInfoResponse.setEmail(user.getEmail());
		
		List<String> permissions = user.getRoles().stream().flatMap(role -> role.getPermissions().stream())
				.map(permission ->  permission.getPermission() ).distinct()
				.collect(Collectors.toList());	
		
		userInfoResponse.setPermissions(permissions);

		return userInfoResponse;	
    }

}
