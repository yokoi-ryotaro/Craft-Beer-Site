package com.example.demo.dto;

import com.example.demo.util.PricingUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * セッションベースのカートに格納される商品情報のDTOクラス。
 *
 * <p>ログイン前のユーザーが商品をカートに追加した際に、
 * 一時的にセッションスコープで保持するための構造。</p>
 *
 * <p>商品ID、名称、数量、税抜価格、税込価格、画像パス、英語名などを保持し、
 * データの受け渡しや表示処理に使用される。</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionCartItem {
	
	/** 商品ID */
	private Long itemId;
	
	/** 商品名 */
	private String name;
	
	/** 商品名（英語） */
	private String nameEnglish;
	
	/** 数量 */
	private int quantity;
	
	/** 税抜価格（単位：円） */
	private int price;
	
	/** 商品画像のファイル名 */
	private String image;
	
	/**
	 * 商品の税込価格を取得する。
	 *
	 * @return 税込価格（円）
	 */
	public int getPriceWithTax() {
		return PricingUtils.calculatePriceWithTax(this.price);
	}
}
