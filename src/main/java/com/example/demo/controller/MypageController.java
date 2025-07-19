package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.Order;
import com.example.demo.security.LoginUser;
import com.example.demo.service.OrderService;

import lombok.RequiredArgsConstructor;

/**
 * ログインユーザーのマイページ表示を扱うコントローラー。
 *
 * <p>ユーザーの氏名と注文履歴情報を取得し、マイページテンプレートへ表示データとして渡す。</p>
 *
 * <p>注文情報は OrderService を通じて取得され、作成日時の降順で一覧として提供される。</p>
 */
@Controller
@RequiredArgsConstructor
public class MypageController {
	
	@Autowired
	private OrderService orderService;
	
	/**
	 * ログインユーザーのマイページ画面を表示する。
	 *
	 * <p>ログイン中のユーザーから氏名を結合して表示用の名前を生成し、
	 * また該当ユーザーに紐づく注文履歴を取得して画面に渡す。</p>
	 *
	 * @param model ビューに渡すモデル（氏名・注文履歴・ページタイトルなど）
	 * @param loginUser 認証済みユーザー情報（Spring Securityによる）
	 * @return マイページテンプレートの論理名
	 */
	@GetMapping("/mypage")
	public String showMypage(Model model, @AuthenticationPrincipal LoginUser loginUser) {
		String fullName = loginUser.getUser().getLastName() + " " + loginUser.getUser().getFirstName();
		model.addAttribute("fullName", fullName);
		
		Long userId = loginUser.getUser().getId();
		List<Order> orderList = orderService.getOrdersByUserIdByCreatedAtDesc(userId);
		model.addAttribute("orders", orderList);
		
		model.addAttribute("title", "マイページ");
		return "mypage";
	}
}
