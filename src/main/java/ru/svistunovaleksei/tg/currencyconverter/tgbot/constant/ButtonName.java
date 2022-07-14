package ru.svistunovaleksei.tg.currencyconverter.tgbot.constant;

public enum ButtonName {
    HELP_BUTTON(TextConstants.help),
    ALL_CURRENCY_BUTTON(TextConstants.allCurrency);

    private final String buttonName;

    ButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}
