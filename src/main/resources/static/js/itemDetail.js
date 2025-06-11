// 商品個数を減らす関数
function decreaseQuantity() {
	let quantityInput = document.getElementById('quantity');
	let currentQuantity = parseInt(quantityInput.value);
	if (currentQuantity > 1) {
		quantityInput.value = currentQuantity - 1;
		document.getElementById('cartQuantity').value = quantityInput.value;
	}
}

// 商品個数を増やす関数
function increaseQuantity() {
	let quantityInput = document.getElementById('quantity');
	let currentQuantity = parseInt(quantityInput.value);
	let maxQuantity = parseInt(quantityInput.max);
	if (currentQuantity < maxQuantity) {
		quantityInput.value = currentQuantity + 1;
		document.getElementById('cartQuantity').value = quantityInput.value;
	}
}