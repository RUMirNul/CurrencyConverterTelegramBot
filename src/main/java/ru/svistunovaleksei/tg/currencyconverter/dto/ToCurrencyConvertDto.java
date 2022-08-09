package ru.svistunovaleksei.tg.currencyconverter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ToCurrencyConvertDto {
    @JsonProperty("currency_name")
    private String currencyName;
    @JsonProperty("rate")
    private String rate;
    @JsonProperty("rate_for_amount")
    private String rateForAmount;

    public ToCurrencyConvertDto() {
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRateForAmount() {
        return rateForAmount;
    }

    public void setRateForAmount(String rateForAmount) {
        this.rateForAmount = rateForAmount;
    }
}
