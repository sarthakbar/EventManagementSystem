package com.sb.util;

import java.util.UUID;

public class PasswordGenerator {

    public static String generateTempPassword() {
         
        	String pass=UUID.randomUUID().toString().substring(0, 8);
        	System.out.println(pass);
        	return pass;
    }
}
