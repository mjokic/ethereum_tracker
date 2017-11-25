package com.wifi.ethereumtracker.app.model;

public class Currency {

    private String name;
    private String sign;

    public Currency(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Currency && this.name.equals(((Currency) obj).name);
    }
}
