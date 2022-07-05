package ru.svistunovaleksei.tg.currencyconverter.currencyapi.entity;

public class ToCurrencyConvert {
    private String currency_name;
    private String rate;
    private String rate_for_amount;

    public ToCurrencyConvert() {
    }

    public String getCurrency_name() {
        return currency_name;
    }

    public void setCurrency_name(String currency_name) {
        this.currency_name = currency_name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRate_for_amount() {
        return rate_for_amount;
    }

    public void setRate_for_amount(String rate_for_amount) {
        this.rate_for_amount = rate_for_amount;
    }
}
