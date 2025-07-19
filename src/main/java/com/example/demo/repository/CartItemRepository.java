package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.CartItem;

/**
 * カート明細（CartItemエンティティ）の検索・削除処理を提供するリポジトリインタフェース。
 *
 * <p>ユーザーのカートに紐づく商品情報の取得や、ログイン時のマージ処理、購入確定時の明細削除などに利用される。</p>
 *
 * <p>Spring Data JPA によって、クエリメソッドが命名規約に従って自動生成される。</p>
 */
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	
	/**
	 * 指定されたカートIDと商品IDに一致する明細情報を取得する。
	 *
	 * <p>ログイン時のカート統合処理などで、重複商品を検出するために利用される。</p>
	 *
	 * @param cartId 検索対象のカートID
	 * @param itemId 検索対象の商品ID
	 * @return 該当する CartItem（Optional）※存在しない場合は空
	 */
	Optional <CartItem> findByCartIdAndItemId (Long cartId, Long itemId);
	
	/**
	 * 指定されたユーザーIDに紐づく全てのカート明細情報を取得する。
	 *
	 * <p>画面表示やマージ処理で、ユーザーが保有する全商品情報の取得に利用される。</p>
	 *
	 * @param userId 検索対象のユーザーID
	 * @return カート内の全商品リスト
	 */
	List <CartItem> findByCart_UserId(Long userId);
	
	/**
	 * 指定されたカートIDと商品IDに一致する明細情報を削除する。
	 *
	 * <p>ユーザーがカートから個別の商品を取り除くときに利用される。</p>
	 *
	 * @param cartId カートID
	 * @param itemId 商品ID
	 */
	void deleteByCartIdAndItemId(Long cartId, Long itemId);
	
	/**
	 * 指定されたカートIDに紐づくすべての明細情報を削除する。
	 *
	 * <p>カート全体の削除、もしくは購入確定時に明細を一括で空にする処理に使用される。</p>
	 *
	 * @param cartId カートID
	 */
	void deleteByCartId(Long cartId);
}
