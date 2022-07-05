package ru.svistunovaleksei.tg.currencyconverter.currencyapi.entity;

import java.util.Map;

public class AllCurrency {

    private Map<String, String> currencies;
    private String status;

    public Map<String, String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Map<String, String> currencies) {
        this.currencies = currencies;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
