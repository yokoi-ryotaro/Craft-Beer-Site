package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.security.CartPreserveFilter;
import com.example.demo.security.CustomAuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;

/**
 * Spring Security のセキュリティ設定を定義するコンフィグレーションクラス。
 *
 * <p>以下の構成要素を中心に、認証・認可のカスタマイズ設定を行う：</p>
 * <ul>
 *   <li>パスワードのエンコーダー（BCrypt）</li>
 *   <li>DaoAuthenticationProvider の構成</li>
 *   <li>AuthenticationManager の Bean 化</li>
 *   <li>URLごとのアクセス権限設定</li>
 *   <li>ログイン成功時のカスタムハンドラ設定</li>
 *   <li>カート保持フィルターの適用</li>
 * </ul>
 *
 * <p>フォームログインやログアウトURLの明示的な定義に加え、静的ファイルの許可、
 * ログイン必須ページの指定などを通してアプリケーション全体のセキュリティ制御を実現する。</p>
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final UserDetailsService userDetailsService;
	
	private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	
	@Autowired
	private CartPreserveFilter cartPreserveFilter;
	
	/**
	 * パスワードの暗号化に使用するBCryptエンコーダーを返す。
	 *
	 * <p>Spring Securityの認証処理で使用され、パスワードの保存や照合時にハッシュ化される。</p>
	 *
	 * @return BCryptPasswordEncoderインスタンス
	 */
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/**
	 * DaoAuthenticationProviderを構成して返す。
	 *
	 * <p>UserDetailsServiceとPasswordEncoderを注入し、Spring Securityによるユーザー認証を可能にする。</p>
	 *
	 * @return 構成済みのDaoAuthenticationProvider
	 */
	@Bean
	DaoAuthenticationProvider authenticationProvider() {
			DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
			authProvider.setUserDetailsService(userDetailsService);
			authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	/**
	 * 認証マネージャーを取得してBean登録する。
	 *
	 * <p>AuthenticationConfigurationから認証マネージャーを抽出し、フォームログインの認証処理に利用される。</p>
	 *
	 * @param config Spring Security の認証設定
	 * @return 認証マネージャー
	 * @throws Exception 設定取得に失敗した場合
	 */
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	/**
	 * セキュリティフィルタチェーンを構成して返す。
	 *
	 * <p>URLごとの認可ルール、フォームログイン設定、ログアウト処理、フィルター追加など
	 * Spring Securityにおける全体的なアクセス制御を行う。</p>
	 *
	 * <p>静的ファイルは誰でもアクセス可能とし、特定URLに対して認証を必須とする構成が含まれる。
	 * カート保存フィルターとログイン成功時ハンドラもここで適用される。</p>
	 *
	 * @param http HttpSecurityのビルダーオブジェクト
	 * @return 構成済みのSecurityFilterChain
	 * @throws Exception 設定構築に失敗した場合
	 */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(
					"/",
					"/items/**",
					"/signup",
					"/login",
					"/css/**",
					"/img/**",
					"/js/**"
				).permitAll()
				.requestMatchers(
					"/mypage/**",
					"/checkout/**"
				).authenticated()
				.anyRequest().permitAll() // その他も一旦許可（必要に応じて制限可能）
			)
			.addFilterBefore(cartPreserveFilter, UsernamePasswordAuthenticationFilter.class)
			.formLogin(form -> form
				.loginPage("/login")
				.successHandler(customAuthenticationSuccessHandler)
				.loginProcessingUrl("/login") 
				.failureUrl("/login?error")
				.permitAll()
			)
			.logout(logout -> logout
				.logoutSuccessUrl("/login?logout")
				.permitAll()
			)
			.authenticationProvider(authenticationProvider());
		
		return http.build();
	}
}
