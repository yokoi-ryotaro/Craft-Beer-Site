package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class StripeService {
	
	@Value("${stripe.success-url}")
	private String successUrl;
	
	@Value("${stripe.cancel-url}")
	private String cancelUrl;
	
	/**
	 * Stripe Checkout セッションを作成する。
	 *
	 * <p>指定された支払金額とユーザーIDを基に、Stripe の決済セッションを生成する。</p>
	 *
	 * <ul>
	 *   <li>支払方法はクレジットカード（CARD）のみをサポート。</li>
	 *   <li>決済完了時のリダイレクト先 URL（successUrl）と、キャンセル時のリダイレクト先 URL（cancelUrl）を設定する。</li>
	 *   <li>ユーザーIDは {@code clientReferenceId} としてセッションに紐づけられる。</li>
	 *   <li>商品情報は「お支払い」という名称で1件のみ登録され、通貨は JPY、数量は 1 固定。</li>
	 * </ul>
	 *
	 * @param amount 支払金額（単位：最小通貨単位、JPY の場合は「円」）
	 * @param userId 決済を行うユーザーのID（セッションと注文を関連付けるために利用）
	 * @return 生成された Stripe Checkout セッション
	 * @throws Exception Stripe API 呼び出しに失敗した場合
	 */
	public Session createCheckoutSession(long amount, Long userId) throws Exception {
		SessionCreateParams params = 
			SessionCreateParams.builder()
				.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
				.setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl(successUrl)
				.setCancelUrl(cancelUrl)
				.setClientReferenceId(String.valueOf(userId))
				.addLineItem(
					SessionCreateParams.LineItem.builder()
						.setQuantity(1L)
						.setPriceData(
							SessionCreateParams.LineItem.PriceData.builder()
								.setCurrency("jpy")
								.setUnitAmount(amount)
								.setProductData(
									SessionCreateParams.LineItem.PriceData.ProductData.builder()
										.setName("お支払い")
										.build()
								)
							.build()
						)
					.build()
				)
		.build();
		return Session.create(params);
	}
}
