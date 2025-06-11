package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Item;

/**
 * 商品テーブルのリポジトリインターフェース
 * <p>
 * itemsテーブルに対するデータ操作を行うためのインターフェース。
 * </p>
 */
public interface ItemRepository extends JpaRepository<Item, Long> {
	
	/**
	 * 新着商品を10件取得するクエリ
	 * <p>
	 * 作成日時が最新の商品情報を最大10件まで取得する。
	 * </p>
	 * @return 最新の作成日時順の商品情報10件のリスト
	 */
	@Query("SELECT i FROM Item i ORDER BY i.createdAt DESC LIMIT 10")
	List<Item> findTop10ByOrderByCreatedAtDesc();
	
	/**
	 * 人気商品を10件取得するクエリ
	 * <p>
	 * 売上数が多い順に商品情報を最大10件まで取得する。
	 * </p>
	 * @return 売上数上位10件の商品情報のリスト
	 */
	@Query("SELECT i FROM Item i ORDER BY i.salesCount DESC LIMIT 10")
	List<Item> findTop10ByOrderBySalesCountDesc();
	
	/**
	 * 商品名（英語）から商品情報を取得するクエリ
	 * @param nameEnglish 商品名（英語）
	 * @return 該当する商品の情報
	 */
	@Query("SELECT i FROM Item i WHERE i.nameEnglish = :nameEnglish")
	@EntityGraph(attributePaths = "country")
	Optional<Item> findByNameEnglish(String nameEnglish);
	
	/**
	 * 商品検索条件に基づいて商品情報をページ単位で取得するクエリ
	 * <p>
	 * キーワード、価格帯、原産国、アルコール分、容量、在庫状況を指定して商品情報を絞り込み、
	 * ページネーションされた結果を取得する。
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
	 * @return 条件に一致する商品のページング済みリスト
	 */
	@Query(value = """
		SELECT i FROM Item i
		WHERE (:keyword IS NULL OR i.name LIKE %:keyword%)
			AND (:minPrice IS NULL OR i.price >= :minPrice)
			AND (:maxPrice IS NULL OR i.price <= :maxPrice)
			AND (:countryId IS NULL OR i.country.id = :countryId)
			AND (:minAbv IS NULL OR i.abv >= :minAbv)
			AND (:maxAbv IS NULL OR i.abv <= :maxAbv)
			AND (:minVolume IS NULL OR i.volume >= :minVolume)
			AND (:maxVolume IS NULL OR i.volume <= :maxVolume)
			AND (:inStock IS NULL OR i.isSoldOut = false)
		""",
		countQuery = """
		SELECT COUNT(i) FROM Item i
		WHERE (:keyword IS NULL OR i.name LIKE %:keyword%)
			AND (:minPrice IS NULL OR i.price >= :minPrice)
			AND (:maxPrice IS NULL OR i.price <= :maxPrice)
			AND (:countryId IS NULL OR i.country.id = :countryId)
			AND (:minAbv IS NULL OR i.abv >= :minAbv)
			AND (:maxAbv IS NULL OR i.abv <= :maxAbv)
			AND (:minVolume IS NULL OR i.volume >= :minVolume)
			AND (:maxVolume IS NULL OR i.volume <= :maxVolume)
			AND (:inStock IS NULL OR i.isSoldOut = false)
		""")
	Page<Item> searchItemsByConditions(
		@Param("keyword") String keyword,
		@Param("minPrice") Integer minPrice,
		@Param("maxPrice") Integer maxPrice,
		@Param("countryId") Long countryId,
		@Param("minAbv") Double minAbv,
		@Param("maxAbv") Double maxAbv,
		@Param("minVolume") Integer minVolume,
		@Param("maxVolume") Integer maxVolume,
		@Param("inStock") Boolean inStock,
		Pageable pageable
	);
}
