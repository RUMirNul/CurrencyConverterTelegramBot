package ru.svistunovaleksei.tg.currencyconverter;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.svistunovaleksei.tg.currencyconverter.constant.BotMessage;
import ru.svistunovaleksei.tg.currencyconverter.handler.MessageHandler;

@Component
public class TelegramBotFacade {

    private final MessageHandler messageHandler;


    public TelegramBotFacade(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }


    public BotApiMethod<?> handler(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            return messageHandler.handler(message);
        }
        return new SendMessage(message.getChatId().toString(), BotMessage.UNKNOWN_COMMAND_MESSAGE.getMessage());
    }
}
