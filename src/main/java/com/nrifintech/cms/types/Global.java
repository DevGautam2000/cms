package com.nrifintech.cms.types;

import java.time.LocalTime;


public interface Global {

	// order freeze time
	Integer PLACE_ORDER_FREEZE_TIME = 20; //8 pm
	
	//order cancellation freeze time
	LocalTime CANCEL_ORDER_FREEZE_TIME = LocalTime.MIDNIGHT; //00 : 00
	
}