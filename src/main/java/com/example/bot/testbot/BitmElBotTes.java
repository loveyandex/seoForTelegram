package com.example.bot.testbot;

import com.google.gson.Gson;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * created By aMIN on 3/23/2019 4:37 PM
 */

public class BitmElBotTes extends TelegramLongPollingBot {

    public static void main(String[] args) {
        
        ApiContextInitializer.init();
        TelegramBotsApi api = new TelegramBotsApi();
        try {
            api.registerBot(new BitmElBotTes());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onUpdateReceived(Update update) {

        if (false) {

            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId("@bitmel")
                    .setPhoto("https://music96.ir/wp-content/uploads/2019/03/Shadmehr-Aghili-Vaghti-Ke-Bad-Misham1-1000x1000.jpg ")
                    .setCaption("#shadmehr\n" +
                            "https://t.me/bitmelbot?start=Dshadmehr")
            ;

            try {
                Message message = execute(sendPhoto);
                System.err.println(new Gson().toJson(message));
                SendAudio sendAudio = new SendAudio()
                        .setChatId("@bitmel")
                        .setAudio("https://t.me/musicaminbot/86")
                        .setAudio("CQADBAADBwYAAuhGYVDYG9SW_FYZtQI")
                        .setAudio("https://t.me/awherestoragesongs/213")
                        .setAudio("https://t.me/bitmel/7")
                        .setReplyToMessageId(message.getMessageId());

                execute(sendAudio);

            } catch (TelegramApiException e) {
                e.printStackTrace();
            }


        } else {

            try {
                execute(new SendMessage(update.getMessage().getChatId(), update.getMessage().getText()));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public String getBotUsername() {
        return null;
    }

    @Override
    public String getBotToken() {
        return "858001251:AAGIgPCmRaHjZlFsdxIH7IMCcNH67RTYT_Y";
    }
}
