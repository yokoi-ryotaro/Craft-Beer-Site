package com.example.demo.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * ユーザー情報を取得するためのサービスクラス。
 *
 * <p>主にメールアドレス（＝ユーザー名）をキーとして、ユーザーIDやUserエンティティを取得する責務を担う。</p>
 *
 * <p>Spring Securityの認証処理やアプリケーション内のユーザー情報参照ロジックで使用される。</p>
 *
 * <p>取得失敗時には UsernameNotFoundException をスローして処理側に通知する。</p>
 */
@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	
	/**
	 * メールアドレスから該当ユーザーのIDを取得する。
	 *
	 * <p>認証済みユーザーの識別や内部照合に使用される。</p>
	 *
	 * @param email 検索対象のメールアドレス
	 * @return ユーザーID（Long型）
	 * @throws UsernameNotFoundException ユーザーが存在しない場合
	 */
	public Long getUserIdByEmail(String email) {
		return userRepository.findByEmail(email)
				.map(User::getId)
				.orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + email));
	}
	
	/**
	 * メールアドレスから該当するUserエンティティを取得する。
	 *
	 * <p>ユーザー詳細の表示や処理ロジックへの連携などに使用される。</p>
	 *
	 * @param email 検索対象のメールアドレス
	 * @return Userエンティティ
	 * @throws UsernameNotFoundException ユーザーが存在しない場合
	 */
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + email));
	}	
}
