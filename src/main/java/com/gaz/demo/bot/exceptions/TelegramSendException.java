package com.gaz.demo.bot.exceptions;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramSendException extends RuntimeException {

    public TelegramSendException(String message, TelegramApiException e) {
        super(message);
    }
}
