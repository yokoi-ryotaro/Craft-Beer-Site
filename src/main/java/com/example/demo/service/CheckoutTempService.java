package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CheckoutForm;
import com.example.demo.entity.CheckoutTemp;
import com.example.demo.repository.CheckoutTempRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckoutTempService {
	
	private final CheckoutTempRepository repository;
	
	/**
	 * ユーザーの購入情報を一時保存、または更新する。
	 *
	 * <p>同一ユーザーのデータが既に存在する場合は更新し、存在しない場合は新規保存する。</p>
	 * <p>主に Stripe 決済前の購入情報入力画面で利用される。</p>
	 *
	 * @param userId ユーザーID
	 * @param form   購入フォーム入力内容
	 */
	@Transactional
	public void saveOrUpdate(Long userId, CheckoutForm form) {
		CheckoutTemp temp = repository.findByUserId(userId).orElse(new CheckoutTemp());
		temp.setUserId(userId);
		temp.setLastName(form.getLastName());
		temp.setFirstName(form.getFirstName());
		temp.setEmail(form.getEmail());
		temp.setPostCode(form.getPostCode());
		temp.setPrefecture(form.getPrefecture());
		temp.setCity(form.getCity());
		temp.setStreet(form.getStreet());
		temp.setBuilding(form.getBuilding());
		temp.setPhoneNumber(form.getPhoneNumber());
		temp.setPaymentMethod(form.getPaymentMethod());
		
		repository.save(temp);
	}
	
	/**
	 * ユーザーIDに紐づく一時購入情報を取得する。
	 *
	 * <p>存在しない場合は {@code null} を返す。</p>
	 *
	 * @param userId ユーザーID
	 * @return 一時購入情報、存在しない場合は {@code null}
	 */
	public CheckoutTemp getByUserId(Long userId) {
		return repository.findByUserId(userId).orElse(null);
	}
	
	/**
	 * ユーザーIDに紐づく一時購入情報を削除する。
	 *
	 * <p>注文完了後に呼び出され、不要になった一時データをクリアする。</p>
	 *
	 * @param userId ユーザーID
	 */
	@Transactional
	public void deleteByUserId(Long userId) {
		repository.deleteByUserId(userId);
	}
}
