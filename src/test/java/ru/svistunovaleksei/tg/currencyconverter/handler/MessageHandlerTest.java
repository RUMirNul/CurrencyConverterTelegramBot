package ru.svistunovaleksei.tg.currencyconverter.handler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.svistunovaleksei.tg.currencyconverter.constant.BotMessage;
import ru.svistunovaleksei.tg.currencyconverter.constant.TextConstants;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageHandlerTest {

    @Autowired
    private MessageHandler messageHandler;

    @Test
    void handler_startMessage() {
        Message message = new Message();
        message.setText(TextConstants.start);

        Chat chat = new Chat();
        chat.setId(13L);
        message.setChat(chat);

        SendMessage answerMessage = (SendMessage) messageHandler.handler(message);
        String answerMessageText = answerMessage.getText();

        assertEquals(answerMessageText, BotMessage.START_MESSAGE.getMessage());
    }

    @Test
    void handler_helpMessage() {
        Message message = new Message();
        message.setText(TextConstants.help);

        Chat chat = new Chat();
        chat.setId(13L);
        message.setChat(chat);

        SendMessage answerMessage = (SendMessage) messageHandler.handler(message);
        String answerMessageText = answerMessage.getText();

        assertEquals(answerMessageText, BotMessage.HELP_MESSAGE.getMessage());
    }

    @Test
    void handler_unknownCommandMessage() {
        Message message = new Message();
        message.setText("qwerty");

        Chat chat = new Chat();
        chat.setId(13L);
        message.setChat(chat);

        SendMessage answerMessage = (SendMessage) messageHandler.handler(message);
        String answerMessageText = answerMessage.getText();

        assertEquals(answerMessageText, BotMessage.UNKNOWN_COMMAND_MESSAGE.getMessage());
    }

    @Test
    void handler_uncorrectedParametersForCurrencyConverting() {
        Message message = new Message();
        message.setText("100 usd to eur rub");

        Chat chat = new Chat();
        chat.setId(13L);
        message.setChat(chat);

        SendMessage answerMessage = (SendMessage) messageHandler.handler(message);
        String answerMessageText = answerMessage.getText();

        assertEquals(answerMessageText, BotMessage.UNKNOWN_COMMAND_MESSAGE.getMessage());
    }

    @Test
    void handler_manyParametersToCurrencyConverting() {
        Message message = new Message();
        message.setText("100 usd to eur,rub,uah,yer,tmt,syp,tzs,try,tnd,sll,qar");

        Chat chat = new Chat();
        chat.setId(13L);
        message.setChat(chat);

        SendMessage answerMessage = (SendMessage) messageHandler.handler(message);
        String answerMessageText = answerMessage.getText();

        assertEquals(answerMessageText, BotMessage.EXCEPTION_CONVERT_TO_ARGS_LENGTH_MESSAGE.getMessage());
    }

    @Test
    void handler_manyParametersFromCurrencyConverting() {
        Message message = new Message();
        message.setText("100 usd,eur to rub");

        Chat chat = new Chat();
        chat.setId(13L);
        message.setChat(chat);

        SendMessage answerMessage = (SendMessage) messageHandler.handler(message);
        String answerMessageText = answerMessage.getText();

        assertEquals(answerMessageText, BotMessage.EXCEPTION_CONVERT_FROM_ARGS_LENGTH_MESSAGE.getMessage());
    }

    @Test
    void handler_inputAmountIncompleteErrorMessageForCurrencyConverting() {
        Message message = new Message();
        message.setText("10. usd to rub");

        Chat chat = new Chat();
        chat.setId(13L);
        message.setChat(chat);

        SendMessage answerMessage = (SendMessage) messageHandler.handler(message);
        String answerMessageText = answerMessage.getText();

        assertEquals(answerMessageText, BotMessage.EXCEPTION_CURRENCY_INPUT_AMOUNT_MESSAGE.getMessage());
    }

    @Test
    void handler_inputAmountHasTextErrorMessageForCurrencyConverting() {
        Message message = new Message();
        message.setText("10a usd to rub");

        Chat chat = new Chat();
        chat.setId(13L);
        message.setChat(chat);

        SendMessage answerMessage = (SendMessage) messageHandler.handler(message);
        String answerMessageText = answerMessage.getText();

        assertEquals(answerMessageText, BotMessage.EXCEPTION_CURRENCY_INPUT_AMOUNT_MESSAGE.getMessage());
    }

    @Test
    void handler_inputAmountLongErrorMessageForCurrencyConverting() {
        Message message = new Message();
        message.setText("10.123456 usd to rub");

        Chat chat = new Chat();
        chat.setId(13L);
        message.setChat(chat);

        SendMessage answerMessage = (SendMessage) messageHandler.handler(message);
        String answerMessageText = answerMessage.getText();

        assertEquals(answerMessageText, BotMessage.EXCEPTION_CURRENCY_INPUT_AMOUNT_MESSAGE.getMessage());
    }

    @Test
    void handler_inputToCurrencyNoValidCodeMessageForCurrencyConverting() {
        Message message = new Message();
        message.setText("10.12 rub to abc");

        Chat chat = new Chat();
        chat.setId(13L);
        message.setChat(chat);

        SendMessage answerMessage = (SendMessage) messageHandler.handler(message);
        String answerMessageText = answerMessage.getText();

        assertEquals(answerMessageText, BotMessage.EXCEPTION_NO_VALID_CURRENCY_CODE_MESSAGE.getMessage());
    }

    @Test
    void handler_inputFromCurrencyNoValidCodeMessageForCurrencyConverting() {
        Message message = new Message();
        message.setText("10.12 abc to rub");

        Chat chat = new Chat();
        chat.setId(13L);
        message.setChat(chat);

        SendMessage answerMessage = (SendMessage) messageHandler.handler(message);
        String answerMessageText = answerMessage.getText();

        assertEquals(answerMessageText, BotMessage.EXCEPTION_NO_VALID_CURRENCY_CODE_MESSAGE.getMessage());
    }


    @Test
    void handler_allCurrencyConvertingList() {
        Message message = new Message();
        message.setText(TextConstants.allCurrency);

        Chat chat = new Chat();
        chat.setId(13L);
        message.setChat(chat);

        SendMessage answerMessage = (SendMessage) messageHandler.handler(message);
        String answerMessageText = answerMessage.getText();

        boolean containsText1 = answerMessageText.contains("Доступные валюты");
        boolean containsText2 = answerMessageText.contains("Код валюты (Полное назание валюты)");


        assertTrue(containsText1 && containsText2);
    }

    @Test
    void handler_CurrencyConvertResult() {
        Message message = new Message();
        message.setText("10.12 usd to rub");

        Chat chat = new Chat();
        chat.setId(13L);
        message.setChat(chat);

        SendMessage answerMessage = (SendMessage) messageHandler.handler(message);
        String answerMessageText = answerMessage.getText();

        boolean containsText1 = answerMessageText.contains("Конвертация по вашему запросу");
        boolean containsText2 = answerMessageText.contains("Текущий курс");
        boolean containsText3 = answerMessageText.contains("Для ваших накоплений конвертация:");


        assertTrue(containsText1 && containsText2 && containsText3);
    }

}