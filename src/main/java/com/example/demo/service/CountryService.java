package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Country;
import com.example.demo.repository.CountryRepository;

import lombok.RequiredArgsConstructor;

/**
 * 国情報の取得を行うサービスクラス。
 * <p>
 * 国情報の取得処理を提供する。
 * </p>
 */
@Service
@RequiredArgsConstructor
public class CountryService {
	
	/** 商品テーブルのリポジトリ */
	private final CountryRepository countryRepository;
	
	/**
	 * 国名の一覧を取得する。
	 * @return 国名リスト
	 */
	public List<Country> getAllCountries() {
		return countryRepository.findAll();
	}
}
