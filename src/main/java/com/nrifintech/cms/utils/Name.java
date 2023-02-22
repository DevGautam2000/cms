package com.nrifintech.cms.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ForDevelopmentOnly
public class Name {
	public static Map<Integer, String> list = new HashMap<>();

	static {
		List<String> l = Arrays.asList("Chicken Curry","Fish Curry","Omlette","Dahi");
		int max = 3;
		int min = 0;
		for (int i = 0; i < 30; i++) {
			list.put(i, l.get( (int) Math.floor(Math.random() *  (max - min + 1) + min)) );
		}
	}
}
