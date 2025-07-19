package com.example.demo.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.example.demo.dto.SessionCartItem;
import com.example.demo.entity.Item;

import lombok.RequiredArgsConstructor;

/**
 * ログイン前のセッションカートを管理するサービスクラス。
 *
 * <p>セッションスコープで一時的な商品情報（SessionCartItem）を保持・操作し、
 * ログイン後にDBカートへ統合する前の操作を担当する。</p>
 * <p>主な機能として、商品追加、数量変更、削除、合計金額・送料計算、カートのクリアなどが含まれる。</p>
 */
@Service
@RequiredArgsConstructor
public class CartService {
	
	private static final String SESSION_CART_KEY = "cart";

	/**
	 * セッションからカート一覧を取得する。
	 *
	 * <p>カートが存在しない場合は空のリストを作成してセッションに保存する。</p>
	 *
	 * @param session 現在のHTTPセッション
	 * @return セッションに格納されたSessionCartItemのリスト
	 */
	@SuppressWarnings("unchecked")
	public List<SessionCartItem> getCart(HttpSession session) {
		List<SessionCartItem> cart = (List<SessionCartItem>) session.getAttribute(SESSION_CART_KEY);
		if (cart == null) {
			cart = new ArrayList<>();
			session.setAttribute(SESSION_CART_KEY, cart);
		}
		return cart;
	}
	
	/**
	 * セッションカートに商品を追加する。
	 *
	 * <p>すでに同一商品が存在する場合は数量を加算し、新規商品ならリストに追加する。</p>
	 *
	 * @param session 現在のHTTPセッション
	 * @param item 追加する商品エンティティ
	 * @param quantity 追加する数量
	 */
	public void addToCart(HttpSession session, Item item, int quantity) {
		List<SessionCartItem> cart = getCart(session);
		for (SessionCartItem cartItem : cart) {
			if (cartItem.getItemId().equals(item.getId())) {
				cartItem.setQuantity(cartItem.getQuantity() + quantity);
				return;
			}
		}
		SessionCartItem newItem = new SessionCartItem();
		newItem.setItemId(item.getId());
		newItem.setName(item.getName());
		newItem.setNameEnglish(item.getNameEnglish());
		newItem.setQuantity(quantity);
		newItem.setPrice(item.getPrice());
		newItem.setImage(item.getImage());
		cart.add(newItem);
	}
	
	/**
	 * セッションカート内の指定商品の数量を更新する。
	 *
	 * @param session 現在のHTTPセッション
	 * @param itemId 更新対象商品のID
	 * @param quantity 設定する新しい数量
	 */
	public void updateQuantity(HttpSession session, Long itemId, int quantity) {
		List<SessionCartItem> cart = getCart(session);
		for (SessionCartItem cartItem : cart) {
			if (cartItem.getItemId().equals(itemId)) {
				cartItem.setQuantity(quantity);
				break;
			}
		}
	}
	
	/**
	 * セッションカート内の商品合計金額（税込）を計算する。
	 *
	 * <p>各商品の税込価格と数量を掛け合わせた合計を返す。</p>
	 *
	 * @param cartItems セッションカート内の商品一覧
	 * @return 合計金額（税込）
	 */
	public int calculateTotalPrice(List<SessionCartItem> cartItems) {
		return cartItems.stream()
				.mapToInt(item -> item.getPriceWithTax() * item.getQuantity())
				.sum();
	}
	
	/**
	 * セッションカートから指定商品を削除する。
	 *
	 * @param session 現在のHTTPセッション
	 * @param itemId 削除対象商品のID
	 */
	public void removeFromCart(HttpSession session, Long itemId) {
		List<SessionCartItem> cart = getCart(session);
		Iterator<SessionCartItem> iterator = cart.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getItemId().equals(itemId)) {
				iterator.remove();
				break;
			}
		}
	}
	
	/**
	 * セッションカート全体をクリアする（購入完了後など）。
	 *
	 * @param session 現在のHTTPセッション
	 */
	public void clearCart(HttpSession session) {
		session.removeAttribute(SESSION_CART_KEY);
	}
}
