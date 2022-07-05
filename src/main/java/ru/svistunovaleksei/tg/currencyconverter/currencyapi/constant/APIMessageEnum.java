package ru.svistunovaleksei.tg.currencyconverter.currencyapi.constant;

public enum APIMessageEnum {
    SUCCESS("success");

    private String message;

    APIMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
