package com.nrifintech.cms.utils;

import java.util.HashMap;
import java.util.Map;

enum WeekDay {
	Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday
}

public class Calender {

	public static Map<WeekDay, Integer> days = new HashMap<>();

	static {
		days.put(WeekDay.Monday, 1);
		days.put(WeekDay.Tuesday, 2);
		days.put(WeekDay.Wednesday, 3);
		days.put(WeekDay.Thursday, 4);
		days.put(WeekDay.Friday, 5);
	}

	public enum Month {
		NONE, January, February, March, April, May, June, July, August, September, November, December

	};

	public static Integer getDays(Month month, Integer year) {

		int arr[] = { -1, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		Integer m = month.ordinal();
		// For checking leap year
		if (month.equals(Month.February) && ((year % 400 == 0) || ((year % 100 != 0) && (year % 4 == 0))))
			return (arr[m] + 1);

		return arr[m];
	}

}
