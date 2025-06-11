package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginForm {
	
	@NotBlank(message = "メールアドレスを入力してください")
	@Email
	private String email;
	
	@NotBlank(message = "パスワードを入力してください")
	private String password;
}
