package com.wifi.ethereumtracker.model.enumerations;

public class CurrencyEnum {

    public static String getSign(String currency) {

        switch (currency) {
            case "eur":
                return "€";
            case "gbp":
                return "£";
            case "btc":
                return "฿";
            default:
                return "$";
        }

    }

}
