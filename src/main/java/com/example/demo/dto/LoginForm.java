package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

/**
 * ユーザー認証に使用されるログインフォームの入力モデル。
 *
 * <p>メールアドレスとパスワードを受け取り、Spring Securityの認証処理に渡される。</p>
 * <p>@NotBlank および @Email によるバリデーションが適用されており、不正な入力を防止する。</p>
 */
@Data
public class LoginForm {
	
	/** ログイン時に入力されるメールアドレス（必須、形式チェックあり） */
	@NotBlank(message = "メールアドレスを入力してください")
	@Email
	private String email;
	
	/** ログイン時に入力されるパスワード（必須） */
	@NotBlank(message = "パスワードを入力してください")
	private String password;
}
