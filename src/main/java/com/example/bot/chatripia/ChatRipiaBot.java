package com.example.bot.chatripia;

import com.example.Meths;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideoNote;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**
 * created By aMIN on 3/23/2019 4:37 PM
 */
@Component
public class ChatRipiaBot extends TelegramLongPollingBot {

    public void sendMsg(String msg) {
        try {
            execute(new SendMessage(145464749L, msg));
        } catch (TelegramApiException e) {
        }


    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();
            Message message = update.getMessage();
            User from = message.getFrom();
            if (message.getText().equals("del")) {
                try {
                    connection.createStatement()
                            .execute("drop table if exists chatuser");
                    sendMsg("del successfully");
                } catch (SQLException e) {
                    sendMsg(e.toString());
                }
            }
            if (message.getText().contains("users")) {
                try {
                    ResultSet resultSet2 = connection.createStatement().executeQuery("select * from chatuser;");
                    while (resultSet2.next()) {
                        execute(new SendMessage(update.getMessage().getChatId(),
                                String.valueOf(resultSet2.getInt(1))));
                    }
                } catch (SQLException e) {
                    sendMsg(e.toString());
                } catch (TelegramApiException e) {
                    sendMsg(e.toString());

                }
            }


            addUser(chatId.intValue());
            ResultSet resultSet2 = null;
            try {
                resultSet2 = connection.createStatement().executeQuery("select * from chatuser;");
                while (resultSet2.next()) {
                    String idofsend = String.valueOf(resultSet2.getInt(1));
                    execute(new SendMessage(idofsend
                            , message.getText()));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (TelegramApiException e) {
                try {
                    execute(new SendMessage(chatId
                            , message.getText()));
                } catch (TelegramApiException e1) {
                    e1.printStackTrace();
                }
            }

            try {
                execute(new SendMessage(chatId
                        , message.getText()));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }


    @Autowired
    private Connection connection;

    @Bean
    public void dbs() {
        try {
            connection.createStatement().execute("create table if not exists chatuser(id serial primary key )");

        } catch (SQLException e) {
            sendMsg(e.toString());
        } catch (Exception e) {
            sendMsg(e.toString());
        }

    }



    private boolean addUser(int idj) {
    dbs();
        String sql = "INSERT INTO chatuser (id)" +
                "    SELECT ?" +
                "WHERE NOT EXISTS (" +
                "    SELECT id FROM Muser WHERE id=? " +
                ");";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idj);
            preparedStatement.setInt(2, idj);
            boolean execute = preparedStatement.execute();
            sendMsg("added..............");
            return execute;

        } catch (SQLException e) {
            sendMsg("in adding user "+e.toString());
        }
        return (true);

    }


    @Override
    public String getBotUsername() {
        return null;
    }

    @Override
    public String getBotToken() {
        return "858288629:AAEpT32pWgpkTHuWlwZ-42ugnCWlXoBmKNM";
    }
}
