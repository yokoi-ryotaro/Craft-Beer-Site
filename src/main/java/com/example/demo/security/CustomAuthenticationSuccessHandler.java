package com.example.demo.security;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.dto.SessionCartItem;
import com.example.demo.service.DbCartService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	private final DbCartService dbCartService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,Authentication authentication)
			throws IOException, ServletException {
		// ログインユーザーのIDを取得
		LoginUser loginUser = (LoginUser) authentication.getPrincipal();
		Long userId = loginUser.getUser().getId();
		
		// セッションからカート情報を取得
		HttpSession session = request.getSession(false);
		if (session != null) {
			@SuppressWarnings("unchecked")
			List<SessionCartItem> sessionCart = (List<SessionCartItem>) session.getAttribute("cart");
			if (sessionCart != null && !sessionCart.isEmpty()) {
				// DBにマージ
				dbCartService.mergeSessionCart(userId, sessionCart);
				// セッションのカートをクリア
				session.removeAttribute("cart");
			}
		}
		// ログイン後の遷移先へリダイレクト
		response.sendRedirect("/");
	}
}
