package com.example.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.LoginUser;

import lombok.RequiredArgsConstructor;

/**
 * Spring Securityが使用するカスタムUserDetailsServiceの実装。
 *
 * <p>認証時にメールアドレスでユーザー情報を検索し、
 * アプリケーション独自のUserDetails実装であるLoginUserとして返却する。</p>
 *
 * <p>このサービスはSecurityConfigでAuthenticationManagerに注入され、
 * ログイン処理時に呼び出される。</p>
 */
@Service
@RequiredArgsConstructor
public class LoginUserDetailsService implements UserDetailsService{
	
	private final UserRepository userRepository;
	
	/**
	 * ユーザー名（ここではメールアドレス）に基づいて認証対象ユーザーを検索する。
	 *
	 * <p>検索結果が存在すればLoginUserオブジェクトとして返却し、
	 * 存在しない場合はUsernameNotFoundExceptionをスローする。</p>
	 *
	 * @param email ログイン時に入力されたメールアドレス
	 * @return LoginUser（UserDetailsの実装）
	 * @throws UsernameNotFoundException ユーザーが見つからなかった場合
	 */
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// emailで検索
		User user = userRepository.findByEmail(email)
								.orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません"));
		return new LoginUser(user);
	}
}
