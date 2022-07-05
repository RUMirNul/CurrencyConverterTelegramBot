package ru.svistunovaleksei.tg.currencyconverter.currencyapi.entity;

import java.util.Map;

public class FromToCurrency {

    private Map<String, ToCurrencyConvert> rates;

    private String status;

    public Map<String, ToCurrencyConvert> getRates() {
        return rates;
    }

    public void setRates(Map<String, ToCurrencyConvert> rates) {
        this.rates = rates;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
