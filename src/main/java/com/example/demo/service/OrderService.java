package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	
	@Transactional
	public void saveOrderWithItems(Order order, List<OrderItem> orderItems) {
		// 注文本体を先に保存（IDを生成するため）
		Order savedOrder = orderRepository.save(order);
		
		// 各OrderItemに保存された注文の参照を設定して保存
		for (OrderItem item : orderItems) {
			item.setOrderId(savedOrder);
			orderItemRepository.save(item);
		}
	}
}
