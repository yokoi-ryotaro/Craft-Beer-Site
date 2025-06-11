package com.example.demo.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Item;
import com.example.demo.model.CartItem;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
	
	private static final String SESSION_CART_KEY = "cart";

	/**
   * セッションからカート取得（なければ新規作成）
   */
	@SuppressWarnings("unchecked")
	public List<CartItem> getCart(HttpSession session) {
		List<CartItem> cart = (List<CartItem>) session.getAttribute(SESSION_CART_KEY);
		if (cart == null) {
			cart = new ArrayList<>();
			session.setAttribute(SESSION_CART_KEY, cart);
		}
		return cart;
	}
	
	/**
   * 商品をカートに追加
   */
	public void addToCart(HttpSession session, Item item, int quantity) {
		List<CartItem> cart = getCart(session);
		for (CartItem cartItem : cart) {
			if (cartItem.getItemId().equals(item.getId())) {
				cartItem.setQuantity(cartItem.getQuantity() + quantity);
				return;
			}
		}
		CartItem newItem = new CartItem();
		newItem.setItem(item);
		newItem.setItemId(item.getId());
		newItem.setName(item.getName());
		newItem.setQuantity(quantity);
		newItem.setImagePath(item.getImage());
		newItem.setNameEnglish(item.getNameEnglish());
		cart.add(newItem);
	}
	
	/**
	 * カート内の合計金額を計算
	 */
	public int calculateTotalPrice(List<CartItem> cart) {
		return cart.stream()
				.mapToInt(item -> item.getPriceWithTax() * item.getQuantity())
				.sum();
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
	* カート内商品の数量更新
	*/
	public void updateQuantity(HttpSession session, Long itemId, int quantity) {
		List<CartItem> cart = getCart(session);
		for (CartItem cartItem : cart) {
			if (cartItem.getItemId().equals(itemId)) {
				cartItem.setQuantity(quantity);
				break;
			}
		}
	}
	
	/**
	 * 商品をカートから削除
	 */
	public void removeFromCart(HttpSession session, Long itemId) {
		List<CartItem> cart = getCart(session);
		Iterator<CartItem> iterator = cart.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getItemId().equals(itemId)) {
				iterator.remove();
				break;
			}
		}
	}
	
	/**
	 * カートを空にする（購入後など）
	 */
	public void clearCart(HttpSession session) {
		session.setAttribute(SESSION_CART_KEY, new ArrayList<CartItem>());
	}
}
