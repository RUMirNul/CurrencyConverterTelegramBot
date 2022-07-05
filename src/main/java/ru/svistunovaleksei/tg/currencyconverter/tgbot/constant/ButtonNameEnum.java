package ru.svistunovaleksei.tg.currencyconverter.tgbot.constant;

public enum ButtonNameEnum {
    HELP_BUTTON(TextConstants.help),
    ALL_CURRENCY_BUTTON(TextConstants.allCurrency);

    private final String buttonName;

    ButtonNameEnum(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}
