package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * アプリケーション内のユーザー情報を表すエンティティ。
 *
 * <p>「users」テーブルとマッピングされ、個人情報、認証情報、住所、アカウント状態などを保持する。
 * 認証・認可のロジック、プロフィール表示、登録・更新処理などで使用される。</p>
 *
 * <p>生成・更新タイムスタンプは自動で設定され、退会フラグ（isDeleted）によって利用可否を判定する。</p>
 */
@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
	
	/** ユーザーID（主キー） */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** 姓（必須） */
	@Column(name = "last_name", nullable = false)
	private String lastName;
	
	/** 名（必須） */
	@Column(name = "first_name", nullable = false)
	private String firstName;
	
	/** 姓（カナ） */
	@Column(name = "last_name_kana")
	private String lastNameKana;
	
	/** 名（カナ） */
	@Column(name = "first_name_kana")
	private String firstNameKana;
	
	/** メールアドレス（ユニーク・認証に使用） */
	@Column(nullable = false, unique = true)
	private String email;
	
	/** パスワード（ハッシュ化済み） */
	@Column(nullable = false, length = 255)
	private String password;
	
	/** 郵便番号 */
	@Column(name = "post_code")
	private String postCode;
	
	/** 都道府県名 */
	private String prefecture;
	
	/** 市区町村 */
	private String city;
	
	/** 町名・番地などの詳細住所 */
	private String street;
	
	/** 建物名・部屋番号など */
	private String building;
	
	/** 電話番号 */
	@Column(name = "phone_number")
	private String phoneNumber;
	
	/** 生年月日 */
	private LocalDate birthday;
	
	/** 退会済みかどうか（trueなら無効ユーザー） */
	@Column(name = "is_deleted")
	private Boolean isDeleted;
	
	/** ユーザーのロール（例: "USER", "ADMIN"） */
	private String role;
	
	 /** 最終ログイン日時 */
	@Column(name = "last_login_at")
	private LocalDateTime lastLoginAt;
}
