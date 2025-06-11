package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Country;

/**
 * 国テーブルのリポジトリインターフェース
 * <p>
 * countriesテーブルに対するデータ操作を行うためのインターフェース。
 * </p>
 */
public interface CountryRepository extends JpaRepository<Country, Long> {
}
