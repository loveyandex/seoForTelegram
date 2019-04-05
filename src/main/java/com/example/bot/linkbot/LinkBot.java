package com.example.bot.linkbot;//package com.intellij.parisa.linkbot;

import com.example.Meths;
import com.example.bot.linkbot.model.Gune;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.Meths.post;

/**
 * created By aMIN on 4/4/2019 12:27 PM
 */
@Component
public class LinkBot extends TelegramLongPollingBot {


    public LinkBot() {
        Meths.sendToBot("constructor is working .................. ");

    }

    @Autowired
    private Connection connection;

    @Bean
    public void setDbs() {
        try {
            connection.createStatement().execute("create table if not exists Muser(id serial primary key)");

            connection.createStatement().execute("delete  from Muser  where id=878712");
            connection.createStatement().execute("insert into Muser (id) values (878712);");
            connection.createStatement().execute("INSERT INTO Muser (id)" +
                    "    SELECT '6363636'" +
                    "WHERE NOT EXISTS (" +
                    "    SELECT id FROM Muser WHERE id='6363636' " +
                    ");");

            ResultSet resultSet = connection.createStatement().executeQuery("select * from Muser;");
            while (resultSet.next()) {
                Meths.sendToBot(String.valueOf(resultSet.getInt(1)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClosing() {
        super.onClosing();
    }

    public void onReplyKey(@MyAnnotation("df") Update update) {

        Response response = new Response(update);
        System.out.println(LocalTime.now().toString());
        Gune[] values = Gune.values();
        List<String> list = new ArrayList<>();

        for (int j = 0; j <= values.length / 3; j++) {
            for (int i = 0; i < 3 && (3 * (j) + i) < values.length; i++) {
                String name = values[3 * (j) + i].name;
                list.add(name);
            }
        }
        String text1 = update.getMessage().getText();


        if (list.contains(text1)) {
            try {
                int indexOf = list.indexOf(text1);
                String name = "gune" + indexOf;
                System.out.println(name);
                response.getClass().getMethod(name).invoke(response, null);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        try {
            response.startMsg();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        onReplyKey(update);

    }

    @Override
    public String getBotUsername() {
        return null;
    }

    @Override
    public String getBotToken() {
        return "810311785:AAFrHm_StwMR6A52NeUpxNRxsIra5De1cKo";
    }


    public class Response {
        Update update;

        public Response(Update update) {
            this.update = update;
        }

        public Response startMsg() throws TelegramApiException {
            if (update.hasMessage() && update.getMessage().hasText()) {
                if (update.getMessage().getText().equals("/start")) {
                    SendMessage method = new SendMessage(update.getMessage().getChatId()
                            , "می تونی موضوعی رو که میخوای تایپ کنی برامون یا از تنوع ما استفاده کنین");
                    method.setReplyMarkup(start());
                    execute(method);
                }
            }


            return this;
        }


        public Response gune0() throws TelegramApiException {
            execute(new SendMessage(update.getMessage().getChatId()
                    , update.getMessage().getText()));


            return this;
        }

        public Response gune1() throws TelegramApiException {
            execute(new SendMessage(update.getMessage().getChatId()
                    , update.getMessage().getText()));


            return this;
        }


    }


    public static InlineKeyboardMarkup tryAgain() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        Gune[] values = Gune.values();
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();

        for (int j = 0; j <= values.length / 3; j++) {
            List<InlineKeyboardButton> list = new ArrayList<>();
            for (int i = 0; i < 3 && (3 * (j) + i) < values.length; i++) {
                String name = values[3 * (j) + i].name;
                InlineKeyboardButton button = new InlineKeyboardButton(name)
                        .setCallbackData(name);
                list.add(button);
            }
            lists.add(list);
        }
        markup.setKeyboard(lists);
        return markup;
    }


    ReplyKeyboardMarkup start() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();

        Gune[] values = Gune.values();


        int k = 4;
        for (int j = 0; j <= values.length / k; j++) {
            KeyboardRow keyboardRow = new KeyboardRow();

            for (int i = 0; i < k && (k * (j) + i) < values.length; i++) {
                String name = values[k * (j) + i].name;
                KeyboardButton button = new KeyboardButton(name);
                keyboardRow.add(button);
            }

            keyboardRows.add(keyboardRow);


        }


        replyKeyboardMarkup.setKeyboard(keyboardRows);

        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }


}
