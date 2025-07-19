package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

/**
 * 商品購入時に入力されるユーザーの配送・連絡先情報を保持するフォームクラス。
 *
 * <p>入力値は @NotBlank アノテーションによりバリデーションが行われ、チェックアウト画面での送信処理に使用される。</p>
 *
 * <p>配送情報、支払い方法、連絡先などが含まれており、購入内容を Order や OrderItem にマッピングする際の基礎データとなる。</p>
 */
@Data
public class CheckoutForm {
	
	/** 姓 */
	@NotBlank(message = "姓は必須です")
	private String lastName;
	
	/** 名 */
	@NotBlank(message = "名は必須です")
	private String firstName;
	
	/** 注文者メールアドレス */
	@NotBlank(message = "メールアドレスは必須です")
	private String email;
	
	/** 郵便番号 */
	@NotBlank(message = "郵便番号は必須です")
	private String postCode;
	
	/** 都道府県 */
	@NotBlank(message = "都道府県は必須です")
	private String prefecture;
	
	/** 市区町村 */
	@NotBlank(message = "市区町村は必須です")
	private String city;
	
	/** 町名・番地などの詳細住所 */
	@NotBlank(message = "番地は必須です")
	private String street;
	
	/** 建物名・部屋番号など（任意） */
	private String building;
	
	/** 電話番号 */
	@NotBlank(message = "電話番号は必須です")
	private String phoneNumber;
	
	/** 決済方法（例："CREDIT", "BANK", "COD"） */
	private String paymentMethod;
}
