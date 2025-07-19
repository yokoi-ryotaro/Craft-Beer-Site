package com.example.demo.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.SignupForm;
import com.example.demo.service.SignupService;

import lombok.RequiredArgsConstructor;

/**
 * ユーザー登録画面の表示と登録処理を担当するコントローラークラス。
 * 
 * <p>登録フォームを表示し、ユーザー入力を検証したうえで新規ユーザーの作成処理を行う。
 * 登録完了後にはログイン画面へリダイレクトされる。</p>
 */
@Controller
@RequiredArgsConstructor
public class SignupController {
	
	private final SignupService signupService;

	/**
	 * ユーザー登録フォーム画面を表示する。
	 *
	 * <p>フォームモデルとページタイトルをModelに渡し、"signup" テンプレートをレンダリングする。</p>
	 *
	 * @param model 画面に渡すデータ保持モデル
	 * @param signform 登録フォームモデル（初期化済み）
	 * @return サインアップ画面のテンプレート名（"signup"）
	 */
	@GetMapping("/signup")
	public String showSingupForm(Model model, SignupForm signform) {
		model.addAttribute("title", "ユーザー登録");
		return "signup";
	}
	
	/**
	 * ユーザー登録フォームの送信処理。
	 *
	 * <p>入力値のバリデーションを実行し、エラーがあれば再描画。正常ならSignupServiceを通じてユーザー登録を行う。</p>
	 * <p>登録完了後はログイン画面へリダイレクトされ、完了メッセージがFlash属性で渡される。</p>
	 *
	 * @param model 画面に渡すデータ保持モデル
	 * @param signupForm 入力された登録フォーム情報
	 * @param bdResult バリデーション結果
	 * @param redirectAttributes リダイレクト時に一時メッセージを渡す属性コンテナ
	 * @return ログイン画面へのリダイレクト（または再描画）
	 */
	@PostMapping("/signup")
	public String processSignup(Model model, @Valid @ModelAttribute SignupForm signupForm, BindingResult bdResult, RedirectAttributes redirectAttributes) {
		if (bdResult.hasErrors()) {
			return showSingupForm(model, signupForm);
		}
		signupService.signup(signupForm);
		redirectAttributes.addFlashAttribute("successMessage", "ユーザー登録が完了しました。ログインしてください。");
		return "redirect:/login";
	}
}
