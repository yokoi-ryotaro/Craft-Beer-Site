package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;

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
}
