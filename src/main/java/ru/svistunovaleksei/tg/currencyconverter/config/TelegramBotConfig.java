package ru.svistunovaleksei.tg.currencyconverter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.telegram-bot")
@Getter
@Setter
public class TelegramBotConfig {

    private String name;
    private String token;
    private String webHookPath;
}
