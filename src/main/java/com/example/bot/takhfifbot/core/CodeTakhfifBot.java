package com.example.bot.takhfifbot.core;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * is created by aMIN on 1/25/2018 at 11:52 PM
 */
@Component
public class CodeTakhfifBot extends TelegramLongPollingBot {

    public void onUpdateReceived(Update update) {

        System.out.println(new Gson().toJson(update));

        final Responses responses = new Responses(update);
        responses
                .start()
                .reyhoon()
                .changal()
        ;

    }


    public String getBotUsername() {
        return null;
    }

    public String getBotToken() {
        return "700687388:AAE6e-dHraCo8G0AmdRAAaNUJXb1zHu1xes";
    }


    private class Responses {
        Update update;

        public Responses(Update update) {
            this.update = update;
        }

        private Responses start() {
            if (update.hasMessage())
                if (update.getMessage().getText().equals("/start"))
                    sendApiMethodAsync(new SendMessage()
                                    .setChatId(update.getMessage().getChatId())
                                    .setText("god is great")
                                    .setReplyMarkup(tryAgain())


                            , new SentCallback<Message>() {
                                @Override
                                public void onResult(BotApiMethod<Message> method, Message response) {

                                }

                                @Override
                                public void onError(BotApiMethod<Message> method, TelegramApiRequestException apiException) {

                                }

                                @Override
                                public void onException(BotApiMethod<Message> method, Exception exception) {

                                }
                            });

            return this;
        }

        private Responses reyhoon() {
            if (update.hasCallbackQuery())
                if (update.getCallbackQuery().getData().equals("reyhoon")) {

                    SendPhoto sendPhoto = new SendPhoto().setCaption(("کد زیر رو با اولین سفارشتون از ریحون استفاده کنید و ۵۰۰۰ تومان تخفیف بگیرید.\uD83D\uDC47\uD83D\uDC47\uD83D\uDC47\uD83D\uDC47"))
                            .setChatId(String.valueOf(update.getCallbackQuery().getFrom().getId()))
                            .setPhoto("https://lh3.googleusercontent.com/Kc7ykia6xFgCs_ZRxkIABx_v8KBljutY5qbvsTQMfuKamAVI67DEYCztR1s7-lCqzlGShShtH5gE9WIAhUPKP_I=h200");
                    try {
                        execute(sendPhoto);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                    sendApiMethodAsync(new SendMessage()
                                    .setChatId(update.getCallbackQuery().getMessage().getChatId())
                                    .setText("REF41846P6WD"), new SentCallback<Message>() {
                                @Override
                                public void onResult(BotApiMethod<Message> method, Message response) {

                                }

                                @Override
                                public void onError(BotApiMethod<Message> method, TelegramApiRequestException apiException) {

                                }

                                @Override
                                public void onException(BotApiMethod<Message> method, Exception exception) {

                                }
                            }
                    );
                }


            return this;
        }


        public Responses changal() {
            if (update.hasCallbackQuery())
                if (update.getCallbackQuery().getData().equals("changal")) {
                    SendPhoto sendPhoto = new SendPhoto().setCaption(("میتوانید با کد تخفیفی که در زیر آمده برای اولین بار از چنگال غذا سفارش دهید و 50% تخفیف دریافت کنید\uD83D\uDC47\uD83D\uDC47\uD83D\uDC47\uD83D\uDC47"))
                            .setChatId(String.valueOf(update.getCallbackQuery().getFrom().getId()))
                            .setPhoto("https://seedroid.com/img/post/covers/1024/com.changal.delivery.jpg");
                    try {
                        execute(sendPhoto);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                    sendApiMethodAsync(new SendMessage()
                                    .setChatId(update.getCallbackQuery().getMessage().getChatId())
                                    .setText("REFBBD2"), new SentCallback<Message>() {
                                @Override
                                public void onResult(BotApiMethod<Message> method, Message response) {

                                }

                                @Override
                                public void onError(BotApiMethod<Message> method, TelegramApiRequestException apiException) {

                                }

                                @Override
                                public void onException(BotApiMethod<Message> method, Exception exception) {

                                }
                            }
                    );
                }

            return this;

        }
    }


    public static InlineKeyboardMarkup tryAgain() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        String[] items = new String[]{"کد تخفیف ریحون", "کد تخفیف چنگال", "گد تخفیف چیلیوری", "کد تخفیف اسنپ فود", "کد تخفیف دلینو"};
        String[] hostNamesItems = new String[]{"reyhoon", "changal", "reyhoon3", "reyhoon4", "reyhoon5",};
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            List<InlineKeyboardButton> list = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton(items[i])
                    .setCallbackData(hostNamesItems[i]);
            list.add(button);
            lists.add(list);
        }
        markup.setKeyboard(lists);


        return markup;
    }

}
