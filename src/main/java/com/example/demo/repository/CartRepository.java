package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Cart;

/**
 * ユーザーのカート情報（Cartエンティティ）に関する永続化および検索操作を提供するリポジトリインタフェース。
 *
 * <p>主にユーザーIDをキーにしたカート情報の取得をサポートしており、
 * ログイン時のカート統合処理や、マイページでの表示、購入フローの前段階で使用される。</p>
 *
 * <p>Spring Data JPA の機能により、findByUserId クエリが自動生成される。</p>
 *
 * @author RYOTARO
 */
public interface CartRepository extends JpaRepository<Cart, Long> {
	
	/**
	 * 指定されたユーザーIDに紐づくカート情報を取得する。
	 *
	 * <p>ログイン済みユーザーが保有している商品情報の確認や、カートマージ処理などに利用される。</p>
	 *
	 * @param userId 検索対象のユーザーID
	 * @return 該当するカート情報（Optional）※存在しない場合は空
	 */
	Optional<Cart> findByUserId(Long userId);
}
