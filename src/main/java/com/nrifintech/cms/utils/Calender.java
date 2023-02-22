package com.nrifintech.cms.utils;

import java.util.HashMap;
import java.util.Map;

enum WeekDay{
	Sunday,
	Monday,
	Tuesday,
	Wednesday,
	Thursday,
	Friday,
	Saturday
}

public class Calender {

	public static Map<WeekDay,Integer> days =  new HashMap<>();
	
	static {
		days.put( WeekDay.Monday,1);
		days.put( WeekDay.Tuesday,2);
		days.put( WeekDay.Wednesday,3);
		days.put( WeekDay.Thursday,4);
		days.put( WeekDay.Friday,5);
	}
	
}
