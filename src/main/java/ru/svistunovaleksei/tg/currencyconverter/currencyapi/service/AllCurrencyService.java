package ru.svistunovaleksei.tg.currencyconverter.currencyapi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.config.CurrencyApiConfig;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.constant.ApiMessage;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.dto.AllCurrencyDto;

import java.util.HashMap;

@Service
public class AllCurrencyService {

    private CurrencyApiConfig currencyApiConfig;
    private String url;

    private AllCurrencyDto allCurrencyDto;

    public AllCurrencyService(CurrencyApiConfig currencyApiConfig) {
        this.currencyApiConfig = currencyApiConfig;
        this.url = currencyApiConfig.getPathAllCurrency().replace("{token}", currencyApiConfig.getToken());
    }

    public AllCurrencyDto getAllCurrency() {
        allCurrencyDto = WebClient.builder()
                .baseUrl(url)
                .build()
                .get()
                .retrieve()
                .bodyToMono(AllCurrencyDto.class)
                .onErrorReturn(new AllCurrencyDto())
                .block();

        if (!allCurrencyDto.getStatus().equalsIgnoreCase(ApiMessage.SUCCESS.getMessage())) return new AllCurrencyDto();

        return allCurrencyDto;
    }

}
