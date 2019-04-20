package com.example.bot.testbot;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


/**
 * created By aMIN on 3/23/2019 4:37 PM
 */
@Component
public class BitmElBotTes extends TelegramLongPollingBot {


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();


            Message message = update.getMessage();
            if (message.hasText()) {
                String text = message.getText();
            }
            if (message.hasPhoto()) {
                List<PhotoSize> photo = message.getPhoto();
                try {
                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setChatId(chatId)
                            .setPhoto(photo.get(0).getFileId())
                            .setCaption(message.getCaption())
                    ;
                    execute(sendPhoto);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            if (message.hasDocument()) {
                Document document = message.getDocument();

            }
            if (message.hasVideo()) {
                Video video = message.getVideo();

            }
            if (message.hasVideoNote())
            {
                VideoNote videoNote = message.getVideoNote();
                SendVideoNote sendVideoNote = new SendVideoNote();
                sendVideoNote.setChatId(chatId)
                        .setVideoNote(videoNote.getFileId());
                try {
                    execute(sendVideoNote);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

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
