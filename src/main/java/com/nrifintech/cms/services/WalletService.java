package com.nrifintech.cms.services;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.entities.Transaction;
import com.nrifintech.cms.entities.Wallet;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.payments.service.StripeService;
import com.nrifintech.cms.repositories.WalletRepo;
import com.nrifintech.cms.types.TransactionType;
import com.nrifintech.cms.utils.Validator;
import com.stripe.exception.StripeException;

/**
 * This class is responsible for all the wallet related operations
 */
@Service
public class WalletService implements Validator {

	public static final Double LIMIT = 100.0;

	@Autowired
	private WalletRepo walletRepo;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private StripeService stripeService;

	/**
	 * This function checks if the current balance is greater than or equal to the minimum limit.
	 * 
	 * @param currentBalance The current balance of the account
	 * @return Boolean
	 */
	private Boolean checkForMinimumLimit(Double currentBalance) {
		return currentBalance >= LIMIT;
	}

	/**
	 * This function checks if the balance of a wallet is greater than the minimum limit.
	 * 
	 * @param w Wallet
	 * @return Boolean
	 */
	public Boolean checkMinimumAmount(Wallet w) {
		return this.checkForMinimumLimit(w.getBalance());
	}

	/**
	 * It saves a wallet to the database
	 * 
	 * @param w Wallet object
	 * @return The wallet object is being returned.
	 */
	public Wallet save(Wallet w) {
		return walletRepo.save(w);
	}

	/**
	 * > It updates the wallet balance and adds a transaction to the wallet's transaction list
	 * 
	 * @param w Wallet object
	 * @param amount The amount to be deducted from the wallet
	 * @return A list of objects.
	 */
	public List<Object> updateWallet(Wallet w, Integer amount) {
		w.setBalance(w.getBalance() - amount);

		Transaction t = transactionService.add(new Transaction(amount, TransactionType.Withdrawl));
		t.setRemarks("ordered food");
		t.setReferenceNumber(UUID.randomUUID().toString());

		if (transactionService.isNotNull(t)) {
			w.getTransactions().add(t);
		}

		return Arrays.asList(w,t);
	}

	/**
	 * It adds money to a wallet, creates a transaction, and then creates a charge
	 * 
	 * @param email the email of the user
	 * @param w Wallet object
	 * @param amount The amount to be charged in cents.
	 * @param token The token returned by Stripe.js
	 * @return The chargeId is being returned.
	 */
	public String addMoneyToWallet(String email, Wallet w, Integer amount, String token) throws StripeException {

		w.setBalance(w.getBalance() + amount);

		Transaction t = transactionService.add(new Transaction(amount, TransactionType.Deposit));
		t.setRemarks("deposit to wallet");
		t.setReferenceNumber(token.split("_")[1]);

		String chargeId = "";

		if (transactionService.isNotNull(t)) {

			chargeId = stripeService.createCharge(email, token, amount);
			t.setChargeId(chargeId);

			w.getTransactions().add(t);
			this.save(w);
		}

		return chargeId;
	}

	/**
	 * It adds money to a wallet, creates a transaction, and creates a charge in Stripe
	 * 
	 * @param email The email of the user
	 * @param w Wallet object
	 * @param amount The amount to be added to the wallet.
	 * @param token The token returned by Stripe.js
	 * @param remarks This is the description of the transaction.
	 * @return The chargeId is being returned.
	 */
	public String addMoneyToWallet(String email, Wallet w, Integer amount, String token, String remarks) throws StripeException {

		w.setBalance(w.getBalance() + amount);

		Transaction t = transactionService.add(new Transaction(amount, TransactionType.Deposit));
		t.setRemarks(remarks);
		t.setReferenceNumber(token.split("_")[1]);

		String chargeId = "";
		if (transactionService.isNotNull(t)) {

			chargeId = stripeService.createCharge(email, token, amount);
			t.setChargeId(chargeId);
			w.getTransactions().add(t);
			this.save(w);
		}

		return chargeId;
	}

	/**
	 * > If the wallet exists, return it, otherwise throw a NotFoundException
	 * 
	 * @param walletId The id of the wallet you want to get.
	 * @return A Wallet object
	 */
	public Wallet getWallet(Integer walletId) {
		return walletRepo.findById(walletId).orElseThrow(() -> new NotFoundException("Wallet"));
	}
	
	
	/**
	 * It takes a wallet, an amount and an orderId, and returns a wallet with the balance increased by the
	 * amount, and a transaction added to the wallet's transactions
	 * 
	 * @param wallet The wallet object of the user
	 * @param amount the amount to be refunded
	 * @param orderId The orderId of the order that is being refunded.
	 * @return A Wallet object
	 */
	public Wallet refundToWallet(Wallet wallet, Integer amount,Integer orderId) {
		
		
		wallet.setBalance(wallet.getBalance() + amount);
		Transaction t = transactionService.add(new Transaction(amount, TransactionType.Deposit));
		t.setRemarks("refunded for orderId: " + orderId);
		t.setReferenceNumber(UUID.randomUUID().toString());

		if (transactionService.isNotNull(t)) {
			wallet.getTransactions().add(t);
		}
		
		return wallet;
	}

}
