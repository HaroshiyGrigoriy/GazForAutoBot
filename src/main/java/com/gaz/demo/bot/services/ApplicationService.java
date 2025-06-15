package com.gaz.demo.bot.services;

import com.gaz.demo.bot.AutoGasBot;
import com.gaz.demo.bot.BotState;
import com.gaz.demo.bot.configs.BotConfig;
import com.gaz.demo.bot.utils.BotUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final AdminNotificationService service;
    private final UserStateService stateService;


    public void sendWelcomeMessage(AutoGasBot bot, Long chatId) {
        String welcomeMessage = "Привет! \uD83D\uDC4B \n" + "Мы занимаемся установкой автооборудования: ГБО 6 поколения. " + "\u2028Оставь заявку — наш специалист перезвонит и все расскажет. ";

        BotUtils.sendMessageWithButtons(bot, chatId, welcomeMessage,
                BotUtils.createKeyboardMarkup(
                        List.of("Оставить заявку"),
                        List.of("LEAVE_APPLICATION"), 2));

    }

    public void processCallback(AutoGasBot bot, CallbackQuery callback) {
        Long chatId = callback.getMessage().getChatId();
        if (callback.getData().equals("LEAVE_APPLICATION")) {
            log.info("Блок кнопки нажат");
            BotUtils.sendMessage(bot, chatId, BotUtils.instruction());
            BotUtils.sendMessage(bot, chatId, BotUtils.pattern());
            stateService.setUserState(chatId, BotState.WAITING_APPLICATION);
            log.info("Пользователь на этапе составления заявки");
        }
        if (callback.getData().equals("CONTACTS")) {
            log.info("Пользователь попросил контакты");
            BotUtils.sendMessage(bot,chatId, """
                    тг-канал @generationdrive
                    Заказать установку — +79373994000 @marsell87
                    """);
        }
        if (callback.getData().equals("INFO_EQUIPMENT")) {
            InputFile info = new InputFile(new File("src/main/resources/pdf/gbo6.pdf"));
            SendDocument document = SendDocument.builder().
                    chatId(chatId.toString())
                    .document(info)
                    .caption("О нас")
                    .build();
            try {
                bot.execute(document);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void processApplication(AutoGasBot bot, Message message) {
        Long chatId = message.getChatId();
        service.sendApplicationToAdmin(bot, message);
        stateService.setUserState(chatId, BotState.APPLICATION_SENT);
        BotUtils.sendMessageWithButtons(bot, chatId, BotUtils.responseToTheUserAfterTheRequest(),
                BotUtils.createKeyboardMarkup(
                        List.of("Задать вопрос", "Информация об оборудовании", "Связаться с нами"),
                        List.of("ASK_QUESTION", "INFO_EQUIPMENT", "CONTACTS"), 2));

    }


}
