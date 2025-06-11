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
        keyboard.setKeyboard(java.util.Collections.singletonList(row));

        SendMessage msg = new SendMessage(String.valueOf(chatId), startText());
        msg.setReplyMarkup(keyboard);
        return msg;
    }

    private String startText() {
        return "Привет! \uD83D\uDC4B\n" +
                "Мы занимаемся установкой автооборудования: ГБО 6 поколения. \n" +
                "Оставь заявку — наш специалист перезвонит и все расскажет.";  }
}
