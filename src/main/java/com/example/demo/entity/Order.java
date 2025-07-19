package com.example.demo.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * ユーザーの注文情報を表すエンティティ。
 *
 * <p>「orders」テーブルにマッピングされ、配送先・決済方法・合計金額・送料・タイムスタンプなど
 * 注文処理に必要な情報を保持する。</p>
 *
 * <p>注文履歴や管理画面、決済連携処理などで使用される中心的な構造。</p>
 */
@Entity
@Table(name = "orders")
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {
	
	/** 注文ID（主キー） */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** 注文者のユーザーID（外部キー） */
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
	
	/** 商品合計金額（税込） */
	@Column(name = "total_price", nullable = false)
	private Integer totalPrice;
	
	/** 送料 */
	@Column(name = "shipping_fee", nullable = false)
	private Integer shippingFee;
	
	/** 支払い合計金額（合計金額 + 送料） */
	@Column(name = "payment_total", nullable = false)
	private Integer paymentTotal;
	
	@OneToMany(mappedBy = "orderId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<OrderItem> orderItems;
	
}
