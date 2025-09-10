package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.CheckoutTemp;
import com.example.demo.entity.Item;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.util.CartUtils;

import lombok.RequiredArgsConstructor;

/**
 * 注文および注文明細（OrderItem）の登録・取得処理を担当するサービスクラス。
 *
 * <p>注文全体の永続化をトランザクションで保証しつつ、ユーザーごとの注文履歴の取得などを提供する。</p>
 *
 * <p>Controller層やマイページ機能などから呼び出され、注文管理に関する責務を集約している。</p>
 */
@Service
@RequiredArgsConstructor
public class OrderService {
	
	@Autowired
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final ItemRepository itemRepository;
	
	/**
	 * 注文エンティティとその明細（OrderItem）をまとめて保存する。
	 *
	 * <p>まず注文本体を保存し、その生成されたIDを各OrderItemに紐づけて個別保存する。
	 * 一連の処理はトランザクションでラップされており、途中で失敗した場合はロールバックされる。</p>
	 *
	 * @param order 注文本体（配送先や支払い情報を含む）
	 * @param orderItems 注文明細リスト（商品ID・数量など）
	 */
	@Transactional
	public void saveOrderWithItems(Order order, List<OrderItem> orderItems) {
		// 注文本体を先に保存（IDを生成するため）
		Order savedOrder = orderRepository.save(order);
		
		// 各OrderItemに保存された注文の参照を設定して保存
		for (OrderItem item : orderItems) {
			item.setOrderId(savedOrder);
			orderItemRepository.save(item);
		}
	}
	
	/**
	 * 指定したユーザーIDに紐づく注文一覧を取得する。
	 *
	 * <p>作成日時の順序は保証されていない。履歴取得やユーザー注文数の集計などで使用される。</p>
	 *
	 * @param userId ユーザーID
	 * @return 該当ユーザーの注文リスト
	 */
	public List<Order> getOrdersByUserId(Long userId) {
		return orderRepository.findByUserId(userId);
	}
	
	/**
	 * 指定ユーザーの注文一覧を作成日時の降順で取得する。
	 *
	 * <p>最新の注文を先頭に並べた状態で取得でき、注文履歴表示などに適している。</p>
	 *
	 * @param userId ユーザーID
	 * @return 降順に並んだ注文リスト
	 */
	public List<Order> getOrdersByUserIdByCreatedAtDesc(Long userId) {
		return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
	}
	
	/**
   * 指定ユーザーのカート情報と一時保存された配送先・支払い情報から注文を作成し、DBに保存する。
   *
   * <p>以下の処理を行う:</p>
   * <ul>
   *   <li>CheckoutTempから配送先・支払い情報をOrderにコピー</li>
   *   <li>カートアイテムごとにOrderItemを作成し、税込価格を設定</li>
   *   <li>商品合計金額から送料を算出し、注文合計金額を計算</li>
   *   <li>{@link #saveOrderWithItems(Order, List)} を呼び出し、注文本体と明細を永続化</li>
   * </ul>
   *
   * <p>すべての処理はトランザクション内で実行され、途中で例外が発生した場合はロールバックされる。</p>
   *
   * @param userId ユーザーID
   * @param temp 一時保存された配送先・支払い情報
   * @param cartItems 注文対象のカートアイテム一覧
   */
	@Transactional
	public void createOrder(Long userId, CheckoutTemp temp, List<CartItem> cartItems) {
		
		Order order = new Order();
		order.setUserId(userId);
		order.setLastName(temp.getLastName());
		order.setFirstName(temp.getFirstName());
		order.setEmail(temp.getEmail());
		order.setPostCode(temp.getPostCode());
		order.setPrefecture(temp.getPrefecture());
		order.setCity(temp.getCity());
		order.setStreet(temp.getStreet());
		order.setBuilding(temp.getBuilding());
		order.setPhoneNumber(temp.getPhoneNumber());
		order.setPaymentMethod(temp.getPaymentMethod());
		
		int totalPrice = 0;
		List<OrderItem> orderItems = new ArrayList<>();

		for (CartItem ci : cartItems) {
			Item item = itemRepository.findById(ci.getItemId()).orElseThrow();
			int priceWithTax = (int) Math.floor(item.getPrice() * 1.1);
			
			OrderItem oi = new OrderItem();
			oi.setOrderId(order);
			oi.setItemId(ci.getItemId());
			oi.setQuantity(ci.getQuantity());
			oi.setPrice(priceWithTax);
			orderItems.add(oi);
			totalPrice += priceWithTax * ci.getQuantity();
		} 
		int shippingFee = CartUtils.calculateShippingFee(totalPrice);
		int paymentTotal = totalPrice + shippingFee;
		order.setTotalPrice(totalPrice);
		order.setShippingFee(shippingFee);
		order.setPaymentTotal(paymentTotal);
		saveOrderWithItems(order, orderItems);
	}
}
