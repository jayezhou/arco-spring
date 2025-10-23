package com.tpzwl.be.api.security.controller;

import java.util.HashSet;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tpzwl.be.api.payload.response.Response;
import com.tpzwl.be.api.security.exception.TokenRefreshException;
import com.tpzwl.be.api.security.jwt.JwtUtils;
import com.tpzwl.be.api.security.model.EnumDeviceType;
import com.tpzwl.be.api.security.model.EnumRole;
import com.tpzwl.be.api.security.model.RefreshToken;
import com.tpzwl.be.api.security.model.Role;
import com.tpzwl.be.api.security.model.User;
import com.tpzwl.be.api.security.payload.request.LoginRequest;
import com.tpzwl.be.api.security.payload.request.RefreshTokenRequest;
import com.tpzwl.be.api.security.payload.request.SignupRequest;
import com.tpzwl.be.api.security.payload.response.LoginResponse;
import com.tpzwl.be.api.security.repository.RoleRepository;
import com.tpzwl.be.api.security.repository.UserRepository;
import com.tpzwl.be.api.security.service.RefreshTokenService;
import com.tpzwl.be.api.security.service.UserDetailsImpl;

/**
 * AuthController: 缺省的用于处理用户认证和注册等等的控制器。
 * 相对应的com.tpzwl.be.api.project.user.controller.FrontendAuthController是为前端定制的处理用户认证和注册等等的控制器
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	RefreshTokenService refreshTokenService;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

//		ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails, loginRequest.getDeviceType());
//
////		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
////				.collect(Collectors.toList());
//
//		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId(),
//				loginRequest.getDeviceType());
//
//		ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());
//
//		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
//				.header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString()).body(new UserInfoResponse(
//						userDetails.getId(), userDetails.getUsername(), userDetails.getEmail()));
		
		String token = jwtUtils.generateTokenFromUsernameAndDeviceType(userDetails.getUsername(), 
				loginRequest.getDeviceType());

		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId(),
				loginRequest.getDeviceType());

		return ResponseEntity.ok().body(new LoginResponse(
						userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), 
						token, refreshToken.getToken()));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			throw new RuntimeException("Error: Username is already taken!");
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			throw new RuntimeException("Error: Email is already in use!");
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(EnumRole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "adm":
					Role adminRole = roleRepository.findByName(EnumRole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "mod":
					Role modRole = roleRepository.findByName(EnumRole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(EnumRole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new Response<User>(20000L, "User registered successfully!", user));
	}

	@PostMapping("/signout")
	public ResponseEntity<?> logoutUser(HttpServletRequest request) {
		Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long userId = null;
		if (principle.toString() != "anonymousUser") {
			userId = ((UserDetailsImpl) principle).getId();
			String jwt = jwtUtils.getJwtFromHeader(request);
			EnumDeviceType deviceType = jwtUtils.getDeviceTypeFromJwtToken(jwt);
			refreshTokenService.deleteByUserIdAndDeviceType(userId, deviceType);
		}

//		ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
//		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
//				.body(new MessageResponse("You've been signed out!"));

		return ResponseEntity.ok().body(new Response<Long>(20000L, "You've been signed out!", userId));		
	}

	@PostMapping("/refreshtoken")
	public ResponseEntity<?> refreshtoken(HttpServletRequest request,
			@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
//		String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);
//
//		if ((refreshToken != null) && (refreshToken.length() > 0)) {
//			return refreshTokenService.findByToken(refreshToken).map(refreshTokenService::verifyExpiration)
//					.map(RefreshToken::getUser).map(user -> {
//						ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user,
//								refreshTokenRequest.getDeviceType());
//
//						RefreshToken refreshTokenObj = refreshTokenService.createRefreshToken(user.getId(),
//								refreshTokenRequest.getDeviceType());
//						ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshTokenObj.getToken());
//
//						return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
//								.header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
//								.body(new MessageResponse("Token is refreshed successfully!"));
//					}).orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token is not in database!"));
//		}
//
//		return ResponseEntity.badRequest().body(new MessageResponse("Refresh Token is empty!"));
		
		String refreshToken = jwtUtils.getRefreshTokenFromHeader(request);

		if ((refreshToken != null) && (refreshToken.length() > 0)) {
			return refreshTokenService.findByToken(refreshToken).map(refreshTokenService::verifyExpiration)
					.map(RefreshToken::getUser).map(user -> {
						String token = jwtUtils.generateTokenFromUsernameAndDeviceType(user.getUsername(),
								refreshTokenRequest.getDeviceType());

						RefreshToken refreshTokenObj = refreshTokenService.createRefreshToken(user.getId(),
								refreshTokenRequest.getDeviceType());
						
						return ResponseEntity.ok().body(new LoginResponse(
								user.getId(), user.getUsername(), user.getEmail(), 
								token, refreshTokenObj.getToken()));
					}).orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token is not in database!"));
		} else {
			throw new TokenRefreshException(refreshToken, "Refresh Token is empty!");
		}	
	}

}
