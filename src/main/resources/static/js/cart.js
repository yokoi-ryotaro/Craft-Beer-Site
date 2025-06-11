function decreaseQuantity(button) {
	const form = button.closest('form');
	const input = form.querySelector('.quantity');
	let current = parseInt(input.value);
	if (current > 1) {
		input.value = current - 1;
		/*updateSubtotal(form);*/
		/*updateCartTotal();*/
		form.submit();
	}
}

function increaseQuantity(button) {
	const form = button.closest('form');
	const input = form.querySelector('.quantity');
	let current = parseInt(input.value);
	const max = parseInt(input.max) || 99;
	if (current < max) {
		input.value = current + 1;
		/*updateSubtotal(form);*/
		/*updateCartTotal();*/
		form.submit();
	}
}

// 合計金額を再計算
/* function updateSubtotal(form) {
	const quantity = parseInt(form.querySelector('.quantity').value);
	const itemRow = form.closest('tr');
	const price = parseInt(itemRow.querySelector('.item-price').dataset.price);
	const subtotal = quantity * price;
	const subtotalEl = itemRow.querySelector('.subtotal');

	if (subtotalEl) {
		subtotalEl.innerHTML = `¥${subtotal.toLocaleString()}`;
	}
}
*/

// ページ全体の合計金額を再計算
/* function updateCartTotal() {
	const subtotals = document.querySelectorAll('.subtotal');
	let total = 0;
	subtotals.forEach(span => {
		const value = span.textContent.replace(/[^\d]/g, '');
		total += parseInt(value || 0);
	});
	document.getElementById('cartTotal').textContent = `¥${total.toLocaleString()}`;
}

document.addEventListener("DOMContentLoaded", function () {
	updateCartTotal();
});
*/