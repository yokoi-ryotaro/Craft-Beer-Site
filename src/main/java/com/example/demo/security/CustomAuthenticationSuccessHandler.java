package com.example.demo.security;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.dto.SessionCartItem;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.DbCartService;

import lombok.RequiredArgsConstructor;

/**
 * ログイン成功後に追加処理を行う Spring Security 用のハンドラー
 * 
 * <p>主に、セッションカート（ログイン前の商品情報）をログインユーザーのDBカートに統合する。</p>
 * <p>また、マージ完了後にセッションカート情報をクリアし、指定ページへリダイレクトする。</p>
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	/** DBに保存されたカート情報を取得するサービスクラス */
	private final DbCartService dbCartService;
	
	private final UserRepository userRepository;

	/**
	 * Spring Security によってログイン認証が成功した際に呼び出されるメソッド。
	 * 
	 * <p>このメソッドでは以下の処理を行う：</p>
	 * <ul>
	 *   <li>認証済みユーザーのIDを取得</li>
	 *   <li>セッションに保存されていた仮カート情報（PRESERVED_CART）を取り出し、DBカートにマージ</li>
	 *   <li>セッションから不要なカート情報を削除</li>
	 *   <li>ログイン後のページへリダイレクト</li>
	 * </ul>
	 * 
	 * @param request 認証成功時のリクエスト
	 * @param response 認証成功時のレスポンス
	 * @param authentication 認証情報（ユーザー情報を含む）
	 * @throws IOException リダイレクト処理中に発生する可能性あり
	 * @throws ServletException サーブレットの処理中に発生する可能性あり
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,Authentication authentication)
			throws IOException, ServletException {
		
		// ログインユーザーのIDを取得
		LoginUser loginUser = (LoginUser) authentication.getPrincipal();
		Long userId = loginUser.getUser().getId();
		
		// 最終ログイン日時を更新
		User user = userRepository.findById(userId)
		    .orElseThrow(() -> new RuntimeException("User not found"));
		user.setLastLoginAt(LocalDateTime.now());
		userRepository.save(user);
		
		// セッションからカート情報を取得
		HttpSession session = request.getSession(false);
		if (session != null) {
			@SuppressWarnings("unchecked")
			List<SessionCartItem> preservedCart = (List<SessionCartItem>) session.getAttribute("PRESERVED_CART");
			
			if (preservedCart != null && !preservedCart.isEmpty()) {
				dbCartService.mergeSessionCart(userId, preservedCart);
				// セッションのカートをクリア
				session.removeAttribute("cart");
				session.removeAttribute("PRESERVED_CART");
			}
		}
		// ログイン後の遷移先へリダイレクト
		response.sendRedirect("/mypage");
	}
}
