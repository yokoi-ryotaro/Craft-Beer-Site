package com.example.demo.model;

import com.example.demo.entity.Item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
	
	private Item item;
	private Long itemId;
	private String name;
	private int quantity;
	public int getPriceWithTax() {
		return (int)(item.getPrice() * 1.1);
	}
	private String imagePath;
	private String nameEnglish;
}
