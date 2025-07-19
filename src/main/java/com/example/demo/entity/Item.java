package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 商品テーブルのエンティティクラス
 * <p>
 * itemsテーブルのデータと対応し、
 * 商品に関する情報を格納します。
 * </p>
 */
@Entity
@Table(name = "items")
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item extends BaseEntity {

	/** 商品ID（主キー） */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 商品の原産国を表すリレーション
	 * <p>
	 * このフィールドは `Country` エンティティへの多対一 (`@ManyToOne`) 関係を表しています。  
	 * `Item` テーブルの `countries_id` カラムが `Country` テーブルの `id` カラムに対応する
	 * 外部キーとしてマッピングされています。
	 * </p>
	 * <p>
	 * `@JoinColumn` の `name` 属性により、データベース上のカラム名 `countries_id` と
	 * マッピングされています。
	 * </p>
	 * <p>
	 * 注意: `countries_id` は `Item` エンティティ内で `@Column` として
	 * 別にマッピングしないようにしてください。重複マッピングを防ぐためです。
	 * </p>
	 */
	@ManyToOne
  @JoinColumn(name = "countries_id")
  private Country country;
	
	/** 商品名 */
	private String name;
	
	/** 商品名（英語） */
	@Column(name = "name_english")
	private String nameEnglish;
	
	/** 税抜価格（単位：円） */
	private Integer price;
	
	/** 商品説明 */
	@Column(name = "item_details")
	private String itemDetails;
	
	/** 在庫数 */
	private Integer stock;
	
	/** 商品画像のファイル名 */
	private String image;
	
	/** 容量（単位：ミリリットル） */
	private Integer volume;
	
	/** アルコール度数（単位：%） */
	private Double abv;
	
	/** 売上数（販売実績のカウント） */
	@Column(name = "sales_count")
	private Integer salesCount;
	
	/** 売り切れフラグ（true：売り切れ、false：在庫あり） */
	@Column(name = "is_sold_out")
	private boolean isSoldOut;
	
	/**
	 * 税込価格
	 * <p>
	 * 一時的に税込価格を保持するフィールド（DBには存在しない）
	 * </p>
	 */
	@Transient
	private Integer priceWithTax;
}
