package com.nrifintech.cms.types;

import java.util.Optional;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class Nullable{

	public static Optional<Object> of(Object obj){
		return Optional.ofNullable(obj);
	}
	
	public static Optional<Object> of(Object ...obj){
		return Optional.ofNullable(obj);
	}
	
}

