package ru.svistunovaleksei.tg.currencyconverter.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.svistunovaleksei.tg.currencyconverter.config.CurrencyApiConfig;
import ru.svistunovaleksei.tg.currencyconverter.constant.ApiMessage;
import ru.svistunovaleksei.tg.currencyconverter.dto.AllCurrencyDto;
import ru.svistunovaleksei.tg.currencyconverter.exceptions.IncorrectAllCurrencyDtoException;

import javax.naming.ServiceUnavailableException;

@Service
public class AllCurrencyService {

    private final CurrencyApiConfig currencyApiConfig;

    public AllCurrencyService(CurrencyApiConfig currencyApiConfig) {
        this.currencyApiConfig = currencyApiConfig;
    }

    public AllCurrencyDto getAllCurrency() throws ServiceUnavailableException, IncorrectAllCurrencyDtoException {
        AllCurrencyDto allCurrencyDto = WebClient.builder()
                .baseUrl(currencyApiConfig.getAllPath())
                .build()
                .get()
                .retrieve()
                .bodyToMono(AllCurrencyDto.class)
                .onErrorReturn(new AllCurrencyDto())
                .block();

        if (allCurrencyDto == null || allCurrencyDto.getStatus() == null || !allCurrencyDto.getStatus().equalsIgnoreCase(ApiMessage.SUCCESS.getMessage())) {
            throw new ServiceUnavailableException();
        }

        if (isValidAllCurrencyDto(allCurrencyDto)) {
            return allCurrencyDto;
        } else {
            throw new IncorrectAllCurrencyDtoException();
        }
    }

    private boolean isValidAllCurrencyDto(AllCurrencyDto allCurrencyDto) {
        return allCurrencyDto.getCurrencies() != null && allCurrencyDto.getStatus() != null;
    }

}
