package com.example.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.SignupForm;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * ユーザー登録処理を担当するサービスクラス。
 *
 * <p>SignupFormからUserエンティティを構築し、パスワードのハッシュ化と初期ロールの設定を行ったうえで
 * UserRepositoryを通じてデータベースへ保存する。</p>
 */
@Service
@RequiredArgsConstructor
public class SignupService {
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	/**
	 * ユーザー登録フォームの入力値を元に、Userエンティティを作成して保存する。
	 *
	 * <p>パスワードはPasswordEncoderを使用してハッシュ化される。
	 * ロールは "user"、退会フラグは false で初期化される。</p>
	 *
	 * @param form 登録フォーム情報（バリデーション済み）
	 */
	public void signup(SignupForm form) {
		User user = User.builder()
			.lastName(form.getLastName())
			.firstName(form.getFirstName())
			.lastNameKana(form.getLastNameKana())
			.firstNameKana(form.getFirstNameKana())
			.email(form.getEmail())
			.password(passwordEncoder.encode(form.getPassword()))
			.postCode(form.getPostCode())
			.prefecture(form.getPrefecture())
			.city(form.getCity())
			.street(form.getStreet())
			.building(form.getBuilding())
			.phoneNumber(form.getPhoneNumber())
			.birthday(form.getBirthday())
			.isDeleted(false)
			.role("user")
			.build();
		userRepository.save(user);
	}
}
