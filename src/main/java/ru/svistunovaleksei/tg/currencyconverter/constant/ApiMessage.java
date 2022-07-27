package ru.svistunovaleksei.tg.currencyconverter.constant;

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
