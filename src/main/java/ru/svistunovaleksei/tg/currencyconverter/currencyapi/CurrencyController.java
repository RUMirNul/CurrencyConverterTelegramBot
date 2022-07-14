package ru.svistunovaleksei.tg.currencyconverter.currencyapi;

import org.springframework.stereotype.Controller;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.constant.ApiMessage;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.dto.FromToCurrency;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.dto.ToCurrencyConvert;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.exceptions.InputAmountException;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.service.AllCurrencyService;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.service.FromToCurrencyService;

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

    public Map<String, String> getAllCurrenciesNames() {
        if (!allCurrencyService.getStatus().equalsIgnoreCase(ApiMessage.SUCCESS.getMessage())) {
            allCurrencyService.update();
        }
        return allCurrencyService.getAllCurrenciesNames();
    }

    public Map<String, ToCurrencyConvert> getCalcRateAmount(String amount, String from, String to) throws InputAmountException, IllegalArgumentException {
        if (!Pattern.compile("\\d{1,13}([\\.\\,]\\d{1,5})?").matcher(amount).matches()) {
            throw new InputAmountException();
        }
        if (validateCurrencyCode(from) && validateCurrencyCode(to)) {
            FromToCurrency fromToCurrency = fromToCurrencyService.getCalcRateAmount(amount, from, to);
            if (fromToCurrency.getStatus().equalsIgnoreCase(ApiMessage.SUCCESS.getMessage())) {
                return fromToCurrency.getRates();
            }
        } else {
            throw new IllegalArgumentException();
        }
        return null;
    }

    private boolean validateCurrencyCode(String code) {
        Map<String, String> allCurrencyNames = allCurrencyService.getAllCurrenciesNames();
        if (!(allCurrencyNames.isEmpty() || allCurrencyNames == null)) {
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
