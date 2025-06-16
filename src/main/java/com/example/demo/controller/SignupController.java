package com.example.demo.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.SignupForm;
import com.example.demo.service.SignupService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SignupController {
	
	private final SignupService signupService;

	@GetMapping("/signup")
	public String showSingupForm(Model model, SignupForm signform) {
		model.addAttribute("title", "ユーザー登録");
		return "signup";
	}
	
	@PostMapping("/signup")
	public String processSignup(Model model, @Valid @ModelAttribute SignupForm signupForm, BindingResult bdResult, RedirectAttributes redirectAttributes) {
		if (bdResult.hasErrors()) {
			return showSingupForm(model, signupForm);
		}
		signupService.signup(signupForm);
		redirectAttributes.addFlashAttribute("successMessage", "ユーザー登録が完了しました。ログインしてください。");
		return "redirect:/login";
	}
}
