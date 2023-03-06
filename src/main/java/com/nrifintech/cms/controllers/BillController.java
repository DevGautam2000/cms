package com.nrifintech.cms.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.cms.entities.Bill;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.BillService;
import com.nrifintech.cms.types.Response;
import com.nrifintech.cms.utils.ErrorHandlerImplemented;

@CrossOrigin
@RestController
@RequestMapping(Route.Bill.prefix)
public class BillController {

	@Autowired
	private BillService billService;

	@GetMapping(Route.Bill.getBills)
	public Response getBills() {

		List<Bill> bills = billService.getBills();

		if (billService.isNotNull(bills))
			return Response.set(bills, HttpStatus.OK);

		return Response.set("Bills not found.", HttpStatus.BAD_REQUEST);
	}

	@ErrorHandlerImplemented(handler = NotFoundException.class)
	@GetMapping(Route.Bill.getBill + "/{billId}")
	public Response getBills(@PathVariable Integer billId) {

		Bill bill = billService.getBill(billId);
		return Response.set(bill, HttpStatus.OK);
	}

}
