package ru.svistunovaleksei.tg.currencyconverter.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.svistunovaleksei.tg.currencyconverter.config.CurrencyApiConfig;
import ru.svistunovaleksei.tg.currencyconverter.constant.ApiResponseCodeMessage;
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
                .baseUrl(currencyApiConfig.getAllCurrencyPath())
                .build()
                .get()
                .retrieve()
                .bodyToMono(AllCurrencyDto.class)
                .onErrorReturn(new AllCurrencyDto())
                .block();

        if (allCurrencyDto == null || allCurrencyDto.getStatus() == null || !allCurrencyDto.getStatus().equalsIgnoreCase(ApiResponseCodeMessage.SUCCESS.getMessage())) {
            throw new ServiceUnavailableException();
        }

        if (isValidAllCurrencyDto(allCurrencyDto)) {
            return allCurrencyDto;
        } else {
            throw new IncorrectAllCurrencyDtoException();
        }
    }

    private boolean isValidAllCurrencyDto(AllCurrencyDto allCurrencyDto) {
        return allCurrencyDto.getStatus() != null && allCurrencyDto.getCurrencies() != null && !allCurrencyDto.getCurrencies().isEmpty();
    }

}
