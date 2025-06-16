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

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class CheckoutController {

	private final CartService cartService;
	private final DbCartService dbCartService;
	private final OrderService orderService;
	private final UserService userService;
	private final ItemRepository itemRepository;
	
	@GetMapping
	public String showCheckout(Model model, HttpSession session, CheckoutForm form,  Principal principal) {
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
			model.addAttribute("user", userService.getUserByEmail(principal.getName()));
		} else {
			List<SessionCartItem> sessionCartItems = cartService.getCart(session);
			total = cartService.calculateTotalPrice(sessionCartItems);
			shippingFee = cartService.calculateShippingFee(total);
			model.addAttribute("cartItems", sessionCartItems);
		}
		
		model.addAttribute("title", "ご購入手続き");
		model.addAttribute("totalPrice", total);
		model.addAttribute("shippingFee", shippingFee);
		model.addAttribute("checkoutForm", form);
		
		return "checkout";
	}

	@PostMapping
	public String checkout(Model model, HttpSession session, CheckoutForm form,  Principal principal) {
		return showCheckout(model, session, form, principal);
	}
	
	@PostMapping("/confirm")
	public String confirmCheckout(Model model, HttpSession session, CheckoutForm form,  Principal principal) {
		return showCheckout(model, session, form, principal).replace("checkout", "checkout/confirm");
	}
	
	@PostMapping("/complete")
	public String completeOrder(Model model, HttpSession session, CheckoutForm form, Principal principal) {
		List<OrderItem> orderItems;
		int totalPrice;
		int shippingFee;
		int paymentTotal;
		
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
			shippingFee = dbCartService.calculateShippingFee(totalPrice);
			paymentTotal = totalPrice + shippingFee;
			order.setTotalPrice(totalPrice);
			order.setShippingFee(shippingFee);
			order.setPaymentTotal(paymentTotal);
			
			orderItems = dbItems.stream()
				.map(ci -> new OrderItem(null, null, ci.getItemId(), ci.getQuantity(), ci.getPriceWithTax(), null, null))
				.collect(Collectors.toList());
			
			orderService.saveOrderWithItems(order, orderItems);
			dbCartService.clearCart(cartId);
		} else {
			List<SessionCartItem> sessionItems = cartService.getCart(session);
			totalPrice = cartService.calculateTotalPrice(sessionItems);
			shippingFee = cartService.calculateShippingFee(totalPrice);
			paymentTotal = totalPrice + shippingFee;
			order.setTotalPrice(totalPrice);
			order.setShippingFee(shippingFee);
			order.setPaymentTotal(paymentTotal);
			
			orderItems = sessionItems.stream()
				.map(ci -> new OrderItem(null, null, ci.getItemId(), ci.getQuantity(), ci.getPriceWithTax(), null, null))
				.collect(Collectors.toList());
			
			orderService.saveOrderWithItems(order, orderItems);
			cartService.clearCart(session);
		}
		
		model.addAttribute("title", "注文完了");
		return "checkout/complete";
	}
}
