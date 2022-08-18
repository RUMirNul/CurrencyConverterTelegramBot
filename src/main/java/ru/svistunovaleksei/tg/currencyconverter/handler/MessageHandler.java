package ru.svistunovaleksei.tg.currencyconverter.handler;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.svistunovaleksei.tg.currencyconverter.constant.BotMessage;
import ru.svistunovaleksei.tg.currencyconverter.constant.TextConstants;
import ru.svistunovaleksei.tg.currencyconverter.dto.AllCurrencyDto;
import ru.svistunovaleksei.tg.currencyconverter.dto.ConversionParametersDto;
import ru.svistunovaleksei.tg.currencyconverter.dto.FromToCurrencyDto;
import ru.svistunovaleksei.tg.currencyconverter.dto.ToCurrencyConvertDto;
import ru.svistunovaleksei.tg.currencyconverter.keyboard.ReplyKeyboardMaker;
import ru.svistunovaleksei.tg.currencyconverter.service.CurrencyService;

import java.util.Map;

@Component
public class MessageHandler {

    private final ReplyKeyboardMaker replyKeyboardMaker;
    private final CurrencyService currencyService;


    public MessageHandler(ReplyKeyboardMaker replyKeyboardMaker, CurrencyService currencyService) {
        this.replyKeyboardMaker = replyKeyboardMaker;
        this.currencyService = currencyService;
    }

    public BotApiMethod<?> handler(Message message) {

        String inputData = message.getText();
        String chatId = message.getChatId().toString();

        switch (inputData) {

            case TextConstants.start:
                return getStartMessage(chatId);

            case TextConstants.help:
                return getMessage(chatId, BotMessage.HELP_MESSAGE);

            case TextConstants.allCurrency:
                return getAllCurrencyMessage(chatId);

            default:
                String newInputData = convertInputData(inputData);
                String[] messageData = newInputData.split(" ");

                if (messageData.length == 4 && (messageData[2].equalsIgnoreCase("to") || messageData[2].equalsIgnoreCase("в"))) {

                    ConversionParametersDto conversionParametersDto = new ConversionParametersDto();
                    conversionParametersDto.setAmount(messageData[0]);
                    conversionParametersDto.setFrom(messageData[1]);
                    conversionParametersDto.setTo(messageData[3]);

                    return currencyCalculationMessage(chatId, conversionParametersDto);
                }
        }

        return getMessage(chatId, BotMessage.UNKNOWN_COMMAND_MESSAGE);
    }

    private SendMessage getStartMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, BotMessage.START_MESSAGE.getMessage());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboard());

        return sendMessage;
    }

    private SendMessage getMessage(String chatId, BotMessage botMessage) {
        SendMessage sendMessage = new SendMessage(chatId, botMessage.getMessage());
        sendMessage.enableMarkdown(true);

        return sendMessage;
    }

    private SendMessage currencyCalculationMessage(String chatId, ConversionParametersDto parameters) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);

        if (parameters.getFrom().split(",").length > 1) {
            sendMessage.setText(BotMessage.EXCEPTION_CONVERT_FROM_ARGS_LENGTH_MESSAGE.getMessage());
            return sendMessage;
        }
        if (parameters.getTo().split(",").length > 10) {
            sendMessage.setText(BotMessage.EXCEPTION_CONVERT_TO_ARGS_LENGTH_MESSAGE.getMessage());
            return sendMessage;
        }

        FromToCurrencyDto fromToCurrencyDto;
        try {
            fromToCurrencyDto = currencyService.calculateRateAmount(parameters);
            sendMessage.setText(generateConvertCurrenciesMessage(fromToCurrencyDto, parameters));

        } catch (Exception e) {
            sendMessage.setText(e.getMessage());
            return sendMessage;
        }
        return sendMessage;
    }

    private SendMessage getAllCurrencyMessage(String chatId) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);

        AllCurrencyDto allCurrencyDto;
        try {
            allCurrencyDto = currencyService.getAllCurrenciesNames();
        } catch (Exception e) {
            sendMessage.setText(BotMessage.EXCEPTION_ALL_CURRENCY_MESSAGE.getMessage());
            return sendMessage;
        }

        sendMessage.setText(generateAllCurrenciesMessage(allCurrencyDto));

        return sendMessage;
    }

    private String getCurrencyCodeFromCurrencyName(String currencyName) throws Exception {
        AllCurrencyDto allCurrencyDto = currencyService.getAllCurrenciesNames();
        Map<String, String> allCurrencyMap = allCurrencyDto.getCurrencies();

        for (Map.Entry<String, String> entry : allCurrencyMap.entrySet()) {
            if (currencyName.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return currencyName;
    }

    private String convertInputData(String input) {
        String newString = input.replaceAll(", ", ",");

        if (newString.endsWith(",")) {
            newString = newString.substring(0, newString.length() - 1);
        }

        return newString;
    }

    @Cacheable("AllCurrencies")
    public String generateAllCurrenciesMessage(AllCurrencyDto allCurrencyDto) {
        Map<String, String> allCurrencyMap = allCurrencyDto.getCurrencies();

            StringBuilder sb = new StringBuilder("""
                    *Доступные валюты*

                    *Код валюты (Полное назание валюты)*
                    """);

            for (String key : allCurrencyMap.keySet()) {
                sb.append(key);
                sb.append(" (").append(allCurrencyMap.get(key)).append(")\n");
            }

            return sb.toString();
    }

    private String generateConvertCurrenciesMessage(FromToCurrencyDto fromToCurrencyDto, ConversionParametersDto parameters) {
        Map<String, ToCurrencyConvertDto> rates = fromToCurrencyDto.getRates();

        StringBuilder sb = new StringBuilder();

        sb.append("*Конвертация по вашему запросу*\n\n");
        for (String key : rates.keySet()) {

            ToCurrencyConvertDto toCurrency = rates.get(key);
            String info;
            try {
                info = TextConstants.convertMessage.replace("{from}", parameters.getFrom().toUpperCase())
                        .replace("{to}", getCurrencyCodeFromCurrencyName(toCurrency.getCurrencyName()))
                        .replace("{rate}", toCurrency.getRate())
                        .replace("{amount}", parameters.getAmount())
                        .replace("{rate_for_amount}", toCurrency.getRateForAmount());
            } catch (Exception e) {
                return BotMessage.EXCEPTION_CURRENCY_CONVERT_MESSAGE.getMessage();
            }

            sb.append(info);
            sb.append("\n");
        }
        return sb.toString();
    }

}
