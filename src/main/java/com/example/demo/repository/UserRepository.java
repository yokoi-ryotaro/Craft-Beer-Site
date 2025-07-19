package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.User;

/**
 * ユーザー情報（Userエンティティ）の永続化および検索操作を提供するリポジトリインタフェース。
 *
 * <p>主にメールアドレスをキーにした検索クエリをサポートし、認証・セッション管理・マイページ表示などに使用される。</p>
 *
 * <p>Spring Data JPA により標準的な CRUD 操作が継承されており、
 * findByEmail によってログイン処理やユーザー識別などの用途をカバーしている。</p>
 */
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * 指定されたメールアドレスに一致するユーザー情報を取得する。
	 *
	 * <p>Spring Security の認証処理や、ユーザー更新・識別に使用される。</p>
	 *
	 * @param email 検索対象のメールアドレス
	 * @return 該当するユーザー（Optional）※存在しない場合は空
	 */
	Optional<User> findByEmail(String email);
}
