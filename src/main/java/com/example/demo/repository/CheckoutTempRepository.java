package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.CheckoutTemp;

public interface CheckoutTempRepository extends JpaRepository<CheckoutTemp, Long> {
	Optional<CheckoutTemp> findByUserId(Long userId);
	void deleteByUserId(Long userId);
}
