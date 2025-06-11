package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Item;
import com.example.demo.model.CartItem;
import com.example.demo.repository.ItemRepository;
import com.example.demo.service.CartService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
	
	private final ItemRepository itemRepository;
	private final CartService cartService;
	
	@GetMapping
	public String showCart(HttpSession session, Model model) {
		List<CartItem> cart = cartService.getCart(session);
		int total = cartService.calculateTotalPrice(cart);
		int shippingFee = cartService.calculateShippingFee(total);
		
		model.addAttribute("title", "ショッピングカート");
		model.addAttribute("cartItems", cart);
		model.addAttribute("totalPrice", total);
		model.addAttribute("shippingFee", shippingFee);
		return "cart";
	}
	
	@PostMapping("/add")
	public String addToCart(@RequestParam Long itemId, @RequestParam Integer quantity, HttpSession session) {
		Item item = itemRepository.findById(itemId).orElseThrow();
		cartService.addToCart(session, item, quantity);
		return "redirect:/cart";
	}
	
	@PostMapping("/delete")
	public String deleteFromCart(@RequestParam Long itemId, HttpSession session) {
		cartService.removeFromCart(session, itemId);
		return "redirect:/cart";
	}
	
	@PostMapping("/update")
	public String updateQuantity(@RequestParam Long itemId, @RequestParam Integer quantity, HttpSession session) {
		cartService.updateQuantity(session, itemId, quantity);
		return "redirect:/cart";
	}
}
