package com.example.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.SignupForm;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignupService {
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	public void signup(SignupForm form) {
		User user = User.builder()
			.lastName(form.getLastName())
			.firstName(form.getFirstName())
			.lastNameKana(form.getLastNameKana())
			.firstNameKana(form.getFirstNameKana())
			.email(form.getEmail())
			.password(passwordEncoder.encode(form.getPassword()))
			.postCode(form.getPostCode())
			.prefecture(form.getPrefecture())
			.city(form.getCity())
			.street(form.getStreet())
			.building(form.getBuilding())
			.phoneNumber(form.getPhoneNumber())
			.birthday(form.getBirthday())
			.isDeleted(false)
			.role("user")
			.build();
		userRepository.save(user);
	}
}
