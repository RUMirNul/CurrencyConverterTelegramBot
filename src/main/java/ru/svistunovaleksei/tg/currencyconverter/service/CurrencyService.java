package ru.svistunovaleksei.tg.currencyconverter.service;

import org.springframework.stereotype.Service;
import ru.svistunovaleksei.tg.currencyconverter.constant.ApiResponseCodeMessage;
import ru.svistunovaleksei.tg.currencyconverter.constant.BotMessage;
import ru.svistunovaleksei.tg.currencyconverter.dto.AllCurrencyDto;
import ru.svistunovaleksei.tg.currencyconverter.dto.ConversionParametersDto;
import ru.svistunovaleksei.tg.currencyconverter.dto.FromToCurrencyDto;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class CurrencyService {

    private final CurrencyApiRequestService currencyApiRequestService;
    private final Pattern intOrDoublePattern = Pattern.compile("\\d{1,13}([.,]\\d{1,5})?");

    public CurrencyService(CurrencyApiRequestService currencyApiRequestService) {
        this.currencyApiRequestService = currencyApiRequestService;
    }

    public AllCurrencyDto getAllCurrenciesNames() throws Exception {
        return currencyApiRequestService.getAllCurrency();
    }

    public FromToCurrencyDto calculateRateAmount(ConversionParametersDto parameters) throws Exception {

        if (!intOrDoublePattern.matcher(parameters.getAmount()).matches()) {
            throw new Exception(BotMessage.EXCEPTION_CURRENCY_INPUT_AMOUNT_MESSAGE.getMessage());
        }

        if (validateCurrencyCode(parameters.getFrom()) && validateCurrencyCode(parameters.getTo())) {
            FromToCurrencyDto fromToCurrencyDto = currencyApiRequestService.calculateRateAmount(parameters);

            if (fromToCurrencyDto.getStatus().equalsIgnoreCase(ApiResponseCodeMessage.SUCCESS.getMessage())) {
                return fromToCurrencyDto;
            } else {
                throw new Exception(BotMessage.EXCEPTION_CURRENCY_CONVERT_MESSAGE.getMessage());
            }

        } else {
            throw new Exception(BotMessage.EXCEPTION_NO_VALID_CURRENCY_CODE_MESSAGE.getMessage());
        }
    }

    private boolean validateCurrencyCode(String code) throws Exception {
        Map<String, String> allCurrencyNames = currencyApiRequestService.getAllCurrency().getCurrencies();

        Set<String> currencyCode = allCurrencyNames.keySet();

        String[] codeList = code.split(",");
        for (String c : codeList) {
            if (!currencyCode.contains(c.toUpperCase())) {
                return false;
            }
        }

        return true;
    }
}
