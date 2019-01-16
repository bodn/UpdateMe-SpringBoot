package com.updateme.utils;

import java.text.DecimalFormat;

public class StringUtils {


    /**
     * Format a given string to a number with comma separation
     * @param number - some number string that needs to be formatted
     * @return a formatted string with comma separation
     */
    public static String toFormatedNumber(String number){

        if(number != null){
            double amount = Double.parseDouble(number);
            DecimalFormat formatter = new DecimalFormat("#,###");
            return formatter.format(amount);
        }
        else{
            return null;
        }

    }
}
