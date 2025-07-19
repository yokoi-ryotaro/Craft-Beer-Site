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
 * ログインユーザーに紐づく商品カートを表すエンティティ。
 *
 * <p>「carts」テーブルにマッピングされ、カート内の商品（CartItem）との一対多リレーションを持つ。
 * ユーザーごとの購入前商品リストを保持し、注文処理や在庫確認などで参照される。</p>
 *
 * <p>作成日時・更新日時を保持し、セッションからDBへ移行する際の保存対象となる。</p>
 */
@Entity
@Table(name = "carts")
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart extends BaseEntity {
	
	/** カートID（主キー） */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** ユーザーID（外部キー） */
	@Column(name = "user_id", nullable = false)
	private Long userId;
	
	/** カート内の商品のリスト（CartItemとの一対多リレーション） */
	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<CartItem> items;
}
