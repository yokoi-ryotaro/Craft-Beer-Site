package com.example.demo.dto;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

/**
 * パスワード変更画面における入力フォームモデル。
 *
 * <p>現在のパスワード・新しいパスワード・確認用パスワードの3項目を保持し、
 * フォーム送信時のバリデーションおよび照合処理のために使用される。</p>
 *
 * <p>このDTOは ChangePasswordController にバインドされ、
 * ユーザー情報の照合と更新処理に連携する。</p>
 */
@Data
public class ChangePasswordForm {
	
	/** 現在のパスワード */
	private String currentPassword;
	
	/** 新しいパスワード */
	@Length(min = 8, max = 20, message = "パスワードは8〜20文字で入力してください")
	private String newPassword;
	
	/** 新しいパスワード（確認用） */
	@Length(min = 8, max = 20, message = "正しく入力してください")
	private String confirmPassword;
	
}
