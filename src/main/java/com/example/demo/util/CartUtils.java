package com.example.demo.util;

/**
 * カート処理に関連するユーティリティクラス。
 *
 * <p>合計金額に対する送料算出など、共通ロジックを提供する。</p>
 */
public class CartUtils {
	
	private CartUtils() {
		// インスタンス化禁止
	}

	/**
	 * 合計金額（税込）に応じた送料を算出する。
	 *
	 * <p>送料ルール：
	 * <ul>
	 *   <li>2,000円未満 → 送料 1,000円</li>
	 *   <li>2,000円～4,999円 → 送料 500円</li>
	 *   <li>5,000円以上 → 送料 無料</li>
	 * </ul>
	 * </p>
	 *
	 * @param totalPrice カート内商品の合計金額（税込）
	 * @return 送料金額（円）
	 */
	public static int calculateShippingFee(int totalPrice) {
		if (totalPrice < 2000) return 1000;
		if (totalPrice < 5000) return 500;
		return 0;
	}
}
