package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * カートに追加された商品の情報を保持するエンティティ。
 *
 * <p>「cart_items」テーブルにマッピングされ、Cartエンティティとの紐付きを持ちつつ、
 * 商品ID・数量・表示用情報などを保持する。</p>
 *
 * <p>決済前の商品の明細として機能し、セッション上またはDB上のカート構成に用いられる。</p>
 */
@Entity
@Table(name = "cart_items")
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem extends BaseEntity {
	
	/** カート内商品の一意なID（主キー） */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** 紐づくカートエンティティ（親エンティティ） */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id")
	private Cart cart;
	
	/** 商品ID（外部キー） */
	@Column(name = "item_id", nullable = false)
	private Long itemId;
	
	/** 注文数量 */
	@Column(nullable = false)
	private Integer quantity;
	
	/** 商品名（画面表示用） */
	@Transient
	private String name;
	
	/** 商品名（英語表記・画面表示用） */
	@Transient
	private String nameEnglish;
	
	/** 商品画像のパス（画面表示用） */
	@Transient
	private String image;
	
	/** 商品の税込価格（画面表示用） */
	@Transient
	private Integer priceWithTax;
}
