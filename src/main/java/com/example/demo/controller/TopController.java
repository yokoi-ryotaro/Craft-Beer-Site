package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.Item;
import com.example.demo.service.ItemService;

import lombok.RequiredArgsConstructor;

/**
 * トップページのコントローラークラス。
 * 
 * <p>クラフトビールECサイトのトップページの初期表示に関する処理を担当する。</p>
 */
@Controller
@RequiredArgsConstructor
public class TopController {
	
	/** 商品情報を取得するサービスクラス */
	private final ItemService itemService;
	
	/**
	 * トップページの初期表示を行う。
	 * 
	 * @param model モデルオブジェクト（画面に表示するデータを保持）
	 * @return トップページのビュー名
	 */
	@GetMapping("/")
	public String view(Model model) {
		List<Item> newItems = itemService.getNewItems();
		List<Item> bestSellers = itemService.getBestSellers();
		model.addAttribute("title", "クラフトビールECサイト");
		model.addAttribute("newItems", newItems);
		model.addAttribute("bestSellers", bestSellers);
		return "top";
	}
}
