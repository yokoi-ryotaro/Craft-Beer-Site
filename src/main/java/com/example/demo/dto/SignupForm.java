package com.example.demo.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

/**
 * ユーザー登録画面における入力フォームモデル。
 *
 * <p>メールアドレス・パスワード・氏名・住所・電話番号・生年月日などの情報を保持し、
 * サインアップ処理の入力値バインドとバリデーションに使用される。</p>
 *
 * <p>バリデーションには @NotBlank や @Email、@Length などが使用され、必須項目や形式チェックが行われる。</p>
 *
 * <p>登録処理完了後は SignupService により User エンティティへと変換され、DB登録へとつながる。</p>
 */
@Data
public class SignupForm {
	
	/** メールアドレス（ユニーク・認証に使用） */
	@NotBlank(message = "メールアドレスは必須です")
	@Email(message = "メールアドレスの形式が正しくありません")
	private String email;
	
	/** パスワード（必須） */
	@NotBlank(message = "パスワードは必須です")
	@Length(min = 8, max = 20, message = "パスワードは8〜20文字で入力してください")
	private String password;
	
	/** 姓（必須） */
	@NotBlank(message = "姓は必須です")
	private String lastName;
	
	/** 名（必須） */
	@NotBlank(message = "名は必須です")
	private String firstName;
	
	/** 姓（カナ） */
	private String lastNameKana;
	
	/** 名（カナ） */
	private String firstNameKana;
	
	/** 郵便番号（必須） */
	@NotBlank(message = "郵便番号は必須です")
	private String postCode;
	
	/** 都道府県名（必須） */
	@NotBlank(message = "都道府県は必須です")
	private String prefecture;
	
	/** 市区町村（必須） */
	@NotBlank(message = "市区町村は必須です")
	private String city;
	
	/** 町名・番地などの詳細住所（必須） */
	@NotBlank(message = "番地は必須です")
	private String street;
	
	/** 建物名・部屋番号など */
	private String building;
	
	/** 電話番号（必須） */
	@NotBlank(message = "電話番号は必須です")
	private String phoneNumber;
	
	/** 生年月日 */
	private LocalDate birthday;
}
