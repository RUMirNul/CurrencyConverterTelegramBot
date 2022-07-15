package ru.svistunovaleksei.tg.currencyconverter.currencyapi.constant;

public enum ApiMessage {
    SUCCESS("success");

    private String message;

    ApiMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
