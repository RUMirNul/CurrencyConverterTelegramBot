package ru.svistunovaleksei.tg.currencyconverter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.telegram-bot")
public class TelegramBotConfig {

    private String name;

    private String token;

    private String webHookPath;

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public String getWebHookPath() {
        return webHookPath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
    }
}
