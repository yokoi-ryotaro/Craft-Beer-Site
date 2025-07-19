package com.example.demo.security;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.dto.SessionCartItem;

/**
 * ログイン処理時にセッションカート情報を一時保存するフィルター。
 * 
 * <p>Spring Securityのログイン時にセッションが再生成される可能性があるため、
 * セッションに保存されていた「cart」情報を「PRESERVED_CART」として一時退避させ、
 * 認証後にカート統合処理で使用可能にする。</p>
 * <p>このフィルターは「/login」へのPOSTリクエストのタイミングでのみ処理される。</p>
 */
@Component
public class CartPreserveFilter extends OncePerRequestFilter {

	/**
	 * フィルターチェーンの中で、ログイン処理へのPOSTリクエスト時のみカート情報を退避する処理を実行する
	 *
	 * <p>セッションに保存されていた「cart」を取り出し、
	 * 同じセッションスコープ内の「PRESERVED_CART」として保存することで、
	 * Spring Securityによるセッション再生成後もカート内容を保持できるようにする。</p>
	 *
	 * @param request 現在のHTTPリクエスト
	 * @param response 現在のHTTPレスポンス
	 * @param filterChain 次のフィルター処理へ進めるチェーン
	 * @throws ServletException フィルター処理中のサーブレット例外
	 * @throws IOException 入出力例外
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		
		// ログイン処理へのPOSTリクエスト時のみ処理
		if ("/login".equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod())) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				@SuppressWarnings("unchecked")
				List<SessionCartItem> cart = (List<SessionCartItem>) session.getAttribute("cart");
				if (cart != null && !cart.isEmpty()) {
				// セッションカートを一時的にリクエスト属性に保存
				session.setAttribute("PRESERVED_CART", cart);
			  }
			}
		}
	// 次のフィルターへ
	filterChain.doFilter(request, response);
	}
}
