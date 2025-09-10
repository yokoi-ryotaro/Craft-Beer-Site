package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * checkoutFormの一時保存する内容を表すエンティティ。
 *
 * <p>「checkout_temp」テーブルにマッピングされ、checkoutFormの内容を一時保存する。</p>
 */
@Entity
@Table(name = "checkout_temp")
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutTemp extends BaseEntity {
	
	/** ID（主キー） */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** 注文者のユーザーID */
	@Column(name = "user_id", nullable = false)
	private Long userId;
	
	/** 姓 */
	@Column(name = "last_name", nullable = false)
	private String lastName;
	
	/** 名 */
	@Column(name = "first_name", nullable = false)
	private String firstName;
	
	/** 注文者メールアドレス */
	@Column(nullable = false)
	private String email;
	
	/** 郵便番号 */
	@Column(name = "post_code", nullable = false)
	private String postCode;
	
	/** 都道府県 */
	@Column(nullable = false)
	private String prefecture;
	
	/** 市区町村 */
	@Column(nullable = false)
	private String city;
	
	/** 町名・番地などの詳細住所 */
	@Column(nullable = false)
	private String street;
	
	/** 建物名・部屋番号など（任意） */
	@Column
	private String building;
	
	/** 電話番号 */
	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;
	
	/** 決済方法（例："CREDIT", "BANK", "COD"） */
	@Column(name = "payment_method", nullable = false)
	private String paymentMethod;
	
}
