package com.gaz.demo.bot.services;

import com.gaz.demo.bot.AutoGasBot;
import com.gaz.demo.bot.configs.BotConfig;
import com.gaz.demo.bot.exceptions.TelegramSendException;
import com.gaz.demo.bot.models.dto.ApplicationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebApplicationServices {

    private final AutoGasBot bot;
    private final BotConfig config;
    private static int count;

    public void sendToAdmin(ApplicationDto dto) {
        count++;
        String sendForAdmin = String.format("""
                           📩 Заявка с сайта: %s
                           
                           👤 Имя: %s
                           🏙️ Город: %s
                           📞 Телефон: %s
                           💬 Комментарий: %s \s
                        """,
                count, dto.getName(), dto.getCity(), dto.getPhone(), dto.getComment());
        SendMessage msg = new SendMessage();
        msg.setChatId(config.getAdminId().toString());
        msg.setText(sendForAdmin);
        try {
            bot.execute(msg);
        } catch (TelegramApiException e) {
            throw new TelegramSendException("Ошибка при отправке в Telegram", e);
        }
    }
}
