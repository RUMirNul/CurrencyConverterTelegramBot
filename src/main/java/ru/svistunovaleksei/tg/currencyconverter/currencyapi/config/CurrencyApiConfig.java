package ru.svistunovaleksei.tg.currencyconverter.currencyapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "currency.api")
public class CurrencyApiConfig {

    private String allPath;

    private String convertPath;


    public CurrencyApiConfig() {
    }


    public String getAllPath() {
        return allPath;
    }

    public void setAllPath(String allPath) {
        this.allPath = allPath;
    }

    public String getConvertPath() {
        return convertPath;
    }

    public void setConvertPath(String convertPath) {
        this.convertPath = convertPath;
    }
}
