package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CheckoutForm {
	@NotBlank(message = "姓は必須です")
  private String lastName;

  @NotBlank(message = "名は必須です")
  private String firstName;
  
  @NotBlank(message = "メールアドレスは必須です")
  private String email;

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
  
  private String paymentMethod;
}
