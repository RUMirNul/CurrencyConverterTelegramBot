package ru.svistunovaleksei.tg.currencyconverter.controller;

import org.springframework.stereotype.Controller;
import ru.svistunovaleksei.tg.currencyconverter.constant.ApiMessage;
import ru.svistunovaleksei.tg.currencyconverter.dto.AllCurrencyDto;
import ru.svistunovaleksei.tg.currencyconverter.dto.ConvertParametersDto;
import ru.svistunovaleksei.tg.currencyconverter.dto.FromToCurrency;
import ru.svistunovaleksei.tg.currencyconverter.exceptions.InputAmountException;
import ru.svistunovaleksei.tg.currencyconverter.service.AllCurrencyService;
import ru.svistunovaleksei.tg.currencyconverter.service.FromToCurrencyService;

import javax.naming.ServiceUnavailableException;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@Controller
public class CurrencyController {

    private AllCurrencyService allCurrencyService;
    private FromToCurrencyService fromToCurrencyService;

    public CurrencyController(AllCurrencyService allCurrencyService, FromToCurrencyService fromToCurrencyService) {
        this.allCurrencyService = allCurrencyService;
        this.fromToCurrencyService = fromToCurrencyService;
    }

    public AllCurrencyDto getAllCurrenciesNames() {
        return allCurrencyService.getAllCurrency();
    }

    public FromToCurrency calculateRateAmount(ConvertParametersDto parameters) throws InputAmountException, IllegalArgumentException, ServiceUnavailableException {

        if (!Pattern.compile("\\d{1,13}([\\.\\,]\\d{1,5})?").matcher(parameters.getAmount()).matches()) {
            throw new InputAmountException();
        }

        if (validateCurrencyCode(parameters.getFrom()) && validateCurrencyCode(parameters.getTo())) {
            FromToCurrency fromToCurrency = fromToCurrencyService.calculateRateAmount(parameters);

            if (fromToCurrency.getStatus().equalsIgnoreCase(ApiMessage.SUCCESS.getMessage())) {
                return fromToCurrency;
            } else {
                throw new ServiceUnavailableException();
            }

        } else {
            throw new IllegalArgumentException();
        }
    }

    private boolean validateCurrencyCode(String code) {
        Map<String, String> allCurrencyNames = allCurrencyService.getAllCurrency().getCurrencies();

        if (!(allCurrencyNames == null || allCurrencyNames.isEmpty())) {
            Set<String> currencyCode = allCurrencyNames.keySet();

            String[] codeList = code.split(",");
            for (String c : codeList) {
                if (!currencyCode.contains(c.toUpperCase())) {
                    return false;
                }
            }
        }
        return true;
    }
}
