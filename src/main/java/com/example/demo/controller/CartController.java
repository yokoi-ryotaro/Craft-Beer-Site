package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.SessionCartItem;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Item;
import com.example.demo.repository.ItemRepository;
import com.example.demo.service.CartService;
import com.example.demo.service.DbCartService;
import com.example.demo.service.UserService;
import com.example.demo.util.CartUtils;

import lombok.RequiredArgsConstructor;

/**
 * ショッピングカート関連の操作を提供するコントローラークラス。
 *
 * <p>ログイン状態に応じて、セッションベースの仮カートとDB永続カートを使い分け、
 * 商品の表示・追加・更新・削除処理を適切にルーティングする。</p>
 *
 * <p>ユーザー未認証時にはセッションスコープのCartServiceを、
 * ログイン済みユーザーには永続化されたDbCartServiceを使用する。</p>
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
	
	/** ログイン前のカート情報を取得するサービスクラス */
	private final CartService cartService;
	
	/** DBに保存されたユーザーカート情報を取得するサービスクラス */
	private final DbCartService dbCartService;
	
	/** ユーザー情報を取得するサービスクラス */
	private final UserService userService;
	
	/** 商品テーブルのリポジトリ */
	private final ItemRepository itemRepository;
	
  /**
   * カート画面の表示処理をする。
   *
   * <p>ログイン状態に応じて、DBカートまたはセッションカートから商品情報を取得し、
   * 合計金額と送料を算出してViewに渡す。</p>
   *
   * @param session 現在のHTTPセッション
   * @param principal ログインユーザー情報（nullの場合は未認証）
   * @param model 画面へ渡すデータの保持モデル
   * @return カート画面のテンプレート名（"cart"）
   */
	@GetMapping
	public String showCart(HttpSession session, Principal principal, Model model) {
		int totalPrice, shippingFee;
		
		if (principal != null) {
			Long userId = userService.getUserIdByEmail(principal.getName());
			List<CartItem> cartItems = dbCartService.getCartItems(userId);
			totalPrice = dbCartService.calculateTotalPrice(cartItems);
			model.addAttribute("cartItems", cartItems);
		} else {
			List<SessionCartItem> sessionCartItems = cartService.getCart(session);
			totalPrice = cartService.calculateTotalPrice(sessionCartItems);
			model.addAttribute("cartItems", sessionCartItems);
		}
		shippingFee = CartUtils.calculateShippingFee(totalPrice);
		
		model.addAttribute("title", "ショッピングカート");
		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("shippingFee", shippingFee);
		return "cart";
	}
	
	/**
	 * 商品をカートに追加する。
	 *
	 * <p>ログイン状態に応じて、DBカートまたはセッションカートに追加処理を行う。</p>
	 *
	 * @param itemId 追加対象商品のID
	 * @param quantity 追加する数量
	 * @param session 現在のHTTPセッション
	 * @param principal ログインユーザー情報（nullの場合は未認証）
	 * @return カート画面へのリダイレクト
	 */
	@PostMapping("/add")
	public String addToCart(@RequestParam Long itemId, @RequestParam Integer quantity, HttpSession session, Principal principal) {
		Item item = itemRepository.findById(itemId).orElseThrow();
		if (principal != null) {
			Long userId = userService.getUserIdByEmail(principal.getName());
			dbCartService.addToCart(userId, item, quantity);
		} else {
			cartService.addToCart(session, item, quantity);
		}
		return "redirect:/cart";
	}
	
	/**
	 * カート内の商品数量を更新する。
	 *
	 * <p>ログイン状態に応じて、対象商品の数量を上書きする。</p>
	 *
	 * @param itemId 更新対象商品のID
	 * @param quantity 設定する新しい数量
	 * @param session 現在のHTTPセッション
	 * @param principal ログインユーザー情報（nullの場合は未認証）
	 * @return カート画面へのリダイレクト
	 */
	@PostMapping("/update")
	public String updateQuantity(@RequestParam Long itemId, @RequestParam Integer quantity, HttpSession session, Principal principal) {
		if (principal != null) {
			Long userId = userService.getUserIdByEmail(principal.getName());
			dbCartService.updateQuantity(userId, itemId, quantity);
		} else {
			cartService.updateQuantity(session, itemId, quantity);
		}
		return "redirect:/cart";
	}
	
	/**
	 * カート内から指定商品を削除する。
	 *
	 * <p>ログイン状態に応じて、DBカートまたはセッションカートから削除処理を行う。</p>
	 *
	 * @param itemId 削除対象商品のID
	 * @param session 現在のHTTPセッション
	 * @param principal ログインユーザー情報（nullの場合は未認証）
	 * @return カート画面へのリダイレクト
	 */
	@PostMapping("/delete")
	public String deleteFromCart(@RequestParam Long itemId, HttpSession session, Principal principal) {
		 if (principal != null) {
			 Long userId = userService.getUserIdByEmail(principal.getName());
			 dbCartService.removeFromCart(userId, itemId);
		 } else {
			 cartService.removeFromCart(session, itemId);
		 }
		 return "redirect:/cart";
	}
}
