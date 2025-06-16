package com.example.demo.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	
	/**
	 * ユーザー名からユーザーIDを取得する
	 */
	public Long getUserIdByEmail(String email) {
		return userRepository.findByEmail(email)
				.map(User::getId)
				.orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + email));
	}
	
	/**
	 * ユーザー名からUserエンティティを取得したい場合はこちら
	 */
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + email));
	}	
}
