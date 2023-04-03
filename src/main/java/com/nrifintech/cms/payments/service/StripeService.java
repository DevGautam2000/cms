package com.nrifintech.cms.payments.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

// A service class which is used to create a charge.
@Service
public class StripeService {

	@Value("${stripe.key.secret}")
	private String API_SECRET_KEY;

	// This is the default constructor.
	public StripeService() {

	}

	/**
	 * It creates a charge using the Stripe API
	 * 
	 * @param email The email address of the customer.
	 * @param token The token returned by Stripe.js.
	 * @param amount The amount to be charged, in the smallest currency unit. For example, if you want to
	 * charge .50, this would be 1050.
	 * @return The chargeId is being returned.
	 */
	public String createCharge(String email, String token, int amount) throws StripeException {

		String chargeId = null;

	
			Stripe.apiKey = API_SECRET_KEY;

			Map<String, Object> chargeParams = new HashMap<>();
			chargeParams.put("description", "Charge for " + email);
			chargeParams.put("currency", "inr");
			chargeParams.put("amount", amount * 100);
			chargeParams.put("source", token);

			Charge charge = Charge.create(chargeParams);

			chargeId = charge.getId();
		
		return chargeId;
	}

}
