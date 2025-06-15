package com.gaz.demo.bot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gaz.demo.bot.configs.BotConfig;
import com.gaz.demo.bot.controller.UpdateController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
@Slf4j
@RequiredArgsConstructor
@Component
public class AutoGasBot extends TelegramLongPollingBot {

    private final UpdateController updateController;
    private final BotConfig config;




    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateController.processUpdate(update, this);
//        try {
//            if (update.hasMessage() && update.getMessage().hasText()) {
//                handleMessage(update);
//            } else if (update.hasCallbackQuery()) {
//                handleCallback(update);
//            }
//        } catch (TelegramApiException e) {
//            throw new RuntimeException("Ошибка");
//        }
    }

//
//    private void handleMessage(Update update) throws TelegramApiException {
//        String text = update.getMessage().getText();
//        long chatId = update.getMessage().getChatId();
//
//        if ("/start".equals(text)) {
//            sendWelcomeMessage(chatId);
//            sessions.put(chatId, BotState.DEFAULT);
//            return;
//        }
//        if ("Оставить заявку".equals(text)) {
//            promptForApplication(chatId);
//            System.out.println("Id пользователя" + update.getMessage().getUsersShared() + "---" + chatId);
//            sendMessage(608098422, "Привет костя");
//        }
//    }
//
//    private void sendWelcomeMessage(Long chatId) throws TelegramApiException {
//        String text = "Привет! 👋\nМы занимаемся установкой автооборудования: ГБО 6 поколения.\nОставь заявку — наш специалист перезвонит и все расскажет.";
//        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder()
//                .keyboardRow(List.of(InlineKeyboardButton.builder()
//                        .text("Оставить заявку").
//                        callbackData("LEAVE_APPLICATION")
//                        .build()))
//                .build();
//        execute(SendMessage.builder().chatId(chatId).text(text).replyMarkup(markup).build());
//    }
//
//    private void sendApplicationTemplate(Long chatId) throws TelegramApiException {
//        String text = """
//                Спасибо, что хотите установить наше оборудование!
//                Для включения в лист ожидания на установку оборудования, пожалуйста, отправьте:
//
//                — Имя
//                — Телефон для связи
//                — Город установки
//                — Марка машины
//                — Когда удобно ответить на наш звонок?
//
//                <b>Для удобства можете воспользоваться шаблоном формы:</b>
//                """;
//
//        String template = """
//                Имя :
//                Телефон для связи :
//                Город установки :
//                Марка машины :
//                Когда удобно ответить на наш звонок :
//                """;
//
//        String example = """
//                Пример заполнения:
//
//                Имя : Петр
//                Телефон для связи : 79871235566
//                Город установки : Чебоксары
//                Марка машины : BMW
//                Когда удобно ответить на наш звонок : В любой день после 15 часов дня
//                """;
//
//        sendText(chatId, text);
//        sendCodeBlock(chatId, template);
//        sendText(chatId, example);
//    }
//
//    private void handleCallback(Update update) throws TelegramApiException {
//        String data = update.getCallbackQuery().getData();
//        long chatId = update.getCallbackQuery().getMessage().getChatId();
//        if (data.equals("LEAVE_APPLICATION")) {
//            sendApplicationTemplate(chatId);
//            sessions.put(chatId, BotState.WAITING_APPLICATION);
//        }
//
//    }
//
//
//    private void sendMessage(long chatId, String text) {
//        SendMessage msg = new SendMessage(String.valueOf(chatId), text);
//        send(msg);
//    }
//
//    private void send(SendMessage msg) {
//        try {
//            execute(msg);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void send(SendPhoto photo) {
//        try {
//            execute(photo);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void promptForApplication(long chatId) throws TelegramApiException {
//        String text = """
//                            Спасибо, что хотите установить наше оборудование!
//                            Для включения в лист ожидания на установку оборудования, пожалуйста, отправьте:
//
//                            — Имя
//                            — Телефон для связи
//                            — Город установки
//                            — Марка машины
//                            — Когда удобно ответить на наш звонок?
//
//                            <b>Для удобства можете воспользоваться шаблоном формы:</b>
//                            <pre>
//                — Имя :
//                — Телефон для связи :
//                — Город установки :
//                — Марка машины :
//                — Когда удобно ответить на наш звонок? :
//                </pre>
//                            """;
//
//        SendMessage msg = SendMessage.builder()
//                .chatId(chatId)
//                .text(text)
//                .parseMode("HTML")   // ключ!
//                .build();
//
//        execute(msg);
//    }
//
//    private void sendText(Long chatId, String text) throws TelegramApiException {
//
//        execute(SendMessage.builder().chatId(chatId).text(text).parseMode("HTML").build());
//    }
//
//    private void sendCodeBlock(Long chatId, String text) throws TelegramApiException {
//        execute(SendMessage.builder().chatId(chatId).text("<pre>" + text + "</pre>").parseMode("HTML").build());
//    }
//
//    private void sendTextWithMarkup(Long chatId, String text, InlineKeyboardMarkup markup) throws TelegramApiException {
//        execute(SendMessage.builder().chatId(chatId).text(text).replyMarkup(markup).build());
//    }
//
//    private void handleApplicationMessage(Long chatId, Message message) throws TelegramApiException {
//        // Пересылаем админу
//        execute(new ForwardMessage(ADMIN_CHAT_ID.toString(), chatId.toString(), message.getMessageId()));
//
//        // Ответ пользователю
//        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder()
//                .keyboardRow(List.of(
//                        InlineKeyboardButton.builder().text("Задать вопрос").callbackData("ASK_QUESTION").build(),
//                        InlineKeyboardButton.builder().text("Информация об оборудовании").callbackData("INFO_EQUIPMENT").build()
//                ))
//                .build();
//
//        sendTextWithMarkup(chatId, "Спасибо, что отправили заявку, мы обязательно вам позвоним!", markup);
//
//        sessions.put(chatId, BotState.DEFAULT);
//    }
}
