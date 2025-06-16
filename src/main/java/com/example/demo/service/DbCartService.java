package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.SessionCartItem;
import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Item;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DbCartService {
	
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ItemRepository itemRepository;
	
	private static final double TAX_RATE = 0.1;
	
	/**
	 * ログインユーザーのカートを取得（なければ新規作成）
	 */
	public Cart getOrCreateCart(Long userId) {
		return cartRepository.findByUserId(userId).orElseGet(() -> {
						Cart newCart = new Cart();
						newCart.setUserId(userId);
						return cartRepository.save(newCart);
		});
	}
	
	/**
	 * カートに商品を追加
	 */
	public void addToCart(Long userId, Item item, int quantity) {
		Cart cart = getOrCreateCart(userId);
		
		Optional<CartItem> existing = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());
		
		if (existing.isPresent()) {
			CartItem cartItem = existing.get();
			cartItem.setQuantity(cartItem.getQuantity() + quantity);
			cartItemRepository.save(cartItem);
		} else {
			CartItem newItem = new CartItem();
			newItem.setCart(cart);
			newItem.setQuantity(quantity);
			newItem.setItemId(item.getId()); 
			cartItemRepository.save(newItem);
		}
	}
	
	/**
	 * カート内のすべての商品を取得
	 */
	public List<CartItem> getCartItems(Long userId) {
		return cartItemRepository.findByCart_UserId(userId);
	}
	
	/**
	 * 数量更新
	 */
	public void updateQuantity(Long userId, Long itemId, int quantity) {
		Cart cart = getOrCreateCart(userId);
		cartItemRepository.findByCartIdAndItemId(cart.getId(), itemId).ifPresent(cartItem -> {
			cartItem.setQuantity(quantity);
			cartItemRepository.save(cartItem);
		});
	}
		
	/**
	 * 商品削除
	 */
	public void removeItem(Long userId, Long itemId) {
		Cart cart = getOrCreateCart(userId);
		cartItemRepository.deleteByCartIdAndItemId(cart.getId(), itemId);
	}
	
	public void removeFromCart(Long userId, Long itemId) {
		removeItem(userId, itemId); // 既存のメソッドをラップ
	}
	
	public int calculateTotalPrice(List<CartItem> cartItems) {
		int total = 0;
		for (CartItem cartItem : cartItems) {
				Long itemId = cartItem.getItemId();
				Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
			int priceWithTax = (int) Math.floor(item.getPrice() * (1 + TAX_RATE));
			total += priceWithTax * cartItem.getQuantity();
		}
		return total;
	}
	
	/**
	 * 送料を計算（例：2000円未満 → 1000円、～4999円 → 500円、5000円～ → 0円）
	 */
	public int calculateShippingFee(int totalPrice) {
		if (totalPrice < 2000) return 1000;
		if (totalPrice < 5000) return 500;
		return 0;
	}
	
	/**
	 * セッションカートとDBカートをマージ（ログイン直後用）
	 */
	public void mergeSessionCart(Long userId, List<SessionCartItem> sessionItems) {
		for (SessionCartItem sessionItem : sessionItems) {
			addToCart(userId, sessionItem.getItem(), sessionItem.getQuantity());
		}
	}
	
	public Long getCartIdByUserId(Long userId) {
		return cartRepository.findByUserId(userId)
			.orElseThrow(() -> new RuntimeException("カートが見つかりません"))
			.getId();
	}
	
	/**
	 * カートを空にする（購入後など）
	 */
	public void clearCart(Long cartId) {
		cartItemRepository.deleteByCartId(cartId);;
	}
}
