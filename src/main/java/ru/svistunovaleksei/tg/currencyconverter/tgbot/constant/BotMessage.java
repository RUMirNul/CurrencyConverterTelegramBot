package ru.svistunovaleksei.tg.currencyconverter.tgbot.constant;

public enum BotMessage {

    START_MESSAGE("Привет! Это Бот для *конвертации валюты*. Он поддерживает *160+* валют.\n" +
            "Чтобы узнать как пользоваться ботом нажмите кнопку " +
            "*\"" + ButtonName.HELP_BUTTON.getButtonName() + "\"*.\n"),
    HELP_MESSAGE("*Информация по работе с ботом*\n\n" +
            "*Доступные валюты*\n" +
            "Кнопка \"*" + ButtonName.ALL_CURRENCY_BUTTON.getButtonName() + "\"* покажет все доступные валюты.\n\n" +
            "*Конвертация валют\n*" +
            "Чтобы получить конвертацию валюты напишите боту сообщение по шаблону: \"<*Количество*> <*Из какой валюты*> *в* <*В какую валюту*>\".\n\n" +
            "*Примечания:*\n" +
            "<> писать не нужно.\n" +
            "В параметре \"<В какую валюту>\" можно перечислить несколько валют, через запятую, но не больше *10*.\n" +
            "Коды валют регистронезависимые.\n\n" +
            "*Примеры:*\n" +
            "102.346 usd в rub - Конвертирует 102.346 Долларов США в Российские рубли\n" +
            "102 usd в rub, eur - Конвертирует 102 Доллара США в Российские рубли и Евро\n\n" +
            "*Число валюты для конвертации*\n" +
            "Можно использовать обычные числа и числа с плавующей точкой(запятой).\n" +
            "Максимум 13 цифр до точки и 5 цифр после точки(запятой).\n\n" +
            "*Примеры:*\n" +
            "120\n" +
            "123456789.98765\n" +
            "123,98\n"),
    UNKNOWN_COMMAND_MESSAGE("*Неизвестная команда*\n" +
            "Используйте *\"" + ButtonName.HELP_BUTTON.getButtonName() + "\"* для получения доступных команд.\n"),
    EXCEPTION_ALL_CURRENCY_MESSAGE("*Не удалось получить информацию о доступных валютах!*\n" +
            "Повторите попытку позже\n"),
    EXCEPTION_CURRENCY_CONVERT_MESSAGE("*Не удалось получить информацию о конвертации валют *\n" +
            "Повторите попытку позже\n"),
    EXCEPTION_NO_VALID_CURRENCY_CODE_MESSAGE("*Некоторые введённые вами валюты не поддерживаются!*\n" +
            "Используйте *\"" + ButtonName.ALL_CURRENCY_BUTTON.getButtonName() + "\"*, чтобы узнать все доступные" +
            " валюты.\n"),
    EXCEPTION_CURRENCY_INPUT_AMOUNT_MESSAGE("*Число для конвертации записано в неверном формате!*\n" +
            "Используйте *\"" + ButtonName.HELP_BUTTON.getButtonName() + "\"* для получения информации о записи числа.\n"),
    EXCEPTION_CONVERT_FROM_ARGS_LENGTH_MESSAGE("*Можно конвертировать только ИЗ одной валюты!*\n"),
    EXCEPTION_CONVERT_TO_ARGS_LENGTH_MESSAGE("*Можно конвертировать не более 10 валют за раз!\n*");


    private final String message;

    BotMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
