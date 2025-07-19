package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.dto.LoginForm;

import lombok.RequiredArgsConstructor;

/**
 * ログイン画面の表示を担当するコントローラークラス。
 *
 * <p>ユーザーが認証を行う際のログインフォームの準備と画面表示処理を提供する。</p>
 * <p>Spring Securityのカスタムログインページと連携し、フォームモデルをビューに渡す役割を持つ。</p>
 */
@Controller
@RequiredArgsConstructor
public class LoginController {

	/**
	 * ログインフォーム画面を表示する。
	 *
	 * <p>ログインフォームの初期状態をModelに渡し、"login" テンプレートをレンダリングする。</p>
	 *
	 * @param model ビューに渡すデータ保持モデル
	 * @param form ログインフォームの入力モデル（初期値設定済み、バインド対象）
	 * @return ログイン画面のテンプレート名（"login"）
	 */
	@GetMapping("/login")
	public String showLoginForm(Model model, LoginForm form) {
		model.addAttribute("title", "ログイン");
		return "login";
	}
}
