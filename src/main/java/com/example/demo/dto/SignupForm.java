package com.example.demo.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class SignupForm {

	@NotBlank(message = "メールアドレスは必須です")
	@Email(message = "メールアドレスの形式が正しくありません")
  private String email;

	@NotBlank(message = "パスワードは必須です")
  @Length(min = 8, max = 20, message = "パスワードは8〜20文字で入力してください")
  private String password;

	@NotBlank(message = "姓は必須です")
  private String lastName;

  @NotBlank(message = "名は必須です")
  private String firstName;
  
  private String lastNameKana;
  
  private String firstNameKana;
  
  @NotBlank(message = "郵便番号は必須です")
  private String postCode;
  
  @NotBlank(message = "都道府県は必須です")
  private String prefecture;
  
  @NotBlank(message = "市区町村は必須です")
  private String city;
  
  @NotBlank(message = "番地は必須です")
  private String street;
  
  private String building;
  
  @NotBlank(message = "電話番号は必須です")
  private String phoneNumber;
  
  private LocalDate birthday;
  
}
