package com.nrifintech.cms.payments.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.Charge;

@Service
public class StripeService {

	@Value("${stripe.key.secret}")
	private String API_SECRET_KEY;

	public StripeService() {

	}

	public String createCharge(String email, String token, int amount) {

		String chargeId = null;

		try {
			Stripe.apiKey = API_SECRET_KEY;

			Map<String, Object> chargeParams = new HashMap<>();
			chargeParams.put("description", "Charge for " + email);
			chargeParams.put("currency", "inr");
			chargeParams.put("amount", amount * 100);
			chargeParams.put("source", token);

			Charge charge = Charge.create(chargeParams);

			chargeId = charge.getId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return chargeId;
	}

}
