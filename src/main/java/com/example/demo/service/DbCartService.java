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
import com.example.demo.util.PricingUtils;

import lombok.RequiredArgsConstructor;

/**
 * DBに保存されたユーザーカートの管理を担うサービスクラス。
 *
 * <p>カートの取得・作成・商品追加・数量更新・削除・合計金額計算など、
 * 永続的なカート操作に関するユースケースを包括的に提供する。</p>
 * <p>セッションカートとのマージや、購入後のカートクリア処理も含まれる。</p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DbCartService {
	
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ItemRepository itemRepository;
	
	/**
	 * 指定ユーザーIDに対応するカートを取得する。
	 * 
	 * <p>カートが存在しない場合は新しく作成して永続化する。</p>
	 *
	 * @param userId 対象ユーザーのID
	 * @return 対応するCartエンティティ
	 */
	public Cart getOrCreateCart(Long userId) {
		return cartRepository.findByUserId(userId).orElseGet(() -> {
			Cart newCart = new Cart();
			newCart.setUserId(userId);
			return cartRepository.save(newCart);
		});
	}
	
	/**
	 * 指定したユーザーのカートに商品を追加する。
	 * 
	 * <p>すでに同一商品が存在する場合は数量を加算し、新規商品ならリストに追加する。</p>
	 *
	 * @param userId 対象ユーザーのID
	 * @param item 追加する商品エンティティ
	 * @param quantity 追加する数量
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
	 * カート内の商品数量を更新する。
	 * 
	 * <p>指定されたユーザーと商品IDに一致するCartItemが存在する場合、数量を上書きする。</p>
	 *
	 * @param userId 対象ユーザーのID
	 * @param itemId 対象商品のID
	 * @param quantity 更新する数量
	 */
	public void updateQuantity(Long userId, Long itemId, int quantity) {
		Cart cart = getOrCreateCart(userId);
		cartItemRepository.findByCartIdAndItemId(cart.getId(), itemId).ifPresent(cartItem -> {
			cartItem.setQuantity(quantity);
			cartItemRepository.save(cartItem);
		});
	}
	
	/**
	 * カート内の合計金額（税込）を計算する。
	 * 
	 * <p>各商品の税込価格と数量を掛け合わせた合計を返す。</p>
	 *
	 * @param cartItems カート内のアイテム一覧
	 * @return 合計金額（税込）
	 */
	public int calculateTotalPrice(List<CartItem> cartItems) {
		int total = 0;
		for (CartItem cartItem : cartItems) {
			Long itemId = cartItem.getItemId();
			Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
			int priceWithTax = PricingUtils.calculatePriceWithTax(item.getPrice());
			total += priceWithTax * cartItem.getQuantity();
		}
		return total;
	}
	
	/**
	 * 指定された商品をカート内から削除する。
	 * 
	 * @param userId 対象ユーザーのID
	 * @param itemId 対象商品のID
	 */
	public void removeFromCart(Long userId, Long itemId) {
		//removeItem(userId, itemId); // 既存のメソッドをラップ
		Cart cart = getOrCreateCart(userId);
		cartItemRepository.deleteByCartIdAndItemId(cart.getId(), itemId);
	}
	
	/**
	 * 指定されたカートIDに紐づくすべての商品アイテムを削除する。
	 *
	 * <p>主に購入完了後やカート初期化処理で使用される。</p>
	 *
	 * @param cartId カートを識別するID
	 */
	public void clearCart(Long cartId) {
		cartItemRepository.deleteByCartId(cartId);;
	}
	
	/**
	 * 指定ユーザーのカートに登録されているすべての商品アイテムを取得する。
	 * 
	 * <p>各CartItemに対して、商品名・画像・税込価格などの表示用情報をセットする。</p>
	 *
	 * @param userId 対象ユーザーのID
	 * @return CartItemの一覧
	 */
	public List<CartItem> getCartItems(Long userId) {
		List<CartItem> cartItems = cartItemRepository.findByCart_UserId(userId);
		for (CartItem cartItem : cartItems) {
			Item item = itemRepository.findById(cartItem.getItemId())
					.orElseThrow(() -> new RuntimeException("Item not found"));
			cartItem.setName(item.getName());
			cartItem.setNameEnglish(item.getNameEnglish());
			cartItem.setImage(item.getImage());
			cartItem.setPriceWithTax(PricingUtils.calculatePriceWithTax(item.getPrice()));
		}
		return cartItems;
	}
	
	/**
	 * 指定ユーザーに紐づくカートIDを取得する。
	 *
	 * <p>ユーザーに対応するCartが存在しない場合は例外をスローする。</p>
	 *
	 * @param userId カートを取得したいユーザーのID
	 * @return ユーザーに紐づくCartのID
	 * @throws RuntimeException ユーザーに対応するCartが存在しない場合
	 */
	public Long getCartIdByUserId(Long userId) {
		return cartRepository.findByUserId(userId)
			.orElseThrow(() -> new RuntimeException("カートが見つかりません"))
			.getId();
	}
	
	/**
	 * ログイン後、セッションカートの内容をDBカートに統合する。
	 * 商品が存在する場合は数量を加算し、存在しない場合は新規追加する。
	 *
	 * @param userId ログインしたユーザーのID
	 * @param sessionItems セッションに保存されていた一時的なカートアイテム一覧
	 */
	public void mergeSessionCart(Long userId, List<SessionCartItem> sessionItems) {
		for (SessionCartItem sessionItem : sessionItems) {
			Long itemId = sessionItem.getItemId();
			int quantity = sessionItem.getQuantity();
			
			// DBから最新のItemを取得
			Item item = itemRepository.findById(itemId)
					.orElseThrow(() -> new RuntimeException("商品が存在しません: itemId=" + itemId));
			
			// DBカートに追加（既に存在すれば加算）
			addToCart(userId, item, quantity);
		}
	}
}
