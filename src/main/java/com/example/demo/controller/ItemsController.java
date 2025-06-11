package com.example.demo.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Country;
import com.example.demo.entity.Item;
import com.example.demo.service.CountryService;
import com.example.demo.service.ItemService;
import com.example.demo.util.SearchUtils;

import lombok.RequiredArgsConstructor;

/**
 * 商品一覧ページのコントローラークラス
 */
@Controller
@RequiredArgsConstructor
public class ItemsController {
	
	/** 商品情報を取得するサービスクラス */
	private final ItemService itemService;
	
	/** 国情報を取得するサービスクラス */
	 private final CountryService countryService;
	
	 /**
	  * 商品を検索して一覧ページを表示する
	  * <p>
	  * キーワード、価格帯、原産国、アルコール分、容量、在庫状況、並び順などの条件を基に商品情報を検索し、
	  * 検索結果をページングして表示します。また、検索フォームの選択肢として国情報も取得します。
	  * </p>
	  * @param page ページ番号（0始まり）
	  * @param pageSize 1ページあたりの表示件数
	  * @param keyword 商品名の検索キーワード（部分一致）
	  * @param priceRange 価格帯
	  * @param countryId 原産国のID
	  * @param abvRange アルコール分の範囲
	  * @param volumeRange 容量の範囲
	  * @param inStock 在庫あり商品のみを表示する場合は true
	  * @param sort 並び順指定
	  * @param model 表示に必要なデータを格納するモデルオブジェクト
	  * @return 商品一覧ページのテンプレート名
	  */
	@GetMapping("/items/search")
	public String searchItems(
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "12") int pageSize, 
			@RequestParam(required = false) String keyword,
			@RequestParam(name = "price", required = false) String priceRange,
			@RequestParam(name = "country", required = false) Long countryId,
			@RequestParam(name = "abv", required = false) String abvRange,
			@RequestParam(name = "volume", required = false) String volumeRange,
			@RequestParam(required = false) Boolean inStock,
			@RequestParam(required = false) String sort,
			Model model) {
		
		Integer minPrice = SearchUtils.parseRangeMinInt(priceRange);
		Integer maxPrice = SearchUtils.parseRangeMaxInt(priceRange);
		Double minAbv = SearchUtils.parseRangeMinDouble(abvRange);
		Double maxAbv = SearchUtils.parseRangeMaxDouble(abvRange);
		Integer minVolume = SearchUtils.parseRangeMinInt(volumeRange);
		Integer maxVolume = SearchUtils.parseRangeMaxInt(volumeRange);
		Pageable pageable = SearchUtils.createPageable(page, pageSize, sort);
		
		Page<Item> items = itemService.searchItems(keyword, minPrice, maxPrice, countryId, 
				minAbv, maxAbv, minVolume, maxVolume, inStock, pageable);
		
		List<Country> countries = countryService.getAllCountries();
		
		model.addAttribute("items", items.getContent());
		model.addAttribute("title", "商品一覧");
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", items.getTotalPages());
		model.addAttribute("countries", countries);
		model.addAttribute("keyword", keyword);
		model.addAttribute("selectedPriceRange", priceRange);
		model.addAttribute("selectedCountryId", countryId);
		model.addAttribute("selectedAbvRange" , abvRange);
		model.addAttribute("selectedVolumeRange", volumeRange);
		model.addAttribute("inStock", inStock);
		model.addAttribute("sort", sort);
		return "items";
	}
}
