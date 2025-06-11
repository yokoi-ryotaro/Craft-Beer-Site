package com.example.demo.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 商品検索に関連するユーティリティクラス。
 */
public class SearchUtils {
	
	/**
	 * 整数の範囲を配列に変換する
	 *
	 * @param range 文字列の範囲
	 * @return [min, max] の Integer 配列（値がない場合は null）
	 */
	public static Integer[] parseIntegerRange(String range) {
		Integer min = null;
		Integer max = null;
		if (range != null && !range.isEmpty()) {
			String[] parts = range.split("-");
			if (!parts[0].isEmpty()) min = Integer.parseInt(parts[0]);
			if (parts.length > 1 && !parts[1].isEmpty()) max = Integer.parseInt(parts[1]);
		}
		return new Integer[] { min, max };
	}
	
	public static Integer parseRangeMinInt(String range) {
		return parseIntegerRange(range)[0];
	}
	
	public static Integer parseRangeMaxInt(String range) {
		return parseIntegerRange(range)[1];
	}
	
	/**
	 * 小数の範囲を配列に変換する
	 *
	 * @param range 文字列の範囲
	 * @return [min, max] の Double 配列（値がない場合は null）
	 */
	public static Double[] parseDoubleRange(String range) {
		Double min = null;
		Double max = null;
		if (range != null && !range.isEmpty()) {
			String[] parts = range.split("-");
			if (!parts[0].isEmpty()) min = Double.parseDouble(parts[0]);
			if (parts.length > 1 && !parts[1].isEmpty()) max = Double.parseDouble(parts[1]);
		}
		return new Double[] { min, max };
	}
	
	public static Double parseRangeMinDouble(String range) {
		return parseDoubleRange(range)[0];
	}
	
	public static Double parseRangeMaxDouble(String range) {
		return parseDoubleRange(range)[1];
	}
	
	/**
	 * ソート指定に基づいて Pageable を作成する。
	 *
	 * @param page ページ番号
	 * @param pageSize 1ページあたりの件数
	 * @param sort ソート指定
	 * @return Pageable オブジェクト
	 */
	public static Pageable createPageable(int page, int pageSize, String sort) {
		if (sort != null && !sort.isEmpty()) {
			String[] sortParams = sort.split("_");
			if (sortParams.length == 2) {
				String field = sortParams[0];
				String direction = sortParams[1];
				if ("salesCount".equals(field)) {
					return PageRequest.of(page, pageSize,
						Sort.by(Sort.Order.desc("salesCount"), Sort.Order.desc("createdAt")));
				}
				Sort sortObj = "asc".equalsIgnoreCase(direction) ?
					Sort.by(field).ascending() :
					Sort.by(field).descending();
				return PageRequest.of(page, pageSize, sortObj);
			}
		}
		// デフォルト：作成日降順
		return PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
	}
}
