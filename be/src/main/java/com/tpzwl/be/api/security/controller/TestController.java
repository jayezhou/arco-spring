package com.tpzwl.be.api.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tpzwl.be.api.payload.response.Response;
import com.tpzwl.be.api.security.model.RoleCount;
import com.tpzwl.be.api.security.model.RoleCountResResult;
import com.tpzwl.be.api.security.service.ExampleService;

//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

	@Autowired
	private ExampleService exampleService;

	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}

//	@GetMapping("/roleCount")
//	public List<RoleCount> roleCount() {
//		return exampleService.roleCount();
//	}

	@GetMapping("/roleCount")
	public Response<RoleCountResResult> roleCount() {
		List<RoleCount> list = exampleService.roleCount();
		RoleCountResResult result = new RoleCountResResult();
		result.setRoleCounts(list);
		Response<RoleCountResResult> res = new Response<RoleCountResResult>(20000L, null, result);
		return res;
	}

	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public String userAccess() {
		return "User Content.";
	}

	@GetMapping("/mod")
	@PreAuthorize("hasRole('MODERATOR')")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}
}
