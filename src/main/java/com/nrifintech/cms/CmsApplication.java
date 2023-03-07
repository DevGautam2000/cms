package com.nrifintech.cms;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import com.nrifintech.cms.utils.Calender;

@SpringBootApplication
@EnableAsync
public class CmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CmsApplication.class, args);
		
//		System.out.println(Calender.getDays(Calender.Month.March, 2023));
	}
}
