package com.gaz.demo.bot.services;

import com.gaz.demo.bot.AutoGasBot;
import com.gaz.demo.bot.configs.BotConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.tools.ForwardingFileObject;
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminNotificationService {
    private final BotConfig config;
    public void sendApplicationToAdmin(AutoGasBot bot, Message userMessage) {
        ForwardMessage forwardMessage = new ForwardMessage();
        forwardMessage.setChatId(config.getAdminId().toString());
        forwardMessage.setFromChatId(userMessage.getChatId().toString());
        forwardMessage.setMessageId(userMessage.getMessageId());
        try {
            bot.execute(forwardMessage);
            log.info("Заявка успешно отправлена админу: {}", config.getAdminId());
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке заявки админу: {}", e.getMessage(), e);
        }
    }
}
