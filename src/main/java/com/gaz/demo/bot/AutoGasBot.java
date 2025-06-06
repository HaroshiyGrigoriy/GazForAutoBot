package com.gaz.demo.bot;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.gaz.demo.bot.controller.StartController;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class AutoGasBot extends TelegramLongPollingBot {

    private final BotConfig config;
    private final StartController startController;
    private final Map<Long, UserSession> sessions = new HashMap<>();

    @Autowired
    public AutoGasBot(BotConfig config, StartController startController) {
        this.config = config;
        this.startController = startController;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update);
        } else if (update.hasCallbackQuery()) {
            handleCallback(update);
        }
    }

    private void handleMessage(Update update) {
        String text = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        UserSession session = sessions.computeIfAbsent(chatId, id -> new UserSession());

        switch (session.getState()) {
            case ASK_FIO:
                session.setFio(text);
                session.setState(BotState.ASK_LOCATION);
                sendMessage(chatId, "Введите место проживания:");
                return;
            case ASK_LOCATION:
                session.setLocation(text);
                session.setState(BotState.ASK_PHONE);
                sendMessage(chatId, "Введите номер телефона в формате +79**-***-**-**:");
                return;
            case ASK_PHONE:
                session.setPhone(text);
                session.setState(BotState.COMPLETE);
                sendMessage(chatId, "Спасибо! Мы приняли заявку и свяжемся с вами.");
                return;
            default:
                break;
        }

        if ("/start".equals(text)) {
            send(startController.buildStartMessage(chatId));
        } else if ("Оставить заявку".equals(text)) {
            session.setState(BotState.ASK_FIO);
            sendMessage(chatId,
                    "Для заявки нам потребуется ваша фамилия имя отчество, место проживания и номер телефона. \nНажмите далее чтобы продолжить.");
            sendMessage(chatId, "Введите ваше ФИО в формате Фамилия Имя Отчество:");
        } else if ("Подробнее об оборудовании".equals(text)) {
            sendInfo(chatId, 0);
        }
    }

    private void handleCallback(Update update) {
        String data = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        if (data.startsWith("info:")) {
            int index = Integer.parseInt(data.substring(5));
            sendInfo(chatId, index);
        } else if ("apply".equals(data)) {
            UserSession session = sessions.computeIfAbsent(chatId, id -> new UserSession());
            session.setState(BotState.ASK_FIO);
            sendMessage(chatId, "Для заявки нам потребуется ваша фамилия имя отчество, место проживания и номер телефона.\nВведите ваше ФИО в формате Фамилия Имя Отчество:");
        }
    }


    private void sendInfo(long chatId, int index) {
        String[] photos = {"classpath:static/photo1.jpg", "classpath:static/photo2.jpg"};
        String[] texts = {"Информация об оборудовании 1", "Информация об оборудовании 2"};
        int next = (index + 1) % photos.length;
        int prev = (index - 1 + photos.length) % photos.length;

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton back = InlineKeyboardButton.builder()
                .text("<-")
                .callbackData("info:" + prev)
                .build();
        InlineKeyboardButton forward = InlineKeyboardButton.builder()
                .text("->")
                .callbackData("info:" + next)
                .build();
        InlineKeyboardButton apply = InlineKeyboardButton.builder()
                .text("Оставить заявку")
                .callbackData("apply")
                .build();
        java.util.List<InlineKeyboardButton> row = java.util.Arrays.asList(back, apply, forward);
        markup.setKeyboard(java.util.Collections.singletonList(row));

        SendPhoto photo = new SendPhoto();
        photo.setChatId(String.valueOf(chatId));
        photo.setPhoto(new InputFile(photos[index]));
        photo.setCaption(texts[index]);
        photo.setReplyMarkup(markup);
        send(photo);
    }

    private void sendMessage(long chatId, String text) {
        SendMessage msg = new SendMessage(String.valueOf(chatId), text);
        send(msg);
    }

    private void send(SendMessage msg) {
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void send(SendPhoto photo) {
        try {
            execute(photo);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
