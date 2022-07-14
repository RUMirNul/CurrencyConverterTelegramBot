package ru.svistunovaleksei.tg.currencyconverter.currencyapi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.config.CurrencyApiConfig;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.dto.FromToCurrency;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.dto.ToCurrencyConvert;

import java.util.Map;

@Service
public class FromToCurrencyService {

    private CurrencyApiConfig currencyApiConfig;
    private String url;
    private Map<String, ToCurrencyConvert> rates;
    private String status;



    public FromToCurrencyService(CurrencyApiConfig currencyApiConfig) {
        this.currencyApiConfig = currencyApiConfig;
        this.url = currencyApiConfig.getPathConvertFromToAmount().replace("{token}", currencyApiConfig.getToken());
    }

    public Map<String, ToCurrencyConvert> getRates() {
        return rates;
    }

    public String getStatus() {
        return status;
    }

    public FromToCurrency getCalcRateAmount(String amount, String from, String to) {
        RestTemplate restTemplate = new RestTemplate();
        String urlWithAgrs = url.replace("{amount}", amount).replace("{from}", from).replace("{to}", to);
        FromToCurrency fromToCurrency = restTemplate.getForEntity(urlWithAgrs,FromToCurrency.class).getBody();
        return fromToCurrency;
    }
}
