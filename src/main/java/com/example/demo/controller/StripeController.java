package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.dto.CheckoutForm;
import com.example.demo.entity.CartItem;
import com.example.demo.service.CheckoutTempService;
import com.example.demo.service.DbCartService;
import com.example.demo.service.StripeService;
import com.example.demo.service.UserService;
import com.example.demo.util.CartUtils;
import com.stripe.model.checkout.Session;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class StripeController {
	
	private final StripeService stripeService;
	private final DbCartService dbCartService;
	private final UserService userService;
	private final CheckoutTempService checkoutTempService;
	
	/**
	 * Stripe Checkout セッションを作成し、決済ページへリダイレクトする。
	 *
	 * <p>ユーザーが購入確認画面から「決済する」を押下した際に呼び出される処理。</p>
	 *
	 * <ul>
	 *   <li>入力フォーム情報を一時保存テーブルに登録する。</li>
	 *   <li>ログインユーザーのカート内容を取得し、商品合計と送料を計算する。</li>
	 *   <li>合計金額をもとに Stripe の Checkout セッションを生成する。</li>
	 *   <li>生成されたセッションの URL へリダイレクトし、Stripe の決済画面へ遷移させる。</li>
	 * </ul>
	 *
	 * @param form 購入者入力フォーム（配送先や連絡先情報を含む）
	 * @param principal ログインユーザー情報（メールアドレス経由でユーザーIDを特定）
	 * @return Stripe の決済画面へリダイレクトするための {@link RedirectView}
	 * @throws Exception Stripe API の呼び出しに失敗した場合
	 */
	@PostMapping("/checkout/create-session")
	public RedirectView createCheckoutSession(CheckoutForm form, Principal principal) throws Exception {
		Long userId = userService.getUserIdByEmail(principal.getName());
		
		checkoutTempService.saveOrUpdate(userId, form);
		
		List<CartItem> cartItems = dbCartService.getCartItems(userId);
		int totalPrice = dbCartService.calculateTotalPrice(cartItems);
		int shippingFee = CartUtils.calculateShippingFee(totalPrice);
		long paymentTotal = totalPrice + shippingFee;
		
		Session stripeSession = stripeService.createCheckoutSession(paymentTotal, userId);
		return new RedirectView(stripeSession.getUrl());
	}
}
