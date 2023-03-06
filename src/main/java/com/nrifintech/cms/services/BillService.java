package com.nrifintech.cms.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.entities.Bill;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.repositories.BillRepo;
import com.nrifintech.cms.utils.Validator;

@Service
public class BillService implements Validator{

	@Autowired
	private BillRepo billRepo;
	
//	TODO: complete the service
	
	public Bill addNewBill(Bill bill) {
		return billRepo.save(bill);
	}
	
	public Bill saveBill(Bill bill) {
		return billRepo.save(bill);
	}
	
	public List<Bill> getBills() {
		return billRepo.findAll();
	}
	
	public Bill getBill(Integer billId) {
		return billRepo.findById(billId).orElseThrow(() ->  new NotFoundException("Bill"));
	}
	
}
