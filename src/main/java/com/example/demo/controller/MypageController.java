package com.example.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.security.LoginUser;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MypageController {

	@GetMapping("/mypage")
	public String showMypage(Model model, @AuthenticationPrincipal LoginUser loginUser) {
		String fullName = loginUser.getUser().getLastName() + " " + loginUser.getUser().getFirstName();
    model.addAttribute("fullName", fullName);
		return "mypage";
	}
}
