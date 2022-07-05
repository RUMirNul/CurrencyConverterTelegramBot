package ru.svistunovaleksei.tg.currencyconverter.tgbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import ru.svistunovaleksei.tg.currencyconverter.tgbot.TelegramBot;
import ru.svistunovaleksei.tg.currencyconverter.tgbot.TelegramBotFacade;

@Configuration
public class AppConfig {

    private final TelegramBotConfig telegramBotConfig;

    public AppConfig(TelegramBotConfig telegramBotConfig) {
        this.telegramBotConfig = telegramBotConfig;
    }

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(telegramBotConfig.getWebHookPath()).build();
    }

    @Bean
    public TelegramBot springWebhookBot(TelegramBotFacade telegramBotFacade, SetWebhook setWebhook) {

        TelegramBot bot = new TelegramBot(telegramBotFacade, setWebhook);

        bot.setBotUsername(telegramBotConfig.getUserName());
        bot.setBotToken(telegramBotConfig.getBotToken());
        bot.setBotPath(telegramBotConfig.getWebHookPath());

        return bot;
    }
}
