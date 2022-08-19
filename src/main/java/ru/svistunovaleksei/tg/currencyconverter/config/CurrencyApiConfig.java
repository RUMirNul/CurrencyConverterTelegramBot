package ru.svistunovaleksei.tg.currencyconverter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.currency-api")
@Getter
@Setter
public class CurrencyApiConfig {

    private String getgeoapiUrl;
    private String getgeoapiToken;

    public CurrencyApiConfig() {
    }
}
