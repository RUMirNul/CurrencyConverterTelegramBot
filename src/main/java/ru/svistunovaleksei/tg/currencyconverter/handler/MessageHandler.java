package ru.svistunovaleksei.tg.currencyconverter.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.svistunovaleksei.tg.currencyconverter.controller.CurrencyController;
import ru.svistunovaleksei.tg.currencyconverter.dto.AllCurrencyDto;
import ru.svistunovaleksei.tg.currencyconverter.dto.ConvertParametersDto;
import ru.svistunovaleksei.tg.currencyconverter.dto.FromToCurrencyDto;
import ru.svistunovaleksei.tg.currencyconverter.dto.ToCurrencyConvertDto;
import ru.svistunovaleksei.tg.currencyconverter.exceptions.IncorrectAllCurrencyDtoException;
import ru.svistunovaleksei.tg.currencyconverter.exceptions.IncorrectFromToCurrencyDtoException;
import ru.svistunovaleksei.tg.currencyconverter.exceptions.InputAmountException;
import ru.svistunovaleksei.tg.currencyconverter.constant.BotMessage;
import ru.svistunovaleksei.tg.currencyconverter.constant.TextConstants;
import ru.svistunovaleksei.tg.currencyconverter.keyboard.ReplyKeyboardMaker;

import javax.naming.ServiceUnavailableException;
import java.util.Map;

@Component
public class MessageHandler {

    private ReplyKeyboardMaker replyKeyboardMaker;
    private CurrencyController currencyController;


    public MessageHandler(ReplyKeyboardMaker replyKeyboardMaker, CurrencyController currencyController) {
        this.replyKeyboardMaker = replyKeyboardMaker;
        this.currencyController = currencyController;
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

                    ConvertParametersDto convertParametersDto = new ConvertParametersDto();
                    convertParametersDto.setAmount(messageData[0]);
                    convertParametersDto.setFrom(messageData[1]);
                    convertParametersDto.setTo(messageData[3]);

                    return currencyCalculationMessage(chatId, convertParametersDto);
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

    private SendMessage currencyCalculationMessage(String chatId, ConvertParametersDto parameters) {
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

        FromToCurrencyDto fromToCurrencyDto = null;
        try {
            fromToCurrencyDto = currencyController.calculateRateAmount(parameters);
            sendMessage.setText(generateConvertCurrenciesMessage(fromToCurrencyDto, parameters));

        } catch (InputAmountException e) {
            sendMessage.setText(BotMessage.EXCEPTION_CURRENCY_INPUT_AMOUNT_MESSAGE.getMessage());

            return sendMessage;

        }catch (ServiceUnavailableException | IncorrectFromToCurrencyDtoException | IncorrectAllCurrencyDtoException e) {
            sendMessage.setText(BotMessage.EXCEPTION_CURRENCY_CONVERT_MESSAGE.getMessage());

            return sendMessage;

        } catch (IllegalArgumentException e) {
            sendMessage.setText(BotMessage.EXCEPTION_NO_VALID_CURRENCY_CODE_MESSAGE.getMessage());

            return sendMessage;
        }

        return sendMessage;
    }

    private SendMessage getAllCurrencyMessage(String chatId) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);

        AllCurrencyDto allCurrencyDto = null;
        try {
            allCurrencyDto = currencyController.getAllCurrenciesNames();
        } catch (ServiceUnavailableException | IncorrectAllCurrencyDtoException e) {
            sendMessage.setText(BotMessage.EXCEPTION_ALL_CURRENCY_MESSAGE.getMessage());
            return sendMessage;
        }

        sendMessage.setText(generateAllCurrenciesMessage(allCurrencyDto));

        return sendMessage;
    }

    private String getCurrencyCodeFromCurrencyName(String currencyName) throws ServiceUnavailableException, IncorrectAllCurrencyDtoException {
        AllCurrencyDto allCurrencyDto = currencyController.getAllCurrenciesNames();
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

    private String generateAllCurrenciesMessage(AllCurrencyDto allCurrencyDto) {
        Map<String, String> allCurrencyMap = allCurrencyDto.getCurrencies();

        if (!(allCurrencyMap == null || allCurrencyMap.isEmpty())) {

            StringBuilder sb = new StringBuilder();
            sb.append("*Доступные валюты*\n\n");
            sb.append("*Код валюты (Полное назание валюты)*\n");

            for (String key : allCurrencyMap.keySet()) {
                sb.append(key);
                sb.append(" (" + allCurrencyMap.get(key) + ")\n");
            }

            return sb.toString();

        } else {
            return BotMessage.EXCEPTION_ALL_CURRENCY_MESSAGE.getMessage();
        }
    }

    private String generateConvertCurrenciesMessage(FromToCurrencyDto fromToCurrencyDto, ConvertParametersDto parameters) {
        Map<String, ToCurrencyConvertDto> rates = fromToCurrencyDto.getRates();

        if (!(rates == null || rates.isEmpty())) {
            StringBuilder sb = new StringBuilder();

            sb.append("*Конвертация по вашему запросу*\n\n");
            for (String key : rates.keySet()) {

                ToCurrencyConvertDto toCurrency = rates.get(key);
                String info = null;
                try {
                    info = TextConstants.convertMessage.replace("{from}", parameters.getFrom().toUpperCase())
                            .replace("{to}", getCurrencyCodeFromCurrencyName(toCurrency.getCurrencyName()))
                            .replace("{rate}", toCurrency.getRate())
                            .replace("{amount}", parameters.getAmount())
                            .replace("{rate_for_amount}", toCurrency.getRateForAmount());
                } catch (ServiceUnavailableException | IncorrectAllCurrencyDtoException e) {
                    return BotMessage.EXCEPTION_CURRENCY_CONVERT_MESSAGE.getMessage();
                }

                sb.append(info);
                sb.append("\n");
            }

            return sb.toString();

        } else {
            return BotMessage.EXCEPTION_CURRENCY_CONVERT_MESSAGE.getMessage();
        }
    }

}
