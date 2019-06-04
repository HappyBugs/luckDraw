package com.likuncheng.common;

import java.util.Random;

public class CreateAccountNumber {

	private static Random random = new Random();

	public static String createAccountNumber() {
		String accountNumber = "";
		for(int i = 0 ; i< 12 ; i++) {
			if(i == 0) {
				//不为0
				accountNumber+=(random.nextInt(9)+1);
				continue;
			}
			accountNumber+=random.nextInt(9);
		}
		return accountNumber;
	}
	

}
