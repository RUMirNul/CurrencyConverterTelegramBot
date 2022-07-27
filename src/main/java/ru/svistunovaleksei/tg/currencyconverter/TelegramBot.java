package ru.svistunovaleksei.tg.currencyconverter;


import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

public class TelegramBot extends SpringWebhookBot {

    private String botPath;
    private String botUsername;
    private String botToken;

    private TelegramBotFacade telegramBotFacade;

    public TelegramBot(TelegramBotFacade telegramBotFacade, SetWebhook setWebhook) {
        super(setWebhook);
        this.telegramBotFacade = telegramBotFacade;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return telegramBotFacade.handler(update);
    }

    @Override
    public String getBotPath() {
        return botPath;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void setBotPath(String botPath) {
        this.botPath = botPath;
    }

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }
}
