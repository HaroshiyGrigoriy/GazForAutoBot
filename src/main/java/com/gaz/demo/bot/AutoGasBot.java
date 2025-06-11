package com.gaz.demo.bot;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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


    private final StartController startController;
    private final Map<Long, UserSession> sessions = new HashMap<>();

    @Autowired
    public AutoGasBot( StartController startController) {

        this.startController = startController;
    }

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;
    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
                handleMessage(update);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else if (update.hasCallbackQuery()) {
            handleCallback(update);
        }
    }

    private void handleMessage(Update update) throws TelegramApiException {
        String text = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        UserSession session = sessions.computeIfAbsent(chatId, id -> new UserSession());

        if ("/start".equals(text)) {
            send(startController.buildStartMessage(chatId));
        } else if ("Оставить заявку".equals(text)) {
            promptForApplication(chatId);
            session.setState(BotState.WAITING_APPLICATION);
        }
    }

    private void handleCallback(Update update) {
        String data = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();

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

    private void promptForApplication(long chatId) throws TelegramApiException {
        String text = """
            Спасибо, что хотите установить наше оборудование!
            Для включения в лист ожидания на установку оборудования, пожалуйста, отправьте:

            — Имя  
            — Телефон для связи  
            — Город установки  
            — Марка машины  
            — Когда удобно ответить на наш звонок?  

            <b>Для удобства можете воспользоваться шаблоном формы:</b>
            <pre>
— Имя :
— Телефон для связи :
— Город установки :
— Марка машины :
— Когда удобно ответить на наш звонок? :
</pre>
            """;

        SendMessage msg = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode("HTML")   // ключ!
                .build();

        execute(msg);
    }
}
