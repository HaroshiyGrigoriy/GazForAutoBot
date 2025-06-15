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

    public static InlineKeyboardMarkup createKeyboardMarkup(List<String> buttonTexts, List<String> callbackData, int buttonsPerRow) {
        List<List<InlineKeyboardButton>> keyboard = buildKeyboardRows(buttonTexts, callbackData, buttonsPerRow);
        return InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();
    }


    public static List<List<InlineKeyboardButton>> buildKeyboardRows(List<String> buttonText, List<String> callback, int bpr) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> currentRaw = new ArrayList<>();

        for (int i = 0; i < buttonText.size(); i++) {
            currentRaw.add(InlineKeyboardButton.builder().text(buttonText.get(i))
                    .callbackData(callback.get(i))
                    .build()
            );
            if (currentRaw.size() == bpr) {
                keyboard.add(new ArrayList<>(currentRaw));
                currentRaw.clear();
            }
        }
        if (!currentRaw.isEmpty()) {
            keyboard.add(new ArrayList<>(currentRaw));
        }
        return keyboard;
    }
    public static String instruction() {
        return """
                 Спасибо, что хотите установить наше оборудование!\u2028
                 Для включения в лист ожидания на установку оборудования, и составления заявки нам понадобится ваше:👇 
                 -Имя
                 -Контактный номер телефона
                 -Город установки
                 -Марка автомобиля на который будет устанавливаться ГБО
                 -Когда удобно ответить на звонок
                  
                  <b>Пример заполненной заявки:  </b>
                  <b>----------</b>
                    Имя : Петр
                    Телефон для связи : 79871235566
                    Город установки : Чебоксары
                    Марка машины : BMW
                    Когда удобно ответить на наш звонок : В любой день после 15 часов дня
                  <b>----------
                    
                    Для удобства рекомендуем скопировать форму ниже и заполнить ее в поле ввода. </b>
                """
                ;
    }

    public static String pattern() {
        return """
                 <pre>
                 Имя :
                 Телефон для связи :
                 Город установки :
                 Марка машины :
                 Когда удобно ответить на наш звонок :
                 </pre>
                """;
    }

    public static String responseToTheUserAfterTheRequest() {
        return """
                 Готово!😎
                 Спасибо, что отправили заявку, мы обязательно вам позвоним!
                """;
    }
}
