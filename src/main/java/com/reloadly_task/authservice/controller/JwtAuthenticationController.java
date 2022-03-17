package com.reloadly_task.authservice.controller;

import com.reloadly_task.authservice.config.JwtTokenUtil;
import com.reloadly_task.authservice.dto.JwtRequest;
import com.reloadly_task.authservice.dto.JwtResponse;
import com.reloadly_task.authservice.dto.UserDto;
import com.reloadly_task.authservice.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController("/authentication")
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@PostMapping(value = "/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}

	@PostMapping(value = "/register")
	public ResponseEntity<?> saveUser(@RequestBody UserDto user) throws Exception {
		return ResponseEntity.ok(userDetailsService.save(user));
	}

	@PutMapping(value = "/update")
	public ResponseEntity<?> updateUser(@RequestBody UserDto user) throws Exception {
		return ResponseEntity.ok(userDetailsService.updateUser(user));
	}

	@GetMapping("/find-details")
	public ResponseEntity<?> findUser(){
		return new ResponseEntity<>(userDetailsService.findUser(), HttpStatus.OK);
	}

	@GetMapping("/validate")
	public ResponseEntity<?> validateJwt(@RequestParam("token") String token){
		UserDetails userDetails = userDetailsService.loadUserByUsername(
				jwtTokenUtil.getUsernameFromToken(token));
		return new ResponseEntity<>(jwtTokenUtil.validateToken(token, userDetails), HttpStatus.OK);
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
