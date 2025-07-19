package com.example.demo.controller;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.dto.ChangePasswordForm;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.LoginUser;

import lombok.RequiredArgsConstructor;

/**
 * ログインユーザーによるパスワード変更処理を扱うコントローラー。
 *
 * <p>変更画面の表示と、変更リクエストのバリデーション・更新処理を担う。</p>
 *
 * <p>Spring Security の認証ユーザー情報（LoginUser）を使用して現在のユーザーを識別し、
 * パスワードの照合およびエンコード後の保存処理を行う。</p>
 *
 * <p>不一致や誤入力に対しては BindingResult を用いたエラーフィードバックを提供する。</p>
 */
@Controller
@RequiredArgsConstructor
public class ChangePasswordController {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	/**
	 * パスワード変更画面を表示する。
	 *
	 * <p>画面タイトルを設定し、フォームの初期表示を行う。
	 * ログインユーザー情報は取得するが、表示には使用されない。</p>
	 *
	 * @param model ビューに渡すモデル
	 * @param loginUser 認証済みユーザー情報
	 * @param form フォームオブジェクト（初期化用）
	 * @return パスワード変更画面のテンプレート名
	 */
	@GetMapping("/change-password")
	public String showChangePassword(Model model, @AuthenticationPrincipal LoginUser loginUser, ChangePasswordForm form) {
		model.addAttribute("title", "パスワード変更");
		return "change-password";
	}
	
	/**
	 * パスワード変更リクエストを受け取り、バリデーションと更新処理を行う。
	 *
	 * <p>以下の検証ステップを順に行う：
	 * <ul>
	 *   <li>現在のパスワードが正しいかの照合</li>
	 *   <li>現在と新しいパスワードが異なることの確認</li>
	 *   <li>バリデーションエラーの有無</li>
	 *   <li>新しいパスワードと確認用パスワードの一致チェック</li>
	 * </ul>
	 * </p>
	 *
	 * <p>すべての条件を満たした場合、エンコードした新しいパスワードを保存する。</p>
	 *
	 * @param model ビューに渡すモデル
	 * @param loginUser 認証済みユーザー情報
	 * @param form 入力されたパスワード変更フォーム
	 * @param bdResult バリデーション結果
	 * @return 処理結果後のテンプレート名
	 */
	@PostMapping("/change-password")
	public String changePassword(Model model, @AuthenticationPrincipal LoginUser loginUser, @Valid @ModelAttribute ChangePasswordForm form, BindingResult bdResult) {
		model.addAttribute("title", "パスワード変更");

		String currentPassword = form.getCurrentPassword();
		String newPassword = form.getNewPassword();
		String confirmPassword = form.getConfirmPassword();
		
		// ユーザー情報の取得（DBから最新情報）
		User user = loginUser.getUser();
		
		// 現在のパスワードの照合
		if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
			bdResult.rejectValue("currentPassword", null, "現在のパスワードが正しくありません。");
		return "change-password";
		}
		
		// 現在のパスワードと新しいパスワードの不一致チェック
		if (newPassword.equals(currentPassword)) {
			bdResult.rejectValue("newPassword", null, "現在のパスワードと異なるものにしてください");
		return "change-password";
		}
		
		// バリデーションチェック
		if (bdResult.hasErrors()) {
			return "change-password";
		}
		
		// 新しいパスワードと確認用の一致チェック
		if (!newPassword.equals(confirmPassword)) {
			bdResult.rejectValue("confirmPassword", null, "新しいパスワードが一致しません。");
		return "change-password";
		}
		
		// 新しいパスワードをエンコードして更新
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
		
		model.addAttribute("successMessage", "パスワードを変更しました");
		return "change-password";
		}
	
}
