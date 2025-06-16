package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	
	Optional <CartItem> findByCartIdAndItemId (Long cartId, Long itemId);
	
	List <CartItem> findByCart_UserId(Long userId);
	
	void deleteByCartIdAndItemId(Long cartId, Long itemId);
	
	// カート内の全商品を削除（カート削除 or 購入確定時）
	void deleteByCartId(Long cartId);
}
