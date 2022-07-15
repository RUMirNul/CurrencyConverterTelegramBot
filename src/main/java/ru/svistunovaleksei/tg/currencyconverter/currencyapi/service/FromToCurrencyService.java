package ru.svistunovaleksei.tg.currencyconverter.currencyapi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.config.CurrencyApiConfig;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.dto.ConvertParametersDto;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.dto.FromToCurrency;

import java.util.HashMap;
import java.util.Map;

@Service
public class FromToCurrencyService {

    private final CurrencyApiConfig currencyApiConfig;



    public FromToCurrencyService(CurrencyApiConfig currencyApiConfig) {
        this.currencyApiConfig = currencyApiConfig;
    }

    public FromToCurrency calculateRateAmount(ConvertParametersDto parameters) {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("amount", parameters.getAmount());
        urlParams.put("from", parameters.getFrom());
        urlParams.put("to", parameters.getTo());

        return WebClient.builder()
                .baseUrl(currencyApiConfig.getConvertPath())
                .defaultUriVariables(urlParams)
                .build()
                .get()
                .retrieve()
                .bodyToMono(FromToCurrency.class)
                .block();
    }
}
