package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;

/**
 * 注文明細（OrderItemエンティティ）の永続化および検索操作を提供するリポジトリインタフェース。
 *
 * <p>主に注文明細と注文（Order）との関連から、明細一覧の取得や個別保存処理に使用される。</p>
 *
 * <p>マイページの注文履歴表示や、注文確認画面における商品情報の展開に役立つ。</p>
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	
	/**
	 * 指定された注文（Order）に紐づく注文明細一覧を取得する。
	 *
	 * <p>1つの注文に複数の商品が含まれる場合、それらすべての明細がリストで返される。</p>
	 *
	 * @param orderId 対象となる注文エンティティ
	 * @return 注文明細のリスト
	 */
  List<OrderItem> findByOrderId(Order orderId);
}
