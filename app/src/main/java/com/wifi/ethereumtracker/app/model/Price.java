package com.wifi.ethereumtracker.app.model;


import java.util.Date;

public class Price {
    /**
        This class represents price response on API
     */

    private Date date;
    private double price;
    private Currency currency;
    private double change24hr;

    public Price() {
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double currentPrice) {
        this.price = currentPrice;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public double getChange24hr() {
        return change24hr;
    }

    public void setChange24hr(double change24hour) {
        this.change24hr = change24hour;
    }

    @Override
    public String toString() {
        return "Price{" +
                "date=" + date +
                ", price=" + price +
                ", currency=" + currency +
                ", change24hr=" + change24hr +
                '}';
    }
}
