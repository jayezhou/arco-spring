package com.tpzwl.be.api.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tpzwl.be.api.payload.response.Response;
import com.tpzwl.be.api.security.model.RoleCount;
import com.tpzwl.be.api.security.model.RoleCountResResult;
import com.tpzwl.be.api.security.model.User;
import com.tpzwl.be.api.security.service.ExampleService;

//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/example")
public class ExampleController {

	@Autowired
	private ExampleService exampleService;

	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> allAccess() {
		return exampleService.findAll();
	}

	@GetMapping("/roleCount")
	@PreAuthorize("hasRole('ADMIN')")
//	@PreAuthorize("hasAuthority('user:delete')")
	public Response<RoleCountResResult> roleCount() {
		List<RoleCount> list = exampleService.roleCount();
		RoleCountResResult result = new RoleCountResResult();
		result.setRoleCounts(list);
		Response<RoleCountResResult> res = new Response<RoleCountResResult>(20000L, null, result);
		return res;
	}
	
	@GetMapping("/dashboardWorkplaceRetrieve")
	@PreAuthorize("hasAuthority('dashboard:workplace:retrieve')")
	public String dashboardWorkplaceRetrieve() {
		return "dashboard:workplace:retrieve";
	}	

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public String userAccess(@PathVariable String id) {
		return "User Content.";
	}

}
