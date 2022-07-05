package ru.svistunovaleksei.tg.currencyconverter.currencyapi.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.config.CurrencyApiConfig;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.constant.APIMessageEnum;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.entity.AllCurrency;

import java.util.Map;

@Service
public class AllCurrencyService {

    private CurrencyApiConfig currencyApiConfig;
    private String url;
    private Map<String, String> allCurrenciesNames;
    private String status;

    public AllCurrencyService(CurrencyApiConfig currencyApiConfig) {
        this.currencyApiConfig = currencyApiConfig;
        this.url = currencyApiConfig.getPathAllCurrency().replace("{token}", currencyApiConfig.getToken());
    }

    public Map<String, String> getAllCurrenciesNames() {
        return allCurrenciesNames;
    }

    public String getStatus() {
        return status;
    }

    @PostConstruct
    public void update() {
        RestTemplate restTemplate = new RestTemplate();
        AllCurrency allCurrency = restTemplate.getForEntity(url, AllCurrency.class).getBody();
        if (allCurrency.getStatus().equalsIgnoreCase(APIMessageEnum.SUCCESS.getMessage())) {
            this.allCurrenciesNames = allCurrency.getCurrencies();
            this.allCurrenciesNames.put("RUB", "Российский рубль");
        }
        this.status = allCurrency.getStatus();
    }

}
