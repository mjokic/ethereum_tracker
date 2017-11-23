package com.wifi.ethereumtracker.app.model;


public class Price {
    /**
        This class represents price response on API
     */

    private double currentPrice;
    private double change24hour;

    public Price() {
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getChange24hour() {
        return change24hour;
    }

    public void setChange24hour(double change24hour) {
        this.change24hour = change24hour;
    }
}