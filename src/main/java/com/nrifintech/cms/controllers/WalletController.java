package com.nrifintech.cms.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.cms.dtos.WalletEmailResponse;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.entities.Wallet;
import com.nrifintech.cms.events.WalletRechargeEvent;
import com.nrifintech.cms.payments.dto.StripeToken;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.UserService;
import com.nrifintech.cms.services.WalletService;
import com.nrifintech.cms.types.Response;
import com.stripe.exception.StripeException;
import com.stripe.model.Application;

@CrossOrigin
@RestController
@RequestMapping(Route.Wallet.prefix)
public class WalletController {
	
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@GetMapping(Route.Wallet.getWallet +"/{walletId}")
	public Response getWallet(@PathVariable Integer walletId,Principal principal) {
		User user = userService.getuser(principal.getName());
		
		Wallet wallet = walletService.getWallet(user.getWallet().getId());
		Wallet w = walletService.getWallet(walletId);

		if( !( w.getId().equals(wallet.getId()) ) )
		return Response.setErr("Wallet does not exist for user.", HttpStatus.NOT_ACCEPTABLE);
		
		return Response.set(w, HttpStatus.OK);
	}
	
	@PostMapping(Route.Wallet.addMoney +"/{amount}")
	public Response addMoney(Principal principal,@PathVariable Integer amount,@RequestBody StripeToken token) throws StripeException {
	
		User user = userService.getuser(principal.getName());
		
		Wallet w = walletService.getWallet(user.getWallet().getId());

		String chargeId = walletService.addMoneyToWallet(user.getEmail(),w,amount,token.getToken());
		
		if(chargeId!=null && chargeId.length() > 0) {
			this.applicationEventPublisher.publishEvent( new WalletRechargeEvent( new WalletEmailResponse(principal.getName() , w.getBalance() , amount , chargeId) ) );
			return Response.setMsg("Money added to wallet.", HttpStatus.OK);
		}
		return Response.setErr("Error adding money.", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
