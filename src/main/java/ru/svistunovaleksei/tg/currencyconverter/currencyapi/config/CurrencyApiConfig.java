package ru.svistunovaleksei.tg.currencyconverter.currencyapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CurrencyApiConfig {
    @Value("${currency.api.path.all.currency}")
    private String pathAllCurrency;

    @Value("${currency.api.path.convert.from.to.amount}")
    private String pathConvertFromToAmount;

    @Value("${currency.api.token}")
    private String token;

    public CurrencyApiConfig() {
    }

    public String getPathAllCurrency() {
        return pathAllCurrency;
    }

    public String getPathConvertFromToAmount() {
        return pathConvertFromToAmount;
    }

    public String getToken() {
        return token;
    }
}
