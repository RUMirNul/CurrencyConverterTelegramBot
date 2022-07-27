package ru.svistunovaleksei.tg.currencyconverter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import ru.svistunovaleksei.tg.currencyconverter.TelegramBot;
import ru.svistunovaleksei.tg.currencyconverter.TelegramBotFacade;

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

        bot.setBotUsername(telegramBotConfig.getName());
        bot.setBotToken(telegramBotConfig.getName());
        bot.setBotPath(telegramBotConfig.getWebHookPath());

        return bot;
    }
}
