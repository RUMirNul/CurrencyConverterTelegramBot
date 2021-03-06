package ru.svistunovaleksei.tg.currencyconverter.tgbot.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.svistunovaleksei.tg.currencyconverter.tgbot.constant.ButtonNameEnum;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReplyKeyboardMaker {
    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        KeyboardRow rowFirst = new KeyboardRow();
        rowFirst.add(new KeyboardButton((ButtonNameEnum.ALL_CURRENCY_BUTTON.getButtonName())));
        rowFirst.add(new KeyboardButton(ButtonNameEnum.HELP_BUTTON.getButtonName()));


        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(rowFirst);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }
}
