package ru.svistunovaleksei.tg.currencyconverter.tgbot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.CurrencyController;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.dto.ToCurrencyConvert;
import ru.svistunovaleksei.tg.currencyconverter.currencyapi.exceptions.InputAmountException;
import ru.svistunovaleksei.tg.currencyconverter.tgbot.constant.BotMessage;
import ru.svistunovaleksei.tg.currencyconverter.tgbot.constant.TextConstants;
import ru.svistunovaleksei.tg.currencyconverter.tgbot.keyboard.ReplyKeyboardMaker;

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

                    return getCurrencyCalculationMessage(chatId, messageData[0], messageData[1], messageData[3]);
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

    private SendMessage getCurrencyCalculationMessage(String chatId, String amount, String from, String to) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);

        if (from.split(",").length > 1) {
            sendMessage.setText(BotMessage.EXCEPTION_CONVERT_FROM_ARGS_LENGTH_MESSAGE.getMessage());
            return sendMessage;
        }
        if (to.split(",").length > 10) {
            sendMessage.setText(BotMessage.EXCEPTION_CONVERT_TO_ARGS_LENGTH_MESSAGE.getMessage());
            return sendMessage;
        }

        Map<String, ToCurrencyConvert> rates = null;
        try {
            rates = currencyController.getCalcRateAmount(amount, from, to);

        } catch (InputAmountException e) {
            sendMessage.setText(BotMessage.EXCEPTION_CURRENCY_INPUT_AMOUNT_MESSAGE.getMessage());

            return sendMessage;

        } catch (IllegalArgumentException e) {
            sendMessage.setText(BotMessage.EXCEPTION_NO_VALID_CURRENCY_CODE_MESSAGE.getMessage());

            return sendMessage;
        }

        if (!(rates.isEmpty() || rates == null)) {
            StringBuilder sb = new StringBuilder();

            sb.append("*Конвертация по вашему запросу*\n\n");
            for (String key : rates.keySet()) {

                ToCurrencyConvert toCurrency = rates.get(key);
                String info = TextConstants.convertMessage.replace("{from}", from.toUpperCase())
                        .replace("{to}", getCurrencyCodeFromCurrencyName(toCurrency.getCurrency_name()))
                        .replace("{rate}", toCurrency.getRate())
                        .replace("{amount}", amount)
                        .replace("{rate_for_amount}", toCurrency.getRate_for_amount());

                sb.append(info);
                sb.append("\n");
            }

            sendMessage.setText(sb.toString());

        } else {
            sendMessage.setText(BotMessage.EXCEPTION_CURRENCY_CONVERT_MESSAGE.getMessage());
        }

        return sendMessage;
    }

    private SendMessage getAllCurrencyMessage(String chatId) {
        Map<String, String> allCurrencyMap = currencyController.getAllCurrenciesNames();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);

        if (!(allCurrencyMap.isEmpty() || allCurrencyMap == null)) {
            StringBuilder sb = new StringBuilder();
            sb.append("*Доступные валюты*\n\n");
            sb.append("*Код валюты (Полное назание валюты)*\n");
            for (String key : allCurrencyMap.keySet()) {
                sb.append(key);
                sb.append(" (" + allCurrencyMap.get(key) + ")\n");
            }
            sendMessage.setText(sb.toString());
        } else {
            sendMessage.setText(BotMessage.EXCEPTION_ALL_CURRENCY_MESSAGE.getMessage());
        }

        return sendMessage;
    }

    private String getCurrencyCodeFromCurrencyName(String currencyName) {
        Map<String, String> allCurrencyMap = currencyController.getAllCurrenciesNames();

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

}
