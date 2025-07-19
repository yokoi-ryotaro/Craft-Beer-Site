package com.example.demo.util;

/**
 * 価格・税率関連の計算処理を提供するユーティリティクラス。
 */
public class PricingUtils {

	private PricingUtils() {
		// インスタンス化禁止
	}
	
	/** 消費税率（例：10%） */
	public static final double TAX_RATE = 0.10;
	
	/**
	 * 税込価格を計算する。
	 *
	 * @param price 税抜価格
	 * @return 税込価格（切り捨て整数）
	 */
	public static int calculatePriceWithTax(int price) {
		return (int) Math.floor(price * (1 + TAX_RATE));
	}
}
