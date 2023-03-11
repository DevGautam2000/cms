package com.nrifintech.cms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.entities.Transaction;
import com.nrifintech.cms.entities.Wallet;
import com.nrifintech.cms.errorhandler.NotFoundException;
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

	public Wallet updateWallet(Wallet w, Double amount) {
		w.setBalance(w.getBalance() - amount);

		Transaction t = transactionService.add(new Transaction(amount, TransactionType.Withdrawl));

		if (transactionService.isNotNull(t)) {
			w.getTransactions().add(t);
		}

		return w;
	}

	public Wallet addMoneyToWallet(Wallet w, Double amount) {

		w.setBalance(w.getBalance() + amount);

		Transaction t = transactionService.add(new Transaction(amount, TransactionType.Deposit));

		if (transactionService.isNotNull(t)) {
			w.getTransactions().add(t);
		}

		return w;
	}

	public Wallet getWallet(Integer walletId) {
		return walletRepo.findById(walletId).orElseThrow(() -> new NotFoundException("Wallet"));
	}

}
