package com.nrifintech.cms.services;

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

@Service
public class WalletService implements Validator {

	private static final Double LIMIT = 100.0;

	@Autowired
	private WalletRepo walletRepo;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private StripeService stripeService;

	private Boolean checkForMinimumLimit(Double currentBalance) {

		if (currentBalance < LIMIT)
			return false;

		return true;
	}

	public Boolean checkMinimumAmount(Wallet w) {
		return this.checkForMinimumLimit(w.getBalance());
	}

	public Wallet save(Wallet w) {
		return walletRepo.save(w);
	}

	public Wallet updateWallet(Wallet w, Integer amount) {
		w.setBalance(w.getBalance() - amount);

		Transaction t = transactionService.add(new Transaction(amount, TransactionType.Withdrawl));
		t.setRemarks("ordered food");
		t.setReferenceNumber(UUID.randomUUID().toString());

		if (transactionService.isNotNull(t)) {
			w.getTransactions().add(t);
		}

		return w;
	}

	public String addMoneyToWallet(String email, Wallet w, Integer amount, String token) {

		w.setBalance(w.getBalance() + amount);

		Transaction t = transactionService.add(new Transaction(amount, TransactionType.Deposit));
		t.setRemarks("deposit to wallet");
		t.setReferenceNumber(token.split("_")[1]);

		String chargeId = "";

		if (transactionService.isNotNull(t)) {

			chargeId = stripeService.createCharge(email, token, amount);
			t.setChargeId(chargeId);
			
			System.out.println(chargeId);
			
			w.getTransactions().add(t);
			this.save(w);
		}

		return chargeId;
	}

	public String addMoneyToWallet(String email, Wallet w, Integer amount, String token, String remarks) {

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

	public Wallet getWallet(Integer walletId) {
		return walletRepo.findById(walletId).orElseThrow(() -> new NotFoundException("Wallet"));
	}

}
