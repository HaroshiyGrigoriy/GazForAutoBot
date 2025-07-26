package com.gaz.demo.bot.controller;

import com.gaz.demo.bot.AutoGasBot;
import com.gaz.demo.bot.BotState;
import com.gaz.demo.bot.services.ApplicationService;
import com.gaz.demo.bot.services.UserStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@RequiredArgsConstructor
@Component
public class UpdateController {

    private final ApplicationService applicationService;
    private final UserStateService userStateService;

    public void processUpdate(Update update, AutoGasBot bot) {

        if (update.hasMessage()) {
            log.info("Пользователь пишет сообщение");
            Message userMessage = update.getMessage();
            Long chatId = update.getMessage().getChatId();
            BotState state = userStateService.getUserStates(chatId);
            if (userMessage.getText() != null && userMessage.getText().equals("/start")) {
                log.info("Вам пишет пользователь - " + userMessage.getFrom().getFirstName());
                applicationService.sendWelcomeMessage(bot, chatId);
                userStateService.setUserState(chatId, BotState.DEFAULT);
                log.info(String.valueOf(userStateService.getUserStates(chatId)));
                return;
            }
            if (state == BotState.WAITING_APPLICATION) {
                applicationService.processApplication(bot, userMessage);
            }
        }
        if (update.hasCallbackQuery()) {
            applicationService.processCallback(bot, update.getCallbackQuery());
        }
    }
}
