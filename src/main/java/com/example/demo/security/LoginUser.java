package com.example.demo.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.entity.User;

import lombok.Getter;

@Getter
public class LoginUser implements UserDetails{
	
	private final User user;
	
	public LoginUser(User user) {
		this.user = user;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()));
	}
	
	@Override
	public String getPassword() {
		return user.getPassword();
	}
	
	@Override
	public String getUsername() {
		return user.getEmail();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true; // 必要に応じて切り替え
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true; // 必要に応じて切り替え
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true; // 必要に応じて切り替え
	}
	
	@Override
	public boolean isEnabled() {
		return !user.getIsDeleted(); // 退会済みかどうかで制御
	}
}
