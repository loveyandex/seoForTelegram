package com.example.bot.currency;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MainApp {

    static public SendMessage initMessage(Update update) {
        Long chatId = update.getMessage().getChatId();
        boolean isStart = update.getMessage().getText().equalsIgnoreCase("/start");
        return new SendMessage(chatId, "کدوم منبع اطلاعاتی رو انتخاب میکنی")
                .setReplyMarkup(InlineKeyboard.setWebsites());
    }
}
