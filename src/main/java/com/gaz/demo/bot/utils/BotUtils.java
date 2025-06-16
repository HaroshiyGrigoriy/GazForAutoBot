package com.gaz.demo.bot.utils;

import com.gaz.demo.bot.AutoGasBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class BotUtils {
    public static void sendMessage(AutoGasBot bot, Long chatId, String text) {
        try {
            bot.execute(SendMessage.builder().chatId(chatId).text(text).parseMode("HTML").build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessageWithButtons(AutoGasBot bot, Long chatId, String text, InlineKeyboardMarkup markup) {

        try {
            bot.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .replyMarkup(markup)
                    .parseMode("HTML").
                    build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public static InlineKeyboardMarkup createKeyboardMarkup(List<CustomButton> buttons) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> currentRaw = new ArrayList<>();

        for (CustomButton button : buttons) {
            InlineKeyboardButton.InlineKeyboardButtonBuilder builder =
                    InlineKeyboardButton.builder().text(button.getText());
            if (button.getUrl() != null) {
                builder.url(button.getUrl());
            } else if (button.getCallback() != null) {
                builder.callbackData(button.getCallback());
            }
            currentRaw.add(builder.build());
        }
        if (!currentRaw.isEmpty()) {
            keyboard.add(new ArrayList<>(currentRaw));
        }

        return InlineKeyboardMarkup.builder().keyboard(keyboard).build();
    }
}
