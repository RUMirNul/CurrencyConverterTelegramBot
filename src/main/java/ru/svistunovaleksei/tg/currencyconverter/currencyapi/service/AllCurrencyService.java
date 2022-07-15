package ru.svistunovaleksei.tg.currencyconverter.currencyapi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.config.CurrencyApiConfig;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.constant.ApiMessage;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.dto.AllCurrencyDto;

@Service
public class AllCurrencyService {

    private final CurrencyApiConfig currencyApiConfig;

    public AllCurrencyService(CurrencyApiConfig currencyApiConfig) {
        this.currencyApiConfig = currencyApiConfig;
    }

    public AllCurrencyDto getAllCurrency() {
        AllCurrencyDto allCurrencyDto = WebClient.builder()
                .baseUrl(currencyApiConfig.getAllPath())
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
