package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 画面の初期表示
 */
@Controller
public class TopContreller {
	
	@GetMapping("/")
	public String view(Model model) {
		model.addAttribute("title", "クラフトビールECサイト");
		return "top";
	}
}
