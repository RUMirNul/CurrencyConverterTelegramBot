package ru.svistunovaleksei.tg.currencyconverter.handler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.svistunovaleksei.tg.currencyconverter.constant.BotMessage;
import ru.svistunovaleksei.tg.currencyconverter.constant.TextConstants;

import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
class MessageHandlerTest {

    @Autowired
    private MessageHandler messageHandler;

    static Stream<Arguments> userMessageAndBotResponseStream() {
        return Stream.of(
                arguments(TextConstants.start, BotMessage.START_MESSAGE.getMessage()),
                arguments(TextConstants.help, BotMessage.HELP_MESSAGE.getMessage()),
                arguments("unknown_message", BotMessage.UNKNOWN_COMMAND_MESSAGE.getMessage()),
                arguments("100 usd to eur rub", BotMessage.UNKNOWN_COMMAND_MESSAGE.getMessage()),
                arguments("100 usd to eur,rub,uah,yer,tmt,syp,tzs,try,tnd,sll,qar", BotMessage.EXCEPTION_CONVERT_TO_ARGS_LENGTH_MESSAGE.getMessage()),
                arguments("100 usd,eur to rub", BotMessage.EXCEPTION_CONVERT_FROM_ARGS_LENGTH_MESSAGE.getMessage()),
                arguments("10. usd to rub", BotMessage.EXCEPTION_CURRENCY_INPUT_AMOUNT_MESSAGE.getMessage()),
                arguments("10a usd to rub", BotMessage.EXCEPTION_CURRENCY_INPUT_AMOUNT_MESSAGE.getMessage()),
                arguments("10.123456 usd to rub", BotMessage.EXCEPTION_CURRENCY_INPUT_AMOUNT_MESSAGE.getMessage()),
                arguments("10,123456 usd to rub", BotMessage.EXCEPTION_CURRENCY_INPUT_AMOUNT_MESSAGE.getMessage()),
                arguments("10.12 rub to abc", BotMessage.EXCEPTION_NO_VALID_CURRENCY_CODE_MESSAGE.getMessage()),
                arguments("10,12 rub to abc", BotMessage.EXCEPTION_NO_VALID_CURRENCY_CODE_MESSAGE.getMessage()),
                arguments("10.12 abc to rub", BotMessage.EXCEPTION_NO_VALID_CURRENCY_CODE_MESSAGE.getMessage()),
                arguments("10,12 abc to rub", BotMessage.EXCEPTION_NO_VALID_CURRENCY_CODE_MESSAGE.getMessage())
        );
    }

    @ParameterizedTest
    @MethodSource("userMessageAndBotResponseStream")
    public void shouldCorrectBotResponseOnUserMessage(String userMessage, String botResponse){

        long charIdRandom = new Random().nextLong();
        Chat chat = new Chat();
        chat.setId(charIdRandom);

        Message message = new Message();
        message.setText(userMessage);
        message.setChat(chat);

        SendMessage answerMessage = (SendMessage) messageHandler.handler(message);
        String answerMessageText = answerMessage.getText();
        long chatId = Long.parseLong(answerMessage.getChatId());

        assertEquals(charIdRandom, chatId);
        assertEquals(answerMessageText, botResponse);
    }

    static Stream<Arguments> userCurrencyConvertRequestMessageAndBotResponseStream() {
        return Stream.of(
                arguments("10 usd to eur", "USD", "EUR"),
                arguments("134.567 usd to rUb", "USD", "RUB"),
                arguments("134,567 usd to BZD", "USD", "BZD"),
                arguments("10 RuB в eur", "RUB", "EUR"),
                arguments("134.567 EUr в rUb", "EUR", "RUB"),
                arguments("134,567 usd в BZD", "USD", "BZD"),
                arguments("10 usd to uSD", "USD", "USD"),
                arguments("134.567 usd to uSd", "USD", "USD"),
                arguments("134,567 BZD to USD", "BZD", "USD")
        );
    }

    @ParameterizedTest
    @MethodSource("userCurrencyConvertRequestMessageAndBotResponseStream")
    void shouldCorrectCurrencyConversionFromTheCorrectUserRequest(String userMessage, String currencyCodeFrom, String currencyCodeTo) {

        long charIdRandom = new Random().nextLong();
        Chat chat = new Chat();
        chat.setId(charIdRandom);

        Message message = new Message();
        message.setText(userMessage);
        message.setChat(chat);

        SendMessage answerMessage = (SendMessage) messageHandler.handler(message);
        String answerMessageText = answerMessage.getText();
        long chatId = Long.parseLong(answerMessage.getChatId());

        boolean containsText1 = answerMessageText.contains("Конвертация по вашему запросу");
        boolean containsText2 = answerMessageText.contains("Текущий курс");
        boolean containsText3 = answerMessageText.contains("Для ваших накоплений конвертация:");
        boolean containsText4 = answerMessageText.contains(currencyCodeFrom);
        boolean containsText5 = answerMessageText.contains(currencyCodeTo);


        assertEquals(charIdRandom, chatId);
        assertTrue(containsText1 && containsText2 && containsText3 && containsText4 && containsText5);
    }


    @Test
    void shouldGetAllCurrencies() {

        long charIdRandom = new Random().nextLong();
        Chat chat = new Chat();
        chat.setId(charIdRandom);

        Message message = new Message();
        message.setText(TextConstants.allCurrency);
        message.setChat(chat);


        SendMessage answerMessage = (SendMessage) messageHandler.handler(message);
        String answerMessageText = answerMessage.getText();
        long chatId = Long.parseLong(answerMessage.getChatId());

        boolean containsText1 = answerMessageText.contains("Доступные валюты");
        boolean containsText2 = answerMessageText.contains("Код валюты (Полное назание валюты)");
        boolean containsText3 = answerMessageText.contains("RUB");
        boolean containsText4 = answerMessageText.contains("USD");
        boolean containsText5 = answerMessageText.contains("EUR");

        assertEquals(charIdRandom, chatId);
        assertTrue(containsText1 && containsText2 && containsText3 && containsText4 && containsText5);
    }

}