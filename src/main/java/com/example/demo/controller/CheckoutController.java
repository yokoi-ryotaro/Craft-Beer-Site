package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.CheckoutForm;
import com.example.demo.dto.SessionCartItem;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.CheckoutTemp;
import com.example.demo.service.CartService;
import com.example.demo.service.CheckoutTempService;
import com.example.demo.service.DbCartService;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import com.example.demo.util.CartUtils;

import lombok.RequiredArgsConstructor;

/**
 * 購入手続きおよび注文確定処理を扱うコントローラー。
 *
 * <p>カート内容の取得・表示、支払い合計の計算、フォーム入力の受け取り、注文登録、カートクリアまで
 * 購入フロー全体のロジックを担当する。</p>
 *
 * <p>ログイン状態に応じて、セッションベースまたはDBベースのカート情報を切り替える仕組みを備えている。</p>
 * <p>現在はログインしないと購入できない設計になっている。</p>
 *
 * <p>画面遷移は「購入画面 → 確認画面 → 完了画面」の3ステップで構成され、ユーザー体験の一貫性を保つ設計となっている。</p>
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class CheckoutController {

	private final CartService cartService;
	private final DbCartService dbCartService;
	private final OrderService orderService;
	private final UserService userService;
	private final CheckoutTempService checkoutTempService;

	/**
	 * 購入画面を表示する。
	 *
	 * <p>ログイン状態に応じてカート内容を取得し、支払い金額と送料を計算して画面に渡す。</p>
	 *
	 * <p>フォーム入力は空のまま初期表示され、DBまたはセッションベースのカート構成が反映される。</p>
	 *
	 * @param model ビューへ渡すモデル
	 * @param session セッション情報（非ログイン時のカート保持）
	 * @param form 購入情報入力フォーム
	 * @param principal ログインユーザー情報
	 * @return 購入画面のテンプレート名
	 */
	@GetMapping
	public String showCheckout(Model model, HttpSession session, CheckoutForm form, Principal principal) {
		int totalPrice, shippingFee, paymentTotal;
		
		if (principal != null) {
			Long userId = userService.getUserIdByEmail(principal.getName());
			List<CartItem> cartItems = dbCartService.getCartItems(userId);
			totalPrice = dbCartService.calculateTotalPrice(cartItems);
			model.addAttribute("cartItems", cartItems);
			model.addAttribute("user", userService.getUserByEmail(principal.getName()));
		} else {
			List<SessionCartItem> sessionCartItems = cartService.getCart(session);
			totalPrice = cartService.calculateTotalPrice(sessionCartItems);
			model.addAttribute("cartItems", sessionCartItems);
		}
		shippingFee = CartUtils.calculateShippingFee(totalPrice);
		paymentTotal = totalPrice + shippingFee;
		
		model.addAttribute("title", "ご購入手続き");
		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("shippingFee", shippingFee);
		model.addAttribute("paymentTotal", paymentTotal);
		model.addAttribute("checkoutForm", form);
		
		return "checkout";
	}
	
	/**
	 * 通常購入リクエスト（POST）を受け取って購入画面を再表示する。
	 *
	 * <p>GET同様の表示処理を再利用して構成される。</p>
	 *
	 * @param model モデル
	 * @param session セッション情報
	 * @param form フォーム情報
	 * @param principal ログインユーザー情報
	 * @return 購入画面テンプレート名
	 */
	@PostMapping
	public String checkout(Model model, HttpSession session, CheckoutForm form,  Principal principal) {
		return showCheckout(model, session, form, principal);
	}
	
	/**
	 * 購入内容の確認画面を表示する。
	 *
	 * <p>基本ロジックは購入画面と同様だが、テンプレートは「checkout/confirm」に切り替えて表示される。</p>
	 *
	 * @param model モデル
	 * @param session セッション情報
	 * @param form フォーム情報
	 * @param principal ログインユーザー情報
	 * @return 購入確認画面テンプレート名
	 */
	@PostMapping("/confirm")
	public String confirmCheckout(Model model, HttpSession session, CheckoutForm form,  Principal principal) {
		return showCheckout(model, session, form, principal).replace("checkout", "checkout/confirm");
	}
	
	 /**
   * 決済成功後に呼び出される注文完了画面を表示する。
   *
   * <p>Stripe の successUrl に設定されており、決済が正常に完了した場合に遷移する。</p>
   * <p>一時保存していた購入情報とカート内容を基に注文をDBに登録し、カートや一時データをクリアする。</p>
   *
   * <p>直接アクセスやセッション切れの場合はエラーページへ遷移する。</p>
   *
   * @param principal ログインユーザー情報
   * @param model ビューへ渡すモデル
   * @return 注文完了画面のテンプレート名、またはエラーページ
   */
	@GetMapping("/complete")
	public String completeCheckout(Principal principal, Model model) {
		if (principal == null) {
			return "redirect:/login";
		}
		
		Long userId = userService.getUserIdByEmail(principal.getName());
		Long cartId = dbCartService.getCartIdByUserId(userId);
		CheckoutTemp temp = checkoutTempService.getByUserId(userId);
		
		if (temp == null) {
			return "error";
		}
		
		List<CartItem> cartItems = dbCartService.getCartItems(userId);
		
		orderService.createOrder(userId, temp, cartItems);
		
		dbCartService.clearCart(cartId);
		checkoutTempService.deleteByUserId(userId);
		
		model.addAttribute("title", "注文完了");
		return "checkout/complete";
	}
	
	 /**
   * 決済キャンセル時に呼び出される購入画面を再表示する。
   *
   * <p>Stripe の cancelUrl に設定されており、ユーザーが決済をキャンセルした場合に遷移する。</p>
   * <p>再度入力や確認が行えるよう、通常の購入画面表示処理を再利用して構成される。</p>
   *
   * @param model ビューへ渡すモデル
   * @param session セッション情報
   * @param form フォーム情報
   * @param principal ログインユーザー情報
   * @return 購入画面テンプレート名
   */
	@GetMapping("/cancel")
	public String cancelCheckout(Model model, HttpSession session, CheckoutForm form, Principal principal) {
		return showCheckout(model, session, form, principal);
	}
}
