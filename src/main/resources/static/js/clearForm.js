document.getElementById('clearButton').addEventListener('click', function () {
	const form = document.getElementById('searchForm');
	
	// テキストボックス
	form.querySelector('input[name="keyword"]').value = '';

	// セレクトボックス（全部）
	form.querySelectorAll('select').forEach(select => {
		select.selectedIndex = 0; // 最初のoptionを選択
	});

	// チェックボックス（在庫あり）
	form.querySelectorAll('input[type="checkbox"]').forEach(checkbox => {
		checkbox.checked = false;
	});
});