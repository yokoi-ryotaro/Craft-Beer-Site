package com.example.demo.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.entity.User;

import lombok.Getter;

/**
 * アプリケーション独自のUserDetails実装クラス。
 *
 * <p>Spring Securityの認証処理に使用され、データベースから取得されたUserエンティティをもとに、
 * ロール・資格情報・アカウント状態などのセキュリティ情報を提供する。</p>
 *
 * <p>LoginUserはSecurityContextに保持され、ログイン済みユーザー情報として利用される。</p>
 */
@Getter
public class LoginUser implements UserDetails{
	
	private final User user;
	
	public LoginUser(User user) {
		this.user = user;
	}
	
	/**
	 * ユーザーのロールに基づいてGrantedAuthorityを返す。
	 *
	 * <p>ロールは "ROLE_" プレフィックス付きで大文字に変換される。</p>
	 *
	 * @return ユーザーに付与された権限の一覧
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()));
	}
	
	/**
	 * 認証に使用されるユーザー名（メールアドレス）を返す。
	 *
	 * @return ユーザーのメールアドレス
	 */
	@Override
	public String getUsername() {
		return user.getEmail();
	}
	
	/**
	 * 認証に使用されるパスワード（ハッシュ済み）を返す。
	 *
	 * @return パスワード（ハッシュ文字列）
	 */
	@Override
	public String getPassword() {
		return user.getPassword();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true; // 必要に応じて切り替え
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true; // 必要に応じて切り替え
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true; // 必要に応じて切り替え
	}
	
	/**
	 * ユーザーが有効かどうかを返す。
	 *
	 * <p>退会済みの場合は無効（false）として扱う。</p>
	 *
	 * @return アカウントが有効ならtrue、無効ならfalse
	 */
	@Override
	public boolean isEnabled() {
		return !user.getIsDeleted(); // 退会済みかどうかで制御
	}
}
