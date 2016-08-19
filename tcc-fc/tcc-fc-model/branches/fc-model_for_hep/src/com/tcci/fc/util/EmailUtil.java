/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Gilbert.Lin
 */
public class EmailUtil {

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Emails that doesn’t match:                                                          
     * “mkyong” – must contains “@” symbol                                             
     * “mkyong@.com.my” – tld can not start with dot “.”                               
     * “mkyong123@gmail.a” – “.a” is not a valid tld, last tld must contains at least t
     * “mkyong123@.com” – tld can not start with dot “.”                               
     * “mkyong123@.com.com” – tld can not start with dot “.”                           
     * “.mkyong@mkyong.com” – email’s first character can not start with dot “.”      
     * “mkyong()*@gmail.com” – email’s is only allow character, digit, underscore and da
     * “mkyong@%*.com” – email’s tld is only allow character and digit                  
     * “mkyong..2002@gmail.com” – double dots “.” are not allow                        
     * “mkyong.@gmail.com” – email’s last character can not end with dot “.”          
     * “mkyong@mkyong@gmail.com” – double “@” is not allow                             
     * “mkyong@gmail.com.1a” -email’s tld which has two characters can not contains digit
     * 
     * @param aEmail
     * @return isValid
     */
    public static boolean validate(final String aEmail) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(aEmail);
        return matcher.matches();
    }

    public static void main(String[] args) {
        System.out.println(validate("gilbert.lin@tcci.com"));

    }
}
