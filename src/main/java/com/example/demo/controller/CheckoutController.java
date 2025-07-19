package com.example.demo.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.CheckoutForm;
import com.example.demo.dto.SessionCartItem;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Item;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.repository.ItemRepository;
import com.example.demo.service.CartService;
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
	private final ItemRepository itemRepository;
	
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
	public String showCheckout(Model model, HttpSession session, CheckoutForm form,  Principal principal) {
		int totalPrice, shippingFee;
		
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
		
		model.addAttribute("title", "ご購入手続き");
		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("shippingFee", shippingFee);
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
	 * 注文内容を確定し、注文情報・注文明細を保存する。
	 *
	 * <p>ログインユーザーの場合はUserID・CartIDを取得してDBカートを処理し、
	 * 非ログインユーザーはセッションベースのカート情報を使用して注文登録を行う。</p>
	 *
	 * <p>注文登録後はカートをクリアし、注文完了画面へ遷移する。</p>
	 *
	 * @param model モデル
	 * @param session セッション情報
	 * @param form フォーム情報
	 * @param principal ログインユーザー情報
	 * @return 注文完了画面テンプレート名
	 */
	@PostMapping("/complete")
	public String completeOrder(Model model, HttpSession session, CheckoutForm form, Principal principal) {
		List<OrderItem> orderItems;
		int totalPrice, shippingFee, paymentTotal;
		
		Order order = new Order();
		order.setLastName(form.getLastName());
		order.setFirstName(form.getFirstName());
		order.setEmail(form.getEmail());
		order.setPostCode(form.getPostCode());
		order.setPrefecture(form.getPrefecture());
		order.setCity(form.getCity());
		order.setStreet(form.getStreet());
		order.setBuilding(form.getBuilding());
		order.setPhoneNumber(form.getPhoneNumber());
		order.setPaymentMethod(form.getPaymentMethod());
		
		if (principal != null) {
			Long userId = userService.getUserIdByEmail(principal.getName());
			Long cartId = dbCartService.getCartIdByUserId(userId);
			order.setUserId(userId);
			List<CartItem> dbItems = dbCartService.getCartItems(userId);
			for (CartItem cartItem : dbItems) {
				Item item = itemRepository.findById(cartItem.getItemId()).orElseThrow();
				cartItem.setPriceWithTax((int) Math.floor(item.getPrice() * 1.1));
			}
			totalPrice = dbCartService.calculateTotalPrice(dbItems);
			shippingFee = CartUtils.calculateShippingFee(totalPrice);
			paymentTotal = totalPrice + shippingFee;
			order.setTotalPrice(totalPrice);
			order.setShippingFee(shippingFee);
			order.setPaymentTotal(paymentTotal);
			
			orderItems = dbItems.stream()
				.map(ci -> new OrderItem(null, null, ci.getItemId(), ci.getQuantity(), ci.getPriceWithTax(), null))
				.collect(Collectors.toList());
			
			orderService.saveOrderWithItems(order, orderItems);
			dbCartService.clearCart(cartId);
		} else {
			List<SessionCartItem> sessionItems = cartService.getCart(session);
			totalPrice = cartService.calculateTotalPrice(sessionItems);
			shippingFee = CartUtils.calculateShippingFee(totalPrice);
			paymentTotal = totalPrice + shippingFee;
			order.setTotalPrice(totalPrice);
			order.setShippingFee(shippingFee);
			order.setPaymentTotal(paymentTotal);
			
			orderItems = sessionItems.stream()
				.map(ci -> new OrderItem(null, null, ci.getItemId(), ci.getQuantity(), ci.getPriceWithTax(), null))
				.collect(Collectors.toList());
			
			orderService.saveOrderWithItems(order, orderItems);
			cartService.clearCart(session);
		}
		
		model.addAttribute("title", "注文完了");
		return "checkout/complete";
	}
}
