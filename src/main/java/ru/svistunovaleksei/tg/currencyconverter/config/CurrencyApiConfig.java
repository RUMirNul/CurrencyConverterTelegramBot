package ru.svistunovaleksei.tg.currencyconverter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.currency-api")
public class CurrencyApiConfig {

    private String allCurrencyPath;

    private String convertPath;


    public CurrencyApiConfig() {
    }


    public String getAllCurrencyPath() {
        return allCurrencyPath;
    }

    public void setAllCurrencyPath(String allCurrencyPath) {
        this.allCurrencyPath = allCurrencyPath;
    }

    public String getConvertPath() {
        return convertPath;
    }

    public void setConvertPath(String convertPath) {
        this.convertPath = convertPath;
    }
}
