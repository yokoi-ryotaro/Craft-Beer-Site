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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 注文に含まれる商品明細を表すエンティティ。
 *
 * <p>「order_items」テーブルにマッピングされ、注文ごとに複数の商品明細として
 * Orderエンティティと紐づく。一件の注文に複数のOrderItemを持つ構成。</p>
 *
 * <p>商品ID・数量・購入時の価格情報を保持し、履歴としての信頼性を担保する。</p>
 */
@Entity
@Table(name = "order_items")
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends BaseEntity {
	
	/** 商品明細ID（主キー） */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** 紐づく注文ID（親エンティティ） */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order orderId;
	
	/** 商品ID（外部キー） */
	@Column(name = "item_id", nullable = false)
	private Long itemId;
	
	/** 注文数量 */
	@Column(nullable = false)
	private Integer quantity;
	
	/** 購入時点の税抜価格（単価） */
	private Integer price;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", insertable = false, updatable = false)
	private Item item;

}
