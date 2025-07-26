package com.gaz.demo.bot.exceptions;

public record ApiError(String telegramError, String message) {
}
