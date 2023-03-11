package com.nrifintech.cms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.cms.entities.Wallet;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.WalletService;
import com.nrifintech.cms.types.Response;

@CrossOrigin
@RestController
@RequestMapping(Route.Wallet.prefix)
public class WalletController {
	
	@Autowired
	private WalletService walletService;

	@GetMapping(Route.Wallet.getWallet +"/{walletId}")
	public Response getWallet(@PathVariable Integer walletId) {
		
		Wallet w = walletService.getWallet(walletId);
		
		return Response.set(w, HttpStatus.OK);
	}
	
	@PostMapping(Route.Wallet.addMoney +"/{walletId}/{amount}")
	public Response addMoney(@PathVariable Integer walletId,@PathVariable Double amount) {
		Wallet w = walletService.getWallet(walletId);
		
		w = walletService.addMoneyToWallet(w,amount);
		
		if(walletService.isNotNull(w)) {
			walletService.save(w);
			return Response.setMsg("Money added to wallet.", HttpStatus.OK);
		}
		return Response.setErr("Error adding money.", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
