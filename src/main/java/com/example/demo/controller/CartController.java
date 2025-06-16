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

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
	
	private final CartService cartService;
	private final DbCartService dbCartService;
  private final UserService userService;
  private final ItemRepository itemRepository;
	
	@GetMapping
	public String showCart(HttpSession session, Principal principal, Model model) {
		int total, shippingFee;
		
		if (principal != null) {
			Long userId = userService.getUserIdByEmail(principal.getName());
			List<CartItem> cartItems = dbCartService.getCartItems(userId);
			
			for (CartItem cartItem : cartItems) {
				Item item = itemRepository.findById(cartItem.getItemId())
	            .orElseThrow(() -> new RuntimeException("Item not found"));
				cartItem.setName(item.getName());
				cartItem.setNameEnglish(item.getNameEnglish());
				cartItem.setImagePath(item.getImage());
				cartItem.setPriceWithTax((int)Math.floor(item.getPrice() * 1.1));
			}
			
			total = dbCartService.calculateTotalPrice(cartItems);
			shippingFee = dbCartService.calculateShippingFee(total);
			model.addAttribute("cartItems", cartItems);
		} else {
			List<SessionCartItem> sessionCartItems = cartService.getCart(session);
			total = cartService.calculateTotalPrice(sessionCartItems);
			shippingFee = cartService.calculateShippingFee(total);
			model.addAttribute("cartItems", sessionCartItems);
		}
		
		model.addAttribute("title", "ショッピングカート");
		model.addAttribute("totalPrice", total);
		model.addAttribute("shippingFee", shippingFee);
		return "cart";
	}
	
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
