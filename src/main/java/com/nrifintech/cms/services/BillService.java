package com.nrifintech.cms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.repositories.BillRepo;

@Service
public class BillService {

	@Autowired
	private BillRepo billRepo;
}
