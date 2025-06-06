package com.gaz.demo.bot.controller;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Component
public class StartController {

    public SendMessage buildStartMessage(long chatId) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("Оставить заявку"));
        row.add(new KeyboardButton("Подробнее об оборудовании"));
        keyboard.setKeyboard(java.util.Collections.singletonList(row));

        SendMessage msg = new SendMessage(String.valueOf(chatId), startText());
        msg.setReplyMarkup(keyboard);
        return msg;
    }

    private String startText() {
        return "\uD83D\uDC4B Добро пожаловать!\n" +
                "Вы подписались на уведомления о постановке в лист ожидания на установку Газобаллонного оборудования (ГБО) автомобиля 6 поколения.\n\n" +
                "Чтобы оставить заявку на установку оборудования на ваш автомобиль — нажмите кнопку \u00ABОставить заявку\u00BB.\n" +
                "Сейчас мы собираем информацию обо всех желающих поставить ГБО, после чего обязательно свяжемся с вами.";
    }
}
