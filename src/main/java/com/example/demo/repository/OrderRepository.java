package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Order;

/**
 * 注文情報（Orderエンティティ）の永続化および検索操作を行うリポジトリインタフェース。
 *
 * <p>主にユーザーIDに基づく注文履歴の取得処理を提供し、
 * Spring Data JPA によりクエリが自動生成される。</p>
 *
 * <p>注文登録は OrderService から行われ、取得はマイページや管理画面などに利用される。</p>
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
	
	/**
	 * 指定したユーザーIDに紐づく注文一覧を取得する。
	 *
	 * <p>作成日時の順序は保証されないため、履歴表示などでは適宜ソートが必要。</p>
	 *
	 * @param userId 検索対象のユーザーID
	 * @return 該当ユーザーの注文リスト
	 */
  List<Order> findByUserId(Long userId);
  
  /**
   * 指定ユーザーの注文一覧を作成日時の降順で取得する。
   *
   * <p>最新の注文が先頭に並ぶようソートされており、マイページの注文履歴表示に適している。</p>
   *
   * @param userId 検索対象のユーザーID
   * @return 降順に並べられた注文リスト
   */
  List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
}
