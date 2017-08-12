package com.wifi.ethereumtracker.model.enumerations;

public class CurrencyEnum {

    public static String getSign(String currency){

        switch (currency){
            case "EUR":
                return "€";
            case "GBP":
                return "£";
            default:
                return "$";
        }

    }

}
