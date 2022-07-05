package ru.svistunovaleksei.tg.currencyconverter.tgbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotConfig {
    @Value("${telegram.bot.name}")
    private String userName;
    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.bot.webhook.path}")
    private String webHookPath;

    public String getUserName() {
        return userName;
    }

    public String getBotToken() {
        return botToken;
    }

    public String getWebHookPath() {
        return webHookPath;
    }
}
