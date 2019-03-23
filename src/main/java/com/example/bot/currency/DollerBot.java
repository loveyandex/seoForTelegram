package com.example.bot.currency;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Component
public class DollerBot extends TelegramLongPollingBot  {
    static private DollerBot dollerBot = new DollerBot();

    private DollerBot() {
    }

    public static DollerBot getDollerBot() {
        return dollerBot;
    }


    @Override
    public void onClosing() {

    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage())
            {

                URL oracle = null;
                try {
                    oracle = new URL("https://music96.ir/wp-content/uploads/2019/03/Macan-Band-Ki-Boodi-To1-85x85.jpg");

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    SendAudio sendAudio = new SendAudio()
                            .setChatId(update.getMessage().getChatId())
                            .setAudio("https://t.me/musicaminbot/86")
                            .setThumb(
                                    new InputFile().setMedia(oracle.openStream(), "dd"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }




            if (update.hasMessage())
                if (update.getMessage().getText().equalsIgnoreCase("/start"))
                    execute(MainApp.initMessage(update));

            if (update.hasCallbackQuery()) {
                sendApiMethod(new SendChatAction(update.getCallbackQuery().getMessage().getChatId()
                        , "typing"));


                if (update.getCallbackQuery().getData().contains("http://")) {
                    InlineKeyboard.isAddedtgju = false;

                    String url = update.getCallbackQuery().getData();
                    if (!url.contains("www.o-xe.com")) {


                        Process.getProcess().getData(url);
                    SendMessage message = new SendMessage(update.getCallbackQuery().getMessage().getChatId()
                            , "نتایج");
                        message.setReplyMarkup(InlineKeyboard.setArzesFortgju());
                        sendApiMethod(message);


                    } else {

//                    if (ProcessDataGathering.isOXE()){
                        SendMessage message = new SendMessage(update.getCallbackQuery().getMessage().getChatId()
                                , "این سایت بفنا رفت اون یکی رو انتخاب کنید");
//                        message.setReplyMarkup(InlineKeyboard.setAllAValaibleNamesArzOXE());
                        sendApiMethod(message);

                    }



                }
                // oxe inline keyboard
                else if (Process.getMonetaryTextItmes().contains(update.getCallbackQuery().getData())) {
                    sendApiMethod(new SendMessage(update.getCallbackQuery().getMessage().getChatId(),
                            update.getCallbackQuery().getData())
                            .setReplyMarkup(InlineKeyboard.setdisiredOXE(
                                    update.getCallbackQuery().getData(),
                                    Process.getMonetaryTextItmes().indexOf(update.getCallbackQuery().getData())
                            ))

                    );
                } else if (update.getCallbackQuery().getData().equalsIgnoreCase(InlineKeyboard.TGJU_MORE)) {
                    SendMessage message = new SendMessage(update.getCallbackQuery().getMessage().getChatId()
                            , "نتایج");
                    message.setReplyMarkup(InlineKeyboard.setMoretgju());
                    sendApiMethod(message);
                }else if (update.getCallbackQuery().getData().equalsIgnoreCase(InlineKeyboard.TGJU_MORE_MORE)) {
                    SendMessage message = new SendMessage(update.getCallbackQuery().getMessage().getChatId()
                            , "نتایج");
                    message.setReplyMarkup(InlineKeyboard.setMoreMoretgju());
                    sendApiMethod(message);
                }

            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void OnStartKeyword() {
    }


    @Override
    public String getBotUsername() {
        return "dollarEurobot";
    }

    @Override
    public String getBotToken() {
        return "542132110:AAEt9bJgC4eYxXKdXYRusdu2hZKvXh5ckQM";
    }


}
