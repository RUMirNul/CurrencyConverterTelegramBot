package ru.svistunovaleksei.tg.currencyconverter.currencyapi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.config.CurrencyApiConfig;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.dto.ConvertParametersDto;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.dto.FromToCurrency;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.dto.ToCurrencyConvert;

import java.util.HashMap;
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

    public FromToCurrency calculateRateAmount(ConvertParametersDto parameters) {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("amount", parameters.getAmount());
        urlParams.put("from", parameters.getFrom());
        urlParams.put("to", parameters.getTo());

        FromToCurrency fromToCurrency = WebClient.builder()
                .baseUrl(url)
                .defaultUriVariables(urlParams)
                .build()
                .get()
                .retrieve()
                .bodyToMono(FromToCurrency.class)
                .block();

        return fromToCurrency;
    }
}
