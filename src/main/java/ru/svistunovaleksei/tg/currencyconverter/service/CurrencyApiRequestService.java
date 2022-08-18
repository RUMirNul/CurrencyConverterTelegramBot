package ru.svistunovaleksei.tg.currencyconverter.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.svistunovaleksei.tg.currencyconverter.config.CurrencyApiConfig;
import ru.svistunovaleksei.tg.currencyconverter.constant.BotMessage;
import ru.svistunovaleksei.tg.currencyconverter.dto.AllCurrencyDto;
import ru.svistunovaleksei.tg.currencyconverter.dto.ConversionParametersDto;
import ru.svistunovaleksei.tg.currencyconverter.dto.FromToCurrencyDto;

import java.time.Duration;

@Service
public class CurrencyApiRequestService {
    private final CurrencyApiConfig currencyApiConfig;
    private final static int TIMEOUT_MILLIS = 3000;
    private final WebClient webClient;

    public CurrencyApiRequestService(CurrencyApiConfig currencyApiConfig) {
        this.currencyApiConfig = currencyApiConfig;
        webClient = WebClient.builder()
                .baseUrl(currencyApiConfig.getGetgeoapiUrl())
                .build();
    }

    public AllCurrencyDto getAllCurrency() throws Exception {

        AllCurrencyDto allCurrencyDto;
        try {
            allCurrencyDto = webClient
                    .get()
                    .uri("/v2/currency/list", uri -> uri.queryParam("api_key", currencyApiConfig.getGetgeoapiToken()).build())
                    .retrieve()
                    .onStatus(HttpStatus::isError, error -> Mono.error(new Exception()))
                    .bodyToMono(AllCurrencyDto.class)
                    .timeout(Duration.ofMillis(TIMEOUT_MILLIS))
                    .block();
            return allCurrencyDto;
        } catch (Exception e) {
            throw new Exception(BotMessage.EXCEPTION_ALL_CURRENCY_MESSAGE.getMessage());
        }
    }

    public FromToCurrencyDto calculateRateAmount(ConversionParametersDto parameters) throws Exception {
        FromToCurrencyDto fromToCurrencyDto;
        try {
            fromToCurrencyDto = webClient
                    .get()
                    .uri("/v2/currency/convert", uri -> {
                        uri.queryParam("api_key", currencyApiConfig.getGetgeoapiToken());
                        uri.queryParam("from", parameters.getFrom());
                        uri.queryParam("to", parameters.getTo());
                        uri.queryParam("amount", parameters.getAmount());
                        return uri.build();
                    })
                    .retrieve()
                    .onStatus(HttpStatus::isError, error -> Mono.error(new Exception()))
                    .bodyToMono(FromToCurrencyDto.class)
                    .timeout(Duration.ofMillis(TIMEOUT_MILLIS))
                    .block();
            return fromToCurrencyDto;
        } catch (Exception e) {
            throw new Exception(BotMessage.EXCEPTION_CURRENCY_CONVERT_MESSAGE.getMessage());
        }
    }
}
