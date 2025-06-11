package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Item;
import com.example.demo.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

/**
 * 商品情報の取得を行うサービスクラス
 * <p>
 * 新着商品や人気商品の取得処理を提供する。
 * </p>
 */
@Service
@RequiredArgsConstructor
public class ItemService {
	
	/** 商品テーブルのリポジトリ */
	private final ItemRepository itemRepository;
	
	/** 税率10% */
	private double taxRate = 1.10;
	
	/**
	 * 新着商品を取得する
	 * <p>
	 * 作成日時が新しい順に最大10件のデータを取得する。
	 * </p>
	 * @return 新着商品のリスト（最大10件）
	 */
	public List<Item> getNewItems(){
		return itemRepository.findTop10ByOrderByCreatedAtDesc();
	}
	
	/**
	 * 人気商品を取得する
	 * <p>
	 * 売上数が多い順に最大10件のデータを取得する。
	 * </p>
	 * @return 人気商品のリスト（最大10件）
	 */
	public List<Item> getBestSellers(){
		return itemRepository.findTop10ByOrderBySalesCountDesc();
	}
	
	/**
	 * 商品名（英語）から商品情報を取得する
	 * @param nameEnglish 商品名（英語）
	 * @return 該当する商品情報
	 */
	public Optional<Item> getItemByNameEnglish(String nameEnglish) {
		return itemRepository.findByNameEnglish(nameEnglish);
	}
	
	/**
	 * 税込価格を計算する
	 * @param price 商品の税抜価格
	 * @return 税込価格
	 */
	public int calculatePriceWithTax(int price) {
		return (int) Math.floor(price * taxRate);
	}
	
	/**
	 * 商品IDから商品情報を取得する
	 * @param id 商品ID
	 * @return 該当する商品情報（存在しない場合は空）
	 */
	public Optional<Item> findById(Long id) {
		return itemRepository.findById(id);
	}
	
	/**
	 * 検索条件に基づいて商品情報をページ単位で取得する
	 * <p>
	 * キーワード、価格帯、原産国、アルコール分、容量、在庫状況を指定して商品情報を検索し、
	 * ページングされた結果を返却する。取得した各商品には税込価格を設定する。
	 * </p>
	 * @param keyword 商品名の部分一致キーワード（null可）
	 * @param minPrice 最小価格（null可）
	 * @param maxPrice 最大価格（null可）
	 * @param countryId 原産国ID（null可）
	 * @param minAbv 最小アルコール分（null可）
	 * @param maxAbv 最大アルコール分（null可）
	 * @param minVolume 最小容量（null可）
	 * @param maxVolume 最大容量（null可）
	 * @param inStock 在庫ありで絞り込む場合はtrue（null可）
	 * @param pageable ページングおよびソート情報
	 * @return 条件に一致する商品のページング済みリスト（各商品に税込価格を含む）
	 */
	public Page<Item> searchItems(String keyword, Integer minPrice, Integer maxPrice, Long countryId, 
			Double minAbv, Double maxAbv, Integer minVolume, Integer maxVolume, Boolean inStock, Pageable pageable) {
		Page<Item> items = itemRepository.searchItemsByConditions(keyword, minPrice, maxPrice, countryId, 
				minAbv, maxAbv, minVolume, maxVolume, inStock, pageable);
		items.forEach(item -> item.setPriceWithTax((int) Math.floor(item.getPrice() * taxRate)));
		return items;
	}
}
