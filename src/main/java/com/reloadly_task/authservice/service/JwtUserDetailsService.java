package com.reloadly_task.authservice.service;

import com.reloadly_task.authservice.dto.UserDto;
import com.reloadly_task.authservice.model.UserDao;
import com.reloadly_task.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private  UserRepository userDao;
	@Autowired
	private  PasswordEncoder bcryptEncoder;
	@Autowired
	private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDao user = userDao.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new User(user.getUsername(), user.getPassword(),
				new ArrayList<>());
	}

	public UserDao save(UserDto user) {
		UserDao newUser = new UserDao();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setFirstName(user.getFirstName());
		newUser.setLastName(user.getLastName());
		newUser.setGender(user.getGender());
		return userDao.save(newUser);
	}

	public UserDao findUser(){
		UserDetails principal =(UserDetails) SecurityContextHolder. getContext(). getAuthentication(). getPrincipal();
		return userRepository.findByUsername(principal.getUsername());
	}
	public UserDao updateUser(UserDto user) {
		Object principal = SecurityContextHolder. getContext(). getAuthentication(). getPrincipal();
		String username = ((UserDetails)principal).getUsername();
		UserDao newUser = userRepository.findByUsername(username);
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setFirstName(user.getFirstName());
		newUser.setLastName(user.getLastName());
		newUser.setGender(user.getGender());
		return userDao.save(newUser);
	}
}