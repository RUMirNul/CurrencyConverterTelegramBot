package ru.svistunovaleksei.tg.currencyconverter.constant;

public enum ApiResponseCodeMessage {
    SUCCESS("success");

    private final String message;
    ApiResponseCodeMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
