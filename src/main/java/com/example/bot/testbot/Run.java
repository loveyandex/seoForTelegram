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
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * created By aMIN on 3/23/2019 4:37 PM
 */

public class Run extends TelegramLongPollingBot {

    public static void main(String[] args) {


        ApiContextInitializer.init();

        TelegramBotsApi api = new TelegramBotsApi();
        try {
            api.registerBot(new Run());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onUpdateReceived(Update update) {

        if (true) {

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
                        .setTitle("vaghti ke ....")
                        .setReplyToMessageId(message.getMessageId());

                execute(sendAudio);

            } catch (TelegramApiException e) {
                e.printStackTrace();
            }


        } else {


            URL oracle = null;
            String spec = "https://music96.ir/wp-content/uploads/2019/03/Shadmehr-Aghili-Vaghti-Ke-Bad-Misham1-85x85.jpg";
            try {
                oracle = new URL(spec);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
//            URL url = new URL(spec);
//
//            BufferedImage img = ImageIO.read(url);
                String pathname = "Macan-Band-Ki-Boodi-To1-85x85.jpg";
                File file = new File(pathname);
                System.out.println(file.exists());
//            ImageIO.write(img, "jpg", file);


                InputFile thumb = new InputFile(oracle.openStream(), pathname);
                SendAudio sendAudio = new SendAudio()
                        .setChatId(update.getMessage().getChatId())
                        .setAudio("https://t.me/musicaminbot/86")
                        .setAudio("CQADBAADBwYAAuhGYVDYG9SW_FYZtQI")
                        .setAudio("https://t.me/awherestoragesongs/213")
                        .setAudio("https://t.me/bitmel/7");

                execute(sendAudio);


            } catch (TelegramApiException e) {
                e.printStackTrace();
            } catch (IOException e) {
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
        return "724200669:AAEy4HThf-kjmE6lhBlOJ0v21RZbMkJJwnI";
    }
}
