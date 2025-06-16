package com.gaz.demo.bot.utils;

public class CustomButton {
    private final String text;
    private final String callback;
    private final String url;

    private CustomButton(String text, String callback, String url) {
        this.text = text;
        this.callback = callback;
        this.url = url;
    }
    public static CustomButton callbackButtonWithUrl(String text, String callbackData,String url) {
        return new CustomButton(text, callbackData, url);
    }

    public static CustomButton callbackButton(String text,String callbackData ) {
        return new CustomButton(text, callbackData, null);
    }


    public String getText() {
        return text;
    }

    public String getCallback() {
        return callback;
    }

    public String getUrl() {
        return url;
    }
}
