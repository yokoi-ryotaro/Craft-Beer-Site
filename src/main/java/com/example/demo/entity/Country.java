package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 国情報を保持するエンティティ。
 *
 * <p>「countries」テーブルにマッピングされ、国名・ISOコード・有効フラグを管理する。</p>
 *
 * <p>商品表示で国を選択させる機能に利用される。</p>
 *
 * <p>「有効フラグ」を用いることで、取り扱い停止中の国を画面側から除外することも可能。</p>
 */
@Entity
@Table(name = "countries")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Country {

	/** 国ID（主キー） */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** 国名 */
	private String name;
	
	/** ISOコード */
	@Column(name = "iso_code")
	private String isoCode;
	
	/** 有効フラグ */
	@Column(name = "is_active")
	private boolean isActive;
}
