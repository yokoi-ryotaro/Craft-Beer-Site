package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// ログインユーザーのID
	@Column(name = "user_id", nullable = false)
	private Long userId;
	
	// 配送情報
	@Column(name = "last_name", nullable = false)
	private String lastName;
	
	@Column(name = "first_name", nullable = false)
	private String firstName;
	
	@Column(nullable = false)
	private String email;
	
	@Column(name = "post_code", nullable = false)
	private String postCode;
	
	@Column(nullable = false)
	private String prefecture;
	
	@Column(nullable = false)
	private String city;
	
	@Column(nullable = false)
	private String street;
	
	@Column
	private String building;
	
	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;
	
	//決済方法：例 "CREDIT", "BANK", "COD"
	@Column(name = "payment_method", nullable = false)
	private String paymentMethod;
	
	// 金額
	@Column(name = "total_price", nullable = false)
	private Integer totalPrice;
	
	@Column(name = "shipping_fee", nullable = false)
	private Integer shippingFee;
	
	@Column(name = "payment_total", nullable = false)
	private Integer paymentTotal;
	
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}
	
	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
}
