package com.nrifintech.cms.utils;

import java.util.HashMap;
import java.util.Map;

import com.nrifintech.cms.types.WeekDay;

/**
 * It's a class that has a static variable WEEKDAY, a static variable days, a static method getDays,
 * and an enum Month
 */
public class Calendar {

	public static WeekDay WEEKDAY;
	public static Map<WeekDay, Integer> days = new HashMap<>();

	// Initializing the static variable days.
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

	/**
	 * It returns the number of days in a month.
	 * 
	 * @param month The month for which you want to get the number of days.
	 * @param year  The year for which you want to get the number of days in the
	 *              month.
	 * @return The number of days in the month.
	 */

	public static Integer getDays(Month month, Integer year) {

		int arr[] = { -1, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		Integer m = month.ordinal();
		// For checking leap year
		if (month.equals(Month.February) && ((year % 400 == 0) || ((year % 100 != 0) && (year % 4 == 0))))
			return (arr[m] + 1);

		return arr[m];
	}

}
