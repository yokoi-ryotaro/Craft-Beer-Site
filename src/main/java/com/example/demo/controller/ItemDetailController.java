package com.example.demo.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.entity.Item;
import com.example.demo.service.ItemService;

import lombok.RequiredArgsConstructor;

/**
 * 商品詳細ページのコントローラークラス。
 */
@Controller
@RequiredArgsConstructor
public class ItemDetailController {
	
	/** 商品情報を取得するサービスクラス */
	private final ItemService itemService;
	
	/**
	 * 商品詳細ページを表示するための処理を行う。
	 * <p>
	 * 指定された英語商品名に基づいて商品情報を取得し、取得できた商品情報をビューに渡して、商品詳細ページを表示する。
	 * 英語商品名に対応する商品が見つからない場合は、404エラーページにリダイレクトされる。
	 * </p>
	 * @param nameEnglish 商品の英語名
	 * @param model ビューにデータを渡すためのオブジェクト
	 * @return 商品詳細ページのテンプレート名（itemDetail）またはエラーページ（404）
	 */
	@GetMapping("/item/{nameEnglish}")
	public String showItemDetail(@PathVariable String nameEnglish, Model model) {
		Optional<Item> itemOpt = itemService.getItemByNameEnglish(nameEnglish);
		if (itemOpt.isEmpty()) {
			return "error/404";
		}
		Item item = itemOpt.get();
		int priceWithTax = itemService.calculatePriceWithTax(itemOpt.get().getPrice());
		model.addAttribute("item", item);
		model.addAttribute("priceWithTax", priceWithTax);
		return "itemDetail";
	}
}
