package com.example.once;


import com.example.MuzicBot;
import com.example.database.QDB;
import com.example.pojos.Cons;
import com.example.pojos.MusicForSave;
import lombok.NonNull;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.MuzicBot.urlToCorrectUrl;


/**
 * is created by aMIN on 12/23/2018 at 12:50 AM
 */
public class TOChannel {
    @NonNull
    private String noti;

    public static void main(String[] args) {
        try {
            TOChannel.toChannel(MuzicBot.getMuzicBot());
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public static void toChannel(DefaultAbsSender sender) throws SQLException {
        final ResultSet resultSet = QDB.getInstance().selectAllFromMusic2("1=1");
        while (resultSet.next()) {

            try {
                final String get1 = resultSet.getString(1);
                final String get2 = resultSet.getString(2);
                final String get3 = resultSet.getString(3);
                final String get4 = resultSet.getString(4);
                final String get5 = resultSet.getString(5);
                final String get6 = resultSet.getString(6);
                final String get7 = resultSet.getString(7);
                final String get8 = resultSet.getString(8);
                final boolean get9 = resultSet.getBoolean(9);


                final String audioUrl = urlToCorrectUrl(get2);
                if (!get9) {

                    System.out.println(audioUrl);
                    final SendAudio sung = new SendAudio()
                            .setAudio(audioUrl)
                            .setChatId(Cons.CHANNELID);

                    try {
                        final Message message = sender.execute(sung);

                        final String fileId = message.getAudio().getFileId();
//                    sender.sendAudio(new SendAudio().setAudio(fileId).setChatId(update.getMessage().getChatId()));

                        QDB.getInstance().insertTOmusic3Table(fileId, fileId, get1, get2, get4, get5, get6, get7);
                        System.out.println(fileId);
                        Thread.sleep(200);
                    } catch (TelegramApiException e) {
                        System.err.println("D:"+audioUrl);
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else
                    continue;

            } catch (SQLException e) {
                e.printStackTrace();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void toChannelOne(MusicForSave musicForSave, Update update, DefaultAbsSender sender) throws SQLException, MalformedURLException {
//
//        final String url = urlToCorrectUrl(musicForSave.getSrc_url());
//
//        System.out.println(url);
//        final SendAudio sung = new SendAudio()
//                .setAudio(url)
//                .setChatId(Cons.CHANNELID);
//
//        try {
//            final Message message = sender.execute(sung);
//            final String fileId = message.getAudio().getFileId();
//                    sender.execute(new SendAudio().setAudio(fileId)
//                            .setCaption(musicForSave.getTags())
//                            .setChatId(update.getMessage().getChatId()));
//
//            QDB.getInstance().insertTOmusic3Table( fileId,fileId,musicForSave.getNameOfSong(), url, musicForSave.getNameOfSinger(), "", musicForSave.getPersianNameOfSong(),
//                    musicForSave.getPersianNameOFSinger());
//            System.out.println(fileId);
//         }catch (TelegramApiException e) {
//            e.printStackTrace();}
    }




}
