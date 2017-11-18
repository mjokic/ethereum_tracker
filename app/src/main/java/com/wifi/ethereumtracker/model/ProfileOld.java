package com.wifi.ethereumtracker.model;


import java.util.List;

public class ProfileOld {
    /**
     * This contains name of 'source' site
     * and list of available currencies
     */


    private String site;
    private List<String> currencies;

    public ProfileOld() {
    }

    public ProfileOld(String site, List<String> currencies) {
        this.site = site;
        this.currencies = currencies;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public List<String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<String> currencies) {
        this.currencies = currencies;
    }

    @Override
    public String toString() {
        return "ProfileOld{" +
                "site='" + site + '\'' +
                ", currencies=" + currencies +
                '}';
    }
}
