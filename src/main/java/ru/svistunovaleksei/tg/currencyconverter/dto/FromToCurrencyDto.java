package ru.svistunovaleksei.tg.currencyconverter.dto;

import java.util.Map;

public class FromToCurrencyDto {

    private Map<String, ToCurrencyConvertDto> rates;

    private String status;

    public Map<String, ToCurrencyConvertDto> getRates() {
        return rates;
    }

    public void setRates(Map<String, ToCurrencyConvertDto> rates) {
        this.rates = rates;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
