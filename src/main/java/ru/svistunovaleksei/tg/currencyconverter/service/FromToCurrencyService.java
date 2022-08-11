package ru.svistunovaleksei.tg.currencyconverter.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.svistunovaleksei.tg.currencyconverter.config.CurrencyApiConfig;
import ru.svistunovaleksei.tg.currencyconverter.constant.ApiResponseCodeMessage;
import ru.svistunovaleksei.tg.currencyconverter.dto.ConversionParametersDto;
import ru.svistunovaleksei.tg.currencyconverter.dto.FromToCurrencyDto;
import ru.svistunovaleksei.tg.currencyconverter.exceptions.IncorrectFromToCurrencyDtoException;

import javax.naming.ServiceUnavailableException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FromToCurrencyService {

    private final CurrencyApiConfig currencyApiConfig;


    public FromToCurrencyService(CurrencyApiConfig currencyApiConfig) {
        this.currencyApiConfig = currencyApiConfig;
    }

    public FromToCurrencyDto calculateRateAmount(ConversionParametersDto parameters) throws ServiceUnavailableException, IncorrectFromToCurrencyDtoException {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("amount", parameters.getAmount());
        urlParams.put("from", parameters.getFrom());
        urlParams.put("to", parameters.getTo());

        FromToCurrencyDto fromToCurrencyDto = WebClient.builder()
                .baseUrl(currencyApiConfig.getConvertPath())
                .defaultUriVariables(urlParams)
                .build()
                .get()
                .retrieve()
                .bodyToMono(FromToCurrencyDto.class)
                .onErrorReturn(new FromToCurrencyDto())
                .block();

        if (fromToCurrencyDto == null || fromToCurrencyDto.getStatus() == null || !fromToCurrencyDto.getStatus().equalsIgnoreCase(ApiResponseCodeMessage.SUCCESS.getMessage())) {
            throw new ServiceUnavailableException();
        }

        if (isValidFromToCurrencyDto(fromToCurrencyDto)) {
            return fromToCurrencyDto;
        } else {
            throw new IncorrectFromToCurrencyDtoException();
        }
    }

    private boolean isValidFromToCurrencyDto(FromToCurrencyDto fromToCurrencyDto) {
        return fromToCurrencyDto.getStatus() != null && fromToCurrencyDto.getRates() != null && !fromToCurrencyDto.getRates().isEmpty();
    }
}
